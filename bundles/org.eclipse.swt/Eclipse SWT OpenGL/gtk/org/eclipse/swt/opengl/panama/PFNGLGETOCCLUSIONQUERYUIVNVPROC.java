// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETOCCLUSIONQUERYUIVNVPROC {

    void apply(int x0, int x1, jdk.incubator.foreign.MemoryAddress x2);
    static MemoryAddress allocate(PFNGLGETOCCLUSIONQUERYUIVNVPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETOCCLUSIONQUERYUIVNVPROC.class, fi, constants$778.PFNGLGETOCCLUSIONQUERYUIVNVPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETOCCLUSIONQUERYUIVNVPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETOCCLUSIONQUERYUIVNVPROC.class, fi, constants$778.PFNGLGETOCCLUSIONQUERYUIVNVPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETOCCLUSIONQUERYUIVNVPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, jdk.incubator.foreign.MemoryAddress x2) -> {
            try {
                constants$778.PFNGLGETOCCLUSIONQUERYUIVNVPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


