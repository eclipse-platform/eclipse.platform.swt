// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLGETQUERYOBJECTI64VEXTPROC {

    void apply(int x0, int x1, jdk.incubator.foreign.MemoryAddress x2);
    static MemoryAddress allocate(PFNGLGETQUERYOBJECTI64VEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLGETQUERYOBJECTI64VEXTPROC.class, fi, constants$673.PFNGLGETQUERYOBJECTI64VEXTPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLGETQUERYOBJECTI64VEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLGETQUERYOBJECTI64VEXTPROC.class, fi, constants$673.PFNGLGETQUERYOBJECTI64VEXTPROC$FUNC, "(IILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLGETQUERYOBJECTI64VEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, jdk.incubator.foreign.MemoryAddress x2) -> {
            try {
                constants$673.PFNGLGETQUERYOBJECTI64VEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


