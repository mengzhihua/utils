import pytest

from utils.cli import main


def test_cli_slugify(capsys):
    assert main(["slugify", "Hello, World!"]) == 0
    assert capsys.readouterr().out.strip() == "hello-world"


def test_cli_truncate(capsys):
    assert main(["truncate", "hello world", "8"]) == 0
    assert capsys.readouterr().out.strip() == "hello..."


def test_cli_unique(capsys):
    assert main(["unique", "3", "1", "3", "2", "1"]) == 0
    assert capsys.readouterr().out.strip() == "3 1 2"


def test_cli_chunk(capsys):
    assert main(["chunk", "2", "a", "b", "c", "d", "e"]) == 0
    assert capsys.readouterr().out.strip() == "a b\nc d\ne"


def test_cli_requires_command(capsys):
    with pytest.raises(SystemExit):
        main([])
