// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETNPOLYGONSTIPPLEPROC {

    void apply(int x0, jdk.incubator.foreign.MemoryAddress x1);
    static MemoryAddress allocate(PFNGLGETNPOLYGONSTIPPLEPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETNPOLYGONSTIPPLEPROC.class, fi, constants$309.PFNGLGETNPOLYGONSTIPPLEPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETNPOLYGONSTIPPLEPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETNPOLYGONSTIPPLEPROC.class, fi, constants$309.PFNGLGETNPOLYGONSTIPPLEPROC$FUNC, "(ILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETNPOLYGONSTIPPLEPROC ofAddress(MemoryAddress addr) {
        return (int x0, jdk.incubator.foreign.MemoryAddress x1) -> {
            try {
                constants$309.PFNGLGETNPOLYGONSTIPPLEPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


