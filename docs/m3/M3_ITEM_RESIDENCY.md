# M3 item residency: parent-owned item state, pressure-driven payload tiers

Status: draft design and engine for user review. The engine is adopted by no widget in this leaf.
Governing invariant: Synexia `docs/M3-SCALE/invariants/M3-LAZY-OBJECT-PRESSURE-1.md`
([#10889](https://github.com/hsoliwal/com.synexia/pull/10889)). Sibling realizations:
`M3LazyObjectArray` ([#10891](https://github.com/hsoliwal/com.synexia/pull/10891)) and
`M3LazyObjectSession` with `M3_LAZY_OBJECT_RESIDENCY.md`
([#10892](https://github.com/hsoliwal/com.synexia/pull/10892)). This document is the widget-specific
realization: handles, always-resident bit lanes, the reproduction contract and identity pinning.
The compile-time half (escape metadata per call site) lives in M3jdk21 `m3/docs/m3-item-residency-metadata.md`.

Owners added by this leaf (package-private, `org.eclipse.swt.widgets`, `Eclipse SWT/common`):

| Owner | Purpose | Web (GWT/RAP) |
|---|---|---|
| `ItemResidency` | bit lanes, payload slots, tiers, CLOCK eviction, pressure arithmetic, identity | yes (int-only) |
| `ItemSpillStore` | COLD tier: file-backed spill with power-of-two block free lists | no (desktop only) |
| `ItemResidencyPressure` | heap / `MemoryPoolMXBean` pressure source | no (desktop only) |

Donor decisions: [`item-residency/FOSS_REUSE_DECISION.tsv`](item-residency/FOSS_REUSE_DECISION.tsv).
Reproduction coverage: [`item-residency/REPRODUCTION_STATE.tsv`](item-residency/REPRODUCTION_STATE.tsv).

## 1. The handle

An Item, node or row object is a thin handle: `parent + item index + generation`. All state lives
in the parent's packed `int[]` lanes and payload arrays. Calling an Item method mutates the parent's
bits and arrays; the handle holds no state of its own. "Half the time the user wants just the
behaviour": a handle obtained only to call `getText()` or `setChecked()` need not exist after the call.

## 2. Always-resident bit lanes

Hot state is one bit per item per lane. Lanes are never swapped and never destroyed. Only payloads
(text, images, colours, fonts, data) go to the COLD or DESTROYED tiers. Destroy-and-recreate restores
every lane bit exactly, because lanes are never part of the payload.

| Lane | Name | Meaning | Default |
|---:|---|---|---|
| 0 | `LANE_EXPANDED` | tree node expanded | 0 |
| 1 | `LANE_HAS_CHILDREN` | node has children (valid when lane 2 set) | 0 |
| 2 | `LANE_CHILDREN_KNOWN` | has-children is known (0 = unknown) | 0 |
| 3 | `LANE_CHILDREN_LOADED` | children materialized from the source | 0 |
| 4 | `LANE_LOADING_PLACEHOLDER` | a loading placeholder row is shown | 0 |
| 5 | `LANE_SELECTED` | selected | 0 |
| 6 | `LANE_FOCUSED` | focus item | 0 |
| 7 | `LANE_HOVERED` | hot (mouse-over) item | 0 |
| 8 | `LANE_DISABLED` | enabled, stored inverted so the zero word is the default | 0 = enabled |
| 9 | `LANE_FILTERED_OUT` | filtered-visible, stored inverted | 0 = visible |
| 10 | `LANE_CHECKED` | check box checked | 0 |
| 11 | `LANE_GRAYED` | check box grayed | 0 |
| 12 | `LANE_CHECKABLE` | row shows a check box | 0 |
| 13 | `LANE_DIRTY_PAINT` | needs repaint | 0 |
| 14 | `LANE_DIRTY_MEASURE` | needs re-measure (set on recreate) | 0 |
| 15 | `LANE_DIRTY_DATA` | payload must be refetched (`clear(index)`) | 0 |
| 16 | `LANE_PINNED` | a pinned handle exists (pinned-identity) | 0 |
| 17-18 | `LANE_TIER_LO/HI` | resident tier, 2 bits (section 4) | 00 = DESTROYED |
| 19 | `LANE_CALLER_SET` | payload written by API outside the data-source fill | 0 |
| 20 | `LANE_MATERIALIZED` | payload was supplied at least once (`VirtualItemState.CACHED`) | 0 |

Names follow `VirtualItemState` (`CHECKED`, `GRAYED`, `EXPANDED`, `PINNED`, `DIRTY`, `CHILDREN_*`), so the
Table/Tree owners map 1:1.

### Layout: one `int[]` per lane, chosen by measurement

`int[][] lanes`, word `item >>> 5`, bit `item & 31`. Measured with
[`bench/LaneLayoutBench.java`](item-residency/bench/LaneLayoutBench.java), 10,000,000 items, 20 lanes,
JDK 21.0.9, `-Xmx1g`, median of 7 after 3 warm-ups, two JVM runs in opposite layout order
([`bench/LANE_LAYOUT_BENCH.tsv`](item-residency/bench/LANE_LAYOUT_BENCH.tsv)):

| Layout | Bytes | Range set+clear 8M (ms) | Count 10M (ms) | Gather 20k windows x 64 rows (ms) | 1M random set+get (ms) |
|---|---:|---|---|---|---|
| A: `long[]` per lane | 25,000,000 | 0.534 / 0.223 | 0.699 / 0.201 | 66.2 / 135.9 | 1.4 / 10.5 |
| **B: `int[]` per lane** | 25,000,000 | **0.087 / 0.117** | 0.241 / 0.236 | 90.8 / 87.1 | 5.8 / 15.2 |
| C: interleaved `int` blocks (stride 20) | 25,000,000 | 4.273 / 2.135 | 1.938 / 1.125 | 27.8 / 32.8 | 51.2 / 20.3 |
| D: one packed `int` per item | 40,000,000 | 5.558 / 4.624 | 3.067 / 3.562 | 9.4 / 1.8 | 52.7 / 15.0 |

Why B: it has the best range operations in both runs (select-all, clear-dirty, filter, count-checked
run at memory bandwidth), random single-lane access in the A/B class, the same 2.5 bytes per item as A
and C, and `int` words (GWT emulates `long`). Its cost is the full-row gather: about 70 ns per row, or
14 microseconds for a 200-row paint. Interleaved blocks (C) gather 3x faster but lose 18-37x on range
operations; D needs 60% more memory. Caveat recorded: one JVM per run with a shared interface call site,
so later layouts in each run see a megamorphic site; the B-over-C range result holds in both orders.

## 3. Payload record and parent scalars

Payload lives in resident slots (arrays sized by slot capacity, never by item count):

| Array | Stride | Fields |
|---|---|---|
| `slotInts` | `ITEM_INTS + columns * CELL_INTS` | `I_FOREGROUND`, `I_BACKGROUND` (RGBA `0xAARRGGBB`, 0 = default), `I_FONT`, `I_IMAGE` (widget-table ids, 0 = none), `I_ROW_HEIGHT` (measured, 0 = unmeasured), `I_INDENT`, `I_LEVEL`, `I_TOOLTIP`, `I_ACCESSIBLE` (ids), `I_DATA` (data-table handle), `I_USER` (adopter-defined); per cell `C_FOREGROUND`, `C_BACKGROUND`, `C_FONT`, `C_IMAGE` |
| `slotText` | `columns` | cell text; column 0 is the item text |
| `slotBlob` | 1 | opaque bytes (thumbnail or image bytes, adopter-encoded payload) |
| `slotItem`, `slotRecord` | 1 | owning item; clean spill copy (-1 = none) |

Parent scalars (always resident): the in-place editor (`editorItem`, `editorColumn`, `editorText`,
`editorCaret`, `editorSelectionStart`, `editorSelectionEnd`; `editorItem < 0` = closed) and the custom-data
table (`Object[] data` plus an int free stack). Item-to-payload directory: paged `int[][]` (4096 per page,
pages allocated on demand and freed when empty); `> 0` = resident slot + 1, `< 0` = -(spill record + 1),
`0` = none.

## 4. Residency tiers

Two lane bits per item. Names are those of `TieredSwapCache.Tier` and M3-LAZY-OBJECT-PRESSURE-1, plus
the user's SWAPPED and DESTROYED:

| Code | Tier | Payload location | Peer equivalent |
|---:|---|---|---|
| 0 | DESTROYED | nowhere; recreated by `DataSource.fill` (SWT `SetData`) on next access | first miss: factory |
| 1 | HOT | resident slot, inside viewport + overscan; never evicted | HOT (pinned lease) |
| 2 | WARM | resident slot outside the window; CLOCK eviction candidate | WARM |
| 3 | COLD (SWAPPED) | spill record in secondary memory; swapped back in on access | NONRESIDENT + `SwapStore` record |

Zero is DESTROYED, so the zero word means "never materialized" for 10M fresh items.

Eviction of a WARM slot (CLOCK second chance; the hand skips window slots and the slot being filled):

1. Source-backed payload (`LANE_CALLER_SET` = 0) with `RESIDENCY_DESTROY`: drop it (DESTROYED). Recreation
   re-runs the fill and sets `LANE_DIRTY_MEASURE`.
2. Otherwise, with `RESIDENCY_SWAP` and a spill: encode into the reusable buffer and write a record
   (COLD). If the slot still holds a clean copy (`slotRecord >= 0`, payload unchanged since swap-in), the
   write is skipped.
3. Otherwise the payload stays resident. Caller-owned payload is never discarded (M3-LAZY-OBJECT-PRESSURE-1);
   capacity may then exceed the budget, and `STAT_UNEVICTABLE` counts the refusals.

Access to a COLD or DESTROYED item from any getter or setter pages it in invisibly. Residency is driven
by `setViewport(first, visibleCount, overscan)` and by pressure, never by the caller.

## 5. Pressure (identical semantics to `SwapPressureController` / `SwapBudgetPlane`)

- Availability ratio in permille: 1000 at heap used <= 70% (`softStart` 0.70), 0 at >= 96% (`hardStop`
  0.96), linear between. `ItemResidencyPressure` also reads every heap `MemoryPoolMXBean` with a usage
  threshold and arms collection-usage notifications at `softStart`; the worst signal wins.
- Shrink is immediate; recovery is rate-limited to +125 permille per sample (`recoveryStep` 0.125).
- A telemetry exception zeroes optional residency (budget falls to the window), counted in `STAT_TELEMETRY_FAILURES`.
- Sampled on the first operation and every `sampleEvery` operations after it; the MXBean notification
  marks the next operation as a sample point.
- Budget slots = `max(window size, hardSlots * permille / 1000)`, computed without int overflow.
  The window is the floor: it must stay resident to paint.

## 6. Reproduction contract (normative)

Every piece of state that affects how an item looks or behaves must be reconstructible from
parent-owned packed state plus the data source, so an item scrolled out and destroyed comes back
identical when scrolled in. Nothing may live only in the transient item object. Classes of state:

| Class | Where | Survives DESTROY because |
|---|---|---|
| bit lanes 0-20 | `lanes` | lanes are never evicted |
| source-backed payload | slot arrays | `DataSource.fill` regenerates it deterministically |
| caller-owned payload (`LANE_CALLER_SET`) | slot arrays / spill | never destroyed: only COLD |
| editor state | parent scalars | never evicted |
| custom data objects | parent data table + `I_DATA` | `setData` is caller-owned, so the item is never destroyed |
| pinned handles | sorted pin table | identity table is never evicted |

[`REPRODUCTION_STATE.tsv`](item-residency/REPRODUCTION_STATE.tsv) lists every state with its lane or array,
owner and the test that proves its round trip. Widget owners add their rows (for example the native
`LVITEM` state on win32) and must point each at a test.
Round-trip test (`Test_ItemResidency`): set every state, force items out (scroll away plus zero pressure,
DESTROY and SWAP), scroll back, require identical values from every getter. `Test_ItemResidencyPrint` paints
a `Canvas` from engine state and requires an identical `Control.print` pixel hash before and after.

## 7. Identity

- **Pinned:** the handle object stays the same object while any caller may retain it (`==`, fields,
  collections, map keys, monitors). Pinned handles live in a sorted `int[]`/`Object[]` table keyed by item
  index; insert/remove re-key them and call `HandleFactory.rebind` so the handle follows its item.
- **Recyclable:** a small flyweight ring (4 handles) rebound per request with a new generation, used only
  when `IDENTITY_BY_METADATA` is set and the compile-time sidecar classifies the call site as
  `SITE_BEHAVIOUR_ONLY`. `isCurrent(handle, item, generation)` detects stale flyweights.
- **Default is safe:** unknown metadata, `SITE_RETAINS`, `SITE_UNKNOWN`, `IDENTITY_PIN_ALL` or a missing
  sidecar all pin. An already pinned item always returns its pinned handle.
- Site key: FNV-1a 32 over the UTF-16 units of `owner.name+desc@bci`, written by the M3 pass. Key
  collisions merge conservatively (any retaining or unknown site makes the key retaining).
- Sidecar wire form (`M3IRS01`, big-endian ints): `magic 0x4D334952`, `version 1`, `count`, then sorted
  `(key, kind)` pairs. `ItemResidency.Sidecar.fromInts(int[])` reads it; file I/O stays with the adopter.

## 8. Construction flags

The flags are one engine-owned `int config`. They use no `Widget.state` bit (win32 bits 25-31 were catalogued
free in Synexia #10864 and are claimed by the visibility agent) and no SWT style bit. Adopters keep one
package-private `int residencyFlags` per widget, passed to the engine constructor; the default comes from
system property `org.eclipse.swt.m3.itemResidency` (decimal int, default `IDENTITY_PIN_ALL`, which is stock behaviour).

| Bit | Flag | Effect |
|---:|---|---|
| 0 | `RESIDENCY_SWAP` | caller-owned or non-destroyable payload may go COLD (needs a spill) |
| 1 | `RESIDENCY_DESTROY` | source-backed payload is dropped and refilled |
| 2 | `IDENTITY_PIN_ALL` | every handle is pinned (stock SWT identity) |
| 3 | `IDENTITY_BY_METADATA` | recycle handles at sites the sidecar proves behaviour-only |
| 4 | `PRESSURE_SAMPLED` | sample the pressure source on the operation cadence |
| 5-31 | reserved | 0 |

## 9. Web and GWT safety

`ItemResidency` uses only `int`, `int[]`, `String[]`, `Object[]`: no `long` arithmetic, no reliance on
int overflow (multiplications are split, counters saturate), no `String[].clone()`, no
`Objects.checkIndex`, no `java.nio`, no `java.lang.management`, no threads. `ItemSpillStore` and
`ItemResidencyPressure` are desktop-only and are excluded from the web module; the web configuration is
`RESIDENCY_DESTROY` without a spill, so caller-owned payload stays resident there.

## 10. Scope

| Family | Owner (adopts later) | Mapping |
|---|---|---|
| SWT Table, Tree, TabFolder, CTabFolder | Table/Tree laziness agent (win32 `Table.java`, `Tree.java`, `TabFolder.java`, `CTabFolder.java`, `VirtualPageSource`) | `VirtualItemStorage.stateMasks` and `VirtualTreeTopology` flags become lanes; `SetData` becomes `DataSource.fill` |
| SWT List, Combo, CCombo | List/Combo agent | text lane only; no handles (List has no items) |
| Nebula grid, xviewer, ctree, combos | Nebula agents | `GridItem` per-cell arrays become payload strides; row spans use `I_USER` |
| Swing JTree, JTable, JList (M3jdk21 `java.desktop`) | TARGET_OWNED_PORT, GPLv2+CPE; design only | renderers already stamp rows; `TreePath` and selection objects become lanes plus recyclable handles |
| Other M3jdk APIs | M3 compiler | any `@M3Transient` type uses the same sidecar |

## 11. Adoption guide

1. Keep public API and behaviour. Add `int residencyFlags` and one `ItemResidency residency` field per widget.
2. Move each per-item boolean to a lane (`getChecked` reads `residency.bit(LANE_CHECKED, index)`).
3. Move per-item payload to `setText/getText`, `setInt/getInt`, `setCellInt/getCellInt`, `setBlob/getBlob`.
4. Route `SWT.SetData` through `DataSource.fill`; writes made inside the fill are source-backed, all other
   writes are caller-owned.
5. Call `setViewport` from the scroll and resize paths with the overscan you paint.
6. Return `handle(index)` from public getters, or `transientHandle(index, siteKey)` where a rewritten
   call site passes its key.
7. Add your rows to `REPRODUCTION_STATE.tsv` and extend `Test_ItemResidency`-style round trips to your widget.

### Worked example: a copy of the GTK Tree residency

Before (SWT fork `a02236c`, `gtk/Tree.java` and `gtk/TreeItem.java`):

```java
boolean virtualFlag (TreeItem item, long flag) {
	if (virtualTopology == null) return false;
	return virtualTopology.flag (virtualItemId (item), flag);
}
boolean checkData (TreeItem item) {
	if ((style & SWT.VIRTUAL) != 0 && virtualFlag (item, VirtualItemState.CACHED)) return true;
	...
	pinVirtualFacade (item);                       // every filled item is pinned
	virtualFlag (item, VirtualItemState.CACHED, true);
	Event event = new Event (); event.item = item; ...
	sendEvent (SWT.SetData, event);
}
boolean isCheckedState () { return parent.virtualFlag (this, VirtualItemState.CHECKED); }
```

After (illustrative copy, not applied to `Tree.java`):

```java
ItemResidency residency = new ItemResidency (residencyFlags, columnCount (), 4096,
	(owner, index) -> fireSetData (index),          // SetData = DataSource.fill
	ItemSpillStore.temporaryOrNull (residencyFlags), // desktop only
	ItemResidencyPressure.system ());
boolean checkData (int index) {
	residency.getText (index, 0);                   // pages in or fills; no pinning
	return true;
}
boolean isCheckedState (int index) { return residency.bit (ItemResidency.LANE_CHECKED, index); }
TreeItem _getItem (int index) { return (TreeItem) residency.handle (index); } // pinned: API exposure
void onScroll (int top, int visible) { residency.setViewport (top, visible, visible / 2); }
```

`virtualTopology` keeps the parent/child shape; `LANE_EXPANDED`, `LANE_HAS_CHILDREN` and
`LANE_CHILDREN_KNOWN` replace its `EXPANDED`, `HAS_CHILDREN` and `CHILDREN_KNOWN` flags by flat row index.

## 12. Verification ledger

Filled from executed runs only; see the PR body. NOT_RUN: GTK, Cocoa, GWT compilation, RAP.
