// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLVERTEXATTRIB1FARBPROC {

    void apply(int x0, float x1);
    static MemoryAddress allocate(PFNGLVERTEXATTRIB1FARBPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIB1FARBPROC.class, fi, constants$386.PFNGLVERTEXATTRIB1FARBPROC$FUNC, "(IF)V");
    }
    static MemoryAddress allocate(PFNGLVERTEXATTRIB1FARBPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLVERTEXATTRIB1FARBPROC.class, fi, constants$386.PFNGLVERTEXATTRIB1FARBPROC$FUNC, "(IF)V", scope);
    }
    static PFNGLVERTEXATTRIB1FARBPROC ofAddress(MemoryAddress addr) {
        return (int x0, float x1) -> {
            try {
                constants$386.PFNGLVERTEXATTRIB1FARBPROC$MH.invokeExact((Addressable)addr, x0, x1);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


