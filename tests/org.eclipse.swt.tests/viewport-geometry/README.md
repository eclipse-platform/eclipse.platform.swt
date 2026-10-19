# SWT resident viewport-window bounds — 2026-10-19

Target base:59eb59e085425cb9591a0024cdfb7c79502eb485.
Canonical recipe/evidence: https://github.com/hsoliwal/com.synexia/pull/12470
Source pin:f3cebda868c7c15459f295223ff6abb3e987b95e (docs c2d701d514a62d476c8828691200b9cb0fffde93).
Crate:synexia-openrewrite-recipes/recipes/widget-backing-20261014/viewport-geometry-20261019/.
Recipe:com.synexia.rewrite.M3SwtViewportGeometryRecipe, extending unchanged
M3GpuToolJoinRecipe/Manifest. This target retains no competing recipe engine.

The existing VirtualRowWindowPlan inside org.eclipse.swt.widgets remains the
allocation-free O(1) native row-window planner for flat viewport controls.
Reject unrepresentable edit endpoints and shifted/grown resident endpoints before
mutating any committed or planned state. This fixes the reproduced overflowing
removal that changed [100,140) into [95,130). Valid edits, native indices,
overlap counts, zero-count handling, pending plan state and existing messages
remain unchanged. No widget, renderer, scrollbar, JNI signature or public API
is added. Other source and prior viewport improvements remain untouched.

Before blob:b330ed3e6a6af57b5648e5dabda008b6a40c812a
After blob:2836212e0cec0b4df112af995391f2fc4e950382
Before SHA256:8c122eefa42c8932a71d160405355fe68c39a87b2c0be8f4801bccfb6f74ff64
After SHA256:b0043634acd8a1b914f5bfb8298434000d44b43de4ef10c42932edcafd2bcd34
Proof blob:de53cc74e6144c1f399dafda5ea9ed8cc04bb3d7

Fresh component proof: strict javac21 --release21 -proc:none -Xlint:all -Werror;
6,758,400 ordinary assertions before/after,6,758,426 with candidate boundary/refusal
checks. Baseline defect and missing-growth-guard mutant failed. Full declared
javap -p -s output is unchanged. Exact-source kernel17 initial/19 replay checks,
all4 mixed states and refusal/fixed point. The proof executes the complete original
planner, not an SWT-shaped stub. Counts are finite assertions, not native GUI tests.

Run the standalone proof from repository root using a fresh output directory:

```sh
javac --release 21 -proc:none -Xlint:all -Werror -d viewport-proof-classes \
  'bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/VirtualRowWindowPlan.java' \
  tests/org.eclipse.swt.tests/viewport-geometry/VirtualRowWindowGeometryProof.java
java -Xmx64m -cp viewport-proof-classes org.eclipse.swt.widgets.VirtualRowWindowGeometryProof
```

DRAFT / COMPONENT_PASS / NATIVE_WIDGET_HOLD. The standalone proof is not added to
default JUnit/Tycho discovery. Actual Maven/OpenRewrite SDK attempt exited127
(mvn absent); kernel replay is not SDK proof. Full SWT bundle, GTK/Win32/Cocoa
Display/scroll/SetData/event traces, screen capture/pixel validators and native
resource budgets are NOT_RUN. No whole-widget acceptance or performance claim.
Original source dual Apache-2.0/EPL-2.0 notice retained; no M3JDK collection change.
