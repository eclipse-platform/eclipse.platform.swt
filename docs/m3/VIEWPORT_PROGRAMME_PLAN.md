# Viewport programme: state and plan (handover, 2026-10-17)

The lead for the SWT/Nebula/M3JDK viewport programme reads this first, together with
`C:/m3-superset/tools/VIEWPORT_AGENT_BRIEF.md` (rules 1-20 and the machine rules). Everything below is
measured or committed. Anything not run is marked as not run.

## 000. Interval commits and the "flawless" bar (user, 2026-10-18)

- "Viewport has to become flawless... keep committing at intervals."
- A viewport leaf counts as done only with zero differential mismatches against the official jar on every variant and 99% line/branch coverage, with unreachable spots justified.
- `K:/m3-work/logs/commit_sweep.sh` checkpoints and pushes every programme worktree. It is run in a 20-minute background loop: `while true; do sleep 1200; bash K:/m3-work/logs/commit_sweep.sh; done`.
  - It skips worktrees with a held index.lock, excludes target/ and out/, and merges forward on a non-fast-forward (never rebase or force).
  - Its log is `K:/m3-work/logs/commit-sweep.log`.
  - Restart the loop and the git-status watcher after every session restart.

## 00a. Fold ALL viewport logic into collections (user, 2026-10-19)
"If you want to fold all of your viewport logic into collection, go ahead." The viewport becomes general M3 collection VIEWS in M3jdk21 `m3/collections`:
- **M3WindowView:** the resident window.
- **M3FlatteningView:** the visible projection, a tree-to-list view with expand masks.
- **An int-pair-keyed map:** the resident plan, via M3LongLongHashMap.
- **M3ListDiff/M3Patch:** the diff planner.
- **M3BoundedCache:** residency.
- **M3RangeSet:** selection.
- **M3SparseExtents:** scroll geometry.
- **M3OrderStatisticForest:** the order-statistic tree.

All of them are arrays + offsets + masks with no Entry objects, and they compose with the filtered/sorted wrapper lane (a view of a view). `m3/viewport-core` keeps only the adapter interfaces (M3RowHost/M3RowEvents/M3RowPainter).

