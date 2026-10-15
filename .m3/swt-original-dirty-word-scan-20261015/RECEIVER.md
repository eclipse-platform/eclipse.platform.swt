<!-- SPDX-License-Identifier: EPL-2.0 -->
# SWT original owner: live dirty-word scan receiver

**DRAFT, not product-admitted.** Canonical recipe and retained original/candidate
source/test files live in [Synexia PR #11607](https://github.com/hsoliwal/com.synexia/pull/11607).
This receiving branch changes only the **existing original**
`org.eclipse.swt.widgets.ViewportPaintGraph` method `forEachDirty`, adds
one SWT JUnit class and this receipt.

## Exact source custody

- Original SWT `master` base: `9cc0683c49255a23400ba491d86e1311e69447c0`.
- Original production Git blob: `0342cb11983d41e39bba2345150c71146a3dc181`;
  UTF-8 SHA-256 `fbfd24c27bb22a42ab32ad573179cd8ce63fa588f94bb6f0b6c1504466d0e429`.
- Current production blob: `8c05dba447e3bb420bf4258697f068ba1bf5e69c`;
  UTF-8 SHA-256 `98dd646aea6416acfa9ccbb721ff76d3bd6067f60ce8d92487eac87b05409385`.
  This is **byte-identical** to the canonical Synexia OpenRewrite candidate.
- New JUnit blob `e3e4888ee1702c544948c63750a767e517f8f103`,
  generated as an **ABSENT-before** exact-source test.
- No other production code, public/protected method signatures, native/JNI files,
  renderer model, GC owner, native handles, M3 item facades or scrollbar state are touched.
  Existing SWT EPL-2.0 headers remain.

## Behavior proof and remaining gates

The canonical branch's isolated Java21 strict-compile comparison passed
**25,000** old/new loop mutation/effect parity trials, additional word-boundary,
append and exception tests. It is a **CPU logic oracle only**; the exact complete SWT
class and the added JUnit have **NOT_RUN** through a product Tycho build.

Still required: executable OpenRewrite SDK run, SWT JUnit and full-platform Win32,
GTK and Cocoa source/native builds; callback exception/disposal/race traces, region
clipping/screenshots, draw ordering, CPU/heap/RSS/GPU performance and
accessibility/RTL/DPI checks. A failed workflow with zero steps is not a test pass.

## Source-owning M3 contract

Per [Synexia #11521](https://github.com/hsoliwal/com.synexia/pull/11521),
the **original widget** remains the viewport; this is one private method-level
refinement of its existing retained paint graph and must not claim all 360
inventory sources have been qualified. Do not transplant Initiative 31
prototype classes into current SWT native master or introduce another Canvas.
