// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
public interface glutIdleFunc$callback {

    void apply();
    static MemoryAddress allocate(glutIdleFunc$callback fi) {
        return RuntimeHelper.upcallStub(glutIdleFunc$callback.class, fi, constants$940.glutIdleFunc$callback$FUNC, "()V");
    }
    static MemoryAddress allocate(glutIdleFunc$callback fi, ResourceScope scope) {
        return RuntimeHelper.upcallStub(glutIdleFunc$callback.class, fi, constants$940.glutIdleFunc$callback$FUNC, "()V", scope);
    }
    static glutIdleFunc$callback ofAddress(MemoryAddress addr) {
        return () -> {
            try {
                constants$940.glutIdleFunc$callback$MH.invokeExact((Addressable)addr);
            } catch (Throwable ex$) {
                throw new AssertionError("should not reach here", ex$);
            }
        };
    }
}


