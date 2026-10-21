<!-- Copyright 2026 Hitesh Soliwal and contributors. SPDX-License-Identifier: Apache-2.0 -->
# SWT scrollbar full-range receiver: exact Java21 proof

## Source identity and additive boundary

- Original SWT master/head source blob: `1fd72c44bf9dcd5c2beca6ce95388640f48e0f67`.
- Canonical generated receiver blob: `7370ac91ca85784b7b72efabef4129ffbfd3cea3`.
- Generated receiver SHA-256: `df4f8498b1f633f2ffeae310f761ecaa0ea686e392557b8cfe2c684864a7f9b4`.
- This branch receives the **exact** canonical source; it does not add a second widget, runtime layer, JNI ABI or storage owner. Original proof-only PR #236 remains intact.
- GitHub receiver commit: `8e5eb98cb71b2751a6690d34aece98973e246f9c`.

## Executed local Java 21 test

Using the exact candidate source file and exact `ScrollbarFullRangeProof.java` file from the proof branch (Git blobs `7370ac91...` and `0765c1a5...`), both materialized as original bytes with Git blob hash comparison:

```text
javac --release 21 -encoding UTF-8 -Xlint:all -Werror -d classes \
  org/eclipse/swt/widgets/ViewportScrollBarGeometry.java \
  org/eclipse/swt/widgets/ScrollbarFullRangeProof.java

java -cp classes org.eclipse.swt.widgets.ScrollbarFullRangeProof strict

GEOMETRY_PASS checks=5723783 ordinaryFailures=0 oracleDigest=27d10dbfbd5f515a
mulDiv mismatches=0
hasThumb mismatches=0
thumbLength mismatches=0
thumbStart mismatches=0
selectionAt mismatches=0
hit mismatches=0
```

The independent proof uses `BigInteger` only in the test oracle, not in production. It evaluates original win32-style rational rounding and 100000 ordinary plus 100000 extreme seeded states, including overflowing endpoints and hit geometry.

## Remaining admission gates

This is a **draft receiver** pending actual canonical Maven/OpenRewrite SDK recipe execution and fixed-point regeneration, exact-head Actions readback, SWT/Tycho build, native Win32/GTK/Cocoa behaviors, DPI, lifecycle/disposal and accessibility checks. No JDK image, JNI/Jtreg, native SWT runtime, full framework CI or release acceptance is claimed.

The original dual SPDX header and package-level API are unchanged. The pre-receiver source remains directly available in immutable Git history and in the preservation checkpoint. No force-push, deletion or ancestry-only acceptance.
