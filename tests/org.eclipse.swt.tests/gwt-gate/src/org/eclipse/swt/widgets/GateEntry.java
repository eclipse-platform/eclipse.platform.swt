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
package org.eclipse.swt.widgets;

import com.google.gwt.core.client.EntryPoint;

/** Gate-only entry point: runs the shared workload so GWT must compile every atom. */
public class GateEntry implements EntryPoint {
	@Override public void onModuleLoad () {
		for (int seed = 1; seed <= 8; seed++) log (GateWorkload.run (seed, 400));
	}

	private static native void log (String text) /*-{
		$wnd.console.log (text);
	}-*/;
}
