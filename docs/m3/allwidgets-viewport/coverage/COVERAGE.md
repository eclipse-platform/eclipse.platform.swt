# JaCoCo coverage of the all-widgets branch (brief rule 15)

Scope: every line the branch `m3/allwidgets-viewport-20261016` adds or changes outside the look and feel package
(that package is measured in [`../../look-and-feel/coverage/COVERAGE.md`](../../look-and-feel/coverage/COVERAGE.md)),
against its merge base with the fork's master, `600d1a44ef`:

- `WidgetMode` (the `m3` and `gpu` modes), measured as a whole class too;
- the ExpandBar viewport (`ExpandBar`, `ExpandItem`) and the hidden-child deferral of the plane mediation
  (`Composite.defersHiddenChildren`, `ViewportPlane`), docs/m3/EXPANDBAR_VIEWPORT.md;
- the CoolBar viewport (`CoolBar.placed`), docs/m3/TOOLBAR_VIEWPORT.md;
- `ViewportScrollBarGeometry` (a Javadoc-only change, measured as a whole class).

Result: every reachable changed line and branch is covered (153/153 lines, 212/212 branches). Two lines and 16
branches cannot be reached and are listed with the reason below, so the raw figures are 98.7 % of the changed lines
and 93.0 % of their branches. Reaching 99 % of the raw branches would mean removing those guards from the product
code, which is a decision for the owner, not for this coverage leaf.

Run: [`allwidgets_coverage.sh`](allwidgets_coverage.sh) `<worktree> <out-dir> <stamp>` after `build_and_tests.sh`
(jar stamp `aw-cov3`), JaCoCo agent 0.8.11, report by JaCoCo CLI 0.8.15, on the hidden desktop (`ui_lock.sh` +
`hidden_java.sh`). It records the official SWT 3.135.0 reference of the ExpandBar and CoolBar differentials, then runs
three JVMs into one exec file, so that both arms of every startup mode flag are measured:

| JVM | Suites | Result |
|---|---|---|
| default | `WidgetMode`, `ViewportScrollBarGeometry`, `ExpandBar_Viewport`, `ExpandBar_Coverage`, `CoolBar_Coverage`, `Composite`, `ScrolledComposite_Viewport`, stock `ExpandBar`, `ExpandItem`, `CoolBar`, `CoolItem`, both differentials against the official recording | 365 successful, 3 failed (the pre-existing `CoolItem` failures below), 3 aborted (assumptions) |
| `-Dswt.widget.mode=original` | the stock suites, both differentials, `ExpandBar_Coverage`, `CoolBar_Coverage` | 246 successful, 3 failed (the same `CoolItem` failures), 7 aborted (the cases that need the plane mediation, assumptions). The first attempt had one flaky step under paging pressure (ExpandBar differential, seed 1 step 86: 21 Move events of item controls in the stock code path against 20 recorded); the rerun into the same exec file passed it. |
| `-Dswt.widget.mode.ExpandBar=original` (Composite viewport on) | stock `ExpandBar`, the ExpandBar differential, `ExpandBar_Coverage`, `CoolBar_Coverage` | 116 successful, 0 failed, 1 aborted |

After a layout case was added to `barInsideAScrolledPlane`, `ExpandBar_Coverage` ran once more in the default mode
into the same exec file (11/11), and `REPORT_ONLY=1 allwidgets_coverage.sh ...` rebuilt the report from it.

Raw numbers: [`changed-lines.tsv`](changed-lines.tsv) (every executable changed line: missed and covered
instructions and branches) and [`jacoco.csv`](jacoco.csv) (whole classes).

## Changed lines

| File | Lines | Branches |
|---|---|---|
| `Composite.java` | 1/1 | - |
| `CoolBar.java` | 10/11 | 21/22 |
| `ExpandBar.java` | 55/55 | 68/74 |
| `ExpandItem.java` | 13/14 | 18/18 |
| `ViewportPlane.java` | 72/72 | 105/114 |
| `WidgetMode.java` | 2/2 | - |
| `ViewportScrollBarGeometry.java` | no executable line changed (Javadoc) | - |
| **Total** | **153/155 (98.7 %)** | **212/228 (93.0 %)** |
| **Reachable** (without the lines and branches listed below) | **153/153 (100 %)** | **212/212 (100 %)** |

## Whole classes

