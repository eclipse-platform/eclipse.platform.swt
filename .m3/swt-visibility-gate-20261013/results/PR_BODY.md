Draft. Leaf 1 of the SWT viewport line: the win32 visibility gate. Leaves 2 and 3 are stacked and queued in #127 and #128 (plan only).

User spec (2026-10-13): "IF A WIDGET IS NOT VISIBLE, WHY SHOULD IT EVEN BE PAINTED."

## What changes

A win32 control that is not effectively visible does no paint or layout work. Its skipped work is recorded as bits 25..31 of `Widget.state`:

| Bit | Name |
|---|---|
| 25 | `OCCLUDED` |
| 26 | `KNOWN` |
| 27 | `OFFSCREEN` |
| 28 | `DIRTY_PAINT` |
| 29 | `DIRTY_LAYOUT` |
| 30 | `DIRTY_MEASURE` |
| 31 | `DIRTY_DATA` |

The control catches up once when it becomes visible: one `updateLayout(true, true)`, one client invalidate, and one internal `VisibilityGate.CATCH_UP` event for consumer kinds.

A control is effectively visible when all of these hold:
- the WS_VISIBLE chain is set (this includes `setRedraw(false)`);
- the shell is not minimized;
- the control has a non-zero size;
- it is not clipped out by any ancestor's client area;
- for paint only: its shell is not covered and not on another virtual desktop.

Occlusion comes from a background tracker that adapts Chromium's z-order algorithm (BSD-3, notice included). Missing natives fail closed.

The query is a bit test with no allocation. The cache is invalidated by `WM_WINDOWPOSCHANGED`, `WM_SHOWWINDOW`, `WM_SETREDRAW` and `setParent`. Every first deferral posts one sweep, which recomputes the state from the OS.

**Public API is unchanged.**
- `isVisible()` and `getVisible()` are untouched.
- `getBounds`, `getLocation`, `getSize`, `getClientArea`, `toControl`, `toDisplay`, `Display.map` and `print` run deferred layouts on demand.
- `-Dswt.visibility.gate=false` restores every stock path.
- `-Dswt.visibility.occlusion=false` disables only the tracker.

**Consumers** use `org.eclipse.swt.internal.VisibilityGate` (`isOffscreen`, `defer`, `CATCH_UP`): StyledText, Nebula, List/Combo, and the Table/Tree/TabFolder lazy-data agent.

**Files:**
- win32 `widgets/`:
  - modified: `Control`, `Composite`, `Decorations`, `Display`, `Scrollable`;
  - new: `EffectiveVisibility`, `VisibilityCatchUp`, `OcclusionTracker`.
- common: `internal/VisibilityGate`.
- new test: `Test_org_eclipse_swt_internal_VisibilityGate`.

Design: `.m3/swt-visibility-gate-20261013/DESIGN.md`, which includes the bit table, the GTK/Cocoa mapping (design only, NOT_RUN) and the findings.

## Measured on win32

Three runs per cell (medians), on the primary monitor. The shells are ON_TOP, click-through and non-activating. There were 100 updates per scenario: `Table.clearAll` (VIRTUAL, 1M rows), a StyledText edit (50k lines), a Canvas redraw, a Label text change, `layout(true, true)` and `requestLayout`.

- `before` is stock `origin/master` classes.
- `after` is this branch.
- `off` is this branch with `-Dswt.visibility.gate=false`.

| Scenario | Mode | Paints while hidden | Layouts while hidden | SetData while hidden | UI CPU ms (p50) | Catch-up paints / layouts / SetData |
|---|---|---|---|---|---|---|
| hidden CTabFolder tab | before | 0 | 200 | 0 | 859 | 9 / 1 / 11 |
| | after | 0 | 99 | 0 | 515 | 9 / 1 / 11 |
| | off | 0 | 200 | 0 | 546 | 9 / 1 / 11 |
| scrolled out of ScrolledComposite | before | 0 | 200 | 0 | 1218 | 9 / 0 / 12 |
| | after | 0 | 100 | 0 | 828 | 11 / 0 / 12 |
| | off | 0 | 200 | 0 | 1265 | 9 / 0 / 12 |
| minimized shell | before | 0 | 200 | 0 | 1937 | 4 / 0 / 12 |
| | after | 0 | 99 | 0 | 1906 | 6 / 0 / 12 |
| | off | 0 | 200 | 0 | 2406 | 4 / 0 / 12 |
| shell covered by another window | before | **300** | 200 | **1200** | 3312 | 0 / 0 / 0 |
| | after | **0** | 200 | **0** | 2687 | 3 / 0 / 12 |
| | off | 300 | 200 | 1200 | 3625 | 0 / 0 / 0 |
| StackLayout hidden pane with a resize storm | before | 0 | 200 | 0 | 1406 | 9 / 0 / 12 |
| | after | 0 | 99 | 0 | 1062 | 9 / 0 / 12 |
| | off | 0 | 200 | 0 | 1187 | 9 / 0 / 12 |

**Measure** (`Layout.computeSize` on the payload) was 0 in every cell: no parent computes the payload's preferred size in these scenarios.

**Cloaked** (another virtual desktop): NOT_RUN, because switching the user's desktop would disturb the session.

**CPU** on this shared host is noisy. Before and off run the same stock code paths and differ by up to 40%.

