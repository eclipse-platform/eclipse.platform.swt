// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLTEXBUFFERPROC {

    void apply(int x0, int x1, int x2);
    static MemoryAddress allocate(PFNGLTEXBUFFERPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLTEXBUFFERPROC.class, fi, constants$175.PFNGLTEXBUFFERPROC$FUNC, "(III)V");
    }
    static MemoryAddress allocate(PFNGLTEXBUFFERPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLTEXBUFFERPROC.class, fi, constants$175.PFNGLTEXBUFFERPROC$FUNC, "(III)V", scope);
    }
    static PFNGLTEXBUFFERPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2) -> {
            try {
                constants$175.PFNGLTEXBUFFERPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


