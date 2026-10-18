# Executed SWT native source distillation

All 124 tracked C-family source files are represented by recoverable atom packets:
58 C files, 54 headers, 11 C++ files and one Objective-C++ file. The 2,973,211
source bytes recompose exactly from 60,054 atoms. Native sources and JNI signatures
retain their bytes. Generated JNI files were not edited.

The canonical owner is `M3NativeSourceAtomizer`, composed through
`M3SwtNativeSourceReceiptRecipe` and `M3NativeSourceReceiptWriter` in
`hsoliwal/com.synexia`. The `native-packets-20261005` Maven recipe crate seals its
Java changes and the SWT workflow/documentation updates. The original native
source-distillation workflow is retained as a read-only PR/master verification
gate, with the canonical CLI pinned by commit.

`atoms/<original-source-path>.tsv` contains ordered atom payloads, byte ranges,
kinds, nesting depths and SHA-256. Base64 preserves high bytes and line endings.
`native-files.tsv` seals whole files and atom signatures; `native-patterns.tsv`
groups lexical kind and depth. The generated `COMPLETE.properties` records the
exact finite source universe. These lexical groups do not claim C/C++ type or
macro semantics, interchangeable behavior, or automatic replacement authority.

To regenerate with the qualified canonical classes:

```sh
java -cp /path/to/canonical/target/classes com.synexia.rewrite.M3NativeSourceReceiptCli "$PWD"
java -cp /path/to/canonical/target/classes com.synexia.rewrite.M3NativeSourceReceiptCli "$PWD" --check
```

The existing Maven/OpenRewrite lane is also retained:

```sh
mvn -f .m3/analysis-pom.xml -Pm3-native-source-distillation org.openrewrite.maven:rewrite-maven-plugin:run
```

Install the qualified canonical recipe artifact first. Its receipt scanner now
recognizes every generated packet and the pattern catalogue, so the second recipe
pass is a fixed point. Full-source OpenRewrite execution and an independent Base64
decoder both checked all native outputs. See `.m3/source-coverage-20261005/` for
current Java packet restoration, native proofs and the explicit remaining runtime
criteria. A lexical atom receipt is not a native optimization benchmark.
