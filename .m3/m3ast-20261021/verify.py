#!/usr/bin/env python3
# SPDX-FileCopyrightText: 2026 Hitesh Soliwal and contributors
# SPDX-License-Identifier: Apache-2.0
"""Read-only source custody for an original Eclipse Maven/Tycho build.

Never parses Java, changes source, activates a recipe or promotes M3AST.
The original Maven/Tycho/ECJ/SWT/EMF compiler remains authoritative.
"""
from __future__ import annotations

import hashlib
import pathlib
import re
import sys
import xml.etree.ElementTree as ET

PROFILE = "m3-ast-source-audit-20261021"
SCHEMA = "M3_ECLIPSE_BUILD_SOURCE_GATE_V1"
KEYS = ("schema", "repository", "source_ref", "root_path", "root_sha256",
        "original_build", "canonical_recipe", "canonical_commit", "status")


def fingerprint(path: pathlib.Path) -> str:
    if path.is_symlink() or not path.is_file():
        raise ValueError("missing or symbolic root source: " + str(path))
    return hashlib.sha256(path.read_bytes()).hexdigest()


def main() -> int:
    root = pathlib.Path(__file__).resolve().parents[2]
    ledger = pathlib.Path(__file__).resolve().with_name("RECEIVER.tsv")
    try:
        rows = [line.split("\t", 1) for line in ledger.read_text(encoding="utf-8").splitlines()]
        if len(rows) != len(KEYS) or any(len(row) != 2 for row in rows):
            raise ValueError("incomplete source receiver metadata")
        if tuple(row[0] for row in rows) != KEYS:
            raise ValueError("source receiver key order/duplication")
        facts = dict(rows)
        if (facts["schema"] != SCHEMA or facts["root_path"] != "pom.xml"
                or facts["source_ref"] != "master"
                or not re.fullmatch(r"hsoliwal/[A-Za-z0-9._-]+", facts["repository"])
                or not re.fullmatch(r"[0-9a-f]{64}", facts["root_sha256"])
                or not re.fullmatch(r"[0-9a-f]{40}", facts["canonical_commit"])
                or not facts["canonical_recipe"].startswith("com.synexia.eclipse.EstateAst")
                or facts["status"] != "HOLD_NATIVE_AND_SEMANTIC_GATES"):
            raise ValueError("source receiver contract or owner changed")
        source = root / facts["root_path"]
        if fingerprint(source) != facts["root_sha256"]:
            raise ValueError("original Maven POM is not the exact recipe postimage")
        xml = ET.fromstring(source.read_bytes())
        profiles = [node for node in xml.iter() if node.tag.split("}")[-1] == "profile"]
        selected = [node for node in profiles if any(
            child.tag.split("}")[-1] == "id" and child.text == PROFILE for child in node)]
        if len(selected) != 1:
            raise ValueError("original Maven POM missing unique opt-in profile")
        refs = [node.text or "" for node in selected[0].iter()
                if node.tag.split("}")[-1] == "argument"]
        if refs != ["$" + "{project.basedir}/.m3/m3ast-20261021/verify.py"]:
            raise ValueError("build profile no longer points to exact source verifier")
        print("M3_SOURCE_PIN_PASS " + facts["repository"] + " root=" + facts["root_sha256"])
        print("M3_AST_SEMANTICS=NOT_RUN JNI=NOT_RUN MAVEN_BUILD=NOT_RUN")
        return 0
    except (OSError, UnicodeError, ValueError, ET.ParseError) as exc:
        print("M3_SOURCE_PIN_REFUSED " + str(exc), file=sys.stderr)
        return 2


if __name__ == "__main__":
    sys.exit(main())
