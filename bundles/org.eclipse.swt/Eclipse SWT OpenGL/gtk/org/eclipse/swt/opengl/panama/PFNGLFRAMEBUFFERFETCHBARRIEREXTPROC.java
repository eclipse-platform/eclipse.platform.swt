// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLFRAMEBUFFERFETCHBARRIEREXTPROC {

    void apply();
    static MemoryAddress allocate(PFNGLFRAMEBUFFERFETCHBARRIEREXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLFRAMEBUFFERFETCHBARRIEREXTPROC.class, fi, constants$665.PFNGLFRAMEBUFFERFETCHBARRIEREXTPROC$FUNC, "()V");
    }
    static MemoryAddress allocate(PFNGLFRAMEBUFFERFETCHBARRIEREXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLFRAMEBUFFERFETCHBARRIEREXTPROC.class, fi, constants$665.PFNGLFRAMEBUFFERFETCHBARRIEREXTPROC$FUNC, "()V", scope);
    }
    static PFNGLFRAMEBUFFERFETCHBARRIEREXTPROC ofAddress(MemoryAddress addr) {
        return () -> {
            try {
                constants$665.PFNGLFRAMEBUFFERFETCHBARRIEREXTPROC$MH.invokeExact((Addressable)addr);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


