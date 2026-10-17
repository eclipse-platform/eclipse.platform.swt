# SWT and Nebula viewport history custody — inventory pass, 2026-10-17

This dated, additive evidence snapshot inventories every accessible PR through **SWT #213** and **Nebula #238**, plus **the 100 latest searchable commit summaries per repository**. It does not claim an exhaustive review of all commit diffs or all repository commits. Preserve this snapshot unchanged; create a new dated inventory for later PRs.

| Fork | PR metadata rows | GitHub merged | Closed without PR merge | Recent commits |
| --- | ---: | ---: | ---: | ---: |
| SWT | 213 | 196 | 17 | 100 |
| Nebula | 238 | 214 | 24 | 100 |
| **Total** | **451** | **410** | **41** | **200** |

Evidence:
- [SWT PR inventory](SWT_PR_INVENTORY_20261017.tsv)
- [Nebula PR inventory](NEBULA_PR_INVENTORY_20261017.tsv)
- [SWT recent commits](SWT_RECENT_COMMITS_20261017.tsv)
- [Nebula recent commits](NEBULA_RECENT_COMMITS_20261017.tsv)

The TSV PR ledgers preserve PR number, state, merged flag, timestamps, base/head refs and SHAs, title and source URL, sorted by PR number.

## Source owners and ancestry checkpoints

| Widget family | PRs to inspect together | Existing owners |
| --- | --- | --- |
| SWT Tree | #196, #197, #201, #203, #205, #210, #213 | TreeViewport, VirtualTreeVisibleProjection, VirtualTreeResidentPlan, topology and stock oracles |
| SWT Table/List/Combo | #199, #200, #201, #203, #206, #209 | TableViewport, ListViewport, ComboViewport, logical scroll metrics |
| SWT StyledText / planes | #115, #118, #121–#128, #201, #202 | logical lines, scrolling, painted damage, original/drawn modes |
| SWT Initiative31 | #172, #194, #212 | separately rooted custom-drawn SWT and M3 renderer naming |
| Nebula Grid | #85, #148, #151, #181, #232, #234, #235, #237, #238 | GridViewport, GridRowExtents, GridRowSpanIndex, intrinsic owner |
| Nebula Gallery | #175–#177, #184, #236 | intrinsic Gallery paint and scrollbar windowing |

**Special case:** SWT [#213](https://github.com/hsoliwal/eclipse.platform.swt/pull/213) is closed and has the GitHub PR merged flag false. However [master merge commit 678da68](https://github.com/hsoliwal/eclipse.platform.swt/commit/678da68f34007af0aab0689aa72bd85fb44b800e) explicitly says it preserves the ancestry of #212 and #213. Preservation of commit ancestry does **not** prove that #213's Tree shrink-selection behavior or its seven-case oracle runs on current master. Classify source provenance and test separately.

The **17** SWT closed-without-PR-merge numbers: 21, 24, 27, 33, 34, 36, 37, 40, 45, 48, 61, 84, 88, 135, 138, 139, 213.

The **24** Nebula closed-without-PR-merge numbers: 2, 4, 6, 13, 20, 21, 33, 34, 41, 42, 55, 56, 58, 72, 75, 77, 104, 146, 172, 173, 177, 184, 194, 208.

These are *reconciliation candidates*, not assertions of missing code and not instructions to cherry-pick them blindly.

## Contract-preserving process for subsequent passes

1. Inventory the concrete owner, public contract, native/resource lifetime, existing recipe and historical commits; inspect commit parents and compare actual source bytes.
2. Converge chronologically and additively. Classify each change as present, preserved ancestry only, superseded, or missing. Never force-push, erase branches, squash away evidence, or overwrite uninspected changes.
3. Consult a searchable problem-category/donor catalogue (indexed rows, interval stabbing, sparse masks, residency, paint damage, native resources), including license, complexity and contract evidence.
4. Run the guarded **Inline → Atomize → Patternize → Comment** sequence, serially per file; regenerate from a source-sealed recipe and prove a fixed point. Unknown preimage means HOLD, not an overwrite.
5. Qualify with small and million-row oracles, boundary/overflow tests, reentrancy, native-handle identity, sparse memory and GC/resource checks.
6. Compare original and viewport modes for API answers, selection/event order, focus/caret, keyboard/mouse, DPI, accessibility, scroll, screenshots and disposal; validate win32, GTK and Cocoa individually.
7. Submit another isolated draft PR with source and test receipts. Merge only after relevant CI and native test gates.

## Separate current draft follow-ups

- [SWT #215](https://github.com/hsoliwal/eclipse.platform.swt/pull/215): ancestor-safe native Tree descendant handles.
- [Nebula #240](https://github.com/hsoliwal/nebula/pull/240): bounded primitive row-span projection sorting.
- [Nebula #243](https://github.com/hsoliwal/nebula/pull/243): precise cached column visibility mask semantics.

The relevant native SWT/JUnit integration suites and visual acceptance have **not been run** for these drafts in the present environment. This PR adds documentation and source evidence only. Nothing here is a claim of full behavioral correctness or complete per-diff historical review.
