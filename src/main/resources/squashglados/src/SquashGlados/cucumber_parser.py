import json

class CucumberParser:
    """ 04-12-2025
    Utility to parse Cucumber JSON report and extract scenario results.
    """
    def __init__(self, report_path):
        self.report_path = report_path
        self.scenarios = []
        self._load_report()

    def _load_report(self):
        try:
            with open(self.report_path, 'r', encoding='utf-8') as f:
                data = json.load(f)
            self._parse_scenarios(data)
        except Exception as e:
            print(f"Error loading Cucumber report: {e}")
            self.scenarios = []

    def _normalize_scenario_name(self, name):
        ### 04-12-2025 Added normalization for scenario names (strip, lower, remove params)
        name = name.strip().lower()
        # Remove parameterization (e.g., "Scenario Outline: ... [Example: ...]")
        if '[' in name:
            name = name.split('[')[0].strip()
        return name

    def _parse_scenarios(self, data):
        scenarios = []
        for feature in data:
            feature_name = feature.get('name', '')
            for element in feature.get('elements', []):
                if element.get('type') == 'scenario':
                    scenario_name = element.get('name', '')
                    normalized_name = self._normalize_scenario_name(scenario_name)  # 20-11-2025: Use normalized name
                    status = 'passed'
                    for step in element.get('steps', []):
                        if step.get('result', {}).get('status') == 'failed':
                            status = 'failed'
                            break
                        elif step.get('result', {}).get('status') == 'skipped':
                            status = 'skipped'
                    scenarios.append({
                        'feature': feature_name,
                        'scenario': normalized_name,  # 20-11-2025: Store normalized name
                        'status': status
                    })
        self.scenarios = scenarios

    def get_scenarios(self):
        return self.scenarios
