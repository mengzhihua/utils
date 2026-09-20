#!/usr/bin/env python3
"""Fixture tests for stage-native.py (no jpackage required)."""
from __future__ import annotations

import subprocess
import sys
import tarfile
import tempfile
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SCRIPT = ROOT / "scripts" / "stage-native.py"


def run_stage(native: Path, version: str, platform: str, arch: str) -> subprocess.CompletedProcess[str]:
    return subprocess.run(
        [sys.executable, str(SCRIPT), str(native), version, platform, arch],
        check=True,
        capture_output=True,
        text=True,
    )


def test_windows_exe() -> None:
    with tempfile.TemporaryDirectory() as raw:
        native = Path(raw)
        (native / "Utils-1.2.3.exe").write_bytes(b"mz")
        run_stage(native, "1.2.3", "windows", "x64")
        dest = native / "utils-1.2.3-windows-x64.exe"
        assert dest.is_file()
        assert dest.read_bytes() == b"mz"
        assert not (native / "Utils-1.2.3.exe").exists()


def test_linux_deb() -> None:
    with tempfile.TemporaryDirectory() as raw:
        native = Path(raw)
        (native / "utils_1.2.3-1_amd64.deb").write_bytes(b"deb")
        run_stage(native, "1.2.3", "linux", "x64")
        dest = native / "utils-1.2.3-linux-x64.deb"
        assert dest.is_file()
        assert dest.read_bytes() == b"deb"


def test_linux_portable_launcher() -> None:
    with tempfile.TemporaryDirectory() as raw:
        native = Path(raw)
        app = native / "Utils"
        (app / "bin").mkdir(parents=True)
        (app / "bin" / "Utils").write_text("#!/bin/sh\necho ok\n", encoding="utf-8")
        (app / "bin" / "Utils").chmod(0o755)
        (app / "lib").mkdir()
        (app / "lib" / "runtime.txt").write_text("jre", encoding="utf-8")
        run_stage(native, "1.2.3", "linux", "x64")
        dest = native / "utils-1.2.3-linux-x64.tar.gz"
        assert dest.is_file()
        assert not app.exists()
        with tarfile.open(dest, "r:gz") as tar:
            names = set(tar.getnames())
            assert "Utils" in names
            assert "runtime/bin/Utils" in names
            launcher = tar.extractfile("Utils")
            assert launcher is not None
            text = launcher.read().decode()
            assert "runtime/bin/Utils" in text


def test_macos_app_zip() -> None:
    with tempfile.TemporaryDirectory() as raw:
        native = Path(raw)
        app = native / "Utils.app"
        (app / "Contents" / "MacOS").mkdir(parents=True)
        (app / "Contents" / "MacOS" / "Utils").write_text("bin", encoding="utf-8")
        run_stage(native, "1.2.3", "macos", "arm64")
        dest = native / "utils-1.2.3-macos-arm64.app.zip"
        assert dest.is_file()
        assert not app.exists()
        with zipfile.ZipFile(dest) as zf:
            names = zf.namelist()
            assert any(name.startswith("Utils.app/") for name in names)
            assert any(name.endswith("Contents/MacOS/Utils") for name in names)


def main() -> int:
    test_windows_exe()
    test_linux_deb()
    test_linux_portable_launcher()
    test_macos_app_zip()
    print("test_stage_native.py ok")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
