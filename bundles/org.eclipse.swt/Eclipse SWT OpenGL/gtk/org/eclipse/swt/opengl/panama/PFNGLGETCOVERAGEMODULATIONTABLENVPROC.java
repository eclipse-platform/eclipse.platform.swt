// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETCOVERAGEMODULATIONTABLENVPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLGETCOVERAGEMODULATIONTABLENVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETCOVERAGEMODULATIONTABLENVPROC.class, fi, constants$749.PFNGLGETCOVERAGEMODULATIONTABLENVPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETCOVERAGEMODULATIONTABLENVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETCOVERAGEMODULATIONTABLENVPROC.class, fi, constants$749.PFNGLGETCOVERAGEMODULATIONTABLENVPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETCOVERAGEMODULATIONTABLENVPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$749.PFNGLGETCOVERAGEMODULATIONTABLENVPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


