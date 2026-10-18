// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.internal.residency;
import org.junit.jupiter.api.Test;
/** Toolkit discovery wrapper; compilation/discovery is a separate gate. */
public class PatternWordContract_Test {
    @Test public void wordArithmeticAndFallback() throws Throwable { PatternWordContract.verify(); }
}
