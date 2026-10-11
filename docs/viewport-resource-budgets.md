<!-- SPDX-License-Identifier: Apache-2.0 -->
# Viewport resource regression gate

`ViewportResourceRegressionTest` measures Java heap, whole-process resident memory (RSS)
and process CPU separately. Counting Java TreeItem facades cannot establish native-memory bounds.
The dedicated GTK3/X11 CI job enables `-Dswt.viewport.resourceRegression=true` and requires
all thirteen tests to execute without skips on JDK 21 and 25. Other platforms do not silently inherit
Linux evidence.

## Frozen workload and budgets

Each positive scenario runs in three fresh JVMs with `-Xms64m -Xmx256m -XX:+UseSerialGC`.
Class loading and measurement-provider initialization precede the sample. Retained heap is
sampled after full GC; GC is outside the CPU interval. RSS includes native GTK/JNI memory,
committed heap, JIT code and other process pages; it is not an allocation counter. Negative
deltas remain visible in the raw before/after values and count as zero growth for the budget.

| Workload | RSS growth, each trial | Retained heap growth, each trial | Median process CPU |
| --- | ---: | ---: | ---: |
| Cold virtual root count 64 -> 1,000,000 | 24 MiB | 8 MiB | 1 second |
| 96 painted scrolls alternating roots 32 and 96, after 16 warm steps | 24 MiB | 8 MiB | 8 seconds |
| 96 expand/collapse cycles with 1,000,000 logical children, after 16 warm cycles | 24 MiB | 8 MiB | 8 seconds |

The median of the three maximum per-step CPU samples must be at most 150 ms. The child
wall-clock timeout is 60 seconds; elapsed time is reported but never substituted for CPU.
Each measured interactive step must receive an actual paint event and reach its requested top
item within two seconds. Draining an empty event queue alone does not settle GTK frame-clock work.
The tests also preserve item identity, text/check state, logical counts, scroll position and
bounded SetData callbacks. They check the peak row count in the model actually attached to GtkTreeView via JNI,
including expanded and collapsed states, independently of Java facade counts and process memory.
The GTK object reference and iterator allocations used for sampling are always released.
Primitive method handles avoid per-row reflective argument-array and boxing overhead.

Three executed negative controls establish that the budget assertions detect resource growth:
a touched, retained 64 MiB direct buffer must fail the memory validator while Java heap stays
below 8 MiB growth; two seconds of additional process CPU must fail the one-second CPU validator.
A third control requests default-model row 4096 and must exceed the 512-row native-view
budget; the validator must reject it. This control is not a passing bounded workload.
Reports and child logs are archived under `target/viewport-resources/`.

## Explicit logical model qualification

Four additional three-trial scenarios explicitly set
`org.eclipse.swt.internal.gtk.virtualTreeLogicalNativeModel=true` in fresh JVMs:

| Scenario | Workload | Median process CPU |
| --- | --- | ---: |
| `logical-cold` | Root count 64 -> 1,000,000 | 1 second |
| `logical-far` | Unpainted, identity-preserving access at 100,000 and 900,000 | 1 second |
| `logical-scroll` | 96 painted scrolls alternating 100,000 and 900,000 after 16 warm steps | 8 seconds |
| `logical-collapse` | 96 painted million-child expand/collapse cycles after 16 warm cycles | 8 seconds |

Each retains the original 24 MiB RSS / 8 MiB heap / 150 ms median maximum-step CPU budgets.
Every positive scenario requires at most 512 rows in the attached native view, sampled after
count/access changes and during expanded/collapsed/painted states. `residentRows` retains its
historical facade/frontier diagnostic meaning; `peakNativeViewRows` is the independent JNI view
measurement. Schema version 2 adds this distinction. The original Tree tests require 24 fresh child reports; the Java2D extension below adds nine reports (33 total).

## Scope and unresolved work

