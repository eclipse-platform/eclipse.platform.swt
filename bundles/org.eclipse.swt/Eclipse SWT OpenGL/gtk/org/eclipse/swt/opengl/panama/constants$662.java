// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$662 {

    static final FunctionDescriptor PFNGLISSEMAPHOREEXTPROC$FUNC = FunctionDescriptor.of(C_CHAR,
        C_INT
    );
    static final MethodHandle PFNGLISSEMAPHOREEXTPROC$MH = RuntimeHelper.downcallHandle(
        "(I)B",
        constants$662.PFNGLISSEMAPHOREEXTPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLSEMAPHOREPARAMETERUI64VEXTPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLSEMAPHOREPARAMETERUI64VEXTPROC$MH = RuntimeHelper.downcallHandle(
        "(IILjdk/incubator/foreign/MemoryAddress;)V",
        constants$662.PFNGLSEMAPHOREPARAMETERUI64VEXTPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLGETSEMAPHOREPARAMETERUI64VEXTPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLGETSEMAPHOREPARAMETERUI64VEXTPROC$MH = RuntimeHelper.downcallHandle(
        "(IILjdk/incubator/foreign/MemoryAddress;)V",
        constants$662.PFNGLGETSEMAPHOREPARAMETERUI64VEXTPROC$FUNC, false
    );
}


