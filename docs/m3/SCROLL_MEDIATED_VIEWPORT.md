# Scrollable composites as viewports: plane geometry (leaf L1, win32 first)

Status: design, measured native rules, implementation on this branch. Plan: [`ALL_WIDGETS_VIEWPORT_PLAN.md`](ALL_WIDGETS_VIEWPORT_PLAN.md).

## Goal

A control's bounds are logical: any `int` position and size that the API accepts are answered back unchanged, and the
control is drawn and hit where those bounds say, even when they leave the range the native window system can store.
ScrolledComposite, a Composite with `H_SCROLL`/`V_SCROLL` that scrolls by moving children, and a Canvas with children
keep their public contract, their pixels and their events. No new API.

## Measured native rules (stock win32, `scroll-viewport/evidence/`)

Oracle `ScrollGeometryOracle` on master `ef565e23ce`, Windows 11, JDK 21, `-Dswt.widget.mode=original`:

1. A child window's position is clamped to [-32768, 32767] pixels and its size to 65535 pixels (both axes).
   `getBounds ()` reads the native rectangle, so it answers the clamped values: `setSize (300, 70000)` answers 65535.
2. ScrolledComposite past the limit: the bar selection follows the request (39703 of 40000) while `getOrigin ()` stops at
   32768; a child at the end of the content is clamped to y = 32767 and never painted.
3. At 200 % (`-Dswt.autoScale=200`) the same pixel limits are 16384 points: a 20000-point form is already broken.
4. Inside the limit a scroll moves the content window with copied bits: the content receives one Paint event for the
   exposed strip (10 px step: `300x10`); a jump larger than the client area repaints the client area.
5. Native scroll messages: `SB_LINEDOWN` = bar increment, `SB_PAGEDOWN` = page; the content's `Move` precedes the bar's
   `Selection`; details `ARROW_DOWN`, `PAGE_DOWN`, `END`, `ARROW_UP`, `HOME`; the wheel scrolls three increments with detail 0.
6. `Canvas` with `V_SCROLL` accepts `Integer.MAX_VALUE` as maximum; `Canvas.scroll` blits and paints the exposed strip.

## Design: planes

Draw2d's `Viewport` keeps figure coordinates logical and applies the view offset only when translating to the parent.
L1 does the same at the native boundary of win32 controls (`ViewportPlane`, win32) with the arithmetic in common code
(`ViewportPlaneGeometry`).

- **Logical bounds.** For every control the API values (pixels) are the logical bounds relative to the parent's logical
  client area. They are stored only when they cannot be derived from the native window: a control whose native position
  is clamped (a *parked* control) or whose size exceeds the band (a *plane*). All other controls keep no extra state and
  their logical bounds are `native + parent base`.
- **Band.** A plane's native window is at most `BAND` = 24576 pixels on an axis, so every native position and size stays
  in the 16-bit range with margin. Per axis the plane has a **base** `b`: the plane's logical coordinate `b` is its native
  coordinate 0. Its children are placed natively at `logical - b`; a child that falls outside the 16-bit range is clamped
  (parked), which is invisible because the band is smaller than the clamp distance.
- **Choosing the base.** The base keeps the part of the plane visible in its parent's native client area inside the band:
  while the visible range `[parentBase - y, parentBase - y + visibleExtent)` stays inside `[b, b + band)`, `b` does not change;
  otherwise `b` is re-centred on the visible range, clamped to `[0, size - band]` and rounded down to a multiple of the
  control's zoom (so `b` pixels are an exact number of points at every scale factor).
- **Rebase without repaint.** A base change happens at a fixed logical origin, before the move that caused it: the plane
  and its children are repositioned with `SWP_NOREDRAW | SWP_NOCOPYBITS` so that every screen pixel stays where it was
  (the plane moves by `Δb`, its children by `-Δb`); a pending update region is shifted by `Δb`. Then the requested move runs
  through the stock `SetWindowPos`, which blits and exposes the same strip as stock. Rebases are rare: one per
  `(band - visible extent) / 2` pixels of scrolling.
