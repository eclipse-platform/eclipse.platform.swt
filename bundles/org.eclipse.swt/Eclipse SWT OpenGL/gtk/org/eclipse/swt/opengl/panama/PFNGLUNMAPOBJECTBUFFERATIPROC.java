// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLUNMAPOBJECTBUFFERATIPROC {

    void apply(int x0);
    static MemoryAddress allocate(PFNGLUNMAPOBJECTBUFFERATIPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLUNMAPOBJECTBUFFERATIPROC.class, fi, constants$495.PFNGLUNMAPOBJECTBUFFERATIPROC$FUNC, "(I)V");
    }
    static MemoryAddress allocate(PFNGLUNMAPOBJECTBUFFERATIPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLUNMAPOBJECTBUFFERATIPROC.class, fi, constants$495.PFNGLUNMAPOBJECTBUFFERATIPROC$FUNC, "(I)V", scope);
    }
    static PFNGLUNMAPOBJECTBUFFERATIPROC ofAddress(MemoryAddress addr) {
        return (int x0) -> {
            try {
                constants$495.PFNGLUNMAPOBJECTBUFFERATIPROC$MH.invokeExact((Addressable)addr, x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


