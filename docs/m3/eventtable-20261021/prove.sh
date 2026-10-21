#!/usr/bin/env bash
# SPDX-FileCopyrightText: 2026 Hitesh Soliwal and Contributors to the Synexia Project
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
set -euo pipefail
root="$(git rev-parse --show-toplevel)"
folder="$root/docs/m3/eventtable-20261021"
original="c04fd7a51dd64350540f316e14e79dcaebd9d59d"
candidate="efd31dc0d4acd1c088295e861b1d6b3a44313a43"
path="bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/EventTable.java"
actual="$(git -C "$root" hash-object -- "$root/$path")"
if [[ "$actual" != "$candidate" ]]; then
  echo "ECLIPSE_M3_EVENTTABLE_REFUSED: original SWT source generation drift $actual" >&2
  exit 2
fi
git -C "$root" cat-file blob "$original" >/dev/null
temporary="$(mktemp -d)"
trap 'rm -rf "$temporary"' EXIT
mkdir -p "$temporary/src/org/eclipse/swt/widgets" "$temporary/classes"
git -C "$root" cat-file blob "$original" \
  | sed 's/^class EventTable {/class LegacyEventTable {/' \
  >"$temporary/src/org/eclipse/swt/widgets/LegacyEventTable.java"
mapfile -d '' proofSources < <(find "$folder/test-src" -name '*.java' -print0)
javac --release 21 -encoding UTF-8 -proc:none -Xlint:all,-serial -Werror \
  -d "$temporary/classes" \
  "$root/$path" "$temporary/src/org/eclipse/swt/widgets/LegacyEventTable.java" \
  "${proofSources[@]}"
java -cp "$temporary/classes" org.eclipse.swt.widgets.EventTableM3Differential
java -Xint -cp "$temporary/classes" org.eclipse.swt.widgets.EventTableM3Differential