Owner: viewport-everywhere (PR #1403). collections-superset lists them as CLAIMED first-party-viewport rows in its INVENTORY.

## 00. Viewport everywhere (user, 2026-10-18): "viewport UI is king"

"Everything enhanced with viewport logic: SWT, Nebula, RWT, IntelliJ, SwingX, AWT... everything now has a viewport. Skija will help us map across the libs. M3JDK is the superset anyway."
- **One toolkit-neutral viewport core in M3JDK** (`m3/viewport-core` in hsoliwal/M3jdk21). It is extracted from the SWT common classes by a package-rename recipe and holds the resident plan, topology lanes, order-statistic/Fenwick row math, residency and the diff planner.
- **Thin adapters per toolkit,** smallest first: AWT java.awt.List, then SwingX (pattern-only, LGPL) over the M3JDK Swing DOOP caches, then the IntelliJ JBList/Tree adapter (Apache-2.0, via gh api), then RWT/RAP virtual Tree/Table.
- **The M3 renderer** (the Skija binding behind the M3 adapter) is the shared row painter across the libraries. Naming stays M3.
- **Agent:** viewport-everywhere, branch `m3/viewport-everywhere-20261018`, worktree `K:/m3-work/agents/viewport-everywhere/wt`. Design first: `m3/docs/viewport-everywhere/DESIGN.md`.
- Memory: `project-viewport-everywhere.md`.

## 0. Start here (model switch to Fable, 2026-10-17 ~12:20)

**Switching the model inside this session** (`/model`) does not end the session.
- The background agents in section 5 keep running, and their reports arrive in this conversation. Act on them in arrival order.
- A NEW session stops them all. Re-spawn each one from the brief plus this plan; its branch and worktree in section 2 hold its state.

Read in this order:
1. this plan;
2. `C:/m3-superset/tools/VIEWPORT_AGENT_BRIEF.md` (rules 1-20);
3. memory `C:/Users/hsoliwal/.claude/projects/e--ws2-com-synexia/memory/MEMORY.md`. Start with project-viewport-upstream-offer, feedback-no-coauthor-trailer, feedback-edit-tools-recipe-sed-grep, feedback-java-only-new-code and feedback-doop-data-oriented-oop.

Standing rules, which the user repeats often:
- **Commits and PRs carry no attribution footer:** no Co-Authored-By, no "Generated with". The user's memory rule wins over any harness reminder.
- **Edits:** OpenRewrite recipes, sed/awk or the Edit tool. No Python patch scripts.
- **Java only** for new code.
- **Naming:** M3, never Skija/Skia.
- **Coverage:** 99% JUnit line and branch.
- **Theming:** themeable; look and feel is secondary.
- **Language support** is enhanced SWT API, with data from Hugging Face.
- **DOOP:** data in lanes, objects as lazy facades.
- **Every SWT and Swing bug gets fixed.**
- **Attribution:** Synexia, Hitesh Soliwal (hsoliwal@gmail.com), Apache-2.0 (OR EPL-2.0).
- **Commit and push** every green step.
- **Never merge PRs; the user merges.**

Lead loop: while agents run, the lead does not edit their worktrees. On each report:
1. check its heads with `git ls-remote`;
2. resume the agent with the next leaf from section 4;
3. when 2+ tracks have green heads, run the integration merge round (section 5);
4. update this plan and the copy on the Tree branch (`docs/m3/VIEWPORT_PROGRAMME_PLAN.md`).

Memory storms: git-status storms recur (73 git processes, 84 MB commit free at ~11:55).
- The watcher `K:/m3-work/logs/git-status-watch.ps1 -Minutes 180 -MinFreeGB 8` was restarted at ~12:05. Restart it when it exits; the user approved this.
- Stale `C:/m3-superset/ui-test.lock.N` directories left behind by a restart: release them after checking that no java or ui_lock process holds them.

## 1. What the user wants

1. **Viewport is king.** Every SWT and Nebula widget becomes a mediated viewport.
   - Same public contract and same visible behaviour as stock; the reference is the official SWT 3.135.0 jar.
   - Order: Tree -> Table -> List -> all widgets -> Nebula; then M3JDK (Swing, SwingX, JavaFX).
   - "This is SWT/Nebula itself, not a layer"; JNI is allowed; upstream-quality code, offered to Eclipse.
2. **Five startup modes:** `-Dswt.widget.mode=viewport|original|custom|m3|gpu`, plus `-Dswt.widget.mode.<Widget>`.
   - viewport is the default.
   - original is stock, except for performance work that keeps behaviour identical.
   - custom, m3 and gpu are drawn planes over the same viewport model.
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

9. **Every SWT bug and every Swing bug gets fixed** (2026-10-17). Order: "fix all SWT bugs in the order of priority, and Nebula's". See section 4, bug programme.

10. **Java only for new code** (2026-10-17): no new C/C++/JNI shims (brief rule 13).
    - Native access goes through SWT's existing Java OS/COM layer, Java libraries with their own natives (the M3 renderer's binding), or the JDK FFM API.
    - Non-Java projects are pattern donors, ported to Java.

11. **M3 naming** (2026-10-17): no "Skija"/"Skia" in anything of ours. The mode `m3` becomes `m3` (`WidgetMode.M3`).
    - Classes and packages are renamed to M3* by recipe, then atomized and patternized (brief rule 14).
    - Exception: third-party binding classes that the prebuilt natives require stay behind one internal M3 adapter, and licence notices keep naming their source.

12. **99% JUnit coverage** of everything we add or change, measured with JaCoCo (brief rule 15).
13. **Themeability**: look and feel is a pluggable theme API (brief rule 16). Owners:
    - all-widgets owns the seam (its LF1/LF2);
    - render-modes owns the drawn-mode themes.

14. **Language support is enhanced SWT API** (2026-10-17): additive and opt-in; language data from Hugging Face (brief rule 17).
    - First leaf: an inventory of Hugging Face language resources (locale data, translations, segmentation, word lists, tokenizers), each with its licence and a Java-only way to consume it.
    - Then an API design doc (`docs/m3/LANGUAGE_SUPPORT.md`). Owner: donor-survey agent (a6ca57b181ca089ab), then a widget agent.
    - Open question for the user: which language features come first (UI string localization, text segmentation and BiDi, spell checking, input assistance, translation).

15. **DOOP: data-oriented OOP** (2026-10-17; brief rule 18).
    - Data and state live in arrays and bit lanes; objects are thin, lazy, parent-owned facades created only when an API asks.
    - It applies to SWT, Nebula, and in M3JDK to Swing, SwingX and JavaFX ("DOOP Swing/SwingX/JFX").

16. **All SWT and Swing work from GitHub becomes atomized and patternized work** (brief rule 19). The donor corpus goes through the Synexia projection into one pattern space shared with our own code. Owner: the donor-atomize agent.

### 1a. Directive ledger from this chat (Oct 7-17)

