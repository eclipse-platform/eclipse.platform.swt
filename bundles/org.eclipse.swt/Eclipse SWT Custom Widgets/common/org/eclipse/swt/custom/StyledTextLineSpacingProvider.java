/**
 *  Copyright (c) 2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - Customize different line spacing of StyledText - Bug 522020
 */
package org.eclipse.swt.custom;

/**
 * Line spacing provider used to customize different line spacing for some lines
 * of {@link StyledText}
 *
 * @since 3.107
 */
@FunctionalInterface
public interface StyledTextLineSpacingProvider {

	/**
	 * Returns the line spacing of the given line index and null otherwise. In this
	 * case, it will use the {@link StyledText#getLineSpacing()}.
	 *
	 * @param lineIndex
	 *            line index.
	 * @return the line spacing of the given line index and null otherwise. In this
	 *         case, it will use the {@link StyledText#getLineSpacing()}.
	 * @since 3.107
	 */
	Integer getLineSpacing(int lineIndex);

}
