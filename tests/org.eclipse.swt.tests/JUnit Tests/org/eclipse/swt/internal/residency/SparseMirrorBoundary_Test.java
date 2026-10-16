// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.internal.residency;

import org.junit.jupiter.api.Test;

/** Native toolkit suite discovery wrapper; the same contract runs standalone. */
public class SparseMirrorBoundary_Test {
    @Test public void sparseStateContract() { SparseMirrorContract.verify(); }
}
