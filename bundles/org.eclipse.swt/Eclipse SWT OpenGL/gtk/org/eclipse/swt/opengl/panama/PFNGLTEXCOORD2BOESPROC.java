// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLTEXCOORD2BOESPROC {

    void apply(byte x0, byte x1);
    static MemoryAddress allocate(PFNGLTEXCOORD2BOESPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLTEXCOORD2BOESPROC.class, fi, constants$410.PFNGLTEXCOORD2BOESPROC$FUNC, "(BB)V");
    }
    static MemoryAddress allocate(PFNGLTEXCOORD2BOESPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLTEXCOORD2BOESPROC.class, fi, constants$410.PFNGLTEXCOORD2BOESPROC$FUNC, "(BB)V", scope);
    }
    static PFNGLTEXCOORD2BOESPROC ofAddress(MemoryAddress addr) {
        return (byte x0, byte x1) -> {
            try {
                constants$410.PFNGLTEXCOORD2BOESPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


