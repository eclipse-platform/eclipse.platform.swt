/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
package org.eclipse.swt.examples.paint;

import org.eclipse.swt.graphics.*;

public class Tool {
	public int id;
	public String name;
	public String group;
	public int type;
	public Runnable action;
	public Image image = null;
	public Object data;

	public Tool(int id, String name, String group, int type) {
		super();
		this.id = id;
		this.name = name;
		this.group = group;
		this.type = type;
	}

	public Tool(int id, String name, String group, int type, Object data) {
		this(id, name, group, type);
		this.data = data;
	}
}
