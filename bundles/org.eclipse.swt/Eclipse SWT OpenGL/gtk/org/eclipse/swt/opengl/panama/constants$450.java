// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$450 {

    static final FunctionDescriptor PFNGLDEPTHRANGEFOESPROC$FUNC = FunctionDescriptor.ofVoid(
        C_FLOAT,
        C_FLOAT
    );
    static final MethodHandle PFNGLDEPTHRANGEFOESPROC$MH = RuntimeHelper.downcallHandle(
        "(FF)V",
        constants$450.PFNGLDEPTHRANGEFOESPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLFRUSTUMFOESPROC$FUNC = FunctionDescriptor.ofVoid(
        C_FLOAT,
        C_FLOAT,
        C_FLOAT,
        C_FLOAT,
        C_FLOAT,
        C_FLOAT
    );
    static final MethodHandle PFNGLFRUSTUMFOESPROC$MH = RuntimeHelper.downcallHandle(
        "(FFFFFF)V",
        constants$450.PFNGLFRUSTUMFOESPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLGETCLIPPLANEFOESPROC$FUNC = FunctionDescriptor.ofVoid(
        C_INT,
        C_POINTER
    );
    static final MethodHandle PFNGLGETCLIPPLANEFOESPROC$MH = RuntimeHelper.downcallHandle(
        "(ILjdk/incubator/foreign/MemoryAddress;)V",
        constants$450.PFNGLGETCLIPPLANEFOESPROC$FUNC, false
    );
}


