/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import org.junit.jupiter.api.*;

/**
 * Contract oracle for the original, package-private SWT paint graph.
 * No fake widget, display, renderer or replacement model is involved.
 */
public final class Test_org_eclipse_swt_widgets_ViewportPaintGraphDirtyWordScan {

    @Test
    public void test_liveWordBoundariesAndCallbackMutations() throws Exception {
        Fixture graph = new Fixture();
        for (int i = 0; i < 140; i++) graph.add();
        graph.allClean();
        for (int id : new int[] {0, 63, 64, 128}) graph.invalidate(id);
        List<Integer> emitted = new ArrayList<>();
        graph.forEachDirty(id -> {
            emitted.add(id);
            if (id == 0) graph.clean(64);
            if (id == 63) graph.invalidate(65);
        });
        assertEquals(List.of(0, 63, 65, 128), emitted,
                "Live later-node dirty/clean mutations must preserve original ascending visitation");
    }

    @Test
    public void test_callbackCanAppendRowsAndClearRemainingDirtyBits() throws Exception {
        Fixture growth = new Fixture();
        growth.add();
        List<Integer> seen = new ArrayList<>();
        growth.forEachDirty(id -> {
            seen.add(id);
            if (id == 0) growth.add();
        });
        assertEquals(List.of(0, 1), seen, "New later dirty nodes must be visited in the same scan");

        Fixture clear = new Fixture();
        for (int i = 0; i < 129; i++) clear.add();
        clear.forEachDirty(id -> {
            seen.add(id);
            clear.allClean();
        });
        assertEquals(3, seen.size(), "Clear-all must stop later callbacks");
        assertEquals(0, clear.dirtyCount());
    }

