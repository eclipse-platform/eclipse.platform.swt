# Look and feel for the drawn modes (leaves LF1 and LF2)

Status: LF1 (seam and selection) and LF2 (defaults table and loader, five built-in tables) implemented in
`org.eclipse.swt.internal.laf`, with unit tests. No widget is drawn by them yet: the drawn realizations of
Tree, Table and List belong to the render-modes work, and LF3 (Button, Label) needs a drawn Button and Label first.

Plan: [`ALL_WIDGETS_VIEWPORT_PLAN.md`](ALL_WIDGETS_VIEWPORT_PLAN.md) section 9. Donors:
[`look-and-feel/FOSS_REUSE_DECISION.tsv`](look-and-feel/FOSS_REUSE_DECISION.tsv).

## LF1: seam and selection (`LookAndFeel`, `RendererFactory`)

- `-Dswt.widget.theme=native|flat|flatdark|swing|initiative31|<registered>` (rule 16) for every widget class and
  `-Dswt.widget.theme.<Widget>=...` for one (`LookAndFeel.requested`); `swt.lookAndFeel[.<Widget>]` is read as an
  alias (the render-modes theme registry reads the same names); the widget property wins; `native` is the default;
  values are trimmed and lower-cased.
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
- `flatdark`: extends `flat` and overrides the variables and a few derived colours;
- `swing` (alias `metal`): Swing's Metal (Ocean) look; every value measured from the JDK's own Swing by
  [`SwingDefaultsProbe`](look-and-feel/evidence/SwingDefaultsProbe.java.txt) (headless; UIManager keys and component
  geometry; output [`swing-defaults.output.txt`](look-and-feel/evidence/swing-defaults.output.txt)): selection
  `#b8cfe5`, grid `#7a8a99`, handle border `#6382bf`, focus `#a3b8cc`, Table rows 16 px, Tree rows 18 px,
  indent 20 (7 + 13), tree lines on;
- `initiative31` (alias `i31`): the look of the Initiative 31 custom-drawn widgets (prototype-skija
  `DefaultColorProvider.createLightInstance`, `Table.SELECTION_COLOR`, EPL-2.0 with its notice): selection
  `#e0eefe`, hover `COLOR_YELLOW`, grey expander triangles, accent `hsb (208, 1, 0.72)` = `#0062b8`.

