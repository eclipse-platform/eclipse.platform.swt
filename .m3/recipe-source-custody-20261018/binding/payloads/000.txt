# M3 RECIPE_FIRST sidecar

Isolated Java 21/OpenRewrite recipe crate; native build files are untouched.

- Build: `mvn -f .m3/openrewrite-recipes/pom.xml clean install`
- Inventory: `mvn -f .m3/analysis-pom.xml -Pm3-recipe-first-inventory org.openrewrite.maven:rewrite-maven-plugin:dryRun`
- Task lane requires `m3.llm.taskCrateFile` plus exact SHA-256 `m3.llm.taskCrateRoot`.

Source-changing work must be authored as a reusable tested recipe after these gates. Direct LLM target-file editing is not an allowed lane.

- Native source receipt: install `com.synexia:synexia-openrewrite-recipes:1.0.0-SNAPSHOT`, then run
  `mvn -f .m3/analysis-pom.xml -Pm3-native-source-distillation org.openrewrite.maven:rewrite-maven-plugin:run`.

## Current canonical recipe source custody (2026-10-18)

The reusable recipe source closure from this fork is held in `hsoliwal/com.synexia` at `215546d1ca0e724eb2c8e2e8ef6b4adce724733a`. The [dated custody binding](SYNEXIA_RECIPE_SOURCE_CUSTODY_20261018.json) records the exact public source revisions, canonical snapshot paths, source seals and the separate execution limits. The current source-custody ledger is `synexia-openrewrite-recipes/recipe-crates/public-fork-home-20261018/SOURCE_CUSTODY.tsv` (SHA-256 `ebcfbd42a49eec806f0a4572af731828deda41f22c17250d34d49af985d78f1d`).

Existing public source and SDK/proof mirrors retain their recorded provider revisions because their build and proof callers have not yet migrated to a qualified canonical handoff. Keep improvements in the canonical recipe source and return qualified postimages to this target. Existing native builds, product APIs, runtime acceptance and promotion remain target-owned.

The retained-mirror source revision is the frozen custody origin. Separate current packet applications may update target paths under those roots; their exact inputs, outputs and qualification limits are recorded independently below.

The binding's original-packet verification covers only its listed packets. The [current recipe census](https://github.com/hsoliwal/com.synexia/blob/215546d1ca0e724eb2c8e2e8ef6b4adce724733a/synexia-openrewrite-recipes/recipe-crates/public-fork-home-20261018/evidence/CURRENT_RECIPE_CENSUS.json) (SHA-256 `cfa9edb42a99cb2e54a988b28de7078a3233fdc667c5e4d8f979675e0f767ee0`) records the separate admission, application and gate evidence for current packets. Canonical kernel checks do not establish SDK or product runtime qualification: the canonical SDK attempt was blocked before compilation with zero tests. The binding-only application does not run native product qualification; gate attempts for separate product packet applications are recorded in the current census.

The sealed binding and execution-receipt packets under `.m3/recipe-source-custody-20261018/` are pinned proof mirrors. They grant no competing recipe ownership or product promotion authority.
