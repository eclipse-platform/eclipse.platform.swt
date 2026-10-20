<!-- Copyright 2026 Hitesh Soliwal and contributors. SPDX-License-Identifier: Apache-2.0 -->
# Full-range scrollbar arithmetic: exact canonical receiver proof

This directory is build-time proof/custody, not a runtime layer. SWT's existing `ViewportScrollBarGeometry` remains the only geometry owner; no new widget, viewport, M3 store, native symbol or runtime dependency. M3JDK21 remains the integrated Java/HotSpot/JNI SDK; Synexia is the canonical convergence/recipe owner.

Canonical source: https://github.com/hsoliwal/com.synexia/pull/12544 at `9372a2d57f1a05394172a7bd85dda3f0d6d83983`. Map this directory's `src/` and `recipes/` paths to `synexia-openrewrite-recipes/` at that revision. Engine and scope-owner Git blobs are pinned in the workflow. Resources, tests and verification driver are exact copies of the canonical source, not a second mutation authority. Any improvements must return to the canonical recipe. Existing source and generated output keep their original Apache-2.0 OR EPL-2.0 notice; first-party proof/recipe expression remains Apache-2.0.

## Two-stage review

The first proof commit leaves production Java unchanged. Run actual Maven/OpenRewrite qualification and generated-output verification first. Only after observing their success may a separate, single-file receiving commit install the exact generated output. The last CI comparison intentionally fails until receiving is complete; earlier successful steps must never be presented as a green overall run.

Pinned original SWT revision `839ce8a904cfe399912caea7f58bd59713a8fbbb`, source blob `1fd72c44bf9dcd5c2beca6ce95388640f48e0f67`, SHA256 `0b8ede7af0551b7afaa270b9723bb405fad315f4f0ddb6e8dfc4db3267e30b44`. Candidate SHA256 `df4f8498b1f633f2ffeae310f761ecaa0ea686e392557b8cfe2c684864a7f9b4`.

## Defect and scope

Page and range endpoints near Integer.MAX_VALUE overflow before the existing rational scaling helper is called. A 100-pixel track incorrectly gets an 8-pixel thumb instead of 100. Large `2*arrow+track` hit-test bounds also wrap and can classify position zero as outside the bar. Widen those expressions before arithmetic; preserve the original package-level signatures, rounding, short-track and non-scrollable rules. The existing int helper delegates to a private bounded-long overload, with no allocation, floating point or runtime BigInteger.

Supported arithmetic domain: nonnegative int minimum < maximum, positive thumb <= range, selection in [minimum,maximum-thumb], nonnegative int track/arrow and positive minimum thumb. This does not qualify native windows with extreme pixel dimensions or every invalid input tuple. Existing thumbStart/selectionAt algorithms remain; their length dependency is corrected. Widget callback, identity, UI-thread, event and native lifetime ownership is unchanged.

## Reproduce

`mvn -B -ntp -f docs/m3/scrollbar-full-range-20261020/recipes/swt-scrollbar-full-range-20261020/pom.xml clean test`

`python3 docs/m3/scrollbar-full-range-20261020/recipes/swt-scrollbar-full-range-20261020/verify.py`

After receiving, append `--receiver .` to the second command. It refuses absent generated output and never substitutes the template. Seven JUnit SDK tests exercise generation, fixed point, missing/duplicate/drift/semantic-change refusal and lexical decoys under order permutations.

The independent BigInteger oracle covers all six geometry operations with 5,723,783 assertions per mode, including 100000 ordinary and 100000 extreme seeded configurations. Expected-value digest is an oracle-corpus identity, not proof that original and corrected observations are identical. Baseline has zero ordinary failures and reproduces the witnessed extreme defects; corrected fixture has zero mismatches. Tiered and C1 local candidate runs passed strict Java21 compilation; four compiled negative variants were rejected. Exact public/package descriptors match. These local facts are not SDK or native execution; actual hosted results must be attached to exact heads.

## Pending acceptance

Native Win32 extreme pixels/drag, GTK/Cocoa, DPI/accessibility/event/disposal semantics, SWT/Tycho, source-built M3JDK21, retained-memory and performance admission remain separate unexecuted gates. Existing 14 recorded Win32 cases and original tests are preserved, not relabeled as fresh native measurements. The Java OS matrix is NOT a native SWT matrix. No full history traversal or full framework support is claimed. Keep PR draft; no automatic merge, rebase, forced update or release promotion.
