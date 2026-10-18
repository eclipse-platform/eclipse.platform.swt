# M3 atomize/patternize analysis

This directory is a non-destructive repository-analysis entry point. It does **not** replace the
repository's native build.

By default it runs the mastered read-only composite:

`com.synexia.m3.EveryModuleAtomPatternMastery`

That composite performs strict FILE -> PACKAGE -> MODULE -> PROJECT -> REPOSITORY atom/pattern
coverage and then reuses the canonical regex/String permutation campaign, compiler/JUnit/Java-JNI
mastery evidence and current V6 fan-in. It has no mutation, source-copy, replacement, merge, or
promotion authority.

For the lighter inventory-only lane, set:

```sh
export SYNEXIA_ATOM_PATTERN_RECIPE=com.synexia.m3.EveryModuleAtomPatternApplication
```

## Bootstrap an unbound repository

A rollout branch may intentionally contain only the shared runner/readme/bootstrap scripts and no
`inventory.tsv` yet. Bind that repository's current ordinary Java/POM bytes first:

```sh
bash .m3/atomize-patternize/bootstrap.sh
```

The bootstrap does **not** invoke Maven or OpenRewrite. It creates only
`.m3/atomize-patternize/inventory.tsv` plus transient evidence under `.m3/target`.

If the repository changes later, rebinding is explicit:

```sh
SYNEXIA_REBIND_INVENTORY=1 bash .m3/atomize-patternize/bootstrap.sh
```

A normal run still fails closed when source/POM bytes drift from the bound inventory.

## Content-bound execution

`inventory.tsv` binds the generated envelope to the exact ordinary Java and `pom.xml` bytes seen
when the envelope was created. Before Maven or OpenRewrite is invoked, `run.sh` rebuilds a sorted
`source-files.tsv`, hashes it, and refuses with `M3_SOURCE_CONTENT_DRIFT` or
`M3_SOURCE_FILE_SET_DRIFT` if the repository no longer matches that inventory.

The execution snapshot is retained under `.m3/target/atomize-patternize/`:

- `source-files.tsv` — exact kind/path/byte-count/SHA-256 rows,
- `execution-receipt.tsv` — structural/content/bound roots and selected recipe artifact,
- OpenRewrite/Maven output — analysis evidence only.

Generated/build/vendor trees remain excluded. File names containing tab, CR, or LF are refused by
the generator because this evidence format is deliberately line-oriented and exact.

## Prerequisite

Install the matching `com.synexia:synexia-openrewrite-recipes` artifact into the Maven repository
visible to this project, then set:

```sh
export SYNEXIA_RECIPE_VERSION=<installed-version>
```

A repository with no Java source is content-verified first and then exits cleanly with
`NO_JAVA_SOURCE_ROOTS`; it does not require Maven or a recipe artifact.

## Run

```sh
bash .m3/atomize-patternize/run.sh
```

Existing Maven reactors are used directly. Repositories without Maven build files use a temporary
POM under `.m3/target/atomize-patternize/` and the Java roots from the verified source manifest.

OpenRewrite DataTables are analysis evidence only. Any content drift, parse/dependency/build
failure, or recipe failure is explicit residue to resolve; it is not permission to copy or mutate
donor source.
