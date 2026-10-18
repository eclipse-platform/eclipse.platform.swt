# GTK virtual Tree sparse bulk traversal — 2026-10-05

## Admission

- Repository: `hsoliwal/eclipse.platform.swt`
- Base master: `071aea415e654cd2d628d39f58959a3d7d1f62be`
- Target: `bundles/org.eclipse.swt/Eclipse SWT/gtk/org/eclipse/swt/widgets/Tree.java`
- Target preimage SHA-256: `4759f0e133e0e58799b9ec2c6572af8233d9460ae22264e49c7facbbc9781b25`
- Target postimage SHA-256: `926b4e3bde0f1dea2e1032ca26baa81663cc49afbdadfbe58d85781f6b3d9b4d`
- Recipe: `com.synexia.m3.viewport.GtkVirtualTreeSparseBulkRecipe`
- Candidate owner: `com.synexia.m3.viewport.GtkVirtualTreeSparseBulkCandidate`

The recipe refuses target-source drift, accepts the exact preimage, reparses its Java candidate through OpenRewrite, and accepts only the exact bound postimage on repeat.

## Preserved contract

Public SWT `Tree` / `TreeItem` APIs, logical child counts, item identity, selection, expansion and `SWT.SetData` semantics are unchanged.

The changed atom is package-private `Tree.modelChildren(parentItem, materialize)`. Expansion still passes `materialize=true` and therefore preserves its existing behavior. Collapse passes `materialize=false` and now traverses only already-materialized coordinates in `VirtualTreeTopology`, matching the established Win32 sparse implementation instead of escaping through public `getItems()`.

## Donor / history evidence

- Synexia viewport invariant: logical model state is independent of bounded physical/native residency.
- SWT Win32 `Tree.modelChildren(..., false)`: existing same-repository donor for materialized-topology traversal.
- Cocoa virtual Tree: existing same-repository sparse `modelChildren(..., false)` behavior.
- GTK virtual Tree already owns `VirtualTreeTopology`, `VirtualTreeVisibleProjection`, `VirtualTreeViewport`, bounded native frontier growth and collapse compaction; this change removes the remaining bulk-collapse eager-materialization escape hatch.

No external source body is copied.

## Regression

`Test_org_eclipse_swt_widgets_Tree.test_virtualGtkCollapseAllDoesNotMaterializeColdRoots`:

1. creates a 4,096-root `SWT.VIRTUAL` GTK tree,
2. materializes one root and one child,
3. expands that touched root,
4. records topology materialized count,
5. calls `collapseAll()`,
6. requires materialized topology not to grow,
7. verifies the exposed root/child identities and logical counts remain unchanged.

## Verification state

Authored gates:
- recipe candidate fixed point,
- preimage/postimage drift refusal,
- OpenRewrite Java round-trip,
- GTK runtime regression in the existing SWT JUnit suite.

Executed independently in the ChatGPT Java 21 environment:
- `javac --release 21 -Xlint:all -Werror` on the sealed candidate plus a deterministic check harness,
- candidate fixed-point: PASS,
- atom-drift refusal: PASS.

OpenRewrite SDK execution was not available in that environment because Maven/OpenRewrite dependencies were not installed.

Repository Actions did trigger for PR #69, but the current repository workflows fail before emitting any jobs; the same SWT Matrix Build failure is present on current `master` and completes in roughly one second. This is therefore recorded as a CI-startup/infrastructure blocker rather than evidence of a source regression. Platform-native SWT runtime acceptance for this branch remains unclaimed.
