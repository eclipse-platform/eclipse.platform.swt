#ifdef NATIVE_STATS
extern int GLU_nativeFunctionCount;
extern int GLU_nativeFunctionCallCount[];
extern char* GLU_nativeFunctionNames[];
#define GLU_NATIVE_ENTER(env, that, func) GLU_nativeFunctionCallCount[func]++;
#define GLU_NATIVE_EXIT(env, that, func) 
#else
#define GLU_NATIVE_ENTER(env, that, func) 
#define GLU_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	gluBeginCurve_FUNC,
	gluBeginPolygon_FUNC,
	gluBeginSurface_FUNC,
	gluBeginTrim_FUNC,
	gluBuild1DMipmaps_FUNC,
	gluBuild2DMipmaps_FUNC,
	gluCylinder_FUNC,
	gluDeleteNurbsRenderer_FUNC,
	gluDeleteQuadric_FUNC,
	gluDeleteTess_FUNC,
	gluDisk_FUNC,
	gluEndCurve_FUNC,
	gluEndPolygon_FUNC,
	gluEndSurface_FUNC,
	gluEndTrim_FUNC,
	gluErrorString_FUNC,
	gluGetNurbsProperty_FUNC,
	gluGetString_FUNC,
	gluGetTessProperty_FUNC,
	gluLoadSamplingMatrices_FUNC,
	gluLookAt_FUNC,
	gluNewNurbsRenderer_FUNC,
	gluNewQuadric_FUNC,
	gluNewTess_FUNC,
	gluNextContour_FUNC,
	gluNurbsCallback_FUNC,
	gluNurbsCurve_FUNC,
	gluNurbsProperty_FUNC,
	gluNurbsSurface_FUNC,
	gluOrtho2D_FUNC,
	gluPartialDisk_FUNC,
	gluPerspective_FUNC,
	gluPickMatrix_FUNC,
	gluProject_FUNC,
	gluPwlCurve_FUNC,
	gluQuadricCallback_FUNC,
	gluQuadricDrawStyle_FUNC,
	gluQuadricNormals_FUNC,
	gluQuadricOrientation_FUNC,
	gluQuadricTexture_FUNC,
	gluScaleImage_FUNC,
	gluSphere_FUNC,
	gluTessBeginContour_FUNC,
	gluTessBeginPolygon_FUNC,
	gluTessCallback_FUNC,
	gluTessEndContour_FUNC,
	gluTessEndPolygon_FUNC,
	gluTessNormal_FUNC,
	gluTessProperty_FUNC,
	gluTessVertex_FUNC,
	gluUnProject_FUNC,
} GLU_FUNCS;
