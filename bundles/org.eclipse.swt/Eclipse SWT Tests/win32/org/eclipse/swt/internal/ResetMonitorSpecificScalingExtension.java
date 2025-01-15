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

import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.extension.*;

/**
 * Resets the monitor-specific scaling configuration after the test has been executed.
 * Disposes the default display before and after test execution.
 */
public sealed class ResetMonitorSpecificScalingExtension implements BeforeEachCallback, AfterEachCallback permits WithMonitorSpecificScalingExtension {
	private boolean wasMonitorSpecificScalingActive;

	protected ResetMonitorSpecificScalingExtension() {
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		wasMonitorSpecificScalingActive = DPIUtil.isMonitorSpecificScalingActive();
		Display.getDefault().dispose();
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		DPIUtil.setMonitorSpecificScaling(wasMonitorSpecificScalingActive);
		Display.getDefault().dispose();
	}

}
