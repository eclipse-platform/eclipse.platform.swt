// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.internal.residency;
import org.junit.jupiter.api.Test;
/** Discovers the full scalar query oracle in the owning toolkit's existing test source tree. */
public class ViewportQuerySeek_Test {
    @Test public void scalarQueriesAndDistantWindows() { ViewportQueryContract.verify(); }
}
