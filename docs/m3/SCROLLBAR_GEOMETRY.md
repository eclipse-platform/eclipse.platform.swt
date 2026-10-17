# Custom scroll bar geometry (leaf L3)

Status: implemented (`ViewportScrollBarGeometry`, common); recorded differential passes against the official SWT 3.135.0
jar on win32. Plan: [`ALL_WIDGETS_VIEWPORT_PLAN.md`](ALL_WIDGETS_VIEWPORT_PLAN.md) leaf L3.

## Why

In the `custom` and `m3` modes SWT draws the scroll bars itself. Same contract, same visible behaviour means the
thumb of a drawn bar must have the native position and length for the same `ScrollBar` values, and a click or drag must
map back to the same selection. In the `viewport` mode nothing changes (native bars).

## Measured native rules (win32, 100 %)

Oracle [`scrollbar-geometry/evidence/ScrollBarGeometryOracle.java.txt`](scrollbar-geometry/evidence/ScrollBarGeometryOracle.java.txt)
with the official 3.135.0 jar on the invisible desktop: 600 random samples (orientation, bar length 40-560 px, minimum,
maximum up to 100,000, thumb, selection, enabled), native `getThumbTrackBounds` and `getThumbBounds`
([`oracle-official-seed1.tsv`](scrollbar-geometry/evidence/oracle-official-seed1.tsv)). SWT passes `nMin = minimum`,
`nMax = maximum`, `nPage = thumb + 1`.

1. Thumb length = `MulDiv (track, thumb + 1, maximum - minimum + 1)`, at least 8 px (`MulDiv` = `a * b / c` rounded half
   away from zero).
2. Thumb start (from the track start) = `MulDiv (track - length, selection - minimum, maximum - thumb - minimum)`.
3. A bar that cannot scroll (`maximum - thumb <= minimum`) starts its thumb one pixel before the track.
4. A disabled bar keeps the same geometry.
5. A track shorter than 8 px shows no thumb (the native report for such tracks is not a visible thumb).

All 574 samples with a visible thumb match these rules exactly; 26 samples had a track shorter than 8 px.

## API (package-private, integer arithmetic, no allocation)

`mulDiv`, `hasThumb`, `thumbLength`, `thumbStart`, `selectionAt` (drag: the inverse for the same rounding, clamped to
`[minimum, maximum - thumb]`), `hit` (part under a position: arrow, page, thumb).

## Tests (invisible desktop, jar `aw10`)

| Test | Result |
|---|---|
| `Test_ViewportScrollBarGeometry` (14 native samples, MulDiv, no thumb in a short track, drag inverse, hit parts) | 5/5 |
| `Test_org_eclipse_swt_widgets_ScrollBar_GeometryDifferential.recordedAgainstReference`: 8 seeds x 150 steps of setValues, resize, enable, selection, thumb; each step records values, track and thumb. Recorded with the official 3.135.0 jar (native thumb); the viewport run computes the thumb | pass |
| the same against the `-Dswt.widget.mode=original` recording | pass |
| `customMatchesNativeInOneRun` (native and computed bars side by side in one JVM) | pass |

## Non-claims

- Only 100 % scale is measured; the 8 px minimum thumb is assumed to be `SM_CYVTHUMB / 2` and is a parameter.
- Drag mapping (`selectionAt`) is the inverse of the native start formula; native thumb tracking needs real input and is
  not measured on the invisible desktop.
- The drawn bar's pixels (arrows, track, thumb painting) belong to the look and feel (plan section 9).
- GTK and Cocoa scroll bars are not measured.
