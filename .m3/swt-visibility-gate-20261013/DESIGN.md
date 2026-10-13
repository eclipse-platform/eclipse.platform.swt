# SWT visibility gate (win32), 2026-10-13

Spec (user, 2026-10-13): "IF A WIDGET IS NOT VISIBLE, WHY SHOULD IT EVEN BE PAINTED."

Rule: a control that is not effectively visible does no paint, layout,
measurement, data population, idle pass or animation work. It records what is
dirty in bits and catches up exactly once when it becomes visible. Paint covers
only the damaged rectangle.

## 1. Effective visibility

`offscreen(c)` is true when any of these holds:

| Reason | Source | Cached | Gates |
|---|---|---|---|
| WS_VISIBLE chain false (hidden, ancestor hidden, `setRedraw(false)`) | `OS.IsWindowVisible(handle)`, live | no | paint, layout |
| Shell minimized | `OS.IsIconic(shell)` | `OFFSCREEN` bit | paint, layout |
| Zero-size window | `GetWindowRect` | `OFFSCREEN` bit | paint, layout |
| Fully clipped by any ancestor client area (ScrolledComposite, Composite bounds) | screen-space intersection of `GetWindowRect(c)` with every ancestor's `GetClientRect`+`MapWindowPoints`, stopping at the Shell | `OFFSCREEN` bit | paint, layout |
| Shell fully covered by opaque windows above it | `OcclusionTracker` background thread | `Shell.OCCLUDED` bit | paint |
| Shell on another virtual desktop | `IVirtualDesktopManager.IsWindowOnCurrentVirtualDesktop` (existing `COM.VtblCall`) | `Shell.OCCLUDED` bit | paint |

The WS_VISIBLE chain is checked live because `WM_SETREDRAW` toggles WS_VISIBLE
without sending a notification. The geometric part is cached; it changes only
through `WM_WINDOWPOSCHANGED` / `WM_SHOWWINDOW`, which invalidate the cache for
the receiver's subtree. Occlusion is asynchronous and therefore gates paint only;
layout stays synchronous for it.

### Bit allocation (win32 `Widget.state`, bits 25..31 were free)

| Bit | Name | Meaning |
|---|---|---|
| 1<<25 | `Shell.OCCLUDED` | Shell covered or on another virtual desktop (shell only) |
| 1<<26 | `VisibilityGate.KNOWN` | `OFFSCREEN` is current for this control |
| 1<<27 | `VisibilityGate.OFFSCREEN` | cached geometric offscreen (iconic, zero size, clipped) |
| 1<<28 | `VisibilityGate.DIRTY_PAINT` | a redraw or WM_PAINT was skipped |
| 1<<29 | `VisibilityGate.DIRTY_LAYOUT` | a layout was deferred (LAYOUT_NEEDED marks kept) |
| 1<<30 | `VisibilityGate.DIRTY_MEASURE` | owner-specific measurement skipped (consumer kind) |
| 1<<31 | `VisibilityGate.DIRTY_DATA` | owner-specific data/idle/animation work skipped (consumer kind) |

Cache invariant: `KNOWN(c)` implies `KNOWN(parent(c))`. Recompute walks the
parent chain top-down from the Shell once (one reusable `Control[]` on Display),
setting `KNOWN`/`OFFSCREEN` on every node; invalidation clears `KNOWN` down the
subtree and stops at the first node already unknown. A query on a known control
is a bit test plus `IsWindowVisible`; no allocation.

GTK and Cocoa: bits 26..31 are free on both (`gtk Widget` uses up to 1<<25,
`cocoa Widget` up to 1<<21). Design only, NOT_RUN on this host: GTK maps
`OFFSCREEN` to `gtk_widget_get_mapped` + allocation clip + `GDK_TOPLEVEL_STATE_MINIMIZED`
and occlusion to `GdkSurface` `is-mapped` / X11 `VisibilityNotify`; Cocoa maps it
to `NSView.isHiddenOrHasHiddenAncestor` + `visibleRect` and occlusion to
`NSWindow.occlusionState & NSWindowOcclusionStateVisible`.

## 2. Gates

| Work | Gate point | Dirty record | Catch-up |
|---|---|---|---|
| `redraw()`, `redraw(x,y,w,h,all)`, internal `redrawInPixels` | `Control.redrawInPixels` | hidden chain: stock drop (Windows repaints on show); other reasons: `DIRTY_PAINT` | one `RedrawWindow(RDW_ERASE|RDW_INVALIDATE|RDW_FRAME|RDW_ALLCHILDREN)` |
| OS-originated `WM_PAINT` (DWM still paints covered/iconic/clipped windows) | `Control.windowProc` before dispatch | `ValidateRect` + `DIRTY_PAINT` | same |
| `update()` | `Control.update(boolean)` | none (nothing visible to flush) | n/a |
| `layout()`, resize-driven layout, `requestLayout` | `Composite.updateLayout` after `findDeferredControl` | `DIRTY_LAYOUT`, LAYOUT_NEEDED kept | `updateLayout(true,true)` once, outermost first |
| Table/Tree `SetData` | falls out of the paint gate (LVN_GETDISPINFO / TVN_GETDISPINFO arrive only while painting or measuring) | `DIRTY_PAINT` | repaint pulls only visible rows |
| StyledText/Nebula/List/Combo idle, measure and timer work | consumer calls `org.eclipse.swt.internal.VisibilityGate.defer(control, kinds)` | `DIRTY_MEASURE` / `DIRTY_DATA` | internal event `VisibilityGate.CATCH_UP` with `detail = kinds` |

