// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLPOINTPARAMETERFVSGISPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLPOINTPARAMETERFVSGISPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLPOINTPARAMETERFVSGISPROC.class, fi, constants$878.PFNGLPOINTPARAMETERFVSGISPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLPOINTPARAMETERFVSGISPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLPOINTPARAMETERFVSGISPROC.class, fi, constants$878.PFNGLPOINTPARAMETERFVSGISPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLPOINTPARAMETERFVSGISPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$878.PFNGLPOINTPARAMETERFVSGISPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


