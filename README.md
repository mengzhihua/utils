# utils

A small collection of general-purpose Python utilities with a command-line interface.

## Features

- `slugify(value)` — turn arbitrary text into a lowercase, URL-safe slug.
- `truncate(value, length, suffix="...")` — shorten text to a maximum length.
- `chunked(items, size)` — split an iterable into fixed-size chunks.
- `flatten(nested)` — flatten one level of nesting.
- `unique(items)` — de-duplicate while preserving first-seen order.
- `retry(attempts=3, delay=0, backoff=1, exceptions=Exception)` — retry decorator.

## Requirements

- Python 3.10+

## Setup

The project uses a standard `pyproject.toml`. Create a virtual environment and install
the package in editable mode with its development tools:

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -e '.[dev]'
```

> In Cloud Agents this is handled automatically by the `install` command in
> `.cursor/environment.json`, which creates `.venv/` and installs the package with dev deps.

## Usage

### As a library

```python
from utils import slugify, chunked, unique, retry

slugify("  Héllo, World! ")        # -> "hello-world"
list(chunked([1, 2, 3, 4, 5], 2))  # -> [[1, 2], [3, 4], [5]]
unique([3, 1, 3, 2, 1])            # -> [3, 1, 2]
```

### As a command-line tool

Once installed, the `utils` command is available (via `.venv/bin/utils` or after
activating the virtualenv):

```bash
utils slugify "Hello, World!"        # hello-world
utils truncate "hello world" 8       # hello...
utils unique 3 1 3 2 1               # 3 1 2
utils chunk 2 a b c d e             # a b / c d / e
utils --version
```

## Development

Run the linter and the test suite:

```bash
.venv/bin/ruff check .
.venv/bin/pytest
```
