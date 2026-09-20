#!/usr/bin/env python3
"""Collapse jpackage output into one arch-tagged archive for GitHub Releases."""
from __future__ import annotations

import argparse
import shutil
import sys
from pathlib import Path


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser()
    parser.add_argument("native_dir")
    parser.add_argument("version")
    parser.add_argument("platform")
    parser.add_argument("arch")
    return parser.parse_args()


def first_match(root: Path, pattern: str) -> Path | None:
    matches = sorted(root.glob(pattern))
    return matches[0] if matches else None


def main() -> int:
    args = parse_args()
    root = Path(args.native_dir).resolve()
    if not root.is_dir():
        print(f"native dir not found: {root}", file=sys.stderr)
        return 1

    stem = f"utils-{args.version}-{args.platform}-{args.arch}"
    app_dir = root / "Utils"
    installer = None
    if args.platform == "macos":
        installer = first_match(root, "Utils*.dmg") or first_match(root, "Utils*.pkg")
    elif args.platform == "windows":
        installer = first_match(root, "Utils*.exe") or first_match(root, "Utils*.msi")

    if installer:
        dest = root / f"{stem}{installer.suffix.lower()}"
        if installer.resolve() != dest.resolve():
            installer.rename(dest)
        print(dest)
        return 0
    if app_dir.is_dir():
        dest = root / f"{stem}.zip"
        if dest.exists():
            dest.unlink()
        shutil.make_archive(str(dest.with_suffix("")), "zip", root_dir=root, base_dir=app_dir.name)
        shutil.rmtree(app_dir)
        print(dest)
        return 0

    print(f"no jpackage output in {root}", file=sys.stderr)
    return 1


if __name__ == "__main__":
    raise SystemExit(main())
