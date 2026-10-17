# Viewport agent brief (shared by every widget agent)

Goal (user, 2026-10-16): every SWT and Nebula widget becomes a viewport with the **same public contract and the
same visible behaviour**. We implement SWT/Nebula itself, not a layer on top. Everything is allowed, JNI
included, as long as the API contract and the effective behaviour do not change. "Viewport logic is king";
this is what will be offered to Eclipse, so write upstream-quality Eclipse code.

## Reference implementation: win32 Tree (read first)

- Worktree `C:/m3-superset/swt-tree`, branch `m3/tree-viewport-20261016`, PR hsoliwal/eclipse.platform.swt#196.
- Design: `docs/m3/TREE_MEDIATED_VIEWPORT.md`. Mediator: `bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets/TreeViewport.java`.
- Common infra in `Eclipse SWT/common/org/eclipse/swt/widgets/`: `VirtualScrollMetrics`, `VirtualTreeResidentPlan`,
  `VirtualItemState`, `VirtualListModel`, `VirtualSelectionModel`, `ViewportScrollLayout`, `VirtualRowExtents`,
  `VirtualPageHost`; residency engine `org.eclipse.swt.internal.residency` (`docs/m3/M3_ITEM_RESIDENCY.md`).
- Pattern: the outer window (`hwndParent`) is the viewport and owns the logical scroll bar; the native control is a
  content plane holding only the resident window; public items are facades whose state lives in lanes
  (bit flags, packed ints); a facade's native handle is validated before use (handles are reused natively).
- Tests: acceptance (`Test_org_eclipse_swt_widgets_Tree_Viewport`) and the **recorded differential**
  (`Test_org_eclipse_swt_widgets_Tree_ViewportDifferential`): seeded random public-API scripts run against a
  plain widget and a viewport widget; every step records answer | scroll bar | selection | focus | events, and
  pixels per seed; any difference fails with the op log (that log is the reproduction). Also
  `docs/m3/tree-viewport/aspect_matrix.py`: every public member and behaviour aspect mapped to its test.
- Native behaviour that the mediator must reproduce is **measured with an oracle program first** (see
  `docs/m3/tree-viewport/evidence/EnsureVisibleOracle.java.txt`), never guessed.

## Rules

1. **Donor-first.** Before new architecture write `docs/m3/<widget>-viewport/FOSS_REUSE_DECISION.tsv`
   (columns: capability, candidate_repo, commit, source_paths, license, contract_fit, decision, rejected_reason,
   adapted_hashes, notice_path). Search first: this fork's PRs and history
   (`gh pr list --repo hsoliwal/eclipse.platform.swt --state all --search <widget> --limit 100`,
   `git log --all --oneline -S<symbol>`, `git log --grep`), hsoliwal/nebula, swt-initiative31 repos, JetBrains/skija,
   Eclipse TM, ECF, Sequoyah SWT VNC. **User 2026-10-17: anything on GitHub pertaining to Swing, SwingX, AWT, JavaFX, SWT,
   Draw2D, Nebula and GEF is a donor for the viewport variants** (e.g. OpenJFX VirtualFlow, Flowless, NatTable, NetBeans Outline, Glazed Lists,
   FlatLaf, SWTSwing). The shared catalogue is K:/m3-work/donors/catalogue/DONOR_CATALOGUE.tsv + TRICKS.md (donor-survey
   agent); check it before designing. Licence per row decides ADAPT/PORT vs pattern-only. Lean structures from hsoliwal/com.synexia and hsoliwal/M3jdk21 may be
   **inlined as copies** (never a dependency), with provenance noted.
2. **Docs precede code**: `docs/m3/<WIDGET>_MEDIATED_VIEWPORT.md` (design, measured native rules, status).
3. **Recipe-first hooks.** Hooks into existing SWT files are one-line statements inserted by the OpenRewrite
   recipe `com.synexia.openrewrite.viewport.M3InsertStatements` (positions AFTER, BEFORE, BLOCK_START, BLOCK_END,
   METHOD_START, METHOD_END; selectors `parameterCount`, `parameterTypes`). Crate (read-only for you):
   `K:/m3-work/syn-recipes/synexia-openrewrite-recipes/crates/m3-viewport-catalogue-recipes`; see
   `tools/gen_tree_viewport_yml.py` there and how `C:/m3-superset/tools/tree_viewport_cycle.sh` applies it with
   `M3PerFileApply` (classpath `$C/target/classes;$(cat $C/target/cp.txt)`). Put YOUR generator and YAML in your
   own worktree under `docs/m3/<widget>-viewport/recipe/` (YAML at `.../recipe/META-INF/rewrite/m3-swt-<widget>-viewport.yml`)
   and prepend that `recipe` dir to the classpath. Never write into the Synexia crate (the lead ports it later).
   New classes (the mediator, tests) are written directly. Apply twice: the second pass must be UNCHANGED.
