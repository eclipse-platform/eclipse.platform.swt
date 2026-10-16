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
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;

/**
 * Original Tree-owned transient input admission, not an alternative widget,
 * item state, renderer, callback scheduler or native mouse dispatcher.
 *
 * <p>A checked/disclosure/header hit never admits the generic row Selection
 * after MouseUp; it keeps its own existing item/expand notification path.</p>
 */
final class TreePointerSelection {
    private long generation;
    private boolean rowPressed;

    void begin() {
        generation++;
        rowPressed = false;
    }

    void rowPressed(int button, long expectedGeneration) {
        // Stale outer MouseDown may resume after a nested newer press.
        if (button == 1 && expectedGeneration == generation) rowPressed = true;
    }

    long generation() {
        return generation;
    }

    boolean consume(int stateMask, long expectedGeneration) {
        if (expectedGeneration != generation) return false;
        boolean selection = rowPressed && (stateMask & SWT.BUTTON1) != 0;
        rowPressed = false;
        return selection;
    }
}
