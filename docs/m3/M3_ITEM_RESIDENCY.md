# M3 item residency: parent-owned item state, pressure-driven payload tiers

Status: draft design and engine for user review. No widget adopts the engine in this leaf.

Governing invariant: Synexia `docs/M3-SCALE/invariants/M3-LAZY-OBJECT-PRESSURE-1.md`
([#10889](https://github.com/hsoliwal/com.synexia/pull/10889)). Sibling realizations:

- `M3LazyObjectArray` ([#10891](https://github.com/hsoliwal/com.synexia/pull/10891));
- `M3LazyObjectSession` with `M3_LAZY_OBJECT_RESIDENCY.md` ([#10892](https://github.com/hsoliwal/com.synexia/pull/10892)).

This document is the widget realization: handles, state lanes and patterns, the reproduction contract,
SetData negotiation and identity pinning. The compile-time half (escape metadata per call site) is
M3jdk21 `m3/docs/m3-item-residency-metadata.md` ([M3jdk21 #826](https://github.com/hsoliwal/M3jdk21/pull/826)).

## 1. Packages and owners

Licence: the engine files of this leaf (`org.eclipse.swt.internal.residency`, `PackedHeapGrowth`, `FileSwapStore`,
`SwapPageBinaryOpsSelector`) are `EPL-2.0 OR Apache-2.0` by the rights holder's decision of 2026-10-13
(see `package-info.java`). The ported Synexia swap files stay Apache-2.0.

The core has no SWT types. Everything below is toolkit-neutral; only a future adapter in
`org.eclipse.swt.widgets` (owned by the widget agents) knows `TableItem`, `TreeItem` or `SWT.SetData`.

| Package (`Eclipse SWT/common`) | Owner | Purpose | Web |
|---|---|---|---|
| `org.eclipse.swt.internal.residency` | `ItemResidency` | facade: tiers, CLOCK eviction, pressure arithmetic, SetData negotiation, data lanes, paint path, identity | yes |
| | `ItemBitSet` | sparse bit set: ARRAY / BITMAP / RUN containers per 65536 positions | yes |
| | `ItemLanes` | 23 state lanes: exceptions, pattern rules, default | yes |
| | `ItemTreeShape` | lazy tree topology: depth rule, exception paths, row and key mapping | yes |
| | `ItemSlots`, `ItemDirectory`, `ItemSparseTable`, `ItemRequests` | payload slots and codec, paged item directory, sparse object lanes, request batching | yes |
| | `ItemSwapSpill`, `ItemSwapPressure` | desktop COLD tier and pressure source over the ported swap owners | no |
| `org.eclipse.swt.internal.swap` | `TieredSwapCache`, `SwapStore`, `SwapBudgetPlane`, `SwapPressureController`, page ops | ported from Synexia (Apache-2.0) | no |
| | `FileSwapStore`, `SwapPageBinaryOpsSelector` | Java fail-closed store; page-ops selection | no |
| `org.eclipse.swt.internal` | `PackedHeapGrowth` | Xmx-aware growth for every growable array | super-source |

Both new packages are exported `x-internal` in `MANIFEST.MF`.

**Swing, SwingX and AWT reuse.** Yes, this needed a package split. The core moved out of
`org.eclipse.swt.widgets`, which holds SWT types, into `org.eclipse.swt.internal.residency`, which holds none.

- **Reuse route.** A Swing adapter (`TreeModel`, `TableModel`, `ListModel`) reuses the three packages through
  a hash-pinned package-rename recipe into M3jdk21 `java.desktop`, for example `sun.swing.m3.residency`.
  Nothing is hand-copied.
- **License (decided 2026-10-13).** The engine is dual-licensed `EPL-2.0 OR Apache-2.0`. Swing reuses it as
  Apache-2.0 code in M3jdk21 `m3/ports/swing-viewport`. Copies into `java.desktop` internals are an explicit
  relicence to GPLv2+CPE, recorded in M3jdk21 `m3/docs/m3-item-residency-explicit-relicense.md`.

## 2. The handle

An Item, node or row object is a thin handle: `parent + item index + generation`. All state lives in the
parent's lanes and arrays; calling an Item method mutates the parent's bits and arrays. "Half the time the
user wants just the behaviour": a handle obtained only to call `getText()` need not outlive the call.

## 3. State: masks, flags and patterns

All state lives only in masks, flags and patterns. Data lives in dynamic arrays and swappable payload lanes.

| Lane | Name | Meaning | Default |
|---:|---|---|---|
| 0 | `LANE_EXPANDED` | expanded (flat widgets; trees use `ItemTreeShape`) | 0 |
| 1 | `LANE_HAS_CHILDREN` | has children (valid when lane 2 set) | 0 |
| 2 | `LANE_CHILDREN_KNOWN` | has-children is known | 0 |
| 3 | `LANE_CHILDREN_LOADED` | children materialized | 0 |
| 4 | `LANE_LOADING_PLACEHOLDER` | loading placeholder shown | 0 |
| 5 | `LANE_SELECTED` | selected | 0 |
| 6 | `LANE_FOCUSED` | focus item | 0 |
| 7 | `LANE_HOVERED` | hot item | 0 |
| 8 | `LANE_DISABLED` | enabled, stored inverted | 0 = enabled |
| 9 | `LANE_FILTERED_OUT` | filtered-visible, stored inverted | 0 = visible |
| 10 | `LANE_CHECKED` | checked | 0 |
| 11 | `LANE_GRAYED` | grayed | 0 |
| 12 | `LANE_CHECKABLE` | shows a check box | 0 |
| 13 | `LANE_DIRTY_PAINT` | needs repaint | 0 |
| 14 | `LANE_DIRTY_MEASURE` | needs re-measure | 0 |
| 15 | `LANE_DIRTY_DATA` | user-owned; `invalidate` sets it, the engine never clears it | 0 |
| 16 | `LANE_PINNED` | a pinned handle exists (engine-owned) | 0 |
| 17-18 | `LANE_TIER_LO/HI` | resident tier (engine-owned, section 5) | 00 = DESTROYED |
| 19 | `LANE_CALLER_SET` | payload written outside a fill (engine-owned) | 0 |
| 20 | `LANE_MATERIALIZED` | payload supplied at least once (engine-owned) | 0 |
| 21 | `LANE_DATA_REQUESTED` | SetData outstanding (engine-owned) | 0 |
| 22 | `LANE_DATA_LOADED` | payload in HOT, WARM or COLD (engine-owned) | 0 |

**Containers.** The design follows RoaringBitmap's container idea (ADAPT, Apache-2.0); the code is original.
Each lane is an `ItemBitSet` of 65536-item chunks, and each chunk takes one of three forms:

- **ARRAY**: sorted `int` positions for sparse exceptions, at most 4096 of them;
- **BITMAP**: 2048 `int` words for dense pages;
- **RUN**: `(start, end)` pairs, used for select-all and check-all.

After a range operation each chunk takes its smallest form. Memory is O(non-default state), not O(items).
A billion-item lane with nothing set costs nothing, and select-all costs one RUN pair per chunk.

**Patterns.** A pattern stores a rule, not per-item bits: packed `(start, end, stride, repeatMask, phase)`.
Item `i` in `[start, end)` is set when bit `(i - start + phase) % stride` of `repeatMask` is set.

- "Select every 3rd row" is stride 3, mask `0b001`.
- "Check rows matching a repeating mask" is stride k with that mask.
- `phase` lets a pattern split or shift on insert and remove without changing any item's state.
- Lookups and counts are arithmetic.
- Mutations split a pattern only where items deviate.

**Precedence** (normative): explicit exception, then pattern, then default.

- An exception is an override bit plus a value bit, both `ItemBitSet`s.
- Inside a pattern, `setBit` records an exception only when the value differs from the rule.
- `setRange` cuts the pattern away.
- `setPattern` drops the exceptions in its range.

Example: "select every 3rd row" over 1,000,000,000 items costs one 5-int entry, and `countRange` stays exact.

**Layout history.** The first layout was chosen by measurement ([`bench/LANE_LAYOUT_BENCH.tsv`](item-residency/bench/LANE_LAYOUT_BENCH.tsv)):
one `int[]` per lane beat interleaved words on range operations (0.087-0.117 ms against 2.1-4.3 ms for
8M items) at equal memory, and `int` words suit GWT.

That dense layout costs O(items), 2.9 GB for 23 lanes at 1B items, so the billion-scale directive replaced
it with containers. The measured per-lane `int` word survives as the BITMAP chunk format.

## 4. Payload and parent lanes

Payload lives in resident slots: arrays sized by slot capacity, never by item count.

- `slotInts`: `I_FOREGROUND`, `I_BACKGROUND` (RGBA, 0 = default), `I_FONT`, `I_IMAGE`, `I_ROW_HEIGHT`,
  `I_INDENT`, `I_LEVEL`, `I_TOOLTIP`, `I_ACCESSIBLE`, `I_USER`, then per cell `C_FOREGROUND`,
  `C_BACKGROUND`, `C_FONT`, `C_IMAGE`.
- `slotText`: cell text.
- `slotBlob`: opaque bytes.

Parent-owned lanes, indexed by item and always resident:

- **Item data lane** (`setData(Object)`) and **keyed data lane** (`setKeyedData(item, key, value)`).
  - Values are pinned: never swapped or dropped, and identity is preserved across release and recreate.
  - Exception: a value accepted by a registered `DataCodec`. Registering one declares the accepted values
    immutable (the peer `M3LazyObjectArray` immutable-codec rule). Such a value travels with a COLD item's
    record and comes back equal, not identical.
- **Editor scalars**: item, column, text, caret, selection start and end.
- **Pin table**: pinned handles.

`setDerivedInt` writes recomputable values (measured height, indent). They are not caller-owned and are
recomputed after DESTROY.

## 5. Residency tiers

| Code | Tier | Payload | Peer equivalent |
|---:|---|---|---|
| 0 | DESTROYED | none; recreated by SetData on next access | first miss: factory |
| 1 | HOT | resident slot inside the viewport runs; never evicted | HOT (pinned lease) |
| 2 | WARM | resident slot outside the window; CLOCK candidate | WARM |
| 3 | COLD (SWAPPED) | record in the ported `TieredSwapCache` over a `SwapStore` | NONRESIDENT + store record |

Eviction of a WARM slot uses CLOCK second chance; the window and the slot being filled are skipped.

1. Source-backed payload with `RESIDENCY_DESTROY` is dropped.
2. Otherwise, with `RESIDENCY_SWAP`, the payload is encoded once into the reusable buffer and written:
   `TieredSwapCache.put`, then `FileSwapStore`. A clean copy (unchanged since swap-in) is not rewritten.
3. Otherwise the payload stays resident. Caller-owned payload is never discarded; `STAT_UNEVICTABLE` counts
   these refusals.

**Swap backend (desktop).**

- `ItemSwapSpill` maps engine int handles to immutable store ids. `TieredSwapCache` keeps recently swapped
  records as WARM bytes, so scroll-back hits memory before disk.
- The Synexia JNI SQLite store is not ported: its JNI symbols are bound to `com.synexia`, and SWT ships no
  Synexia native library. The backend fails closed to the Java `FileSwapStore`.
- An embedder may pass a native page-ops candidate. It is admitted only behind
  `ParityCheckedSwapPageBinaryOps`, after a self-test.

**Web:** DESTROY only, no spill.

## 6. Pressure and growth

`ItemSwapPressure` wraps the ported `SwapPressureController` with unchanged semantics:

- softStart 0.70 and hardStop 0.96 over heap, cgroup and PSI; worst signal wins;
- shrink is immediate, recovery is rate-limited to +125 permille;
- a telemetry failure zeroes optional residency.

The same decision readjusts the engine's slot budget and the `TieredSwapCache` byte budget.
A collection-usage threshold at 70% marks the next operation as a sample point. The budget is
`max(HOT window size, hardSlots * permille / 1000)`, computed without int overflow. A read that triggers a
fill still sees its value at zero residency.

Every growable array (bit-set chunks, pattern tables, slot tables, directory spine, pin and data tables,
swap handle table) grows only through `PackedHeapGrowth.capacity`. It samples Xmx and used heap once per
capacity miss. Under low headroom it returns the exact length instead of 1.5x slack (tested with a
simulated sampler).

## 7. SetData negotiation (engine-owned)

- The viewport arrives as item runs in priority order: visible, then overscan in the scroll direction,
  then prefetch beyond it.
- `ItemRequests` batches each run into contiguous ranges of items that are neither loaded nor requested.
  It marks `LANE_DATA_REQUESTED` and calls `DataSource.request(owner, from, to)`.
- Outstanding requests outside every run are cancelled in ranges through `DataSource.cancel`.
- Writes between `beginData` and `endData` are source-backed; `endData` marks the item loaded.
- An item restored from HOT, WARM or COLD is loaded and never re-requested. Only DESTROYED re-fires SetData.
  The 10M stress verifies zero re-fires for COLD items.

## 8. Trees at billion scale (`ItemTreeShape`)

No parent/child index is materialized. Node keys are arithmetic over per-depth fan-outs from the data
source: the child `c` of node `k` at depth `d` is `levelStart[d] + (k - levelStart[d-1]) * fanout[d] + c`.

Expansion is stored as a depth rule ("expand to depth N", one int) plus explicit exceptions. Only the
exception paths are kept, each with sorted child deltas and prefix sums.

Row-to-node mapping descends one level at a time:

- below the first exception of a level: arithmetic;
- among exceptions: binary search over `childIndex * unit + prefix`.

`setViewportRuns` takes the key runs of the visible rows. Lanes are keyed by node key, so state survives
collapse and expand.

Scope of this leaf: uniform fan-out per depth. Per-node child counts stay with the Tree owner's
`VirtualTreeTopology` until they adopt a per-node variant. The topology is verified against a materialized
tree of 1,010,100 nodes.

## 9. Paint path (normative for adopters)

Widget paint code reads only through:

- `beginPaint()` and `endPaint()`;
- `paintSlot(item)`, `slotText`, `slotInt`, `slotCellInt`, `slotBlob`;
- `paintLanes(item)`;
- the bulk forms `paintLanes(from, to, int[])` and `paintSlots(from, to, int[])`.

These never page in, request data, allocate or create handles. Inside a paint scope, any public getter that
would materialize increments `materializedDuringPaint`, which must stay 0, and tests assert it.

The rule: if redraw triggers materialization, change the widget, never the engine contract.

## 10. Reproduction contract (normative)

Every piece of state that affects how an item looks or behaves must be reconstructible from parent-owned
state plus the data source. An item scrolled out and destroyed must come back identical when scrolled in.
Nothing may live only in the transient item object.

[`REPRODUCTION_STATE.tsv`](item-residency/REPRODUCTION_STATE.tsv) lists each state with its lane or array,
owner and test. Widget owners add their rows.

Tests:

- `Test_ItemResidency` round trips through DESTROY and through COLD, comparing every getter.
- `Test_ItemResidencyPrint` paints a `Canvas` through the paint path and compares `Control.print` pixel
  hashes before and after the round trip.

## 11. Identity

- **Pinned** (default): the handle stays the same object while any caller may retain it. Pins are re-keyed
  on insert and remove through `HandleFactory.rebind`.
- **Recyclable**: a 4-handle flyweight ring rebound with a new generation. It is used only with
  `IDENTITY_BY_METADATA` when the compile-time sidecar classifies the call site `SITE_BEHAVIOUR_ONLY`.
  `isCurrent(handle, item, generation)` detects stale flyweights.
- **Default is safe**: unknown sites, retaining sites, `IDENTITY_PIN_ALL` and a missing sidecar all pin.
- **Site key**: FNV-1a 32 over the UTF-16 units of `owner.name+desc@bci`, with the bytecode offset exact.
  It is identical in the M3 pass.
- **Sidecar** `M3IRS01`: `0x4D334952`, `1`, `count`, then sorted `(key, mask << 2 | class)` pairs.

## 12. Construction flags

One engine-owned `int config`. It uses no `Widget.state` bit (win32 bits 25-31 are claimed by the
visibility agent) and no style bit. Adopters keep one `int residencyFlags` per widget. The default comes
from system property `org.eclipse.swt.m3.itemResidency` and is `IDENTITY_PIN_ALL`, which is stock behaviour.

| Bit | Flag | Effect |
|---:|---|---|
| 0 | `RESIDENCY_SWAP` | non-destroyable payload may go COLD |
| 1 | `RESIDENCY_DESTROY` | source-backed payload is dropped and refilled |
| 2 | `IDENTITY_PIN_ALL` | every handle pinned |
| 3 | `IDENTITY_BY_METADATA` | recycle at sites proven behaviour-only |
| 4 | `PRESSURE_SAMPLED` | sample pressure on the operation cadence |

## 13. Web and GWT safety

The core (`residency` without the two desktop owners) is web-safe:

- `int` state and arithmetic only;
- no reliance on int overflow (the FNV multiply is split so every step is exact in a double);
- no `clone()`, no `Objects.checkIndex`, no `java.nio`, no `java.lang.management`, no threads.

`PackedHeapGrowth` needs a GWT super-source that returns the geometric capacity. The swap package,
`ItemSwapSpill` and `ItemSwapPressure` are desktop-only. GWT compilation was NOT_RUN.

## 14. UI-driven additions and port choices

| Structure | Choice | What changed, and the UI reason |
|---|---|---|
| `TieredSwapCache`, `SwapStore`, `SwapBudgetPlane`, `SwapPressureController`, page ops | port (package rename) | Two readjust overloads for unported owners removed. No widget-path change was needed, so no upstream PR. |
| `SwapPageBinaryOpsSelector` | rewrite | No `com.synexia.nativeinterop`: Java oracle by default, native candidate only behind a parity check. |
| `FileSwapStore` | generated gap | Synexia has no Java `SwapStore`. Ids carry location plus sequence, so the scroll-back path needs no per-record heap index. |
| `PackedHeapGrowth` | copy and specialize | Any positive row width (packed slot tables), a sampler hook, counters (low-headroom test). |
| `ItemResidency.paintLanes(from, to, int[])`, `paintSlots(from, to, int[])` | new | Bulk read of a visible window into a reusable array, with zero materialization. |
| `setViewport` prefetch run, `setPrefetch` | new | Overscan and prefetch hint in the scroll direction. |
| HOT runs | new | The visible window is pinned against eviction. |
| `ItemResidency.pinResidency`, `unpinResidency` | new | Keep an open editor row, a drag source or a sticky header resident outside the viewport. |
| `ItemLanes.setRange`, `setPattern`, `countRange`, `nextSet` | new | Check-all, select every k-th row, exact selection count, and selection enumeration. |
| `ItemTreeShape.expandToDepth`, `setExpanded`, `keyRuns` | new | Expand-to-depth as an O(1) rule; row-to-key runs for the viewport. |

## 15. Scope

| Family | Owner (adopts later) | Mapping |
|---|---|---|
| SWT Table, Tree, TabFolder, CTabFolder | Table/Tree laziness agent (win32 `Table.java`, `Tree.java`, `TabFolder.java`, `CTabFolder.java`, `VirtualPageSource`) | `VirtualItemStorage` state masks become lanes; `SetData` becomes `DataSource.request` |
| SWT List, Combo, CCombo | List/Combo agent | text payload and lanes, no handles |
| Nebula grid, xviewer, ctree, combos | Nebula agents | `GridItem` per-cell arrays become payload strides |
| Swing JTree, JTable, JList, SwingX, AWT List | Swing/AWT agent (TARGET_OWNED_PORT, GPLv2+CPE) | package-rename recipe of the core, plus a `TreeModel`/`TableModel`/`ListModel` adapter |
| Other M3jdk APIs | M3 compiler | `@M3Transient` types use the same sidecar |

## 16. Adoption guide

1. Keep the public API and behaviour. Add `int residencyFlags` and one `ItemResidency residency` per widget.
2. Move per-item booleans to lanes (`getChecked` becomes `residency.bit(LANE_CHECKED, index)`). Use
   `setRange` and `setPattern` for check-all, select-all and every-k-th.
3. Move payload to `setText`/`getText`, `setInt`/`getInt`, `setCellInt`/`getCellInt` and `setBlob`/`getBlob`.
   Move `setData` to the data lanes.
4. Route `SWT.SetData` through `DataSource.request` with `beginData` and `endData`. Let the engine decide what
   to request. Never fire SetData from paint.
5. Call `setViewport` (flat) or `setViewportRuns` (trees, via `ItemTreeShape.keyRuns`) from scroll and resize.
6. **Paint only through the paint path (section 9)** and assert `materializedDuringPaint() == 0` in tests.
7. Return `handle(index)` from public getters, or `transientHandle(index, siteKey)` where a rewritten call
   site passes its key.
8. Grow every widget-side array through `PackedHeapGrowth.capacity`.
9. Add your rows to `REPRODUCTION_STATE.tsv` and add a round trip plus a `Control.print` hash test.

### Worked example: a copy of the GTK Tree residency

Before (SWT fork `a02236c`, `gtk/Tree.java` and `gtk/TreeItem.java`):

```java
boolean checkData (TreeItem item) {
	if ((style & SWT.VIRTUAL) != 0 && virtualFlag (item, VirtualItemState.CACHED)) return true;
	pinVirtualFacade (item);                       // every filled item is pinned
	virtualFlag (item, VirtualItemState.CACHED, true);
	Event event = new Event (); event.item = item; ...
	sendEvent (SWT.SetData, event);
}
boolean isCheckedState () { return parent.virtualFlag (this, VirtualItemState.CHECKED); }
```

After (illustrative copy, not applied to `Tree.java`):

```java
ItemTreeShape shape = new ItemTreeShape (fanoutFromSource ());
ItemSwapSpill spill = ItemSwapSpill.temporaryOrNull (residencyFlags, 8 << 20); // desktop only
ItemResidency residency = new ItemResidency (residencyFlags, columnCount (), 4096,
	(owner, from, to) -> { for (int k = from; k < to; k++) { owner.beginData (k); fireSetData (k); owner.endData (k); } },
	spill, ItemSwapPressure.system (spill));
boolean isCheckedState (int key) { return residency.bit (ItemResidency.LANE_CHECKED, key); }
boolean getExpanded (int key) { return shape.isExpanded (key); }
TreeItem _getItem (int key) { return (TreeItem) residency.handle (key); }       // pinned on API exposure
void onScroll (int topRow, int rows) {
	int n = shape.keyRuns (Math.max (0, topRow - rows / 2), 2 * rows, runs);
	residency.setViewportRuns (runs, n, n);
}
void onPaint (GC gc) { residency.beginPaint (); /* paintSlot, slotText, paintLanes only */ residency.endPaint (); }
```

## 17. Verification ledger

Executed on win32 x86_64, JDK 21.0.9, from standalone `javac` builds of the engine and tests.

**Tests**

- `Test_ItemResidency` and `Test_ItemResidencyLanes` (junit-platform-console 1.11.4): 20/20 pass.
  They cover the round trip through DESTROY and through COLD, lanes, patterns at 1B, the `ItemBitSet`
  differential against `BitSet`, the tree differential on 1,010,100 nodes, SetData negotiation, pressure,
  telemetry failure, identity, flyweights, the paint path, low-headroom growth and `FileSwapStore`.
- `Test_ItemResidencyPrint`: 1/1 pass against the SWT 3.135.0 win32 jar. The `Control.print` pixel hash is
  identical after the DESTROY and COLD round trip, with `materializedDuringPaint` = 0.

**Stress** (`-Xmx256m -XX:+UseSerialGC`, one JVM per row): [`STRESS_RESULTS.tsv`](item-residency/STRESS_RESULTS.tsv).

| Run | Items | Heap after GC | RSS | Lane bytes | Swap out / in | Swap-in p50 / p95 / p99 (us) | SetData re-fires for COLD | Reproduction |
|---|---:|---:|---:|---:|---|---|---:|---|
| flat | 1,000,000 | 14.7 MB | 196 MB | 160 KB | 38,031 / 20,033 | 10 / 34 / 111 | 0 | identical |
| flat | 10,000,000 | 14.9 MB | 225 MB | 179 KB | 38,031 / 20,033 | 10 / 26 / 65 | 0 | identical |
| flat | 100,000,000 | 15.2 MB | 203 MB | 325 KB | 38,031 / 20,033 | 8 / 19 / 48 | 0 | identical |
| flat | 1,000,000,000 | 18.9 MB | 196 MB | 1.45 MB | 38,031 / 20,033 | 7 / 16 / 47 | 0 | identical |
| tree | 1,010,100 | 3.6 MB | 49 MB | 6 KB | 10 / 10 | 58 / 1,212 / 1,212 | 0 | identical |
| tree | 101,001,000 | 6.7 MB | 50 MB | 79 KB | 10 / 10 | 49 / 1,260 / 1,260 | 0 | identical |
| tree | 1,001,001,000 | 7.2 MB | 61 MB | 651 KB | 10 / 10 | 45 / 1,107 / 1,107 | 0 | identical |

Flat run, at every scale: select-all, check-all and an every-3rd pattern over all items; viewports at the
start, middle and end; then a 200,000-row scroll region where every 10th row is caller-edited. That gives
19,995 rows COLD and 343,006 destroyed. The scroll-back produced 20,014 timed swap-ins.

- Heap after GC stays between 14.7 and 18.9 MB from 1M to 1B items.
- The swap file is 5.1 MB.
- `getSelectionCount`-style `countRange` is exact: 1,000,000,000 selected and 333,333,334 every-3rd at 1B.

Tree run, at each scale: `expandToDepth(2)` is O(1); then the middle and end viewports; collapse one node
(1,001,001,000 to 1,000,000,000 rows at 1B); select-all; check every 3rd (333,667,000, exact).
Collapse-all except one path leaves 3,000 rows and 2 path nodes. The tree p95 comes from 10 swap-ins only.

RSS includes the 256 MB `-Xms` reservation as touched, so compare heap after GC across scales.

Tests still to run after the interview pause (relicensed headers, pin methods, crate and rename replays) are listed in [`TESTS_TO_RUN.md`](item-residency/TESTS_TO_RUN.md).

**NOT_RUN:** GTK, Cocoa, GWT compilation, RAP, the Synexia JNI SQLite backend, any widget adoption, and the
OSGi test bundle (the tests ran on a flat classpath).
