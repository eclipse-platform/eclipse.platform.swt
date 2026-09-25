/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *     SAP SE and others - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.canvasext;

public interface IExternalFontMetrics {

	public int getAscent();

	public double getAverageCharacterWidth();

	@Deprecated
	public int getAverageCharWidth();

	public int getDescent();

	public int getHeight();

	public int getLeading();

}