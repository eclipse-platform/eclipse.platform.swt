// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLSECONDARYCOLOR3USEXTPROC {

    void apply(short x0, short x1, short x2);
    static MemoryAddress allocate(PFNGLSECONDARYCOLOR3USEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLSECONDARYCOLOR3USEXTPROC.class, fi, constants$660.PFNGLSECONDARYCOLOR3USEXTPROC$FUNC, "(SSS)V");
    }
    static MemoryAddress allocate(PFNGLSECONDARYCOLOR3USEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLSECONDARYCOLOR3USEXTPROC.class, fi, constants$660.PFNGLSECONDARYCOLOR3USEXTPROC$FUNC, "(SSS)V", scope);
    }
    static PFNGLSECONDARYCOLOR3USEXTPROC ofAddress(MemoryAddress addr) {
        return (short x0, short x1, short x2) -> {
            try {
                constants$660.PFNGLSECONDARYCOLOR3USEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


