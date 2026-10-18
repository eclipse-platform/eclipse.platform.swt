"""Median-of-trials markdown tables from StyledTextViewportBench TSV rows (CPU only).

usage: python -B summarize.py results.tsv > summary.md
"""
import csv
import statistics
import sys
from collections import defaultdict

OPEN = ["setTextMs", "firstPaintMs", "idleDrainMs", "idleTimedOut", "scrollEndMs", "caretEndMs", "scrollTopMs",
        "uiCpuMs", "heapRetainedMiB", "heapPeakMiB", "procWorkingSetMiB", "procPrivateMiB"]
SPLIT = ["frameP50Ms", "frameP95Ms", "frameMaxMs", "paintP50Ms", "paintP95Ms", "paintMaxMs", "dispatchP95Ms",
         "dispatchMaxMs", "pageCountP50Us", "pageCountP95Us", "pageIndexP50Us", "pageIndexP95Us", "indexBuildMs",
         "indexBuildWorkerMs"]
PIXELS = ["topPrintSha", "endPrintSha"]


def median(values):
    if all(v in ("true", "false") for v in values):
        return f"{sum(v == 'true' for v in values)}/{len(values)}"
    numbers = [float(v) for v in values if v not in ("n/a", "")]
    if not numbers:
        return "n/a"
    return f"{statistics.median(numbers):.2f}"


def main():
    rows = [r for r in csv.DictReader(open(sys.argv[1], encoding="utf-8"), delimiter="\t") if r.get("sizeMiB")]
    groups = defaultdict(list)
    for r in rows:
        groups[(int(r["sizeMiB"]), r["mode"], r["label"], r["contentClass"])].append(r)
    keys = sorted(groups, key=lambda k: (k[0], k[1], k[2] != "before"))
    for title, columns in (("Open, scroll, memory", OPEN), ("UI-thread / CPU split", SPLIT), ("Control.print pixels", PIXELS)):
        print(f"\n#### {title} (median of trials)\n")
        print("| size MiB | mode | build | content | n | " + " | ".join(columns) + " |")
        print("|" + "---|" * (len(columns) + 5))
        for key in keys:
            values = groups[key]
            if title.startswith("Control"):
                cells = [",".join(sorted({v[c] for v in values})) for c in columns]
            else:
                cells = [median([v[c] for v in values]) for c in columns]
            print(f"| {key[0]} | {key[1]} | {key[2]} | {key[3]} | {len(values)} | " + " | ".join(cells) + " |")


if __name__ == "__main__":
    main()
