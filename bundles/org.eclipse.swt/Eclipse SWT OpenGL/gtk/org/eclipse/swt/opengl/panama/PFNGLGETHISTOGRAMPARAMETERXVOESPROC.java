// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETHISTOGRAMPARAMETERXVOESPROC {

    void apply(int x0, int x1, jdk.incubator.foreign.MemoryAddress x2);
    static MemoryAddress allocate(PFNGLGETHISTOGRAMPARAMETERXVOESPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETHISTOGRAMPARAMETERXVOESPROC.class, fi, constants$431.PFNGLGETHISTOGRAMPARAMETERXVOESPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETHISTOGRAMPARAMETERXVOESPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETHISTOGRAMPARAMETERXVOESPROC.class, fi, constants$431.PFNGLGETHISTOGRAMPARAMETERXVOESPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETHISTOGRAMPARAMETERXVOESPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, jdk.incubator.foreign.MemoryAddress x2) -> {
            try {
                constants$431.PFNGLGETHISTOGRAMPARAMETERXVOESPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


