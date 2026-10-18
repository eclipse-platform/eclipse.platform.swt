# Tests to run: leaf 1, visibility gate (branch `m3/swt-visibility-gate-20261013`)

All commands are run from Git Bash on the win32 host. They are memory gated (at least 4 GB free and 8 GB of commit headroom) and use one JVM at a time. The scripts are in `.m3/swt-visibility-gate-20261013/scripts/`; the workspace is `C:/m3-work-c/visgate`, with the 4975r10 natives copied to `natives/`.

## One command

```sh
sh .m3/swt-visibility-gate-20261013/scripts/run-after-interview.sh
```

## What it runs

1. **Build.** Run `build-swt.sh` twice:
   - stock classes (`base-classes`) from the base snapshot;
   - gated classes (`after-classes`) from this branch.
2. **Gate JUnit.** Run `Test_org_eclipse_swt_internal_VisibilityGate`, which has 6 tests and passed 6/6 on `1823e948`.
3. **JUnit regression, before and after.** Run the touched classes with `junit.sh before|after`:
   - Composite, Shell, Canvas, ScrolledComposite, CTabFolder;
   - GridData, FormAttachment, BorderLayout;
   - Group, TabFolder, Table, Tree, StyledText.

   Display runs alone in its own JVM: in a shared JVM, 60 stock failures come from "Another Display is already associated". Pass criterion: no test fails only in the after run.
4. **Bench matrix.** Run `bench.sh <tab|scrolled|minimized|covered|stack> <before|after|after-off> 100` 3 times, then summarize with `summarize.py`. Parity uses screen capture; skip any row whose column 31 is greater than 0, because a foreign window obstructed the capture.
5. **Examples smoke.** Run `smoke.sh <control|custom> <before|after|after-off> 10`.
6. **Recipe crate** (Synexia PR #10920). Run `mvn -o test` in `recipe-crates/swt-visibility-gate-20261013` with `JAVA_HOME` set to JDK 21. It passed 3/3 on `cbeeffbe`.

## Not run on this host

- GTK and Cocoa: design only.
- Cloaked (another virtual desktop): switching the user's virtual desktop would disturb the session.
