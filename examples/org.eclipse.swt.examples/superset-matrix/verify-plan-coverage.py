#!/usr/bin/env python3
# SPDX-License-Identifier: EPL-2.0
"""Fail-closed plan-to-result coverage oracle for the SWT/Nebula viewport matrix.

Structural completeness is independent of the OpenCV pixel oracle. A partial
run may be recorded for diagnosis, but must never be represented as complete.
"""
from __future__ import annotations

import argparse
import csv
import hashlib
import json
from pathlib import Path
import tempfile

KEY_COLUMNS = ("concept", "widget_class", "scale")
REQUIRED_COLUMNS = (*KEY_COLUMNS, "result")


def require(ok: bool, explanation: str) -> None:
    if not ok:
        raise AssertionError(explanation)


def key(row: dict[str, str]) -> tuple[str, str, str]:
    return tuple(row[column] for column in KEY_COLUMNS)


def digest(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def planned(path: Path) -> tuple[set[tuple[str, str, str]], dict[tuple[str, str, str], str]]:
    require(path.is_file(), "Missing plan.txt")
    run: set[tuple[str, str, str]] = set()
    static: dict[tuple[str, str, str], str] = {}
    for line_no, line in enumerate(path.read_text(encoding="utf-8").splitlines(), 1):
        if not line:
            continue
        cells = line.split("\t")
        tag = cells[0]
        if tag == "PLAN":
            require(len(cells) >= 5, f"Malformed PLAN at line {line_no}")
            k = (cells[1], cells[2], "-" if cells[3] == "0" else cells[3])
            require(all(k), f"Empty planned coordinate at line {line_no}")
            require(k not in run and k not in static, f"Duplicate planned coordinate: {k}")
            run.add(k)
        elif tag == "ROW":
            require(len(cells) >= 19, f"Malformed static ROW at line {line_no}")
            k = (cells[1], cells[2], cells[3])
            require(all(k), f"Empty static coordinate at line {line_no}")
            require(k not in run and k not in static, f"Duplicate planned coordinate: {k}")
            expected = cells[18]
            require(expected in {"NOT_RUN", "NOT_IMPLEMENTED"},
                    f"Static ROW cannot masquerade as executed: {k} = {expected}")
            static[k] = expected
        else:
            raise AssertionError(f"Unexpected plan tag at line {line_no}: {tag!r}")
    require(run or static, "No planned matrix coordinates")
    return run, static


def verify(root: Path, strict: bool = False) -> dict[str, object]:
    plan_file = root / "plan.txt"
    matrix_file = root / "VIEWPORT_TEST_MATRIX.tsv"
    receipt_file = root / "matrix-coverage-validation.json"
    receipt_file.unlink(missing_ok=True)  # a failed recheck cannot retain yesterday's PASS
    run, static = planned(plan_file)
    require(matrix_file.is_file(), "Missing VIEWPORT_TEST_MATRIX.tsv")
    with matrix_file.open(newline="", encoding="utf-8") as stream:
        reader = csv.DictReader(stream, delimiter="\t")
        require(reader.fieldnames is not None, "Matrix header missing")
        require(all(c in reader.fieldnames for c in REQUIRED_COLUMNS), "Matrix columns missing")
        require(len(reader.fieldnames) == len(set(reader.fieldnames)), "Duplicate matrix columns")
        result: dict[tuple[str, str, str], str] = {}
        scaling_failures: set[tuple[str, str, str]] = set()
        for index, row in enumerate(reader, 2):
            require(None not in row and all(v is not None for v in row.values()),
                    f"Malformed TSV row {index}")
            k = key(row)
            require(all(k), f"Empty matrix coordinate at line {index}")
            require(k not in result, f"Duplicate matrix coordinate: {k}")
            require(k in run or k in static, f"Unplanned matrix coordinate: {k}")
            state = row["result"]
            require(state in {"PASS", "FAIL", "NOT_RUN", "NOT_IMPLEMENTED"},
                    f"Unknown result for {k}: {state}")
            if k in static:
                require(state == static[k], f"Static plan/result disagreement at {k}")
            result[k] = state
            if k in run and row.get("size_independence", "").startswith("FAIL"):
                scaling_failures.add(k)
    missing = (run | set(static)) - result.keys()
    require(not missing, f"Missing planned matrix coordinates: {sorted(missing)[:8]}")
    counts = {s: sum(result[k] == s for k in run)
              for s in ("PASS", "FAIL", "NOT_RUN", "NOT_IMPLEMENTED")}
    complete = bool(run) and counts["PASS"] == len(run) and not scaling_failures
    receipt: dict[str, object] = {
        "status": "COMPLETE" if complete else "INCOMPLETE",
        "planSha256": digest(plan_file),
        "matrixSha256": digest(matrix_file),
        "plannedRunnable": len(run),
        "plannedStatic": len(static),
        "matrixRows": len(result),
        "runnableResults": counts,
        "sizeIndependenceFailures": len(scaling_failures),
        "scope": "Matrix plan/result completeness only; native and pixel correctness require separate gates",
    }
    receipt_file.write_text(json.dumps(receipt, indent=2, sort_keys=True) + "\n", encoding="utf-8")
    require(not strict or complete,
            f"STRICT viewport acceptance refused: {len(run) - counts['PASS']} runnable cells not PASS; "
            f"{len(scaling_failures)} scaling regressions")
    return receipt


def selftest() -> None:
    header = "concept\twidget_class\tscale\tresult\tnotes\n"
    plan = "PLAN\ttable\tSWT.Table\t1000\t256\nPLAN\tcanvas\tSWT.Canvas\t0\t256\n"
    plan += "ROW\tunused\tUnsupported\t-" + "\t-" * 14 + "\tNOT_IMPLEMENTED\t-\n"
    rows = ["table\tSWT.Table\t1000\tPASS\t-",
            "canvas\tSWT.Canvas\t-\tPASS\t-",
            "unused\tUnsupported\t-\tNOT_IMPLEMENTED\t-"]
    with tempfile.TemporaryDirectory(prefix="swt-viewport-coverage-") as temp:
        root = Path(temp)
        (root / "plan.txt").write_text(plan, encoding="utf-8")
        matrix = root / "VIEWPORT_TEST_MATRIX.tsv"
        def write(values: list[str]) -> None:
            matrix.write_text(header + "\n".join(values) + "\n", encoding="utf-8")
        def rejected(label: str, func) -> None:
            try:
                func()
            except AssertionError:
                return
            raise AssertionError("Defect admitted: " + label)
        write(rows)
        assert verify(root, strict=True)["status"] == "COMPLETE"
        for state in ("FAIL", "NOT_RUN", "NOT_IMPLEMENTED"):
            write([rows[0].replace("PASS", state), *rows[1:]])
            assert verify(root)["status"] == "INCOMPLETE"
            rejected("incomplete:" + state, lambda: verify(root, strict=True))
        write(rows[:1] + rows[2:])
        rejected("missing", lambda: verify(root))
        assert not (root / "matrix-coverage-validation.json").exists()
        write(rows + rows[:1])
        rejected("duplicate", lambda: verify(root))
        write(rows + ["invented\tWidget\t1000\tPASS\t-"])
        rejected("unplanned", lambda: verify(root))
        write([*rows[:2], rows[2].replace("NOT_IMPLEMENTED", "PASS")])
        rejected("static fake pass", lambda: verify(root))
        write(rows)
        # A cell may say PASS before post-processing reveals scaling regression.
        matrix.write_text(header.replace("notes\n", "notes\tsize_independence\n")
                          + "\n".join(r + ("\tFAIL max_ratio=1.50" if i == 0 else "\t-")
                                      for i, r in enumerate(rows)) + "\n", encoding="utf-8")
        assert verify(root)["sizeIndependenceFailures"] == 1
        rejected("scaling regression", lambda: verify(root, strict=True))
        write(rows)
        (root / "plan.txt").write_text(plan + "PLAN\ttable\tSWT.Table\t1000\t256\n", encoding="utf-8")
        rejected("duplicate plan", lambda: verify(root))
        (root / "plan.txt").write_text("ROW\tunused\tUnsupported\t-" + "\t-" * 14
                                           + "\tNOT_IMPLEMENTED\t-\n", encoding="utf-8")
        write(rows[2:])
        assert verify(root)["status"] == "INCOMPLETE"
        rejected("no executed cells", lambda: verify(root, strict=True))
    print("M3_VIEWPORT_COVERAGE_SELFTEST_PASS: complete accepted; partial/missing/duplicate/fake refused")


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("root", nargs="?", type=Path)
    parser.add_argument("--strict", action="store_true")
    parser.add_argument("--selftest", action="store_true")
    args = parser.parse_args()
    if args.selftest:
        selftest()
    else:
        require(args.root is not None, "Provide a matrix output directory")
        print(json.dumps(verify(args.root, strict=args.strict), indent=2))


if __name__ == "__main__":
    main()
