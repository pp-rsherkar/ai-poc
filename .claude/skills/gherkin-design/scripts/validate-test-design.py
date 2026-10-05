#!/usr/bin/env python3
"""Validate Sutra's schema and its traceability to Netra's analysis."""

import json
from pathlib import Path
import re
import sys

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


def contains_investigative_outcome(value: str) -> bool:
    return any(pattern.search(value) for pattern in INVESTIGATIVE_OUTCOME_PATTERNS)


def contains_vague_fixture(value: str) -> bool:
    has_vague_phrase = any(pattern.search(value) for pattern in VAGUE_FIXTURE_PATTERNS)
    has_concrete_detail = any(
        pattern.search(value) for pattern in CONCRETE_FIXTURE_PATTERNS
    )
    return has_vague_phrase and not has_concrete_detail


def load_json(path: Path) -> dict:
    with path.open(encoding="utf-8") as handle:
        return json.load(handle)


def scenario_outline_example_rows(feature_path: Path) -> dict[str, int]:
    """Return the total number of data rows for each Scenario Outline."""
    outlines: dict[str, int] = {}
    current_name = None
    in_examples = False
    table_rows = 0

    def finish_examples():
        nonlocal table_rows, in_examples
        if in_examples and current_name is not None:
            outlines[current_name] += max(table_rows - 1, 0)
        table_rows = 0
        in_examples = False

    for line in feature_path.read_text(encoding="utf-8").splitlines():
        stripped = line.strip()
        outline_match = re.match(r"^Scenario Outline:\s*(.+)$", stripped)
        scenario_boundary = re.match(
            r"^(Scenario|Scenario Outline|Background|Rule|Feature):", stripped
        )
        if outline_match:
            finish_examples()
            current_name = outline_match.group(1).strip()
            outlines[current_name] = 0
            continue
        if scenario_boundary:
            finish_examples()
            current_name = None
            continue
        if current_name is None:
            continue
        if re.match(r"^Examples\s*:", stripped):
            finish_examples()
            in_examples = True
            continue
        if in_examples and stripped.startswith("|") and stripped.endswith("|"):
            table_rows += 1
        elif in_examples and stripped and not stripped.startswith(("#", "@")):
            finish_examples()

    finish_examples()
    return outlines


def normalize_gherkin_text(value: str) -> str:
    return " ".join(value.split())


def existing_scenario_assertions(feature_path: Path) -> dict[str, list[set[str]]]:
    """Index exact Then/And/But assertion lines by existing scenario name."""
    scenarios: dict[str, list[set[str]]] = {}
    current_assertions = None
    primary_keyword = None

    for line in feature_path.read_text(encoding="utf-8").splitlines():
        stripped = line.strip()
        scenario_match = re.match(r"^Scenario(?: Outline| Template)?:\s*(.+)$", stripped)
        if scenario_match:
            name = scenario_match.group(1).strip()
            current_assertions = set()
            scenarios.setdefault(name, []).append(current_assertions)
            primary_keyword = None
            continue
        if re.match(r"^(Feature|Rule|Background):", stripped):
            current_assertions = None
            primary_keyword = None
            continue
        if current_assertions is None:
            continue
        step_match = re.match(r"^(Given|When|Then|And|But)\s+(.+)$", stripped)
        if not step_match:
            continue
        keyword, body = step_match.groups()
        if keyword in {"Given", "When", "Then"}:
            primary_keyword = keyword
        effective_keyword = primary_keyword if keyword in {"And", "But"} else keyword
        if effective_keyword == "Then":
            current_assertions.add(normalize_gherkin_text(stripped))
            current_assertions.add(normalize_gherkin_text(body))

    return scenarios


def generated_scenario_steps(feature_path: Path) -> dict[str, list[list[tuple[str, str]]]]:
    """Index Gherkin steps with inherited And/But keyword semantics."""
    scenarios: dict[str, list[list[tuple[str, str]]]] = {}
    current_steps = None
    primary_keyword = None

    for line in feature_path.read_text(encoding="utf-8").splitlines():
        stripped = line.strip()
        scenario_match = re.match(r"^Scenario(?: Outline| Template)?:\s*(.+)$", stripped)
        if scenario_match:
            name = scenario_match.group(1).strip()
            current_steps = []
            scenarios.setdefault(name, []).append(current_steps)
            primary_keyword = None
            continue
        if re.match(r"^(Feature|Rule|Background):", stripped):
            current_steps = None
            primary_keyword = None
            continue
        if current_steps is None:
            continue
        step_match = re.match(r"^(Given|When|Then|And|But)\s+(.+)$", stripped)
        if not step_match:
            continue
        keyword, body = step_match.groups()
        if keyword in {"Given", "When", "Then"}:
            primary_keyword = keyword
        effective_keyword = primary_keyword if keyword in {"And", "But"} else keyword
        current_steps.append((effective_keyword, body))

    return scenarios


