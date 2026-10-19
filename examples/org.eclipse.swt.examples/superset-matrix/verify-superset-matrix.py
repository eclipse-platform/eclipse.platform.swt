#!/usr/bin/env python3
# SPDX-License-Identifier: EPL-2.0
"""OpenCV acceptance oracle for Superset Viewport Test Matrix screenshots."""
from __future__ import annotations

import csv
import hashlib
import json
from pathlib import Path
import sys
import tempfile

import cv2
import numpy as np


def require(condition: bool, message: str) -> None:
    if not condition:
        raise AssertionError(message)


def props(path: Path) -> dict[str, str]:
    require(path.is_file(), "Missing sidecar: " + str(path))
    result: dict[str, str] = {}
    for line in path.read_text(encoding="utf-8").splitlines():
        if "=" in line:
            key, value = line.split("=", 1)
            result[key] = value
    return result


def read_png(path: Path, expected_hash: str, label: str) -> np.ndarray:
    require(path.is_file(), "Missing PNG: " + str(path))
    actual = hashlib.sha256(path.read_bytes()).hexdigest()
    require(actual == expected_hash, "PNG hash mismatch: " + label)
    image = cv2.imread(str(path), cv2.IMREAD_COLOR)
    require(image is not None and image.size > 0, "PNG decode failed: " + label)
    require(image.shape[0] > 8 and image.shape[1] > 8, "PNG too small: " + label)
    interior = image[4:-4, 4:-4]
    require(interior.size > 0, "No interior pixels: " + label)
    edges = int(np.count_nonzero(cv2.Canny(interior, 60, 140)))
    variance = float(np.var(interior))
    require(edges > 8 or variance > 8.0, "Blank/unrendered capture: " + label)
    return image


def shot(root: Path, rel: str) -> np.ndarray:
    png = root / rel
    sidecar = png.with_suffix(".properties")
    meta = props(sidecar)
    require(meta.get("mode") == "print", "Unexpected snapshot mode: " + rel)
    require(meta.get("png") == png.name, "Snapshot filename mismatch: " + rel)
    return read_png(png, meta["sha256"], rel)


def final_pair(root: Path, rel_top: str, capture_mode: str) -> tuple[np.ndarray, np.ndarray | None]:
    top = root / rel_top
    name = top.name
    require("_top.png" in name, "Unexpected top screenshot name: " + name)
    stem = name[:-len("_top.png")] + "_final"
    sidecar = props(top.parent / (stem + "-capture.properties"))
    print_name = sidecar["print"]
    print_image = read_png(top.parent / print_name, sidecar["print.sha256"], stem + ":print")
    display_name = sidecar["display"]
    display_image = None
    if capture_mode == "print+display":
        require(display_name != "-", "Matrix promised display capture but sidecar omitted it: " + stem)
        display_image = read_png(
            top.parent / display_name, sidecar["display.sha256"], stem + ":display")
        require(print_image.shape == display_image.shape,
                "Print/display dimensions differ: " + stem)
    elif display_name != "-":
        display_image = read_png(
            top.parent / display_name, sidecar["display.sha256"], stem + ":display")
    return print_image, display_image


def number(value: str) -> int | None:
    try:
        return int(value)
    except (TypeError, ValueError):
        return None


def verify(root: Path) -> dict:
    matrix = root / "VIEWPORT_TEST_MATRIX.tsv"
    require(matrix.is_file(), "Missing VIEWPORT_TEST_MATRIX.tsv")
    receipt_path = root / "opencv-matrix-validation.json"
    receipt_path.unlink(missing_ok=True)

    with matrix.open(newline="", encoding="utf-8") as handle:
        rows = list(csv.DictReader(handle, delimiter="\t"))

    require(rows, "Matrix has no data rows")
    seen: set[str] = set()
    checked = changed = paired = 0
    failures: list[str] = []

    for row in rows:
        if row.get("result") != "PASS":
            continue
        top_rel = row.get("screenshot_top", "-")
        end_rel = row.get("screenshot_end", "-")
        if top_rel == "-" or end_rel == "-":
            failures.append(row.get("concept", "?") + ": PASS row missing top/end screenshot")
            continue
        for rel in (top_rel, end_rel):
            require(rel not in seen, "Duplicate screenshot artifact: " + rel)
            seen.add(rel)

        top = shot(root, top_rel)
        end = shot(root, end_rel)
        checked += 2

        p, d = final_pair(root, top_rel, row.get("capture_mode", "-"))
        checked += 1 + (1 if d is not None else 0)
        if d is not None:
            paired += 1

        scale = number(row.get("scale", "-"))
        bound = number(row.get("bound", "-"))
        materialized = number(row.get("materialized", "-"))
        should_move = (
            scale is not None and bound is not None and materialized is not None
            and scale > max(32, bound * 2)
        )
        if top.shape == end.shape:
            delta = int(np.count_nonzero(cv2.absdiff(top, end)))
            if should_move:
                require(delta > 20,
                        "Stale top/end viewport pixels: "
                        + row.get("concept", "?") + " / " + row.get("widget_class", "?"))
                changed += 1

    require(not failures, "; ".join(failures))
    require(checked > 0, "No PASS screenshots were qualified")
    receipt = {
        "status": "PASS",
        "matrixRows": len(rows),
        "pngsQualified": checked,
        "printDisplayPairs": paired,
        "largeViewportPairsChanged": changed,
        "opencv": cv2.__version__,
        "numpy": np.__version__,
        "manualQaRequired": False,
        "scope": "Superset matrix persisted SWT/Nebula captures; native behavior remains owned by matrix/JUnit/resource gates",
    }
    receipt_path.write_text(json.dumps(receipt, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(receipt, indent=2))
    return receipt


def selftest() -> None:
    with tempfile.TemporaryDirectory(prefix="superset-opencv-") as tmp:
        root = Path(tmp)
        good = np.full((96, 128, 3), 240, dtype=np.uint8)
        moved = good.copy()
        cv2.rectangle(good, (12, 12), (92, 64), (0, 0, 0), 2)
        cv2.rectangle(moved, (24, 14), (114, 68), (0, 0, 0), 2)
        blank = np.zeros_like(good)
        for name, image in (("good", good), ("moved", moved), ("blank", blank)):
            require(cv2.imwrite(str(root / (name + ".png")), image), "Synthetic PNG write: " + name)

        def evidence(name: str) -> str:
            path = root / (name + ".png")
            return hashlib.sha256(path.read_bytes()).hexdigest()

        read_png(root / "good.png", evidence("good"), "good")
        read_png(root / "moved.png", evidence("moved"), "moved")
        require(np.count_nonzero(cv2.absdiff(good, moved)) > 20, "Synthetic frame-change oracle")

        def reject(label, fn):
            try:
                fn()
            except AssertionError:
                return
            raise AssertionError("Synthetic defect admitted: " + label)

        reject("forged-hash", lambda: read_png(root / "good.png", "0" * 64, "forged"))
        reject("blank", lambda: read_png(root / "blank.png", evidence("blank"), "blank"))
        reject("stale", lambda: require(
            np.count_nonzero(cv2.absdiff(good, good)) > 20, "stale"))
    print("SUPERSET_OPENCV_SELFTEST_PASS: valid frames accepted; forged/blank/stale defects rejected")


if __name__ == "__main__":
    if len(sys.argv) == 2 and sys.argv[1] == "--selftest":
        selftest()
    else:
        require(len(sys.argv) == 2,
                "Usage: verify-superset-matrix.py MATRIX_RUN_DIR | --selftest")
        verify(Path(sys.argv[1]))