    @Test
    public void test_sparseOrdinalsMatchIndependentAscendingOracle() throws Exception {
        Random random = new Random(0x20261015L);
        for (int trial = 0; trial < 64; trial++) {
            Fixture graph = new Fixture();
            int n = 1 + random.nextInt(260);
            for (int i = 0; i < n; i++) graph.add();
            graph.allClean();
            List<Integer> expected = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (random.nextInt(17) == 0) {
                    graph.invalidate(i);
                    expected.add(i);
                }
            }
            List<Integer> actual = new ArrayList<>();
            graph.forEachDirty(actual::add);
            assertEquals(expected, actual, "trial=" + trial);
        }
    }

    @Test
    public void test_nullConsumerAndPrimaryExceptionRemainUnchanged() throws Exception {
        Fixture graph = new Fixture();
        graph.add();
        assertThrows(IllegalArgumentException.class, () -> graph.forEachDirty(null));
        IllegalStateException failure = new IllegalStateException("paint callback");
        assertSame(failure, assertThrows(IllegalStateException.class,
                () -> graph.forEachDirty(id -> { throw failure; })));
        assertEquals(1, graph.dirtyCount(),
                "Failing paint callbacks do not implicitly acknowledge dirty state");
    }

    @Test
    public void test_sparseOriginalOwnerHybridPreservesLaterMutations() throws Exception {
        Fixture sparse = new Fixture();
        for (int id = 0; id < 1024; id++) sparse.add();
        sparse.allClean();
        for (int id : new int[] {0, 63, 64, 255, 256, 512, 1000}) sparse.invalidate(id);
        List<Integer> calls = new ArrayList<>();
        sparse.forEachDirty(id -> {
            calls.add(id);
            if (id == 0) {
                sparse.clean(64);
                sparse.invalidate(65);
            }
        });
        assertEquals(List.of(0, 63, 65, 255, 256, 512, 1000), calls);
    }

    @Test
    public void test_denseOriginalOwnerUsesLivePerNodePath() throws Exception {
        Fixture dense = new Fixture();
        for (int id = 0; id < 1024; id++) dense.add();
        dense.clean(31);
        List<Integer> calls = new ArrayList<>();
        dense.forEachDirty(id -> {
            calls.add(id);
            if (id == 0) dense.clean(64);
        });
        assertEquals(1022, calls.size());
        assertEquals(0, calls.getFirst());
        assertEquals(1023, calls.getLast());
        assertFalse(calls.contains(31));
        assertFalse(calls.contains(64));
    }

    @Test
    public void test_nestedDirtyTraversalObservesLiveLaterRows() throws Exception {
        Fixture nested = new Fixture();
        for (int id = 0; id < 320; id++) nested.add();
        nested.allClean();
        nested.invalidate(0);
        nested.invalidate(64);
        List<Integer> outer = new ArrayList<>(), inner = new ArrayList<>();
        nested.forEachDirty(id -> {
            outer.add(id);
            if (id == 0) nested.forEachDirty(child -> {
                inner.add(child);
                if (child == 0) {
                    nested.clean(64);
                    nested.invalidate(65);
                }
            });
        });
        assertEquals(List.of(0, 65), inner);
        assertEquals(List.of(0, 65), outer);
    }

    @Test
    public void test_locallyDenseWordAndGloballySparseCallbacks() throws Exception {
        Fixture lanes = new Fixture();
        for (int id = 0; id < 1024; id++) lanes.add();
        lanes.allClean();
        for (int id = 0; id < 64; id++) lanes.invalidate(id);
        List<Integer> calls = new ArrayList<>();
        lanes.forEachDirty(id -> {
            calls.add(id);
            if (id == 0) {
                lanes.clean(31);
                lanes.invalidate(1000);
            }
        });
        assertEquals(64, calls.size());
        assertFalse(calls.contains(31));
        assertEquals(1000, calls.getLast());
    }

    private static final class Fixture {
        private final Object graph;
        private final Method addNode;
        private final Method markClean;
        private final Method markAllClean;
        private final Method invalidateBounds;
        private final Method forEachDirty;
        private final Method dirtyCount;
        private final int body;

        Fixture() throws ReflectiveOperationException {
            Class<?> type = Class.forName("org.eclipse.swt.widgets.ViewportPaintGraph");
            Constructor<?> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            graph = ctor.newInstance();
            addNode = method(type, "addNode", long.class, int.class, long.class,
                    long.class, long.class, long.class);
            markClean = method(type, "markClean", int.class);
            markAllClean = method(type, "markAllClean");
            invalidateBounds = method(type, "invalidateBounds", long.class, long.class,
                    long.class, long.class, int.class);
            forEachDirty = method(type, "forEachDirty", IntConsumer.class);
            dirtyCount = method(type, "dirtyCount");
            Class<?> layers = Class.forName("org.eclipse.swt.widgets.ViewportLayerState");
            Field constant = layers.getDeclaredField("BODY");
            constant.setAccessible(true);
            body = constant.getInt(null);
        }

        int add() { return (Integer) invoke(addNode, 0L, body, (long)dirtyCount() * 2L, 0L, 1L, 1L); }
        void clean(int id) { invoke(markClean, id); }
        void allClean() { invoke(markAllClean); }
        void invalidate(int id) { invoke(invalidateBounds, id * 2L, 0L, 1L, 1L, body); }
        int dirtyCount() { return (Integer) invoke(dirtyCount); }
        void forEachDirty(IntConsumer action) { invoke(forEachDirty, action); }

        private Object invoke(Method method, Object... arguments) {
            try {
                return method.invoke(graph, arguments);
            } catch (InvocationTargetException failure) {
                Throwable primary = failure.getCause();
                if (primary instanceof RuntimeException exception) throw exception;
                if (primary instanceof Error error) throw error;
                throw new AssertionError(primary);
            } catch (ReflectiveOperationException failure) {
                throw new AssertionError(failure);
            }
        }

        private static Method method(Class<?> type, String name, Class<?>... args)
                throws ReflectiveOperationException {
            Method method = type.getDeclaredMethod(name, args);
            method.setAccessible(true);
            return method;
        }
    }
}
