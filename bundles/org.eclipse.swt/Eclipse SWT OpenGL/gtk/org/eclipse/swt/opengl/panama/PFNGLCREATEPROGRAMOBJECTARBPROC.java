// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLCREATEPROGRAMOBJECTARBPROC {

    int apply();
    static MemoryAddress allocate(PFNGLCREATEPROGRAMOBJECTARBPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLCREATEPROGRAMOBJECTARBPROC.class, fi, constants$360.PFNGLCREATEPROGRAMOBJECTARBPROC$FUNC, "()I");
    }
    static MemoryAddress allocate(PFNGLCREATEPROGRAMOBJECTARBPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLCREATEPROGRAMOBJECTARBPROC.class, fi, constants$360.PFNGLCREATEPROGRAMOBJECTARBPROC$FUNC, "()I", scope);
    }
    static PFNGLCREATEPROGRAMOBJECTARBPROC ofAddress(MemoryAddress addr) {
        return () -> {
            try {
                return (int)constants$360.PFNGLCREATEPROGRAMOBJECTARBPROC$MH.invokeExact((Addressable)addr);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


