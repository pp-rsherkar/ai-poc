import json
import os

print("=== parse_jira_fields.py started ===")
print("=== parse_jira_fields.py: script started ===", flush=True)
print(f"=== Working directory: {os.getcwd()} ===", flush=True)
print(f"=== Files in cwd: {os.listdir('.')} ===", flush=True)

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

print(f"=== JSON loaded, keys: {list(data.get('fields', {}).keys())[:5]} ===")

fields = data.get("fields", {})
summary = fields.get("summary", "No Summary")[:300]

raw_desc = fields.get("description") or {}
print(f"=== Description type: {type(raw_desc).__name__}, is dict: {isinstance(raw_desc, dict)} ===")

description = extract_adf_text(raw_desc).strip()[:12000]

comments_parts = []
for c in fields.get("comment", {}).get("comments", []):
    body = c.get("body") or {}
    comments_parts.append(extract_adf_text(body).strip())
comments = "\n".join(comments_parts)[:8000]

print(f"Summary     : {len(summary)} chars")
print(f"Description : {len(description)} chars")
print(f"Comments    : {len(comments)} chars")
print(f"Description preview: {description[:200]}")

with open("jira_fields.env", "w", encoding="utf-8") as f:
    f.write(f"SUMMARY<<ENVSEP\n{summary}\nENVSEP\n")
    f.write(f"DESCRIPTION<<ENVSEP\n{description}\nENVSEP\n")
    f.write(f"COMMENTS<<ENVSEP\n{comments}\nENVSEP\n")

print("=== jira_fields.env written successfully ===")
