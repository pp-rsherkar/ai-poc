const url = process.env.SQUASH_URL;
const token = process.env.SQUASH_TOKEN;
const searchValue = process.env.SEARCH_VALUE;

if (!url || !token || !searchValue) {
  console.error("Missing required environment variables.");
  process.exit(1);
}

const isNumeric = /^\d+$/.test(searchValue);

const structuralArgs = {
  case_id: isNumeric ? parseInt(searchValue, 10) : 0,
  folder_id: 0,
  project_id: 0,
  semantic_query: isNumeric ? "" : searchValue,
  name_keyword_filter: "",
  creator: "",
  status_list: "",
  weight_list: "",
  nature_list: "",
  type_list: "",
  test_category_list: "",
  exclude_test_category_list: "",
  automated: "any",
  page: 1
};

const payload = {
  jsonrpc: "2.0",
  id: 1,
  method: "tools/call",
  params: {
    name: "search-for-test-case",
    arguments: structuralArgs
  }
};

async function run() {
  console.log(`Sending HTTP POST to Squash MCP Gateway via Local Network: ${url}`);
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
        'Accept': 'application/json, text/event-stream'
      },
      body: JSON.stringify(payload)
    });

    console.log(`HTTP Status: ${response.status} ${response.statusText}`);
    
    const text = await response.text();
    console.log("================ MCP RESPONSE ================");
    try {
      console.log(JSON.stringify(JSON.parse(text), null, 2));
    } catch {
      console.log(text);
    }
    console.log("==============================================");

    if (!response.ok) {
      process.exit(1);
    }
  } catch (error) {
    console.error("Network or execution payload error:", error);
    process.exit(1);
  }
}

run();
