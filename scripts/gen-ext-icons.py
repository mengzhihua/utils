#!/usr/bin/env python3
"""Generate Chrome extension PNG icons from the existing U-logo design."""
import struct
import zlib
from pathlib import Path

SRC = 256
RADIUS = 52


def source_pixel(x, y):
    dx = min(x, SRC - 1 - x)
    dy = min(y, SRC - 1 - y)
    if dx < 10 or dy < 10:
        if (dx - 10) ** 2 + (dy - 10) ** 2 > RADIUS ** 2 and (dx < 10 and dy < 10):
            return 0, 0, 0, 0
    if x < 14 or y < 14 or x >= SRC - 14 or y >= SRC - 14:
        return 15, 118, 110, 255
    cx, cy = SRC // 2, SRC // 2 + 8
    in_u = False
    if 78 <= x <= 104 and 70 <= y <= 176:
        in_u = True
    if 152 <= x <= 178 and 70 <= y <= 176:
        in_u = True
    if 78 <= x <= 178 and 150 <= y <= 186:
        in_u = True
    if in_u:
        return 255, 253, 248, 255
    return 21, 94, 117, 255


def write_png(path: Path, size: int):
    raw = bytearray()
    for y in range(size):
        raw.append(0)
        sy = min(SRC - 1, (y * SRC) // size)
        for x in range(size):
            sx = min(SRC - 1, (x * SRC) // size)
            raw.extend(source_pixel(sx, sy))

    def chunk(tag, data):
        return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

    png = b"\x89PNG\r\n\x1a\n"
    png += chunk(b"IHDR", struct.pack(">IIBBBBB", size, size, 8, 6, 0, 0, 0))
    png += chunk(b"IDAT", zlib.compress(bytes(raw), 9))
    png += chunk(b"IEND", b"")
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(png)


if __name__ == "__main__":
    out_dir = Path(__file__).resolve().parents[1] / "extension" / "icons"
    for size in (16, 48, 128):
        dest = out_dir / f"icon{size}.png"
        write_png(dest, size)
        print(dest, dest.stat().st_size)
