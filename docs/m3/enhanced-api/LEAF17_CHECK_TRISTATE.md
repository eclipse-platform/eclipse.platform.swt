# Leaf 17: tri-state check dig-in (M3CheckState)

Map row M3-CHECK-TRISTATE. In a `SWT.CHECK` tree, applications almost always want "dig-in": checking a parent checks
its subtree, and a parent shows the partial state (checked and grayed) when only some descendants are checked.
Stock SWT leaves this to every application. JFace has `CheckboxTreeViewer.setSubtreeChecked` but no ancestor state.

## Surface (package `org.eclipse.swt.widgets`, public, `@since 3.138`)

| member | meaning |
|---|---|
| `UNCHECKED` (0), `CHECKED` (1), `PARTIAL` (2) | the three states; `PARTIAL` is checked and grayed |
| `state(TreeItem)` | the item's state from `getChecked` and `getGrayed` |
| `setChecked(TreeItem, boolean)` | checks or unchecks the item and its whole subtree, then recomputes every ancestor |
| `setDigIn(Tree, boolean)` | when on, a user check toggle (`SWT.Selection` with `detail == SWT.CHECK`) does the same as `setChecked` |
| `isDigIn(Tree)` | whether dig-in is on |

## Contract

- After `setChecked(item, c)` every descendant has state c (CHECKED or UNCHECKED, never PARTIAL).
- Each ancestor is:
  - CHECKED when all its children are CHECKED;
  - UNCHECKED when all are UNCHECKED;
  - PARTIAL otherwise.
- The state is stored in the stock checked/grayed bits, so it is visible to every existing reader and survives a
  mode switch.
- Dig-in reacts to user toggles only. A plain `TreeItem.setChecked` by the application stays single-item, as in stock
  SWT.
- Cost: O(subtree) for the descendants plus O(depth × siblings) for the ancestors, through the public API. A virtual
  tree materializes the children it visits.

## DOOP

- No new state beyond the stock check bits and one listener per dig-in tree, kept in keyed data.
- The descendant walk is iterative, using an explicit stack lane of items.

## Algorithm choices

An iterative subtree walk plus an ancestor recount. The map row's O(log n) per toggle needs per-node checked counts in
the topology (`VirtualTreeTopology` flags). That waits for a seam owned by the tree agent and is recorded here as the
open part. No catalogue row.
