// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$977 {

    static final FunctionDescriptor getsubopt$FUNC = FunctionDescriptor.of(C_INT,
        C_POINTER,
        C_POINTER,
        C_POINTER
    );
    static final MethodHandle getsubopt$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "getsubopt",
        "(Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)I",
        constants$977.getsubopt$FUNC, false
    );
    static final FunctionDescriptor getloadavg$FUNC = FunctionDescriptor.of(C_INT,
        C_POINTER,
        C_INT
    );
    static final MethodHandle getloadavg$MH = RuntimeHelper.downcallHandle(
        glut_h.LIBRARIES, "getloadavg",
        "(Ljdk/incubator/foreign/MemoryAddress;I)I",
        constants$977.getloadavg$FUNC, false
    );
    static final MemorySegment __PRI64_PREFIX$SEGMENT = CLinker.toCString("l", ResourceScope.newImplicitScope());
    static final MemorySegment __PRIPTR_PREFIX$SEGMENT = CLinker.toCString("l", ResourceScope.newImplicitScope());
    static final MemorySegment PRId8$SEGMENT = CLinker.toCString("d", ResourceScope.newImplicitScope());
    static final MemorySegment PRId16$SEGMENT = CLinker.toCString("d", ResourceScope.newImplicitScope());
}


