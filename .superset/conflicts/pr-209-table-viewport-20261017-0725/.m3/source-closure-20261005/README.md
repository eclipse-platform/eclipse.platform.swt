# Complete SWT source closure and bounded GTK admission

Base: `97be47c95bc6ee075b3992bb8f0fa2d2e0cbbe46` (merged PR #80).
Parser: `com.synexia.recipe:openrewrite-line-terminators:8.90.4-m3-javadoc-e898332e8f88`.

All **2,473 tracked Java files** have complete statement/block/loop composition packets:
**372,780 non-root atoms**, zero missing/stale sources, zero parse errors, exact source
recomposition, fresh reparse and recipe fixed point. This includes donor/reference and
negative fixture Java files. `all-files.tsv` exactly matches the tracked Java path set.

`statement-atoms.tsv.gz` stores the existing canonical packet fields. `RestoreAtoms`
reconstructs all original JSON packets from the current sealed sources and this index,
checking packet hashes and byte-exact source recomposition. `patterns.tsv` counts the
existing pattern categories, including root-file atoms. `COMPOSED-STAGES.json` records
the initial full replay, the four files refreshed after PR #80, and the recipe postimages.

`native-and-images.tsv` seals all 124 C-family source files and 615 images, including
416 PNGs, against base Git bytes. They are unchanged. The existing native receipt CLI
reconstructs 60,054 lexical/structural native atoms. PNGs remain binary assets; native
packets do not assert C/C++ preprocessor-aware semantic refactoring.

## Runtime repair through existing recipes

The new logical GTK model had a missing count-snapshot publication, eager materialization
of a cold top row, and a broken clear path. The paired Maven/OpenRewrite task crate retains
all compiler/test failures and fixes them through six hash-pinned snapshot trials. Every
trial proves fixed point and source-drift refusal. The sequence reproduces the two changed
Java files exactly. A missing display binding in the new regression test is also repaired.

The logical native model still grows GtkTreeView memory with logical row count: in separate
process probes, 64 -> 1,000,000 hidden roots added 124 KiB RSS with the bounded frontier
versus 62,552 KiB with the logical model. These are diagnostic samples, not universal
allocation/performance guarantees. The proven bounded frontier is therefore the default.
The logical model and repairs remain available through explicit
`org.eclipse.swt.internal.experimentalLogicalTreeModel` admission, latched per widget.
Its existing test opts in only during construction and immediately restores the property.
Every original assertion remains intact; all default frontier tests are unchanged.

Public/protected Tree signatures and all native sources/JNI entry points remain unchanged.
GTK3 libraries were rebuilt under the repository warnings-as-errors gate and explicitly
selected during tests. No built binary is included. `verification.json` records the final
reactor gate; `gtk-jni-build.json` seals the local native artifacts used by it.

## Reproduce

Install the paired Nebula source-owned parser with Java 21, then build the canonical
`synexia-openrewrite-recipes/recipe-crates/ui-source-closure-20261005` Maven crate and its
runtime dependency classpath. Use its `run.py` to regenerate the entire source universe,
and `RestoreAtoms` to independently check this packed index. Use `replay.py --repository swt`
on a clean pinned SWT base to reproduce the two Java postimages into an external directory.

Build and test SWT with GTK development dependencies and a working display:
`GTK_VERSION=3.0 mvn -Pbuild-individual-bundles -Dnative=gtk.linux.x86_64 clean verify`.
Set `-Dswt.library.path` to the rebuilt GTK binary folder for forked test JVMs so a stale
extracted library cannot hide or fabricate results. On headless Linux use Xvfb in the same
execution environment as Maven. Keep ordinary tests enabled.

Coverage is source-bound. Windows/macOS native execution, strict canonical promotion,
universal equivalence and a cold dependency mirror remain separate qualifications.
