"""Aggregate VisibilityGateBench rows: median per (scenario, mode) plus parity pass counts.

Columns (1-based) of a bench row: 1 scenario, 2 mode, 3 updates, 4-7 update paints/layouts/measures/setData,
8 update cpu ms, 9 update wall ms, 10-13 query counters, 14-17 catch-up counters, 18 catch-up cpu ms,
19 apiHiddenEqTwin, 20 apiShownEqTwin, 21 apiHiddenHash, 22 screenEq, 23 rawEqFresh, 24-25 hashes,
26 occluded shells after updates, 27 deferred-list size, 28 occluded at capture, 29 maskedEq, 30 maskedRawEqFresh.
"""
import statistics
import sys

rows = {}
for line in open(sys.argv[1], encoding="utf-8"):
    cells = line.rstrip("\n").split("\t")
    if len(cells) < 25 or cells[0].startswith("API"):
        continue
    rows.setdefault((cells[0], cells[1]), []).append(cells)

def med(group, col):
    return int(statistics.median(int(r[col - 1]) for r in group))

def count(group, col):
    return sum(1 for r in group if len(r) >= col and r[col - 1] == "true")

print("\t".join(["scenario", "mode", "runs", "upd_paints", "upd_layouts", "upd_measures", "upd_setdata", "upd_cpu_ms_p50",
                 "catch_paints", "catch_layouts", "catch_setdata", "api_shown_eq", "screen_eq", "raw_eq_fresh",
                 "masked_eq", "masked_raw_eq_fresh", "hidden_api_hash"]))
order = ["tab", "scrolled", "minimized", "covered", "stack"]
for scenario in order:
    for mode in ["before", "after", "after-off"]:
        g = rows.get((scenario, mode))
        if not g:
            continue
        n = len(g)
        print("\t".join(map(str, [scenario, mode, n, med(g, 4), med(g, 5), med(g, 6), med(g, 7), med(g, 8),
                                  med(g, 14), med(g, 15), med(g, 17),
                                  f"{count(g, 20)}/{n}", f"{count(g, 22)}/{n}", f"{count(g, 23)}/{n}",
                                  f"{count(g, 29)}/{n}", f"{count(g, 30)}/{n}", ",".join(sorted({r[20] for r in g}))])))
