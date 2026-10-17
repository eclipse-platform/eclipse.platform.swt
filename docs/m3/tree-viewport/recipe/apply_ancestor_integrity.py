#!/usr/bin/env python3
# SPDX-License-Identifier: EPL-2.0
"""Source-sealed, fail-closed viewport recipe. Default is check-only; --apply writes."""
from __future__ import annotations

import argparse
import hashlib
from pathlib import Path
import subprocess
import sys

ROOT = Path(__file__).resolve().parents[4]
TARGET = "bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/VirtualTreeResidentPlan.java"
PATCH = Path(__file__).with_name("ancestor_integrity.patch")
BEFORE = "d8dbd392399d671b486685cbe6b0943a7084cad8"
AFTER = "e6d2cdcac06d125c7f20f3158bf124f525443f07"


def git_blob_id(payload: bytes) -> str:
    return hashlib.sha1(b"blob " + str(len(payload)).encode("ascii") + b"\0" + payload).hexdigest()


def git(*args: str) -> subprocess.CompletedProcess[str]:
    return subprocess.run(["git", *args], cwd=ROOT, text=True, capture_output=True, check=False)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--apply", action="store_true", help="Apply only to the exact untouched source image")
    args = parser.parse_args()

    destination = ROOT / TARGET
    current = git_blob_id(destination.read_bytes())
    if current == AFTER:
        print("FIXED_POINT: already at expected postimage; no files changed")
        return 0
    if current != BEFORE:
        print(f"HOLD: unrecognized file SHA {current}; preserving all existing changes", file=sys.stderr)
        return 2

    status = git("status", "--porcelain", "--", TARGET)
    if status.returncode != 0 or status.stdout.strip():
        print("HOLD: target has pending index/worktree changes; preserving source", file=sys.stderr)
        return 2

    checked = git("apply", "--check", str(PATCH))
    if checked.returncode != 0:
        print("HOLD: patch is not applicable without conflict: " + checked.stderr, file=sys.stderr)
        return 2
    if not args.apply:
        print("READY: exact baseline and patch verified; use --apply to generate postimage")
        return 0

    applied = git("apply", str(PATCH))
    if applied.returncode != 0:
        print("HOLD: git apply failed: " + applied.stderr, file=sys.stderr)
        return 2
    produced = git_blob_id(destination.read_bytes())
    if produced != AFTER:
        print(f"HOLD: unexpected generated image {produced}; investigate before proceeding", file=sys.stderr)
        return 2
    print("GENERATED: expected postimage verified; run the relevant JUnit/GUI acceptance gates")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
