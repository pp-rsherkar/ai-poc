#!/usr/bin/env python3

import argparse
import json
from pathlib import Path
import re

from jsonschema import Draft202012Validator


REPOSITORY_ROOT = Path(__file__).resolve().parents[4]

INVESTIGATIVE_OUTCOME_PATTERNS = (
    re.compile(r"^\s*(?:observe|investigate)\b", re.IGNORECASE),
    re.compile(
        r"^\s*(?:document|record|note)\s+(?:whether|if|the\s+"
        r"(?:observed|actual|result|behaviou?r|outcome))\b",
        re.IGNORECASE,
    ),
    re.compile(
        r"\b(?:document|observe|investigate|determine|assess|record|note|"
        r"confirm|verify|check|see|establish)\s+(?:whether|if)\b",
        re.IGNORECASE,
    ),
    re.compile(r"\b(?:log|raise|create)\s+(?:a\s+)?gap\b", re.IGNORECASE),
)
VAGUE_FIXTURE_PATTERNS = (
    re.compile(r"\bconfigured\s+to\s+reproduce\b", re.IGNORECASE),
    re.compile(r"\bconfigured\s+with\s+known\b", re.IGNORECASE),
)
CONCRETE_FIXTURE_PATTERNS = (
    re.compile(r"\b[A-Za-z][A-Za-z0-9 _-]{1,30}\s*(?:=|:)\s*\S+"),
    re.compile(r"(?<!-)\b\d+(?:\.\d+)?\b"),
    re.compile(
        r"\b(?:active|inactive|enabled|disabled|zero|none|no\s+avails?)\b",
        re.IGNORECASE,
    ),
)
MISSING_FACT_PATTERN = re.compile(
    r"\b(?:missing|unknown|unresolved|requires?|not\s+(?:specified|documented|known))\b",
    re.IGNORECASE,
)


def contains_investigative_outcome(value):
    return any(pattern.search(value) for pattern in INVESTIGATIVE_OUTCOME_PATTERNS)


def contains_vague_fixture(value):
    has_vague_phrase = any(pattern.search(value) for pattern in VAGUE_FIXTURE_PATTERNS)
    has_concrete_detail = any(
        pattern.search(value) for pattern in CONCRETE_FIXTURE_PATTERNS
    )
    return has_vague_phrase and not has_concrete_detail


def parse_args():
    parser = argparse.ArgumentParser(
        description="Validate Netra analysis schema and test-coverage contracts."
    )
    parser.add_argument("analysis", type=Path)
    parser.add_argument(
        "--schema",
        type=Path,
        default=REPOSITORY_ROOT / ".claude/contracts/analysis-output.schema.json",
    )
    return parser.parse_args()


def validate_schema(schema, analysis):
    errors = sorted(
        Draft202012Validator(schema).iter_errors(analysis),
        key=lambda error: list(error.absolute_path),
    )
    return [
        f"/{'/'.join(str(part) for part in error.absolute_path)} {error.message}"
        for error in errors
    ]


def validate_coverage(analysis):
    errors = []
    for ticket in analysis.get("tickets", []):
        ticket_key = ticket.get("key", "<unknown>")
        requirements = ticket.get("requirements", [])
        coverage = ticket.get("testCoverage", {})
        cases = coverage.get("testCases", [])
        requirement_ids = {requirement["id"] for requirement in requirements}
        covered_types = {requirement_id: set() for requirement_id in requirement_ids}

        case_ids = [case["id"] for case in cases]
        duplicates = sorted(
            case_id for case_id in set(case_ids) if case_ids.count(case_id) > 1
        )
        if duplicates:
            errors.append(
                f"{ticket_key}: duplicate test case IDs: {', '.join(duplicates)}"
            )

        variation_groups = {}
        for case in cases:
            is_blocked = case.get("type") == "blocked" and case.get("status") == "blocked"
            if contains_investigative_outcome(case.get("expectedResult", "")) and not is_blocked:
                errors.append(
                    f"{ticket_key}: {case['id']} has a non-verifiable expectedResult; "
                    "classify it as blocked and identify the missing fact"
                )
            if contains_vague_fixture(case.get("testData", "")) and not is_blocked:
                errors.append(
                    f"{ticket_key}: {case['id']} has non-reproducible testData; "
                    "provide a concrete fixture, state, or value, or classify it as blocked"
                )
            if is_blocked and not MISSING_FACT_PATTERN.search(case.get("comments", "")):
                errors.append(
                    f"{ticket_key}: {case['id']} is blocked but comments do not identify "
                    "the missing fact"
                )
            group = case.get("variationGroup")
            key = case.get("variationKey")
            if bool(group) != bool(key):
                errors.append(
                    f"{ticket_key}: {case['id']} must declare variationGroup and "
                    "variationKey together"
                )
            if group and key:
                variation_groups.setdefault(group, []).append((case["id"], key))

        for group, members in sorted(variation_groups.items()):
            if len(members) < 2:
                errors.append(
                    f"{ticket_key}: variation group {group!r} must contain at least "
                    "two test cases"
                )
            keys = [key for _, key in members]
            duplicate_keys = sorted(
                key for key in set(keys) if keys.count(key) > 1
            )
            if duplicate_keys:
                errors.append(
                    f"{ticket_key}: variation group {group!r} has duplicate "
                    f"variation keys: {', '.join(duplicate_keys)}"
                )

        for case in cases:
            for requirement_id in case.get("requirementIds", []):
                if requirement_id not in requirement_ids:
                    errors.append(
                        f"{ticket_key}: {case['id']} references unknown requirement "
                        f"{requirement_id}"
                    )
                    continue
                covered_types[requirement_id].add(case["type"])

        for requirement_id, case_types in sorted(covered_types.items()):
            missing = {"positive", "negative"} - case_types
            if missing:
                errors.append(
                    f"{ticket_key}: {requirement_id} is missing explicit "
                    f"{' and '.join(sorted(missing))} coverage"
                )

        minimum = coverage.get("minimumTestCases", 0)
        if len(cases) < minimum:
            errors.append(
                f"{ticket_key}: generated {len(cases)} test cases; minimum is {minimum}"
            )

    return errors


