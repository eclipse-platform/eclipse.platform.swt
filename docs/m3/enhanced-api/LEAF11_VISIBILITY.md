# Leaf 11: visible-range observer (M3VisibilityObserver)

Map row M3-VISIBILITY-OBSERVER (IntersectionObserver, Nebula Grid `GridVisibleRangeSupport`, RecyclerView attach and
detach). M3Prefetch (leaf 9) reports the rows a widget holds. This leaf reports the rows the user can see, for
impressions, lazy decorations, and starting and stopping per-row animations.

## Surface (package `org.eclipse.swt.widgets`, public, `@since 3.138`)

| member | meaning |
|---|---|
| `M3VisibilityObserver.EVENT` (`0x4D330003`) | event type; `detail` is `ENTERED` (0) or `LEFT` (1); `start` and `end` are one range (inclusive; Tree: `M3RowMath` visible rows) |
| `addListener` / `removeListener(Table / List / Tree, Listener)` | the first listener hears the current visible range as `ENTERED` |

The visible range is `M3ItemSelection.getVisibleRange`: the rows that intersect the client area.

## Contract

- After each change the listeners have received `LEFT` for old minus new, then `ENTERED` for new minus old. Each is
  at most two ranges, and together they partition the change.
- No event is sent when the range is unchanged.
- Triggers:
  - scrolling (scroll bar, wheel, keyboard and `setTopIndex` / `setTopItem` in the viewport mode);
  - resize;
  - expand and collapse;
  - structural changes.
  Each settles to one pair of announcements. The check runs once at the end of the triggering event (coalesced with
  `asyncExec`), and immediately at the viewport window commits.
- Ranges are delivered, never items.
- In the original mode, programmatic `setTopIndex` without a following paint or event is announced at the next
  trigger. This is stated in the Javadoc.

## DOOP and seam

- One latch per listened widget (the last announced range plus a scheduled flag) in keyed data.
- Listeners are kept in the event table.
- The viewport window seams of leaf 9 (`TreeViewport.ensureWindow`, `ListViewport.layout` and `rebuild`,
  `TableViewport.cacheHint`, and the Table structural seam) also call the observer.
- Common listeners for Resize, Expand, Collapse and the vertical scroll bar Selection are installed only while a
  listener exists.

## Algorithm choices

The interval difference of leaf 9 is reused, so `ALGORITHM_CATALOGUE.tsv` gets no new row.
