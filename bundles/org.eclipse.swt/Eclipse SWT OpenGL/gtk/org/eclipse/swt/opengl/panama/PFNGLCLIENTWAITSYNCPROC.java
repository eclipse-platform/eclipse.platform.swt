// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLCLIENTWAITSYNCPROC {

    int apply(jdk.incubator.foreign.MemoryAddress x0, int x1, long x2);
    static MemoryAddress allocate(PFNGLCLIENTWAITSYNCPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLCLIENTWAITSYNCPROC.class, fi, constants$182.PFNGLCLIENTWAITSYNCPROC$FUNC, "(Ljdk/incubator/foreign/MemoryAddress;IJ)I");
    }
    static MemoryAddress allocate(PFNGLCLIENTWAITSYNCPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLCLIENTWAITSYNCPROC.class, fi, constants$182.PFNGLCLIENTWAITSYNCPROC$FUNC, "(Ljdk/incubator/foreign/MemoryAddress;IJ)I", scope);
    }
    static PFNGLCLIENTWAITSYNCPROC ofAddress(MemoryAddress addr) {
        return (jdk.incubator.foreign.MemoryAddress x0, int x1, long x2) -> {
            try {
                return (int)constants$182.PFNGLCLIENTWAITSYNCPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


