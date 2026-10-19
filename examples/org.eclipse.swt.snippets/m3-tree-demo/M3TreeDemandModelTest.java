/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.util.HashSet;
import java.util.Set;

/** Headless Java 21 executable regression; no SWT display or JNI dependency. */
public final class M3TreeDemandModelTest {
    private static long checks;
    private static void check(boolean result, String why) {
        checks++;
        if (!result) throw new AssertionError(why);
    }
    private static void failure(Class<? extends Throwable> type, Runnable body) {
        try { body.run(); throw new AssertionError("expected " + type.getName()); }
        catch (Throwable cause) {
            if (!type.isInstance(cause)) throw cause;
            checks++;
        }
    }
    public static void main(String[] args) {
        check(M3TreeDemandModel.LOGICAL_NODES == 394764L, "complete logical geometry");
        check(M3TreeDemandModel.PAGE_SIZE == 24, "bounded physical page");
        Set<M3TreeDemandModel.Key> keys = new HashSet<>();
        for (int group = 0; group < M3TreeDemandModel.GROUPS; group++) {
            var parent = M3TreeDemandModel.root(group);
            check(M3TreeDemandModel.children(parent) == 128, "group child cardinality");
            for (int pack = 0; pack < M3TreeDemandModel.PACKAGES; pack++) {
                var packageKey = M3TreeDemandModel.child(parent, pack);
                check(keys.add(packageKey), "unique package address");
                check(M3TreeDemandModel.children(packageKey) == 256, "package child cardinality");
                for (int file = 0; file < M3TreeDemandModel.FILES; file++) {
                    var leaf = M3TreeDemandModel.child(packageKey, file);
                    check(keys.add(leaf), "unique file address");
                    check(M3TreeDemandModel.children(leaf) == 0, "leaf terminal");
                    check(M3TreeDemandModel.describe(leaf).address().equals(
                            group + "/" + pack + "/" + file), "stable address");
                }
            }
        }
        check(keys.size() == 12 * 128 * 257, "all nodes unique");
        var root = M3TreeDemandModel.root(3);
        failure(IndexOutOfBoundsException.class, () -> M3TreeDemandModel.child(root, -1));
        failure(IndexOutOfBoundsException.class, () -> M3TreeDemandModel.child(root, 128));
        var pkg = M3TreeDemandModel.child(root, 127);
        failure(IndexOutOfBoundsException.class, () -> M3TreeDemandModel.child(pkg, 256));
        failure(IllegalArgumentException.class, () -> new M3TreeDemandModel.Key(0, -1, 0));
        failure(IllegalArgumentException.class, () -> M3TreeDemandModel.root(12));

        var mediator = new M3TreeDemandModel.Mediator();
        var first = M3TreeDemandModel.root(0);
        var second = M3TreeDemandModel.child(first, 10);
        mediator.deliver(first, 0, (key, index) -> {
            check(mediator.depth() == 1 && mediator.current().equals(first), "outer callback context");
            mediator.deliver(second, 10, (other, at) -> {
                check(at == 10 && mediator.depth() == 2, "nested callback receives correct index");
                check(mediator.current().equals(second), "nested callback context");
                failure(IllegalStateException.class,
                        () -> mediator.deliver(first, 0, (unused, i) -> {}));
            });
            check(mediator.current().equals(first) && mediator.depth() == 1, "outer context restored");
        });
        check(mediator.depth() == 0 && mediator.current() == null, "completion restores context");
        check(mediator.completed() == 2 && mediator.refused() == 1, "exact callback counts");
        failure(IllegalStateException.class,
                () -> mediator.deliver(first, 0, (key, at) -> { throw new IllegalStateException("provider"); }));
        check(mediator.depth() == 0, "provider error releases in-flight state");
        mediator.deliver(first, 0, (key, at) -> check(at == 0, "retry works after exception"));
        check(mediator.completed() == 3, "only completed deliveries counted");
        failure(IndexOutOfBoundsException.class,
                () -> mediator.deliver(first, -1, (key, at) -> {}));
        System.out.println("M3_NONVIRTUAL_TREE_MODEL_PASS checks=" + checks
                + " logicalNodes=" + M3TreeDemandModel.LOGICAL_NODES
                + " setDataCalls=" + mediator.completed());
    }
}
