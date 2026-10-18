<!-- SPDX-License-Identifier: Apache-2.0 -->
# Automated widget QA recipe receipt — 2026-10-12

Canonical recipe and evidence: [Synexia PR #10534](https://github.com/hsoliwal/com.synexia/pull/10534),
commit `e4aff2e7302be68ef1ebe1d3c488a01baa259722`, directory
`synexia-openrewrite-recipes/recipe-crates/widget-qa-completion-20261012/`.

The actual OpenRewrite SDK emitted `.github/workflows/build.yml` and
`tests/org.eclipse.swt.tests/verify_viewport_screenshots.py` onto SWT base
`3d57f02361fae4fc2da05951a37514fc917a12ff`. Source candidate
`1737076eaf15db78a145d8bedaec975d236f4bbd` contains those exact outputs;
this follow-up adds only this provenance receipt.

The existing native capture tests use `GC(Display).copyArea` for visible pixels.
The extracted existing OpenCV oracle is shared by SWT CI and Nebula's combined
runner. It now validates actual JUnit cases, not summary counters alone, and
invalidates prior PASS output before a new validation attempt. Resource mode
validates the existing CPU/heap/RSS reports and 24 property receipts without
requiring OpenCV. Existing pixel oracles, Java/JNI contracts and budgets are unchanged.

Commands used by the existing CI and receiver:

```sh
python tests/org.eclipse.swt.tests/verify_viewport_screenshots.py --test-root tests/org.eclipse.swt.tests
python tests/org.eclipse.swt.tests/verify_viewport_screenshots.py --test-root tests/org.eclipse.swt.tests --resources
```

The first command requires the already pinned OpenCV/NumPy environment and native
screenshot test outputs. The second requires the resource test outputs. Neither
creates test evidence or permits missing/failed/skipped tests.

Canonical verification passed on Java 21 / OpenRewrite 8.17.1: two JUnit recipe
tests, 14 Python validator/runner tests, unchanged replay and six source-drift
refusals across SWT and Nebula. Synthetic PNGs test the validators only.

Fresh native execution remains blocked in the authoring environment: GTK/Xvfb
are unavailable, package installation failed, and the environment policy rejected
sandbox escalation. Target CI must run native widget, vision and resource gates.
No manual QA or screenshot sign-off is required. No fresh platform pass, complete
widget/donor distillation or performance improvement is claimed by this receipt.
Existing Eclipse notices and source licensing are unchanged.

## Current-source successor

[Synexia #10550](https://github.com/hsoliwal/com.synexia/pull/10550), commit
`829067596dff4bb72705ad4ccfc98e57122340da`, owns the executed successor in
`synexia-openrewrite-recipes/recipe-crates/swt-viewport-reconciliation-20261012/`.
Generated source commit `1eb87b687039e8858fb50284d93d7bf12c4e8d58` reconciles
current master `be5cef162a1b743ecebfefe0bf79b046810e8c10` and the earlier QA
candidate through an additive two-parent commit.

It preserves all five screenshot tests and thirteen SWT scenes, recovers the
lost ScrolledComposite compilation/build-identity fixes, and restores the exact
eleven-row resource viewport without raising its limit. The AGENTS invariant
requires SWT names; public/protected Java declarations are unchanged.

Java 21 product source compilation passes for GTK (494 files), Win32 (673) and
Cocoa (634). Eleven existing headless viewport/selection tests pass per build;
the resource fixture compiles on all three. The task's offline Maven run passes
three recipe JUnit tests and eighteen Python QA tests, eighteen complete-input
drift refusals and no-change replay. The generated source matches the compiled
candidate exactly. PNG unit fixtures are synthetic, not native execution proof.

Fresh widget capture and resource measurement remain blocked locally by missing
GTK/Xvfb and the rejected installation escalation. CI must execute native JUnit,
OpenCV and independent CPU/heap/RSS gates. No manual QA is required. This successor
does not qualify every widget, the full reactor or the absent historical Java2D
bridge. Preserve the earlier receipt above as dated source history.
