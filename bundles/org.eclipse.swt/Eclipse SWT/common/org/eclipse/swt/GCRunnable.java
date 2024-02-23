/*******************************************************************************
 * Copyright (c) 2024 Tobias Melcher and others
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Tobias Melcher - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt;

import org.eclipse.swt.graphics.*;

/**
 * A method that gets a GC as input, returns no result and may throw a checked
 * exception of the given type.
 *
 * @since 3.125
 */

@FunctionalInterface
public interface GCRunnable<E extends Exception> {
	void run(GC gc) throws E;
}
