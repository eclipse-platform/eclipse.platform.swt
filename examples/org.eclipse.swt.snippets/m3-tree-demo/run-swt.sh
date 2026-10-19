#!/usr/bin/env bash
# SPDX-License-Identifier: Apache-2.0
set -euo pipefail
root="$(cd "$(dirname "$0")" && pwd)"
swt_src="$root/../src/org/eclipse/swt/snippets"
swt_classpath="$(printenv SWT_CLASSPATH || true)"
if [[ -z "$swt_classpath" ]]; then
  echo 'Set SWT_CLASSPATH to an approved SWT core + platform JAR path list.' >&2
  exit 2
fi
work="$(mktemp -d)"
trap 'rm -rf "$work"' EXIT
javac --release 21 -Xlint:all -Werror -cp "$swt_classpath" -d "$work" \
  "$swt_src/M3TreeDemandModel.java" "$swt_src/M3NonVirtualTreeDemo.java"
java -cp "$work:$swt_classpath" org.eclipse.swt.snippets.M3NonVirtualTreeDemo "$@"
