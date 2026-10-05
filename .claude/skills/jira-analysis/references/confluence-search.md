# Contextual Confluence Search

Use this procedure only when `confluenceDetailMode` is `shallow` or `full`.

1. Cluster tickets that share a feature area so one search can serve related tickets.
2. Build one concise natural-language query per cluster from its domain, intent, components, labels, and parent or epic context.
3. Use semantic Atlassian search. Do not replace it with exact-phrase CQL matching.
4. Keep only Confluence pages from mixed Jira and Confluence results.
5. Retain a focused ranked set, normally the top four to six pages per cluster.
6. Deduplicate pages by page ID. Attribute each retained page to every ticket in the cluster and keep its best rank.
7. In `shallow` mode, store the returned excerpt.
8. In `full` mode, hydrate only the highest-ranked primary specification for each cluster unless more pages are necessary. Store the body only when retrieval succeeds.
9. Record tickets with no matching documentation. Do not treat an umbrella or release-notes page as a dedicated specification.

Never refetch Jira during this procedure. Never invent page content or attribution. Documentation may add context, but it does not automatically clear a readiness flag.
