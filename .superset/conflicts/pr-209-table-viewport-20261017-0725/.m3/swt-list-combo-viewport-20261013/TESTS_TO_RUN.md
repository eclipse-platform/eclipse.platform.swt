<!-- SPDX-License-Identifier: EPL-2.0 -->
# Tests to run (FIX-ONLY mode since 2026-10-13 ~16:00 IST)

The coordinator paused test runs ("fix it all, we'll test after the interview"). Everything below is written and committed but **not run** on the current head unless a result is linked. Harness scripts live outside the repository on the author host (`C:\m3-work-c\lc-bench\scripts`); the commands below are what they execute. Every JVM run is memory-gated (>= 4 GB free, >= 8 GB commit headroom), one JVM at a time.

## Already run (earlier head, before FIX-ONLY)

| what | head | result | evidence |
|---|---|---|---|
| Differential trace base vs after (default thresholds) | `09af2cd9dc` (classes compiled 15:10 IST) | 93,262 gated lines, **0 differences**; 7,925 focus/capture-dependent lines outside the gate (84 differ, see NotifyProbe); 20 defect-lane lines | `evidence/differential-summary.txt` |
| Differential trace, forced rebuild (`removeRebuildThreshold=0`) | `09af2cd9dc` | 226 differing lines (top row after rebuild, D1) | `evidence/differential-after-t0.diff` |
| NotifyProbe with focus (plain vs VIRTUAL, SINGLE/MULTI) | `23d49d415e` + calibration edits (14:3x) | identical events and selection for key, char and click | `bench/NotifyProbe.java` output in the PR body |
| Snippets 126 147 217 237 24 26 269 289 332 359 361 365 384 386 39 58 59 65 75, ControlExample, CustomControlExample (launch, 10 s, no exception) | `09af2cd9dc` | base 21/21 PASS, after 21/21 PASS | `evidence/snippets-*.tsv` |
| ListViewportBench 100K / 1M / 10M | `09af2cd9dc` | after and after-setdata rows complete; before 1M and 10M ended with exit 127 and no output (cause not determined) | `evidence/results-before-after.tsv`, `evidence/bench-exit-codes.txt` |
| Native probes N1-N19 | 13:5x-15:0x | facts in `NATIVE_LIMITS.tsv` | `evidence/native-*.tsv` |

## To run (not run yet)

1. Compile the test classes against base and after (`compile-tests.sh`, paths quoted through an @argfile): `Test_org_eclipse_swt_widgets_List`, `_Combo`, `Test_org_eclipse_swt_custom_CCombo`, `Test_org_eclipse_swt_widgets_List_Virtual`, `Test_org_eclipse_swt_custom_CCombo_Virtual`, `Test_VirtualListModel`, `Test_org_eclipse_swt_widgets_List_Viewport` (after only), `Test_VirtualSelectionModel_Hybrid` (after only), `Test_org_eclipse_swt_widgets_Combo_Virtual` (after only).
2. JUnit base: List, Combo, CCombo, List_Virtual, CCombo_Virtual, VirtualListModel (`run-tests.sh base base -Xmx768m -- ...`).
3. JUnit after: the same plus List_Viewport, VirtualSelectionModel_Hybrid, Combo_Virtual.
4. JUnit after, scale: `-Xmx2g -Dswt.list.scaleTest=true -- Test_org_eclipse_swt_widgets_List_Viewport` (10,000,000 rows).
5. Differential trace again on the final head (`run-diff.sh`), default and forced-rebuild lanes; expected 0 gated differences.
6. Bench before 1M and 10M again with a larger `-Xmx` and a hs_err capture (`-XX:ErrorFile`), to determine the exit 127.
7. Snippets/examples on the final head (`run-snippets.sh base|after`).
8. Synexia crate: `mvn -B -o -ntp -f synexia-openrewrite-recipes/recipe-crates/swt-list-combo-viewport-20261013/pom.xml verify` (exact postimages, fixed point, drift and missing-source refusal).
9. Real mouse thumb drag above 65535 rows (N16) and a screen-reader pass over the accessible row names: need the user's mouse and an AT; NOT_RUN.
10. GTK and Cocoa: design only; NOT_RUN.
