# SWT architecture and coverage

This guide extends the repository's [contributor README](../../README.md) with source-backed package/API contracts and an explicit coverage ledger. It documents the current fork; it does not declare every package complete or convert proposed architecture into implemented behavior.

## Revision and provenance

- Fork: [hsoliwal/eclipse.platform.swt](https://github.com/hsoliwal/eclipse.platform.swt)
- Verified upstream: [eclipse-platform/eclipse.platform.swt](https://github.com/eclipse-platform/eclipse.platform.swt)
- Source inspected: `c05dff79c9cc737c9cffa501c8f5cef353141dc6`, branch `docs/fork-contributor-navigation-20261014`, on 14 October 2026
- Existing draft: [PR #164](https://github.com/hsoliwal/eclipse.platform.swt/pull/164), targeting the fork's `master`
- Base of that draft when read: `8d607fc88f6ba5b3b3bf07a6d34967c104da63e8`
- Only Markdown is changed by this extension. Existing source, API, native files, generated files, dependencies, build configuration, workflows, license and attribution are preserved
- Source evidence uses immutable repository permalinks and Git blob identities. Private task material and unrelated project details are not part of these pages

The inspected revision precedes this documentation commit. The publishing comparison must therefore show only documentation changes; source-sensitive updates require a fresh source baseline rather than silently reusing these claims.

## Navigation

1. [Lifecycle and package contracts](lifecycle-contracts.md): resource ownership, Font/Color differences, Display/widget threading, event dispatch, layout data and JNI
2. [Project and module ledger](module-ledger.md): descriptor-backed identities, dependencies, source roots, exports and build boundaries
3. [Java package/source-set ledger](package-ledger.md): every Java-containing source directory, representative package declaration, blob identity, existing docs and explicit gaps
4. [Exports without a local Java witness](export-boundaries.md): fragment, split/resource/fixture export boundaries not represented by a local Java file
5. [Supporting sources](supporting-sources.md): native/script units, root-tree ownership, model/extension and opaque artifact boundaries

## Build and test routes

The [root README](../../README.md#building) documents `mvn clean verify`; [native build guidance](../../bundles/org.eclipse.swt/Readme.md) supplies host-specific prerequisites and JNI regeneration. The inspected Actions lane selects JDK 25 and Maven 3.9.16. The SWT bundle manifest declares JavaSE-21, which is an execution-environment declaration rather than a statement that the selected CI runs on JDK 21.

The root POM selects host-specific test fragments through OS profiles. The tests parent build properties default `skipNativeTests=true`; the native CI build explicitly sets it false. Do not infer native tests ran merely from a successful Maven invocation. The optional Skija backend is profile-gated; it is not evidence of default backend replacement.

Read [the matrix workflow](../../.github/workflows/maven.yml), [build workflow](../../.github/workflows/build.yml) and [PR checks](../../.github/workflows/pr-checks.yml) for their actual scope. The native matrix ignores Markdown/docs-only changes, while the PR consistency workflow can still run. A skipped/absent native matrix is not a native test pass.

Build instructions above were read from public source. No Maven, compiler, test, native library, example, script, model generator or application was run for this documentation extension.

## Coverage and open gates

| Level | Discovered/included | Excluded | Reviewed-complete | Partial | Missing / unresolved depth |
| --- | ---: | ---: | ---: | ---: | --- |
| Repository | 1 | 0 | 0 | 1 | Full lower-level semantics and verification remain open |
| Descriptor-backed projects/build units | 34 | 0 | 0 | 34 | Complete project responsibilities/configuration/artifacts and active build membership require deeper review |
| OSGi modules/fragments | 24 | 0 | 0 | 24 | Manifest boundaries recorded; complete exported/internal behavior and test mapping remain open |
| Java source-directory package occurrences | 162 | 0 | 0 | 32 | 130 have no substantive package review in this pass |
| Export clauses without a same-owner Java witness | 177 | 0 | 0 | 177 | Resolve shared-source, resource/generated or fixture semantics using the export ledger |

Reviewed-complete coverage is **0%** at each nonempty level under the strict all-content/all-verification gate. This does not mean there is no useful documentation: the new contract guide explains selected method bodies, and 18 source occurrences link to those contracts. It means an inventory, existing package page, draft PR or unrun test is not being counted as complete documentation.

The 2692 Java-file tree entries map to 162 physical source directories. Representative declarations witness 82 distinct named namespaces and 15 unnamed-package directories. Repeated namespaces are kept separate by source path/owner/variant. Every discovered directory has a scope disposition, but every Java declaration has not yet been scanned; differently declared fixtures or mixed directories can add namespace identities. Package counts are consequently an explicit discovery baseline, not a claimed exhaustive semantic denominator.

Additional unresolved boundaries are included in [supporting sources](supporting-sources.md): opaque archives/binaries were not unpacked, generated code correspondence was not verified, and source/script/model metadata is not full behavioral documentation. These gaps prevent an overall completeness claim.

### What was inspected

- Complete Git trees, without a truncated subtree; all discovered conventional project descriptors and alternate analysis/Ant descriptors called out in the module ledger
- Bundle manifests and available build.properties, including source roots, module identities, dependency/import/export declarations and platform filters
- One complete representative Java file per source directory for namespace/type discovery; existing package.html/package-info.java locations
- Full relevant declarations and method bodies behind the selected lifecycle contracts, plus specifically linked test bodies and build/CI entry points
- Existing repository instructions and README content; external edits are preserved

### Verification receipt and limits

| Stage | Result | Scope |
| --- | --- | --- |
| Immutable source/tree and descriptor retrieval | OBSERVED | Connector reads at the source revision above |
| Documentation navigation and path/anchor checks | PASS | 1345 links checked in generated architecture pages against the fetched tree and generated headings; no external-site availability guarantee |
| Final remote Markdown-only diff/read-back | Publication receipt on the draft PR | Separate from this source snapshot; not a runtime or semantic completeness claim |
| Java compiler / Maven / Tycho / Javadoc / API analysis | NOT_RUN | Source-only connector work |
| Tests / native builds / UI / performance / memory | NOT_RUN | No runtime execution performed |
| Cross-platform equivalence or compatibility qualification | NOT_RUN | A GTK method read or one package test source cannot establish other platform outcomes |

### Next review units

1. Complete the other methods/types in graphics/widgets/events/layout and reconcile lifecycle details across GTK, Win32 and Cocoa
2. Review browser, clipboard/drag-and-drop, accessibility, AWT, OLE, OpenGL, printing, custom widget/StyledText, internal image/residency/swap and optional GPU contracts
3. Follow every native declaration and generated bridge to its ownership, pointer/array/callback constraints and host-specific verification
4. Complete tooling, examples, snippets, native fragments and tests, including disabled/profile-gated and historical benchmark paths

For every unit above, inspect complete relevant implementation and tests, document inputs/outputs/errors/state/effects/ownership/concurrency, reconcile existing inline/package docs, and record exact executed or unrun checks. Markdown-only scope leaves Java package-info/Javadoc edits and runtime checks unperformed; it does not waive their eventual completion gates.

## Project terms

Existing [LICENSE](../../LICENSE), [NOTICE](../../NOTICE) and [CONTRIBUTING.md](../../CONTRIBUTING.md) remain authoritative for this repository. No license compatibility, upstream adoption, performance, production-readiness or broad implementation-completeness claim is made by these documentation pages.
