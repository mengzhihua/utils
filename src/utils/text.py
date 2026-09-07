"""String manipulation helpers."""

from __future__ import annotations

import re
import unicodedata

_SLUG_STRIP_RE = re.compile(r"[^\w\s-]")
_SLUG_HYPHENATE_RE = re.compile(r"[\s_-]+")


def slugify(value: str) -> str:
    """Return a lowercase, URL-safe slug derived from ``value``.

    Unicode characters are normalized to their closest ASCII representation,
    non-word characters are dropped, and runs of whitespace/underscores/hyphens
    collapse into a single hyphen.

    >>> slugify("  Héllo, World! ")
    'hello-world'
    """
    normalized = unicodedata.normalize("NFKD", value)
    ascii_value = normalized.encode("ascii", "ignore").decode("ascii")
    stripped = _SLUG_STRIP_RE.sub("", ascii_value).strip().lower()
    return _SLUG_HYPHENATE_RE.sub("-", stripped)


def truncate(value: str, length: int, suffix: str = "...") -> str:
    """Truncate ``value`` to at most ``length`` characters.

    When truncation occurs, ``suffix`` is appended and the total length still
    does not exceed ``length``.

    >>> truncate("hello world", 8)
    'hello...'
    """
    if length < 0:
        raise ValueError("length must be non-negative")
    if len(value) <= length:
        return value
    if length <= len(suffix):
        return value[:length]
    return value[: length - len(suffix)] + suffix
