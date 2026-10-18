# Leaf 5: uniform emptiness notification (M3Emptiness)

Map row M3-EMPTINESS. Stock SWT has `SWT.EmptinessChanged` (56, `@since 3.118`), but only for Tree, and only on two
paths: the first item created (detail 0) and the last item disposed (detail 1). Stock `Tree.removeAll` and
`Tree.setItemCount` send nothing, and Table and List have no emptiness event at all. The tree viewport reproduces the
stock rules exactly (`TreeViewport.becameEmpty`, the EmptyOracle parity), so the stock event must not change.

## Surface (package `org.eclipse.swt.widgets`, public, `@since 3.138`)

| member | meaning |
|---|---|
| `M3Emptiness.EVENT` | the event type, `0x4D330001`: outside the range SWT uses for its own types, and distinct from `SWT.EmptinessChanged` so that both can be listened to |
| `BECAME_NON_EMPTY` (0), `BECAME_EMPTY` (1) | `event.detail`, the same values the stock event uses |
| `addListener(Tree / Table / List, Listener)` | records the current emptiness and adds the listener to the widget's event table under `EVENT` |
| `removeListener(Tree / Table / List, Listener)` | removes it; the recorded state goes with the last listener |

The event also carries `event.count`, the item count after the change (for a Tree, the root item count).

## Contract

- The event is sent when the item count crosses zero, through every structural path:
  - Tree: item creation and disposal, `removeAll`, `setItemCount`;
  - Table: item creation and disposal, `remove(int)`, `remove(int, int)`, `remove(int[])`, `removeAll`, `setItemCount`;
  - List: `add`, `remove` (all forms), `removeAll`, `setItems`, `setItemCount`.
- Nothing is sent when the count stays on the same side of zero: 1 to 2, 2 to 1, `setItem`, `clearAll`, or
  `setItemCount` to the same count. Child items of a Tree do not count.
- One operation sends at most one event. Nested public calls (`List.remove(String)` calls `remove(int)`, the native
  `List.setItemCount` calls `add` and `remove`) cannot repeat it. `List.setItem` (remove, then add) is held, so a
  one-item list never reports a transient empty state.
- The behavior is identical in the viewport and original modes, because it compares `getItemCount()` and that is
  mode-independent.
- The stock Tree `SWT.EmptinessChanged` is untouched.
- Platforms: the win32 widgets send the event. On GTK and Cocoa the listener is kept but not called yet (stated in the
  Javadoc).

## DOOP

- Listeners live in the widget's own event table.
- One small per-widget state object exists only while a listener exists: the last announced emptiness (boolean) and
  the open holds (int). It is kept as keyed widget data.
- The funnel is gated by `hooks(EVENT)`, so a widget nobody listens to pays one event-table probe per structural call.
- A listened Tree reads its root count only when a root item is created or disposed (the parent is checked first), or
  on `removeAll` and `setItemCount`.

## Widget seam (win32)

Each public mutator keeps its signature and Javadoc and becomes a two-line wrapper: it calls the unchanged body under a
new package-private name, then `M3Emptiness.changed(this)`. New early returns that the viewport agents add later are
covered automatically. `Table.createItem` has a single exit and gets one line at its end. `List.setItem` wraps its
body in `M3Emptiness.hold` / `release` (try/finally).

## Algorithm choices

See `ALGORITHM_CATALOGUE.tsv` (rule 21): the edge detection is a latched level comparison (last announced state)
rather than before/after counts per call, native notifications, or an asynchronous settle check.
