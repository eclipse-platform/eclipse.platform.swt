// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXATTRIBI3IPROC {

    void apply(int x0, int x1, int x2, int x3);
    static MemoryAddress allocate(PFNGLVERTEXATTRIBI3IPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIBI3IPROC.class, fi, constants$153.PFNGLVERTEXATTRIBI3IPROC$FUNC, "(IIII)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXATTRIBI3IPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIBI3IPROC.class, fi, constants$153.PFNGLVERTEXATTRIBI3IPROC$FUNC, "(IIII)V", scope);
    }
    static PFNGLVERTEXATTRIBI3IPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, int x3) -> {
            try {
                constants$153.PFNGLVERTEXATTRIBI3IPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


