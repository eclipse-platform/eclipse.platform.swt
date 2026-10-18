# ExpandBar viewport (leaf L6)

Status: implemented on win32 (invisible desktop). The original mode is recorded equal to the official SWT 3.135.0 jar.
The viewport mode is recorded equal to it except for two upstream defects it fixes (brief rule 12,
[`expandbar-viewport/UPSTREAM_DEFECTS.tsv`](expandbar-viewport/UPSTREAM_DEFECTS.tsv)): D1, stock clamps an item control
beyond the 16-bit native range, and D2, a native scroll then moves the clamped window. Donors:
[`expandbar-viewport/FOSS_REUSE_DECISION.tsv`](expandbar-viewport/FOSS_REUSE_DECISION.tsv).

Plan row: [`ALL_WIDGETS_VIEWPORT_PLAN.md`](ALL_WIDGETS_VIEWPORT_PLAN.md) section 7, rank 6.

## Oracle first

`Test_org_eclipse_swt_widgets_ExpandBar_ScaleOracle` (opt-in, `-Dswt.expandbar.oracle=true`, win32) measures one bar
of 400 x 600 with 1000 and 4000 items of height 40, every other item expanded and every tenth item with a `Label`
control: create, paint, scroll a page (`WM_VSCROLL SB_PAGEDOWN`), expand or collapse the first item, resize, and
(from `aw21`) append: 200 more items after the measurements, per item. Medians in microseconds, invisible desktop:

| Jar / mode | items | create | paint | scroll | expand | resize |
|---|---|---|---|---|---|---|
| official 3.135.0 | 1000 | 9 498 754 | 62 180 | 66 678 | 180 080 | 157 800 |
| official 3.135.0 | 4000 | 114 607 529 | 460 639 | 429 623 | 1 105 327 | 905 620 |
| this SWT, original (`aw17`) | 1000 | 8 032 529 | 74 187 | 79 726 | 192 070 | 196 333 |
| this SWT, original (`aw17`) | 4000 | 115 856 915 | 100 439 | 104 570 | 369 020 | 306 787 |
| this SWT, viewport (`aw20`) | 1000 | 1 971 779 | 2 109 | 25 899 | 25 331 | 29 494 |
| this SWT, viewport (`aw20`) | 4000 | 4 878 452 | 2 169 | 89 606 | 103 282 | 105 729 |

Run of 2026-10-17 (jar `aw21`, three desktops shared with other agents, so absolute numbers carry load noise; the
original mode ran during the heaviest load):

| Jar / mode | items | create | paint | scroll | expand | resize | append (per item) |
|---|---|---|---|---|---|---|---|
| official 3.135.0 | 1000 | 8 281 475 | 58 887 | 59 021 | 156 809 | 137 451 | 19 220 |
| official 3.135.0 | 4000 | 131 131 006 | 227 549 | 240 849 | 602 195 | 485 603 | 51 693 |
| this SWT, original (`aw21`) | 1000 | 31 446 540 | 126 309 | 111 344 | 295 173 | 258 510 | 41 195 |
| this SWT, original (`aw21`) | 4000 | 327 243 875 | 580 234 | 814 855 | 1 720 161 | 1 840 011 | 81 861 |
| this SWT, viewport (`aw21`) | 1000 | 2 279 089 | 2 054 | 12 348 | 6 686 | 50 313 | 2 019 |
| this SWT, viewport (`aw21`) | 4000 | 8 690 385 | 3 111 | 7 918 | 11 416 | 310 201 | 2 993 |

The same 4000 items run back to back on one desktop (a standalone measuring program outside the repository, five
rounds) separate the
jars without the load noise: expand 89-116 ms (`aw20`) and 11-13 ms (`aw21`); scroll a page 59-99 ms and 5-9 ms;
resize 115-171 ms and 120-153 ms (unchanged: sizes are applied natively, see the non-claims); moving the 331
controls parked beyond the native range by one pixel 52-103 ms and 1.3-3.5 ms; moving 60 in-range controls off the
client area 11-23 ms and 0.4-1.5 ms.

