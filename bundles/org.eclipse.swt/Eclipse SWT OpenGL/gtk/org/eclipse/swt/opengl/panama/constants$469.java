// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$469 {

    static final FunctionDescriptor PFNGLGENNAMESAMDPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLGENNAMESAMDPROC$MH = RuntimeHelper.downcallHandle(
        "(IILjdk/incubator/foreign/MemoryAddress;)V",
        constants$469.PFNGLGENNAMESAMDPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLDELETENAMESAMDPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLDELETENAMESAMDPROC$MH = RuntimeHelper.downcallHandle(
        "(IILjdk/incubator/foreign/MemoryAddress;)V",
        constants$469.PFNGLDELETENAMESAMDPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLISNAMEAMDPROC$FUNC = FunctionDescriptor.of(C_CHAR,
        C_INT,
        C_INT
    );
    static final MethodHandle PFNGLISNAMEAMDPROC$MH = RuntimeHelper.downcallHandle(
        "(II)B",
        constants$469.PFNGLISNAMEAMDPROC$FUNC, false
    );
}


