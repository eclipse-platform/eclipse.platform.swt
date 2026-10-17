# ToolBar and CoolBar viewport (leaf L11, native bars)

Status: measured on win32 (invisible desktop). CoolBar: one change in the viewport mode, recorded equal to the
official SWT 3.135.0 jar in both modes. ToolBar: measured, no change (the cost is in the native control).

Plan rows: [`ALL_WIDGETS_VIEWPORT_PLAN.md`](ALL_WIDGETS_VIEWPORT_PLAN.md) section 4 ("ToolBar, CoolBar: D only; item
counts are small") and section 7, rank 11 (the drawn `custom`/`skija`/`gpu` realizations are not part of this leaf).

## Oracle

`Test_org_eclipse_swt_widgets_ToolBar_ScaleOracle` (opt-in, `-Dswt.toolbar.oracle=true`, sizes with
`-Dswt.toolbar.oracle.counts=500,2000`) measures

- a `SWT.FLAT | SWT.RIGHT` ToolBar, single row and `SWT.WRAP`, with n items (every seventh a separator, the others
  with text and tool tip): create, `computeSize`, paint, `getItems`, `indexOf` the last item, rename a middle item,
  dispose the first item and insert one at index 0, and one more item at the end (200 appended, per item);
- a `SWT.FLAT` CoolBar with n / 20 bands, each a `SWT.DROP_DOWN` CoolItem holding a packed ToolBar of 20 items with
  its preferred size: create, `setWrapIndices`, paint and resize.

Medians in microseconds. ToolBar, run of 2026-10-17 (jar `aw21`; three desktops shared with other agents):

| Jar / mode | bar | items | create | computeSize | paint | getItems | indexOf | rename | dispose + insert at 0 | append (per item) |
|---|---|---|---|---|---|---|---|---|---|---|
| official 3.135.0 | single | 500 | 186 219 | 935 | 2 691 | 604 | 7 | 2 648 | 2 728 | 369 |
| official 3.135.0 | single | 2000 | 969 875 | 1 356 | 3 157 | 2 147 | 12 | 24 | 4 118 | 939 |
| official 3.135.0 | wrap | 2000 | 2 179 050 | 609 | 10 378 | 1 935 | 9 | 26 | 11 888 | 1 531 |
| this SWT, viewport | single | 2000 | 687 880 | 696 | 1 902 | 1 491 | 6 | 19 | 3 347 | 531 |
| this SWT, viewport | wrap | 2000 | 1 318 589 | 549 | 10 121 | 2 025 | 7 | 24 | 10 802 | 1 461 |

A JFR profile (native method samples) of the ToolBar and CoolBar oracle at 2000 items put the ToolBar time in the
native toolbar window procedure: `TB_INSERTBUTTON` from `ToolBar.createItem`, and the `WM_SETFONT` relayout that
`ToolItem.setText` sends to work around a Windows layout bug, both O(n) in comctl32. SWT's own loops in
`ToolBar.layoutItems` (text/image scan, `resizeControl`) and the id search in `createItem` are a small part, so the
ToolBar is left unchanged: making them O(1) would not change the measured growth.

The CoolBar profile put a third of all samples in `SetWindowPos` called from `CoolBar.wmNotifyChild`
(`RBN_CHILDSIZE`): on every band layout the rebar sends this notification for every band, and SWT sets the child's
bounds again to the rectangle the rebar has just applied. The stock comment says why: a drop-down `Combo` loses its
list height when the rebar sizes it; "if the control is not a combo, this does nothing". It still costs a
`SetWindowPos` with a frame change per band per layout, O(bands) per band insert.

## What the viewport mode changes

| Site | Original mode (stock) | Viewport mode |
|---|---|---|
| `CoolBar.wmNotifyChild`, `RBN_CHILDSIZE` | `control.setBoundsInPixels` with the band child rectangle, for every child | the same call, skipped when the child is not a `Combo`, has no `ViewportPlane` record, the bar has no open batch of deferred window positions, the rebar is not mirrored and the child's native rectangle already equals the notified one (`CoolBar.placed`); the skipped call would only send a frame change to an unchanged window |

CoolBar, run of 2026-10-17 (jar `aw22`; the host was short of memory during the run, so the numbers are noisy):

| Jar / mode | bands | create | setWrapIndices | paint | resize |
|---|---|---|---|---|---|
| official 3.135.0 | 25 | 890 363 | 64 992 | 467 | 13 435 |
| official 3.135.0 | 100 | 10 876 491 | 399 097 | 2 125 | 42 200 |
| this SWT, original (`aw22`) | 25 | 1 169 905 | 55 531 | 540 | 14 515 |
| this SWT, original (`aw22`) | 100 | 11 116 353 | 195 526 | 1 584 | 35 742 |
| this SWT, viewport (`aw22`) | 25 | 447 806 | 31 213 | 352 | 5 307 |
| this SWT, viewport (`aw22`) | 100 | 5 432 405 | 145 093 | 2 310 | 35 082 |

The mode is `-Dswt.widget.mode.CoolBar=original` (or the global `-Dswt.widget.mode=original`); viewport is the
default (`CoolBar.VIEWPORT`). No upstream defect is fixed (brief rule 12): both modes behave as stock, and
[`toolbar-viewport/UPSTREAM_DEFECTS.tsv`](toolbar-viewport/UPSTREAM_DEFECTS.tsv) has its header only. Donors:
[`toolbar-viewport/FOSS_REUSE_DECISION.tsv`](toolbar-viewport/FOSS_REUSE_DECISION.tsv).

## Proof

Recorded differential `Test_org_eclipse_swt_widgets_CoolBar_ViewportDifferential` (8 seeds x 100 steps). The steps
add up to 24 bands at a time (tool bars of 1 to 9 items, drop-down combos, texts and labels; drop-down bands or not),
dispose bands, set wrap indices, the lock, band sizes, preferred and minimum sizes, the item layout (order, wraps and
sizes), add and rename tools, set the font of a band control and fit the band again, replace or remove a band
control, and resize the bar and the shell. Each step records the bar (count, size, lock, order, wrap indices), every
band (bounds, size, preferred and minimum size, its control's bounds and visibility), the Resize events of the bar,
the Move and Resize events of every band control, and the Paint regions of the bar; each seed records the pixels
(`WM_PRINT`). Every probe is compared with equality.

Results, jar `aw22`, invisible desktop:

| Comparison | Result |
|---|---|
| viewport mode vs the official recording | pass |
| original mode vs the official recording | pass |
| viewport mode vs the original-mode recording | pass |
| `Test_org_eclipse_swt_widgets_CoolBar` + `CoolItem` + `ToolBar` + `ToolItem`, viewport | 231 passed, 3 failed, 2 aborted (no foreground alpha) |

The three failures (`CoolItem.test_setControl`, `test_getBounds`, `test_setSizeII`) fail the same way with the
official jar, with `aw21` (before this leaf) and with `aw22` in the original mode on the invisible desktop: they are
not caused by this leaf.

## Non-claims

- The ToolBar is unchanged: creating, renaming and laying out items stay O(n) per operation in the native control.
- CoolBar band layout itself (comctl32 rebar) stays O(bands) per insert; only SWT's redundant re-set is removed.
- A combo band child is still set again on every notification, as stock (its drop-down height depends on it).
- GTK and Cocoa are not measured here.
