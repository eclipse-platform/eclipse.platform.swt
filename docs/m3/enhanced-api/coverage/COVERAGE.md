# Coverage of the enhanced API classes (JaCoCo 0.8.15)

Run: `Test_M3ItemRange`, `Test_M3ItemSelection`, `Test_M3Scrolling`, `Test_M3TextSegments`, `Test_M3TokenMatch` (55 tests) on the hidden
desktop, once with `-Dswt.widget.mode=viewport` and once with `-Dswt.widget.mode=original`; both 55/55; the two
execution files merged. Classes: the M3 API classes compiled from this branch, placed ahead of jar `ta20261017a` on
the class path. Per-class numbers: `jacoco-m3-api.csv`.

| measure | covered | total | percent |
|---|---|---|---|
| lines | 428 | 428 | 100.0% |
| branches | 311 | 330 | 94.2% |

All 19 missed branches are listed in `COVERAGE_GAPS.tsv`. Each is the true side of a one-condition
`if (...) SWT.error (...)` argument check. Every one is exercised by an `assertThrows` test, named in the TSV. JaCoCo
cannot record them: `SWT.error` always throws, so the probe at the end of the single-predecessor block never fires.
A two-condition check (`a || b`) gets its probe on the edge and is recorded, which is why `M3Mode.name`,
`M3Scrolling` alignment and `M3TextSegments` checks show as covered. With these artifacts counted as exercised,
every branch (330 of 330) is reached. The code keeps the SWT `SWT.error` idiom on purpose, for upstream style.
