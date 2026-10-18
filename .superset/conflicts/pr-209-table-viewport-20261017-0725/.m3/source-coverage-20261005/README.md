# Current SWT source coverage

Source baseline: master `16ba3b1dd0343624ff271647477982f8e3c9835d`, including
PR #74 and PR #75 Java-owned virtual Tree presentation. The new PR #75 test was
repaired through `swt-pr75-test-repair` after the real compiler rejected its
undefined Display variable and inaccessible TreeItem.clear call. Canonical recipe owner:
[Synexia PR #9153](https://github.com/hsoliwal/com.synexia/pull/9153),
`native-packets-20261005` Maven crate.

| Delivered source set | Files | Atoms | Verification |
| --- | ---: | ---: | --- |
| Java, including tracked M3 tooling | 2,471 | 371,843 behavioral atoms plus 2,471 compilation roots | Every original canonical packet restored exactly |
| C / headers / C++ / Objective-C++ | 124 | 60,054 lexical atoms | Every byte restored independently from Base64 payloads |

`java-files.tsv` is the current complete Java manifest. `statement-atoms.tsv.gz`
reuses exact source payloads through sealed ranges; the canonical `RestoreAtoms`
runner reconstructs each original JSON packet and verifies its SHA-256. Fourteen
changed/new files were freshly processed through OpenRewrite; their payload
packets are in `java-atoms.json.gz` and their receipts in
`java-supplement-files.tsv`. Earlier packet receipts remain intact as history.

Native payload packets and their pattern catalogue live in
`../native-source-distillation-20261005/`. UTF-8 and UTF-16 BOMs, original offsets,
raw strings, continued comments, directives and line endings are retained. Kind
and nesting depth are lexical classifications, not native type or macro semantics.

`coverage.json` verifies exact tracked Java/native file coverage with no missing
source. It also verifies all 412 original PNG hashes and records the earlier two
GTK stress screenshots and two new PR #75 validation screenshots separately. The earlier corrupt/truncated image fixtures
remain byte exact.

The canonical proof run passed 15 tests, 32 C/C++ compile/run pairs, seven sealed
Java recipe targets and seven source-drift refusals. The full native OpenRewrite
pass generated 127 exact receipt/packet outputs from 124 native sources and reached
a fixed point. The SWT workflow/documentation recipe has two sealed targets and
two drift refusals. Local execution of the new CI compile/check commands passed.

The integrated 22-module GTK reactor passed **4,494 tests, zero failures/errors,
and 20 existing skips**. The repaired Tree suite separately passed all 161 tests. Eleven focused viewport/
screenshot tests passed, and all twenty stress scenes completed in 19.727 seconds
in one run (not a statistical speedup claim). Two representative screenshots are
included; all scene sidecars and artifact hashes are retained.
`native-integrated-proof.json` binds the selected receipts to the full execution
log hash and the exact Java/native source-set hash. This delivery preserves the
PR #75 widget implementation and repairs its regression test through public SWT
APIs. Generated JNI files and native binaries are not modified. GitHub-hosted
checks failed before starting any jobs; local execution is the available native
proof. Windows/macOS native execution, canonical full-reactor/strict promotion
and repository-owned offline dependency closure remain unverified.

## Remaining measured runtime criterion

Bounded GTK Tree native residency is **not satisfied**. The live store still
requires a native prefix for distant items. A separate fresh-JVM probe shows
that attaching even the dormant sparse logical model to GtkTreeView with 500,000
roots increases process RSS by 30,760 KiB (single sample; not a benchmark claim).
The GTK view itself retains linear row storage. Activating that model alone cannot
meet the bounded-residency invariant. The probe and exact results are retained in
the canonical crate; no unqualified backend replacement is activated here.
