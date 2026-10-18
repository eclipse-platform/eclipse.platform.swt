# SWT Tree Bounded Residency Implementation Plan

**Goal:** Attach and reconcile native GTK3 virtual Tree rows from the logical visible-plus-overscan window, preserve Java semantic authority and exposed Item identity, and publish the source with its exact canonical recipe.

**Current source milestone:** delivered in [SWT #81](https://github.com/hsoliwal/eclipse.platform.swt/pull/81) and [recipe #9339](https://github.com/hsoliwal/com.synexia/pull/9339), with exact immutable readback of all 58 files. Runtime and merge qualification remain separate pending gates.

**Worktrees:** `swt-residency-live` and sparse `synexia-residency-live`.
**Source baselines:** SWT `97be47c95bc6ee075b3992bb8f0fa2d2e0cbbe46`; canonical develop `aa37ab2d7bb851bb2e86f314225dc43e118df071`.

## Existing direction and donor custody

- [SWT #72](https://github.com/hsoliwal/eclipse.platform.swt/pull/72): shared viewport owner.
- [SWT #75](https://github.com/hsoliwal/eclipse.platform.swt/pull/75): Java presentation authority.
- [SWT #76](https://github.com/hsoliwal/eclipse.platform.swt/pull/76): native prefix-residency limitation.
- [SWT #77](https://github.com/hsoliwal/eclipse.platform.swt/pull/77) / [recipe #9194](https://github.com/hsoliwal/com.synexia/pull/9194): prior RowWindow capacity/atomicity correction.
- SWT master includes #78's single-pass sparse visible-window walk, #79's stack-safe topology deletion and #80's full logical-model activation. This candidate retains those owners and algorithms.
- [SWT draft #81](https://github.com/hsoliwal/eclipse.platform.swt/pull/81) and merged [canonical phase1 #9293](https://github.com/hsoliwal/com.synexia/pull/9293) contain the earlier primitive planner/native packet preparation. The older `.m3/gtk-tree-bounded-residency-20261005` receipt is preserved as historical evidence.

## Constraints

- Java topology, packed state, presentation and compressed selection remain authoritative.
- Ordinary viewport planning uses primitive lanes and does not create a logical sibling prefix or facade per logical row.
- Preserve exposed Items and public SWT signatures; explicit result enumeration may materialize its requested Items.
- Use the existing real SWT GC and viewport body/header/editor/feedback owners.
- Apply all nine source targets through exact hash-pinned OpenRewrite recipes; retain source drift refusal.
- Keep generated JNI files and compiled binaries outside the cohort.
- Publish additively. Do not rewrite history or update the merged canonical phase1 PR.
- Tests are not added or run unless requested. Compilation does not establish runtime behavior or merge readiness.

## Task 1: Inventory and source seals

- [x] Read viewport/planner PRs and stronger current SWT owners.
- [x] Trace logical/native materialization, rendering, hit paths, mutation, selection/focus, scrolling and accessibility callback ownership.
- [x] Separate explicit API enumeration from bounded viewport demand.
- [x] Seal eight Java owners and hand-maintained `os_custom.c` to exact pre/post hashes and retained notices.
- [x] Re-read current canonical develop and compile/replay against its unchanged parser engine.

## Task 2: Primitive residency planner

- [x] Reuse the existing single-pass visible preorder traversal and stack-safe topology lifetime.
- [x] Emit visible-plus-overscan rows, deduplicated ancestor closure and bounded expander hints.
- [x] Separate logical coordinates, dense physical parent/sibling paths, paint entries and facade IDs.
- [x] Reuse caller-owned primitive buffers; topology and geometry generations invalidate snapshots.
- [x] Compile the eight common sources with Java 21 `-Xlint:all -Werror`.

## Task 3: Live GTK3 VIRTUAL adapter

- [x] Attach a bounded `SwtVirtualTreeModel`; keep the full logical model detached for semantic API paths.
- [x] Translate rendering, hit testing, item demand and native callbacks between resident and logical coordinates.
- [x] Reconcile while detached under redraw/signal suppression, with `finally` restoration and stamp invalidation.
- [x] Preserve Java Item identity and presentation, logical focus, compressed selection and logical scrolling.
- [x] Add logical accessibility table/cell demand, bounded headers, selection rank lookup, state/focus notifications and disposal tombstones.
- [x] Resolve source-review findings for disposal coordinate shifts, singleton AT selection lookup, Shift contraction, keypad/select-all routing, multi-item expansion, inherited backgrounds, disposed targets and no-op keyboard events.
- [x] Apply the final source recipe: nine changed owners, followed by zero changed owners.
- [x] Compile all 493 GTK Java sources with exactly the baseline's 148-warning inventory; compile GTK3 `os_custom.c` as an object with `-Wall -Werror`.

## Task 4: Draft delivery and evidence

- [x] Prepare exact source/engine custody, compile-input inventory, reuse ledgers and retained license notices.
- [x] Review the explicit file cohort and preserve current canonical develop outside the new recipe/crate paths.
- [x] Publish an additive SWT #81 update and a new canonical live recipe draft.
- [x] Match both immutable published Git trees and all 58 source/recipe blobs to the reviewed local candidate.
- [x] Record the delivered PR links and source milestone receipt; the goal tracker closes after final receipt readback.

## Pending qualification

Behavioral/refusal tests, GTK/GUI execution, live AT/AT-SPI behavior, native linking, DND/editor/owner-draw parity, complete keyboard action-signal coverage, cross-platform parity, full reactors, benchmarks, atom reconciliation, strict M3 admission and merge qualification remain unexecuted. Full GTK Java `-Werror` remains open because the source baseline already has 148 warnings. ATK/SWT integer ABI ranges and double pixel precision remain explicit limits.

The detailed receipt is [here](../../../.m3/gtk-tree-residency-live-20261005/evidence.json); the [progress record](../../../.m3/gtk-tree-residency-live-20261005/progress.md) names the implemented scope and remaining gates.