The default-model gate qualifies cold count growth and repeated use of the visited near viewport.
It does **not** establish constant memory for arbitrary distant access: the current default
GTK3 frontier can build a native prefix when `getItem(100000)` is requested. The standalone
`far-diagnostic` scenario records that cost with an explicitly unqualified scope. It is not
counted as a passing bounded-memory test. The four explicit logical-model workloads additionally qualify a bounded cold/distant visited
window. They do not change the default model or prove bounded memory over arbitrary visited history.

These bounded workloads do not prove leak freedom, universal latency, screenshot parity or
cross-platform correctness. Existing screenshot, widget, topology, API and platform gates remain
required. A changed budget or corpus is a new experiment with preserved previous receipts.

## Reproduce

Build/install the SWT reactor and its GTK3 natives using the existing build instructions. Then,
from `tests/org.eclipse.swt.tests` with JDK 21 or the CI JDK:

```sh
SWT_GTK4=0 GDK_BACKEND=x11 xvfb-run -a mvn -B \
  -Dtest=ViewportResourceRegressionTest -Dswt.viewport.resourceRegression=true \
  -DforkCount=1 -DfailIfNoTests=true -DskipNativeTests=true integration-test
```

The source recipe and exact pre/postimage seals live in Synexia's
`synexia-openrewrite-recipes/recipe-crates/viewport-resource-gates-20261006` (original) and
`synexia-openrewrite-recipes/recipe-crates/viewport-distant-resource-gates-20261006`.
The newly authored resource test, this document and Synexia recipe tooling are Apache-2.0.
Existing SWT files retain their upstream notices; running a recipe does not change their license.
See `docs/licenses/synexia-viewport-Apache-2.0.txt` for the license and
`docs/licenses/synexia-viewport-NOTICE.txt` for the exact scope.

## SWT screen capture accompanies resource qualification

`ViewportScreenshotRegressionTest` uses SWT `GC(Display).copyArea` for the required
`<scenario>-screen.png` screen artifact. The retained `<scenario>.png` records the separate
`Control.print` path (or its control-GC fallback). These represent different observations:
the on-screen image includes overlapping controls and the actual viewport composition.
Each scenario sidecar binds API, screen bounds, logical coordinate space, pixel dimensions,
DPI, backend and both image hashes. Existing optional `<scenario>-native.png` shell captures remain available. The screen-capture canary moves/repaints a colored control
under a second control and verifies known colors in decoded before/after PNGs.

The dedicated GTK3/X11 lane requires both screenshot tests, nine screen/rendered pairs and
two canary PNGs. Other platform/backend support requires its own run; no silent rendering
fallback qualifies screen capture. Existing heap/RSS/CPU tests and budgets remain unchanged;
screenshot work runs in the separate screenshot phase. Recipe ownership remains in Synexia:
`synexia-openrewrite-recipes/recipe-crates/viewport-swt-screen-capture-20261007`.

OpenCV 4.12.0.88 and NumPy 2.2.6 independently decode the PNGs and verify exact canary geometry, overlap, clip bounds and repaint area. Six negative controls must fail. Scene checks reject blank captures and stale scroll/resize frames; they do not establish cross-theme golden equivalence. JUnit supplies behavior checks; heap/RSS/CPU gates run separately. Acceptance is fully automated, with no manual QA sign-off.

### Capture contracts and graphics lifetime

The automated screenshot lane now requires four JUnit tests. The added boundary test checks
ancestor clipping, zero size, edge-touching containment, visibility, disposal, null input and
wrong-thread access. The graphics-lifetime test performs 48 captures through the three existing
paths and three deterministic PNG-write failures, with no retained GC/Image wrappers. Two
positive controls verify that the observer detects retained Image and GC resources. The receipt
is `target/capture-qualification/graphics-lifetime.properties` and is checked by the OpenCV step.
SWT tracking is an ownership oracle, not a claim about all native allocations; retain the separate
33-process-report CPU/RSS/heap gate. No manual QA is required.

### GTK clipping-region coordinate parity

