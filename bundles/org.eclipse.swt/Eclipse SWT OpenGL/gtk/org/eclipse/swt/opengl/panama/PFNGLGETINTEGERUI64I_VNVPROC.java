// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETINTEGERUI64I_VNVPROC {

    void apply(int x0, int x1, jdk.incubator.foreign.MemoryAddress x2);
    static MemoryAddress allocate(PFNGLGETINTEGERUI64I_VNVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETINTEGERUI64I_VNVPROC.class, fi, constants$839.PFNGLGETINTEGERUI64I_VNVPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETINTEGERUI64I_VNVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETINTEGERUI64I_VNVPROC.class, fi, constants$839.PFNGLGETINTEGERUI64I_VNVPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETINTEGERUI64I_VNVPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, jdk.incubator.foreign.MemoryAddress x2) -> {
            try {
                constants$839.PFNGLGETINTEGERUI64I_VNVPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


