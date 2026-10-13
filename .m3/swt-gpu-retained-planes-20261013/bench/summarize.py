"""Summarize retained-plane evidence TSVs into Markdown tables (CPU only, deterministic).

usage: python -B summarize.py <frames.tsv> [graphics.tsv] [graphics.diff] [probe.log]
"""
import sys
from collections import OrderedDict


def rows(path):
    lines = [l.rstrip("\n") for l in open(path, encoding="utf-8") if l.strip()]
    header = None
    out = []
    for line in lines:
        cells = line.split("\t")
        if cells[0] == "mode":
            header = cells
            continue
        if header and len(cells) == len(header):
            out.append(dict(zip(header, cells)))
    return out


def frames_table(path):
    data = rows(path)
    keyed = OrderedDict()
    for r in data:
        keyed.setdefault((r["widget"], r["scenario"]), {})[r["mode"]] = r
    print("| widget | scenario | stock GDI p50 / p95 / max ms | retained GDI p50 / p95 / max ms | Skija GPU p50 / p95 / max ms |")
    print("| --- | --- | --- | --- | --- |")
    for (w, s), modes in keyed.items():
        cells = []
        for m in ("off", "gdi", "skija"):
            r = modes.get(m)
            cells.append("%s / %s / %s" % (r["p50ms"], r["p95ms"], r["maxms"]) if r else "NOT_RUN")
        print("| %s | %s | %s |" % (w, s, " | ".join(cells)))
    print()
    print("| widget | scenario | mode | CPU ms/frame | alloc B/frame | GPU wait ms/frame | pen hit % | brush hit % | font hit % | paint hit % | blob hit % | tiles reused | tiles rastered | GPU-rastered tiles | record fails | GPU bytes | CPU tile bytes | focus |")
    print("| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |")
    for (w, s), modes in keyed.items():
        for m in ("off", "gdi", "skija"):
            r = modes.get(m)
            if not r:
                continue
            print("| %s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s | %s |" % (
                w, s, m, r["cpuMsMean"], r["allocBytesMean"], r["gpuWaitMsMean"], r["penHit%"], r["brushHit%"],
                r["fontHit%"], r["paintHit%"], r["blobHit%"], r["tileReused"], r["tileRaster"], r["gpuRasterTiles"],
                r.get("recordFails", "-"), r["gpuBytes"], r["cpuTileBytes"], r["focus"]))
    print()


def graphics_table(path):
    print("| mode | GraphicsExample tab | frames | p50 ms | p95 ms | max ms | alloc B/frame |")
    print("| --- | --- | --- | --- | --- | --- | --- |")
    for line in open(path, encoding="utf-8"):
        c = line.rstrip("\n").split("\t")
        if len(c) >= 7:
            print("| %s | %s | %s | %s | %s | %s | %s |" % tuple(c[:7]))
    print()


def diff_table(path):
    print("| capture vs stock GDI | pixels | exactly equal | max channel delta | > 8 | > 32 | > 64 | over tolerance 48 |")
    print("| --- | --- | --- | --- | --- | --- | --- | --- |")
    for line in open(path, encoding="utf-8"):
        c = line.rstrip("\n").split("\t")
        if len(c) == 11 and c[1].isdigit():
            print("| %s | %s | %s (%s) | %s | %s | %s | %s | %s (%s) |" % (c[0], c[1], c[2], c[3], c[4], c[5], c[6], c[7], c[9], c[10]))
    print()


if __name__ == "__main__":
    frames_table(sys.argv[1])
    if len(sys.argv) > 2:
        graphics_table(sys.argv[2])
    for extra in sys.argv[3:]:
        diff_table(extra)
