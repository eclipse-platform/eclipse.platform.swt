# Viewport programme: state and plan (handover, 2026-10-17)

The lead for the SWT/Nebula/M3JDK viewport programme reads this first, together with
`C:/m3-superset/tools/VIEWPORT_AGENT_BRIEF.md` (rules 1-12 and the machine rules). Everything below is
measured or committed. Anything not run is marked as not run.

## 1. What the user wants

1. **Viewport is king.** Every SWT and Nebula widget becomes a mediated viewport.
   - Same public contract and same visible behaviour as stock; the reference is the official SWT 3.135.0 jar.
   - Order: Tree -> Table -> List -> all widgets -> Nebula; then M3JDK (Swing, SwingX, JavaFX).
   - "This is SWT/Nebula itself, not a layer"; JNI is allowed; upstream-quality code, offered to Eclipse.
2. **Five startup modes:** `-Dswt.widget.mode=viewport|original|custom|skija|gpu`, plus `-Dswt.widget.mode.<Widget>`.
   - viewport is the default.
   - original is stock, except for performance work that keeps behaviour identical.
   - custom, skija and gpu are drawn planes over the same viewport model.
   - "All five modes are optimized heavily."
3. **Fix all errors.** The default modes fix genuine upstream defects (brief rule 12; `UPSTREAM_DEFECTS.tsv` per widget).
   Look and feel is secondary but important. Drawn-mode themes: `native` (default), `swing`, `initiative31`.
4. **Donors.** Anything on GitHub about Swing, SwingX, AWT, JavaFX, SWT, Draw2D, Nebula or GEF is a donor.
   - Initiative 31 maps SWT onto Swing (prototype-swing); there are many SWT/Swing libraries.
   - Inline efficient collection structures from our own repositories (Synexia, M3jdk21) freely, with provenance.
   - Donor-first (`FOSS_REUSE_DECISION.tsv` before new architecture).
5. **Attribution.** New files carry `C:/m3-superset/tools/SYNEXIA_HEADER.java.txt`:
   "Synexia, Hitesh Soliwal (hsoliwal@gmail.com)", `Apache-2.0 OR EPL-2.0`.
   - Upstream files get a contributor line "Hitesh Soliwal (Synexia) - ...".
   - Atomize and patternize everything (Synexia).
6. **Tests never touch the user's desktop:** no mouse moves, no focus theft. UI runs happen only on hidden desktops.
7. **Logging:** java.util.logging, asynchronous, through `org.eclipse.swt.internal.SwtLog`.
8. **Commit and push after every leaf.** The user merges PRs; `gh pr merge` is denied to the agent.

## 2. Repositories, branches, PRs (heads as of this handover)

| Track | Repo / branch | Head | PR |
|---|---|---|---|
| Integration | hsoliwal/eclipse.platform.swt `m3/viewport-integration-20261016` | 36dceb43b9 (50 ahead of master) | **#203 draft** (round 2; #201 merged 2026-10-17) |
| Tree (lead) | `m3/tree-viewport-20261016`, worktree `C:/m3-superset/swt-tree` | 0904279df1 | via #203 (#196 merged) |
| Table | `m3/table-viewport-20261016`, `K:/m3-work/agents/swt-table/wt` | 48dc6cf84c | via #203 (#200 merged) |
| List + Combo | `m3/list-viewport-20261016`, `K:/m3-work/agents/swt-list/wt` | 6ec7052d3a | via #203 (#199 merged) |
| All widgets | `m3/allwidgets-viewport-20261016`, `K:/m3-work/agents/swt-allwidgets/wt` | 6a1fe02b18 | via #203 (#198 merged) |
| Order statistic | `m3/tree-order-statistic-20261016` | e757cb8dd7 | #197 merged |
| Drawn modes | `m3/render-modes-20261017`, `K:/m3-work/agents/render-modes/wt` | 403c384e5f | **#202 draft** |
| Attribution | `m3/synexia-attribution-20261017`, `K:/m3-work/agents/synexia-attribution/wt` | f19ef43695 (WIP) | none yet |
| Nebula Grid | hsoliwal/nebula `m3/grid-viewport-20261016`, `K:/m3-work/agents/nebula-grid/wt` | 23f7f5cdd5 (G3 WIP) | **nebula#235 draft** (#234 merged) |
| M3JDK Swing | hsoliwal/M3jdk21 `m3/swing-viewport-20261017`, `K:/m3-work/agents/m3jdk-swing/wt` | 248efacb9f | **M3jdk21#1001 draft** |
| Recipes | hsoliwal/com.synexia `m3/viewport-catalogue-recipes-20261016`, worktree `K:/m3-work/syn-recipes` (sparse) | a0d7fe239f07 | none |
| Attribution (Synexia) | hsoliwal/com.synexia `codex/m3-synexia-attribution-20261017` | fa36c71fe11b | none yet |

