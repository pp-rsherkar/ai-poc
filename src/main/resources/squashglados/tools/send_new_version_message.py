import requests
import sys

# get command line arg for version
version = "unknown"
if len(sys.argv) > 1:
    print("version:", sys.argv[1])
    version = sys.argv[1]
else:
    print("This script expects one argument for the version.")
    sys.exit(1)

version = version.replace("refs/tags/", "")
message_json = { "activity": "SquashGlados AutoDeployer", "text": f"a new version of ib-SquashGlados=={version} was deployed to the IB QAA pip server" }
url = "https://hooks.ringcentral.com/webhook/v2/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvdCI6ImMiLCJvaSI6IjExMDQ4Nzk2MTciLCJpZCI6IjMxNTg1MzIxMjMifQ.wMA-hsZEjIsW2t0Tjjl8mjuDpTycfwYkK-vQNph1YHM"
result = requests.post(url, json=message_json)
print(f"result {result.status_code} {result.content}")