`GC.getClipping(Region)` must report the same user-coordinate space as `GC.getClipping()`.
The GTK receiver now reuses the existing clipping-transform atom for both overloads, preserving
nonrectangular holes and releasing its temporary Cairo region. The JUnit regression enumerates
81 pairs of incoming/current translations, including null, unchanged, replaced and removed
transforms. Nebula's complete mutable-GC-state restoration test is the cross-project reproducer.
No public signature or JNI ABI changes. Actual GTK JNI and viewport/resource checks remain required;
this does not qualify other platforms, general arbitrary affine rasterization or the whole reactor.

### Viewport affine scope restoration

A viewport scope captures clipping under the identity transform when its incoming
transform is nonidentity, and restores that device-space region before restoring
the incoming transform. This prevents integer Region inverse/forward rounding from
losing pixels, filling holes or expanding clipping. The user-space snapshot remains
the existing clip-operation boundary. Identity scopes allocate no additional Region.
All snapshots are released on constructor failure, close, and already-disposed GCs.

The state-restoration oracle records bounds after installing the original transform;
pre-transform bounds are a different coordinate space. The test-owned Transform is
released even if an assertion fails. A separate six-affine regression checks both
user-space bounds and exact device-region symmetric differences, including holes.
The native matrix must qualify Windows/Cocoa independently; GTK proof is not enough.


## Java2D visible-raster resource qualification

Three additive `Java2DCanvas` scenarios use the same isolated child-process runner,
full-GC retained-heap measurement, `/proc/self/status` RSS measurement and process CPU
operators as the Tree gate. Existing Tree scenarios, native-view row assertions, budgets
and all three negative controls are preserved. Java2D assertions have their own model
scope and do not reinterpret Tree-native-row measurements.

Each scenario has three fresh JVM trials with the existing `-Xms64m -Xmx256m
-XX:+UseSerialGC` settings. Java2D class loading, native presentation initialization and
measurement-provider initialization occur before the sample. Interactive scenarios warm
16 real painted steps before measuring 96 further painted steps. The logical extent is
220,000,000 by 220,000,000; the largest shell is 480 by 320 logical pixels.

| Scenario | Frozen workload | Each-trial RSS / retained heap | Median process CPU | Median maximum-step CPU |
| --- | --- | --- | --- | --- |
| `java2d-cold` | Allocate and paint the initially absent visible raster | 24 MiB / 8 MiB | 1 second | Cold allocation uses the total-CPU limit |
| `java2d-scroll` | 96 painted distant logical-origin alternations after 16 warm steps | 24 MiB / 8 MiB | 8 seconds | 150 ms |
| `java2d-resize-zoom` | 96 painted 480x320 / 360x240 shell-size and 1.0 / 1.5 zoom alternations, with distant logical origins, after 16 warm steps | 24 MiB / 8 MiB | 8 seconds | 150 ms |

Each measured step must receive both an actual SWT Paint event and a Java2D renderer
callback within two seconds. Every observed raster is checked against its current client
size; the physical child is checked against the viewport. The same Canvas must remain the
ScrolledComposite content at `(0, 0)`, with preserved data and exact parent-owned logical
origin and extent. Reports retain callback/paint/check counts and maximum raster/client
pixels alongside independent heap, RSS and CPU before/after measurements. Raster dimensions
are a structural check; they do not replace process-memory measurements.

The nine new reports use `model=java2d-visible-raster` and schema
`swt-viewport-resources/3`; the existing 24 Tree reports retain their existing schema.
The JUnit class now has 13 tests and the dedicated gate requires 33 child reports with no
skips. Screenshots, persisted-image OpenCV checks and graphics-ownership fixtures remain
outside measured resource intervals. This frozen Java2D rectangle-rendering workload does
not prove a speedup over another renderer, arbitrary application-painter cost, native
Win32/Cocoa acceptance, GTK4 acceptance or full-reactor qualification. New scenarios are
qualification requirements until their actual run receipts are retained.
