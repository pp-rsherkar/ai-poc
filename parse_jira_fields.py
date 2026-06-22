import json
import os
import sys

print("=== parse_jira_fields.py started ===", flush=True)


def extract_adf_text(node):
    if not isinstance(node, dict):
        return ""
    node_type = node.get("type", "")
    if node_type == "text":
        return node.get("text", "")
    block_types = {
        "paragraph", "heading", "bulletList", "orderedList",
        "listItem", "blockquote", "codeBlock", "panel", "rule", "mediaGroup"
    }
    text = ""
    for child in node.get("content", []):
        text += extract_adf_text(child)
    if node_type in block_types:
        text += "\n"
    return text


with open("jira_response.json", "r", encoding="utf-8") as f:
    data = json.load(f)

fields    = data.get("fields", {})
summary   = fields.get("summary", "No Summary")[:300]
raw_desc  = fields.get("description") or {}
desc_full = extract_adf_text(raw_desc).strip() if isinstance(raw_desc, dict) else str(raw_desc).strip()

comments_parts = []
for c in fields.get("comment", {}).get("comments", []):
    body = c.get("body") or {}
    if isinstance(body, dict):
        comments_parts.append(extract_adf_text(body).strip())
    elif isinstance(body, str):
        comments_parts.append(body.strip())
comments_full = "\n".join(comments_parts)

# Write summary to env (short, safe)
with open("jira_fields.env", "w", encoding="utf-8") as f:
    f.write(f"SUMMARY<<ENVSEP\n{summary}\nENVSEP\n")

# Write description and comments to FILES instead of env vars
with open("jira_description.txt", "w", encoding="utf-8") as f:
    f.write(desc_full)

with open("jira_comments.txt", "w", encoding="utf-8") as f:
    f.write(comments_full)

print(f"Summary     : {len(summary)} chars")
print(f"Description : {len(desc_full)} chars → jira_description.txt")
print(f"Comments    : {len(comments_full)} chars → jira_comments.txt")
print("=== done ===", flush=True)
