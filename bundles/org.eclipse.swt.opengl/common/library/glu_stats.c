#include "swt.h"
#include "glu_stats.h"

#ifdef NATIVE_STATS

int GLU_nativeFunctionCount = 51;
int GLU_nativeFunctionCallCount[51];
char * GLU_nativeFunctionNames[] = {
	"gluBeginCurve", 
	"gluBeginPolygon", 
	"gluBeginSurface", 
	"gluBeginTrim", 
	"gluBuild1DMipmaps", 
	"gluBuild2DMipmaps", 
	"gluCylinder", 
	"gluDeleteNurbsRenderer", 
	"gluDeleteQuadric", 
	"gluDeleteTess", 
	"gluDisk", 
	"gluEndCurve", 
	"gluEndPolygon", 
	"gluEndSurface", 
	"gluEndTrim", 
	"gluErrorString", 
	"gluGetNurbsProperty", 
	"gluGetString", 
	"gluGetTessProperty", 
	"gluLoadSamplingMatrices", 
	"gluLookAt", 
	"gluNewNurbsRenderer", 
	"gluNewQuadric", 
	"gluNewTess", 
	"gluNextContour", 
	"gluNurbsCallback", 
	"gluNurbsCurve", 
	"gluNurbsProperty", 
	"gluNurbsSurface", 
	"gluOrtho2D", 
	"gluPartialDisk", 
	"gluPerspective", 
	"gluPickMatrix", 
	"gluProject", 
	"gluPwlCurve", 
	"gluQuadricCallback", 
	"gluQuadricDrawStyle", 
	"gluQuadricNormals", 
	"gluQuadricOrientation", 
	"gluQuadricTexture", 
	"gluScaleImage", 
	"gluSphere", 
	"gluTessBeginContour", 
	"gluTessBeginPolygon", 
	"gluTessCallback", 
	"gluTessEndContour", 
	"gluTessEndPolygon", 
	"gluTessNormal", 
	"gluTessProperty", 
	"gluTessVertex", 
	"gluUnProject", 
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(GLU_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return GLU_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(GLU_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, GLU_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(GLU_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return GLU_nativeFunctionCallCount[index];
}

#endif
