# Leaf 4: token-aware matching (M3TokenVocabulary, M3TokenMatch, M3TextSegments.tokens)

Map row M3-TOKEN-CURSOR. This leaf joins the language seed (leaf 3) to the m3-regex-cursor track: hsoliwal/M3jdk21 PR #1402
(branch `m3/regex-cursor-token-20261018`, head ba9a23087f; `m3/ports/regex-cursor/src/main/java/com/m3/regexcursor/`).
Its `M3TokenCursor.java` is byte-identical to the copy on `m3/regex-cursor-20261018` at cd45c35981 that this leaf was
designed against (checked with diff).

## Shape reuse, not a fork

The regex-cursor track owns the cursor. It defines:
- `M3ChunkCursor`: chunked symbol reading with `chunkLength`, `unit`, `offset`, `totalLength`, `advance`, `backtrack`, `seek` and `unitAt`;
- `M3CursorFacts`: `unitPresenceMask`, where bit `id & 63` is set when the symbol may occur;
- `M3TokenCursor.of(int[])` and `M3TokenCursor.lanes(int[][])`: a cursor over interned non-negative token ids.

SWT cannot depend on that module (it lives in M3JDK), so this leaf does not copy the interfaces. Instead it produces
exactly the data that `M3TokenCursor` consumes:
- `M3TextSegments.tokens(text, vocabulary, add)` returns an `int[]` lane of non-negative token ids, one per word segment
  (`M3TokenCursor.of(ids)` on M3JDK);
- a list of items gives `int[][]` lanes (`M3TokenCursor.lanes(...)`);
- `M3TokenMatch.presenceMask(ids)` uses the same bit rule as `M3CursorFacts.bit` (`1L << (id & 63)`), so the
  presence gate composes with the regex gates.

When the viewport-everywhere agent moves the language API into the M3JDK core, the SWT entry points delegate to
`M3TokenCursor` there, and the lanes keep their shape.

## Surface (package `org.eclipse.swt.m3`, public, `@since 3.138`)

| type | members |
|---|---|
| `M3TokenVocabulary` | `M3TokenVocabulary(int capacity, boolean foldCase)`; `UNKNOWN` (0); `intern(CharSequence, start, end)`; `lookup(CharSequence, start, end)`; `size()`, `capacity()`, `foldsCase()`, `token(int id)` |
| `M3TextSegments` | `tokens(String text, M3TokenVocabulary vocabulary, boolean add) -> int[]`: ids of the word segments in order (empty for other kinds) |
| `M3TokenMatch` | `presenceMask(int[] ids)`; `mayContain(long haystackMask, int[] needle)`; `indexOf(int[] haystack, int[] needle, int from)` |

## Contract

- Ids are dense: 1..size in interning order. `UNKNOWN` (0) is returned when a token is absent, for `lookup`, or when the
  vocabulary is full, for `intern`. Ids are stable for the life of the vocabulary (a bounded, append-only table: TIL
  "bounded interner", no `String.intern`).
- Case folding (optional) is per UTF-16 unit: `Character.toLowerCase(Character.toUpperCase(c))`. It is
  locale-independent, so it makes 'ς' and 'σ' equal; supplementary letters keep their case. Hash and equality use the
  same folding, so lookups are consistent.
- No `String` is created by `intern`, `lookup` or `tokens`. Keys live in one char pool (folded) with int start/end
  lanes; `token(id)` builds a String on demand, as a facade.
- `indexOf` finds the first occurrence of `needle` at or after `from`, comparing ids:
  - an empty needle matches at `min(max(from, 0), length)`, like `String.indexOf("")`;
  - a needle holding `UNKNOWN` matches nothing, because unknown words never match;
  - otherwise matching over ids equals matching over the folded word sequences (the test oracle).
- `mayContain(mask, needle)` is false only when some needle id's presence bit is clear in the haystack mask. It never
  gives a false negative.

## DOOP

- Vocabulary lanes: `char[] pool`, `int[] starts`, `int[] ends`, `int[] hashes`, and an open-addressing `int[] table`
  of ids. Linear probing, load factor at most 1/2, power-of-two table that doubles with the size.
- Tokens are one `int[]` lane per text.
- Matching allocates one `int[m]` failure table per call (KMP); nothing is allocated per haystack element.

## Algorithm choices

See `ALGORITHM_CATALOGUE.tsv` (rule 21): open addressing over a char pool against `HashMap<String,Integer>`, and KMP
plus a presence gate against naive and Rabin-Karp search.

## Relation to M3TokenPattern (PR #1402)

PR #1402 also adds `M3TokenPattern`, a token-aware regex over the same lanes (gates, lazy DFA, PikeVM; notation such as
`<#IDENT> <20> (<#IDENT>|<#NUMBER>)`). `M3TokenMatch.indexOf` is only the contiguous-sequence subset: a needle of ids,
matched with KMP. It is what a widget needs for type-ahead and find over item text. Once the language API moves to the
M3JDK core, a sequence needle is the pattern `<id1> <id2> ...`, and alternation, classes and repetition come from
`M3TokenPattern` rather than from SWT.
