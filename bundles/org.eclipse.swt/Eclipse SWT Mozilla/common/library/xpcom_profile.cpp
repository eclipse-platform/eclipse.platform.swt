#include "nsCOMPtr.h"
#include "nsProfileDirServiceProvider.h"
#include "jni.h"
#include "xpcom_stats.h"

extern "C" {

#define XPCOM_NATIVE(func) Java_org_eclipse_swt_internal_mozilla_XPCOM_##func

#ifndef NO_NS_1NewProfileDirServiceProvider
JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1NewProfileDirServiceProvider)
	(JNIEnv *env, jclass that, jboolean arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, NS_1NewProfileDirServiceProvider_FUNC);
	if (arg1) if ((lparg1 = env->GetIntArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jint)NS_NewProfileDirServiceProvider(arg0, (nsProfileDirServiceProvider**)lparg1);
fail:
	if (arg1 && lparg1) env->ReleaseIntArrayElements(arg1, lparg1, 0);
	XPCOM_NATIVE_EXIT(env, that, NS_1NewProfileDirServiceProvider_FUNC);
	return rc;
}
#endif


#ifndef NO_ProfileDirServiceProvider_1Register
JNIEXPORT jint JNICALL XPCOM_NATIVE(ProfileDirServiceProvider_1Register)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, ProfileDirServiceProvider_1Register_FUNC);
	rc = (jint)((nsProfileDirServiceProvider *)arg0)->Register();
	XPCOM_NATIVE_EXIT(env, that, ProfileDirServiceProvider_1Register_FUNC);
	return rc;
}
#endif

#ifndef NO_ProfileDirServiceProvider_1SetProfileDir
JNIEXPORT jint JNICALL XPCOM_NATIVE(ProfileDirServiceProvider_1SetProfileDir)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, ProfileDirServiceProvider_1SetProfileDir_FUNC);
	rc = (jint)((nsProfileDirServiceProvider *)arg0)->SetProfileDir((nsIFile *)arg1);
	XPCOM_NATIVE_EXIT(env, that, ProfileDirServiceProvider_1SetProfileDir_FUNC);
	return rc;
}
#endif

#ifndef NO_ProfileDirServiceProvider_1Shutdown
JNIEXPORT jint JNICALL XPCOM_NATIVE(ProfileDirServiceProvider_1Shutdown)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, ProfileDirServiceProvider_1Shutdown_FUNC);
	rc = (jint)((nsProfileDirServiceProvider *)arg0)->Shutdown();
	XPCOM_NATIVE_EXIT(env, that, ProfileDirServiceProvider_1Shutdown_FUNC);
	return rc;
}
#endif

}
