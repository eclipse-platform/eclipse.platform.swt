// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLTEXCOORD2XOESPROC {

    void apply(int x0, int x1);
    static MemoryAddress allocate(PFNGLTEXCOORD2XOESPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLTEXCOORD2XOESPROC.class, fi, constants$444.PFNGLTEXCOORD2XOESPROC$FUNC, "(II)V");
    }
    static MemoryAddress allocate(PFNGLTEXCOORD2XOESPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLTEXCOORD2XOESPROC.class, fi, constants$444.PFNGLTEXCOORD2XOESPROC$FUNC, "(II)V", scope);
    }
    static PFNGLTEXCOORD2XOESPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1) -> {
            try {
                constants$444.PFNGLTEXCOORD2XOESPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


