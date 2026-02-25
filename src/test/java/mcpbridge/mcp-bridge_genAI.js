const { GoogleGenAI } = require("@google/genai");

async function main() {
    const intent = process.argv[2];
    let axTree = "";

    // Read the Accessibility Tree from Java's stdin
    process.stdin.on('data', (chunk) => { axTree += chunk; });

    process.stdin.on('end', async () => {
        try {
            // 1. Initialize the new Client
            const client = new GoogleGenAI({
                apiKey: "AIzaSyDxGMMlYs6PcDBiIuMrVrJ8UyoMvKBoUuU"
            });
			
			// CORRECTED DEBUG LOG
			try {
				const modelsList = await client.models.list();
				console.log("Available Models:", modelsList.models.map(m => m.name));
				const models = await genAI.listModels();
				console.log(models);
			} catch (e) {
				// ignore
			}

            // 2. Select the current "Workhorse" model for 2026
            const modelId = "gemini-2.5-flash"; //gemini-1.5-flash

            // 3. Craft the prompt

			const prompt = `
				You are an expert AI Test Automation Engineer specializing in Playwright and Model Context Protocol (MCP).

				Find the best CSS or XPath selector for: "${intent}".
				
				CRITICAL RULES:
				1. Return ONLY a raw selector string.
				2. DO NOT return code snippets like getByRole(), getByText(), or page.locator().
				3. Use ONLY:
				   - CSS Selectors (e.g., .loginBtn, button[type='submit'])
				   - XPath Selectors (e.g., //button[contains(., 'Log In')])
				4. NO markdown (no \` \` \`).
				
				CONTEXT:
				${axTree}
			`;

            // 4. Generate Content (New Method Structure)
            const response = await client.models.generateContent({
                model: modelId,
                contents: [{ role: 'user', parts: [{ text: prompt }] }]
            });

            const healedSelector = response.text.trim();

            // 5. Send back to Java
            process.stdout.write(healedSelector);
            process.exit(0);
        } catch (err) {
            console.error("AI Healing Error: " + err.message);
            process.exit(1);
        }
    });
}

main();