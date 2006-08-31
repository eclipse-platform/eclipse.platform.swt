/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

/**
 * Used to perform an action after an item in a Menu has been selected.
 * 
 * @see org.eclipse.swt.examples.graphics.ColorMenu.java
 * @see org.eclipse.swt.examples.graphics.GraphicsBackground.java
 */
public interface ColorListener {

	public void setColor(GraphicsBackground gb);

}
