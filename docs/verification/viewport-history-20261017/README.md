# SWT pull request and viewport history audit — 2026-10-17

## Snapshot and coverage

Repository: [hsoliwal/eclipse.platform.swt](https://github.com/hsoliwal/eclipse.platform.swt).
Audited master: `678da68f34007af0aab0689aa72bd85fb44b800e`.

The all-state PR endpoint was paginated in ascending creation order, 100 per page:
pages 1, 2, and 3 returned 100, 100, and 13 records. The complete inventory is
**213 PRs: 196 merged, 17 closed without a GitHub merge timestamp, zero open**.

All 213 PR numbers, titles, full bodies, states, timestamps, base/head revisions,
merge commit fields and URLs are preserved in `pull-requests.json.gz`.
Every PR's full changed-path list is compared against the pinned master by exact
Git blob identity in `pr-file-state-audit.json.gz`. Enumeration completed for all
213 PRs without a diff-enumeration or head-ancestry error.

Read-only review inventory: all 213 per-PR review-submission endpoints returned
zero reviews; the repository-wide inline-review-comment endpoint returned zero
comments; the repository-wide issue-comment endpoint returned all 15 comments.
These are retained separately, including historical qualification/HOLD statements.

The clone has complete commit history (it is not shallow). Master reaches
32,900 commits; its first-parent history has 31,862 commits. The logs are retained
in `history-all.tsv.gz` and `history-first-parent.tsv.gz`; 710 commits dated since
2026-09-26 are also provided in `recent-master-history.tsv`. These are history
inventories, not a claim that every upstream statement or platform behavior has
been qualified.

## Preserve source meaning as well as ancestry

The exact head of 111 of the 213 PRs is an ancestor of the audited master.
The other 102 heads remain available as Git objects/branch history in this
checkout. A head that is not an ancestor does not by itself prove lost behavior:
squash, replay, replacement and later equivalent changes require content review.

The converse is equally significant. Several integration commits explicitly
retained the first parent's entire tree while attaching incoming work as an
additional parent. In particular:

| Master commit | Preserved work | Tree effect at that merge |
| --- | --- | --- |
| `ef565e23ce85b013b006fa0621f9cc67f3a2c186` | Initiative31 #177, #178, #179, #180, #181, #192, #194 | Existing master tree retained |
| `fe6bd10cc0900bdca15366de12277dc3dc465849` | Integration #201 | Existing master tree retained |
| `d8f71813b2a3556994ead3455f3cac7ad29cd741` | Tree checkpoint #210/#205 | Existing master tree retained |
| `3bb05f5fbcb1fdd7226d4cf4a324634bf77c28d3` | Render-tools checkpoint #211/#208 | Existing master tree retained |
| `0d8193176aee4ebf7054cd5a088ea7dae2eb7936` | Integration #203 and attribution #204 | Existing master tree retained |
| `678da68f34007af0aab0689aa72bd85fb44b800e` | Naming #212 and Tree shrink checkpoint #213 | Existing master tree retained |

The Table checkpoint preservation commit
`afa16ac65ab71942ae32eb455bc7bb8894937ecc` also adds an archive of incoming
conflicting files. Its tree is therefore not identical to its first parent,
but this does not establish active-path adoption of those files.

The latest active `TreeViewport.java` is 53,226 bytes; the preserved #205
checkpoint file is 84,498 bytes. Active `TableViewport.java` is 11,805 bytes;
the #206 checkpoint file is 44,558 bytes. Byte size is only a locator for
substantial divergence, not a correctness or performance metric.

## Concrete successor candidates

| History | Verified current state | Required treatment |
| --- | --- | --- |
| [#190](https://github.com/hsoliwal/eclipse.platform.swt/pull/190), paint-graph growth | All four changed paths exactly match their preimages (including absent-before additions). The runtime graph is `cf774d12e68c96ccd46cb4f63f66543474a14440`. #190 head is not an ancestor of master. | Recover exact source via a qualified new recipe and successor PR; preserve the predecessor #185 dirty traversal. |
| [#213](https://github.com/hsoliwal/eclipse.platform.swt/pull/213), shrink/caret selection | The four-file checkpoint is ancestry-preserved. Active owner lacks the surrounding caret/shrink/pattern helpers required by the tip patch. | Reconcile behavioral atoms and stock oracles in their original owner; do not replace the whole active file with the checkpoint. |
| [#201](https://github.com/hsoliwal/eclipse.platform.swt/pull/201) | Of 138 PR path changes, 96 current paths still match preimages, 28 match postimages and 14 have diverged. | Treat the integration's source adoption as incomplete until per-path and contract closure. |
| [#203](https://github.com/hsoliwal/eclipse.platform.swt/pull/203) | Of 163 changes, 121 still match preimages, 30 diverge and 12 are absent at active paths. | Reconcile the Tree stock-rule, render-mode and further widget work separately. |
| [#182](https://github.com/hsoliwal/eclipse.platform.swt/pull/182), [#187](https://github.com/hsoliwal/eclipse.platform.swt/pull/187), [#188](https://github.com/hsoliwal/eclipse.platform.swt/pull/188), [#189](https://github.com/hsoliwal/eclipse.platform.swt/pull/189), [#191](https://github.com/hsoliwal/eclipse.platform.swt/pull/191) | Heads are not current-master ancestors; their common Initiative31 widget/graphics owners are absent from native master's active paths. | Preserve that separate source lineage and requalify within the owning branch; these are not drop-in native-master replacements. |
| [#183](https://github.com/hsoliwal/eclipse.platform.swt/pull/183), ItemBitSet RUN queries | Head is not a current-master ancestor; the one production source differs from both exact preimage and postimage. | Inspect live private query atoms, preserve the existing growth/endpoint protections, and reuse the canonical recipe if applicable. |

### Order-statistic sibling index

[#197](https://github.com/hsoliwal/eclipse.platform.swt/pull/197) has a final
recorded head `c5b4eb47922c16f009f642253a561fb653c9dbf6` whose actual commit
changes only documentation. The source implementation is in separately preserved commits:

- `486ca58dc3208eb22f9dce2f6a6c8e421a01e01c`: original sibling-index implementation,
  topology/projection changes, independent differential test and suite registration.
- `5d739970216d6327b8b8b1c49cfda24dc696453a`: measured small-sibling linear path
  and row-mapping benchmark.

Both commits are current-master ancestors through preserved integration history;
the active master nevertheless lacks `VirtualTreeSiblingIndex.java` and retains
the linear projection owner. `supplemental-index-history.json` records exact
commit messages and paths. PR prose describing benchmarks does not establish
that those implementation files are active.

### Preserved Tree shrink-selection oracle

The stock transcript retained by #213 reports the following seven MULTI Tree cases
from 300 roots with the caret at root 299:

| Initial selection and action | Expected caret | Expected selection | Expected top |
| --- | --- | --- | --- |
| Root 139 only; shrink to 298 | Root 297 | Empty | Root 277 |
| Same, first scroll to top 0; shrink to 298 | Root 297 | Empty | Root 277 |
| Select all; shrink to 299 | Root 298 | 299 survivors | Root 278 |
| Select all, deselect new caret 298; shrink to 299 | Root 298 | 297 selected survivors | Root 278 |
| Roots 139 and 297 selected; shrink to 298 | Root 297 | Roots 139 and 297 | Root 277 |
| Caret 299 only selected; shrink to 299 | Root 298 | Empty | Root 278 |
| Root 139 only, perform UP/DOWN first; shrink to 298 | Root 297 | Empty | Root 277 |

Every transcript case reports no Selection events. These are **historical stock
observations**, not newly executed Win32 acceptance. The preserved patch and complete donor owner remain available at the pinned
[PR #213](https://github.com/hsoliwal/eclipse.platform.swt/pull/213) head. This audit does not copy them into active runtime paths. Active master's `setItemCount` releases facades,
updates topology and relayouts; its `releaseFacades` clears a removed focus ID.
It lacks `removesCaret`, `caretAfterShrink`, `moveCaretAfterRemoval`,
`selectSilently`, `becameEmpty` and the later select-all pattern lifecycle.

## Meaning of file classifications

- `matches_pr_postimage`: exact current blob equals the PR result, including
  absence if the PR deleted that path.
- `still_matches_preimage`: exact current blob equals the preimage at
  `merge-base(base, head)`, including absence for an unapplied added path.
- `missing_on_master`: current path is absent while neither comparison above
  applies.
- `diverged_on_master`: current bytes match neither exact side.

These classifications locate work. They do not automatically authorize a replay
or prove semantic regression. Source changes must preserve current contracts and
other accepted fixes, be generated by the qualified recipe, and receive platform
evidence appropriate to the owner.

## Reproduction and qualification boundaries

Use the existing repository build/qualification entry points:

- Complete supported-platform reactor: `mvn clean verify`.
- Focused SWT JUnit: `mvn verify -pl :org.eclipse.swt.tests -am -DskipNativeTests=false -Dsurefire.failIfNoSpecifiedTests=false -Dtest=ClassName`.
- JNI regeneration for native source changes:
  `mvn --non-recursive -Pjni-generator exec:exec@run-jni-generator`.
- Tree/List/Table recorded differential and oracle runners live under
  `docs/m3/{tree,list,table}-viewport/`; these include Windows-specific
  environment requirements and must run on their intended platform.
- The #190 recovery reuses canonical Synexia [#11681](https://github.com/hsoliwal/com.synexia/pull/11681)
  proof source and source seals; its new run must separately record actual
  Maven/OpenRewrite execution, real JUnit discovery and source compilation.

Historical draft/HOLD/native-red comments remain relevant evidence even where
GitHub later marked the associated PR merged. Whole SWT/Nebula viewport parity,
native pixel output, accessibility, callback order and CPU/heap/RSS qualification
cannot be inferred from merge status, archive retention or headless helper tests.

## Compressed complete inventories

The full metadata and history inventories are gzip-compressed with a deterministic timestamp.
`inventory-sha256.json` seals both the original UTF-8 content and compressed bytes.
For example, `gzip -dc pull-requests.json.gz` restores the full PR bodies and recorded revisions.
The readable per-PR state table and recent history remain uncompressed for review.
