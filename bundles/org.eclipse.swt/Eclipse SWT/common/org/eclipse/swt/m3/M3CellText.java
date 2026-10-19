/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.m3;

/**
 * The text of a cell of the application's source rows (enhanced M3 API).
 * <p>
 * It is the function the application uses to fill its items, given to {@link M3ColumnFilter}
 * (text criteria) and {@link M3ColumnSort} (default text keys), so that neither reads widget
 * items.
 * </p>
 *
 * @since 3.138
 */
@FunctionalInterface
public interface M3CellText {

	/**
	 * Returns the text of a cell.
	 *
	 * @param sourceRow the source row
	 * @param column the column
	 * @return the text, or <code>null</code> for an empty cell
	 */
	String text (int sourceRow, int column);
}