def generated_scenario_declarations(feature_path: Path) -> dict[str, list[dict]]:
    """Index declared kind and immediately preceding source metadata."""
    lines = feature_path.read_text(encoding="utf-8").splitlines()
    declarations: dict[str, list[dict]] = {}
    for index, line in enumerate(lines):
        match = re.match(r"^\s*(Scenario|Scenario Outline):\s*(.+)$", line)
        if not match:
            continue
        keyword, name = match.groups()
        has_todo = index >= 1 and lines[index - 1].strip() == "@todo"
        source_line = lines[index - 2].strip() if index >= 2 and has_todo else ""
        has_source = source_line.startswith("# Source:")
        source_ids = set(source_line.removeprefix("# Source:").split()) if has_source else set()
        declarations.setdefault(name.strip(), []).append(
            {
                "kind": "scenario_outline" if keyword == "Scenario Outline" else "scenario",
                "sourceIds": source_ids,
                "sourceImmediatelyAboveTodo": has_todo and has_source,
            }
        )
    return declarations


def validate(analysis_path: Path, design_path: Path) -> list[str]:
    analysis = load_json(analysis_path)
    design = load_json(design_path)
    errors: list[str] = []

    for payload, schema_name, label in (
        (analysis, "analysis-output.schema.json", "analysis"),
        (design, "test-design-output.schema.json", "test design"),
    ):
        schema = load_json(REPOSITORY_ROOT / ".claude" / "contracts" / schema_name)
        validator = Draft202012Validator(schema)
        for error in sorted(validator.iter_errors(payload), key=lambda item: list(item.path)):
            location = ".".join(str(part) for part in error.path) or "root"
            errors.append(f"{label}.{location}: {error.message}")

    if errors:
        return errors
    if analysis["status"] != "ready":
        errors.append("source analysis status must be ready")

    source = design["sourceAnalysis"]
    declared_analysis_path = Path(source["path"])
    if not declared_analysis_path.is_absolute():
        declared_analysis_path = REPOSITORY_ROOT / declared_analysis_path
    if declared_analysis_path.resolve() != analysis_path.resolve():
        errors.append("sourceAnalysis.path does not match the validated Netra file")
    if source["artifactId"] != analysis["artifactId"]:
        errors.append("sourceAnalysis.artifactId does not match Netra")
    if source["schemaVersion"] != analysis["schemaVersion"]:
        errors.append("sourceAnalysis.schemaVersion does not match Netra")

    tickets = {ticket["key"]: ticket for ticket in analysis["tickets"]}
    test_cases = {
        case["id"]: (ticket["key"], case)
        for ticket in analysis["tickets"]
        for case in ticket["testCoverage"]["testCases"]
    }
    requirement_ids = {
        requirement["id"]
        for ticket in analysis["tickets"]
        for requirement in ticket["requirements"]
    }
    scenario_by_id = {scenario["id"]: scenario for scenario in design["scenarios"]}
    feature_by_path = {feature["artifactPath"]: feature for feature in design["featureFiles"]}
    triage_by_case = {item["testCaseId"]: item for item in design["triage"]}
    duplicate_evidence_cache = {}
    generated_step_cache = {}
    generated_declaration_cache = {}

    triage_ids = [item["testCaseId"] for item in design["triage"]]
    missing = sorted(set(test_cases) - set(triage_ids))
    unknown = sorted(set(triage_ids) - set(test_cases))
    duplicates = sorted({item for item in triage_ids if triage_ids.count(item) > 1})
    if missing:
        errors.append(f"missing triage records: {', '.join(missing)}")
    if unknown:
        errors.append(f"unknown triage test cases: {', '.join(unknown)}")
    if duplicates:
        errors.append(f"duplicate triage records: {', '.join(duplicates)}")

    for item in design["triage"]:
        case_id = item["testCaseId"]
        if case_id not in test_cases:
            continue
        expected_ticket, source_case = test_cases[case_id]
        if item["ticketKey"] != expected_ticket:
            errors.append(f"triage {case_id} has the wrong ticketKey")
        if set(item["requirementIds"]) != set(source_case["requirementIds"]):
            errors.append(f"triage {case_id} changed its requirementIds")
        if item["disposition"] == "authored" and not item["scenarioIds"]:
            errors.append(f"authored triage {case_id} has no scenarioIds")
        source_is_unresolved = (
            source_case.get("type") == "blocked"
            or source_case.get("status") == "blocked"
            or contains_investigative_outcome(source_case.get("expectedResult", ""))
            or contains_vague_fixture(source_case.get("testData", ""))
        )
        if source_is_unresolved and (
            item["disposition"] != "blocked"
            or item["automationCandidate"] != "blocked"
            or item["scenarioIds"]
        ):
            errors.append(
                f"triage {case_id} must remain blocked until its expected outcome and "
                "test fixture are concrete"
            )
        if item["disposition"] == "duplicate":
            evidence = item["coveredBy"]
            feature_path = (REPOSITORY_ROOT / evidence["featurePath"]).resolve()
            feature_root = (REPOSITORY_ROOT / "src/test/resources/features").resolve()
            if not feature_path.is_relative_to(feature_root):
                errors.append(
                    f"duplicate triage {case_id} coveredBy.featurePath escapes the "
                    "repository feature root"
                )
            elif not feature_path.is_file():
                errors.append(
                    f"duplicate triage {case_id} references missing feature "
                    f"{evidence['featurePath']}"
                )
            else:
                if feature_path not in duplicate_evidence_cache:
                    duplicate_evidence_cache[feature_path] = existing_scenario_assertions(
                        feature_path
                    )
                matching_scenarios = duplicate_evidence_cache[feature_path].get(
                    evidence["scenarioName"], []
                )
                if not matching_scenarios:
                    errors.append(
                        f"duplicate triage {case_id} references missing scenario "
                        f"{evidence['scenarioName']!r}"
                    )
                elif len(matching_scenarios) > 1:
                    errors.append(
                        f"duplicate triage {case_id} references ambiguous scenario "
                        f"{evidence['scenarioName']!r}"
                    )
                else:
                    anchor = normalize_gherkin_text(evidence["assertionAnchor"])
                    if anchor not in matching_scenarios[0]:
                        errors.append(
                            f"duplicate triage {case_id} assertionAnchor is absent from "
                            f"scenario {evidence['scenarioName']!r}"
                        )
        for scenario_id in item["scenarioIds"]:
            scenario = scenario_by_id.get(scenario_id)
            if scenario is None:
                errors.append(f"triage {case_id} references unknown scenario {scenario_id}")
            elif case_id not in scenario["testCaseIds"]:
                errors.append(f"scenario {scenario_id} does not map back to {case_id}")

    for scenario_id, scenario in scenario_by_id.items():
        if scenario["ticketKey"] not in tickets:
            errors.append(f"scenario {scenario_id} references unknown ticket")
        unknown_cases = sorted(set(scenario["testCaseIds"]) - set(test_cases))
        unknown_requirements = sorted(set(scenario["requirementIds"]) - requirement_ids)
        if unknown_cases:
            errors.append(f"scenario {scenario_id} has unknown test cases: {', '.join(unknown_cases)}")
        if unknown_requirements:
            errors.append(
                f"scenario {scenario_id} has unknown requirements: {', '.join(unknown_requirements)}"
            )
        feature = feature_by_path.get(scenario["featurePath"])
        if feature is None:
            errors.append(f"scenario {scenario_id} references an undeclared feature file")
        elif scenario_id not in feature["scenarioIds"]:
            errors.append(f"feature file does not map back to scenario {scenario_id}")

        artifact_path = Path(scenario["featurePath"])
        if not artifact_path.is_absolute():
            artifact_path = REPOSITORY_ROOT / artifact_path
        if artifact_path.is_file():
            resolved_path = artifact_path.resolve()
            if resolved_path not in generated_step_cache:
                generated_step_cache[resolved_path] = generated_scenario_steps(artifact_path)
                generated_declaration_cache[resolved_path] = (
                    generated_scenario_declarations(artifact_path)
                )
            matching_scenarios = generated_step_cache[resolved_path].get(
                scenario["name"], []
            )
            declarations = generated_declaration_cache[resolved_path].get(
                scenario["name"], []
            )
            if not declarations:
                errors.append(
                    f"scenario {scenario_id} name does not match a generated scenario"
                )
            elif len(declarations) > 1:
                errors.append(
                    f"scenario {scenario_id} name is ambiguous in its feature file"
                )
            else:
                declaration = declarations[0]
                if declaration["kind"] != scenario["kind"]:
                    errors.append(
                        f"scenario {scenario_id} kind does not match generated Gherkin"
                    )
                if not declaration["sourceImmediatelyAboveTodo"]:
                    errors.append(
                        f"scenario {scenario_id} must place # Source immediately above @todo"
                    )
                authored_case_ids = {
                    case_id
                    for case_id in scenario["testCaseIds"]
                    if triage_by_case.get(case_id, {}).get("disposition") == "authored"
                }
                missing_source_ids = sorted(
                    authored_case_ids - declaration["sourceIds"]
                )
                if missing_source_ids:
                    errors.append(
                        f"scenario {scenario_id} source tag is missing authored cases: "
                        f"{', '.join(missing_source_ids)}"
                    )
                if scenario["kind"] == "scenario_outline":
                    row_count = scenario_outline_example_rows(artifact_path).get(
                        scenario["name"], 0
                    )
                    if row_count < 2:
                        errors.append(
                            f"scenario {scenario_id} outline must contain at least two "
                            "Examples rows"
                        )
            if len(matching_scenarios) == 1:
                for keyword, body in matching_scenarios[0]:
                    if keyword == "Then" and contains_investigative_outcome(body):
                        errors.append(
                            f"scenario {scenario_id} contains a non-verifiable "
                            f"assertion step: {body!r}"
                        )

    variation_groups = {}
    for case_id, (ticket_key, case) in test_cases.items():
        if case.get("variationGroup"):
            identity = (ticket_key, case["variationGroup"])
            variation_groups.setdefault(identity, []).append(case_id)

    outline_rows_by_feature = {}
    for (ticket_key, group), case_ids in sorted(variation_groups.items()):
        triage_items = [triage_by_case.get(case_id) for case_id in case_ids]
        if any(item is None for item in triage_items):
            continue
        dispositions = {item["disposition"] for item in triage_items}
        if dispositions <= {"queued", "blocked", "not_automatable"}:
            continue
        if dispositions != {"authored"}:
            errors.append(
                f"variation group {ticket_key}/{group} must be wholly authored or "
                "wholly deferred"
            )
            continue
        mapped_scenarios = {
            scenario_id
            for item in triage_items
            for scenario_id in item["scenarioIds"]
        }
        if len(mapped_scenarios) != 1:
            errors.append(
                f"variation group {ticket_key}/{group} must map to exactly one scenario"
            )
            continue
        scenario_id = next(iter(mapped_scenarios))
        scenario = scenario_by_id.get(scenario_id)
        if scenario is None:
            continue
        if scenario["kind"] != "scenario_outline":
            errors.append(
                f"variation group {ticket_key}/{group} must map to a scenario_outline"
            )
            continue
        if not set(case_ids).issubset(scenario["testCaseIds"]):
            errors.append(
                f"variation group {ticket_key}/{group} is not fully mapped by {scenario_id}"
            )
        feature_path = Path(scenario["featurePath"])
        if not feature_path.is_absolute():
            feature_path = REPOSITORY_ROOT / feature_path
        if not feature_path.is_file():
            continue
        resolved_path = feature_path.resolve()
        if resolved_path not in outline_rows_by_feature:
            outline_rows_by_feature[resolved_path] = scenario_outline_example_rows(feature_path)
        row_count = outline_rows_by_feature[resolved_path].get(scenario["name"])
        if row_count is None:
            errors.append(
                f"variation group {ticket_key}/{group} cannot find Scenario Outline "
                f"{scenario['name']!r} in {scenario['featurePath']}"
            )
        elif row_count != len(case_ids):
            errors.append(
                f"variation group {ticket_key}/{group} has {len(case_ids)} cases but "
                f"{scenario_id} has {row_count} Examples rows"
            )

    for feature in design["featureFiles"]:
        artifact_path = Path(feature["artifactPath"])
        if not artifact_path.is_absolute():
            artifact_path = REPOSITORY_ROOT / artifact_path
        if artifact_path.suffix != ".feature" or not artifact_path.is_file():
            errors.append(f"feature artifact does not exist: {feature['artifactPath']}")
        if not set(feature["ticketKeys"]).issubset(tickets):
            errors.append(f"feature {feature['artifactPath']} references an unknown ticket")
        if not set(feature["scenarioIds"]).issubset(scenario_by_id):
            errors.append(f"feature {feature['artifactPath']} references an unknown scenario")

    if design["status"] == "ready" and design["queuedTickets"]:
        errors.append("ready design cannot contain queuedTickets")
    if design["status"] == "partial" and not design["queuedTickets"]:
        errors.append("partial design must identify queuedTickets")

    return errors


def main() -> int:
    if len(sys.argv) != 3:
        raise SystemExit("Usage: validate-test-design.py <analysis.json> <test-design-output.json>")

    errors = validate(Path(sys.argv[1]), Path(sys.argv[2]))
    if errors:
        for error in errors:
            print(f"ERROR: {error}", file=sys.stderr)
        return 1
    print("Sutra test-design contract validation passed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
