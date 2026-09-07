import pytest

from utils.collections import chunked, flatten, unique


def test_chunked_even():
    assert list(chunked([1, 2, 3, 4], 2)) == [[1, 2], [3, 4]]


def test_chunked_remainder():
    assert list(chunked([1, 2, 3, 4, 5], 2)) == [[1, 2], [3, 4], [5]]


def test_chunked_invalid_size():
    with pytest.raises(ValueError):
        list(chunked([1, 2, 3], 0))


def test_flatten():
    assert flatten([[1, 2], [3], [4, 5]]) == [1, 2, 3, 4, 5]


def test_unique_preserves_order():
    assert unique([3, 1, 3, 2, 1]) == [3, 1, 2]


def test_unique_strings():
    assert unique(["a", "b", "a", "c"]) == ["a", "b", "c"]
