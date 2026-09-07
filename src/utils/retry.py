"""A small, dependency-free retry decorator."""

from __future__ import annotations

import functools
import time
from collections.abc import Callable
from typing import TypeVar

T = TypeVar("T")


def retry(
    attempts: int = 3,
    *,
    delay: float = 0.0,
    backoff: float = 1.0,
    exceptions: type[Exception] | tuple[type[Exception], ...] = Exception,
) -> Callable[[Callable[..., T]], Callable[..., T]]:
    """Retry the decorated callable when it raises one of ``exceptions``.

    ``attempts`` is the total number of tries (must be >= 1). ``delay`` is the
    initial sleep between tries in seconds, multiplied by ``backoff`` after each
    failure. The final exception is re-raised once attempts are exhausted.
    """
    if attempts < 1:
        raise ValueError("attempts must be >= 1")
    if backoff < 1:
        raise ValueError("backoff must be >= 1")

    def decorator(func: Callable[..., T]) -> Callable[..., T]:
        @functools.wraps(func)
        def wrapper(*args: object, **kwargs: object) -> T:
            current_delay = delay
            last_exc: Exception | None = None
            for attempt in range(1, attempts + 1):
                try:
                    return func(*args, **kwargs)
                except exceptions as exc:
                    last_exc = exc
                    if attempt == attempts:
                        break
                    if current_delay > 0:
                        time.sleep(current_delay)
                    current_delay *= backoff
            assert last_exc is not None
            raise last_exc

        return wrapper

    return decorator
