"""Helpers for working with iterables and collections."""

from __future__ import annotations

from collections.abc import Hashable, Iterable, Iterator
from typing import TypeVar

T = TypeVar("T")
H = TypeVar("H", bound=Hashable)


def chunked(items: Iterable[T], size: int) -> Iterator[list[T]]:
    """Yield successive lists of at most ``size`` items from ``items``.

    >>> list(chunked([1, 2, 3, 4, 5], 2))
    [[1, 2], [3, 4], [5]]
    """
    if size <= 0:
        raise ValueError("size must be a positive integer")
    batch: list[T] = []
    for item in items:
        batch.append(item)
        if len(batch) == size:
            yield batch
            batch = []
    if batch:
        yield batch


def flatten(nested: Iterable[Iterable[T]]) -> list[T]:
    """Flatten one level of nesting into a single list.

    >>> flatten([[1, 2], [3], [4, 5]])
    [1, 2, 3, 4, 5]
    """
    return [item for sub in nested for item in sub]


def unique(items: Iterable[H]) -> list[H]:
    """Return the unique items from ``items`` preserving first-seen order.

    >>> unique([3, 1, 3, 2, 1])
    [3, 1, 2]
    """
    seen: set[H] = set()
    result: list[H] = []
    for item in items:
        if item not in seen:
            seen.add(item)
            result.append(item)
    return result
