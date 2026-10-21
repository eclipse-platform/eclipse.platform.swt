<!-- SPDX-FileCopyrightText: 2026 Hitesh Soliwal and Contributors to the Synexia Project -->
<!-- SPDX-License-Identifier: Apache-2.0 OR EPL-2.0 -->

# SWT itself on lean M3 physical-slot arrays — EventTable

This is a **direct, in-place rewrite of an original Eclipse SWT source
owner**, not an M3 library adapter installed alongside SWT. The existing
`org.eclipse.swt.widgets.EventTable` is still used by real SWT widgets.
Its original `int[] types` and `Listener[] listeners` remain the only
callback backing arrays. It gains one packed primitive high-water
coordinate, not a second listener map, node graph, or event snapshot.

## Problem and source facts

In the original class `EventTable.hook` scans the entire allocated
`int[] types` backwards to locate the last live event before each
registration. Queries, event dispatch and unhook also scan the full
capacity, including unused tail slots.

The new M3-style `int highWater` is the physical position immediately
after the last nonzero event type. Registration uses it to find the next
physical slot in O(1) amortized time, except existing array growth.
Normal scans stop at `highWater`. The original physical listener
and type arrays are reused with no additional retained payload or
per-listener object.

**Compatibility:**
- Public SWT `Widget.addListener/removeListener/getListeners/notifyListeners`
  signatures and original `EventTable` method signatures unchanged.
- Event occurrence order, dynamically added listeners during a live
  dispatch, early cancellation via `event.type=SWT.None`, nested/reentrant
  add/remove and original `ExceptionStash` exception handling unchanged.
- The sign-based dispatch `level` still defers tombstone compaction
  until the enclosing listener dispatch finishes; trailing deleted
  references are cleared through original capacity.
- `SWT.None=0` remains a special internal sentinel: original
  `getListeners(0)`, `hooks(0)` and zero-type unhooks see all
  allocated zero slots, including historical null positions. The
  fast path does not change that exceptional observable behavior.
- Only a primitive `int` field is added. No JNI native signature,
  Display/SWT-thread scheduling, Widget/Item ownership, model, or
  global interner is changed. No net heap/performance improvement is
  claimed without matched upstream JVM measurements.

## Exact source/recipe custody

- Original source Git blob: `c04fd7a51dd64350540f316e14e79dcaebd9d59d`.
- Direct Eclipse product source Git blob:
  `efd31dc0d4acd1c088295e861b1d6b3a44313a43`.
- UTF-8 SHA-256: `dd4c0c31385024cfc8d652a562a07a81aefea8e8b291f6e473e1d53d6d7403ce`
  → `82201e5f512e8ff2f9f0e8f2187b59535861d2c6a8b8a7b9a9f32da2e65ab10c`.
- Existing canonical `M3HashPinnedJavaSnapshotRecipe` prepared **first**
  in `hsoliwal/M3JDK21` recipe commit
  `285fdace40b124357d603214df4f1398e6c2b0de`, dataset
  `swt-event-table-m3-20261021`, including the preimage, postimage,
  exact SHA-256 and catalogue/refusal metadata.
- Licensed under the original SWT file's **EPL-2.0**; first-party tests
  and provenance documents are **Apache-2.0 OR EPL-2.0**.
  No challenge-site solution body copied.

## Independent differential proof

`bash docs/m3/eventtable-20261021/prove.sh` loads **the original
EventTable Git blob** with `git cat-file`, renames just its internal
class identifier to `LegacyEventTable` in a private temporary source
directory, and compiles the unchanged Eclipse owner and proposed
high-water owner on Java 21 with strict warnings.

Test-only SWT/ExceptionStash API facsimiles avoid launching a native
display; **they are not replacement SWT product classes**.

`EventTableM3Differential` executes 2,000 seeded registration,
unregistration and event-dispatch operations on both original and
candidate. It compares exact `int[]` event codes, callback reference
identities, array capacity, size, hooks/typed-unhook queries and all
zero-type tombstone positions after every action. Directed suites
exercise nested dispatch, in-flight registration, self-removal,
event cancellation, typed listener identity, errors and original
exception stash rethrow/suppressed behavior in default and interpreted
(`-Xint`) JVM modes. This script also refuses an input source whose
Git blob is not the exact proposed generated output.

**Draft/hold until actually run.** The isolated test is not proof
of complete SWT compatibility. Native Win32/GTK/Cocoa event loops,
display ownership, widget disposal, event listener exceptions with
installed Eclipse handlers, Eclipse/Tycho/PDE full builds, plugin
extensions, Java/JNI/native parity, API signatures and measured
retained-memory/latency remain separate gates.

## Multi-pass Eclipse rollout

This is one source/behavioral atom. Next passes should continue
directly in the original Eclipse file owners:

- **SWT**: virtual Table/Tree/List, `VirtualItemStorage`, viewport
  rows and native paint packets backed by original M3 indexed arrays,
  offsets, masks and sparse resident facades.
- **JFace/Platform UI**: viewers, filtered/sorted selections, command
  registry and UI delta publication using original tree/row indexes.
- **JDT**: original compiler/DOM structures and one-per-generation
  M3AST projection; never reparse for each analyzer.
- **Resources/Equinox**: M3File and M3FS interned metadata,
  provider-specific freshness hints, hashed deltas, workspace jobs,
  extension/module scopes and lazy traversal.

Preserve original Eclipse interfaces, external listeners, event timing,
selection/accessibility and disposal semantics. Fix the reusable recipe
and record its qualification memory when a regression appears; never
hand-patch the generated SWT implementation.
