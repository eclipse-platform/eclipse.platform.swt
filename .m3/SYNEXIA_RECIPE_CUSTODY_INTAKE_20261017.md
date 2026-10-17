<!-- SPDX-License-Identifier: EPL-2.0 -->
# Synexia-owned recipe custody — public fork receiving record (2026-10-17)

This public target **does not own generic reusable OpenRewrite recipes**. Canonical source,
templates, mastery fixtures, adversarial regression corpus, and recipe convergence belong
in `hsoliwal/com.synexia`. Exact candidate handoff:
[Synexia draft #12144](https://github.com/hsoliwal/com.synexia/pull/12144),
revision `ca8ae679d7839ef2c8e88ab43ff0d4417a639761` (private; access depends on repository permissions).
That draft inventories the target's existing recipe-source surface, not yet migrated source.

## Exact source intake

- Target original `master` commit: `678da68f34007af0aab0689aa72bd85fb44b800e`.
- Existing scoped receipt root: `.m3/openrewrite-recipes/src/`.
- Git-blob-identified scoped source assets: **9** total,
  consisting of **6 main Java** (**4** named `*Recipe.java`),
  **0 YAML source resources**, **3 test Java**.
- This is **only this known recipe module's source and test tree**, not every recipe
  in the full fork, other modules, PR branches, third-party dependencies or donors.
- Per-path immutable Git blobs, ownership-review holds and asset types live in
  the Synexia canonical intake `docs/M3-SCALE/runs/20261017-public-fork-recipe-intake/SOURCE_INTAKE.tsv`.
  The local TSV is a thin receiving pointer, not an independent recipe catalogue.
- **State**: `INTAKE_PINNED`; reusable recipe transfer `NOT_DONE`;
  production transformation `NOT_APPLIED`; CI/runtime admission `PENDING`.

## Source-first implementation gate

1. Compare each candidate against `synexia-openrewrite-recipes/CANONICAL_RECIPE_HOME.tsv`,
   `.m3/recipe-ownership.tsv`, legacy migration status and relevant active PR heads.
   Existing canonical recipes win; keep historical target-local mirrors frozen.
2. For a genuine portable gap, qualify and commit recipe implementation, Maven POM,
   tests, explicit donor/rights lineage, source/pre/post hashes and refusal/fixed-point
   proof **in Synexia first**, using `M3EveryModuleRecipeFirstTaskRecipe` and its
   existing task crate. Never manually edit source to simulate recipe application.
3. Accept only exact-hash-qualified **product postimages** from the canonical source
   into a separate product PR after passing the target's applicable compiler/API,
   behavior, native/ABI, resource and UI/runtime gates. Preserve independent FILE
   serial order and prove each widening scope before promotion.
4. Keep earlier optimizations and stacked PR evidence; never force-push, rewrite
   an active head, or silently merge this or any qualifying receiver.

**Product-specific constraints:** SWT owns original Widget/Control/Tree/Table, event, rendering and native JNI contracts. Do not introduce second viewport Controls. Keep auto-generated C JNI glue under SWT's Java-source JNI generator. Leave Tycho and native platform builds authoritative; keep target-specific GTK/Win32/Cocoa adaptations local.

**License gate:** EPL/EDL target licensing preserved, Apache source only where per-file compatible.

**Required target proof:** SWT Tycho/API, Java 21, JNI generator, Linux/Windows/macOS native/UI/paint, target EPL/EDL review.

This is deliberately a metadata/proof-only change. It neither executes nor
claims success for the user's request to apply *all* recipes. That requires an
individual applicability/contract decision and real verified run receipts.

## Second-pass canonical recipe census

The linked Synexia draft now also includes `ESTATE_RECIPE_CANDIDATES.tsv`,
`ESTATE_CANONICAL_CROSSWALK.tsv` and exact source-tree verifier. Across
three frozen public fork heads, the candidate taxonomy identifies 428
recipe-related source assets. 40 are exact Git-blob duplicates of files
already present in the three canonical Synexia source roots; 23 have
filename-only potential matches; 365 lack a source match in those roots.
These are **audit counts**, not executable recipe counts or qualification.
Potentially portable gaps require canonical source/contract/rights review
before any future product materialization. The focused rank installer
example is tracked separately in [Synexia #12162](https://github.com/hsoliwal/com.synexia/pull/12162)
(M3JDK21 only, candidate **not applied**).
