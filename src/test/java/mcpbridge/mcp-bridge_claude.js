process.stdin.on('end', async () => {
        try {
            const apiKey = "YOUR_ANTHROPIC_API_KEY"; // Get this from console.anthropic.com

            const transport = new StdioClientTransport({
                command: "npx",
                args: ["-y", "@playwright/mcp@latest"],
                env: { 
                    ...process.env, 
                    ANTHROPIC_API_KEY: apiKey 
                }
            });

            const client = new Client({ name: "java-bridge", version: "1.0.0" });
            await client.connect(transport);

            // This tool specifically asks the LLM to find a selector based on the snapshot
            const result = await client.callTool({
                name: "playwright_get_selector", 
                arguments: { 
                    description: intent,
                    snapshot: axTree 
                }
            });

            // Output only the selector string so Java can read it
            const healedSelector = result.content[0].text;
            process.stdout.write(healedSelector);
            process.exit(0);
        } catch (err) {
            console.error(err.message);
            process.exit(1);
        }
    });