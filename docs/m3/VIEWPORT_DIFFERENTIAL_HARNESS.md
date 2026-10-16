# Shared recorded-differential harness (leaf L2)

Class: `tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit/ViewportDifferential.java`.
Self-test: `Test_ViewportDifferentialHarness` (no display needed).

Every viewport leaf proves "same contract, same visible behaviour" the same way: seeded random scripts of public API
operations run against two subjects that must behave alike, every step records one line, every seed compares pixels,
and any difference fails with the operation log of the seed (the reproduction).

## API

| Piece | Meaning |
|---|---|
| `ViewportDifferential.of (name, seedBase, ops)` | a harness; seed `k` uses `new Random (seedBase + k)` |
| `Ops<S>.next (random, log)` | draws the next operation and appends its name to the log |
| `Op<S>.apply (subject)` | performs it and answers what a caller observes |
| `.probe (label, probe)` | appends `\| label value` to every step line (bars, selection, focus, events, geometry, ...) |
| `.beforeSeed`, `.beforeStep`, `.afterStep` | reset to the seed's start state; clear event logs; drain the event loop |
| `.pixels (capture)` | compares pixels after every seed; captures repeat until two agree; differing pixels are saved as PNG |
| `.compare (expected, actual)` | both subjects in one JVM (plain vs viewport widget, near vs shifted scene) |
| `.compareWithRecording (subject, record, dir)` | the stock run (`-Dswt.widget.mode=original`) records one file per seed; the viewport run compares; no recording = assumption skip |

Helpers: `render (control)` (win32 `WM_PRINT` with children, synchronous; `Control.print` elsewhere), `parkCursor`,
`drain`, `settle` (wakes the display: posted events run only after a native message), `listen` (event codes with detail
and item text), `bar` (scroll bar state), `send`/`makeLParam` (win32 messages by reflection, so the test bundle compiles on
every platform), `save` (PNG under `-Dswt.viewport.evidence`, default `java.io.tmpdir`), `hash`.

## How each family maps onto it

| Family | Subjects | Probes (what Tree/Table/List record today) | Pixels |
|---|---|---|---|
| Tree (#196) | plain Tree and virtual (viewport) Tree | bar; selection; caret item; Expand/Collapse/Selection/DefaultSelection with item text; geometry after `update ()`: top item bounds, hit tests at rows 0 and 5 | `render` of each tree, the other hidden, focus on the printed one |
| Table, List | plain and virtual widget | the same, plus column/cell/image/text bounds for Table | as Tree |
| ScrolledComposite / Composite (L1) | stock run vs viewport run (recording); near vs far scene (compare) | origin, content and child bounds, client areas, `Display.map`; bars; Move/Resize/Paint/bar Selection | `render` of the ScrolledComposite (client area only when shifted) |
| Nebula Grid | plain and viewport Grid | the Tree probes with Grid's item and cell bounds | `render` of the grid; Nebula tests copy the class (as with `WidgetMode`) |

## Status

`Test_ViewportDifferentialHarness` 7/7. The ScrolledComposite differentials run on the harness: stock recording 1/1,
viewport run 21/21 together with the plane geometry and acceptance tests (jar `aw5`, win32). The Tree differential on
#196 is not migrated here (another agent's file); its probes are listed above so the move is mechanical.
