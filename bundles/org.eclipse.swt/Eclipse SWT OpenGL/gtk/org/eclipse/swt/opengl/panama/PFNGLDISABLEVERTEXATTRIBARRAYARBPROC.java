// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLDISABLEVERTEXATTRIBARRAYARBPROC {

    void apply(int x0);
    static MemoryAddress allocate(PFNGLDISABLEVERTEXATTRIBARRAYARBPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLDISABLEVERTEXATTRIBARRAYARBPROC.class, fi, constants$398.PFNGLDISABLEVERTEXATTRIBARRAYARBPROC$FUNC, "(I)V");
    }
    static MemoryAddress allocate(PFNGLDISABLEVERTEXATTRIBARRAYARBPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLDISABLEVERTEXATTRIBARRAYARBPROC.class, fi, constants$398.PFNGLDISABLEVERTEXATTRIBARRAYARBPROC$FUNC, "(I)V", scope);
    }
    static PFNGLDISABLEVERTEXATTRIBARRAYARBPROC ofAddress(MemoryAddress addr) {
        return (int x0) -> {
            try {
                constants$398.PFNGLDISABLEVERTEXATTRIBARRAYARBPROC$MH.invokeExact((Addressable)addr, x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


