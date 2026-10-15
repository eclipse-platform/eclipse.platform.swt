<!-- SPDX-License-Identifier: EPL-2.0 -->
# SWT original owner: live dirty-word scan receiver

**DRAFT, not product-admitted.** Canonical recipe and retained original/candidate
source/test files live in [Synexia PR #11607](https://github.com/hsoliwal/com.synexia/pull/11607).
This receiving branch changes only the **existing original**
`org.eclipse.swt.widgets.ViewportPaintGraph` method `forEachDirty`, adds
one SWT JUnit class, its explicit `AllWidgetTests` suite registration,
and this receipt.

## Exact source custody

- Original SWT `master` base: `9cc0683c49255a23400ba491d86e1311e69447c0`.
- Original production Git blob: `0342cb11983d41e39bba2345150c71146a3dc181`;
  UTF-8 SHA-256 `fbfd24c27bb22a42ab32ad573179cd8ce63fa588f94bb6f0b6c1504466d0e429`.
- Current production blob: `cf774d12e68c96ccd46cb4f63f66543474a14440`;
  UTF-8 SHA-256 `fd00903d07343044836963f54cf34e98cf24b794d38cc8242fcddfdf72527d7c`.
  This is **byte-identical** to the canonical Synexia OpenRewrite candidate.
- New JUnit blob `bed9551e82a9e95fc0e1fa36159fc8694f4cd7bd`,
  generated as an **ABSENT-before** exact-source test.
- Existing `AllWidgetTests.java` original Git blob
  `99c20fde98d2a029995d795e1268ea9d0d207818` is updated by one
  class registration; canonical generated Git blob
  `566f6c3abd56cca3d1ed16393ffc766450557b4a` is byte-identical.
- No other production code, public/protected method signatures, native/JNI files,
  renderer model, GC owner, native handles, M3 item facades or scrollbar state are touched.
  Existing SWT EPL-2.0 headers remain.

## Current source-sealed hybrid receiver

The target source above is the **hybrid** candidate, not the initial sparse-only
snapshot. Tiny (<256) and globally dense (>=25% dirty) viewports retain the
original per-node traversal. Sparse viewports visit live dirty 64-bit words;
locally dense words recheck each node. Mutation and callback ordering remain
synchronous in the original SWT owner, without a new Canvas or JNI product API.

Executed exact source-level evidence in canonical
[Synexia #11607](https://github.com/hsoliwal/com.synexia/pull/11607):

- Strict Java21 original and hybrid source compilation, same `javap -package -s`
  declared package/public/protected ABI signatures.
- **13,000** original-graph reference parity cases and **804,198** observed
  callback invocations, plus nested and exception checks.
- C17/JNI **24,000** independently compared immutable bitmap snapshots and
  **25,000** original-versus-hybrid Java callback mutation cases, run with
  `-Xcheck:jni`, and again under `-Xint` with GCC UBSan.
- Isolated 10-distribution adaptive benchmark: strong sparse improvements but
  0.93–0.94x outcomes on two dense distributions. No GPU speedup is claimed.

Evidence: [exact source proof](https://github.com/hsoliwal/com.synexia/blob/m3/swt-original-paint-dirty-word-scan-20261015/synexia-openrewrite-recipes/recipe-crates/swt-original-dirty-word-scan-20261015/verification/EXACT_OWNER_JAVA_JNI_PROOF_20261015.md).

**Still HOLD:** real Maven/OpenRewrite SDK, full SWT/Tycho/JUnit, Win32/GTK/Cocoa
Display/GC/Skija pixels, native events/accessibility/IME/DPI and full process
performance remain NOT_RUN. This does not advance the 360-source catalogue.

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