Not ours, left alone: `K:/m3-work/com.synexia` has 101 uncommitted changes on `codex/m3-java-corpus-extend-20261009`.
These are older work and possibly another session's. Ask the user before touching them.

## 3. Method (keep it)

- **Recorded differential.**
  - Seeded public-API scripts are recorded on the official jar (`-Dswt.widget.mode=original`), each test in its own JVM, on a hidden desktop.
  - The candidate build compares against the recording.
  - Survey mode: `-Dswt.viewport.survey=true`.
  - Tree variants: plain, check+columns, presentation (with a per-step pixel CRC).
  - Pixels are captured with keyboard cues hidden: stock's cue state depends on input history and test order (CueOracle).
- **Oracles before mirroring.** Measure the stock rule with a small program on both jars, then mirror it, then record the evidence.
  - Sources and stock outputs: `docs/m3/tree-viewport/evidence/oracles/` on the Tree branch.
  - NoCaretKey, DisposeAboveTop, SetDataTiming, CaretDispose, Cue, Empty.
  - Scratch replay/oracle sources: `K:/codex-work/agent-temp/claude/e--ws2-com-synexia/a6de83fe-6489-4056-8689-f1217602e9cc/scratchpad/replay/`.
  - `ReplayTree args: seed stop recording [columns|presentation]`, `-Dreplay.before=<test>`, `-Dreplay.hidecues=true`.
- **Recipe-first hooks** into existing SWT files.
  - Synexia `m3-viewport-catalogue-recipes`: `tools/gen_tree_viewport_yml.py`, then `mvn -o -q compile`, then `M3PerFileApply` twice (the second pass must report UNCHANGED).
  - Identical statements are deduplicated, so give every hook a distinct statement.
- **Tools** (`C:/m3-superset/tools`):
  - `build_lock.sh` and `build_and_tests.sh <wt> <outdir> <stamp>`;
  - `ui_lock.sh` (3 slots, each its own desktop `m3-ui-tests-N`), `hidden_java.sh`, `commit_free_kb.sh` (timed memory probe);
  - `compile_platform.sh`;
  - the git-status watcher `K:/m3-work/logs/git-status-watch.ps1` (the user approved killing git-status storms).

## 4. Status and next leaves per track

### Tree (lead): `docs/m3/TREE_MEDIATED_VIEWPORT.md`
- **Passing against official 3.135.0:** plain and check+columns variants (12 seeds x 150 steps), acceptance 8/8, resident-plan tests 8/8.
  - Oracles CaretDispose, Empty, NoCaretKey and DisposeAboveTop are identical to stock.
- **Open:** presentation variant, 66 kinds of difference (cascades).
  - Next: replay each seed with `ReplayTree <seed> 149 K:/m3-work/runs/tree-stock-recording-i presentation` on both jars and fix the first divergence.
  - Seeds with early hints: 5 (step 138 key HOME), 6 (137 setItemCount), 8 (112 image), 10 (118 key DOWN).
  - Recording: `K:/m3-work/runs/tree-stock-recording-i`. Latest jar: tv20261017j in `C:/m3-superset/tvd`.
- **Aspect gaps, in order:**
  1. custom draw (MeasureItem/EraseItem/PaintItem);
  2. tree font change and item height;
  3. mouse via messages (click, expander, check box, double click);
  4. DnD feedback; tooltips; accessibility; DPI; dispose and reentrancy in listeners.
  - Also add a Tree `UPSTREAM_DEFECTS.tsv` when defects are found.
- **Perf candidate:** Tree `setItemCount(10^6)` costs 4.9 ms, O(window); the official jar cannot do it at all. Low priority.

### Table / List / Combo / all widgets
- Mirror the Tree rules (same family) and check each against stock with the oracles:
  - synchronous repaint on setSelection, keys and wheel;
  - no keyboard-cue changes;
  - emptiness event and image-list release;
  - off-window geometry through a probe row with stale-handle validation.
- Perf candidates from the five-mode benchmark: Table scrollPage 13.8 vs 9.0 ms and selectAll 6.1 vs 2.2 ms; List scroll10k 10.0 vs 2.3 ms and scrollPage 4.2 vs 2.3 ms.
- All widgets next: L7 decision (a Nebula Table-like widget), LF1/LF2 (look-and-feel seam and defaults).
  - Done there: CTabFolder, ExpandBar, CoolBar; Text and ToolBar measured with no change needed.

### Drawn modes (render-modes, #202)
- Stage 1: the native control stays as an invisible behaviour plane; the drawn plane paints the resident window.
- Tree custom and skija pass the non-pixel probes of both Tree variants.
- Next:
  - R5: Direct2D via a minimal JNI shim (gpu on win32; OpenGL through Skija elsewhere; Dawn/WebGPU is a candidate).
  - R7: optimizations.
  - Then Table and List.
  - Then themes (native, swing, initiative31); look and feel is secondary.

