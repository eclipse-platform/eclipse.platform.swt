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

import org.eclipse.swt.SWT;
import org.junit.jupiter.api.Test;

/** Regression for original Tree's transient, UI-thread-owned pointer selection. */
public final class Test_org_eclipse_swt_widgets_TreePointerSelection {
    @Test public void testOrdinaryRowEmitsSelectionOnce() throws Exception {
        Fixture gesture = new Fixture();
        gesture.begin();
        gesture.rowPressed(1);
        long epoch = gesture.generation();
        assertTrue(gesture.consume(SWT.BUTTON1, epoch));
        assertFalse(gesture.consume(SWT.BUTTON1, epoch));
    }

    @Test public void testDisclosureCheckHeaderAndEmptyNeverEmitGenericSelection() throws Exception {
        Fixture gesture = new Fixture();
        for (int i = 0; i < 8; i++) {
            gesture.begin();
            assertFalse(gesture.consume(SWT.BUTTON1, gesture.generation()));
        }
    }

    @Test public void testRightButtonAndMissingPressedMaskNeverEmitSelection() throws Exception {
        Fixture gesture = new Fixture();
        gesture.begin();
        gesture.rowPressed(3);
        assertFalse(gesture.consume(SWT.BUTTON1, gesture.generation()));
        gesture.begin();
        gesture.rowPressed(1);
        assertFalse(gesture.consume(0, gesture.generation()));
    }

    @Test public void testNestedReentrancyDoesNotConsumeNewerGesture() throws Exception {
        Fixture gesture = new Fixture();
        gesture.begin();
        gesture.rowPressed(1);
        long obsolete = gesture.generation();
        gesture.begin();
        gesture.rowPressed(1);
        assertFalse(gesture.consume(SWT.BUTTON1, obsolete));
        assertTrue(gesture.consume(SWT.BUTTON1, gesture.generation()));
    }

    @Test public void testNewMouseDownInvalidatesEarlierRowPress() throws Exception {
        Fixture gesture = new Fixture();
        gesture.begin();
        gesture.rowPressed(1);
        gesture.begin();
        assertFalse(gesture.consume(SWT.BUTTON1, gesture.generation()));
    }

    @Test public void testOverflowDoesNotAliasAdjacentGestureEpochs() throws Exception {
        Fixture gesture = new Fixture();
        gesture.generationField().setLong(gesture.target, Long.MAX_VALUE);
        gesture.begin();
        gesture.rowPressed(1);
        assertEquals(Long.MIN_VALUE, gesture.generation());
        assertTrue(gesture.consume(SWT.BUTTON1, Long.MIN_VALUE));
    }

    private static final class Fixture {
        final Object target;
        final Method begin;
        final Method rowPressed;
        final Method generation;
        final Method consume;
        final Field epoch;
        Fixture() throws ReflectiveOperationException {
            Class<?> type = Class.forName("org.eclipse.swt.widgets.TreePointerSelection");
            Constructor<?> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            target = ctor.newInstance();
            begin = method(type, "begin");
            rowPressed = method(type, "rowPressed", int.class);
            generation = method(type, "generation");
            consume = method(type, "consume", int.class, long.class);
            epoch = type.getDeclaredField("generation");
            epoch.setAccessible(true);
        }
        private static Method method(Class<?> type, String name, Class<?>... args)
                throws ReflectiveOperationException {
            Method result = type.getDeclaredMethod(name, args);
            result.setAccessible(true);
            return result;
        }
        Field generationField() { return epoch; }
        private Object call(Method method, Object... args) {
            try { return method.invoke(target, args); }
            catch (InvocationTargetException e) {
                if (e.getCause() instanceof RuntimeException runtime) throw runtime;
                if (e.getCause() instanceof Error error) throw error;
                throw new AssertionError(e.getCause());
            } catch (ReflectiveOperationException e) { throw new AssertionError(e); }
        }
        void begin() { call(begin); }
        void rowPressed(int button) { call(rowPressed, button); }
        long generation() { return (Long) call(generation); }
        boolean consume(int mask, long seen) { return (Boolean) call(consume, mask, seen); }
    }
}
