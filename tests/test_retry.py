import pytest

from utils.retry import retry


def test_retry_succeeds_after_failures():
    calls = {"n": 0}

    @retry(attempts=3)
    def flaky():
        calls["n"] += 1
        if calls["n"] < 3:
            raise ValueError("boom")
        return "ok"

    assert flaky() == "ok"
    assert calls["n"] == 3


def test_retry_reraises_after_exhausting():
    calls = {"n": 0}

    @retry(attempts=2)
    def always_fails():
        calls["n"] += 1
        raise RuntimeError("nope")

    with pytest.raises(RuntimeError, match="nope"):
        always_fails()
    assert calls["n"] == 2


def test_retry_only_catches_listed_exceptions():
    @retry(attempts=3, exceptions=ValueError)
    def raises_type_error():
        raise TypeError("unhandled")

    with pytest.raises(TypeError):
        raises_type_error()


def test_retry_invalid_attempts():
    with pytest.raises(ValueError):
        retry(attempts=0)