- **Events.** For a plane or parked control, the native `WM_MOVE`/`WM_SIZE` caused by the mediation do not reach the
  application (`Display.planeQuiet`); `Move` and `Resize` (with the Composite layout that `WM_SIZE` performs and the
  `CANVAS` invalidation) are sent from the logical change, in stock order (Move, then Resize). In-band children of a plane
  use the stock path unchanged, translated by the base, so their deferred layout batching and native events are stock.
- **Answers.** `getBounds`, `getLocation`, `getSize`, `getClientArea`, `toDisplay`, `toControl` and `Display.map` answer
  logical coordinates; mouse events of a plane and its Paint events are translated by the base, and the paint GC's window
  origin is offset by the base so applications draw in logical coordinates. `redraw (x, y, w, h)` is translated.
- **Scroll bars.** ScrolledComposite already sets its bars from the content's logical size and its origin from the
  content's logical location (`ViewportRuntime.solvePixels`), so it needs no change: the bars and the origin agree again.
- **Mode.** `WidgetMode.of ("ScrolledComposite", WidgetMode.VIEWPORT)` would only gate one class; the mediation is in
  `Control`, so it is gated as `WidgetMode.of ("Composite", WidgetMode.VIEWPORT) == WidgetMode.VIEWPORT`
  (`-Dswt.widget.mode.Composite=original` restores stock geometry). Controls that fit natively never take the new path,
  so in-range behaviour is the stock behaviour in both modes.

## Hooks (recipe `scroll-viewport/recipe`, `M3InsertStatements`)

| Class.method | Position | Statement |
|---|---|---|
| `Control` | member | `ViewportPlane plane;` (null for every control that fits natively) |
| `Control.setBoundsInPixels (6)` | METHOD_START | mediated controls and children of planes go through `ViewportPlane.setBounds` |
| `Control.getBoundsInPixels`, `getLocationInPixels`, `getSizeInPixels` | METHOD_START | logical answers |
| `Scrollable.getClientAreaInPixels` | METHOD_START | logical client area of a plane |
| `Control.toDisplayInPixels`, `toControlInPixels` | before/after the native mapping | plane base |
| `Display.mapInPixels` (point, rectangle) | before/after `MapWindowPoints` | plane base of `from` and `to` |
| `Control.WM_MOVE`, `Control.WM_SIZE`, `Composite.WM_SIZE` | METHOD_START | quiet while the mediation moves windows |
| `Widget.sendMouseEvent` | after the event location | plane base |
| `Composite.WM_PAINT` | before each Paint dispatch | event bounds and GC window origin |
| `Control.redrawInPixels` | METHOD_START | plane base |

## Tests

- `ViewportPlaneGeometryTest` (no display): base choice, hysteresis, zoom alignment, clamping, exact point conversion.
- `Test_org_eclipse_swt_widgets_ScrolledComposite_Viewport` (acceptance; skips unless the plane is active): logical
  `getSize`/`getLocation`/`getOrigin` past 32767 px, bar and origin agree, the end of a 1,000,000 px content is painted,
  rebases do not repaint, a scroll step paints only the exposed strip.
- `Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential`: seeded scripts over `setOrigin`, `setMinSize`,
  `setExpandHorizontal/Vertical`, `setContent`, scroll bar messages, wheel, shell resize and child layout run on two
  ScrolledComposites, one whose content is in the native range and one whose content is shifted far beyond it (the same
  children, `SHIFT` pixels further down). Each step records `answer | bars | origin | events` (answers shifted back) and,
  per seed, the pixels of both; any difference fails with the operation log.

## Non-claims and gaps

- GTK3/X11 has the same 16-bit window protocol; GTK and Cocoa are not changed or measured here.
- A parked `Combo` reports the bounds it was given (stock answers the native edit height).
- Background images and theme backgrounds of a plane are aligned to the native band, not to the logical origin.
- Visible extents above `BAND / 2` (12288 px) inside nested planes are not handled.
- `Canvas.scroll`, carets, IME composition and `new GC (plane)` outside Paint are not translated yet (next leaf).
