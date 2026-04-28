/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.canvasext;

import java.util.function.*;
import org.eclipse.swt.widgets.*;

/**
 * Interface with features necessary for external canvas handling.
 */
public interface IExternalCanvasHandler {

	Object paint(Consumer<Event> consumer, long wParam, long lParam);

	void redrawTriggered();

	void redrawTriggered(int x, int y, int width, int height, boolean all);

}