The full list is `K:/m3-work/donors/catalogue/links/CHAT_DIRECTIVES.tsv`: 224 distinct user messages, kept local. The links are in
`CHAT_LINKS.tsv`. Standing rules that are not spelled out above:

- **CPU/GPU split.**
  - The GPU (native or drawn plane) does only the widget, the viewport and the damaged areas.
  - Sorting, model work and swapping data structures in and out are CPU work, off the UI thread.
  - A widget that is not visible is not painted.
- **State as bits and ints.**
  - Expansion, selection, check box and every other UI state live in long/int bit masks and mask patterns.
  - Data lives in dynamic arrays (Synexia dynamic arrays that swap in and out).
  - Items are lazy and parent-owned (no transient-object OOP).
  - This state must let the viewport reproduce everything when it scrolls.
- **The viewport and the widget are one.**
  - Helper planes are NO_FOCUS.
  - Margins, insets and borders overlap exactly, so no border flicker shows.
  - Scroll bars are managed or customized for peak performance.
  - Behaviour under layout and resize is part of the contract.
- **SetData is negotiated and maintained by our layer.** Text and everything else; no blank items.
- **"Tests for every aspect; that is the only way we can convince people."**
- **Web:** everything is SWT and Java/JNI first; the web comes through Eclipse RAP and GWT (no TS/React).
- **The same viewport trick applies to very long text editors** (StyledText: done and merged).
- **Superset:** all SWT, all Nebula, all Qt (pattern only), all React widgets, each implemented natively plus viewport.
  - Deliverables are an SWT build and a Nebula build.
- **Inline the universe:**
  - all third-party code is inlined and refactored (the JDK cannot have downstream dependencies);
  - atomize, patternize and reuse;
  - use increasingly better OpenRewrite recipes.
