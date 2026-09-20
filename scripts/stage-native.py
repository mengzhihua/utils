#!/usr/bin/env python3
"""Turn jpackage output into one arch-tagged, double-clickable artifact per OS."""
from __future__ import annotations

import argparse
import shutil
import stat
import subprocess
import sys
import tarfile
from pathlib import Path


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser()
    parser.add_argument("native_dir")
    parser.add_argument("version")
    parser.add_argument("platform")
    parser.add_argument("arch")
    return parser.parse_args()


def first_match(root: Path, *patterns: str) -> Path | None:
    for pattern in patterns:
        matches = sorted(
            path for path in root.glob(pattern)
            if path.is_file() or path.is_dir()
        )
        if matches:
            return matches[0]
    return None


def rename_file(src: Path, dest: Path) -> Path:
    if dest.exists() and dest.resolve() != src.resolve():
        dest.unlink()
    if src.resolve() != dest.resolve():
        src.rename(dest)
    return dest


def zip_directory(folder: Path, dest_zip: Path) -> Path:
    if dest_zip.exists():
        dest_zip.unlink()
    ditto = shutil.which("ditto")
    if ditto:
        subprocess.run(
            [ditto, "-c", "-k", "--keepParent", str(folder), str(dest_zip)],
            check=True,
        )
        return dest_zip
    archive_base = str(dest_zip.with_suffix("")) if dest_zip.suffix == ".zip" else str(dest_zip)
    produced = Path(shutil.make_archive(archive_base, "zip", root_dir=folder.parent, base_dir=folder.name))
    if produced.resolve() != dest_zip.resolve():
        produced.rename(dest_zip)
    return dest_zip


def make_macos_dmg(app: Path, dest: Path) -> Path | None:
    hdiutil = shutil.which("hdiutil")
    if not hdiutil:
        return None
    if dest.exists():
        dest.unlink()
    subprocess.run(
        [
            hdiutil,
            "create",
            "-volname",
            "Utils",
            "-srcfolder",
            str(app),
            "-ov",
            "-format",
            "UDZO",
            str(dest),
        ],
        check=True,
    )
    return dest


def write_launcher(path: Path, target: str) -> None:
    path.write_text(
        "#!/bin/sh\n"
        "HERE=$(CDPATH= cd -- \"$(dirname -- \"$0\")\" && pwd)\n"
        f"exec \"$HERE/{target}\" \"$@\"\n",
        encoding="utf-8",
    )
    path.chmod(path.stat().st_mode | stat.S_IXUSR | stat.S_IXGRP | stat.S_IXOTH)


def stage_linux_portable(root: Path, app_dir: Path, dest: Path) -> Path:
    if dest.exists():
        dest.unlink()
    staging = root / f".portable-{dest.stem}"
    if staging.exists():
        shutil.rmtree(staging)
    staging.mkdir()
    runtime = staging / "runtime"
    shutil.copytree(app_dir, runtime, symlinks=True)
    write_launcher(staging / "Utils", "runtime/bin/Utils")
    (staging / "README.txt").write_text(
        "解压后在本目录执行 ./Utils\n"
        "浏览器会打开本机办公工作台。\n",
        encoding="utf-8",
    )
    with tarfile.open(dest, "w:gz") as tar:
        tar.add(staging / "Utils", arcname="Utils")
        tar.add(staging / "README.txt", arcname="README.txt")
        tar.add(runtime, arcname="runtime")
    shutil.rmtree(staging)
    return dest


def main() -> int:
    args = parse_args()
    root = Path(args.native_dir).resolve()
    if not root.is_dir():
        print(f"native dir not found: {root}", file=sys.stderr)
        return 1

    stem = f"utils-{args.version}-{args.platform}-{args.arch}"
    created: list[Path] = []

    if args.platform == "macos":
        app = first_match(root, "Utils.app")
        if app and app.is_dir():
            created.append(zip_directory(app, root / f"{stem}.app.zip"))
            dmg = make_macos_dmg(app, root / f"{stem}.dmg")
            if dmg:
                created.append(dmg)
            shutil.rmtree(app)
        installer = first_match(root, "Utils*.dmg", "Utils*.pkg")
        if installer and installer.is_file():
            created.append(rename_file(installer, root / f"{stem}{installer.suffix.lower()}"))

    if args.platform == "windows":
        installer = first_match(root, "Utils*.exe", "Utils*.msi")
        if installer and installer.is_file():
            created.append(rename_file(installer, root / f"{stem}{installer.suffix.lower()}"))

    if args.platform == "linux":
        installer = first_match(root, "utils*.deb", "Utils*.deb", "utils*.rpm", "Utils*.rpm")
        if installer and installer.is_file():
            created.append(rename_file(installer, root / f"{stem}{installer.suffix.lower()}"))

    app_dir = root / "Utils"
    if app_dir.is_dir():
        if args.platform == "linux":
            created.append(stage_linux_portable(root, app_dir, root / f"{stem}.tar.gz"))
        else:
            created.append(zip_directory(app_dir, root / f"{stem}.zip"))
        shutil.rmtree(app_dir)

    if not created:
        print(f"no jpackage output in {root}", file=sys.stderr)
        return 1

    for path in created:
        print(path)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
