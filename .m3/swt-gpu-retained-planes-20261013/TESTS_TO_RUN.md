# Tests to run after RESUME TESTS

The scripts live in `C:\m3-work-c\gpu-planes\scripts` on this host. Each one waits for the memory gate (at least 4 GB free and 8 GB of commit headroom), runs one JVM at a time, and writes to `C:\m3-work-c\gpu-planes`.

Run them in this order. Run each long one in the background.

| # | Command | What it proves | Status at pause (2026-10-13) |
| --- | --- | --- | --- |
| 1 | `sh scripts/rebuild.sh` | Compiles the win32 SWT tree, the Skija backend and the bench classes. | Done on 41977d7e7b. Repeat after any source change. |
| 2 | `sh scripts/junit.sh` | `Test_org_eclipse_swt_internal_gpu_RetainedPlanes` in off, gdi and skija: packed-int tables, zero-allocation hit path, rect algebra, the facade fallback, and focus on the owning widget with no child window. | v5 passed: 11/11 in each mode. Repeat after the fix-only changes. |
| 3 | `sh scripts/probe-all.sh` | Pixel diffs against stock GDI for the shapes, scroll, overlay and StyledText scenes. Capture mode: display `GC.copyArea`, shell topmost. | v5 done. |
| 4 | `sh scripts/seam-run.sh results-seams-v6.tsv` | Border, scrollbar, margin and seam pixel changes, plus the #121 recorder's `damagePx`/`paintedPx`/`protectedHitPx` and overpaint, at 100%, 125%, 150% and 200%. Capture mode: window-DC `BitBlt`. | v5 completed only 100% before the pause: all zeros. 125%, 150% and 200% are **pending**. |
| 5 | `sh scripts/bench-all.sh results-frames-v6.tsv 200 2` | Frame times (canvas, StyledText, 1M-row Table, 1M-node Tree), CPU, allocations, GPU wait and submit, and cache hit rates, on the **RFB frame model**. | **Pending.** The v3 matrix was measured on the earlier tile-composite architecture (06abbe2053). |
| 6 | `sh scripts/graphics-run.sh results-graphics-v6.tsv` | GraphicsExample static-tab pixel diffs and animated-tab frame times. | **Pending.** The harness compile fix is in 80ffcda76e. |
| 7 | `sh scripts/grid-run.sh results-grid-v6.tsv` | Nebula Grid with 1M rows: scroll, selection and focus-row frames. | v3 ran on the old architecture. **Pending** on the RFB model. |
| 8 | `sh scripts/mem-run.sh results-memory-v6.tsv` | Process commit, heap, accounted GPU bytes, and GPU submit and wait per frame. | **Pending.** |
| 9 | `mvn -B -o -f synexia-openrewrite-recipes/recipe-crates/swt-gpu-retained-planes-20261013/pom.xml verify`, in `C:\m3-work-c\wt\synexia-gpu-planes` | OpenRewrite 8.17.1 SDK: exact postimages, fixed point, refusal of drift and missing sources, on the sealed crate. | **Pending.** It is a JVM run. |
| 10 | `python -B scripts/make-readme.py v6 <run-dir>/README.md`, then `sh scripts/collect-evidence.sh v6` | Writes the README tables and copies the evidence into the fork and the crate. | Pending. |

Hidden and occluded surfaces: `RetainedFrame.effectivelyVisible()` is the single read. A test should minimize the shell during a scroll sequence and assert that `RASTER_PIXELS` and `TILE_RASTER` do not grow until it is restored. To add to JUnit after resume.
