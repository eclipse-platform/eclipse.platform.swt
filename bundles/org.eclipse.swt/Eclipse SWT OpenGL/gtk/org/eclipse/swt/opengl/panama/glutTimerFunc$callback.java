// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface glutTimerFunc$callback {

    void apply(int x0);
    static MemoryAddress allocate(glutTimerFunc$callback fi) {
        return RuntimeHelper.upcallStub(glutTimerFunc$callback.class, fi, constants$939.glutTimerFunc$callback$FUNC, "(I)V");
    }
    static MemoryAddress allocate(glutTimerFunc$callback fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(glutTimerFunc$callback.class, fi, constants$939.glutTimerFunc$callback$FUNC, "(I)V", scope);
    }
    static glutTimerFunc$callback ofAddress(MemoryAddress addr) {
        return (int x0) -> {
            try {
                constants$939.glutTimerFunc$callback$MH.invokeExact((Addressable)addr, x0);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


