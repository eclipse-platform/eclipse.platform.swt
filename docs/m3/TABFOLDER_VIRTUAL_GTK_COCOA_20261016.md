# TabFolder `SWT.VIRTUAL` on GTK and Cocoa (2026-10-16)

## Contract

A `TabFolder` created with `SWT.VIRTUAL` sends `SWT.SetData` the first time a tab becomes the selection while the folder is showing. The listener creates the tab's control and calls `setControl`. While the folder is hidden, only a pending bit is set, and the first paint catches up once.

Before this change, only win32 kept that contract, through `fillLazyTab`. GTK and Cocoa had the shared `VirtualTabContent` (common) and `paintCatchUpByListener ()`, but no caller.

## Wiring

| Event | win32 (reference) | GTK | Cocoa |
| --- | --- | --- | --- |
| Insert: filled-bit lane shifts | `lazyFilled.insert` | `createItem`, after `items [index] = item;` | same |
| First tab is created | `fillLazyTab (0)` after the closing comment | end of `if (itemCount == 1)`, after `// the widget could be destroyed at this point` | end of `createItem`, guarded by `count == 0` |
| Remove: lane shifts | — | `destroyItem`, after `items [itemCount] = null;` | `destroyItem`, after `items [count] = null;` |
| Selection moves after a removal | — | `destroyItem`, start of `if (newIndex != -1)` | — (native selection callback) |
| Native selection | `TCN_SELCHANGE` | `gtk_switch_page` | `tabView_didSelectTabViewItem`, start of the matching item block |
| Programmatic selection | `setSelection` → `fillLazyTab` | `setSelection (int, boolean)` | `setSelection (int, boolean, boolean)` |

Each platform gets the same two members:

- the field `virtualContent`;
- the accessor `lazyContent ()`, which creates the `VirtualTabContent` on first use for a `SWT.VIRTUAL` folder and returns `null` for an ordinary folder.

## Recipe-first provenance

The 41 added lines are recipe output, not hand edits. The recipes are in `hsoliwal/com.synexia`, branch `m3/viewport-catalogue-recipes-20261016`, crate `synexia-openrewrite-recipes/crates/m3-viewport-catalogue-recipes`:

- `com.synexia.openrewrite.viewport.SwtTabFolderLazyContentGtk`
- `com.synexia.openrewrite.viewport.SwtTabFolderLazyContentCocoa`
- `com.synexia.openrewrite.viewport.SwtTabFolderLazyContentMembers`

They are built from two leaf recipes:

- `M3AddMemberIfMissing`, for the field and the accessor;
- `M3InsertStatements`, which inserts at an anchor and handles the `AFTER`, `BLOCK_START`, `BLOCK_END` and `METHOD_END` positions, plus overload selection by parameter count.

To re-run:

```text
M3PerFileApply com.synexia.openrewrite.viewport.SwtTabFolderLazyContentGtk   .../gtk/org/eclipse/swt/widgets/TabFolder.java
M3PerFileApply com.synexia.openrewrite.viewport.SwtTabFolderLazyContentCocoa .../cocoa/org/eclipse/swt/widgets/TabFolder.java
```

| Gate | Result |
| --- | --- |
| Pass 1 | CHANGED (gtk, cocoa) |
| Pass 2 (fixed point) | UNCHANGED (gtk, cocoa) |
| Crate JUnit | 19 tests, 0 failures, 0 skipped. Includes byte-exact qualification against SWT commits `cff6936c13` and `8f9b8665ec`, and YAML-bound options |
| `git diff --check` | clean |
| `javac --release 21`, GTK source set (543 files) | 0 errors |
| `javac --release 21`, Cocoa source set (685 files) | 0 errors |
| GTK or Cocoa runtime tests | NOT_RUN: no Linux or macOS display on this host |

The first recipe run found two defects in the recipe itself, and both were fixed in the recipe rather than in its output:

- the Cocoa `if (index != -1)` anchor also matched `setSelection (TabItem [])`, so overloads are now selected by parameter count;
- on GTK, `BLOCK_END` placed the new statement above the block's closing comment, so closing comments now stay directly after the last original statement.
