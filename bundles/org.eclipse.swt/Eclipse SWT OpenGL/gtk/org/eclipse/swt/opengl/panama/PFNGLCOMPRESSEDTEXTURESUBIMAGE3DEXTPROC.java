// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLCOMPRESSEDTEXTURESUBIMAGE3DEXTPROC {

    void apply(int x0, int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9, int x10, jdk.incubator.foreign.MemoryAddress x11);
    static MemoryAddress allocate(PFNGLCOMPRESSEDTEXTURESUBIMAGE3DEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLCOMPRESSEDTEXTURESUBIMAGE3DEXTPROC.class, fi, constants$566.PFNGLCOMPRESSEDTEXTURESUBIMAGE3DEXTPROC$FUNC, "(IIIIIIIIIIILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLCOMPRESSEDTEXTURESUBIMAGE3DEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLCOMPRESSEDTEXTURESUBIMAGE3DEXTPROC.class, fi, constants$566.PFNGLCOMPRESSEDTEXTURESUBIMAGE3DEXTPROC$FUNC, "(IIIIIIIIIIILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLCOMPRESSEDTEXTURESUBIMAGE3DEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9, int x10, jdk.incubator.foreign.MemoryAddress x11) -> {
            try {
                constants$566.PFNGLCOMPRESSEDTEXTURESUBIMAGE3DEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


