// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETARRAYOBJECTIVATIPROC {

    void apply(int x0, int x1, jdk.incubator.foreign.MemoryAddress x2);
    static MemoryAddress allocate(PFNGLGETARRAYOBJECTIVATIPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETARRAYOBJECTIVATIPROC.class, fi, constants$499.PFNGLGETARRAYOBJECTIVATIPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETARRAYOBJECTIVATIPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETARRAYOBJECTIVATIPROC.class, fi, constants$499.PFNGLGETARRAYOBJECTIVATIPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETARRAYOBJECTIVATIPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, jdk.incubator.foreign.MemoryAddress x2) -> {
            try {
                constants$499.PFNGLGETARRAYOBJECTIVATIPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


