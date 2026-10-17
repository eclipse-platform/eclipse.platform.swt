# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
"""Markdown table of benchmark TSVs: min / median ms (allocated KB) per widget x operation, one column per mode.

Usage: python -I bench_table.py <tsv>... ; later files override earlier rows of the same widget, mode and operation.
"""
import csv, sys

MODES = ["official", "original", "viewport", "custom", "skija", "gpu"]
rows = {}
for path in sys.argv[1:]:
    with open(path, encoding="utf-8", newline="") as f:
        for r in csv.DictReader(f, delimiter="\t"):
            rows[(r["widget"], r["operation"], r["mode"])] = r

def cell(r):
    if r is None:
        return "not run"
    if r["status"] == "ok":
        return "%s / %s (%d KB)" % (r["min_ms"], r["median_ms"], round(int(r["alloc_bytes_median"]) / 1024))
    if r["status"].startswith("skipped"):
        return "-"
    return r["status"].split(" (")[0]

modes = [m for m in MODES if any(k[2] == m for k in rows)]
print("| widget | operation | " + " | ".join(modes) + " |")
print("|---|---|" + "---|" * len(modes))
seen = []
for (w, op, m) in rows:
    if (w, op) not in seen:
        seen.append((w, op))
for w, op in seen:
    if all(rows.get((w, op, m), {}).get("status", "").startswith("skipped") for m in modes if (w, op, m) in rows):
        continue
    print("| %s | %s | %s |" % (w, op, " | ".join(cell(rows.get((w, op, m))) for m in modes)))