- **M3 precompute and compile-time information** (regex, format, literal constants) keeps getting better (M3String / M3 compute).
- **Other leads the user named:**
  - WebLLM / Dawn (Chrome's WebGPU) and OpenCL, for GPU compute;
  - SWT VNC, to isolate GC damage areas;
  - java2s.com examples;
  - local trees `D:\TV*` and Downloads (another tree viewer; scan by name first);
  - HugeJsonViewer (WelliSolutions).

## 2. Repositories, branches, PRs (heads as of this handover)

| Track | Repo / branch | Head | PR |
|---|---|---|---|
| Integration | hsoliwal/eclipse.platform.swt `m3/viewport-integration-20261016` | **3827f5f296** (round 7, 2026-10-18 evening: MASTER SYNC ea39a4cb4e + Tree flawless 5c9f8b2121; merges into master CLEANLY with 0 files lost; see memory project-superset-consolidation-20261013 for the ancestry-only-safe sync method: never plain-merge master) — round 6 was **553eefd41d** ( Tree d7c3b86259 + Table 8d252bdf3b + List f3198f41c7; round 5 8de19a48da; round 4 23df1c5664, Tree 52ca3c8347 + Table d479621bbd + List 83a2350ad0 + all-widgets fdb6c79e47 + Tree-coverage 7be675526f; round 3 was ff36ce3c51 with SKIJA->M3 in drawn-plane classes; LookAndFeel.named conflict resolved for the all-widgets `final candidate` form; win32 765 sources 0 errors) | **#203 draft** (round 3 and 4 comments posted; render-modes kept in #202 because its Table/List hooks conflict; it re-applies them by recipe) |
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

WIP heads after the session restarts (agents stopped; their uncommitted work was committed as WIP and pushed):
Swing 87e7de2600, then 7a0e46da08; Grid 6fc11f3505, then d2368e54c6; render-modes 7c2d3d84b0, then 8acb50a229; List afa5384874; attribution 1a7fd8c37f.
The Tree branch head is cee94f8d6e (brief: no-trailer rule).
New at ~12:10: `m3/tree-viewport-coverage-20261017`, worktree `C:/m3-superset/swt-tree-cov`, owned by the tree-coverage agent (tests only); build output `C:/m3-superset/tvc`.

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
- **FLAWLESS MILESTONE 2026-10-18 (tv20261018a, head 5c9f8b2121):** zero mismatches against official 3.135.0 on ALL THREE variants: plain, check+columns, presentation; 12 seeds × 150 steps with per-step pixel CRC. Acceptance 8/8.
  - Fixes:
    - a key whose target is the caret row is a no-op;
    - shrinking past the caret keeps the selection per ShrinkSelectionOracle (7 cases);
    - caret-anchor bounds via `translateAnchor`.
  - The seed-10 "flake" was a REAL bug: rows retired without a caret kept stale native handles, and the TreeView reuses handles, so a check, gray or clear wrote another row's box.
    - Fix: always drop the handle when a row leaves the window, and check the handle in the `clear()` and `updateNativeVirtualCheckState()` recipe hooks.
    - Regression test: `Test_org_eclipse_swt_widgets_Tree_ViewportHandles`.
  - One residual no seed reaches: selectAll, deselect the next caret row, shrink. It gets oracle cases next.
- **2026-10-19:**
  - **Residual stock rule:** derived from 60 oracle cases, mirrored in 81b33c37a2 (UNTESTED: native OOM, so held out of integration).
  - **CONFIRMED DEFECT** (VkSubtractCollapseOracle, coverage PR #228, 1f20e174ad): a native key collapse (VK_SUBTRACT or VK_LEFT) of an expanded caret row.
    - Stock: Collapse only; getExpanded false; bar max 339 -> 299; the top moves to the row when it was below it.
    - Viewport: the row stays expanded because the EXPANDED lane misses the native collapse, and with the top inside the subtree it also sends a spurious Expand.
  - Fix plus a regression test go to tree-presentation. The differential vocabulary gains native expand/collapse keys so this class is covered.
- **Next:** custom draw as a 4th differential variant with a new stock recording, then font/item height, then mouse.
- (Earlier: presentation variant had 66 kinds of difference, mostly cascades.)
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
- **Coverage baseline** (JaCoCo 0.8.15, jar tv20261017j, `K:/m3-work/logs/tree_coverage.sh`, report `K:/m3-work/runs/coverage/tree/`): TOTAL line 85.1% (1796/2111), branch 69.4% (1075/1550).

  | Class | Line | Branch |
  |---|---|---|
  | TreeViewport | 86.3% | 69.6% |
  | VirtualTreeViewport | 85.2% | 64.7% |
  | VirtualTreeResidentPlan | 84.0% | 69.3% |
  | VirtualTreeTopology | 91.6% | 83.5% |
  | VirtualTreeVisibleProjection | 93.3% | 83.7% |
  | VirtualTreeVisibleProjection.Residency | 61.2% | 29.3% |
  | WidgetMode | 80.0% | 30.0% |
  | SwtLog | 51.6% | 16.7% |

  Target: 99/99. The tree-coverage agent works on this; unreachable code goes to `docs/m3/tree-viewport/COVERAGE_GAPS.tsv` for the owner.
- **Possible race:**
  - Under JaCoCo during the memory storm, check+columns seed 10's final pixels showed row "root 208" with its box checked; stock has it unchecked.
  - A rerun without JaCoCo on the same jar passed (`K:/m3-work/runs/tree-evidence-k`).
  - Suspect: a stale TVIS_STATEIMAGEMASK on a reused resident row. The tree-presentation agent checks it after presentation.
- Agents: tree-presentation owns TreeViewport/VirtualTree* bodies and the recipe generator; tree-coverage owns tests only.

### Table / List / Combo / all widgets
- **Table 2026-10-18 (55dd5b6c69, build tt20261018p3):**
  - TableViewport coverage: 100% line / 99.8% branch. The one stock `LVM_DELETEITEM` arm is documented.
  - Suites: 258 tests in viewport mode, 223 in original, 0 failures.
  - Differential: 88/88 seeds against official, accepting exactly the 155 listed steps.
  - T-Table-12 closed: the plain page-scroll cost is the owner-data paint protocol, and the viewport is still no slower than stock.
  - Next: TableStockItems coverage, then the NAME CONVERGENCE to Tree's names by an OpenRewrite rename recipe (ListViewport shares 24 names with Tree; TableViewport shares only 4).
- **List 2026-10-18:**
  - Scroll speed: scroll10k 2.95 -> 1.71 ms, scrollPage 2.30 -> 1.15 ms.
  - **Differential DONE (4470b70850):** List 3/3, Combo read-only + editable 2/2.
    - Captures are cue-neutral (557c48ada2, user-approved "Approve + verify").
    - A WH_CALLWNDPROC cue-message trace shows identical traffic on both jars. **The viewport sends no cue message, so there is no defect.** Evidence is in docs/m3/list-viewport/evidence/cues/.
  - Open:
    - Re-record the resize/zoom differentials: the system highlight colour changed (0,120,215 -> 0,120,212), so the 10-17 recordings are stale.
    - list_coverage.sh: stopped at C: = 248 MB.
- Mirror the Tree rules (same family) and check each against stock with the oracles:
  - synchronous repaint on setSelection, keys and wheel;
  - no keyboard-cue changes;
  - emptiness event and image-list release;
  - off-window geometry through a probe row with stale-handle validation.
- Perf candidates from the five-mode benchmark: Table scrollPage 13.8 vs 9.0 ms and selectAll 6.1 vs 2.2 ms; List scroll10k 10.0 vs 2.3 ms and scrollPage 4.2 vs 2.3 ms.
- All widgets next: L7 decision (a Nebula Table-like widget), LF1/LF2 (look-and-feel seam and defaults).
  - Done there: CTabFolder, ExpandBar, CoolBar; Text and ToolBar measured with no change needed.

- **ON HOLD by the user (2026-10-17):** the all-widgets agent's SWT bug harvest (its script and `swt-allwidgets-BUGS.tsv`) and its K: build-output cleanup. The user rejected an edit to the harvest script; resume either only on their explicit go. Approved: the M3 rename in LookAndFeel, the theme seam, coverage.

### Drawn modes (render-modes, #202)
- Stage 1: the native control stays as an invisible behaviour plane; the drawn plane paints the resident window.
- Tree custom and m3 pass the non-pixel probes of both Tree variants.
- Next:
  - R5: gpu in Java only (brief rule 13). The planned mingw Direct2D JNI shim is dropped.
    - Use the M3 renderer's GPU contexts (Direct3D/GL, through the third-party binding behind the M3 adapter) or Direct2D reached through SWT's COM layer / JDK FFM.
    - Dawn/WebGPU is a pattern donor only.
  - R7: optimizations.
  - Then Table and List.
  - Then themes (native, swing, initiative31); look and feel is secondary.

### Nebula Grid (nebula#235)
- Done: G2 O(log n) row tree; legacy 457/457; D1-D21; header overflow.
- **G3 DONE 2026-10-17 (head fca53507):** GridSelectionList (O(1) row selection, stock order) + GridCellSelectionList (per-cell counts, column-shift reindex); selectAllCells 20k x 4: 3.5 s -> 0.22 s; one-of-200k toggle 1.48 -> 0.05 ms. Native focus-rectangle rule by recipe (pass 2 UNCHANGED); the flaky tree scene is deterministic via explicit WM_CHANGEUISTATE cues. Suite 610/610 both modes; stock differential 8/8 seeds strict pixels.
- **Bug harvest DONE (rule: every Nebula bug gets fixed):** `docs/m3/grid-viewport/NEBULA_BUGS.tsv` (open GitHub tracker + all 46 non-FIXED archived Bugzilla Grid reports, triaged). P1 shipped: BZ455474 fixed default-mode (D23 disposed colours/fonts), GH700 kept, BZ218708 not reproducible, BZ454575 verified fixed.
- **2026-10-18:**
  - **Coverage:** JaCoCo baseline cb1cdd80, line 88.3% / branch 77.1% over 614 tests and 38 classes. Gaps: GridRowSpanIndex, GridModelJobs, GridNativeCellRenderer, the selection lists.
  - **Bugs:** P2 bugs 377617, 259087, 382376, 238601, 202517 and 330224 fixed (D24-D29, each with a stock-behaviour test). 360798, 273923 and 385351 verified already fixed. P1+P2 closed; P3 next.
  - **G4 approved (lead, under "enrich the API"):** first a SetData-once differential plus memory measurement on the existing API. Then an additive `GridItem.setItemCount` that mirrors SWT TreeItem's virtual contract:
    - viewport mode: sparse child facades;
    - original mode: eager children, with stock behaviour unchanged.
- Next: G5 variable-height/tree blit scroll (exact row-tree pixel deltas), G6 column metrics, GridItem DOOP lanes, coverage to 99%.

### M3JDK (M3jdk21#1001): the user asks about this repeatedly

**Leaf 1 DONE 2026-10-17 (head a00549b373b, PR #1001 draft):** DOOP `sun.swing.tree.M3VariableHeightLayoutCache` — SoA int lanes + packed flags, per-node Fenwick trees for rows/heights, no stored y origins, TreePath/Rectangle only on API demand; opt-in `-Dm3.swing.viewport=true`, BasicTreeUI hook by recipe (pass 2 UNCHANGED). Proof: layout differential 500 seeds x 400 steps and JTree pixel differential 120 x 250 (3,120 captures equal) on stock vs --patch-module; JaCoCo 100.00% line / 99.71% branch. At 10^6 nodes: getRowForPath 174-214 us -> 0.95-2.9 us; collapse+expand 433 ms/44.4 s -> 3.5-23 us; heap 125-249 MB -> 21-53 MB (6-40,000x). Upstream defects: D1 stale y origins (fixed by construction), D2 DefaultTreeSelectionModel AIOOBE (open). Bug harvest: 2,653 open JBS client-libs bugs in `K:/m3-work/runs/bugs/jdk-swing-BUGS.tsv`; first fix JDK-8299722 (DefaultRowSorter AIOOBE) shipped with a failing-on-stock test. OpenJDK make/jtreg PENDING_CI.
**PR #1001 MERGED by the user. Leaf 2 DONE 2026-10-18 (head 82a71ecbb51, PR #1394):** DOOP `M3FixedHeightLayoutCache` for large-model JTree.
- Differential: fixed mode 20x200, plus 60x300 in JUnit.
- JTree pixel differential: fixed 60x250 (1,560 equal captures) and variable 30x250.
- Coverage: 100% line / 99.7% branch.
- Both BasicTreeUI hooks applied by recipe; pass 2 UNCHANGED.
- D4: stock's parent-row answer for a vanished child is mirrored on purpose; returning -1 would expose D2.
- DESIGN 7a documents the cache seams for viewport-everywhere.
**Leaf 3 DONE 2026-10-19 (PR #1404, stacked on #1394):**
- JList row offsets via the `M3SwingViewportBasicListUI` recipe hook. `M3InsertStatements` now handles brace-less else-if/case, as committed in Synexia 8c122afccd25.
- 40/40 seeds x 300 steps, with 1,240 equal pixel captures; a planted off-by-one was caught.
- Coverage 100/100.
- At 10^6 rows: locationToIndex 360 µs -> 2.5 µs; frame p50 0.6-0.9 ms -> 0.37 ms.
**Recipe crate replay:** com.synexia #11799 was an ancestry-only merge (cae38e5b47, 0 files). The crate was replayed onto develop as **draft PR #12401** (35 files, clean add).
**Next leaves:** lazy JList row measurement (relayout is still ~470 ms), JTree expansion-table index, D2 fix, then bugs in TSV order (next JDK-8366188).
- Goal: DOOP (data-oriented OOP, brief rule 18) plus the viewport mediation for Swing (JTree, JList, JTable), then SwingX and JavaFX ("DOOP Swing/SwingX/JFX").
  - Node and row state goes into lanes; TreeNode/cell objects become lazy facades.
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
- **DONE 2026-10-18:** ledger-driven recipe crate `m3-synexia-attribution` (M3SynexiaAttribution, M3SynexiaTextAttribution, M3NamingJava + m3-naming.sed; 33/33 JUnit) = synexia **#12227 MERGED**; SWT `m3/synexia-attribution-20261017` = **#204 MERGED** (440 files REHEAD/INSERT, M3 naming of gpu/render classes, WidgetMode.M3, gpu.m3 bundle), follow-up on integration = **draft #226** (e13a1aefe7); I31 naming **#212 MERGED**; Nebula **#238 MERGED**. Classification: SYNEXIA_AUTHORED 364, UPSTREAM_MODIFIED 1,324 (162 contributor lines), DONOR_DERIVED 9 (IBM StylePolicy/StyleBits, Chromium OcclusionTracker). Atomize run `docs/M3-SCALE/runs/20261017-swt-viewport-atomize`: 7 donors, 642 files, 26,472 leaves; PATTERN_REUSE.md confirms "Table close to Tree, List close to Table" (pair overlap Table 533 > List 437 > Grid 303); 1,584 cross-donor mediator classes flagged DOOP_LANE/OBJECT_GRAPH/MIXED. JVM atom-mastery lane parked (LANES.tsv, needs disk/memory).
- Standing job: re-apply after each integration round; keep #226 current.
- Earlier: classification ledger in `docs/m3/attribution/` (WIP commit).
  - SYNEXIA_AUTHORED 364, UPSTREAM_MODIFIED 1,324 (171 with real fork content get a contributor line; 1,123 are formatting only and get none), DONOR_DERIVED 10.
  - The 98 `.m3` evidence files are not edited.
- Next:
  1. apply the recipe to a branch from integration, second pass UNCHANGED, compile, then open a draft PR;
  2. Nebula the same way;
  3. atomize/patternize the corpus into `docs/M3-SCALE/runs/20261017-swt-viewport-atomize/`, with `PATTERN_REUSE.md` (patterns shared across the Tree/Table/List/Grid mediators).

### Bug programme: every SWT bug, every Swing bug (new track, not started)

1. **Harvest (CPU, read-only):**
   - eclipse-platform/eclipse.platform.swt GitHub issues (open, bug labels), plus legacy SWT Bugzilla where linked;
   - eclipse/nebula issues;
   - for Swing, OpenJDK JBS client-libs (javax.swing / java.awt) bugs that still reproduce on JDK 21.
   - Store them as `K:/m3-work/runs/bugs/<repo>-BUGS.tsv` (id, title, widget, platform, priority, state, link).
2. **Triage** by priority (the user's order), then widget, then platform (win32 first).
   - Map each bug to the owning track: Tree / Table / List / all widgets / Grid / M3JDK.
3. **Per bug:**
   - write a failing test that reproduces it on the official jar (or JDK 21);
   - fix it in the default mode (rule 12: the original mode stays stock);
   - add a row to `UPSTREAM_DEFECTS.tsv`;
   - keep the differential green;
   - one PR per batch.
4. **Swing bugs** are fixed in M3jdk21 `src/java.desktop` (GPLv2+CPE), with tests run through `--patch-module`.

### Donor atomize (rule 19): DONE 2026-10-18
- com.synexia **draft PR #12398** (`codex/m3-ui-donor-atomize-20261017`, 061542b4).
- Coverage:
  - 69 donors, 6,934 Java files, 159,232 leaves;
  - 85,140 exact classes (11,381 shared across donors), 46,722 structural (8,115 shared);
  - IntelliJ UI 459 files, RAP RWT 192, t-rex 452 leaves;
  - skiko and regex-cursor as blob hashes only.
- `PATTERN_MAP.md` findings:
  - prefix-sum row math is the most lane-shaped theme (24/51);
  - **damage/blit has no lane-shaped donor (0/46)**: our resident-window blit math is first-of-its-kind;
  - expansion maps are object graphs (2/62): VirtualTreeTopology is the lane-shaped extraction target.
- Licence flag: t-rex has no LICENSE file (README says Apache-2.0), so it is LICENSE_REVIEW_REQUIRED.

### Donor wave 2 (user 2026-10-19: "Skija, IntelliJ as donor; you can add more")
- Queued on donor-atomize, in this order:
  1. **JetBrains/skija** Java API: Canvas, Paint, Paragraph/TextLine, Surface/DirectContext. Feeds the M3 row painter and M3TextSegments.
  2. **JetBrainsRuntime** java.desktop Swing/AWT/Java2D, as a diff against OpenJDK 21. It is GPLv2+CPE, the same licence as M3jdk21, so it is a TARGET_OWNED_PORT-compatible source for Swing bug fixes.
  3. More **intellij-community** UI: openapi.ui, treetable, table, render, hover, scale, tree, laf, speed search.
  4. **HumbleUI/JWM**.
  5. HumbleUI.
  6. jewel and compose-multiplatform, as hashes plus lazy-list paths.
  7. Added on the user's repeat:
     - the **IntelliJ editor viewport**: EditorView/EditorPainter/VisualLinesIterator, soft wraps, folding, inlays, scrolling. The prime StyledText-viewport donor.
     - **jediterm**: Swing terminal text viewport over a line ring buffer; dual LGPL/Apache, Apache elected.
     - **compose-multiplatform-core** LazyList measure/prefetch.
     - **skiko** frame pacing, for gpu mode.
- Scratch: S:\m3-scratch\donor-atomize.

### Donor catalogue
- `K:/m3-work/donors/catalogue/DONOR_CATALOGUE.tsv` (179 rows) + `TRICKS.md` (30 techniques) + `HF_LANGUAGE.tsv` (46) + `LANGUAGE_SUPPORT.md`. Done by the donor-survey agent.
- Session-log links: `K:/m3-work/donors/catalogue/links/` (USER_LINKS.md with 180 repos/sites typed by the user, CHAT_DIRECTIVES.tsv with 224 rows, the CODEX/CLAUDE URL lists).
  - Kept local on purpose: they contain personal links.
- **Commit history and PR bodies: DONE 2026-10-17 ~13:00** ("all donor links are in the commit history and the session logs", user). Never re-mine; the outputs are:
  - `links/COMMIT_URLS.tsv`: 14,013 rows (2,083 commit msgs, 6,839 HEAD docs, 4,150 PR bodies, 941 PR comments), deduped by SHA across the shared object stores; com.synexia PRs read via gh GraphQL (11,991 PRs).
  - `links/DONOR_LINKS_ALL.tsv`: 1,479 unique slugs/sites (645 GitHub), categorized.
  - `links/VIEWPORT_DONOR_DELTA.tsv`: 14 verified new donors with licence/stars/archived/head_sha; appended to `DONOR_CATALOGUE.tsv` (now 194 lines, .bak kept, source=commit-history). Notables: swt-initiative31 skija-canvas + prototype-skiavcl (EPL-2.0), eclipse.jdt.ui, harfbuzz, ICU + CLDR (Unicode-3.0), webfx (Apache-2.0), chromium (BSD-3; pattern only).
  - All local on K: (personal links never committed). The donor-atomize agent consumes the delta.

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
| a6ca57b181ca089ab | donor survey (completed) |
| af3db5218820e576c | donor-atomize (GitHub UI donor corpus -> Synexia atoms/patterns, rule 19) |
| a56862a3f7f3ea02d | tree-presentation (presentation variant; then the seed-10 race; then the aspect gaps) |
| a708603ec47eadfeb | m3-regex-cursor: **delivered 2026-10-18.** M3jdk21 draft PRs **#1384** (cursor regex; module m3/ports/regex-cursor) and **#1402** (M3TokenCursor/M3TokenPattern, stacked on #1384). 120k random + 640k mastery + 46k token cases, 0 mismatches; line coverage 99.9%. Next: O(1) cursor step, DFA cache reset, \b/MULTILINE/CASE_INSENSITIVE, java.base recipe + M3JdkHandoff. — brief: (user 2026-10-18: helix-editor/regex-cursor "for M3 structures" + token-aware regex; M3ChunkCursor over M3String leaves, literal prefilter, cursor lazy DFA, M3TokenCursor over interned ids; differential vs java.util.regex; M3jdk21 branch m3/regex-cursor-20261018) |
| aeba9473d82661f83 | viewport-everywhere (toolkit-neutral M3JDK m3/viewport-core by recipe from SWT; adapters AWT -> SwingX -> IntelliJ -> RWT; M3 renderer as the cross-library row painter) |
| a4a54cbc5baf005fc | collections-superset (user 2026-10-18: "implement all Guava, Apache, Eclipse, Spring and JDK collections, superset them, using arrays and masks, no Entry objects"; M3jdk21 m3/collections, branch m3/collections-superset-20261018, worktree K:/m3-work/agents/collections-superset/wt; FOSS gate first, SUPERSET_MAP.tsv, wave 1 M3SupersetHashMap/Multimap/Multiset/BiMap/MultiValueMap with donor oracles; Eclipse Collections pattern-only) |
| a13c57aeb02e89e47 | api-enrichment (2026-10-18: ENHANCED_API_MAP 46 rows with per-toolkit applicability; shipped M3ItemRange/M3ItemSelection/M3Mode, M3ScrollInfo/M3Scrolling, M3TextSegments; 44/44 tests both modes; 100% line / 93.5% branch, the gap being always-throwing SWT.error checks, accepted; draft PR #227; next M3-TOKEN-CURSOR, M3-EMPTINESS, M3-ROW-MATH) — earlier brief: api-enrichment (user 2026-10-17 "enrich the API", "expand and include the shared links": donor capabilities -> additive M3 API; branch m3/enhanced-api-20261017, worktree C:/m3-superset/swt-api; map ENHANCED_API_MAP.tsv, first leaves M3ItemRange selection, M3ScrollInfo smooth scroll, M3TextSegments language seed) |
| af4655bf4cce9b7fd | tree-coverage (99% line/branch, tests only, `C:/m3-superset/swt-tree-cov`) |
| a17996dd318549981 | link-miner (commit history and PR bodies -> donor links; local outputs only) |

All of these were (re)started at ~12:10 on 2026-10-17 with the no-trailer rule and rules 13-20.

**2026-10-18 state.** Session restarts stop every agent; each restart was followed by a checkpoint-commit sweep and a resume message. At ~10:00 IST the account's session limit (HTTP 429, resets 14:00 IST) cut off tree-presentation, tree-coverage, List, Grid and render-modes mid-leaf; their work is saved in their transcripts and worktrees. Resume them with SendMessage after the reset; do not re-spawn. User priorities that day: "the UI viewport is very very important"; "scaffold all the collections first, then commit and push"; commit, push and consolidate after every report. all-widgets finished LookAndFeel coverage (100% lines, 99.5% branches, 11/11; two unreachable spots documented in docs/m3/look-and-feel/coverage/COVERAGE.md). Collections on M3jdk21: another worker owns #1239 (M3EvictingQueue), #1233, #1226, #1241 and Spring LinkedCaseInsensitiveMap; the collections-superset agent builds the five-provider INVENTORY + template scaffolds in #1239's layout and fills the OPEN types one PR each. Disk: a 77 GB system pagefile (from the git-status storms) ate C:; K: is the user's personal 488 GB disk at ~2 GB free; agents run lean and park under 500 MB; only the user decides what else goes.

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
