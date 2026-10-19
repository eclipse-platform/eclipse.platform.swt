# Leaf 12: locale collation keys (M3Collation)

Map row M3-LOCALE-COLLATION. It comes before M3-PROXY-MODEL in this branch because the proxy model's sort stage
consumes it. Sorting widget rows by `String.compareTo` is wrong for every language:

- German ä sorts with a.
- Swedish puts å, ä and ö after z.
- Turkish has a dotted and a dotless i.

`java.text.Collator` is right, but comparing with it allocates and re-decomposes both strings on every call of an
O(n log n) sort.

## Surface (package `org.eclipse.swt.m3`, public, `@since 3.138`)

| member | meaning |
|---|---|
| `M3Collation.keys(String[] items, Locale locale)` / `keys(String[], Locale, int strength)` | builds the sort keys of the items once |
| `M3SortKeys.size()`, `compare(int i, int j)` | key order of two items: the same sign as `Collator.compare(items[i], items[j])` |
| `M3SortKeys.sortedOrder()` | a stable ascending permutation of the item indices |
| `M3SortKeys.keyLength(int i)` | the byte length of one key (diagnostics) |

## Contract

- `sign(compare(i, j)) == sign(collator.compare(items[i], items[j]))` for the collator of the locale and strength
  (default `Collator.TERTIARY`).
- `sortedOrder` is ascending by that order and stable: equal keys keep their input order.
- Locale differences are visible: de "ä" < "b"; sv "ä" > "z"; tr "ı" < "i".
- null items are rejected.

## DOOP

- All keys live in one `byte[]` lane, with an `int[]` offset lane (n + 1 entries).
- A comparison is an unsigned byte-range compare (`Arrays.compareUnsigned`), with no allocation.
- `sortedOrder` sorts an `int[]` permutation with a stable merge sort over the key lanes; it does not box.

## Algorithm choices

See `ALGORITHM_CATALOGUE.tsv`: precomputed collation keys (ICU/CLDR practice) against comparing with a Collator.
