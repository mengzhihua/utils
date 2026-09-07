"""utils: a small collection of general-purpose Python utilities.

The public API re-exports the most commonly used helpers so callers can do::

    from utils import slugify, chunked, retry
"""

from __future__ import annotations

from .collections import chunked, flatten, unique
from .retry import retry
from .text import slugify, truncate

__version__ = "0.1.0"

__all__ = [
    "__version__",
    "chunked",
    "flatten",
    "unique",
    "retry",
    "slugify",
    "truncate",
]
