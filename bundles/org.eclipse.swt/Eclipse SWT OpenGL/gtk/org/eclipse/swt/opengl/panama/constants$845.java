// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$845 {

    static final FunctionDescriptor PFNGLLOADPROGRAMNVPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLLOADPROGRAMNVPROC$MH = RuntimeHelper.downcallHandle(
        "(IIILjdk/incubator/foreign/MemoryAddress;)V",
        constants$845.PFNGLLOADPROGRAMNVPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLPROGRAMPARAMETER4DNVPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_DOUBLE,
        C_DOUBLE,
        C_DOUBLE,
        C_DOUBLE
    );
    static final MethodHandle PFNGLPROGRAMPARAMETER4DNVPROC$MH = RuntimeHelper.downcallHandle(
        "(IIDDDD)V",
        constants$845.PFNGLPROGRAMPARAMETER4DNVPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLPROGRAMPARAMETER4DVNVPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLPROGRAMPARAMETER4DVNVPROC$MH = RuntimeHelper.downcallHandle(
        "(IILjdk/incubator/foreign/MemoryAddress;)V",
        constants$845.PFNGLPROGRAMPARAMETER4DVNVPROC$FUNC, false
    );
}


