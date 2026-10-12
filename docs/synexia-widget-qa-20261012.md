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