def validate_source_manifests(analysis):
    errors = []
    for ticket in analysis.get("tickets", []):
        ticket_key = ticket.get("key", "<unknown>")
        manifest = ticket.get("sourceManifest", [])
        identities = [(entry["type"], entry["id"]) for entry in manifest]
        duplicates = sorted(
            f"{source_type}:{source_id}"
            for source_type, source_id in set(identities)
            if identities.count((source_type, source_id)) > 1
        )
        if duplicates:
            errors.append(
                f"{ticket_key}: duplicate source manifest entries: "
                f"{', '.join(duplicates)}"
            )
        if not any(
            entry["type"] == "ticket"
            and entry["id"] == ticket_key
            and entry["disposition"] == "fetched"
            for entry in manifest
        ):
            errors.append(
                f"{ticket_key}: source manifest must record the primary ticket as fetched"
            )
    return errors


def validate_release_summary(analysis):
    tickets = analysis.get("tickets", [])
    summary = analysis.get("releaseSummary", {})
    if not tickets or not summary:
        return []

    verdict_counts = {
        verdict: sum(
            ticket.get("readiness", {}).get("verdict") == verdict
            for ticket in tickets
        )
        for verdict in ("ready", "warning", "blocked")
    }
    total_test_cases = sum(
        len(ticket.get("testCoverage", {}).get("testCases", []))
        for ticket in tickets
    )
    expected = {
        "totalTickets": len(tickets),
        "ready": verdict_counts["ready"],
        "warnings": verdict_counts["warning"],
        "blocked": verdict_counts["blocked"],
        "totalTestCases": total_test_cases,
        "averageTestCasesPerTicket": round(total_test_cases / len(tickets), 2),
    }
    errors = [
        f"releaseSummary.{field} must be {value!r}, got {summary.get(field)!r}"
        for field, value in expected.items()
        if summary.get(field) != value
    ]
    coverage_counts = {
        ticket["key"]: len(ticket.get("testCoverage", {}).get("testCases", []))
        for ticket in tickets
    }
    highest_count = max(coverage_counts.values())
    highest_keys = {
        key for key, count in coverage_counts.items() if count == highest_count
    }
    if summary.get("highestCoverageTicket") not in highest_keys:
        errors.append(
            "releaseSummary.highestCoverageTicket must identify a ticket with "
            f"{highest_count} test case(s)"
        )
    return errors


def main():
    args = parse_args()
    schema = json.loads(args.schema.read_text(encoding="utf-8"))
    analysis = json.loads(args.analysis.read_text(encoding="utf-8"))

    errors = validate_schema(schema, analysis)
    if not errors:
        errors.extend(validate_source_manifests(analysis))
        errors.extend(validate_coverage(analysis))
        errors.extend(validate_release_summary(analysis))
    if analysis.get("status") != "ready":
        errors.append(f"Netra status is {analysis.get('status')!r}, not 'ready'")

    if errors:
        raise SystemExit("Invalid Netra analysis:\n" + "\n".join(errors))

    case_count = sum(
        len(ticket["testCoverage"]["testCases"])
        for ticket in analysis.get("tickets", [])
    )
    print(
        f"VALID: {len(analysis.get('tickets', []))} ticket(s), "
        f"{case_count} test case(s)"
    )


if __name__ == "__main__":
    main()
