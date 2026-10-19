# SWT viewport repair verification, 2026-10-04

Product baseline: `bc0dad0f517fdca0623e250cee6dc909bf9e0771`, plus the ten source changes
in this PR. Generated source owners and pre/post SHA-256 are sealed by the canonical Synexia
OpenRewrite crates; no handwritten production-file application or native binary is shipped.

## Repairs

- Align the eight reactor fragment manifests with the SWT bundle's exact `3.136.0` version.
- Let the two viewport contract tests reuse a suite-owned Display and dispose only their own.
- Rename four retained-path bounding locals that illegally shadowed outer clip-bound locals.
  Retained path payload, stroke/clip behavior, affine transforms and resource cleanup are preserved.

## Executed gates

Java 21.0.12, Maven 3.9.11, OpenRewrite 8.17.1, Linux GTK3/X11 with Xvfb.

| Gate | Result |
| --- | --- |
| Focused canonical recipe replay/refusal/fixed-point tests | 8 pass, 0 skipped |
| Selected SWT Java source roots | GTK 486, Win32 665, Cocoa 626 compile |
| Native graph, retained-path, affine scheduling and viewport contracts | 30 pass, 0 skipped |
| Test module dependency-reactor validate | 12 modules pass |
| Full Tycho compile/verify | Blocked on dependency resolution |
| Large native manual screenshot suite | Incomplete; timed out after five captures |

The original combined run reproduced 22 passes and two multiple-Display failures; the lifecycle
repair made that same 24-test set pass. Later path/scheduling additions raise the current set to 30.
The tests are `Test_ViewportRewriteContracts`, `Test_org_eclipse_swt_internal_ViewportPaintGraph`
and `Test_org_eclipse_swt_widgets_ViewportPaintGraph`.

Offline Tycho resolution lacks `assertj-core.source:3.27.7`. Online retrieval of required
`org.eclipse.osgi:3.25.0.v20261001-1946` fails with `UnresolvedAddressException`. Interrupted
compiler logs are not acceptance evidence. Native Win32/Cocoa and GTK4 have not run here.

## Stress evidence and remaining work

The unchanged manual harness renders top, middle, end and checked-selection states for its
2,000,000-row Table. At the middle position the capture shows row 1,000,000 and WidgetSpy
reports 62 materialized TableItem wrappers. At the last/selection states it reports 92.
These snapshots do not establish a bound over an arbitrarily long scrolling session.

The 500,000-root Tree's first-expanded state captures 31 materialized TreeItem wrappers and
510,000 logical visible rows. The suite then exceeds the 180-second limit. A separate diagnostic
run samples the UI thread at 75, 120 and 165 seconds in `Tree.setItemCount`, executing
`gtk_tree_store_insert_after` / `gtk_tree_store_set` from the fixture's SetData listener.
This is unresolved native-model work; Java wrapper counts alone are not a native residency proof.

ATK was built locally from existing generated native sources to keep accessibility active.
No native binary is committed. The full screenshot suite, source-wide widget adoption,
List/ScrolledComposite integration, native residency/performance and all-platform runtime
contracts remain explicit gates. This PR repairs compile/test blockers, not whole-toolkit completion.
