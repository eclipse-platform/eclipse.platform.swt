// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLLABELOBJECTEXTPROC {

    void apply(int x0, int x1, int x2, jdk.incubator.foreign.MemoryAddress x3);
    static MemoryAddress allocate(PFNGLLABELOBJECTEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLLABELOBJECTEXTPROC.class, fi, constants$535.PFNGLLABELOBJECTEXTPROC$FUNC, "(IIILjdk/incubator/foreign/MemoryAddress;)V");
    }
    static MemoryAddress allocate(PFNGLLABELOBJECTEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLLABELOBJECTEXTPROC.class, fi, constants$535.PFNGLLABELOBJECTEXTPROC$FUNC, "(IIILjdk/incubator/foreign/MemoryAddress;)V", scope);
    }
    static PFNGLLABELOBJECTEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, int x1, int x2, jdk.incubator.foreign.MemoryAddress x3) -> {
            try {
                constants$535.PFNGLLABELOBJECTEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


