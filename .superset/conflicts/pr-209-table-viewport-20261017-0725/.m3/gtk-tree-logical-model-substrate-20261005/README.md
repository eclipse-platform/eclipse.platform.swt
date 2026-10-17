# GTK virtual Tree logical-model substrate — 2026-10-05

## Objective

Provide the native substrate needed to remove GTK virtual Tree's remaining prefix-residency dependency without changing live Tree behavior in this atom.

This is stacked on `m3/swt-viewport-gtk-bulk-sparse-20261005` / PR #69. It is intentionally dormant: `Tree.createHandle()` still selects the existing `GtkTreeStore`.

## Canonical ownership

`VirtualTreeTopology` remains the semantic owner of logical hierarchy, child counts, expansion state and materialized SWT identities.

The new native model receives a rebuildable primitive snapshot only. It is not a second semantic tree, and it retains no `TreeItem` objects.

Snapshot layout:

- element 0: topology storage capacity,
- element 1: logical root child count,
- next `capacity` ints: parent IDs,
- next `capacity` ints: logical child indices,
- next `capacity` ints: known child counts.

Cold logical siblings require no Java or native node.

## Recipe-first admission

Recipe owners:

- `.m3/openrewrite-recipes/src/main/java/com/synexia/m3/viewport/GtkVirtualTreeModelSubstrateCandidate.java`
- `.m3/openrewrite-recipes/src/main/java/com/synexia/m3/viewport/GtkVirtualTreeModelSubstrateRecipe.java`
- `.m3/openrewrite-recipes/src/test/java/com/synexia/m3/viewport/GtkVirtualTreeModelSubstrateRecipeTest.java`

Target 1:
- `bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/VirtualTreeTopology.java`
- preimage SHA-256: `d257dbfe16ff6b06f5f22a7d4bb0d3f9fe635272ab2e0cb4391930646cbd81b3`
- postimage SHA-256: `0ae6c0796b39cfe318efb7175b2a2b8981335e409d473d6e06256dd67e0b2eef`

Target 2:
- `bundles/org.eclipse.swt/Eclipse SWT PI/gtk/library/os_custom.c`
- preimage SHA-256: `f49d321ef991514c53c2b0e5da6812c199540e2ea770ed411b8e4b4488c51e28`
- postimage SHA-256: `17916c76157392c7f94c6f3cda3538ec9543419b4dfa4b7572cd2e91bd7f0ee1`

The candidate refuses preimage drift and, once materialized, refuses postimage drift. Java output is reparsed through OpenRewrite; C output is replayed as exact `PlainText`.

## Native model

`SwtVirtualTreeModel` is GTK3-only and currently dormant.

Its `GtkTreeIter` represents logical `(parentId, childIndex)` directly. A materialized SWT ID is looked up from the sparse snapshot when present; a cold logical row remains valid without a `GtkTreeStore` node.

Implemented callbacks:

- get iter / path / value,
- next sibling,
- children / has-child / child count / nth child,
- parent reconstruction.

The model is registered through the already-existing `content_providers_create_gtype` bridge next to `SwtVirtualTableModel`.

## Regression

`test_virtualTreeNativeModelSnapshotStaysSparseAcrossTenMillionRoots` proves:

- logical root count = 10,000,000,
- only two materialized topology IDs are needed,
- snapshot length follows topology storage capacity, not logical root count,
- parent/index/count lanes preserve hierarchy exactly,
- unobserved child counts remain unknown.

## Verification limits

GitHub Actions for the current repository are failing before jobs are emitted on current master as well as PR branches, so Actions currently cannot provide source-specific acceptance evidence.

The execution container has Java 21 but cannot resolve `github.com`, so it cannot check out this public branch for a local reactor/native build. No Maven/native/GTK execution is claimed for this atom.

Activation is intentionally deferred. The next atom must migrate GTK virtual Tree presentation state to Java-owned item state, select `SwtVirtualTreeModel`, synchronize snapshots, and prove selection/expansion/SetData/accessibility/scroll behavior before replacing the live `GtkTreeStore`.
