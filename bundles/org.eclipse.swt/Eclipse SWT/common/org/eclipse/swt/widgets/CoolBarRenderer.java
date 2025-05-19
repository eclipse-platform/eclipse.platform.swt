/*******************************************************************************
 * Copyright (c) 2025 Advantest Europe GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 				Raghunandana Murthappa (Advantest)
 *******************************************************************************/
package org.eclipse.swt.widgets;

public abstract class CoolBarRenderer extends ControlRenderer {

	protected final CoolBar coolbar;

	public CoolBarRenderer(CoolBar coolbar) {
		super(coolbar);
		this.coolbar = coolbar;
	}
}