### Nebula Grid (nebula#235)
- Done: G2 O(log n) row tree; legacy 457/457; suite 604/604; D1-D21; header overflow.
- Next:
  - G3: selection as ranges, focus by position. A WIP commit exists, not built.
  - G4-G6 per `docs/m3/grid-viewport`.

### M3JDK (M3jdk21#1001): the user asks about this repeatedly
- Goal: the same viewport mediation for Swing (JTree, JList, JTable), then SwingX and JavaFX ("DOOP Swing/SwingX/JFX").
- Leaf 1: arithmetic layout cache for JTree, behind `m3.swing.viewport`, with a seeded differential against the stock `AbstractLayoutCache`.
  - Local proof: `javac/java --patch-module java.desktop`. OpenJDK make runs on CI only.
- Donors:
  - first party: SWT `VirtualTreeSiblingIndex` (AVL order statistics), Grid `GridRowTree` (subtree size, visible count, pixel extent), Synexia collections;
  - patterns: OpenJFX VirtualFlow; SwingX (LGPL, pattern only).
- Licence:
  - `src/**` is GPLv2+CPE, through an explicit relicence row naming Hitesh Soliwal (Synexia) as rightsholder;
  - `m3/**` is Apache-2.0.
- Next: JList, then JTable (SizeSequence is already prefix sums; selection as ranges), then the JavaFX VirtualFlow comparison.

### Attribution + atomize/patternize (Synexia)
- Done: classification ledger in `docs/m3/attribution/` (WIP commit).
  - SYNEXIA_AUTHORED 364, UPSTREAM_MODIFIED 1,324 (171 with real fork content get a contributor line; 1,123 are formatting only and get none), DONOR_DERIVED 10.
  - The 98 `.m3` evidence files are not edited.
- Next:
  1. apply the recipe to a branch from integration, second pass UNCHANGED, compile, then open a draft PR;
  2. Nebula the same way;
  3. atomize/patternize the corpus into `docs/M3-SCALE/runs/20261017-swt-viewport-atomize/`, with `PATTERN_REUSE.md` (patterns shared across the Tree/Table/List/Grid mediators).

### Donor catalogue
- `K:/m3-work/donors/catalogue/DONOR_CATALOGUE.tsv` + `TRICKS.md` (donor-survey agent; in progress).
- Session-log links: `K:/m3-work/donors/catalogue/links/USER_LINKS.md`, 180 repos/sites typed by the user.
  - Kept local on purpose: they contain personal links.

## 5. Agents (in-session; resume with SendMessage to the id)

| id | track |
|---|---|
| a9cb7083090852321 | Table |
| aee2e6b4539e3dbab | List + Combo |
| a71bd716dfddf4a41 | all widgets |
| af8772c3d2096e614 | Nebula Grid |
| a6d60c3dd8a5f3a96 | drawn modes |
| a1500db490724abfc | attribution + atomize |
| a4f3571116e413c4d | M3JDK Swing |
| a6ca57b181ca089ab | donor survey |

- Session restarts stop them. Resume each one, telling it to commit any WIP first.
- In a new session these ids are gone. Re-spawn from the brief plus this plan; each branch and worktree above is the agent's state.
- Merge round for integration:
  1. fetch each branch with an explicit refspec;
  2. `git merge --no-ff` in `K:/m3-work/agents/consolidate/wt`; on a shared-harness conflict, take the newest `ViewportDifferential.java`;
  3. `compile_platform.sh`;
  4. push, then update #203's body.

## 6. Pitfalls already paid for

- **Bash tool:**
  - Python heredocs mangle backslashes; write patch scripts to files.
  - Git Bash rewrites a leading `/` argument into a Windows path; use `MSYS_NO_PATHCONV=1` (this added a bogus sparse rule once).
  - `rm -rf $VAR` is blocked by the safety check; avoid it.
- **Synexia clones are sparse.** Use `git add --sparse` and `git sparse-checkout add` for the crate. HTTPS pushes sometimes hang up; retry with `-c http.version=HTTP/1.1`.
- **Fetch with explicit refspecs:** tracking refs go stale otherwise. GitHub prints "protected ref" or "creations restricted" noise on push; verify with `ls-remote`.
- **Hung probes:** a hung `Get-CimInstance` blocked every lock. Kill only with the user's approval; probes now have timeouts.
- **Stale native handles:** TreeView handles are reused, so always validate a facade's handle (`residentHandle`, `needsProbe`).
- **Shared JVM state:** the image list survives within a JVM, and test order changes keyboard cues. Record per test class and compare like with like.

## 7. Decisions waiting for the user

1. Merge #203, #202, nebula#235, M3jdk21#1001 when ready.
2. Keep or drop the EPL-2.0 alternative in the Synexia header (it keeps the Eclipse upstream offer possible).
3. What to do with the 101 uncommitted changes in `K:/m3-work/com.synexia`.
4. GTK and Cocoa have never been run.
5. Fork CI on GitHub is disabled; the user must click "Enable workflows".
