#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Hitesh Soliwal and Contributors to the Synexia Project
# SPDX-License-Identifier: Apache-2.0
"""Thin receiving adapter for qualified OpenRewrite Results; no template execution engine."""

import argparse
import hashlib
import os
from pathlib import Path
import stat
import tempfile
import xml.etree.ElementTree as ET


def digest(data):
    return hashlib.sha256(data).hexdigest()


def rows(path, width):
    result = []
    for line in path.read_text(encoding="utf-8").splitlines():
        if not line or line.startswith("#"):
            continue
        cells = line.split("\t")
        if len(cells) != width:
            raise ValueError("invalid receipt row: " + str(path))
        result.append(cells)
    if not result or len({r[0] for r in result}) != len(result):
        raise ValueError("empty or duplicate receipt rows: " + str(path))
    return result


def contained(root, relative):
    if not relative or Path(relative).is_absolute() or ".." in Path(relative).parts:
        raise ValueError("invalid receiving path: " + relative)
    path = root / relative
    if not path.resolve().is_relative_to(root.resolve()):
        raise ValueError("receiving path escapes root: " + relative)
    cursor = path
    while cursor != root:
        if cursor.is_symlink():
            raise ValueError("symlink target or parent is not admitted: " + relative)
        cursor = cursor.parent
    return path


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--target", required=True, type=Path)
    parser.add_argument("--crate", type=Path, default=Path(__file__).resolve().parent)
    parser.add_argument("--recipe-owner", type=Path)
    parser.add_argument("--check", action="store_true", help="admit and report, without writing")
    args = parser.parse_args()
    crate = args.crate.resolve()
    target = args.target.resolve()
    if not target.is_dir():
        raise ValueError("target root is absent")
    owner = args.recipe_owner or (crate / "sdk-owner" if (crate / "sdk-owner").is_dir() else crate / "../..")
    owner = owner.resolve()
    candidate = crate / "target/candidate"
    report = crate / "target/surefire-reports/TEST-com.synexia.rewrite.M3TaskRecipeTest.xml"
    suite = ET.parse(report).getroot()
    if int(suite.attrib.get("tests", "0")) < 5 or any(
            int(suite.attrib.get(key, "0")) for key in ("failures", "errors", "skipped")):
        raise ValueError("all actual SDK qualification tests must execute successfully")
    bound = dict(rows(candidate / "SDK_INPUTS.tsv", 2))
    expected_paths = {"crate/pom.xml", "crate/receive.py"}
    for directory in (crate / "src", crate / "contract"):
        if directory.exists():
            expected_paths.update("crate/" + p.relative_to(crate).as_posix()
                                  for p in directory.rglob("*") if p.is_file())
    expected_paths.update("owner/src/main/java/com/synexia/rewrite/" + name for name in
                         ("M3EditScope.java", "M3HashPinnedJavaSnapshotRecipe.java",
                          "M3HashPinnedTextSnapshotRecipe.java", "M3ScopeInference.java"))
    if set(bound) != expected_paths:
        raise ValueError("incomplete or extra SDK source binding")
    for path, expected in bound.items():
        domain, relative = path.split("/", 1)
        if domain not in ("crate", "owner"):
            raise ValueError("unknown qualification input domain")
        actual = contained(crate if domain == "crate" else owner, relative).read_bytes()
        if digest(actual) != expected:
            raise ValueError("recipe changed after SDK qualification: " + path)

    manifests = {}
    for manifest in (crate / "src/main/resources/com/synexia/rewrite").glob("hash-pinned-*/*/manifest.tsv"):
        for path, before, after, resource in rows(manifest, 4):
            if path in manifests:
                raise ValueError("duplicate Java/text target: " + path)
            if digest((manifest.parent / resource).read_bytes()) != after:
                raise ValueError("template changed: " + path)
            manifests[path] = (before, after)
    actual_rows = rows(candidate / "SDK_RESULTS.tsv", 3)
    if {r[0] for r in actual_rows} != set(manifests):
        raise ValueError("SDK result target set differs from qualified manifests")

    pending = []
    for path, before, after in sorted(actual_rows):
        if (before, after) != manifests[path]:
            raise ValueError("SDK result manifest mismatch: " + path)
        output = contained(candidate, path).read_bytes()
        if digest(output) != after:
            raise ValueError("SDK result changed after qualification: " + path)
        receiving = contained(target, path)
        current = digest(receiving.read_bytes()) if receiving.exists() else "ABSENT"
        if current not in (before, after):
            raise ValueError("target source drift: " + path)
        if current != after:
            pending.append((receiving, output))

    # All files are admitted before the first write. Each file is replaced serially.
    if not args.check:
        for path, data in pending:
            path.parent.mkdir(parents=True, exist_ok=True)
            mode = stat.S_IMODE(path.stat().st_mode) if path.exists() else 0o644
            descriptor, temporary = tempfile.mkstemp(prefix=".m3-recipe-", dir=path.parent)
            try:
                with os.fdopen(descriptor, "wb") as stream:
                    stream.write(data)
                os.chmod(temporary, mode)
                os.replace(temporary, path)
            finally:
                if os.path.exists(temporary):
                    os.unlink(temporary)
    print("M3_SDK_RESULTS_RECEIVER_PASS mode=" + ("check" if args.check else "apply")
          + " targets=" + str(len(actual_rows)) + " changes=" + str(len(pending)))


if __name__ == "__main__":
    main()
