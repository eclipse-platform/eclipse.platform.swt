# Executed SWT Java statement distillation

Base: `b3ae2d485ed706d0c63c3f21b35fcbf40f97b2e4`.
Recipe implementation and proof crate: https://github.com/hsoliwal/com.synexia/pull/8957

All 2,462 application Java files were processed serially through the existing block-normalization
and statement-composition recipes. This produces 371,035 executable atoms and 1,304 actual Java
postimages. All three tracked M3 tooling Java files add 77 atoms without source changes.

Every source passed exact reconstruction, reparse and fixed-point replay. Independent javac body
equivalence and atom census cover all 2,462 application files. The 22-module compile passed.
Before the later inherited viewport/child-count repairs, full GTK/Win32/Cocoa comparisons
preserved executable classfile content after removing debug metadata: 708 / 1,029 / 836 classes.
Those receipts describe that earlier pin; the final source set is bound by the new native receipt
and the independent javac equivalence/census checks on the inherited changes.

`statement-atoms.tsv.gz` stores every atom, its parent, kind, pattern and UTF-16 range. Literal
payload is shared with the exact delivered source. `all-files.tsv` seals the before/after source
and original canonical JSON packet for every file. `RestoreAtoms` in the linked canonical recipe
crate reconstructs all 2,465 original packets byte for byte; the restoration receipt is included.
This is an executable source composition, not just an inventory count or a list of method names.

The asset catalogue retains all 412 PNGs, including deliberately invalid/truncated image fixtures.
All 170 tracked native C/header/library payloads are byte exact; built native binaries are excluded.
Existing APIs, style atoms, examples, tests, resources, build definitions and workflows are retained.

## Gate status

The 22-module GTK-enabled Maven verify reactor passed. The main SWT suite ran 4,430 tests with
zero failures/errors and 20 skips. The separate GTK test bundle retains its upstream default test
skip; Windows/Cocoa native execution, strict canonical
repository admission, full canonical reactor and repository-owned offline dependency closure are
not claimed here. The earlier strict-toolchain attempt, pinned in the canonical receipt, stopped at a missing
parenthesis in `ChallengeSupersetCatalog.java`; that historical failure is not a current-head claim.
Native payload preservation does not establish complete native semantic atomization or optimization.
These generated sources remain review candidates until the required promotion gates pass.

Candidate synthesis follows the fan-in invariant: every distinct ordered composition is retained
as a new recipe candidate with source, contract and parent lineage. Actual OpenRewrite scan/visit
phases must be verified; the serial driver deliberately completes normalization before scanning
the final source into packets.

The final NLS-aware replay preserves line-bound compiler directives on their original literals.
All 259 NLS-containing inputs were replayed; 32 delivered source files and their packets changed
relative to the first published pass. The native verify and all three classfile comparisons were
rerun on these final sources. `native-verify.json` binds the receipt to `all-files.tsv`.

The final reconciliation retains master `071aea415e654cd2d628d39f58959a3d7d1f62be`,
including the GTK pending-scroll and Win32 known-child-count repairs. The two changed upstream
files were reparsed, atomized and independently checked with javac; no new normalization was
needed. The three earlier full-fragment classfile comparisons are historical evidence before
those inherited runtime repairs, not an assertion that the repairs preserve old class bytes.
See `upstream-reconciliation.json` for the exact three-way source custody.
Canonical NLS recipe and tests: https://github.com/hsoliwal/com.synexia/pull/9023 .

Final post-reconciliation 22-module native GTK `verify`: **BUILD SUCCESS**. The main SWT suite
reports 4,430 tests, zero failures/errors and 20 skips. Including the three other test module
summaries, the reactor reports 4,490 tests, zero failures/errors and 20 skips. The separate GTK
test bundle still uses its upstream default skip. No skip or timeout policy was relaxed.