The `native`/`flat`/`flatdark` colour values are this project's own; FlatLaf (Apache-2.0) contributed the approach
(properties, inheritance, variables, references, Less-style colour functions), not code or values. The `swing`
values are measured numbers (the JDK's GPL+CPE source stays out); the `initiative31` values are adapted EPL-2.0
constants (donor rows in the TSV).

## LF6: Swing-style pluggable look and feel, improved (user 2026-10-19)

"Pull in Swing-style themeability", "LAF logic and improve it": the seam of LF1/LF2 grows into Swing's pluggable look
and feel (`UIManager`, `UIDefaults`, `ComponentUI`, `SwingUtilities.updateComponentTreeUI`), with the data laid out
for the drawn modes (rule 18, DOOP) and each improvement measured against a `UIManager`-style baseline (a
`Hashtable<Object, Object>` of boxed values, a full tree update on a switch) in
[`look-and-feel/ALGORITHM_CATALOGUE.tsv`](look-and-feel/ALGORITHM_CATALOGUE.tsv) (rule 21). Native widgets and the
`original` and `viewport` modes are untouched: nothing below runs for a widget that has no UI delegate installed.

| Swing | M3 (`org.eclipse.swt.internal.laf`) | Improvement |
|---|---|---|
| `UIManager.setLookAndFeel`, `getLookAndFeel`, `PropertyChangeListener "lookAndFeel"` | `M3LookAndFeel.setLookAndFeel (name)` at runtime, `getLookAndFeel ()`, `addListener` | (3) the switch diffs the two themes and touches only the widgets whose delegates read a changed key: a repaint for colours, a relayout for metrics; no full tree update |
| `UIDefaults` (`Hashtable`, boxed values, `get (Object)`) | `M3UIDefaults`: keys interned once to int ids (`M3UIKeys`), values in typed lanes (ARGB, int, boolean, font id, insets as int quads), presence in a bitset | (1) O(1) array reads, no hashing and no boxing on the paint path |
| `UIManager.put` (developer defaults over the look and feel) | `M3UIDefaults.put`: a developer lane over the theme lanes, kept across switches | |
| `UIDefaults.LazyValue`, `ActiveValue`; FlatLaf derived colours | expressions compiled once per theme with their dependencies (CSR arrays) | (2) a changed base recomputes only the values derived from it |
| `JComponent.putClientProperty` (a map per component) | `M3LookAndFeel.override (control, key, value)`: one sparse lane per display (control slot x key id, open addressing) | (6) no map per widget; a widget without overrides costs nothing |
| `ComponentUI` (`installUI`, `uninstallUI`, `paint`, `getPreferredSize`) | `M3ComponentUI`, created by the look and feel's `RendererFactory`, kept in the widget's data; `M3RowUI` for the drawn rows of Tree, Table and List | each delegate declares the key ids it reads (the diff in (3)) |
| `SwingUtilities.updateComponentTreeUI` | `M3LookAndFeel.updateComponentTreeUI (control)` (full, the baseline) and the diff-driven update | |
| FlatLaf `IntelliJTheme`, `FlatPropertiesLaf`, `UIDefaultsLoader` | `M3ThemeImport`: IntelliJ `.theme.json` and FlatLaf `.properties` into the LF2 table format; `M3ThemeWatcher`: live reload of theme files during development | (4) |
| FlatLaf accent colour, `Component.accentColor` | `M3Palette`: an accent generates the palette (HSL ramps); a high-contrast theme from the platform; metrics scaled by the widget's zoom | (5) |

Built-in look and feels: `native`, `swing` (`metal`), `initiative31` (`i31`), `light` (alias of `flat`), `dark`
(alias of `flatdark`), and themes registered or imported at runtime. Inheritance (`@extends`) and the LF2 expression
syntax stay; FlatLaf's function names (`rgb`, `rgba`, `hsl`, `hsla`, `saturate`, `desaturate`, `spin`, `tint`,
`shade`, `fadein`, `fadeout`) and its unspaced call syntax are accepted on import.

Steps, each committed and pushed when green: (a) this design, the donor rows and the catalogue; (b) `M3UIKeys`,
`M3UIDefaults` and the sparse override lane, with the lookup benchmark; (c) `M3LookAndFeel`, `M3ComponentUI`, the
resource cache and the diff-driven switch, with the switch benchmark; (d) dependency tracking; (e) imports and live
reload; (f) palette, high contrast and DPI-aware metrics; (g) `light`/`dark` and the default row delegate; (h)
coverage (rule 15).

## Tests

`Test_org_eclipse_swt_internal_laf_LookAndFeel` (one test needs a display, so the suite runs on the hidden desktop): colours in every notation, integers, booleans
and insets, the colour functions with exact expected values, references and variables through inheritance, every
error path, every built-in table defining every catalogue key (with exact measured `swing` and donor `initiative31`
values and the `metal`/`i31` aliases), selection by property (`swt.widget.theme` over the `swt.lookAndFeel` alias)
and mode with the fallback to `native`, and registration of a table with a renderer factory. 11 tests, all pass
(jar `aw-cov3` on the hidden desktop). JaCoCo: 100 % of lines and 99.5 % of branches; the one uncovered branch is
listed with its reason in [`look-and-feel/coverage/COVERAGE.md`](look-and-feel/coverage/COVERAGE.md).

## Non-claims

- No widget reads a look and feel yet (LF3-LF5 need drawn widgets).
- A table resolved on a display thread keeps the platform colours of that moment; a later system theme change is
  not followed.
- Fonts are not part of the table yet.
