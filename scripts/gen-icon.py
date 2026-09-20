#!/usr/bin/env python3
"""Generate a simple PNG app icon without extra dependencies."""
import struct
import zlib
from pathlib import Path

SIZE = 256
RADIUS = 52


def pixel(x, y):
    dx = min(x, SIZE - 1 - x)
    dy = min(y, SIZE - 1 - y)
    if dx < 10 or dy < 10:
        if (dx - 10) ** 2 + (dy - 10) ** 2 > RADIUS ** 2 and (dx < 10 and dy < 10):
            return 0, 0, 0, 0
    if x < 14 or y < 14 or x >= SIZE - 14 or y >= SIZE - 14:
        return 15, 118, 110, 255
    # letter U
    cx, cy = SIZE // 2, SIZE // 2 + 8
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


def write_png(path: Path):
    raw = b"".join(
        b"\x00" + bytes(ch for x in range(SIZE) for ch in pixel(x, y))
        for y in range(SIZE)
    )
    def chunk(tag, data):
        return struct.pack(">I", len(data)) + tag + data + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)

    png = b"\x89PNG\r\n\x1a\n"
    png += chunk(b"IHDR", struct.pack(">IIBBBBB", SIZE, SIZE, 8, 6, 0, 0, 0))
    png += chunk(b"IDAT", zlib.compress(raw, 9))
    png += chunk(b"IEND", b"")
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(png)


if __name__ == "__main__":
    out = Path(__file__).resolve().parents[1] / "packaging" / "icon.png"
    write_png(out)
    print(out)
