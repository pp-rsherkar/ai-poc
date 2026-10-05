#!/usr/bin/env python3
"""Apply the repository's deterministic Gherkin formatter to selected files."""

from pathlib import Path
import subprocess
import sys


def main() -> int:
    if len(sys.argv) < 2:
        raise SystemExit("Usage: align-feature-tables.py <feature-file> [<feature-file> ...]")

    repository_root = Path(__file__).resolve().parents[4]
    formatter = repository_root / "scripts" / "format-features.sh"
    files = [str(Path(path).resolve()) for path in sys.argv[1:]]
    return subprocess.run([str(formatter), *files], cwd=repository_root, check=False).returncode


if __name__ == "__main__":
    raise SystemExit(main())
