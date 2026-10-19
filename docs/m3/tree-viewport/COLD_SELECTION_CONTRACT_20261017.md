# Win32 Tree: cold logical selection is not the native resident selection

## Exact inspected current master

- `Tree.java` blob **6a223529c297cd0828325f0512290b86033f2302**, method `selectAll()` calls native `select(hItem,tvItem)` without a viewport guard.
- `TreeViewport.java` blob **6a4963d3d54a412fcbf444baddb0989d822fa924**, 1,317 lines. Its `getSelectionCount()` traverses materialized topology only; its existing methods do **not** contain `selectAll()`, `patternLimits` or `anyChildrenSelected`.
- [SWT #201](https://github.com/hsoliwal/eclipse.platform.swt/pull/201), [#203](https://github.com/hsoliwal/eclipse.platform.swt/pull/203) and the preserved Tree owner [#213](https://github.com/hsoliwal/eclipse.platform.swt/pull/213) contain a larger `TreeViewport` with a bounded select-all pattern. That historical source is **not** safe to replay wholesale over today's master.

## Contract and failure mechanism

A mediated SWT.VIRTUAL Tree exposes many logical rows while native TreeView holds only the visible window, overscan and ancestors. `Tree.selectAll()` selects only native residents in current master; `TreeViewport.getSelectionCount()` visits materialized rows. Therefore when a 10,000-row logical forest is only partly realized, selection of the entire logical model can be lost. Explicit user-visible behaviors requiring parity: logical selection count, off-window row selection, deselectAll, shrinking and re-extending child counts, selectAll in SWT.SINGLE, SetData materialization budget, and absence of spurious Selection events.

## New product oracle

`Test_org_eclipse_swt_widgets_Tree_ViewportSelection` is a new, unmodified-source, win32-only JUnit Jupiter class. It measures selectAll at 10,000 cold roots, distant scroll, shrink + append, SINGLE no-op, SetData budget and selection-event silence. It is expected RED on the above master.

## Source-first promotion plan

1. Treat preserved `TreeViewport.java` pattern-based implementation as **donor candidate** with original EPL-2.0 and source provenance; compare current/owner semantic atoms in order (not whole-file replacement).
2. Only the canonical Synexia `m3-viewport-catalogue-recipes` Maven/OpenRewrite owner may produce code changes. Restore missing helpers/selection masks via bounded, source-hash-pinned recipes with closure and fixed-point proof. No parallel rewrite engine; no manual changes to `Tree.java` or `TreeViewport.java`.
3. Run original and candidate win32 SWT JUnit, Tree acceptance 8/8, recorded seeded differential 12x150, seven-case ShrinkSelectionOracle and screenshots/GC/resource checks. Assert the native residents remain bounded and no new distant tree facades are required to count selection.
4. Keep `-Dswt.widget.mode.Tree=original` as stock behavior. Do not infer GTK/Cocoa or GPU parity without native execution.

**Status: DRAFT / EXPECTED RED.** No native build or JUnit test was run in this authoring step. This PR adds only the oracle and the gate specification.
