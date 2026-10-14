# Retained GPU planes beneath SWT `GC` (win32), run of 2026-10-13

Status: draft candidate. **Not merged; the user merges.** GDI/GDI+ stay the default and are the oracle.

* The design is in [docs/m3/SWT_GPU_RETAINED_PLANES_DESIGN.md](../../docs/m3/SWT_GPU_RETAINED_PLANES_DESIGN.md).
* The donor ledger is [FOSS_REUSE_DECISION.tsv](FOSS_REUSE_DECISION.tsv), and the notices are in [docs/licenses/swt-gpu-retained-planes-NOTICE.txt](../../docs/licenses/swt-gpu-retained-planes-NOTICE.txt).
* The recipe crate is Synexia `synexia-openrewrite-recipes/recipe-crates/swt-gpu-retained-planes-20261013` (`swt-gpu-retained-planes-20261013-v1`).

## Host and method (this run)

| | |
| --- | --- |
| OS | Windows 11 Home 10.0.26300; win32 x86_64; 100% display zoom |
| CPU / GPU | Intel Core i9-14900HX (32 logical CPUs); NVIDIA GeForce RTX 4060 Laptop GPU + Intel UHD (Optimus hybrid; the WGL context uses the driver's default adapter, not pinned) |
| Java | Oracle JDK 21.0.9, `-Xmx1536m`, one JVM at a time, memory-gated (≥ 4 GB free, ≥ 8 GB commit headroom) |
| SWT | This branch, compiled from source; native libraries are the SWT 3.135 win32 DLLs (4974r10) renamed to 4975r10 for the fork's version string |
| GPU | Skija 0.143.18, Skia m143, OpenGL through SWT's WGL bindings; DirectContext on a hidden, disabled GL host popup |
| Contention | The host was shared with about seven other agents. Absolute times vary between runs; compare modes within one trial. Modes are interleaved per trial and each configuration runs twice. |

A **frame** is one state change followed by `Control.update()`: a synchronous `WM_PAINT` that rasterizes, composites and presents.

* **Wall time** is `System.nanoTime` around the frame.
* **CPU** is thread CPU time.
* **Allocation** is `ThreadMXBean.getThreadAllocatedBytes`.
* **GPU wait** is the time inside the readback fence.
* **GPU bytes** are the accounted tile and frame render targets.
* **CPU tile bytes** is the GDI atlas.

Modes:

| Mode | Meaning |
| --- | --- |
| `off` | Stock SWT. |
| `gdi` | `-Dswt.gpu=gdi`: retained GDI objects, tiles and planes, composited with GDI. |
| `skija` | `-Dswt.gpu=skija`: Skia GL raster, composite and readback present. |

Widgets:

* **canvas**: a plane-aware `Canvas` that scrolls with `Canvas.scroll` and draws its selection and caret in overlay planes.
* **styledtext**: this fork's `StyledText` (100,000 lines, Consolas 10). It is unmodified: it scrolls with a full `redraw()` and draws the selection inside `TextLayout`.
* **table**: native virtual `Table`, 1,000,000 rows.
* **tree**: native virtual `Tree`, 1,000 roots × 1,000 children (1,000,000 logical nodes, children realized on expansion).
* **nebula-grid**: Nebula `Grid` (hsoliwal/nebula `eab2a65c`), 1,000,000 virtual rows × 4 columns, compiled from source against this SWT.
* **GraphicsExample**: the fork's example, which exercises the whole `GC` API.

The scenarios are scroll, selection drag (selection move for `Table`/`Tree`), and caret blink. `Table` and `Tree` have no caret, so their caret frame repaints the focus row.

## JUnit (`Test_org_eclipse_swt_internal_gpu_RetainedPlanes`, 11 tests per mode)

| mode | result |
| --- | --- |
| off | 11/11 successful |
| gdi | 11/11 successful |
| skija | 11/11 successful |

## Pixel differential against stock GDI

Capture mode: display `GC.copyArea` over the control's client area, with the shell topmost (`ON_TOP`) and active. This is what reached the screen, including the GPU-composited output. `Control.print` is not used for these diffs: `WM_PRINTCLIENT` runs the stock GDI paint, not the GPU path. Tolerance: a pixel counts as different when its maximum channel delta exceeds 48. Glyph anti-aliasing differs between ClearType GDI and Skia LCD text.

| capture vs stock GDI | pixels | exactly equal | max channel delta | > 8 | > 32 | > 64 | over tolerance 48 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| shapes-gdi.ppm | 268800 | 267677 (0.9958) | 1 | 0 | 0 | 0 | 0 (0.0000%) |
| shapes-skija.ppm | 268800 | 259690 (0.9661) | 255 | 2704 | 2312 | 1562 | 1890 (0.7031%) |
| scroll-gdi.ppm | 268800 | 248693 (0.9252) | 1 | 0 | 0 | 0 | 0 (0.0000%) |
| scroll-skija.ppm | 268800 | 248693 (0.9252) | 1 | 0 | 0 | 0 | 0 (0.0000%) |
| overlay-gdi.ppm | 268800 | 267607 (0.9956) | 1 | 0 | 0 | 0 | 0 (0.0000%) |
| overlay-skija.ppm | 268800 | 259193 (0.9643) | 255 | 3178 | 2605 | 1730 | 2143 (0.7972%) |
| styled-gdi.ppm | 268800 | 268800 (1.0000) | 0 | 0 | 0 | 0 | 0 (0.0000%) |
| styled-skija.ppm | 268800 | 220793 (0.8214) | 255 | 29619 | 20670 | 2567 | 7253 (2.6983%) |

## Seams, borders, scrollbar and margin during scroll (DamageSeamProbe)

Capture mode: window-DC `BitBlt` (`GetWindowDC` of the control). This reads the DWM redirection surface the widget presented, client and non-client, and is immune to other windows overlapping the shell on this shared host.

The scene:
* a bordered `Canvas` with `V_SCROLL` and a fixed 24 pt margin plane;
* 30 `Canvas.scroll` steps of 13 pt down, then 30 back up, then 10 selection-overlay moves.

Regions:
* the border ring;
* the scrollbar column;
* the margin;
* tile seams: ±1 device pixel around every 256 px tile edge, in content space.

Columns:
* **frames differing** compares each frame with the stock GDI frame at the same step;
* **own-first-frame changes** counts frames whose border, scrollbar or margin changed during the run (target 0).

| zoom-mode | region | frames | frames differing from stock | pixels differing | own-first-frame changes |
| --- | --- | --- | --- | --- | --- |
| zoom100-gdi | border | 70 | 0 | 0 | 0 |
| zoom100-gdi | scrollbar | 70 | 0 | 0 | 0 |
| zoom100-gdi | margin | 70 | 0 | 0 | 0 |
| zoom100-gdi | seams | 70 | 0 | 0 | - |
| zoom100-skija | border | 70 | 0 | 0 | 0 |
| zoom100-skija | scrollbar | 70 | 0 | 0 | 0 |
| zoom100-skija | margin | 70 | 0 | 0 | 0 |
| zoom100-skija | seams | 70 | 0 | 0 | - |

RFB damage per run. `ViewportDamageRecorder` from SWT #121 is fed by `RetainedPlanes.DamageListener`; the protected area is the margin plane.
* **needed px** is the OS update area of each frame;
* **raster px** is what the retained path painted;
* **overpaint** is raster beyond needed, in pixels and tiles (target 0);
* **protected-hit frames** are frames that painted the margin. The first full frame paints it once.

| mode | zoom | frames | painted px (recorder) | needed px | raster px | overpaint px | overpaint tiles | protected-hit frames | protected-hit px |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| skija | 100 | 71 | 485064 | 710400 | 244200 | 0 | 0 | 1 | 9984 |

The per-frame `damagePx/paintedPx/protectedHitPx` lists are in `evidence/results-seams-v5.tsv.damage`. For scroll steps, painted pixels equal the exposed band. Scroll-back frames paint 0 px at integral zoom: the band is refilled from tiles (RFB cache hit). Overlay-only frames paint 0 content pixels.

## Frame times (p50 / p95 / max ms; two interleaved trials, median p50/p95, max of max)

Run `v3` was measured on the earlier tile-composite frame driver (SWT 06abbe2053), before the RFB content-framebuffer model. The rerun on the final code is pending; see [TESTS_TO_RUN.md](TESTS_TO_RUN.md).

| widget | scenario | stock GDI p50 / p95 / max ms | retained GDI p50 / p95 / max ms | Skija GPU p50 / p95 / max ms |
| --- | --- | --- | --- | --- |
| canvas | scroll | 0.356 / 1.257 / 18.990 | 1.347 / 5.684 / 9.340 | 1.965 / 3.744 / 6.069 |
| canvas | select | 0.476 / 1.606 / 67.785 | 0.152 / 1.769 / 6.682 | 0.601 / 1.164 / 2.703 |
| canvas | caret | 0.238 / 0.936 / 39.695 | 0.108 / 0.338 / 1.170 | 0.787 / 2.218 / 4.728 |
| styledtext | scroll | 11.452 / 29.151 / 52.598 | 19.093 / 40.879 / 71.944 | 8.625 / 18.044 / 27.917 |
| styledtext | select | 14.143 / 42.321 / 95.222 | 12.288 / 22.256 / 38.423 | 7.127 / 14.492 / 28.629 |
| styledtext | caret | 0.372 / 0.889 / 2.420 | 0.277 / 0.719 / 2.355 | 0.360 / 0.669 / 1.378 |
| table | scroll | 2.993 / 8.085 / 21.197 | 2.026 / 6.194 / 12.775 | 2.667 / 6.868 / 14.078 |
| table | select | 7.168 / 17.184 / 26.990 | 7.006 / 18.092 / 34.138 | 8.519 / 21.402 / 65.900 |
| table | caret | 0.190 / 0.885 / 2.219 | 0.148 / 1.032 / 4.114 | 0.063 / 0.599 / 2.092 |
| tree | scroll | 13.670 / 32.100 / 52.486 | 14.496 / 31.048 / 41.245 | 13.511 / 23.280 / 30.092 |
| tree | select | 17.759 / 34.697 / 64.615 | 18.638 / 29.793 / 362.826 | 47.129 / 72.399 / 78.870 |
| tree | caret | 0.289 / 1.067 / 4.709 | 0.528 / 1.449 / 6.656 | 1.156 / 2.985 / 14.290 |

| widget | scenario | mode | CPU ms/frame | alloc B/frame | GPU wait ms/frame | pen hit % | brush hit % | font hit % | paint hit % | blob hit % | tiles reused | tiles rastered | GPU-rastered tiles | record fails | GPU bytes | CPU tile bytes | focus |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| canvas | scroll | off | 0.234 | 3594 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| canvas | scroll | gdi | 0.938 | 5798 | 0.000 | - | 100.0 | - | - | - | 3965 | 1070 | 0 | 0 | 0 | 12582912 | true |
| canvas | scroll | skija | 1.055 | 4751 | 0.996 | - | - | - | 100.0 | 99.7 | 3965 | 1070 | 1070 | 0 | 12727388 | 0 | true |
| canvas | select | off | 0.508 | 5741 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| canvas | select | gdi | 0.156 | 1642 | 0.000 | - | 100.0 | - | - | - | 3000 | 0 | 0 | 0 | 0 | 12582912 | true |
| canvas | select | skija | 0.430 | 1608 | 0.320 | - | - | - | 100.0 | - | 3000 | 0 | 0 | 0 | 7484508 | 0 | true |
| canvas | caret | off | 0.234 | 3321 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| canvas | caret | gdi | 0.156 | 1492 | 0.000 | - | 100.0 | - | - | - | 3000 | 0 | 0 | 0 | 0 | 12582912 | true |
| canvas | caret | skija | 0.703 | 1468 | 0.311 | - | - | - | 100.0 | - | 3000 | 0 | 0 | 0 | 7484508 | 0 | true |
| styledtext | scroll | off | 7.968 | 53368 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| styledtext | scroll | gdi | 10.078 | 54449 | 0.000 | - | - | - | - | - | 3000 | 3000 | 0 | 0 | 0 | 12582912 | true |
| styledtext | scroll | skija | 4.101 | 33556 | 1.363 | - | - | - | 100.0 | 99.9 | 3000 | 3000 | 3000 | 0 | 7405152 | 0 | true |
| styledtext | select | off | 8.281 | 63264 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| styledtext | select | gdi | 6.875 | 63356 | 0.000 | - | 100.0 | - | - | - | 3000 | 3000 | 0 | 0 | 0 | 12582912 | true |
| styledtext | select | skija | 4.726 | 41633 | 1.419 | - | - | - | 100.0 | 100.0 | 3000 | 3000 | 3000 | 0 | 7405152 | 0 | true |
| styledtext | caret | off | 0.195 | 0 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| styledtext | caret | gdi | 0.000 | 0 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 12582912 | true |
| styledtext | caret | skija | 0.039 | 0 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 7405152 | 0 | true |
| table | scroll | off | 1.446 | 7964 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| table | scroll | gdi | 1.016 | 7964 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| table | scroll | skija | 1.172 | 7964 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| table | select | off | 5.117 | 30724 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| table | select | gdi | 5.156 | 30723 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| table | select | skija | 5.547 | 30723 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| table | caret | off | 0.117 | 194 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| table | caret | gdi | 0.039 | 194 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| table | caret | skija | 0.117 | 192 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| tree | scroll | off | 9.921 | 90512 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| tree | scroll | gdi | 10.625 | 90512 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| tree | scroll | skija | 8.593 | 90512 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| tree | select | off | 18.398 | 30562 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | false |
| tree | select | gdi | 19.609 | 42734 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| tree | select | skija | 46.719 | 30208 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| tree | caret | off | 0.391 | 1168 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| tree | caret | gdi | 0.312 | 1168 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |
| tree | caret | skija | 0.664 | 1168 | 0.000 | - | - | - | - | - | 0 | 0 | 0 | 0 | 0 | 0 | true |

Notes:

* Native `Table` and `Tree` items are painted by Windows common controls, outside `GC`. Their rows run the same code in every mode, so the spread between modes is the host noise.
* `StyledText` caret blink is the native system caret and triggers no paint in any mode.

## Nebula Grid (1,000,000 virtual rows, compiled from hsoliwal/nebula eab2a65c)

| mode | widget | scenario | frames | p50ms | p95ms | maxms | cpuMsMean | allocBytesMean | gpuWaitMsMean | gpuRasterTiles | recordFails | failBits | blobHit% | focus |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| off | nebula-grid | scroll | 300 | 30.149 | 209.820 | 326.967 | 45.156 | 456353 | 0.000 | 0 | 0 | 0 | - | true |
| gdi | nebula-grid | scroll | 300 | 14.678 | 30.553 | 46.371 | 10.781 | 454682 | 0.000 | 0 | 0 | 0 | - | true |
| skija | nebula-grid | scroll | 300 | 67.852 | 83.025 | 89.775 | 31.563 | 423270 | 17.462 | 9000 | 0 | 1 | 99.922 | true |
| off | nebula-grid | select | 300 | 46.169 | 73.926 | 106.645 | 31.979 | 231983 | 0.000 | 0 | 0 | 0 | - | true |
| gdi | nebula-grid | select | 300 | 19.113 | 33.363 | 52.656 | 14.167 | 235659 | 0.000 | 0 | 0 | 0 | - | true |
| skija | nebula-grid | select | 300 | 26.050 | 40.603 | 77.166 | 16.771 | 234586 | 6.277 | 0 | 9 | 1 | - | true |
| gdi | nebula-grid | caret | 300 | 3.698 | 6.753 | 10.001 | 1.510 | 19856 | 0.000 | 0 | 0 | 0 | - | true |

