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

import java.util.*;
import java.util.Map.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class DPIZoomChangeRegistry {

	private static Map<Class<? extends Widget>, DPIZoomChangeHandler> dpiZoomChangeHandlers =  new TreeMap<>(
			(o1, o2) -> {
	            if(o1.isAssignableFrom(o2)) {
	            	return -1;
	            }
	            if(o2.isAssignableFrom(o1)) {
	            	return 1;
	            }
	            return o1.getName().compareTo(o2.getName());
	        });

	/**
	 * Calling this method will propagate the zoom change to all registered handlers that are responsible for the
	 * class of the provided widget or one of its super classes or interfaces. Usually there will be multiple handlers
	 * called per widget. To have a reliable and consistent execution order, the handler responsible for the most
	 * general class in the class hierarchy is called first, e.g. if a {@code Composite} is updated, the handlers are
	 * executed like ({@code Widget} -> {@code Control} -> {@code Scrollable} -> {@code Composite}). Each handler
	 * should only take care to update the attributes the class, it is registered for, adds to the hierarchy.
	 *
	 * @param widget widget the zoom change shall be applied to
	 * @param newZoom zoom in % of the standard resolution to be applied to the widget
	 * @param scalingFactor factor as division between new zoom and old zoom, e.g. 1.5 for a scaling from 100% to 150%
	 */
	public static void applyChange(Widget widget, int newZoom, float scalingFactor) {
		if (widget == null) {
			return;
		}
		for (Entry<Class<? extends Widget>, DPIZoomChangeHandler> entry : dpiZoomChangeHandlers.entrySet()) {
			Class<? extends Widget> clazz = entry.getKey();
			DPIZoomChangeHandler handler = entry.getValue();
			if (clazz.isInstance(widget)) {
				handler.handleDPIChange(widget, newZoom, scalingFactor);
			}
		}
		Event event = new Event();
		event.type = SWT.ZoomChanged;
		event.widget = widget;
		event.detail = newZoom;
		event.doit = true;
		widget.notifyListeners(SWT.ZoomChanged, event);
	}

	public static void registerHandler(DPIZoomChangeHandler zoomChangeVisitor, Class<? extends Widget> clazzToRegisterFor) {
		dpiZoomChangeHandlers.put(clazzToRegisterFor, zoomChangeVisitor);
	}
}