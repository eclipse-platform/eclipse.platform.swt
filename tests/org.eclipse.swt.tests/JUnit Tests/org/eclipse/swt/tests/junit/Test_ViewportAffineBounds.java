/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import org.junit.Test;

/** Runs against the real SWT internal owner, without a native Display or an API-shaped double. */
public class Test_ViewportAffineBounds {
    @Test
    public void testFourCornerNumericContract() throws Exception {
        ViewportAffineBoundsContract.verify();
    }
}
