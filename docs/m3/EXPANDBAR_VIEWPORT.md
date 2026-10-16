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
control: create, paint, scroll a page (`WM_VSCROLL SB_PAGEDOWN`), expand or collapse the first item, resize. Medians
in microseconds, invisible desktop:

| Jar / mode | items | create | paint | scroll | expand | resize |
|---|---|---|---|---|---|---|
| official 3.135.0 | 1000 | 9 498 754 | 62 180 | 66 678 | 180 080 | 157 800 |
| official 3.135.0 | 4000 | 114 607 529 | 460 639 | 429 623 | 1 105 327 | 905 620 |
| this SWT, original (`aw17`) | 1000 | 8 032 529 | 74 187 | 79 726 | 192 070 | 196 333 |
| this SWT, original (`aw17`) | 4000 | 115 856 915 | 100 439 | 104 570 | 369 020 | 306 787 |
| this SWT, viewport (`aw20`) | 1000 | 1 971 779 | 2 109 | 25 899 | 25 331 | 29 494 |
| this SWT, viewport (`aw20`) | 4000 | 4 878 452 | 2 169 | 89 606 | 103 282 | 105 729 |

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

Item controls beyond 32767 pixels are placed logically by the L1 plane geometry (`ViewportPlane`, the `Composite`
viewport); before this leaf a native scroll left them behind by the scroll amount. Their `getBounds` answers the
logical position; stock answers the clamped one.

The band height and the clipped drawing follow the ExpandBar mode (`-Dswt.widget.mode.ExpandBar=original`,
`ExpandBar.VIEWPORT`). The two defect fixes follow the Composite mode (`-Dswt.widget.mode.Composite=original`,
`ViewportPlane.ENABLED`), which owns the far child geometry: D1 is the L1 plane geometry itself, and D2 must go with it,
since without it a child kept logically would stay behind on a native scroll. The global `-Dswt.widget.mode=original`
turns everything off: stock.

## Proof

Recorded differential `Test_org_eclipse_swt_widgets_ExpandBar_ViewportDifferential` (8 seeds x 120 steps, up to 115
items, content up to 52 104 pixels). The steps add up to 60 items at a time (every third with a `Label` control,
heights up to 920 pixels), dispose items, expand and collapse them by API and by clicks on the bar, change heights,
spacing, texts, images and the bar font, scroll with `WM_VSCROLL` (line, page, top, bottom, repeated) and the wheel,
and resize the bar. Each step records the scroll bar, client area, spacing, item headers, heights and states, the
bounds and visibility of item controls, the moves of item controls inside the native range, whether every control is
at the position the public API gives (relative to the first control), the Expand and Collapse events and the Paint
regions; each seed records the pixels (`WM_PRINT`).

Stock places controls away from their logical position in 10 steps of the recording (all beyond the native range).
The test accepts exactly D1 and D2 (`LOGICAL_WHERE_STOCK_CLAMPS`, in the widget's test): there the viewport line must
say every control is logical and equal the stock line in everything but the control y positions and the in-range move
count. Everywhere else, and for the events, Paint regions and pixels everywhere, the lines are equal.

Results, jar `aw20`, invisible desktop:

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

- Expanding, collapsing and resizing still move every later item and every item control (O(n) `SetWindowPos` and
  invalidations; 103 ms at 4000 items). A scroll re-places every control kept beyond the native range (90 ms at 4000
  items, 400 controls), where stock leaves them at wrong positions (D2).
- Keyboard navigation and focus drawing are not in the scripts (the invisible desktop does not give the bar focus).
- GTK and Cocoa are not measured here (GTK `GtkExpander` is native; Cocoa is drawn by its own code).
