// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$525 {

    static final FunctionDescriptor PFNGLSEPARABLEFILTER2DEXTPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_INT,
        C_INT,
        C_INT,
        C_INT,
        C_POINTER,
        C_POINTER
    );
    static final MethodHandle PFNGLSEPARABLEFILTER2DEXTPROC$MH = RuntimeHelper.downcallHandle(
        "(IIIIIILjdk/incubator/foreign/MemoryAddress;Ljdk/incubator/foreign/MemoryAddress;)V",
        constants$525.PFNGLSEPARABLEFILTER2DEXTPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLTANGENT3BEXTPROC$FUNC = FunctionDescriptor.ofVoid(
        C_CHAR,
        C_CHAR,
        C_CHAR
    );
    static final MethodHandle PFNGLTANGENT3BEXTPROC$MH = RuntimeHelper.downcallHandle(
        "(BBB)V",
        constants$525.PFNGLTANGENT3BEXTPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLTANGENT3BVEXTPROC$FUNC = FunctionDescriptor.ofVoid(
        C_POINTER
    );
    static final MethodHandle PFNGLTANGENT3BVEXTPROC$MH = RuntimeHelper.downcallHandle(
        "(Ljdk/incubator/foreign/MemoryAddress;)V",
        constants$525.PFNGLTANGENT3BVEXTPROC$FUNC, false
    );
}


