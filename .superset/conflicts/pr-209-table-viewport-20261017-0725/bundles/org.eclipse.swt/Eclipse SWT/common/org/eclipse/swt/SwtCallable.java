/*******************************************************************************
 * Copyright (c) 2021 Joerg Kubitz and others
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Joerg Kubitz - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt;

/**
 * A method that returns a result and may throw a checked exception of the given
 * type.
 *
 * @see org.eclipse.swt.widgets.Display#syncCall(SwtCallable)
 * @since 3.118
 * @author Joerg Kubitz
 */
@FunctionalInterface
public interface SwtCallable<V, E extends Exception> {
	/**
	 * Computes a value, or throws an exception.
	 *
	 * @return the result
	 * @throws E the Exception of given type
	 */
	V call() throws E;
}
