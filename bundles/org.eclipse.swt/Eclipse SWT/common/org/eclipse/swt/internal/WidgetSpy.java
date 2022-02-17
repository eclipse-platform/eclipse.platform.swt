/*******************************************************************************
 * Copyright (c) 2022 Simeon Andreev and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.util.*;
import java.util.List;

import org.eclipse.swt.widgets.*;

/**
 * Helper class to allow widget creation and disposal monitoring
 */
public class WidgetSpy {

	/**
	 * Flag to prevent {@link Widget} from entering this class during debugging,
	 * if tracking of creation and disposal is not enabled.
	 */
	public static boolean isEnabled;

	private static final WidgetSpy instance = new WidgetSpy();

	private WidgetTracker widgetTracker;

	private WidgetSpy() {
		// singleton
	}

	public static WidgetSpy getInstance() {
		return instance;
	}

	/**
	 * Enables tracking of {@link Widget} object creation and disposal.
	 *
	 * WARNING: the tracker will be called from the UI thread. Do not block
	 * it and do not throw any exceptions.
	 *
	 * @param tracker                notified when a widget is created or disposed. Use
	 *                               {@code null} to disable tracking. The tracker will be
	 *                               notified of widgets created and disposed after setting the tracker.
	 */
	public void setWidgetTracker(WidgetTracker tracker) {
		isEnabled = tracker != null;
		widgetTracker = tracker;
	}

	public void widgetCreated(Widget widget) {
		if (widgetTracker != null) {
			widgetTracker.widgetCreated(widget);
		}
	}

	public void widgetDisposed(Widget widget) {
		if (widgetTracker != null) {
			widgetTracker.widgetDisposed(widget);
		}
	}

	/**
	 * Custom callback to register widget creation / disposal
	 */
	public static interface WidgetTracker {
		default void widgetCreated(Widget widget) {}

		default void widgetDisposed(Widget widget) {}
	}

	/**
	 * Default implementation simply collects all created and not disposed widgets
	 */
	public static class NonDisposedWidgetTracker implements WidgetTracker {

		private final Map<Widget, Error> nonDisposedWidgets = new LinkedHashMap<>();
		private final Set<Class<? extends Widget> > trackedTypes = new HashSet<>();

		@Override
		public void widgetCreated(Widget widget) {
			boolean isTracked = isTracked(widget);
			if (isTracked) {
				Error creationException = new Error("Created widget of type: " + widget.getClass().getSimpleName());
				nonDisposedWidgets.put(widget, creationException);
			}
		}

		@Override
		public void widgetDisposed(Widget widget) {
			boolean isTracked = isTracked(widget);
			if (isTracked) {
				nonDisposedWidgets.remove(widget);
			}
		}

		public Map<Widget, Error> getNonDisposedWidgets() {
			return Collections.unmodifiableMap(nonDisposedWidgets);
		}

		public void startTracking() {
			clearNonDisposedWidgets();
			WidgetSpy.getInstance().setWidgetTracker(this);
		}

		private void clearNonDisposedWidgets() {
			nonDisposedWidgets.clear();
		}

		public void stopTracking() {
			WidgetSpy.getInstance().setWidgetTracker(null);
		}

		public void setTrackingEnabled(boolean enabled) {
			if (enabled) {
				startTracking();
			} else {
				stopTracking();
			}
		}

		public void setTrackedTypes(List<Class<? extends Widget>> types) {
			trackedTypes.clear();
			trackedTypes.addAll(types);
		}

		private boolean isTracked(Widget widget) {
			boolean isTrackingAllTypes = trackedTypes.isEmpty();
			if (isTrackingAllTypes) {
				return true;
			}
			if (widget != null) {
				Class<? extends Widget> widgetType = widget.getClass();
				if (trackedTypes.contains(widgetType)) {
					return true;
				}
				for (Class<? extends Widget> filteredType : trackedTypes) {
					if (filteredType.isAssignableFrom(widgetType)) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
