<!-- SPDX-License-Identifier: EPL-2.0 -->
# SWT canonical recipe receiving: actual SDK execution gate

At `hsoliwal/eclipse.platform.swt@8a89f2e6f3644572417dcaddc50ece5e49bf247a`, the public-fork
`M3BootstrapInventoryRecipe` and `M3BootstrapTaskRecipe` have the
same original Git blobs `8f1018a1bcfa203420253d59a8473ef5a18e19b6`
and `cfb9fb8befe2c5ccde5035e96ae7c99b89b5f490` as the canonical
Synexia first-party implementations imported in
[hsoliwal/com.synexia#12184](https://github.com/hsoliwal/com.synexia/pull/12184).

The new `M3BootstrapCrossForkExecutionTest` executes the actual
OpenRewrite Java21 SDK `Recipe.run` on a synthetic source
and a second fixed-point pass, asserting **zero source changes**. The
task-crate wrapper verifies an exact SHA-256 and refuses mutated inputs.
The GitHub workflow asserts unchanged SWT widget sources. This is an
**inventory** and **proof** recipe, not a widget transformation; no new
target optimization, JNI parity or UI behavior is claimed.

The source remains under the public fork's existing EPL/OpenRewrite
build until that dependency bridge is qualified; canonical reusable
source custody stays in private Synexia. Do not relicense EPL/JDK material
or rebalance SWT source merely to satisfy a recipe count.
Other SWT viewport recipes require independent GTK/native/UI proof.
