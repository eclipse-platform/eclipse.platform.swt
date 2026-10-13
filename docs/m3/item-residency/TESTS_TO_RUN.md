# Tests to run after RESUME TESTS

Paused during the user's interview (2026-10-13). Each line runs alone behind the memory gate, one JVM at a time, with `-Xmx2g` or less. Scripts live in the agent scratch area `C:/m3-work-c/item-res/`.

| # | What | Command | Why it is pending |
|---:|---|---|---|
| 1 | Engine JUnit (`Test_ItemResidency`, `Test_ItemResidencyLanes`) | `bash run-tests.sh` | dual-licence headers, `pinResidency`/`unpinResidency`, residency pins shifted on insert/remove (new code since the last 20/20 run) |
| 2 | New test `Test_ItemResidency#residencyPinsBlockEvictionAndShift` (written, compile-checked) | part of 1 | covers the new pin methods |
| 3 | `Control.print` round trip | `bash run-print.sh` | rerun on the relicensed head |
| 4 | Stress flat 1M/10M/100M/1B and tree 1M/100M/1B | `bash run-stress.sh flat:1000000 flat:10000000 flat:100000000 flat:1000000000 tree:100,100,100 tree:1000,1000,100 tree:1000,1000,1000` | confirms the numbers in STRESS_RESULTS.tsv on the final head |
| 5 | SWT snippets compile for the metadata corpus | fix `build-examples.sh` failing-file grep (paths printed as `C:\...`), rebuild, then `bash run-m3-scan.sh` | Snippet174 and Snippet195 need OpenGL/LWJGL jars; the first build excluded nothing and produced no snippet classes |
| 6 | M3jdk21 pass tests | `bash run-m3-tests.sh` | rerun on the final M3jdk21 head |
| 7 | Synexia crate replay: apply, fixed point, drift refusal | `java RecipeReplay.java <crate> C:/m3-work-c/wt/swt-replay` twice, then edit one target and rerun (expect exit 3) | crate sealed while paused |
| 8 | Swing package-rename recipe | `java ResidencyRename.java <swt-worktree> <out>` and compare with `RENAME_MANIFEST.tsv` post hashes | the expected hashes come from the CPU (Python) transform; the Java run must match them byte for byte |
