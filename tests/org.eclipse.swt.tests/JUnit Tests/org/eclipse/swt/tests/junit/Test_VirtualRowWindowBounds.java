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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;

import org.junit.jupiter.api.Test;

/**
 * Original SWT flat native viewport window: reject integer-unrepresentable
 * insert/remove intervals before modifying the committed resident window.
 * See canonical OpenRewrite candidate hsoliwal/com.synexia#12083.
 *
 * This test is an intentionally red regression on its frozen pre-fix source:
 * do not merge it without the recipe-generated production candidate.
 */
public class Test_VirtualRowWindowBounds {

    private static Object window() {
        try {
            Constructor<?> constructor = Class.forName("org.eclipse.swt.widgets.VirtualRowWindowPlan")
                    .getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (ReflectiveOperationException failure) {
            throw new AssertionError(failure);
        }
    }

    private static int call(Object receiver, String method, int... arguments) {
        Class<?>[] types = new Class<?>[arguments.length];
        java.util.Arrays.fill(types, int.class);
        try {
            Method selected = receiver.getClass().getDeclaredMethod(method, types);
            selected.setAccessible(true);
            Object result = selected.invoke(receiver, java.util.Arrays.stream(arguments).boxed().toArray());
            return result instanceof Integer integer ? integer : 0;
        } catch (InvocationTargetException failure) {
            if (failure.getCause() instanceof RuntimeException runtime) throw runtime;
            throw new AssertionError(failure.getCause());
        } catch (ReflectiveOperationException failure) {
            throw new AssertionError(failure);
        }
    }

    private static Object at(int first, int length) {
        Object result = window();
        call(result, "plan", first, length);
        call(result, "commit");
        return result;
    }

    private static void assertResident(Object resident, int first, int length) {
        assertEquals(first, call(resident, "first"));
        assertEquals(length, call(resident, "size"));
        assertEquals(first + length, call(resident, "end"));
    }

    @Test
    public void beforeWindowOverflowCannotWrapTheCommittedEndpoint() {
        Object resident = at(Integer.MAX_VALUE - 2, 2);
        assertThrows(IllegalArgumentException.class, () -> call(resident, "inserted", 0, 1));
        assertResident(resident, Integer.MAX_VALUE - 2, 2);
    }

    @Test
    public void insideWindowOverflowCannotWrapTheCommittedEndpoint() {
        Object resident = at(Integer.MAX_VALUE - 2, 2);
        assertThrows(IllegalArgumentException.class,
                () -> call(resident, "inserted", Integer.MAX_VALUE - 2, 1));
        assertResident(resident, Integer.MAX_VALUE - 2, 2);
    }

    @Test
    public void overflowingNonresidentRemovalCannotDeleteResidentRows() {
        Object resident = at(100, 40);
        assertThrows(IllegalArgumentException.class,
                () -> call(resident, "removed", Integer.MAX_VALUE - 2, 5));
        assertResident(resident, 100, 40);
        assertEquals(0, call(resident, "removed", Integer.MAX_VALUE - 2, 2));
        assertResident(resident, 100, 40);
    }

    @Test
    public void representableEndpointAndZeroLengthOperationsStayUnchanged() {
        Object resident = at(Integer.MAX_VALUE - 3, 2);
        assertEquals(-1, call(resident, "inserted", 0, 1));
        assertResident(resident, Integer.MAX_VALUE - 2, 2);
        assertEquals(-1, call(resident, "inserted", Integer.MAX_VALUE, 0));
        assertEquals(0, call(resident, "removed", Integer.MAX_VALUE, 0));
        assertResident(resident, Integer.MAX_VALUE - 2, 2);
    }

    @Test
    public void disjointInsertsRemainNoOpsAndExistingRangeValidationStaysInForce() {
        Object resident = at(100, 40);
        assertEquals(-1, call(resident, "inserted", 500, 10));
        assertEquals(0, call(resident, "removed", 500, 10));
        assertResident(resident, 100, 40);
        assertThrows(IllegalArgumentException.class, () -> call(resident, "inserted", -1, 1));
        assertThrows(IllegalArgumentException.class, () -> call(resident, "removed", 0, -1));
        assertResident(resident, 100, 40);
    }
}
