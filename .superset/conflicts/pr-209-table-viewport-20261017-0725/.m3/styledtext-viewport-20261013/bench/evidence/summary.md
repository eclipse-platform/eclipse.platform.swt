
#### Open, scroll, memory (median of trials)

| size MiB | mode | build | content | n | setTextMs | firstPaintMs | idleDrainMs | idleTimedOut | scrollEndMs | caretEndMs | scrollTopMs | uiCpuMs | heapRetainedMiB | heapPeakMiB | procWorkingSetMiB | procPrivateMiB |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| 1 | fixed | before | DefaultContent | 3 | 42.89 | 6.01 | 1.05 | 0/3 | 14.50 | 6.51 | 15.27 | 46.88 | 5.60 | 12.50 | 349.90 | 597.70 |
| 1 | fixed | after | PagedContent | 3 | 26.40 | 7.89 | 0.04 | 0/3 | 14.58 | 7.41 | 17.96 | 46.88 | 2.10 | 10.50 | 360.60 | 609.90 |
| 1 | wrap | before | DefaultContent | 3 | 44.23 | 8.49 | 647.71 | 0/3 | 11.37 | 4.33 | 10.72 | 703.13 | 5.60 | 41.90 | 290.90 | 508.90 |
| 1 | wrap | after | PagedContent | 3 | 38.39 | 14.14 | 686.14 | 0/3 | 9.31 | 3.99 | 9.01 | 703.13 | 2.20 | 40.50 | 345.30 | 582.50 |
| 100 | fixed | before | DefaultContent | 3 | 599.49 | 13.25 | 1.32 | 0/3 | 10.21 | 9.39 | 11.03 | 562.50 | 397.50 | 660.10 | 876.80 | 1704.80 |
| 100 | fixed | after | PagedContent | 3 | 60.09 | 11.43 | 0.04 | 0/3 | 11.61 | 4.37 | 12.22 | 62.50 | 5.90 | 142.80 | 413.50 | 507.00 |
| 100 | wrap | before | DefaultContent | 3 | 660.98 | 75.99 | 30030.83 | 3/3 | 165244.67 | 10.48 | 112.01 | 184500.00 | 397.40 | 1360.30 | 1200.00 | 1627.80 |
| 100 | wrap | after | PagedContent | 3 | 67.35 | 11.57 | 9.78 | 0/3 | 15.54 | 5.22 | 10.02 | 62.50 | 6.10 | 142.80 | 358.30 | 449.40 |

#### UI-thread / CPU split (median of trials)

| size MiB | mode | build | content | n | frameP50Ms | frameP95Ms | frameMaxMs | paintP50Ms | paintP95Ms | paintMaxMs | dispatchP95Ms | dispatchMaxMs | pageCountP50Us | pageCountP95Us | pageIndexP50Us | pageIndexP95Us | indexBuildMs | indexBuildWorkerMs |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| 1 | fixed | before | DefaultContent | 3 | 5.95 | 11.02 | 25.98 | 1.96 | 5.08 | 7.62 | 0.52 | 0.52 | 110.40 | n/a | 110.40 | n/a | 1.77 | 0.00 |
| 1 | fixed | after | PagedContent | 3 | 6.64 | 11.28 | 19.82 | 2.34 | 5.69 | 8.52 | 0.00 | 0.00 | 93.60 | 195.40 | 273.60 | 356.00 | 2.81 | 1.41 |
| 1 | wrap | before | DefaultContent | 3 | 6.06 | 11.03 | 15.33 | 4.33 | 7.20 | 12.04 | 52.71 | 57.28 | 113.00 | n/a | 113.00 | n/a | 1.81 | 0.00 |
| 1 | wrap | after | PagedContent | 3 | 7.20 | 16.08 | 20.64 | 4.87 | 12.87 | 16.81 | 51.74 | 53.19 | 100.50 | 130.80 | 334.00 | 660.40 | 4.43 | 1.77 |
| 100 | fixed | before | DefaultContent | 3 | 7.02 | 16.22 | 38.87 | 2.30 | 5.56 | 19.49 | 0.64 | 0.64 | 167.80 | n/a | 167.80 | n/a | 268.45 | 0.00 |
| 100 | fixed | after | PagedContent | 3 | 8.79 | 18.37 | 23.20 | 2.30 | 12.98 | 16.27 | 0.00 | 0.00 | 36.30 | 45.40 | 341.30 | 374.40 | 8.74 | 56.40 |
| 100 | wrap | before | DefaultContent | 3 | 25.23 | 40.85 | 48.77 | 16.82 | 28.26 | 39.10 | 54.28 | 65.62 | 569.90 | n/a | 569.90 | n/a | 911.84 | 0.00 |
| 100 | wrap | after | PagedContent | 3 | 6.63 | 19.09 | 44.10 | 2.85 | 13.20 | 29.35 | 9.66 | 9.66 | 36.30 | 46.80 | 341.70 | 370.30 | 9.79 | 60.94 |

#### Control.print pixels (median of trials)

| size MiB | mode | build | content | n | topPrintSha | endPrintSha |
|---|---|---|---|---|---|---|
| 1 | fixed | before | DefaultContent | 3 | b9042e7c312eae8d | ae454d6cbf2b6afb |
| 1 | fixed | after | PagedContent | 3 | b9042e7c312eae8d | ae454d6cbf2b6afb |
| 1 | wrap | before | DefaultContent | 3 | 72dcb47e2ba8ef8f | 4088ba8d5ffc9029 |
| 1 | wrap | after | PagedContent | 3 | 72dcb47e2ba8ef8f | 4088ba8d5ffc9029 |
| 100 | fixed | before | DefaultContent | 3 | b9042e7c312eae8d | 913d416a12cf9a66 |
| 100 | fixed | after | PagedContent | 3 | b9042e7c312eae8d | 913d416a12cf9a66 |
| 100 | wrap | before | DefaultContent | 3 | 72dcb47e2ba8ef8f | f1427050e6dee549 |
| 100 | wrap | after | PagedContent | 3 | 72dcb47e2ba8ef8f | f1427050e6dee549 |
