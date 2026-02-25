const GITHUB_TOKEN = "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";
const MODEL_NAME = "gpt-4o-mini";

const MAX_CONTEXT = 15000; // Prevent token overflow

function cleanSelector(output) {
    if (!output) return null;

    return output
        .replace(/```/g, "")
        .replace(/^["']|["']$/g, "")
        .trim();
}

async function main() {
    const intent = process.argv[2];
    let axTree = "";

    if (!GITHUB_TOKEN) {
        console.error("Missing GITHUB_TOKEN environment variable.");
        process.exit(1);
    }

    process.stdin.setEncoding("utf8");

    process.stdin.on("data", chunk => {
        axTree += chunk;
    });

    process.stdin.on("end", async () => {

        if (!axTree || !intent) {
            console.error("Missing intent or context.");
            process.exit(1);
        }

        // Trim large DOM
        const trimmedContext =
            axTree.length > MAX_CONTEXT
                ? axTree.substring(0, MAX_CONTEXT)
                : axTree;

        const prompt = `
			You are an expert AI Test Automation Engineer specializing in Playwright and Model Context Protocol (MCP).

			Find the best CSS or XPath selector for: "${intent}".

			CRITICAL RULES:
			1. Return ONLY ONE raw selector string.
			2. DO NOT return code snippets like getByRole(), getByText(), or page.locator().
			3. Use ONLY:
			   - CSS Selectors (e.g., .loginBtn, button[type='submit'])
			   - XPath Selectors (e.g., //button[contains(., 'Log In')])
			4. NO markdown (no \`\`\`).
			5. DO NOT provide multiple options.
			6. No commas, no explanations, no markdown

			CONTEXT:
			${trimmedContext}
			`;

        try {
            const response = await fetch(
                "https://models.inference.ai.azure.com/chat/completions",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${GITHUB_TOKEN}`
                    },
                    body: JSON.stringify({
                        model: MODEL_NAME,
                        messages: [
                            {
                                role: "system",
                                content: "You generate raw CSS or XPath selectors only."
                            },
                            {
                                role: "user",
                                content: prompt
                            }
                        ],
                        temperature: 0.1,
                        top_p: 0.9,
                        max_tokens: 200
                    })
                }
            );

            if (!response.ok) {
                const errText = await response.text();
                console.error("HTTP Error:", errText);
                process.exit(1);
            }

            const data = await response.json();

            const rawOutput =
                data.choices?.[0]?.message?.content;

            const selector = cleanSelector(rawOutput);

            if (!selector) {
                console.error("Invalid AI response:", JSON.stringify(data));
                process.exit(1);
            }

            process.stdout.write(selector);
            process.exit(0);

        } catch (err) {
            console.error("Bridge Error:", err.message);
            process.exit(1);
        }
    });
}

main();