**Why 99 layouts remain.** In the hidden scenarios, StyledText calls the public `getClientArea()` on every edit, and that call pulls the deferred layout on demand so the result stays correct. The StyledText owner removes it by asking `VisibilityGate.defer(this, MEASURE)` first.

**Stock already suppresses some paints.** Windows does not send WM_PAINT to minimized shells or to fully clipped children, so those stock paint counts are already 0. DWM does keep painting covered windows, and the gate removes that work.

### SWT examples harness (ControlExample, CustomControlExample)

Each harness ran 10 rounds of `layout(true, true)` plus `redraw()` on every tab page, with all pages hidden except one. Then it toured every tab once.

| Example | Mode | Storm paints | Storm Resize events | Storm CPU ms | Tour paints | Tour Resize events | Tour CPU ms | Errors |
|---|---|---|---|---|---|---|---|---|
| ControlExample (26 tabs) | before | 100 | 1265 | 7953 | 350 | 163 | 843 | 0 |
| | after | 100 | **0** | **390** | 360 | 295 | 1656 | 0 |
| | off | 100 | 1265 | 7390 | 350 | 163 | 703 | 0 |
| CustomControlExample (5 tabs) | before | 550 | 184 | 1890 | 340 | 23 | 281 | 0 |
| | after | 550 | **0** | **640** | 344 | 148 | 406 | 0 |
| | off | 550 | 184 | 2156 | 257 | 23 | 343 | 0 |

Hidden pages pay their layout once, on first show. For ControlExample, total CPU across storm and tour fell from 8796 ms to 2046 ms, and Resize events from 1428 to 295.

### Parity

**Public API.** After reveal, the gated control's API values equal those of a never-hidden twin in 15/15 runs. The values compared:
- `getSize`, `getClientArea` and child `getBounds`;
- `computeSize`;
- Table count and top index;
- StyledText char count, line count and top index;
- Label text and bounds;
- `toDisplay`.

The hidden-state API hash is also identical between before and after for every scenario. For example, a query while hidden returns the same values as stock, through the on-demand layout.

**Pixels.** Captured with screen copyArea, cropped to the gated control's visible area. The masked hash zeroes BORDER rings and the rounded shell corners. These are the counts where the gated control equals the never-hidden twin:

| Mode | Strict hash | Masked hash |
|---|---|---|
| after | 4/15 | 11/15 |
| before (stock) | 4/14 | 10/14 |
| off | 1/5 | 3/5 |

The rates in after are the same as in stock, so the gate adds no mismatch. The mismatches I inspected came from:
- foreign windows over the bench shells (another agent's StyledText showcase);
- Windows 11 rounded corners;
- DWM shadows from the neighbouring shell.

`Control.print` was rejected as the oracle: PrintWindow returned black client areas and the classic 3D border intermittently, on stock too. Capture obstruction is now recorded per run (column 31). A parity rerun that excludes obstructed captures is scripted for after the interview pause (`TESTS_TO_RUN.md`).

**Defects found by parity and fixed here:**
- Catch-up with `RDW_FRAME`, and a paint gate built on `ValidateRect`, both replaced SWT's themed CANVAS border with the classic 3D edge.
- A deferred layout could outlive `setLayout (null)`, causing an NPE at catch-up.
- The deferred list could hold duplicates.

### JUnit

- `Test_org_eclipse_swt_internal_VisibilityGate` passed 6/6. It covers:
  - hidden layout deferral with on-demand geometry;
  - open runs the deferred layout once;
  - minimize and hide;
  - ScrolledComposite clipping;
  - consumer catch-up;
  - scrollbar track painted after catch-up.
- Regression, stock run: 1419 tests, 62 failed. 60 of those are `Test_org_eclipse_swt_widgets_Display`, which fails with "Another Display is already associated" when it shares a JVM. The other two are `Shell.setImage` and `StyledText.backspaceAndDelete`.
- The gated regression run was stopped by the interview pause. It is the first item in `TESTS_TO_RUN.md`.

## Audits (docs only)

**`NEBULA_HIDDEN_WORK.tsv`** (hsoliwal/nebula `eab2a65c`, grep only): 69 rows over 62 files in 25 modules.

| Category | Rows |
|---|---|
| PAINT_IGNORES_CLIP | 39 |
| FULL_CLIENT_BUFFER | 9 |
| TIMER_NO_VIS_CHECK | 17 |
| TIMER_WITH_VIS_CHECK | 4 |

The timers include Gallery, Tiles, ProgressCircle, opal Launcher, HeapManager and Notifier, PrintPreview and PrintViewer, Oscilloscope, carousel and cwt animation. #10864 references this file.

**`CUSTOM_PAINT_CLIP.tsv`** (SWT custom package): AnimatedProgress, MouseNavigator and ViewForm ignore the paint clip. AnimatedProgress, BusyIndicator, CTabFolder and the Table/TreeEditor timers run with no visibility check.

## Recipe

Synexia PR hsoliwal/com.synexia#10920 holds crate `swt-visibility-gate-20261013`. It has 10 hash-pinned targets, and its SDK proof passes 3/3: exact postimages with a fixed point, a drift refusal and a missing-source refusal.

Not merged; the user merges.

🤖 Generated with [Claude Code](https://claude.com/claude-code)
