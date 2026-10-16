# CTabFolder viewport (leaf L5)

Status: implemented on win32 (invisible desktop); both modes recorded equal to the official SWT 3.135.0 jar.

Plan row: [`ALL_WIDGETS_VIEWPORT_PLAN.md`](ALL_WIDGETS_VIEWPORT_PLAN.md) section 7, rank 5.

## Oracle first

`Test_org_eclipse_swt_custom_CTabFolder_ScaleOracle` (opt-in, `-Dswt.ctabfolder.oracle=true`) measures one folder of
800 x 300 with 10^3, 10^4 and 5 x 10^4 tabs: create, layout after a resize, paint, select a random tab, mouse move.
Medians in microseconds, invisible desktop:

| Jar / mode | tabs | create | layout | paint | select | move |
|---|---|---|---|---|---|---|
| official 3.135.0 | 10^4 | 1 086 937 | 10 048 | 2 454 | 199 876 | 65 |
| official 3.135.0 | 5 x 10^4 | 4 557 394 | 22 010 | 4 213 | 716 932 | 293 |
| this SWT, original | 10^4 | 319 230 | 7 211 | 2 197 | 103 862 | 40 |
| this SWT, original | 5 x 10^4 | 2 869 857 | 8 832 | 1 684 | 438 652 | 256 |
| this SWT, viewport | 10^4 | 218 157 | 9 362 | 1 973 | 12 862 | 61 |
| this SWT, viewport | 5 x 10^4 | 1 836 458 | 11 686 | 2 379 | 18 120 | 314 |

The original and official rows run the same code; their gap is run-to-run noise on a host short of memory.

A JFR profile (native method samples) at 2 x 10^4 tabs put the cost in one place: every folder update
(`runUpdate`, after any `setSelection`, `setText`, `showItem` ...) calls `updateTabHeight`, and
`CTabFolderRenderer.computeSize(PART_HEADER)` measures the text of **every** tab (`GC.textExtent`) to take the
largest height. Layout (`setItemSize`, `setItemLocation`) measures only the tabs that fit, plus O(n) integer work.
Paint draws only the tabs in the damage, but allocated two `Rectangle`s per tab to test that. A mouse move tests every
tab with integer compares (6 ns per tab).

So the plan's Fenwick prefix sums of tab widths were not needed: no layout path sums all widths with measurement, and
the O(n) integer loops cost microseconds. The leaf removes the O(n) text measurement instead.

## What the viewport mode changes

| Site | Original mode (stock) | Viewport mode |
|---|---|---|
| `CTabFolderRenderer.computeSize(PART_HEADER)` | measures the text of every tab | measures a tab's text again only when its text, its font or the GC font is another object than at the last measurement, or a font was disposed (`CTabItem.extentText`, `extentItemFont`, `extentGcFont`, `extentHeight`) |
| `CTabFolder.onPaint` | `getBounds ()` and `event.getBounds ()` per tab | the same intersection test on the tab's fields; a `Rectangle` only for the tabs drawn |
| `CTabFolder.createItem` at the end | rebuilds the priority array element by element | `System.arraycopy` (every entry is below the new index, so the loop would copy them unchanged) |

The header height is still the largest height over all tabs: the image height is read each time, and the text height
comes from the same `GC.textExtent (text, FLAGS)` call as before when anything it depends on changed. The text is a
`String` (immutable), so the same object has the same characters. A GC font is per zoom (`SWTFontProvider`), so a zoom
change gives another font object. A disposed font is measured again, so it fails the way stock does. The measurement is
reused only for the default renderer (`getClass () == CTabFolderRenderer.class`): a subclass that overrides
`computeSize` or `computeTrim` is called for every tab as before. `PART_HEADER` is computed only by
`updateTabHeight`, with a `GC` on the folder.

The mode is `-Dswt.widget.mode.CTabFolder=original` (or the global `-Dswt.widget.mode=original`); viewport is the
default (`CTabFolder.VIEWPORT`).

`SWT.VIRTUAL` lazy tab content (`SWT.SetData` the first time a tab is selected while the folder is showing) was
already in this SWT (#195 family) and is unchanged; without the style the folder behaves as stock.

## Proof

Recorded differential `Test_org_eclipse_swt_custom_CTabFolder_ViewportDifferential` (8 seeds x 120 steps, up to 311
tabs). Each step records the answer, the state (count, selection, client area, tab height, how many tabs are showing,
the first one shown, four tab bounds, four hit tests), the Selection and Resize events and the Paint regions; each seed
records the pixels (`WM_PRINT`). The steps add up to 200 tabs at a time, dispose tabs, select, show, toggle simple, MRU,
min/max, unselected close, tab height, minimum characters, tab position and size, rename tabs, set tab fonts (two
sizes or none), the folder font, tab images (12 and 30 pixels high or none) and two-line texts. The tab height takes 14
different values in the recording (18 to 61 pixels), so the measurement reuse is exercised.

Results, jar `aw16`, invisible desktop:

| Comparison | Result |
|---|---|
| viewport mode vs the official recording | pass |
| original mode vs the official recording | pass |
| viewport mode vs the original-mode recording | pass |
| official recording vs original-mode recording | identical files |
| `Test_org_eclipse_swt_custom_CTabFolder` + `CTabItem`, viewport | 145 passed, 1 skipped, 1 aborted (no foreground alpha on win32) |
| same, original | 144 passed, 1 skipped, 1 aborted |

The survey of the fork against the official jar before this leaf (`-Dswt.viewport.survey=true`) found no difference in
either mode, so no earlier deviation had to go behind the mode.

## Non-claims

- Creating tabs one at a time stays O(n) per tab (O(n^2) in total): `items` is an exact-length array that the
  renderer and every loop read (`parent.items.length`), and each insert copies it. 5 x 10^4 tabs take about 1.8 s.
- A mouse move still tests every tab (integer compares; 0.3 ms at 5 x 10^4 tabs).
- GTK and Cocoa are not measured here. The change is common code.
