# SWT M3 distillation and absorption pipeline

SWT is already a Maven/Tycho reactor. Do not replace that build. The M3 lane is an opt-in
analysis/promotion layer on top of the ordinary SWT build.

## Stage 0 — preserve the ordinary SWT build

The standard Tycho build, API checks, JNI generation, platform tests and examples remain the
behavioral authority. The M3 profiles do not run unless explicitly selected.

## Stage 1 — structural and semantic distillation

Install the Synexia recipe pack, then run the existing SWT distillation inventory:

```bash
mvn -Pm3-distillation rewrite:dryRunNoFork
```

The recipe exports structural/logic/similarity atoms and an attributed semantic graph. It is
read-only.

## Stage 2 — atomize and patternize the live Maven/Tycho source model

Run:

```bash
mvn -Pm3-atomize-patternize rewrite:dryRunNoFork
```

The active `M3SwtAtomizePatternizeRecipe` composes canonical Synexia owners for:

- SWT structural/logic atom inventory;
- attributed semantic relationships;
- Java declaration atoms: classes, methods, fields, initializers, lambdas, anonymous classes
  and enum values;
- public/protected/internal/JNI contract-surface classification;
- framework/OpenRewrite pattern review;
- pinned mechanical donor-shape evidence.

This stage is also read-only. It has no source-copy, mutation or promotion authority.

## Stage 3 — normalize donor projects separately

Donors are not pasted into SWT.

Raw donor projects are first Mavenized through the Synexia donor Mavenizer where needed, then
inventoried using `M3DonorMavenizedAtomPatternRecipe`.

Current donor/evidence families include:

- SWT snippets/examples and large manual tests;
- Java2s SWT, SWT 2D Graphics, Swing and Swing Event catalogues;
- Eclipse Nebula/Grid/CompositeTable/XViewer mechanics;
- the supplied deferred/lazy viewer and viewport experiments;
- Virtual TreeView;
- viewport/rendering libraries used as algorithmic or architectural evidence.

The donor catalogue grants no source-copy or replacement authority. It exists to make equivalent
behavioral shapes mechanically comparable after both target and donor are normalized.

## Stage 4 — bounded absorption

Select one bounded cohort from the target/donor inventories. Examples:

- viewport window calculation;
- row-coordinate scrollbar metrics;
- header/body z-plane invalidation;
- sparse Tree topology;
- packed lifecycle/state masks;
- collapse residency;
- paint command recording;
- affine transform / retained paint DAG atoms.

Implement that cohort through a dedicated source-sealed OpenRewrite recipe crate with exact
pre/post SHA-256 custody.

Required properties:

1. exact target source identity;
2. additive behavior preservation;
3. fixed-point replay;
4. refusal on source drift;
5. smallest justified edit scope;
6. no public SWT contract change unless deliberately versioned;
7. no rebase/force-push of shared history.

## Stage 5 — compiler, runtime and visual promotion gates

A candidate is not promoted because the recipe produced source.

Run the applicable SWT gates:

- Tycho compile/build;
- JUnit;
- GTK/Cocoa/Win32 native tests;
- API/Javadoc consistency;
- JNI generator consistency;
- relevant SWT snippets/manual cases;
- large virtual Table/Tree cases.

### Screenshot gate

`org.eclipse.swt.tests.manual.ViewportRewriteStress` is the retained visual proof harness.

Configure:

```text
-Dswt.viewport.screenshots=<output-directory>
-Dswt.viewport.screenshots.exit=true
-Dswt.viewport.screenshots.native=true
```

The suite captures deterministic behavioral states including:

- virtual Table top / middle / end / checked-selection;
- lazy Tree expansion/collapse states;
- logical viewport top / middle / horizontal offset;
- narrow viewport with both scrollbars;
- wide viewport;
- vertical-only scrolling;
- tiny logical model with no scrollbars.

The ordinary PNG path uses SWT control painting. The optional native capture adds OS/native chrome
when the platform permits desktop capture. The accompanying widget-spy snapshots record retained
TreeItem/TableItem residency.

Screenshot evidence is a regression aid, not a replacement for event/API/native tests.

## Stage 6 — promote only the verified superset

Only a candidate that passes the source-sealed recipe, compile/test/native/API/JNI and relevant
visual gates is eligible to merge.

If an application depends on implementation-specific legacy behavior that cannot yet be preserved,
keep the optimized forward implementation intact and use the maintained `swt-classic` branch as
the compatibility escape hatch.
