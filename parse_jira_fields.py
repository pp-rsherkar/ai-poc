"""
scripts/parse_jira_fields.py
Reads jira_response.json, writes jira_fields.env
"""
import json
import os


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

fields = data.get("fields", {})
summary = fields.get("summary", "No Summary")[:300]
desc_node = fields.get("description") or {}
description = extract_adf_text(desc_node).strip()[:12000]

comments_parts = []
for c in fields.get("comment", {}).get("comments", []):
    body = c.get("body") or {}
    comments_parts.append(extract_adf_text(body).strip())
comments = "\n".join(comments_parts)[:8000]

with open("jira_fields.env", "w", encoding="utf-8") as f:
    f.write(f"SUMMARY<<ENVSEP\n{summary}\nENVSEP\n")
    f.write(f"DESCRIPTION<<ENVSEP\n{description}\nENVSEP\n")
    f.write(f"COMMENTS<<ENVSEP\n{comments}\nENVSEP\n")

print(f"Summary     : {len(summary)} chars")
print(f"Description : {len(description)} chars")
print(f"Comments    : {len(comments)} chars")
