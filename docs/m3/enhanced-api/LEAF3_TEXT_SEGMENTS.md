# Leaf 3: M3TextSegments (language API seed)

Map row M3-TEXT-SEGMENTS (`docs/m3/ENHANCED_API_MAP.tsv`); donor ledger `FOSS_REUSE_DECISION.tsv`
(capability `text-segments-api`: JDK `java.text.BreakIterator` as the platform engine; ICU4J under licence review
for a later engine behind the same surface; CLDR segmentation tailorings and the Hugging Face treebanks and word
lists of `K:/m3-work/donors/catalogue/HF_LANGUAGE.tsv` as data with provenance, per `docs/m3/LANGUAGE_SUPPORT.md`).

## Surface (package `org.eclipse.swt.m3`, public, `@since 3.138`)

`M3TextSegments.words|graphemes|lineBreaks|sentences(String, Locale)` and `of(kind, String, Locale)`;
`kind()`, `count()`, `length()`, `start(i)`, `end(i)`, `isWord(i)`, `segmentAt(offset)`, `boundaries()` (copy).
Kinds `WORD`, `GRAPHEME`, `LINE`, `SENTENCE`.

## Contract

- Segments partition the text in `char` offsets: `start(0) == 0`, `end(count - 1) == length`,
  `end(i) == start(i + 1)`, every segment non-empty; an empty text has no segment and boundaries `{0}`.
- Boundaries equal the JDK iterator's sequence for the same kind and locale (the oracle of the tests), so the
  locale reaches the engine unchanged (Thai dictionary breaks differ from the English rules and both match).
- `isWord(i)` is set for WORD segments holding a letter or a digit (any code point, surrogate pairs included),
  never for the other kinds. `segmentAt` is a binary search over the boundary lane; -1 outside the text.
- Widget-free: no Display is needed; the facts feed item text measurement, caret movement and the later
  M3-LOCALE-COLLATION and M3-SHAPING-MEASURE rows.
- Known engine limit, recorded not hidden: the JDK character instance predates UAX #29 extended grapheme
  clusters for emoji sequences (ZWJ, modifiers); combining marks are handled. ICU4J (ledger row, licence review)
  is the planned engine upgrade behind the same surface.

## DOOP

One `int[]` boundary lane (`count + 1`) and one `long[]` word-bit lane per text; no substring, no per-segment
object. The boundary lane grows by doubling while iterating and is trimmed once.

## Tests

`Test_M3TextSegments` (headless, 8 tests): JDK oracle equality for all four kinds, partition invariants, word
flags against a per-segment code-point scan, combining mark and surrogate pair, three locales over Thai text,
`segmentAt` for every offset, empty text, a 140-segment text (second bit word, growing lane), argument checks.
