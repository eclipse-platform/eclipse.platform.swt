// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface PFNGLNAMEDBUFFERSTORAGEMEMEXTPROC {

    void apply(int x0, long x1, int x2, long x3);
    static MemoryAddress allocate(PFNGLNAMEDBUFFERSTORAGEMEMEXTPROC fi) {
        return RuntimeHelper.upcallStub(PFNGLNAMEDBUFFERSTORAGEMEMEXTPROC.class, fi, constants$647.PFNGLNAMEDBUFFERSTORAGEMEMEXTPROC$FUNC, "(IJIJ)V");
    }
    static MemoryAddress allocate(PFNGLNAMEDBUFFERSTORAGEMEMEXTPROC fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(PFNGLNAMEDBUFFERSTORAGEMEMEXTPROC.class, fi, constants$647.PFNGLNAMEDBUFFERSTORAGEMEMEXTPROC$FUNC, "(IJIJ)V", scope);
    }
    static PFNGLNAMEDBUFFERSTORAGEMEMEXTPROC ofAddress(MemoryAddress addr) {
        return (int x0, long x1, int x2, long x3) -> {
            try {
                constants$647.PFNGLNAMEDBUFFERSTORAGEMEMEXTPROC$MH.invokeExact((Addressable)addr, x0, x1, x2, x3);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


