/*******************************************************************************
 * Copyright (c) 2024 Patrick Ziegler and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

/**
 * This class implements the GIO AsyncReadyCallback type and is used to
 * transform an asynchronous {@code async} and synchronous {@code await}
 * operation into a single synchronous {@code run} operation.
 */
public interface SyncFinishCallback<T> {
	/**
	 * This method is responsible for initializes the asynchronous operation
	 *
	 * @param callback The callback address to execute when the operation is
	 *                 complete.
	 */
	void async(long callback);

	/**
	 * This method is called from within the callback function in order to
	 * finish the executed operation and to return the result.
	 *
	 * @param result The generic, asynchronous function result.
	 * @return The specific result of the operation.
	 */
	T await(long result);
}