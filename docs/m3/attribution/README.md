# Synexia attribution of the SWT fork

Every file of this branch whose bytes differ from upstream `eclipse-platform/eclipse.platform.swt` master
(`eeb3482af3`) is classified once, by content, and the classification drives a recipe. Nothing is edited by hand.

| File | What it is |
| --- | --- |
| `CLASSIFICATION.tsv` | One row per differing path: blob, header holders, class, action, note, origin, evidence, rule. |
| `CLASSIFICATION.json` | Counts per class and action, head and upstream commits, SHA-256 of the TSV. |
| `OVERRIDES.tsv` | The five reviewed exceptions, each with its reason. |
| `recipe/META-INF/rewrite/m3-synexia-attribution-swt.yml` | The ledger: one content-addressed decision per file (`<git blob> <action> [note]`). |

## Classes and actions

- `SYNEXIA_AUTHORED`: no licence header, the programme template holder only, or first-party holders only
  (Synexia, Hitesh Soliwal). The header is replaced by the canonical Synexia header (Apache-2.0 OR EPL-2.0);
  provenance notes and earlier first-party contributor entries are kept below it.
- `UPSTREAM_MODIFIED`: the path exists in upstream history and the fork changed its content. The upstream licence
  and notice stay; one contributor entry `Hitesh Soliwal (Synexia) - <what changed>` is added.
- `DONOR_DERIVED`: the header names a third-party holder (IBM, The Chromium Authors, ...). Licence and notice stay;
  the same contributor entry is added.
- `GENERATED`, `NON_CODE`: recorded only.

Provenance is content-addressed: a blob that upstream history (merge base to master) or a donor ref
(`origin/donor/initiative31*`) ever held is upstream drift or a verbatim donor file, and gets no line.

## Decisions that are reversible by regenerating the ledger

- **No contributor line for formatting-only changes** (1,122 upstream files): the fork's diff is whitespace, braces
  and import lines only. Formatting is not authorship.
- **`.m3/` evidence is not edited** (98 files): its bytes are pinned by receipts and hash-pinned manifests; the
  class is recorded in the TSV only.

Either decision is reversed by regenerating the classification with `classify_attribution.py` (different action for
those rows) and `gen_attribution_yml.py`, then applying the ledger again.

## Reproduce

Tools live in hsoliwal/com.synexia, crate `synexia-openrewrite-recipes/crates/m3-synexia-attribution`
(recipes `M3SynexiaAttribution` for Java, `M3SynexiaTextAttribution` for scripts and native sources).

```text
python tools/classify_attribution.py --repo <wt> --upstream refs/remotes/upstream/master \
  --donor-refs <donor-refs> --overrides docs/m3/attribution/OVERRIDES.tsv --fetch \
  --out docs/m3/attribution/CLASSIFICATION.tsv --summary docs/m3/attribution/CLASSIFICATION.json
python tools/gen_attribution_yml.py --classification docs/m3/attribution/CLASSIFICATION.tsv \
  --name com.synexia.attribution.Swt --yml docs/m3/attribution/recipe/META-INF/rewrite/m3-synexia-attribution-swt.yml \
  --java-files java.files --text-files text.files
java -cp "docs/m3/attribution/recipe;<crate classes>;<viewport crate classes>;<cp>" \
  com.synexia.openrewrite.viewport.M3PerFileApply com.synexia.attribution.SwtJava <java files>
java -cp ... com.synexia.openrewrite.attribution.M3AttributionTextApply com.synexia.attribution.SwtText <other files>
```

A file whose bytes are not in the ledger is refused (`ERROR`), never changed. A second application changes nothing.

## Receipt

| Step | Result |
| --- | --- |
| Classified head | `1a7fd8c37f` (integration `36dceb43b9` merged) |
| Pass 1 | 440 CHANGED, 0 UNCHANGED, 0 ERROR (399 Java via `M3PerFileApply`, 41 via `M3AttributionTextApply`) |
| Pass 2 | 0 CHANGED, 440 UNCHANGED, 0 ERROR |
| Compile check | `compile_platform.sh <wt> win32`: 765 sources, 0 errors |
| `CLASSIFICATION.tsv` | sha256 `f5efa1618fd4cca717e6d8533a2320ed7b9c54b0c19e95cdfc5fe487e4e999da` |
| Ledger YAML | sha256 `7a84f6fd5601fd6964c207a84efeef7a656de7f784738f100259beb4eb1fe271` |
| Re-application after integration ff36ce3c51 (merge 20463b2244) | classified 2,517 paths: 4 REHEAD, rest already attributed; pass 1 CHANGED 1, pass 2 UNCHANGED 4; naming recipe pass 1 CHANGED 1, pass 2 UNCHANGED 11; sed fixed point; compile win32 765 sources, 0 errors |