4. **Viewport is the DEFAULT** (user decision 2026-10-16). Modes are chosen at startup by one SWT-global switch:
   `-Dswt.widget.mode=viewport|original|custom|skija|gpu` (global) and `-Dswt.widget.mode.<Widget>=...` (per widget class).
   `original` = stock SWT, `custom` = GC custom-drawn, `skija` = Skija-drawn, `gpu` = drawn on the graphics hardware
   (Direct2D on win32, OpenGL elsewhere; user decision 2026-10-17). **All five modes are optimized heavily**
   (user 2026-10-17): in `original` only where behaviour stays identical to stock (differential vs the official jar). Add the class
   `Eclipse SWT/common/org/eclipse/swt/internal/WidgetMode.java` to your branch **byte-identical** to
   `C:/m3-superset/tools/WidgetMode.java.txt` (so branches merge cleanly) and gate your widget with
   `static final boolean ENABLED = WidgetMode.of ("<Widget>", WidgetMode.VIEWPORT) == WidgetMode.VIEWPORT;`.
   Nebula reads the same property names (`swt.widget.mode`, `swt.widget.mode.Grid`) through its own copy of that
   class in a Nebula internal package. Upstream tests that are white-box tests of the replaced native internals skip
   (JUnit assumption) only when the viewport is active. Tests for the stock path run with `-Dswt.widget.mode=original`.
5. **CPU/GPU split**: never do O(n) backing-structure work on the UI thread; only O(window) native operations there.
6. **Logging** (user 2026-10-16): java.util.logging, asynchronous, through `org.eclipse.swt.internal.SwtLog`
   (copy `C:/m3-superset/tools/SwtLog.java.txt` byte-identical; Nebula: own package copy):
   `SwtLog.getLog ("org.eclipse.swt.widgets.<Widget>")`, `LOG.trace/debug/info (() -> ...)` only. Never System.out.
7. **Event objects** may be reused from a pressure-aware LRU once out of listeners (unless the user kept them).
8. **Code style**: Eclipse SWT style (tabs, `method (args)` spacing), Javadoc, no
   internal jargon in public docs. Methods small. LF line endings (Python writes need `newline=''`; check
   `git diff --cached --check` before every commit).
   **Attribution (user 2026-10-17): every new file we author carries the Synexia header**
   `C:/m3-superset/tools/SYNEXIA_HEADER.java.txt` (Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others; Apache-2.0, with EPL-2.0 as
   the alternative so Eclipse can take it; `SPDX-License-Identifier: Apache-2.0 OR EPL-2.0`); scripts use
   `SYNEXIA_HEADER.script.txt`. Upstream Eclipse/Nebula files we modify keep their licence and get a contributor line
   `Synexia - <what changed>`; donor-derived files keep the donor's licence and notice and add Synexia as contributor.
   Never relabel third-party code.

9. **One family, maximum reuse, minimum planner** (user 2026-10-16): "Table must be close to Tree and List must
   be close to Table." Shape every mediator like `TreeViewport` (same method names, same phases: capture → plan →
   retire/insert → commit → bind → restore focus; same `residentHandle` validation; same selection-neutral caret
   restore; same recorded-differential harness). Put shared logic in `Eclipse SWT/common/.../widgets` classes
   reused by all (a flat list is a Tree with depth 1; a Table is a List with columns). The planner per rebuild must
   be the minimum: diff old vs new window, touch only changed rows; no per-row allocation on the hot path.
10. **Optimize massively, arithmetic first.** Prefer integer arithmetic structures (Fenwick/BIT prefix sums for row
    heights and visible counts, order-statistic indexes, binary lifting for ancestor/row mapping, interval
    arithmetic for damage rectangles, bit-packed lanes, sorted int arrays + binary search) over object graphs.
    Changing JNI/native code is allowed when contract and behaviour are kept.
11. **More donors:** Eclipse GEF **Draw2d** (`org.eclipse.draw2d.Viewport`, `FreeformViewport`, `RangeModel`,
    `DefaultRangeModel`, `ScrollPane`, `DeferredUpdateManager` damage coalescing; EPL-2.0) and the Initiative 31 /
    Skija survey at `C:/m3-superset/tools/I31_SKIJA_TRICKS.md` (visible-range math, paint cull, SetData once,
    row-rect damage, scrollbar model, ListLikeModel anchor/current; `Canvas.scroll` ScrollWindowEx blit).
    Initiative 31 renderers belong to the `custom`/`skija` modes only; the `viewport` mode keeps native pixels.

