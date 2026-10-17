# Look and feel for the drawn modes (leaves LF1 and LF2)

Status: LF1 (seam and selection) and LF2 (defaults table and loader) implemented in
`org.eclipse.swt.internal.laf`, with unit tests. No widget is drawn by them yet: the drawn realizations of
Tree, Table and List belong to the render-modes work, and LF3 (Button, Label) needs a drawn Button and Label first.

Plan: [`ALL_WIDGETS_VIEWPORT_PLAN.md`](ALL_WIDGETS_VIEWPORT_PLAN.md) section 9. Donors:
[`look-and-feel/FOSS_REUSE_DECISION.tsv`](look-and-feel/FOSS_REUSE_DECISION.tsv).

## LF1: seam and selection (`LookAndFeel`, `RendererFactory`)

- `-Dswt.lookAndFeel=native|flat|flatdark|<registered>` for every widget class and
  `-Dswt.lookAndFeel.<Widget>=...` for one (`LookAndFeel.requested`); the widget property wins; `native` is the
  default; values are trimmed and lower-cased.
- `LookAndFeel.of (widget, mode)` answers the requested look and feel only when the widget's `WidgetMode` draws it
  (`custom`, `m3`, `gpu`); in `viewport` and `original` the pixels are native, so it answers `native`.
- `LookAndFeel.named (name)` loads and resolves a table once per name and shares it. An unknown name or a table
  that cannot be read falls back to `native` with an info log line; `native` itself never falls back. A table
  resolved on a thread without a display (with the fallback platform colours) is not cached.
- `RendererFactory.create (type, widget)`: the renderers of a look and feel, one per drawn widget class, like a
  Swing UI delegate (measure, paint, hit-test; no widget state). `null` means "use the widget's default renderer
  with this look and feel's defaults". `LookAndFeel.register (name, table, renderers)` adds a look and feel (a table
  that may extend a built-in one, and/or a factory).

## LF2: defaults table (`LookAndFeelDefaults`, `LookAndFeelLoader`, `LookAndFeelThemes`)

Properties format, resolved once into packed ints (a colour is ARGB, a boolean 0 or 1, insets four ints); painting
looks a key up once (`index`) and then reads `value (index)`:

| Form | Meaning |
|---|---|
| `key = value` | one entry; `#` or `!` starts a comment line; a later line overrides an earlier one |
| `@extends = name` | inherit every key of another table and override some |
| `@name = value` | a variable (not a key), used as `@name` |
| `#rgb`, `#rgba`, `#rrggbb`, `#rrggbbaa` | colours |
| `12`, `true`, `1,2,3,4` | integer, boolean, insets (top, left, bottom, right) |
| `$Other.key` | another key's value, resolved after inheritance (a derived table that overrides a key or a variable changes every key that refers to it) |
| `darken (c, n%)`, `lighten (c, n%)` | HSL lightness moved by n points |
| `fade (c, n%)`, `mix (a, b, n%)` | alpha set to n percent; n percent of `a` and the rest of `b` |
| `system (COLOR_...)` | a platform colour by SWT constant name (`Display.getSystemColor` on a display thread, the Windows light defaults otherwise) |

Errors name the table, the key and the cause: unknown keys and variables, circular references or `@extends`,
wrong argument counts, values that are not colours where colours are needed.

Built-in tables (Java text in `LookAndFeelThemes`, so they ship in the class files on every platform), each
defining every key of `LookAndFeelThemes.KEYS` (77 keys: `Component.*`, `Button.*`, `Label.*`, `Link.*`, `Text.*`,
`List.*`, `Table.*`, `Tree.*`, `ScrollBar.*`, `TabFolder.*`, `ProgressBar.*`, `ToolTip.*`):

- `native`: platform system colours, so a drawn widget looks like its native neighbours;
- `flat`: a light flat theme derived from seven variables;
- `flatdark`: extends `flat` and overrides the variables and a few derived colours.

The colour values are this project's own; FlatLaf (Apache-2.0) contributed the approach (properties, inheritance,
variables, references, Less-style colour functions), not code or values.

## Tests

`Test_org_eclipse_swt_internal_laf_LookAndFeel` (no display needed): colours in every notation, integers, booleans
and insets, the colour functions with exact expected values, references and variables through inheritance, every
error path, every built-in table defining every catalogue key, selection by property and mode with the fallback to
`native`, and registration of a table with a renderer factory. 8 tests, all pass (standalone run against jar
`aw22` plus these classes).

## Non-claims

- No widget reads a look and feel yet (LF3-LF5 need drawn widgets).
- A table resolved on a display thread keeps the platform colours of that moment; a later system theme change is
  not followed.
- Fonts are not part of the table yet.
