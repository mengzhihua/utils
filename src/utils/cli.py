"""Command-line interface for the utils library.

Usage examples::

    utils slugify "Hello, World!"
    utils truncate "hello world" 8
    utils unique 3 1 3 2 1
    utils chunk 2 a b c d e
    utils --version
"""

from __future__ import annotations

import argparse
import sys
from collections.abc import Sequence

from . import __version__
from .collections import chunked, unique
from .text import slugify, truncate


def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(prog="utils", description="General-purpose utilities.")
    parser.add_argument("--version", action="version", version=f"utils {__version__}")
    sub = parser.add_subparsers(dest="command", required=True)

    p_slug = sub.add_parser("slugify", help="Convert text into a URL-safe slug.")
    p_slug.add_argument("text")

    p_trunc = sub.add_parser("truncate", help="Truncate text to a maximum length.")
    p_trunc.add_argument("text")
    p_trunc.add_argument("length", type=int)
    p_trunc.add_argument("--suffix", default="...")

    p_unique = sub.add_parser("unique", help="Print unique values preserving order.")
    p_unique.add_argument("values", nargs="+")

    p_chunk = sub.add_parser("chunk", help="Split values into fixed-size chunks.")
    p_chunk.add_argument("size", type=int)
    p_chunk.add_argument("values", nargs="+")

    return parser


def main(argv: Sequence[str] | None = None) -> int:
    parser = build_parser()
    args = parser.parse_args(argv)

    if args.command == "slugify":
        print(slugify(args.text))
    elif args.command == "truncate":
        print(truncate(args.text, args.length, args.suffix))
    elif args.command == "unique":
        print(" ".join(unique(args.values)))
    elif args.command == "chunk":
        for batch in chunked(args.values, args.size):
            print(" ".join(batch))
    else:  # pragma: no cover - argparse enforces a valid command
        parser.error(f"unknown command: {args.command}")

    return 0


if __name__ == "__main__":  # pragma: no cover
    sys.exit(main())
