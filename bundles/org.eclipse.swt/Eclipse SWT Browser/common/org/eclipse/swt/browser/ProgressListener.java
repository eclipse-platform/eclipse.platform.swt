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
 * a {@link ProgressEvent} notification when a {@link Browser}
 * makes a progress in loading the current URL or when the
 * current URL has been loaded.
 *
 * @see Browser#addProgressListener(ProgressListener)
 * @see Browser#removeProgressListener(ProgressListener)
 * @see Browser#getUrl()
 *
 * @since 3.0
 */
public interface ProgressListener extends SWTEventListener {

/**
 * This method is called when a progress is made during the loading of the
 * current location.
 *
 *
 * <p>The following fields in the <code>ProgressEvent</code> apply:</p>
 * <ul>
 * <li>(in) current the progress for the location currently being loaded
 * <li>(in) total the maximum progress for the location currently being loaded
 * <li>(in) widget the <code>Browser</code> whose current URL is being loaded
 * </ul>
 *
 * @param event the <code>ProgressEvent</code> related to the loading of the
 * current location of a <code>Browser</code>
 *
 * @since 3.0
 */
public void changed(ProgressEvent event);

/**
 * This method is called when the current location has been completely loaded.
 *
 *
 * <p>The following fields in the <code>ProgressEvent</code> apply:</p>
 * <ul>
 * <li>(in) widget the <code>Browser</code> whose current URL has been loaded
 * </ul>
 *
 * @param event the <code>ProgressEvent</code> related to the <code>Browser</code>
 * that has loaded its current URL.
 *
 * @since 3.0
 */
public void completed(ProgressEvent event);

/**
 * Static helper method to create a <code>ProgressListener</code> for the
 * {@link #changed(ProgressEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return LocationListener
 * @since 3.107
 */
public static ProgressListener changedAdapter(Consumer<ProgressEvent> c) {
	return new ProgressAdapter() {
		@Override
		public void changed(ProgressEvent e) {
			c.accept(e);
		}
	};
}

/**
 * Static helper method to create a <code>ProgressListener</code> for the
 * {@link #completed(ProgressEvent e)}) method, given a lambda expression or a method reference.
 *
 * @param c the consumer of the event
 * @return LocationListener
 * @since 3.107
 */
public static ProgressListener completedAdapter(Consumer<ProgressEvent> c) {
	return new ProgressAdapter() {
		@Override
		public void completed(ProgressEvent e) {
			c.accept(e);
		}
	};
}
}
