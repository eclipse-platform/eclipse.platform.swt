/*******************************************************************************
 * Copyright (c) 2000, 2024 Yatta Solutions and others.
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

import org.eclipse.swt.widgets.*;

public class DPIZoomChangeRegistry {

	private static Set<Class<? extends Widget>> blacklist = new HashSet<>();
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

	public static void unregisterForOnZoomChange(Class<? extends Widget> classToRegister) {
		blacklist.add(classToRegister);
	}

	private static boolean isDPIZoomChangeApplicable(Widget widget) {
		if (blacklist.contains(widget.getClass())) {
			return false;
		}
		return true;
	}

	public static <E> void applyChange(Widget widget, int newZoomFactor, float scalingFactor) {
		if (isDPIZoomChangeApplicable(widget)) {
			int zoomFactor = widget.getZoomFactor();
			if (zoomFactor != newZoomFactor) {
				for (Entry<Class<? extends Widget>, DPIZoomChangeHandler> entry : dpiZoomChangeHandlers.entrySet()) {
					Class<? extends Widget> clazz = entry.getKey();
					DPIZoomChangeHandler handler = entry.getValue();
					if (clazz.isInstance(widget)) {
						handler.handleDPIChange(widget, newZoomFactor, scalingFactor);
					}
				}
			}
		}
	}

	public static <E> void registerHandler(DPIZoomChangeHandler zoomChangeVisitor, Class<? extends Widget> clazzToRegisterFor) {
		dpiZoomChangeHandlers.put(clazzToRegisterFor, zoomChangeVisitor);
	}
}