12. **Upstream defect policy** (lead decision 2026-10-16, from "same behaviour" + "our release fixes errors"):
    - `original` mode stays behaviourally identical to stock (it is the compatibility reference); never fix stock
      defects there.
    - The default mode (`viewport`, also `custom`/`skija`) fixes genuine upstream defects: behaviour that
      contradicts the API's own Javadoc, crashes, data loss, or clipped/wrong rendering.
    - Every such fix gets a row in `docs/m3/<widget>-viewport/UPSTREAM_DEFECTS.tsv` (widget, API, stock behaviour, our
      behaviour, cause with stock file:line, Javadoc quote), and the stock differential accepts exactly the listed
      divergences (in the widget's test, not the shared harness). Behaviour changes that are improvements but not
      defect fixes stay out unless the user asks.

## Machine rules (hard)

- **Hidden desktop (user requirement 2026-10-16: never move the user's mouse, never steal focus):** every GUI
  program or UI test runs on the invisible desktop `WinSta0\m3-ui-tests`:
  `bash C:/m3-superset/tools/ui_lock.sh <agent> bash C:/m3-superset/tools/hidden_java.sh <seconds> <log-file> <java args...>`
  (launcher `C:/m3-superset/tools/run_hidden_desktop.ps1`; WM_PRINT captures, uxtheme, window messages work there;
  SendInput/Display.post/SetCursorPos do NOT — tests needing real input are separate, opt-in, and must ask the lead).
  ui_lock.sh hands out up to 3 slots (`M3_UI_SLOTS`), each its own invisible desktop `m3-ui-tests-<slot>`
  (exported as `M3_UI_DESKTOP`, read by hidden_java.sh), so three agents run UI tests at once. Memory probes
  (`tools/commit_free_kb.sh`) have hard timeouts.
  Record references on the hidden desktop too, so both sides render under the same conditions.
- **Reference = official stock SWT:** record the reference with the official jar
  `~/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar`
  on the class path (harness `ViewportDifferential.isReference`), not only the fork's `original` mode (the fork's
  original paths can deviate from stock, e.g. Tree bar maximum 294 vs 299).
- (legacy) visible-desktop runs used
  `bash C:/m3-superset/tools/ui_lock.sh <agent> bash C:/m3-superset/tools/run_guarded.sh <seconds> <java ...>`.
  A hung UI test leaves a busy window on the user's desktop. Acceptance tests must skip (JUnit assumption),
  not block, when the viewport is inactive. Park the cursor outside the shell before pixel captures.
- **Builds:** every javac/jar build runs ONLY under `bash C:/m3-superset/tools/build_lock.sh <agent> <cmd>`.
  Use `C:/m3-superset/tools/compile_platform.sh <worktree> win32` (compile check) and
  `C:/m3-superset/tools/build_and_tests.sh <worktree> <your-out-dir> <stamp>` (jar + tests). Your out dir:
  `K:/m3-work/agents/<agent>/out`. JAVA_HOME `C:/Program Files/Java/jdk-21`; use `python`, never `python3`.
- **Memory is scarce.** Gates refuse below 4 GB (build) / 6 GB (UI) commit headroom; on refusal wait with a
  bounded retry loop inside one bash script (e.g. up to 30 x 20 s), then retry. Never stop or kill processes
  you did not start (other sessions' `git status` processes are the user's).
- **Disk:** C: ~5 GB, K: ~8 GB free. Worktrees go to `K:/m3-work/agents/<agent>/wt` (blobless repo exists at
  `C:/m3-superset/swt-tree`'s object store; use `git -C C:/m3-superset/swt-tree worktree add ...`).
- **Git:** branch `m3/<widget>-viewport-20261016` from `origin/master` (hsoliwal/eclipse.platform.swt) unless told
  otherwise; commit often with real messages ending in
  `Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>`; push to origin; open a **draft** PR against
  `hsoliwal/eclipse.platform.swt` master whose body ends with `🤖 Generated with [Claude Code](https://claude.com/claude-code)`.
  Never merge, never force-push, never `--admin`, never touch master directly. Do not change ACLs on
  `C:\m3-work-c` or `C:\m3-superset\i31-build`. Do not touch other agents' worktrees.
- **Truth:** report only what you ran; tests not run are "not run", with the reason.

## Report back (under 500 words)

Branch, commits, PR URL; what is mediated; measured native rules; tests and their exact results; open gaps;
next leaves.
