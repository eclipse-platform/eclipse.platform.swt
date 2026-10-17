# Bounded residency progress

Plan: `docs/superpowers/plans/2026-10-05-swt-tree-bounded-residency.md`.
Goal tracker: active; implementation work is continuing.

## Completed prior cohort

- SWT #77 and Synexia #9194 contain the shared RowWindow capacity/atomicity candidate.
- Java 21 model compilation and exact source replay passed; runtime tests were not run.

## Task 1 findings

- `Tree.ensureVirtualNativeItem(parentIter, index)` requests `index + 1`
  resident children. `ensureVirtualNativeChildren` builds that whole prefix.
- Collapse compaction keeps `highestPinned + 1` children. A single far pinned
  facade can therefore keep a large native prefix alive.
- `VirtualTreeTopology.nativeModelSnapshot()` is sparse in materialized IDs,
  but its root/count lanes advertise full logical child counts.
- `SwtVirtualTreeModel.iter_n_children` and sibling traversal consume those
  full logical counts. Bounded Java/C snapshot storage alone does not bound
  GtkTreeView's attached row structures.
- `Tree.getId`, item construction, index/path resolution, selection, bounds and
  expansion currently share native model coordinates. The native view projection
  needs an explicit logical/physical translation contract at these boundaries.
- The branch named `m3/swt-gtk-tree-logical-model-activate-20261005` was read at
  `434ada5771e1165e505d0e797a38ff7ce9a96c6d`. Its inspected owner still uses
  `GtkTreeStore`, `index + 1` growth and the older append loop. The branch name
  is not evidence of delivered logical-model activation. Classification:
  `PROOF_REQUIRED`; do not replace today's stronger presentation/frontier owners.
- Official GTK model documentation states that node references include top-level
  and expanded rows beyond the on-screen viewport, and require referenced parents:
  https://docs.gtk.org/gtk3/iface.TreeModel.html . Ancestor closure and native row
  count are therefore explicit projection requirements.

## Environment

- Windows has JDK 21.0.7 and Maven 3.9.12 available for candidate compilation.
- WSL `Ubuntu-24.04` now has GCC 13.3.0, pkg-config, GTK3 3.24.41
  development headers and OpenJDK 21.0.12.1. The hand-maintained GTK3 C owner
  compiled as an object with `-Wall -Werror`; no native runtime was executed.
- GitHub CLI 2.102.0 is installed under the user's local Programs directory.
  Its device login attempt expired; GitHub connector publication worked for
  both prior draft PRs. No CLI authentication success is claimed.

## Implemented source cohort

- `VirtualTreeVisibleProjection.Residency` reuses caller-owned primitive arrays.
  It plans the paint rows plus deduplicated ancestors from the existing `rowAt`
  traversal, without creating facades or a sibling prefix.
- The buffer separates logical coordinates from physical parent/sibling indices,
  provides both directions of physical path translation and emits a bounded
  four-lane native packet with separate SWT facade IDs.
- Topology revisions cover binding, counts, expansion, pruning and coordinate
  shifts. Viewport invalidation also covers same-top geometry/window changes.
- The existing native model accepts `swt-residency`, validates its physical
  counts and maps entry IDs to facade IDs. Replacement invalidates old iterators
  and must occur while the consuming view is detached.
- GTK `TreeItem` keeps its semantic ID in Java. ID allocation preserves topology
  slots with no facade yet; logical index and known-parent lookups use that owner.
- The canonical exact text recipe now admits the one hand-maintained
  `os_custom.c` path. Its gate change was itself applied by the existing exact
  Java recipe. Generated JNI owners are outside that new admission.

## Executed build/source operations

- Canonical bounded recipe compilation: five Java sources, release 21,
  `-Xlint:all -Werror`, offline cached Maven dependencies.
- Complete sealed source application: six changed SWT files; second application
  made zero changes. Canonical text-engine bootstrap: one change, then zero.
- Eight common Java sources: release 21, `-Xlint:all -Werror` compilation passed.
- All 493 production GTK Java sources compiled with `-Xlint:all`. The baseline
  and candidate have the identical 148-warning inventory. Full-owner `-Werror`
  qualification remains open; its first attempt stopped at pre-existing warnings.
- GTK3 `os_custom.c` compiled as an object with the repository's C flags and
  `-Wall -Werror`. The initial make invocation needed the common library include
  path; the recorded compiler command supplies that existing owner directly.
- No behavioral, JUnit, refusal, GUI or native runtime tests were added or run.

## Remaining live adapter work

1. Finish translating public materialization, mutation, geometry and native
   callbacks between topology coordinates and resident physical paths.
2. Attach the bounded model and reconcile windows under redraw/callback
   suppression with `finally` restoration. The live GtkTreeStore still grows
   sibling prefixes today; this phase has not closed that requirement.
3. Preserve logical scrolling, selection/focus, accessibility, DND and editor
   behavior while native rows enter and leave the window.
4. Publish/re-read the completed source cohort and keep all unexecuted
   qualification gates explicit in the paired draft PRs.

The original native residency goal remains active. The primitive planner and
native packet are implementation stages, not completion of the live GTK goal.

For further payload iterations, restore only this task's six sealed source
preimages with `tools/residency_crate.py restore-preimages` **before resealing**
changed payloads. That helper refuses any unexpected worktree byte changes before
writing. Then seal the new payloads, compile the recipe and apply it again.
