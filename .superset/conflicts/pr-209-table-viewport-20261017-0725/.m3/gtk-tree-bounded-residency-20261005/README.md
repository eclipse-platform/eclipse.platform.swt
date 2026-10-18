# Active GTK Tree native residency goal

The [implementation plan](../../docs/superpowers/plans/2026-10-05-swt-tree-bounded-residency.md)
and [progress record](progress.md) track the original live GTK residency goal.
The goal remains active.

Implemented: caller-owned primitive paint/ancestor planning, logical/physical
path mapping, topology/geometry invalidation, a bounded native packet and Java
semantic Item IDs. The attached GtkTreeStore still grows sibling prefixes;
live model reconciliation and the remaining callback translation are pending.

Build/source operations: exact six-file recipe application and unchanged second
application; strict eight-source common Java compilation; all 493 production GTK
Java sources compiled with the same 148 warnings as the baseline; GTK3 C object
compilation passed with `-Wall -Werror`. See [evidence.json](evidence.json) for
exact pre/post hashes and qualification state.

No runtime tests were added or run. Native/GUI behavior, full reactors, platform
parity, atom reconciliation, strict admission and merge qualification remain open.
