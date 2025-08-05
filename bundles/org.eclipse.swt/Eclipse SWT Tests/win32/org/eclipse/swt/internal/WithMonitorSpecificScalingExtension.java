/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal;

import org.junit.jupiter.api.extension.*;

/**
 * Runs a test with monitor specific scaling. Disposes the default display before and after execution.
 */
public final class WithMonitorSpecificScalingExtension extends ResetMonitorSpecificScalingExtension {

	private WithMonitorSpecificScalingExtension() {
		super();
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		super.beforeEach(context);
		Win32DPIUtils.setMonitorSpecificScaling(true);
	}

}
