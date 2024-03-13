/*******************************************************************************
 * Copyright (c) 2003, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.util.function.*;

import org.eclipse.swt.internal.*;

/**
 * This listener interface may be implemented in order to receive
 * a {@link LocationEvent} notification when a {@link Browser}
 * navigates to a different URL.
 *
 * @see Browser#addLocationListener(LocationListener)
 * @see Browser#removeLocationListener(LocationListener)
 * @see LocationAdapter
 *
 * @since 3.0
 */
public interface LocationListener extends SWTEventListener {

/**
 * This method is called when the current location is about to be changed.
 *
 * <p>The following fields in the <code>LocationEvent</code> apply:</p>
 * <ul>
 * <li>(in) location the location to be loaded
 * <li>(in) widget the <code>Browser</code> whose location is changing
 * <li>(in/out) doit can be set to <code>false</code> to prevent the location
 * from being loaded
 * </ul>
 *
 * @param event the <code>LocationEvent</code> that specifies the location
 * to be loaded by a <code>Browser</code>
 *
 * @since 3.0
 */
public void changing(LocationEvent event);

/**
 * This method is called when the current location is changed.
 *
 * <p>The following fields in the <code>LocationEvent</code> apply:</p>
 * <ul>
 * <li>(in) location the current location
 * <li>(in) top <code>true</code> if the location opens in the top frame or
 * <code>false</code> otherwise
 * <li>(in) widget the <code>Browser</code> whose location has changed
 * </ul>
 *
 * @param event the <code>LocationEvent</code> that specifies  the new
 * location of a <code>Browser</code>
 *
 * @since 3.0
 */
public void changed(LocationEvent event);

/**
 * Static helper method to create a <code>LocationListener</code> for the
 * {@link #changing(LocationEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return LocationListener
 * @since 3.107
 */
public static LocationListener changingAdapter(Consumer<LocationEvent> c) {
	return new LocationAdapter() {
		@Override
		public void changing(LocationEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>LocationListener</code> for the
 * {@link #changed(LocationEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return LocationListener
 * @since 3.107
 */
public static LocationListener changedAdapter(Consumer<LocationEvent> c) {
	return new LocationAdapter() {
		@Override
		public void changed(LocationEvent e) {
			c.accept(e);
		}
	};
}

}
