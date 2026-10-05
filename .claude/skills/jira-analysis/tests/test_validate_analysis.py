import copy
import json
import subprocess
import sys
import tempfile
import unittest
from pathlib import Path


TEST_ROOT = Path(__file__).resolve().parent
SKILL_ROOT = TEST_ROOT.parent
SCRIPT = SKILL_ROOT / "scripts" / "validate-analysis.py"
VALID_ANALYSIS = TEST_ROOT / "fixtures" / "valid-analysis.json"


class ValidateAnalysisTest(unittest.TestCase):
    def add_blocked_case(self, analysis, comments):
        cases = analysis["tickets"][0]["testCoverage"]["testCases"]
        blocked_case = copy.deepcopy(cases[1])
        blocked_case.update(
            {
                "id": "TC_QA-100_04",
                "type": "blocked",
                "testData": "Configured to reproduce QA-999",
                "expectedResult": "Document whether all warnings render and log a gap",
                "status": "blocked",
                "comments": comments,
            }
        )
        cases.append(blocked_case)
        analysis["releaseSummary"].update(
            {"totalTestCases": 4, "averageTestCasesPerTicket": 4}
        )

    def run_analysis(self, analysis):
        with tempfile.TemporaryDirectory() as temporary_directory:
            analysis_path = Path(temporary_directory) / "analysis.json"
            analysis_path.write_text(json.dumps(analysis), encoding="utf-8")
            return subprocess.run(
                [sys.executable, str(SCRIPT), str(analysis_path)],
                capture_output=True,
                text=True,
                check=False,
            )

    def test_default_schema_is_independent_of_working_directory(self):
        with tempfile.TemporaryDirectory() as temporary_directory:
            result = subprocess.run(
                [sys.executable, str(SCRIPT), str(VALID_ANALYSIS)],
                cwd=temporary_directory,
                capture_output=True,
                text=True,
                check=False,
            )

        self.assertEqual(0, result.returncode, result.stderr)
        self.assertIn("VALID:", result.stdout)

    def test_inconsistent_release_summary_is_rejected(self):
        analysis = json.loads(VALID_ANALYSIS.read_text(encoding="utf-8"))
        analysis["releaseSummary"]["warnings"] = 1
        result = self.run_analysis(analysis)

        self.assertNotEqual(0, result.returncode)
        self.assertIn("releaseSummary.warnings must be 0, got 1", result.stderr)

    def test_source_manifest_must_record_primary_ticket(self):
        analysis = json.loads(VALID_ANALYSIS.read_text(encoding="utf-8"))
        analysis["tickets"][0]["sourceManifest"] = [
            {
                "type": "linked_issue",
                "id": "QA-101",
                "disposition": "excluded",
                "reason": "Out of scope",
            }
        ]

        result = self.run_analysis(analysis)

        self.assertNotEqual(0, result.returncode)
        self.assertIn("primary ticket as fetched", result.stderr)

    def test_variation_fields_must_be_declared_together(self):
        analysis = json.loads(VALID_ANALYSIS.read_text(encoding="utf-8"))
        del analysis["tickets"][0]["testCoverage"]["testCases"][0]["variationKey"]

        result = self.run_analysis(analysis)

        self.assertNotEqual(0, result.returncode)
        self.assertIn("variationKey", result.stderr)

    def test_variation_keys_must_be_unique(self):
        analysis = json.loads(VALID_ANALYSIS.read_text(encoding="utf-8"))
        cases = analysis["tickets"][0]["testCoverage"]["testCases"]
        cases[2]["variationKey"] = cases[0]["variationKey"]

        result = self.run_analysis(analysis)

        self.assertNotEqual(0, result.returncode)
        self.assertIn("duplicate variation keys", result.stderr)

    def test_non_verifiable_expected_result_is_rejected(self):
        analysis = json.loads(VALID_ANALYSIS.read_text(encoding="utf-8"))
        analysis["tickets"][0]["testCoverage"]["testCases"][1][
            "expectedResult"
        ] = "Document whether the request is rejected and log a gap if it is not."

        result = self.run_analysis(analysis)

        self.assertNotEqual(0, result.returncode)
        self.assertIn("non-verifiable expectedResult", result.stderr)

    def test_vague_regression_fixture_is_rejected(self):
        analysis = json.loads(VALID_ANALYSIS.read_text(encoding="utf-8"))
        analysis["tickets"][0]["testCoverage"]["testCases"][1][
            "testData"
        ] = "Request configured to reproduce QA-999"

        result = self.run_analysis(analysis)

        self.assertNotEqual(0, result.returncode)
        self.assertIn("non-reproducible testData", result.stderr)

    def test_reproduction_fixture_with_concrete_state_is_allowed(self):
        analysis = json.loads(VALID_ANALYSIS.read_text(encoding="utf-8"))
        analysis["tickets"][0]["testCoverage"]["testCases"][1][
            "testData"
        ] = "Request configured to reproduce QA-999 with status=inactive and retries=0"

        result = self.run_analysis(analysis)

        self.assertEqual(0, result.returncode, result.stderr)

    def test_unresolved_question_is_allowed_when_blocked_with_missing_fact(self):
        analysis = json.loads(VALID_ANALYSIS.read_text(encoding="utf-8"))
        self.add_blocked_case(
            analysis,
            "Missing expected warning precedence and concrete fixture configuration.",
        )

        result = self.run_analysis(analysis)

        self.assertEqual(0, result.returncode, result.stderr)

    def test_blocked_case_must_name_the_missing_fact(self):
        analysis = json.loads(VALID_ANALYSIS.read_text(encoding="utf-8"))
        self.add_blocked_case(analysis, "Follow up with the product team.")

        result = self.run_analysis(analysis)

        self.assertNotEqual(0, result.returncode)
        self.assertIn("comments do not identify the missing fact", result.stderr)


if __name__ == "__main__":
    unittest.main()
