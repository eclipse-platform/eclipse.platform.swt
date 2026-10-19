# Native verification of viewport stroke-culling PR #38

Verified source: `d61516c37074fab56deb82f2d04465a8b965a50d` (merged SWT #38).
This receipt adds executed results to the earlier source-only proof boundary.
It changes no production source, test, recipe, native binary or CI gate.

## Result

- Java 21.0.12 compilation succeeded for complete selected SWT source roots:
  GTK 484 files, Win32 663 files and Cocoa 624 files.
- Real GTK3/X11 execution under Xvfb: **17 tests passed, 0 failed, 0 skipped**.
- Differential baseline: substitute the graph from merged pre-repair master
  `b678db3bae164f5334f098348e79c57c1877d06d` while running the same expanded
  test class: **12 passed, 5 failed**. All five failures are missing-pixel
  comparisons against the immediate native SWT GC.

The five regressions cover thick lines, square caps, rectangle joins,
fractional line widths and a child stroke crossing the coarse group-culling
boundary. All inherited affine, layer, coordinate mapping, template-reuse and
coarse subtree tests remain enabled.

## Source identity

| File | SHA-256 |
| --- | --- |
| `bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/internal/ViewportPaintGraph.java` | `f8d696de442779ebaf0d0309572588f8ef2ca0e05ccbac0cf8cce3480234b686` |
| `tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit/Test_org_eclipse_swt_internal_ViewportPaintGraph.java` | `68f0cc9e7522204f0ea7dccab340438e46cb4d1ae530aed70de621d5c78db4ae` |

## Execution

Production Java was compiled directly with `javac --release 21` from each
platform's selected source-root set; no fake graphics or SWT classes were used.
The test class was compiled against the resulting GTK classes and executed
with the JUnit Platform console runner, selecting
`org.eclipse.swt.tests.junit.Test_org_eclipse_swt_internal_ViewportPaintGraph`.
The test oracle compares pixels produced by retained replay with immediate
calls on a real SWT GC under the identical clip.

The run used existing SWT 4975r7 GTK native libraries; no native rebuild is
claimed. Library identity:

| Library | SHA-256 |
| --- | --- |
| `libswt-gtk-4975r7.so` | `37e0fdf931b6edf192729c74ff9fb4e75233a7d7546d72c91a729fc4f44f3685` |
| `libswt-pi3-gtk-4975r7.so` | `992bbcbd9ebbd2ac4b7eb904779730d0ee5e93f51d959825fb59921158995dcb` |
| `libswt-cairo-gtk-4975r7.so` | `9831d69a167f8f939da8ed49eeefda34638b99753d08c05b589696003d3283e5` |

## JUnit result

```text
Test run finished after 235 ms
[         4 containers found      ]
[         0 containers skipped    ]
[         4 containers started    ]
[         0 containers aborted    ]
[         4 containers successful ]
[         0 containers failed     ]
[        17 tests found           ]
[         0 tests skipped         ]
[        17 tests started         ]
[         0 tests aborted         ]
[        17 tests successful      ]
[         0 tests failed          ]

```

The baseline log reports 12 successes and five assertion failures at visible
pixels; it does not fail because native GTK could not initialize.

## Limits and integrated gate

This is focused native GTK3 evidence for the pinned source, not a completed
SWT widget/platform acceptance matrix. Native Cocoa/Win32, GTK4, API tooling,
performance benchmarks, and complete reactor acceptance were not established.

A full Maven/Tycho command was attempted earlier against source
`621d3544efdb59ad9c02b5e322b0a0866a7a7f2e` and stopped before tests because SWT
3.136.0 required a matching GTK fragment while the selected fragment declared
3.135.100. That failed attempt is not promoted to a pass by these direct Java
compilations or native image tests.

The original four stroke regressions were recipe-generated; their source
custody was merged through Synexia #8625. The canonical retained-paint recipe
is being evolved for #38. This receipt does not claim new recipe execution for
that independent consolidation.
