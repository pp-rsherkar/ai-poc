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

        GOAL: Find the most stable CSS or XPath selector for the element described as: "${intent}".

        CRITICAL RULES:
        1. Return ONLY ONE raw selector string.
        2. STRICT CONTEXT ONLY: You MUST find an element that actually exists in the provided CONTEXT.
        3. NO HALLUCINATIONS: Do not guess class names. Use exact attributes and text found in CONTEXT.
        4. ATTRIBUTE HIERARCHY:
            - If the intent contains an email or unique text, prioritize exact text-based XPath: //div[text()='buyer2@ppcom']
            - Then use the closest class match found IN THE CONTEXT.
        5. VERIFICATION: Double-check that your selector exists in CONTEXT before outputting.
        6. OUTPUT: Return ONLY the raw selector string.
        7. FUZZY MATCHING: Match intent text to visible element placeholders or innerText, not assumed attributes.
        8. DO NOT return code snippets like getByRole(), getByText(), or page.locator().
        9. Use ONLY:
            - Placeholder text (e.g., //input[@placeholder='Search'])
            - CSS selectors (e.g., .loginBtn, button[type='submit'], ignoring 'ng-' or 'active' classes)
            - XPath selectors (e.g., //button[contains(., 'Log In')])
            - Text-based XPath (e.g., //button[contains(., 'Log In')])
        10. NO markdown.
        11. DO NOT provide multiple options.
        12. No commas or explanations.
        13. UNIQUENESS & PRECISION: If multiple elements match, differentiate them using parent containers, combined classes, or index-based XPath ([1], [last()]) to ensure a 1:1 match.
        14. VISIBILITY & POSITION: Target only elements visible in the UI. Prefer elements in header, top-right, or user-profile sections if CONTEXT shows them. Ignore hidden duplicates.
        15. EXACT TEXT PRIORITY: For intents containing emails, usernames, or unique identifiers, prioritize exact or substring text matches (e.g., //div[text()='buyer2@ppcom']).
        16. SELECTOR PREFERENCE ORDER:
            Exact text XPath > XPath with combined parent/child classes > Indexed XPath > CSS selector.

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