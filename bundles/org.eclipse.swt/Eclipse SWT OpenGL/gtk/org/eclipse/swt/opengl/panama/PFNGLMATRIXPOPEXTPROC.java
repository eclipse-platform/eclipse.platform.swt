// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLMATRIXPOPEXTPROC {

    void apply(int x0);
    static MemoryAddress allocate(PFNGLMATRIXPOPEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLMATRIXPOPEXTPROC.class, fi, constants$541.PFNGLMATRIXPOPEXTPROC$FUNC, "(I)V");
    }
    static MemoryAddress allocate(PFNGLMATRIXPOPEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLMATRIXPOPEXTPROC.class, fi, constants$541.PFNGLMATRIXPOPEXTPROC$FUNC, "(I)V", scope);
    }
    static PFNGLMATRIXPOPEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0) -> {
            try {
                constants$541.PFNGLMATRIXPOPEXTPROC$MH.invokeExact((Addressable)addr, x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


