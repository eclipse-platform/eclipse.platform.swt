# SWT M3 viewport PR history and additive reconciliation (2026-10-17)

This is a **historical metadata index**, not a claim that every PR diff and head workflow has passed review. The normalized `PR_HISTORY.tsv` lists the 213 returned GitHub PR records, sorted by PR number. It is a point-in-time snapshot. Counts: 196 merged, 17 closed-unmerged, 0 open. Repository: https://github.com/hsoliwal/eclipse.platform.swt. Recent retrieved commit: `678da68f34007af0aab0689aa72bd85fb44b800e` (commit-search ordering; verify exact branch head before promotion).

No existing production file, existing branch, base branch, PR title, license, or test was modified. No rebases, force pushes, merges, deletions or updates to old PRs were performed.

## How to read this ledger

- `merged=true` indicates GitHub PR state, **not** that a particular merged file's postimage is still in today's first-parent tree.
- For each candidate: compare **current source blob SHA**, source owner branch and any preserved checkpoints; classify semantic atoms and dependency closure; rerun stock/viewport differential and exhaustive Java/JNI/native tests at the actual receiving head.
- Closed-unmerged PRs are retained in this index and must be reviewed before deciding whether their deltas were superseded, absorbed via another route, or lost. Do not auto-cherry-pick them.
- Preserve EPL-2.0/Synexia notices and pinned donor attribution. Catalogue LeetCode, HackerRank, GeeksforGeeks and GitHub donors by category, separately from user-owned implementation authority.

## Verified source discrepancy

- Current `master` **TreeViewport.java** blob: `6a4963d3d54a412fcbf444baddb0989d822fa924`, 1,317 lines.
- Preserved `preservation/claude-tree-shrink-20261017` blob: `d06f592e445f4132a1810f343862b420dab4e1ab`, 1,971 lines.
- Current source lacks the `caretAfterShrink`, `patternLimits`, and `anyChildrenSelected` tokens present in the preserved source. This is a **content discrepancy**, not proof that any particular user-visible regression occurs in an untested build.
- `preservation/claude-tree-shrink-20261017` is **117 commits behind** master and 0 ahead; its commit is already in master's ancestry. DO NOT reapply the whole file: a later tree differs.
- [#213](https://github.com/hsoliwal/eclipse.platform.swt/pull/213) is closed without merge, yet its ancestor is present. Its seven-case stock oracle is valuable test input, not passing head-run evidence.

## Contract qualifications to close

1. Re-run the seven-case `ShrinkSelectionOracle` and the 12×150-step Tree differential on the current master, preserved owner and official SWT 3.135.0, separately for MULTI/SINGLE and columns/checkbox variants.
2. Mechanically classify each missing source atom, not each entire file; compare ordered behavior/selection/native events/pixels, then independently apply only proven atoms through the **canonical Synexia Maven/OpenRewrite recipe**.
3. Refuse changes when the exact current source hash differs; fix/re-run the recipe instead of hand-editing production source.
4. Keep original, custom, M3/Skija and GPU render modes orthogonal to the logical viewport; preserve native object leases, exact event order, OS-specific behavior and per-widget ownership.
5. Test win32 first with hidden-desktop native probes; GTK/Cocoa remain unverified unless specifically executed.

## Explicit PR anchors

- [#196](https://github.com/hsoliwal/eclipse.platform.swt/pull/196) Tree mediation; [#197](https://github.com/hsoliwal/eclipse.platform.swt/pull/197) order-statistic index; [#198](https://github.com/hsoliwal/eclipse.platform.swt/pull/198) all-widgets plan; [#200](https://github.com/hsoliwal/eclipse.platform.swt/pull/200) Table; [#201](https://github.com/hsoliwal/eclipse.platform.swt/pull/201) integrated viewport; [#203](https://github.com/hsoliwal/eclipse.platform.swt/pull/203) next integration; [#210](https://github.com/hsoliwal/eclipse.platform.swt/pull/210) checkpoint; [#213](https://github.com/hsoliwal/eclipse.platform.swt/pull/213) unmerged preserved shrink oracle.

**Promotion: HOLD** until currently materialized source, exact-head build, native differential, event timing and painting parity are verified. No CI/build or native UI tests were executed by this audit.
