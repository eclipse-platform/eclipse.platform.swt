// Generated by jextract

package org.eclipse.swt.opengl.panama;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import jdk.incubator.foreign.*;
import static jdk.incubator.foreign.CLinker.*;
class constants$315 {

    static final FunctionDescriptor PFNGLGETIMAGEHANDLEARBPROC$FUNC = FunctionDescriptor.of(C_LONG,
        C_INT,
        C_INT,
        C_CHAR,
        C_INT,
        C_INT
    );
    static final MethodHandle PFNGLGETIMAGEHANDLEARBPROC$MH = RuntimeHelper.downcallHandle(
        "(IIBII)J",
        constants$315.PFNGLGETIMAGEHANDLEARBPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLMAKEIMAGEHANDLERESIDENTARBPROC$FUNC = FunctionDescriptor.ofVoid(
        C_LONG,
        C_INT
    );
    static final MethodHandle PFNGLMAKEIMAGEHANDLERESIDENTARBPROC$MH = RuntimeHelper.downcallHandle(
        "(JI)V",
        constants$315.PFNGLMAKEIMAGEHANDLERESIDENTARBPROC$FUNC, false
    );
    static final FunctionDescriptor PFNGLMAKEIMAGEHANDLENONRESIDENTARBPROC$FUNC = FunctionDescriptor.ofVoid(
        C_LONG
    );
    static final MethodHandle PFNGLMAKEIMAGEHANDLENONRESIDENTARBPROC$MH = RuntimeHelper.downcallHandle(
        "(J)V",
        constants$315.PFNGLMAKEIMAGEHANDLENONRESIDENTARBPROC$FUNC, false
    );
}


