// SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
package org.eclipse.swt.internal;
public final class ExceptionStash implements AutoCloseable {
    private Throwable failed;
    public void stash(Throwable error) {
        if (failed == null) failed = error;
        else failed.addSuppressed(error);
    }
    @Override public void close() {
        if (failed instanceof Error error) throw error;
        if (failed instanceof RuntimeException error) throw error;
    }
}
