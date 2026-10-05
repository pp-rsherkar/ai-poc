import copy
import importlib.util
import json
from pathlib import Path
import tempfile
import unittest


SCRIPT = Path(__file__).resolve().parents[1] / "scripts" / "validate-test-design.py"
SPEC = importlib.util.spec_from_file_location("validate_test_design", SCRIPT)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
SPEC.loader.exec_module(MODULE)


class TestValidateTestDesign(unittest.TestCase):
    def setUp(self):
        root = Path(__file__).resolve().parents[4]
        self.analysis_path = root / ".claude/skills/jira-analysis/tests/fixtures/valid-analysis.json"
        self.design_path = Path(__file__).parent / "fixtures" / "valid-test-design.json"

    def design_with_duplicate(self):
        design = copy.deepcopy(MODULE.load_json(self.design_path))
        design["featureFiles"][0]["scenarioIds"].remove("SC_QA-100_02")
        design["scenarios"] = [
            scenario
            for scenario in design["scenarios"]
            if scenario["id"] != "SC_QA-100_02"
        ]
        design["frameworkGaps"][0]["affectedScenarioIds"].remove("SC_QA-100_02")
        triage = next(
            item for item in design["triage"] if item["testCaseId"] == "TC_QA-100_02"
        )
        triage.update(
            {
                "automationCandidate": "duplicate",
                "disposition": "duplicate",
                "rationale": "An existing scenario contains the exact assertion.",
                "scenarioIds": [],
                "coveredBy": {
                    "featurePath": "src/test/resources/features/life/Life_PMP.feature",
                    "scenarioName": "A tactic showing a no-avails warning can still be saved and activated, and the warning clears once avails resume",
                    "assertionAnchor": "The tactic can still be saved and can still be activated",
                },
            }
        )
        return design

    def design_with_blocked_case(self):
        design = copy.deepcopy(MODULE.load_json(self.design_path))
        design["featureFiles"][0]["scenarioIds"].remove("SC_QA-100_02")
        design["scenarios"] = [
            scenario
            for scenario in design["scenarios"]
            if scenario["id"] != "SC_QA-100_02"
        ]
        design["frameworkGaps"][0]["affectedScenarioIds"].remove("SC_QA-100_02")
        triage = next(
            item for item in design["triage"] if item["testCaseId"] == "TC_QA-100_02"
        )
        triage.update(
            {
                "automationCandidate": "blocked",
                "disposition": "blocked",
                "rationale": "The expected behavior is unresolved.",
                "scenarioIds": [],
            }
        )
        return design

    def validate_temporary_payloads(self, analysis, design, temporary_directory):
        analysis_path = Path(temporary_directory) / "analysis.json"
        design_path = Path(temporary_directory) / "design.json"
        analysis_path.write_text(json.dumps(analysis), encoding="utf-8")
        design["sourceAnalysis"]["path"] = str(analysis_path)
        design_path.write_text(json.dumps(design), encoding="utf-8")
        return MODULE.validate(analysis_path, design_path)

    def test_valid_design_passes(self):
        self.assertEqual([], MODULE.validate(self.analysis_path, self.design_path))

    def test_missing_triage_mapping_fails(self):
        original = MODULE.load_json(self.design_path)
        invalid = copy.deepcopy(original)
        invalid["triage"].pop()

        temporary = Path(__file__).parent / "fixtures" / "invalid-test-design.tmp.json"
        temporary.write_text(__import__("json").dumps(invalid), encoding="utf-8")
        try:
            errors = MODULE.validate(self.analysis_path, temporary)
        finally:
            temporary.unlink(missing_ok=True)

        self.assertTrue(any("missing triage records" in error for error in errors))

    def test_variation_group_requires_scenario_outline(self):
        invalid = copy.deepcopy(MODULE.load_json(self.design_path))
        invalid["scenarios"][0]["kind"] = "scenario"

        with tempfile.TemporaryDirectory() as temporary_directory:
            path = Path(temporary_directory) / "design.json"
            path.write_text(json.dumps(invalid), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, path)

        self.assertTrue(any("must map to a scenario_outline" in error for error in errors))
        self.assertTrue(any("kind does not match generated Gherkin" in error for error in errors))

    def test_variation_group_requires_one_examples_row_per_case(self):
        design = copy.deepcopy(MODULE.load_json(self.design_path))
        source_feature = Path(__file__).parent / "fixtures" / "Sample.feature"

        with tempfile.TemporaryDirectory() as temporary_directory:
            feature_path = Path(temporary_directory) / "Sample.feature"
            feature_path.write_text(
                source_feature.read_text(encoding="utf-8").replace(
                    "      | APPROVED       |\n", ""
                ),
                encoding="utf-8",
            )
            design["featureFiles"][0]["artifactPath"] = str(feature_path)
            design["scenarios"][0]["featurePath"] = str(feature_path)
            design["scenarios"][1]["featurePath"] = str(feature_path)
            design_path = Path(temporary_directory) / "design.json"
            design_path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, design_path)

        self.assertTrue(any("has 2 cases but SC_QA-100_01 has 1 Examples rows" in error for error in errors))

    def test_duplicate_with_resolvable_assertion_evidence_passes(self):
        design = self.design_with_duplicate()

        with tempfile.TemporaryDirectory() as temporary_directory:
            path = Path(temporary_directory) / "design.json"
            path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, path)

        self.assertEqual([], errors)

    def test_duplicate_without_covered_by_fails_schema(self):
        design = self.design_with_duplicate()
        del design["triage"][1]["coveredBy"]

        with tempfile.TemporaryDirectory() as temporary_directory:
            path = Path(temporary_directory) / "design.json"
            path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, path)

        self.assertTrue(any("coveredBy" in error for error in errors))

    def test_duplicate_with_missing_feature_fails(self):
        design = self.design_with_duplicate()
        design["triage"][1]["coveredBy"]["featurePath"] = (
            "src/test/resources/features/life/Does_Not_Exist.feature"
        )

        with tempfile.TemporaryDirectory() as temporary_directory:
            path = Path(temporary_directory) / "design.json"
            path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, path)

        self.assertTrue(any("references missing feature" in error for error in errors))

    def test_duplicate_with_missing_scenario_fails(self):
        design = self.design_with_duplicate()
        design["triage"][1]["coveredBy"]["scenarioName"] = "Broadly similar behavior"

        with tempfile.TemporaryDirectory() as temporary_directory:
            path = Path(temporary_directory) / "design.json"
            path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, path)

        self.assertTrue(any("references missing scenario" in error for error in errors))

    def test_duplicate_with_missing_assertion_fails(self):
        design = self.design_with_duplicate()
        design["triage"][1]["coveredBy"]["assertionAnchor"] = (
            "The exact day-seven boundary clears the warning"
        )

        with tempfile.TemporaryDirectory() as temporary_directory:
            path = Path(temporary_directory) / "design.json"
            path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, path)

        self.assertTrue(any("assertionAnchor is absent" in error for error in errors))

    def test_generated_then_cannot_contain_investigative_instruction(self):
        design = copy.deepcopy(MODULE.load_json(self.design_path))
        source_feature = Path(__file__).parent / "fixtures" / "Sample.feature"

        with tempfile.TemporaryDirectory() as temporary_directory:
            feature_path = Path(temporary_directory) / "Sample.feature"
            feature_path.write_text(
                source_feature.read_text(encoding="utf-8").replace(
                    "Then The request is rejected",
                    "Then Document whether the request is rejected and log a gap",
                ),
                encoding="utf-8",
            )
            design["featureFiles"][0]["artifactPath"] = str(feature_path)
            for scenario in design["scenarios"]:
                scenario["featurePath"] = str(feature_path)
            design_path = Path(temporary_directory) / "design.json"
            design_path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, design_path)

        self.assertTrue(any("non-verifiable assertion step" in error for error in errors))

    def test_investigative_netra_case_cannot_be_authored(self):
        analysis = MODULE.load_json(self.analysis_path)
        analysis["tickets"][0]["testCoverage"]["testCases"][1][
            "expectedResult"
        ] = "Confirm whether the request is rejected."
        design = copy.deepcopy(MODULE.load_json(self.design_path))

        with tempfile.TemporaryDirectory() as temporary_directory:
            errors = self.validate_temporary_payloads(
                analysis, design, temporary_directory
            )

        self.assertTrue(any("must remain blocked" in error for error in errors))

    def test_vague_netra_fixture_cannot_be_authored(self):
        analysis = MODULE.load_json(self.analysis_path)
        analysis["tickets"][0]["testCoverage"]["testCases"][1][
            "testData"
        ] = "Request configured with known production traffic"
        design = copy.deepcopy(MODULE.load_json(self.design_path))

        with tempfile.TemporaryDirectory() as temporary_directory:
            errors = self.validate_temporary_payloads(
                analysis, design, temporary_directory
            )

        self.assertTrue(any("must remain blocked" in error for error in errors))

    def test_blocked_netra_case_passes_when_sutra_keeps_it_blocked(self):
        analysis = MODULE.load_json(self.analysis_path)
        source_case = analysis["tickets"][0]["testCoverage"]["testCases"][1]
        source_case.update(
            {
                "type": "blocked",
                "status": "blocked",
                "expectedResult": "Document whether the request is rejected.",
                "comments": "Missing authoritative rejection behavior.",
            }
        )
        design = self.design_with_blocked_case()

        with tempfile.TemporaryDirectory() as temporary_directory:
            errors = self.validate_temporary_payloads(
                analysis, design, temporary_directory
            )

        self.assertEqual([], errors)

    def test_source_tag_must_be_immediately_above_todo(self):
        design = copy.deepcopy(MODULE.load_json(self.design_path))
        source_feature = Path(__file__).parent / "fixtures" / "Sample.feature"

        with tempfile.TemporaryDirectory() as temporary_directory:
            feature_path = Path(temporary_directory) / "Sample.feature"
            feature_path.write_text(
                source_feature.read_text(encoding="utf-8").replace(
                    "# Source: QA-100-R01 TC_QA-100_02\n  @todo",
                    "# Source: QA-100-R01 TC_QA-100_02\n\n  @todo",
                ),
                encoding="utf-8",
            )
            design["featureFiles"][0]["artifactPath"] = str(feature_path)
            for scenario in design["scenarios"]:
                scenario["featurePath"] = str(feature_path)
            design_path = Path(temporary_directory) / "design.json"
            design_path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, design_path)

        self.assertTrue(any("# Source immediately above @todo" in error for error in errors))

    def test_source_tag_must_list_every_authored_case(self):
        design = copy.deepcopy(MODULE.load_json(self.design_path))
        source_feature = Path(__file__).parent / "fixtures" / "Sample.feature"

        with tempfile.TemporaryDirectory() as temporary_directory:
            feature_path = Path(temporary_directory) / "Sample.feature"
            feature_path.write_text(
                source_feature.read_text(encoding="utf-8").replace(
                    " TC_QA-100_03", ""
                ),
                encoding="utf-8",
            )
            design["featureFiles"][0]["artifactPath"] = str(feature_path)
            for scenario in design["scenarios"]:
                scenario["featurePath"] = str(feature_path)
            design_path = Path(temporary_directory) / "design.json"
            design_path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, design_path)

        self.assertTrue(any("source tag is missing authored cases" in error for error in errors))

    def test_outline_requires_two_examples_rows(self):
        design = copy.deepcopy(MODULE.load_json(self.design_path))
        source_feature = Path(__file__).parent / "fixtures" / "Sample.feature"

        with tempfile.TemporaryDirectory() as temporary_directory:
            feature_path = Path(temporary_directory) / "Sample.feature"
            feature_path.write_text(
                source_feature.read_text(encoding="utf-8").replace(
                    "      | APPROVED       |\n", ""
                ),
                encoding="utf-8",
            )
            design["featureFiles"][0]["artifactPath"] = str(feature_path)
            for scenario in design["scenarios"]:
                scenario["featurePath"] = str(feature_path)
            design_path = Path(temporary_directory) / "design.json"
            design_path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, design_path)

        self.assertTrue(any("outline must contain at least two" in error for error in errors))

    def test_scenario_name_must_match_generated_gherkin(self):
        design = copy.deepcopy(MODULE.load_json(self.design_path))
        design["scenarios"][1]["name"] = "A generalized validation scenario"

        with tempfile.TemporaryDirectory() as temporary_directory:
            path = Path(temporary_directory) / "design.json"
            path.write_text(json.dumps(design), encoding="utf-8")
            errors = MODULE.validate(self.analysis_path, path)

        self.assertTrue(any("name does not match" in error for error in errors))


if __name__ == "__main__":
    unittest.main()
