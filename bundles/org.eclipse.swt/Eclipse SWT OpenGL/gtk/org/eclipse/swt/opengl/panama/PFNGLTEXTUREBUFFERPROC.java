// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLTEXTUREBUFFERPROC {

    void apply(int x0, int x1, int x2);
    static MemoryAddress allocate(PFNGLTEXTUREBUFFERPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLTEXTUREBUFFERPROC.class, fi, constants$286.PFNGLTEXTUREBUFFERPROC$FUNC, "(III)V");
    }
    static MemoryAddress allocate(PFNGLTEXTUREBUFFERPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLTEXTUREBUFFERPROC.class, fi, constants$286.PFNGLTEXTUREBUFFERPROC$FUNC, "(III)V", scope);
    }
    static PFNGLTEXTUREBUFFERPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2) -> {
            try {
                constants$286.PFNGLTEXTUREBUFFERPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


