// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEX2HNVPROC {

    void apply(short x0, short x1);
    static MemoryAddress allocate(PFNGLVERTEX2HNVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEX2HNVPROC.class, fi, constants$761.PFNGLVERTEX2HNVPROC$FUNC, "(SS)V");
    }
    static MemoryAddress allocate(PFNGLVERTEX2HNVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEX2HNVPROC.class, fi, constants$761.PFNGLVERTEX2HNVPROC$FUNC, "(SS)V", scope);
    }
    static PFNGLVERTEX2HNVPROC ofAddress(MemoryAddress addr) {
        return (short x0, short x1) -> {
            try {
                constants$761.PFNGLVERTEX2HNVPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