The per-item append cost stays near 2-3 ms from 1000 to 4000 items in the viewport mode (SWT's own work per append
is O(1) amortized; the rest is native window creation for every tenth item and paint), where the official jar goes
from 19 to 52 ms.

Where the time went (stock code, read with the numbers):

- `ExpandBar.getBandHeight ()` takes a DC, selects the font, reads `GetTextMetrics` and releases the DC, and every
  item header asks for it (`ExpandItem.getHeaderHeightInPixels`) from layout, paint and redraw. Appending an item lays
  out from its index, which first sums the heights of every earlier item: n band heights per append, O(n^2) DC
  round trips to create n items (115 s for 4000).
- `drawWidget` draws every item, header, text, chevron and border, whatever the damage.

## What the viewport mode changes

| Site | Original mode (stock) | Viewport mode |
|---|---|---|
| `ExpandBar.getBandHeight` | `GetDC`, `GetTextMetrics`, `ReleaseDC` per call | measured once per font handle and zoom (`bandFont`, `bandZoom`, `bandHeight`); a disposed font is measured each time, as stock |
| `ExpandBar.drawWidget` | `drawItem` for every item | `drawItem` only for items whose drawing reaches the damage rows (`ExpandItem.paintsWithin`: the header from `y`, and when expanded the border down to `y + header + height`); the paint DC clips to the damage, so a skipped item changed no pixel |
| `ExpandBar.wmScroll`, `showFocus` (D2) | `ScrollWindowEx (SW_SCROLLCHILDREN)` moves the child windows natively; a child beyond the native range was clamped at 32767 and moves from there | `scrollWindow`: the same native scroll, then every child that did not move by the scroll amount (a control kept beyond the native range by `ViewportPlane`, or one the scroll takes out of it) moves there logically |
| `ExpandBar.createItem` | the item array grows by 4 | the array doubles (appending n items copies O(n) references in all) |
| `ExpandBar.indexOf` (every `setHeight`, `setExpanded`, `setImage` of an item asks it) | scans from item 0 | first tries the index the item had when last found (`ExpandItem.indexHint`; items are unique, so a hit is the stock answer), then scans |
| `ExpandBar.layoutItems (i)`, i > 0 (every append lays out from the new item) | sums the header, spacing and expanded height of every item before i | continues from item i - 1 (`y + header + spacing + expanded height`) when every position was computed with the current band height (`layoutBand`); every change of a header, height or expanded state lays out from the item after it, so item i - 1 is where the sum would put it. A band height change without a layout from item 0 (a disposed bar font, a system font change) falls back to the sum until the next layout from item 0 |
| `ExpandItem.redraw` | `InvalidateRect` for the header, image and border rectangles | the same, skipped for a rectangle entirely off the client area (the update region is clipped to the client area, so it adds nothing) |
| item controls moved off the client area (`Control.setBounds` through `ViewportPlane`) | `SetWindowPos` for every control of every later item on expand, collapse, layout and scroll | **deferred**: a move whose native rectangle and new logical rectangle both lie off the client area keeps the native window where it is and moves only the logical position, with the Move event the native move would send (`ViewportPlane.defers`, `Composite.defersHiddenChildren`). The control is placed natively again by its next move onto the client area or with a size change, and when a native scroll or a resize of the bar brings its native or logical rectangle onto the client area (`ViewportPlane.exposeDeferred`). Before a native scroll every such child moves logically by the scroll amount, so the Move event of the native move answers the new position (`ViewportPlane.scrollDeferred`). Mapping (`toDisplay`, `Display.map`) uses the native window's actual position |

Item controls beyond 32767 pixels are placed logically by the L1 plane geometry (`ViewportPlane`, the `Composite`
viewport); before this leaf a native scroll left them behind by the scroll amount. Their `getBounds` answers the
logical position; stock answers the clamped one.

