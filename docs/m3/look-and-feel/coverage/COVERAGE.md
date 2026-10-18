# JaCoCo coverage of `org.eclipse.swt.internal.laf` (brief rule 15)

Run: `laf_coverage.sh <worktree> <out-dir> <stamp>` after `build_and_tests.sh` (jar stamp `aw-cov3`, branch
`m3/allwidgets-viewport-20261016`), JaCoCo agent 0.8.11 (`includes=org.eclipse.swt.internal.laf.*`), report by
JaCoCo CLI 0.8.15; the suite `Test_org_eclipse_swt_internal_laf_LookAndFeel` ran on the hidden desktop
(`ui_lock.sh` + `hidden_java.sh`) because one test creates a `Display`. 11 tests, 11 successful. Raw numbers:
[`jacoco-laf.csv`](jacoco-laf.csv).

| Class | Lines | Branches |
|---|---|---|
| `LookAndFeel` | 53/53 (100 %) | 32/32 (100 %) |
| `LookAndFeelDefaults` | 34/34 (100 %) | 28/28 (100 %) |
| `LookAndFeelLoader` | 85/85 (100 %) | 68/68 (100 %) |
| `LookAndFeelLoader.Resolver` | 83/83 (100 %) | 58/59 (98.3 %) |
| `LookAndFeelLoader.Value` | 5/5 (100 %) | - |
| `LookAndFeelThemes` | 17/17 (100 %) | 2/2 (100 %) |
| `RendererFactory` (+ `NONE`) | 3/3 (100 %) | - |
| **Total** | **280/280 (100 %)** | **188/189 (99.5 %)** |

## Not covered, with reason

| Where | What | Why it cannot be covered |
|---|---|---|
| `LookAndFeelLoader.Resolver.value`, line 170 | the `resolving.isEmpty ()` true arm of the "unknown key" message | defensive message building: `resolve ()` only asks for keys the table has, so an unknown key or variable is always found inside a reference chain (`resolving` is never empty there). One branch of 189. |

## What the suite exercises

Colours in every notation, integers, booleans, insets and the packed `value (index)` read; `format` of every kind
and of a missing key; the fallback of every typed getter for a missing key and for a key of another kind; every
colour function with exact values, including clamping of lightness and alpha, a bare percentage, and the HSL
branches for red, green, blue and magenta hues; references, variables and inheritance; every error path (circular
references and `@extends`, unknown key and unknown variable, bad colours, bad percentages, wrong arity, unknown
function, unclosed function, non-colour arguments, missing `=`, a system-colour cause without a message); every
built-in table defining every catalogue key with exact `swing` and `initiative31` values and the aliases; property
selection (`swt.widget.theme` over `swt.lookAndFeel`, widget over global, blank values); `of` in a drawn and in a
native mode; `native` itself never falling back; registration of tables and renderer factories; resolution and
caching on a display thread with the display's platform colours, and the debug record of that resolution (FINE on: the
supplier runs on the calling thread, the SWT log thread only publishes).
