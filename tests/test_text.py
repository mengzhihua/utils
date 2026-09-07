import pytest

from utils.text import slugify, truncate


@pytest.mark.parametrize(
    ("value", "expected"),
    [
        ("  Héllo, World! ", "hello-world"),
        ("Already-Slug", "already-slug"),
        ("multiple   spaces", "multiple-spaces"),
        ("snake_case_name", "snake-case-name"),
        ("", ""),
    ],
)
def test_slugify(value, expected):
    assert slugify(value) == expected


def test_truncate_no_change_when_short():
    assert truncate("hello", 10) == "hello"


def test_truncate_adds_suffix():
    assert truncate("hello world", 8) == "hello..."
    assert len(truncate("hello world", 8)) == 8


def test_truncate_custom_suffix():
    assert truncate("hello world", 6, suffix="…") == "hello…"


def test_truncate_negative_length_raises():
    with pytest.raises(ValueError):
        truncate("x", -1)