The band height and the clipped drawing follow the ExpandBar mode (`-Dswt.widget.mode.ExpandBar=original`,
`ExpandBar.VIEWPORT`). The two defect fixes follow the Composite mode (`-Dswt.widget.mode.Composite=original`,
`ViewportPlane.ENABLED`), which owns the far child geometry: D1 is the L1 plane geometry itself, and D2 must go with it,
since without it a child kept logically would stay behind on a native scroll. The global `-Dswt.widget.mode=original`
turns everything off: stock. The per-widget `-Dswt.widget.mode.ExpandBar=original` alone keeps the Composite plane fixes
D1 and D2 (accepted by the coordinator, 2026-10-17). The rows after D2 in the table follow the ExpandBar mode; the
deferral of hidden controls needs both modes (it is ViewportPlane mediation, opted in by the ExpandBar viewport).

## Proof

Recorded differential `Test_org_eclipse_swt_widgets_ExpandBar_ViewportDifferential` (8 seeds x 120 steps, up to 115
items, content up to 52 104 pixels). The steps add up to 60 items at a time (every third with a `Label` control,
heights up to 920 pixels), dispose items, expand and collapse them by API and by clicks on the bar, change heights,
spacing, texts, images and the bar font, scroll with `WM_VSCROLL` (line, page, top, bottom, repeated) and the wheel,
resize the bar, and (from `aw21`) give the bar or the shell keyboard focus (`forceFocus`, with focus rectangles shown
through `WM_UPDATEUISTATE`) and press Up, Down, Space and Return (`WM_KEYDOWN`/`WM_KEYUP`): the focus item moves,
`showFocus` scrolls and the focus item expands or collapses. Each step records the scroll bar, client area, spacing, item headers, heights and states, the
bounds and visibility of item controls, the moves of item controls inside the native range, whether every control is
at the position the public API gives (relative to the first control), the Expand and Collapse events and the Paint
regions; each seed records the pixels (`WM_PRINT`).

Stock places controls away from their logical position in 10 steps of the recording (all beyond the native range).
The test accepts exactly D1 and D2 (`LOGICAL_WHERE_STOCK_CLAMPS`, in the widget's test): there the viewport line must
say every control is logical and equal the stock line in everything but the control y positions and the in-range move
count. Everywhere else, and for the events, Paint regions and pixels everywhere, the lines are equal.

Results, jars `aw20` and `aw21` (`aw21` with the keyboard and focus steps, the append and layout continuation, the
redraw culling and the deferral of hidden controls), invisible desktop:

| Comparison | Result |
|---|---|
| viewport mode vs the official recording | pass |
| original mode vs the official recording (equality) | pass |
| viewport mode vs the original-mode recording | pass |
| `Test_org_eclipse_swt_widgets_ExpandBar` + `ExpandItem` + `ExpandBar_Viewport`, viewport | 123 passed, 1 aborted (no foreground alpha on win32) |
| same, original | 121 passed, 2 aborted (no foreground alpha; the far-content acceptance applies to the viewport mode only) |
| `Test_org_eclipse_swt_widgets_ExpandBar_Viewport` (80 items of 600 pixels: controls logical after layout, at the end, back at the top, after a collapse), viewport | pass |

`Test_org_eclipse_swt_widgets_ExpandBar_Viewport` is the direct acceptance of the far content; it is skipped in the
original mode.

The survey of this SWT against the official jar before this leaf found no difference inside the native range in
either mode; the viewport mode already differed beyond it (L1 geometry), which is what the relation above admits.

## Non-claims

- Expanding and collapsing still visit every later item (O(n) integer work and one `GetWindowRect` per control);
  only the controls that are or come into view move natively.
- Resizing still resizes every item control natively (about 120 ms at 4000 items, the same as before the deferral):
  a size is never deferred, because answers of some controls depend on their native size (the line count of a
  wrapping `Text`, the height a drop-down `Combo` takes).
- A deferred control's native window keeps a stale off-screen position; native APIs that read it directly
  (accessibility rectangles, `MonitorFromWindow`) see that position, not the logical one, as for controls parked
  beyond the native range by L1.
- GTK and Cocoa are not measured here (GTK `GtkExpander` is native; Cocoa is drawn by its own code).
