/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer (Syntevo) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

/**
 * This is an (experimental) class to provide colors for custom-drawn controls.
 * @noreference this is still experimental API and might be removed
 */
public interface ColorProvider {

	/**
	 * Returns the color associated with the specified key.
	 *
	 * @param key a non-null key
	 * @return a non-null color
	 */
	Color getColor(String key);
}
