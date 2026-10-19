#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
set -euo pipefail
root="$(cd "$(dirname "$0")" && pwd)"
swt_src="$root/../src/org/eclipse/swt/snippets"
work="$(mktemp -d)"
trap 'rm -rf "$work"' EXIT
javac --release 21 -Xlint:all -Werror -d "$work" \
  "$swt_src/M3TreeDemandModel.java" "$root/M3TreeDemandModelTest.java"
java -cp "$work" org.eclipse.swt.snippets.M3TreeDemandModelTest
