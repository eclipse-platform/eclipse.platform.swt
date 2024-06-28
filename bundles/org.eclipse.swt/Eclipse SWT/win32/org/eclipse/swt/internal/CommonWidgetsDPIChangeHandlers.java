/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * This class is used in the win32 implementation only to support
 * adjusting widgets in the common package to DPI changes
 * <p>
 * <b>IMPORTANT:</b> This class is <em>not</em> part of the public
 * API for SWT. It is marked public only so that it can be shared
 * within the packages provided by SWT. It is not available on all
 * platforms, and should never be called from application code.
 * </p>
 * @noreference This class is not intended to be referenced by clients
 */
public class CommonWidgetsDPIChangeHandlers {

	public static void registerCommonHandlers() {
		DPIZoomChangeRegistry.registerHandler(CommonWidgetsDPIChangeHandlers::handleItemDPIChange, Item.class);
		DPIZoomChangeRegistry.registerHandler(CommonWidgetsDPIChangeHandlers::handleStyledTextDPIChange, StyledText.class);
		DPIZoomChangeRegistry.registerHandler(CommonWidgetsDPIChangeHandlers::handleCComboDPIChange, CCombo.class);
	}

	private static void handleItemDPIChange(Widget widget, int newZoom, float scalingFactor) {
		if (!(widget instanceof Item item)) {
			return;
		}
		// Refresh the image
		Image image = item.getImage();
		if (image != null) {
			item.setImage(image);
		}
	}

	private static void handleStyledTextDPIChange(Widget widget, int newZoom, float scalingFactor) {
		if (!(widget instanceof StyledText styledText)) {
			return;
		}


		StyledText.updateAndRefreshCarets(styledText, caretToRefresh -> {
			DPIZoomChangeRegistry.applyChange(caretToRefresh, newZoom, scalingFactor);
			Caret.win32_setHeight(caretToRefresh, styledText.getLineHeight());
		});
	}
	private static void handleCComboDPIChange(Widget widget, int newZoom, float scalingFactor) {
		if (!(widget instanceof CCombo combo)) {
			return;
		}
		CCombo.updateAndRefreshChildren(combo, childWidget -> DPIZoomChangeRegistry.applyChange(childWidget, newZoom, scalingFactor));
	}
}
