#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Builds the Direct2D shim of the gpu drawn planes (swt-m3-d2d-win32.dll) with mingw-w64 g++ and packs it as the
# class path resource /swt-m3-d2d-win32.dll of a small jar. Prints the toolchain, flags and hashes (recorded in
# docs/m3/RENDER_MODES.md). Run under build_lock.sh.
# Usage: build_direct2d_shim.sh <worktree> <out-jar>
WT=$1; OUTJAR=$2
JDK="C:/Program Files/Java/jdk-21"
SRC="$WT/bundles/org.eclipse.swt/Eclipse SWT Render Direct2D/win32/library/swt_m3_d2d.cpp"
WORK=$(dirname "$OUTJAR")/direct2d-shim; rm -rf "$WORK"; mkdir -p "$WORK"
FLAGS="-O2 -shared -static -Wall -Wno-unknown-pragmas"
g++ $FLAGS -I"$JDK/include" -I"$JDK/include/win32" -o "$WORK/swt-m3-d2d-win32.dll" "$SRC" -ld2d1 -ldwrite || exit 1
echo "toolchain: $(g++ --version | head -1)"
echo "flags: $FLAGS -ld2d1 -ldwrite"
echo "source sha256: $(sha256sum "$SRC" | cut -c1-64)"
echo "dll sha256: $(sha256sum "$WORK/swt-m3-d2d-win32.dll" | cut -c1-64) ($(stat -c %s "$WORK/swt-m3-d2d-win32.dll") bytes)"
rm -f "$OUTJAR"; (cd "$WORK" && "$JDK/bin/jar" --create --file "$OUTJAR" swt-m3-d2d-win32.dll) && echo "jar: $OUTJAR"
