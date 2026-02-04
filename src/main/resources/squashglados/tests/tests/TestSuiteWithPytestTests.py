import pytest
import SquashGlados

@pytest.mark.usefixtures('base_setup')
class TestSuiteWithPytestTests:
    def test_shouldUsePytestAndPass(self, browser):
        print(f"Running test with browser: {browser}")
        assert True, "test_shouldUsePytestAndPass is NOT supposed to fail"

    def test_runPytestAndFail(self):
        assert False, "test_runPytestAndFail is failing and this is the error message"

    def test_runPytestAndMarkSettled(self, request, execution_id):
        comment = "This test is marked as SETTLED in the test suite."
        request.node.comment = comment
        SquashGlados.SquashGlados().update_execution(execution_id, "SETTLED", comment, squash_server="prod")
        request.node.test_status = "SETTLED"
        assert True, "test_runPytestAndMarkSettled is SETTLED"

    def test_runPytestAndMarkBlocked(self, request, execution_id):
        comment = "This test is marked as BLOCKED in the test suite."
        request.node.comment = comment
        SquashGlados.SquashGlados().update_execution(execution_id, "BLOCKED", comment, squash_server="prod")
        # how can I set a varaible here that can be accessed in the pytest_runtest_protocol?
        request.node.test_status = "BLOCKED" # 
        assert False, "test_runPytestAndMarkBlocked is BLOCKED"