Catch-up order: every deferral enlists the control once on `Display`
(`Control[] visDirty`, reused, compacted on sweep). A sweep is posted (one
`asyncExec`, never more than one outstanding) on: `WM_SHOWWINDOW`,
`WM_WINDOWPOSCHANGED` with move/size/show/hide, `WM_ACTIVATE`, `setRedraw(true)`,
`setVisible(true)`, occlusion change. `setVisible(true)` and `setRedraw(true)`
also run the pending layouts of the receiver's subtree synchronously before the
window is shown, so the first frame is laid out (Qt `show_recursive` pattern).

## 3. Public API compatibility

- `isVisible()`, `getVisible()`, `isLayoutDeferred()` are unchanged.
- Results are computed on demand. `getBounds`, `getLocation`, `getSize`,
  `getClientArea`, `toControl`, `toDisplay`, `Display.map`, `print` first run any
  deferred layout of the receiver's ancestors (outermost first, gate bypassed).
  The fast path is one int test (`display.visPendingLayouts == 0`).
- `computeSize` never depended on deferred layout results and is not gated.
- Timing difference (same as stock `setLayoutDeferred(true)`): `SWT.Resize` /
  `SWT.Move` events of descendants of an offscreen composite are delivered at
  catch-up or at the first geometry query, not inside the original `layout()` call.
- Kill switches, read once: `-Dswt.visibility.gate=false` restores every stock
  path; `-Dswt.visibility.occlusion=false` disables only the occlusion tracker.

## 4. Occlusion tracker (Chromium-derived, BSD-3)

Background daemon thread "SWT Occlusion Tracker", started with the first Shell,
stopped with the Display. Each pass walks the z-order front to back
(`GetWindow(GetDesktopWindow(), GW_CHILD)`, then `GW_HWNDNEXT`), keeps a GDI
region of covered desktop, and for every visible, non-iconic top-level window of
this Display's thread tests whether its client rect minus the covered region is
`NULLREGION`. Occluder predicate (Chromium `IsWindowVisibleAndFullyOpaque`):
visible, not iconic, not `WS_EX_TRANSPARENT`, `WS_EX_TOOLWINDOW` only for
`Shell_TrayWnd`, layered only with `LWA_ALPHA` 255 and no colorkey, non-empty rect,
not a tooltip class, on the current virtual desktop. Missing natives fail closed:

- no `DWMWA_CLOAKED`: classes `ApplicationFrameWindow` and
  `Windows.UI.Core.CoreWindow` never occlude;
- no `DWMWA_EXTENDED_FRAME_BOUNDS`: occluder rects of sizable, non-maximized
  windows shrink by twice the system frame width;
- `IVirtualDesktopManager` unavailable: tracker disabled.

Cadence: 100 ms while any shell is occluded, 250 ms otherwise; occlusion must
hold for two passes before it is published, un-occlusion is published at once.
Results are published as a `long[]` of occluded HWNDs and applied on the UI
thread as `Shell.OCCLUDED` bits. `WM_ACTIVATE` and z-order `WM_WINDOWPOSCHANGED`
on a shell clear `OCCLUDED` immediately (fail toward painting).

## 5. Consumer contract (StyledText, Nebula, List/Combo agents)

```java
import org.eclipse.swt.internal.VisibilityGate;
if (VisibilityGate.defer(control, VisibilityGate.MEASURE)) return; // skipped, recorded
control.addListener(VisibilityGate.CATCH_UP, e -> { if ((e.detail & VisibilityGate.MEASURE) != 0) remeasure(); });
boolean hidden = VisibilityGate.isOffscreen(control, true);          // true = include occlusion (paint)
```

`isOffscreen` and `defer` are allocation-free. On GTK/Cocoa (no port installed)
both return false, so consumers fall back to stock behaviour.

## 6. Paint clip

Core custom-drawn paths (`Composite.WM_PAINT` with and without
`SWT.DOUBLE_BUFFERED`) already size buffers and the Paint event to
`PAINTSTRUCT.rcPaint`. Custom-package and Nebula widgets that fill the whole
client area are listed in `NEBULA_HIDDEN_WORK.tsv` and `CUSTOM_PAINT_CLIP.tsv`
(audit only; owned by other agents).

## 7. Proof plan

`bench/VisibilityGateBench.java`, one JVM per scenario, `-Xmx1g`, memory gated:
hidden CTabFolder tab (1M-row virtual Table + large StyledText), minimized shell,
ScrolledComposite scrolled out, shell covered by an ON_TOP shell, cloaked shell
(NOT_RUN: switching the user's virtual desktop would disturb the session).
Counters: Paint events, Layout.layout calls, Layout.computeSize calls, SetData,
UI thread CPU (ThreadMXBean). Before = origin/master classes, after = gate
classes, plus after with `-Dswt.visibility.gate=false`. Parity: `Control.print`
SHA-256 of the gated control after it becomes visible vs a twin that was never
hidden, and equality of `getBounds`/`getClientArea`/`getItemCount`/`getText`.
