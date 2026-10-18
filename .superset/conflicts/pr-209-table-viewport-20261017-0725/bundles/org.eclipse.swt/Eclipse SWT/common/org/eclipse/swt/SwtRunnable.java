/*******************************************************************************
 * Copyright (c) 2022 Christoph Läubrich and others
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt;

/**
 * A method that returns no result and may throw a checked exception of the given
 * type.
 * @since 3.123
 */
@FunctionalInterface
public interface SwtRunnable<E extends Exception> {
	/**
	 * runs a given action, or throws an exception.
	 *
	 * @throws E the Exception of given type
	 */
	void run() throws E;
}