| Class | Lines | Branches | Note |
|---|---|---|---|
| `WidgetMode` | 20/20 (100 %) | 10/10 (100 %) | whole class |
| `ViewportScrollBarGeometry` | 27/27 (100 %) | 34/34 (100 %) | whole class |
| `ViewportPlane` | 284/305 | 273/314 | the plane mediation of the scroll viewport leaf; this branch adds the deferral, measured above by changed lines |
| `ExpandBar`, `ExpandItem`, `CoolBar`, `Composite` | see [`jacoco.csv`](jacoco.csv) | | upstream classes: the gate is the changed lines |

## Not covered, with reason

| Where | What | Why it cannot be covered |
|---|---|---|
| `CoolBar.placed`, lines 1180-1181 | `rect.right < rect.left` true and its `return false` (1 branch, 1 line) | `MapWindowPoints` with exactly two points into a mirrored window swaps left and right so that left is never greater (Win32 documentation); `Test_org_eclipse_swt_widgets_CoolBar_Coverage.mirroredBandChildren` runs `placed` in a mirrored rebar and the guard stays false. Kept as the twin of the same guard in `Control.boundsUnchanged`. |
| `ExpandBar.drawWidget`, line 328 | `clipRect != null` false (1) | both callers (`WM_PAINT`, `WM_PRINTCLIENT`) pass a rectangle; the guard keeps the stock contract of `ExpandItem.drawItem`, which accepts no clip. |
| `ExpandBar.getBandHeight`, line 360 | `nativeZoom == bandZoom` false (1) | the zoom of a widget changes only with rescaling at runtime across monitors of different scale (`Display.isRescalingAtRuntime`); the hidden desktop has one monitor at one zoom. |
| `ExpandBar.layoutItems`, lines 515 and 533 | `layoutBand >= 0` false and `band == layoutBand` false at an index above 0, and the `-1` arm (3) | need a band height change between two layouts with no layout from item 0 in between: a font change (`setFont`) and a zoom change (`handleDPIChange`) both lay out from item 0, and a disposed `Font` keeps its native handle (measured: the same height, `disposedFontIsMeasuredAgain`). Defensive: the O(1) start must never continue from positions computed with another band height. |
| `ExpandBar.indexOf`, line 485 | `0 <= hint` false (1) | `createItem` gives every item its index hint before the item is visible to any caller, so the field default `-1` is never read. |
| `ExpandItem.redraw`, line 407 | the `invalidate` call (1 line) | stock dead code: the stock condition `imageHeightInPixels > headerHeightInPixels` (line 405) cannot hold, because `getHeaderHeightInPixels` is at least the image height plus `IMAGE_MARGIN`; the branch only changed `InvalidateRect` to `invalidate` inside it. |
| `ViewportPlane.defers`, line 91 | `parent == null`, `control instanceof Decorations` and `!parent.defersHiddenChildren ()` true (3) | both callers (`mediates`, `setBounds`) call `defers` only for a control that is not a shell, after `parent.defersHiddenChildren ()` answered true. |
| `ViewportPlane.defers`, line 92 | `(flags & ~DEFERRABLE) != 0` and `(flags & SWP_NOZORDER) == 0` true (2) | every SWT path into `Control.setBoundsInPixels` passes `SWP_NOZORDER | SWP_NOACTIVATE | SWP_DRAWFRAME` (with `SWP_NOMOVE` or `SWP_NOSIZE`); z-order, show and hide go to `SetWindowPos` directly. The guards keep a future caller from deferring such a change. |
| `ViewportPlane.scrollDeferred`, line 479, and `exposeDeferred`, line 499 | `child.isDisposed ()` true (2) | the children come from `_getChildren ()` with no event in between, so none is disposed. |
| `ViewportPlane.scrollDeferred`, line 483 | the native rectangle of a small, undeferred plane child without a base meets the client area (1) | such a child keeps a plane only while it is parked past the native range (`place` and `rebaseChild` drop the plane of a child that fits), and the parked position (32767) lies outside every native client area, which is at most the plane band (24576). |
| `ViewportPlane.exposeDeferred`, line 495 | `display.planeScratch == null` true (1) | `ExpandBar.scrollWindow` calls it after `scrollDeferred`, which creates the scratch, and `Composite` calls `parentResized` only when the scratch exists. |

## Pre-existing failures (not of this branch)

`Test_org_eclipse_swt_widgets_CoolItem`: `test_getBounds` (expected 86, was 11), `test_setSizeII` (expected 50x50,
was 11x50) and `test_setControlLorg_eclipse_swt_widgets_Control` fail in the default and in the original mode, and
identically with the official SWT 3.135.0 jar on the hidden desktop (`<out-dir>/coolitem-official.log`: 21
successful, 3 failed). They are not caused by the CoolBar viewport.
