// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$797 {

    static final FunctionDescriptor PFNGLSTENCILTHENCOVERSTROKEPATHINSTANCEDNVPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_INT,
        C_POINTER,
        C_INT,
        C_INT,
        C_INT,
        C_INT,
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLSTENCILTHENCOVERSTROKEPATHINSTANCEDNVPROC$MH = RuntimeHelper.downcallHandle(
        "(IILjdk/incubator/foreign/MemoryAddress;IIIIILjdk/incubator/foreign/MemoryAddress;)V",
        constants$797.PFNGLSTENCILTHENCOVERSTROKEPATHINSTANCEDNVPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLPATHGLYPHINDEXRANGENVPROC$FUNC = FunctionDescriptor.of(C_INT,
        C_INT,
        C_POINTER,
        C_INT,
        C_INT,
        C_FLOAT,
        C_POINTER
    );
    static final MethodHandle PFNGLPATHGLYPHINDEXRANGENVPROC$MH = RuntimeHelper.downcallHandle(
        "(ILjdk/incubator/foreign/MemoryAddress;IIFLjdk/incubator/foreign/MemoryAddress;)I",
        constants$797.PFNGLPATHGLYPHINDEXRANGENVPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLPATHGLYPHINDEXARRAYNVPROC$FUNC = FunctionDescriptor.of(C_INT,
        C_INT,
        C_INT,
        C_POINTER,
        C_INT,
        C_INT,
        C_INT,
        C_INT,
        C_FLOAT
    );
    static final MethodHandle PFNGLPATHGLYPHINDEXARRAYNVPROC$MH = RuntimeHelper.downcallHandle(
        "(IILjdk/incubator/foreign/MemoryAddress;IIIIF)I",
        constants$797.PFNGLPATHGLYPHINDEXARRAYNVPROC$FUNC, false
    );
}


