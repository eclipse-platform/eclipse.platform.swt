#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Builds the m3 backend of the drawn planes (source folder "Eclipse SWT Render M3", loaded by name at run
# time) against an SWT jar and the Skija 0.116.3 jars. Run under build_lock.sh.
# Usage: build_m3_backend.sh <worktree> <swt-jar> <out-jar> [skija-lib-dir]
WT=$1; SWT=$2; OUTJAR=$3; SK=${4:-C:/m3-superset/i31/binaries/org.eclipse.swt.win32.win32.x86_64/lib}
JDK="C:/Program Files/Java/jdk-21"
TMP=$(dirname "$OUTJAR")/skija-backend-classes; rm -rf "$TMP"; mkdir -p "$TMP"
find "$WT/bundles/org.eclipse.swt/Eclipse SWT Render M3" -name '*.java' | sed 's#.*#"&"#' > "$TMP.src"
"$JDK/bin/javac" -J-Xmx512m --release 21 -encoding UTF-8 -nowarn -proc:none -cp "$SWT;$SK/skija-shared-0.116.3.jar;$SK/types-0.1.1.jar" -d "$TMP" @"$TMP.src" || exit 1
rm -f "$OUTJAR"; (cd "$TMP" && "$JDK/bin/jar" --create --file "$OUTJAR" .) && echo "m3 backend: $OUTJAR ($(sha256sum "$OUTJAR" | cut -c1-16))"
