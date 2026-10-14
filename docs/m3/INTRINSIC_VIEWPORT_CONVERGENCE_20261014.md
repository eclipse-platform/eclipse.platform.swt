# Intrinsic SWT viewport convergence — candidate, not whole-toolkit completion

Base: 8d607fc88f6ba5b3b3bf07a6d34967c104da63e8. The base includes the existing Table/Tree viewport and intrinsic DPI lifecycle implementations. This branch changes the existing owners, not a wrapper control, renderer hierarchy, item API, event registry or native backend.

## New source, canonical recipe first

Synexia [PR #11406](https://github.com/hsoliwal/com.synexia/pull/11406), commit 0505992a5cb8978144603ba5448056c7ffaff31f, owns the qualified kernel replay, source-sealed templates, standalone Maven crate, authored SDK tests, Java/C17 JNI test oracle, and category/coverage evidence. Named recipe `com.synexia.M3SwtPatternLanes20261014` reuses `M3GpuToolJoinRecipe` and `M3GpuToolJoinManifest`.

- ItemLanes: forward cursor over existing pattern records for windowed masks; override > pattern > default retained. Exact output blob 3a438f739d422331f607de0400608a480f04e77c.
- ItemBitSet: stage all four directory replacements before publication; current heap policy unchanged. Exact output blob 1690d39ba60cd9a8e398597819049e94b0d2ce50.

Both exact input blobs were independently checked on this master baseline. Received output matches previously committed Synexia templates. Correct recipes and replay, never hand-repair targets. Public/protected descriptors unchanged in the finite helper comparison.

## Existing reviewed source reused, not counted as newly implemented

- SWT #160 / Synexia #11346: VirtualItemStorage staged growth; received blob e8ab2f2333b77b8faaa43499a099f02d6b46504b. Current master preimage f6396574de53ff696a526c4f4446fb447c89c568 matches the original recipe.
- SWT #161 / Synexia #11364: resident-only virtual Table DPI processing with nested-state/disposal guards; received blob ab998a81dac8bb47afa0da75e34f550bed929397. Master preimage 20c182fcfa91492e7adeb2030cf2f852a6e9aaab matches.
- SWT #155 / Synexia #11275: primitive affine corners and translation path; received blob 3d9f9b6a9f9b03930fe6e2dfeac3fdc16979a395, plus both original regression files. Master preimage 84bad64728d0c323fd167b5e36523c6211ed9f01 matches.

These are exact earlier recipe outputs, not new transformations or an assertion that the combined tree has passed UI qualification. Original PRs remain open. Preserve their independent proof histories and merge holds.

## Executed vs pending

New cross-SWT/Nebula helper campaign: 3,671,313 assertions / 33,184 windows at three heap limits, 982 canonical-kernel checks, staged-growth failure injection, three faulty gather controls, unchanged public/protected descriptors; actual C17 scalar JNI oracle covers 1,516 windows and seven invalid-input refusals. Repeated checks overlap. `SWT.error` is an explicitly declared minimal fixture only; no Display/GC/widget lifecycle is emulated. JNI is proof-only, not product dispatch.

NOT_RUN: actual Maven/OpenRewrite SDK, full final combined SWT/Tycho build, native Win32/GTK/Cocoa Display, GWT, painting, overscan/scrollbars, DPI/IME/accessibility, reentrancy/disposal, exposed Item identity and production benchmarks. Prior PR evidence is not relabelled as fresh combined-tree evidence. Keep this PR draft.

## Entire SWT scope

Use Synexia's existing all-widget catalogue under `docs/M3-SCALE/runs/20261013-all-widget-viewport/` and the task `COVERAGE_DELTA.tsv`. List/Combo/CCombo/StyledText, menu/tool/tab/expand/cool-bar, scalar controls, containers and native integrations require their own family-specific implementation and original test receipts. This branch does not mark those families complete.

Initiative31 List #154/#156/#157 remains on its own alternative implementation lineage; do not overwrite native master List with that source. Widget-owned state, native scrollbars, caller-held Item identity, events and lifecycle remain the integration contract. No automatic merge, default-branch edits, rebase or force-push.

All existing EPL-2.0 / dual-licensed source notices are preserved. No challenge solutions or unlicensed donor bodies are copied.
