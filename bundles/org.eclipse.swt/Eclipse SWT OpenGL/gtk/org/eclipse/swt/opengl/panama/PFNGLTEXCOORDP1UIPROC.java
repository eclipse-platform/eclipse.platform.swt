// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLTEXCOORDP1UIPROC {

    void apply(int x0, int x1);
    static MemoryAddress allocate(PFNGLTEXCOORDP1UIPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLTEXCOORDP1UIPROC.class, fi, constants$197.PFNGLTEXCOORDP1UIPROC$FUNC, "(II)V");
    }
    static MemoryAddress allocate(PFNGLTEXCOORDP1UIPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLTEXCOORDP1UIPROC.class, fi, constants$197.PFNGLTEXCOORDP1UIPROC$FUNC, "(II)V", scope);
    }
    static PFNGLTEXCOORDP1UIPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1) -> {
            try {
                constants$197.PFNGLTEXCOORDP1UIPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


