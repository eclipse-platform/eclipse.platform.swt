/*
 * (c) Copyright IBM Corp. 2002.
 * All Rights Reserved
 *
 * Andre Weinand, OTI - Initial version
 */

#ifndef _Included_JNIOut
#include "org_eclipse_swt_internal_carbon_OS.h"
#endif

#include <Carbon/Carbon.h>
	
#define USE_ATSUI 1

static const Rect NULL_RECT;

// forward declarations
static Point point(JNIEnv *env, jshortArray a);
static void copyEvent(JNIEnv *env, jintArray eData, EventRecord *event);
static void copyEventData(JNIEnv *env, EventRecord *event, jintArray eData);
static void setColor(struct RGBColor *c, int rgb);

#define RC(f) checkStatus(__LINE__, (f))

static int checkStatus(int line, int rc) {
    switch (rc) {
	case 0:
	case eventNotHandledErr:
	case eventLoopTimedOutErr:
	case errControlIsNotEmbedder:
		break;
	default:
        fprintf(stderr, "OS: line: %d %d\n", line, rc);
		break;
	}
    return rc;
}


// callbacks
struct Closure {
	JNIEnv *env;
	jobject target;
	jmethodID action;
};

static struct Closure *createClosure(JNIEnv *env, jobject target, jstring method, const char *signature) {
	const char *name;
	jclass clazz;
	struct Closure *closure= malloc(sizeof(struct Closure));
	closure->env= env;
	closure->target= (*env)->NewGlobalRef(env, target);
	clazz= (*env)->GetObjectClass(env, target);
	name= (*env)->GetStringUTFChars(env, method, NULL);
	closure->action= (*env)->GetMethodID(env, clazz, name, signature);
	if (closure->action == 0) {
		fprintf(stderr, "can't find method: %s%s\n", name, signature);
	}
	(*env)->ReleaseStringUTFChars(env, method, name);

	return closure;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlFontStyle(JNIEnv *env, jclass zz,
				jint cHandle, jshort font, jshort size, jshort style) {
	ControlFontStyleRec fontRec;
	fontRec.flags= kControlUseFontMask | kControlUseSizeMask | kControlUseFaceMask;
	fontRec.font= font;
	fontRec.size= size;
	fontRec.style= style;
	return (jint) RC(SetControlFontStyle((ControlRef) cHandle, &fontRec));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetUpControlBackground(JNIEnv *env, jclass zz,
				jint cHandle, jshort depth, jboolean isColorDevice) {
	return (jint) RC(SetUpControlBackground((ControlRef) cHandle, depth, isColorDevice));
}

//---- callback.c

#define PLATFORM "carbon"

JNIEXPORT jstring JNICALL Java_org_eclipse_swt_internal_Callback_getPlatform(JNIEnv *env, jclass that) {
	return (*env)->NewStringUTF(env, PLATFORM);
}

//---- fonts

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_FMGetFontFamilyFromName(JNIEnv *env, jclass zz,
			jbyteArray name) {
	jbyte *sa= (*env)->GetByteArrayElements(env, name, 0);
	jshort id= (jshort) FMGetFontFamilyFromName((ConstStr255Param) sa);
	(*env)->ReleaseByteArrayElements(env, name, sa, 0);
	return id;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_FMGetFontFamilyName(JNIEnv *env, jclass zz,
			jshort id, jbyteArray name) {
	Str255 s;
	jint status= (jshort) RC(FMGetFontFamilyName((FMFontFamily) id, s));
	jbyte *sa= (*env)->GetByteArrayElements(env, name, 0);
	memcpy(sa, s, 255);
	(*env)->ReleaseByteArrayElements(env, name, sa, 0);
	return status;
}

void lisAllFonts() {
	FMFontFamilyIterator iter;
	FMFontFamily family;
	FMCreateFontFamilyIterator(NULL, NULL, kFMUseGlobalScopeOption, &iter);

	while (FMGetNextFontFamily(&iter, &family) != kFMIterationCompleted) {
		Str255 name;
		FMGetFontFamilyName(family, name);
		name[name[0]+1]= 0;
		fprintf(stderr, "fontfamily: %s\n", &name[1]);
	}
	FMDisposeFontFamilyIterator(&iter);
}

//---- Appearance Manager

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RegisterAppearanceClient(JNIEnv *env, jclass zz) {
	return (jint) RC(RegisterAppearanceClient());
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetThemeWindowBackground(JNIEnv *env, jclass zz,
				jint wHandle, jshort brush, jboolean update) {
	return (jint) RC(SetThemeWindowBackground((WindowRef) wHandle, (ThemeBrush) brush, (Boolean) update));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeTextBox(JNIEnv *env, jclass zz,
				jint sHandle, jshort fontID, jint state, jboolean wrapToWidth, jshortArray bounds, jshort just, jint context) {

    jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	jint status= RC(DrawThemeTextBox(
		(CFStringRef)      sHandle,
                (ThemeFontID)      fontID,
		(ThemeDrawState)   state,
		(Boolean)          wrapToWidth,
		(const Rect *)     sa,
		(SInt16)           just,
		(void *)           context));
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetThemeTextDimensions(JNIEnv *env, jclass zz,
				jint sHandle, jshort fontID, jint state, jboolean wrapToWidth, jshortArray size, jshortArray baseLine) {

    jshort *sa= (*env)->GetShortArrayElements(env, size, 0);
	jint status= RC(GetThemeTextDimensions(
		(CFStringRef)      sHandle,
		(ThemeFontID)      fontID,
		(ThemeDrawState)   state,
		(Boolean)          wrapToWidth,
		(Point *)     	   sa,
		(SInt16*)          baseLine));
	(*env)->ReleaseShortArrayElements(env, size, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeEditTextFrame(JNIEnv *env, jclass zz,
				jshortArray bounds, jint state) {
	jint *sa= (*env)->GetIntArrayElements(env, bounds, 0);
	OSStatus status= RC(DrawThemeEditTextFrame((Rect*)sa, state));
	(*env)->ReleaseIntArrayElements(env, bounds, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeFocusRect(JNIEnv *env, jclass zz,
				jshortArray bounds, jboolean hasFocus) {
	jint *sa= (*env)->GetIntArrayElements(env, bounds, 0);
	OSStatus status= RC(DrawThemeFocusRect((Rect*)sa, hasFocus));
	(*env)->ReleaseIntArrayElements(env, bounds, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeGenericWell(JNIEnv *env, jclass zz,
				jshortArray bounds, jint state, jboolean fillCenter) {
	jint *sa= (*env)->GetIntArrayElements(env, bounds, 0);
	OSStatus status= RC(DrawThemeGenericWell((Rect*)sa, state, fillCenter));
	(*env)->ReleaseIntArrayElements(env, bounds, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeSeparator(JNIEnv *env, jclass zz,
				jshortArray bounds, jint state) {
	jint *sa= (*env)->GetIntArrayElements(env, bounds, 0);
	OSStatus status= RC(DrawThemeSeparator((Rect*)sa, state));
	(*env)->ReleaseIntArrayElements(env, bounds, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetThemeFont(JNIEnv *env, jclass zz,
				jshort themeFontId, jshort scriptCode, jbyteArray fontName, jshortArray fontSize, jbyteArray style) {
	jbyte *sa= NULL;
	jshort * sb= NULL;
	jbyte *sc= NULL;
	jint status= 0;
	if (fontName != NULL)
		sa= (*env)->GetByteArrayElements(env, fontName, 0);
	sb= (*env)->GetShortArrayElements(env, fontSize, 0);
	sc= (*env)->GetByteArrayElements(env, style, 0);
	status= (jint) RC(GetThemeFont((ThemeFontID)themeFontId, (ScriptCode)scriptCode, (unsigned char*)sa, (SInt16*)sb, (Style*)sc));
	if (sa != NULL)
		(*env)->ReleaseByteArrayElements(env, fontName, sa, 0);
	(*env)->ReleaseShortArrayElements(env, fontSize, sb, 0);
	(*env)->ReleaseByteArrayElements(env, style, sc, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawThemeButton(JNIEnv *env, jclass zz,
				jshortArray bounds, jshort kind, jshortArray newInfoArray, jshortArray prevInfoArray, jint eraseProc,
					jint labelProc, jint userData) {
	ThemeButtonDrawInfo info;
	ThemeButtonDrawInfo newInfo, *prevInfo= NULL;	
	jint status= 0;
	jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	jshort *sb= (*env)->GetShortArrayElements(env, newInfoArray, 0);
	jshort *sc= NULL;
	
	
	newInfo.state= sb[0];
	newInfo.value= sb[1];
	newInfo.adornment= sb[2];
	
	if (prevInfoArray != NULL) {
		sc= (*env)->GetShortArrayElements(env, prevInfoArray, 0);
		info.state= sc[0];
		info.value= sc[1];
		info.adornment= sc[2];
		prevInfo= &info;
	}

	status= (jint) RC(DrawThemeButton((const Rect *)sa, (ThemeButtonKind)kind, &newInfo, prevInfo, (ThemeEraseUPP)eraseProc,
						(ThemeButtonDrawUPP) labelProc, (UInt32)userData));
						
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
	(*env)->ReleaseShortArrayElements(env, newInfoArray, sb, 0);
	if (sc != NULL)
		(*env)->ReleaseShortArrayElements(env, prevInfoArray, sc, 0);
	
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetThemeBackground(JNIEnv *env, jclass zz,
				jshort brush, jshort depth, jboolean isColorDevice) {
	return RC(SetThemeBackground(brush, depth, isColorDevice));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetThemeDrawingState(JNIEnv *env, jclass zz,
				jintArray state) {
	jint *sa= (*env)->GetIntArrayElements(env, state, 0);
	jint status= (jint) RC(GetThemeDrawingState((ThemeDrawingState*) sa));
	(*env)->ReleaseIntArrayElements(env, state, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetThemeDrawingState(JNIEnv *env, jclass zz,
				jint state, jboolean disposeNow) {
	return RC(SetThemeDrawingState((ThemeDrawingState)state, disposeNow));
}

//---- tabs

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateTabFolderControl(JNIEnv *env, jclass zz,
				jint wHandle, jintArray outControl) {

    jint *sa= NULL;
	OSStatus status;
	if (outControl != 0)
		sa= (*env)->GetIntArrayElements(env, outControl, 0);
	status= RC(CreateTabsControl((WindowRef) wHandle, &NULL_RECT, kControlTabSizeSmall, kControlTabDirectionNorth,  0, NULL, (ControlRef*)sa));
	if (sa != NULL)
		(*env)->ReleaseIntArrayElements(env, outControl, sa, 0);
	return (jint)status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_setTabText(JNIEnv *env, jclass zz,
				jint cHandle, jint index, jint sHandle) {
	ControlTabInfoRecV1 tab;
			
	tab.version= kControlTabInfoVersionOne;
	tab.iconSuiteID= 0;
	tab.name= (CFStringRef) sHandle;
	return RC(SetControlData((ControlRef)cHandle, index, kControlTabInfoTag, sizeof(ControlTabInfoRecV1), &tab));
}

//---- Combo

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlPopupMenuHandle(JNIEnv *env, jclass zz,
				jint cHandle, jint mHandle) {
	SetControlPopupMenuHandle((ControlRef)cHandle, (MenuRef) mHandle);
}

//---- DataBrowser

static struct Closure *gDataBrowserDataCallbackClosure;
static OSStatus dataBrowserDataCallback(ControlRef browser, DataBrowserItemID item, DataBrowserPropertyID property,
								DataBrowserItemDataRef itemData, Boolean setValue) {
        if (gDataBrowserDataCallbackClosure != NULL) {
            JNIEnv *env= gDataBrowserDataCallbackClosure->env;
            return (*env)->CallIntMethod(env, gDataBrowserDataCallbackClosure->target, gDataBrowserDataCallbackClosure->action,
						(jint)browser, (jint)item, (jint)property, (jint)itemData);
        }
        return noErr;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewDataBrowserDataCallbackUPP(JNIEnv *env, jclass zz,
					jobject target, jstring method) {
					
	if (gDataBrowserDataCallbackClosure != NULL) {
		fprintf(stderr, "NewDataBrowserDataCallbackUPP: can't install more than one callback\n");
		exit(-1);
	}
	gDataBrowserDataCallbackClosure= createClosure(env, target, method, "(IIII)I");
	return (jint) NewDataBrowserItemDataUPP(dataBrowserDataCallback);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_setDataBrowserItemDataCallback(JNIEnv *env, jclass zz,
					jint cHandle, jint upp) {
	DataBrowserCallbacks callbacks;
	GetDataBrowserCallbacks((ControlRef) cHandle, &callbacks);
	callbacks.u.v1.itemDataCallback= (DataBrowserItemDataUPP) upp;
	SetDataBrowserCallbacks((ControlRef) cHandle, &callbacks);
}

///////////////////

static struct Closure *gDataBrowserItemNotificationCallbackClosure;
static void dataBrowserItemNotificationCallback(ControlRef browser, DataBrowserItemID item, DataBrowserItemNotification message) {
	if (gDataBrowserItemNotificationCallbackClosure != NULL) {
		JNIEnv *env= gDataBrowserItemNotificationCallbackClosure->env;
		(*env)->CallIntMethod(env, gDataBrowserItemNotificationCallbackClosure->target, gDataBrowserItemNotificationCallbackClosure->action,
						(jint)browser, (jint)item, (jint)message);
	}
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewDataBrowserItemNotificationCallbackUPP(JNIEnv *env, jclass zz,
					jobject target, jstring method) {
					
	if (gDataBrowserItemNotificationCallbackClosure != NULL) {
		fprintf(stderr, "NewDataBrowserItemNotificationCallbackUPP: can't install more than one callback\n");
		exit(-1);
	}
	gDataBrowserItemNotificationCallbackClosure= createClosure(env, target, method, "(III)I");
	return (jint) NewDataBrowserItemNotificationUPP(dataBrowserItemNotificationCallback);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_setDataBrowserItemNotificationCallback(JNIEnv *env, jclass zz,
						jint cHandle, jint upp) {
	DataBrowserCallbacks callbacks;
	GetDataBrowserCallbacks((ControlRef) cHandle, &callbacks);
	callbacks.u.v1.itemNotificationCallback= (DataBrowserItemNotificationUPP) upp;
	SetDataBrowserCallbacks((ControlRef) cHandle, &callbacks);
}

///////////////////

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_createDataBrowserControl(JNIEnv *env, jclass zz, jint wHandle) {
	ControlRef controlRef;
	DataBrowserCallbacks callbacks;
	
	CreateDataBrowserControl((WindowRef)wHandle, &NULL_RECT, kDataBrowserListView, &controlRef);
	
	callbacks.version= kDataBrowserLatestCallbacks;
	InitDataBrowserCallbacks(&callbacks);
	SetDataBrowserCallbacks(controlRef, &callbacks);
		
	return (jint) controlRef;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_newColumnDesc(JNIEnv *env, jclass zz,
			jint propertyID, jint propertyType, jint propertyFlags, jshort minimumWidth, jshort maximumWidth) {

	DataBrowserListViewColumnDesc *columnDesc= (DataBrowserListViewColumnDesc*) calloc(sizeof(DataBrowserListViewColumnDesc), 1);
			
	columnDesc->propertyDesc.propertyID= propertyID;
	columnDesc->propertyDesc.propertyType= propertyType;
	columnDesc->propertyDesc.propertyFlags= propertyFlags;
	
	columnDesc->headerBtnDesc.version= kDataBrowserListViewLatestHeaderDesc;
	columnDesc->headerBtnDesc.minimumWidth= minimumWidth;
	columnDesc->headerBtnDesc.maximumWidth= maximumWidth;
	
	columnDesc->headerBtnDesc.titleOffset= 0;
	columnDesc->headerBtnDesc.titleString= NULL;
	
	/*
	columnDesc.headerBtnDesc.titleAlignment= teCenter;
	columnDesc.headerBtnDesc.titleFontTypeID= kControlFontViewSystemFont;
	columnDesc.headerBtnDesc.btnFontStyle= normal;
	*/

	return (jint)columnDesc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserActiveItems(JNIEnv *env, jclass zz,
		jint cHandle, jboolean active) {
	return RC(SetDataBrowserActiveItems((ControlRef) cHandle, (Boolean)active));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AddDataBrowserItems(JNIEnv *env, jclass zz,
						jint cHandle, jint containerID, jint numItems, jintArray items, jint sortProperty) {
    jint *sa= NULL;
	OSStatus status;
	if (items != 0)
		sa= (*env)->GetIntArrayElements(env, items, 0);
	status= RC(AddDataBrowserItems((ControlRef) cHandle, containerID, numItems, (const DataBrowserItemID*) sa, sortProperty));
	if (sa != NULL)
		(*env)->ReleaseIntArrayElements(env, items, sa, 0);
	return (jint) status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RemoveDataBrowserItems(JNIEnv *env, jclass zz,
						jint cHandle, jint containerID, jint numItems, jintArray items, jint sortProperty) {
    jint *sa= NULL;
	OSStatus status;
	if (items != 0)
		sa= (*env)->GetIntArrayElements(env, items, 0);
	status= RC(RemoveDataBrowserItems((ControlRef) cHandle, containerID, numItems, (const DataBrowserItemID*) sa, sortProperty));
	if (sa != NULL)
		(*env)->ReleaseIntArrayElements(env, items, sa, 0);
	return (jint) status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserItemDataText(JNIEnv *env, jclass zz, jint itemId, jint sHandle) {
	return (jint) RC(SetDataBrowserItemDataText((DataBrowserItemDataRef) itemId, (CFStringRef)sHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserHasScrollBars(JNIEnv *env, jclass zz,	
				jint cHandle, jboolean hScroll, jboolean vScroll) {
	return RC(SetDataBrowserHasScrollBars((ControlRef) cHandle, hScroll, vScroll));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserListViewHeaderBtnHeight(JNIEnv *env, jclass zz,
				jint cHandle, jshort height) {
	return RC(SetDataBrowserListViewHeaderBtnHeight((ControlRef) cHandle, height));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AddDataBrowserListViewColumn(JNIEnv *env, jclass zz,
				jint cHandle, jint handle, jint index) {
	return RC(AddDataBrowserListViewColumn((ControlRef)cHandle, (DataBrowserListViewColumnDesc*)handle, (DataBrowserTableViewColumnIndex)index));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_UpdateDataBrowserItems(JNIEnv *env, jclass zz,
				jint cHandle, jint container, jint numItems, jintArray items, jint preSortProperty, jint propertyID) {
    jint *sa= NULL;
	OSStatus status;
	if (items != 0)
		sa= (*env)->GetIntArrayElements(env, items, 0);
	status= RC(UpdateDataBrowserItems((ControlRef)cHandle, (DataBrowserItemID)container, numItems, sa,
				(DataBrowserPropertyID)preSortProperty, (DataBrowserPropertyID) propertyID));
	if (sa != NULL)
		(*env)->ReleaseIntArrayElements(env, items, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserSelectionFlags(JNIEnv *env, jclass zz,
				jint cHandle, jint selectionFlags) {
	return RC(SetDataBrowserSelectionFlags((ControlRef)cHandle, (DataBrowserSelectionFlags) selectionFlags));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDataBrowserItemCount(JNIEnv *env, jclass zz,
				jint cHandle, jint container, jboolean recurse, jint state, jintArray numItems) {
	jint *sa= (*env)->GetIntArrayElements(env, numItems, 0);
	OSStatus status= RC(GetDataBrowserItemCount((ControlRef)cHandle, (DataBrowserItemID)container, recurse, state, sa));
	(*env)->ReleaseIntArrayElements(env, numItems, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetDataBrowserSelectedItems(JNIEnv *env, jclass zz,
				jint cHandle, jint numItems, jintArray items, jint operation) {
	jint *sa= (*env)->GetIntArrayElements(env, items, 0);
	OSStatus status= RC(SetDataBrowserSelectedItems((ControlRef)cHandle, numItems, sa, operation));
	(*env)->ReleaseIntArrayElements(env, items, sa, 0);
	return status;
}

//---- events

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CallNextEventHandler(JNIEnv *env, jclass zz,
				jint nextHandler, jint eventRefHandle) {
	return (jint) CallNextEventHandler((EventHandlerCallRef) nextHandler, (EventRef) eventRefHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3I_3I_3C(JNIEnv *env, jclass zz,
			jint eRefHandle, jint paramName, jint paramType, jintArray outParamType, jintArray outActualSize, jcharArray data) {
	jint status;
    jint *sa= NULL;
    jint *sb= NULL;
    jchar *sc= NULL;
	int size= 0;
	
	if (outParamType != NULL)
		sa= (*env)->GetIntArrayElements(env, outParamType, 0);
	if (outActualSize != NULL)
		sb= (*env)->GetIntArrayElements(env, outActualSize, 0);
	if (data != NULL) {
		sc= (*env)->GetCharArrayElements(env, data, 0);
		size= (*env)->GetArrayLength(env, data);
	}
	
	status= (jint) RC(GetEventParameter((EventRef)eRefHandle, (EventParamName)paramName, (EventParamType)paramType, 
			(EventParamType*)sa, size * sizeof(jchar), (UInt32*)sb, (void*)sc));
	
	if (sa != NULL)
		(*env)->ReleaseIntArrayElements(env, outParamType, sa, 0);
	if (sb != NULL)
		(*env)->ReleaseIntArrayElements(env, outActualSize, sb, 0);
	if (sc != NULL)
		(*env)->ReleaseCharArrayElements(env, data, sc, 0);

	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3I_3I_3S(JNIEnv *env, jclass zz,
			jint eRefHandle, jint paramName, jint paramType, jintArray outParamType, jintArray outActualSize, jshortArray data) {
	jint status;
    jint *sa= NULL;
    jint *sb= NULL;
    jshort *sc= NULL;
	int size= 0;
	
	if (outParamType != NULL)
		sa= (*env)->GetIntArrayElements(env, outParamType, 0);
	if (outActualSize != NULL)
		sb= (*env)->GetIntArrayElements(env, outActualSize, 0);
	if (data != NULL) {
		sc= (*env)->GetShortArrayElements(env, data, 0);
		size= (*env)->GetArrayLength(env, data);
	}
	
	status= (jint) RC(GetEventParameter((EventRef)eRefHandle, (EventParamName)paramName, (EventParamType)paramType, 
			(EventParamType*)sa, size * sizeof(jshort), (UInt32*)sb, (void*)sc));
	
	if (sa != NULL)
		(*env)->ReleaseIntArrayElements(env, outParamType, sa, 0);
	if (sb != NULL)
		(*env)->ReleaseIntArrayElements(env, outActualSize, sb, 0);
	if (sc != NULL)
		(*env)->ReleaseShortArrayElements(env, data, sc, 0);

	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventParameter__III_3I_3I_3I(JNIEnv *env, jclass zz,
			jint eRefHandle, jint paramName, jint paramType, jintArray outParamType, jintArray outActualSize, jintArray data) {
	jint status;
    jint *sa= NULL;
    jint *sb= NULL;
    jint *sc= NULL;
	int size= 0;
	
	if (outParamType != NULL)
		sa= (*env)->GetIntArrayElements(env, outParamType, 0);
	if (outActualSize != NULL)
		sb= (*env)->GetIntArrayElements(env, outActualSize, 0);
	if (data != NULL) {
		sc= (*env)->GetIntArrayElements(env, data, 0);
		size= (*env)->GetArrayLength(env, data);
	}
	
	status= (jint) RC(GetEventParameter((EventRef)eRefHandle, (EventParamName)paramName, (EventParamType)paramType, 
			(EventParamType*)sa, size * sizeof(jint), (UInt32*)sb, (void*)sc));
	
	if (sa != NULL)
		(*env)->ReleaseIntArrayElements(env, outParamType, sa, 0);
	if (sb != NULL)
		(*env)->ReleaseIntArrayElements(env, outActualSize, sb, 0);
	if (sc != NULL)
		(*env)->ReleaseIntArrayElements(env, data, sc, 0);

	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetEventParameter(JNIEnv *env, jclass zz,
			jint eRefHandle, jint paramName, jint paramType, jcharArray data) {
	jint status;
    jchar *sc= NULL;
	int size= 0;
	
	if (data != NULL) {
		sc= (*env)->GetCharArrayElements(env, data, 0);
		size= (*env)->GetArrayLength(env, data);
	}
	
	status= (jint) RC(SetEventParameter((EventRef)eRefHandle, (EventParamName)paramName, (EventParamType)paramType, 
			size * sizeof(jchar), (void*)sc));
	
	if (sc != NULL)
		(*env)->ReleaseCharArrayElements(env, data, sc, 0);

	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InstallEventHandler(JNIEnv *env, jclass zz,
						jint eventTargetRef, jint eventHandlerUPP, jintArray eventTypes, jint clientData) {
    jint *sa= (*env)->GetIntArrayElements(env, eventTypes, 0);
    jsize length= (*env)->GetArrayLength(env, eventTypes);
	jint status;
	
	status= (jint) RC(InstallEventHandler(
			(EventTargetRef) eventTargetRef, 
			(EventHandlerUPP) eventHandlerUPP, 
			length/2, (EventTypeSpec*) sa,
			(void*) clientData,
			NULL
		));
		
	(*env)->ReleaseIntArrayElements(env, eventTypes, sa, 0);
		
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlEventTarget(JNIEnv *env, jclass zz, jint cHandle) {
	return (jint) GetControlEventTarget((ControlRef) cHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuEventTarget(JNIEnv *env, jclass zz, jint mHandle) {
	return (jint) GetMenuEventTarget((MenuRef) mHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetUserFocusEventTarget(JNIEnv *env, jclass zz) {
	return (jint) GetUserFocusEventTarget();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetApplicationEventTarget(JNIEnv *env, jclass zz) {
	return (jint) GetApplicationEventTarget();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetUserFocusWindow(JNIEnv *env, jclass zz) {
	return (jint) GetUserFocusWindow();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_EventAvail(JNIEnv *env, jclass zz, jshort mask, jint eHandle) {
	return (jboolean) EventAvail(mask, (EventRecord*) eHandle);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetNextEvent(JNIEnv *env, jclass zz, jshort mask, jintArray eData) {
	EventRecord event;
	jboolean rc= GetNextEvent(mask, &event);
	copyEvent(env, eData, &event);
	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_WaitNextEvent(JNIEnv *env, jclass zz, jshort mask, jintArray eData, jint sleeptime) {
	EventRecord event;
	jboolean rc= WaitNextEvent(mask, &event, sleeptime, nil);	
	copyEvent(env, eData, &event);
	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_StillDown(JNIEnv *env, jclass zz) {
	return (jboolean) StillDown();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMouse(JNIEnv *env, jclass zz, jshortArray where) {
	jshort *sa= (*env)->GetShortArrayElements(env, where, 0);
	GetMouse((struct Point*)sa);
	(*env)->ReleaseShortArrayElements(env, where, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_AEProcessAppleEvent(JNIEnv *env, jclass zz,
				jintArray eventData) {
	EventRecord event;
	copyEventData(env, &event, eventData);
	AEProcessAppleEvent(&event);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_MenuEvent(JNIEnv *env, jclass zz,
				jintArray eventData) {
	EventRecord event;
	copyEventData(env, &event, eventData);
	return (jint) RC(MenuEvent(&event));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_PostEvent(JNIEnv *env, jclass zz, jshort kind, jint message) {
	return RC(PostEvent(kind, message));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetKeyboardFocus(JNIEnv *env, jclass zz, jint wHandle, jintArray cHandle) {
    jint *sa= (*env)->GetIntArrayElements(env, cHandle, 0);
	int rc= RC(GetKeyboardFocus((WindowRef) wHandle, (ControlRef*) sa));
	(*env)->ReleaseIntArrayElements(env, cHandle, sa, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetKeyboardFocus(JNIEnv *env, jclass zz,
			jint wHandle, jint cHandle, jshort part) {
	return (jint) RC(SetKeyboardFocus((WindowRef) wHandle, (ControlRef) cHandle, part));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetCurrentEventLoop(JNIEnv *env, jclass zz) {
	return (jint) GetCurrentEventLoop();
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsShowContextualMenuClick(JNIEnv *env, jclass zz, jintArray eventData) {
	EventRecord event;
	copyEventData(env, &event, eventData);
	return (jboolean) IsShowContextualMenuClick(&event);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ContextualMenuSelect(JNIEnv *env, jclass zz,
			jint mHandle, jshortArray location, jshortArray menuId, jshortArray index) {
	UInt32 outUserSelectionType;	
    jshort *sa= (*env)->GetShortArrayElements(env, menuId, 0);
    jshort *sb= (*env)->GetShortArrayElements(env, index, 0);
	jint status= RC(ContextualMenuSelect(
				(MenuRef) mHandle,
				point(env, location), 
				false, 
				0, // kCMHelpItemOtherHelp, 
				0, // "\pinHelpItemString", 
				NULL, 
				&outUserSelectionType, 
				(SInt16*) sa, 
				(MenuItemIndex*) sb));
	(*env)->ReleaseShortArrayElements(env, menuId, sa, 0);
	(*env)->ReleaseShortArrayElements(env, index, sb, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InstallEventLoopTimer(JNIEnv *env, jclass zz,
		jint inEventLoop, jdouble inFireDelay, jdouble inInterval, jint inTimerProc, jint inTimerData, jintArray outTimer) {
	jint *sa= (*env)->GetIntArrayElements(env, outTimer, 0);
	int rc= RC(InstallEventLoopTimer((EventLoopRef) inEventLoop, (EventTimerInterval) inFireDelay, (EventTimerInterval) inInterval,
                (EventLoopTimerUPP) inTimerProc, (void*) inTimerData, (EventLoopTimerRef*) sa));
	(*env)->ReleaseIntArrayElements(env, outTimer, sa, 0);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RemoveEventLoopTimer(JNIEnv *env, jclass zz, jint timer) {
	return RC(RemoveEventLoopTimer((EventLoopTimerRef) timer));
}

JNIEXPORT jdouble JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetLastUserEventTime(JNIEnv *env, jclass zz) {
	return GetLastUserEventTime();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ReceiveNextEvent(JNIEnv *env, jclass zz,
		jintArray eventTypeSpecList, jdouble inTimeout, jboolean inPullEvent, jintArray outEvent) {
	jint status;
	jint *sa= NULL, *sb= NULL;
	UInt32 inNumTypes= 0;
	if (eventTypeSpecList != NULL) {
		sa= (*env)->GetIntArrayElements(env, eventTypeSpecList, 0);
		inNumTypes= (*env)->GetArrayLength(env, eventTypeSpecList)/2;
	}
	if (outEvent != NULL)
		sb= (*env)->GetIntArrayElements(env, outEvent, 0);
	status= (jint) RC(ReceiveNextEvent(inNumTypes, (const EventTypeSpec*) sa, inTimeout, inPullEvent, (EventRef*) sb));
	if (sa != NULL)
		(*env)->ReleaseIntArrayElements(env, eventTypeSpecList, sa, 0);	
	if (sb != NULL)
		(*env)->ReleaseIntArrayElements(env, outEvent, sb, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventDispatcherTarget(JNIEnv *env, jclass zz) {
	return (jint) GetEventDispatcherTarget();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SendEventToEventTarget(JNIEnv *env, jclass zz, jint eHandle, jint target) {
	return (jint) RC(SendEventToEventTarget((EventRef)eHandle, (EventTargetRef) target));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ReleaseEvent(JNIEnv *env, jclass zz, jint eHandle) {
	ReleaseEvent((EventRef)eHandle);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_ConvertEventRefToEventRecord(JNIEnv *env, jclass zz, jint eHandle, jintArray data) {
	EventRecord event;
	jboolean rc= ConvertEventRefToEventRecord((EventRef) eHandle, &event);
	copyEvent(env, data, &event);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InstallStandardEventHandler(JNIEnv *env, jclass zz, jint target) {
	return (jint) RC(InstallStandardEventHandler((EventTargetRef) target));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowEventTarget(JNIEnv *env, jclass zz, jint wHandle) {
	return (jint) GetWindowEventTarget((WindowRef) wHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventClass(JNIEnv *env, jclass zz, jint eHandle) {
	return (jint) GetEventClass((EventRef) eHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetEventKind(JNIEnv *env, jclass zz, jint eHandle) {
	return (jint) GetEventKind((EventRef) eHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMouseLocation(JNIEnv *env, jclass zz, jint eHandle, jshortArray loc) {
	jshort *sa= (*env)->GetShortArrayElements(env, loc, 0);
	jint status= RC(GetEventParameter((EventRef) eHandle, kEventParamMouseLocation, typeQDPoint, NULL, sizeof(Point), NULL, (Point*)sa));
	(*env)->ReleaseShortArrayElements(env, loc, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TrackMouseLocation(JNIEnv *env, jclass zz, jint portHandle, jshortArray loc, jshortArray part) {
	jshort *sa= (*env)->GetShortArrayElements(env, loc, 0);
	jshort *sb= NULL;
	jint status;
	if (part != NULL)
		sb= (*env)->GetShortArrayElements(env, part, 0);
	status= RC(TrackMouseLocation((GrafPtr) portHandle, (Point*) sa, sb));
	(*env)->ReleaseShortArrayElements(env, loc, sa, 0);
	if (sb != NULL)
		(*env)->ReleaseShortArrayElements(env, part, sb, 0);
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetGlobalMouse(JNIEnv *env, jclass zz, jshortArray where) {
	jshort *sa= (*env)->GetShortArrayElements(env, where, 0);
	GetGlobalMouse((Point*)sa);
	(*env)->ReleaseShortArrayElements(env, where, sa, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateEvent(JNIEnv *env, jclass zz,
			jint allocator, jint classID, jint kind, jdouble when, jint flags, jintArray eventRef) {
        jint *sa= (*env)->GetIntArrayElements(env, eventRef, 0);
	jint status= RC(CreateEvent((CFAllocatorRef)allocator, classID, kind, (EventTime)when, (EventAttributes)flags, (EventRef*) sa));
	(*env)->ReleaseIntArrayElements(env, eventRef, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_PostEventToQueue(JNIEnv *env, jclass zz,
			jint queue, jint event, jshort priority) {
	return RC(PostEventToQueue((EventQueueRef)queue, (EventRef)event, (EventPriority) priority));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMainEventQueue(JNIEnv *env, jclass zz) {
	return (jint) GetMainEventQueue();
}

//---- GrafPort

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPort(JNIEnv *env, jclass zz) {
	GrafPtr p;
	GetPort(&p);
	return (jint) p;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetPort(JNIEnv *env, jclass zz, jint portHandle) {
	SetPort((GrafPtr) portHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetPortWindowPort(JNIEnv *env, jclass zz, jint wHandle) {
	SetPortWindowPort((WindowRef) wHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_EraseRect(JNIEnv *env, jclass zz, jshortArray bounds) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	EraseRect((Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_FrameRect(JNIEnv *env, jclass zz, jshortArray bounds) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	FrameRect((Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_PaintRect(JNIEnv *env, jclass zz, jshortArray bounds) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	PaintRect((Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_FrameOval(JNIEnv *env, jclass zz, jshortArray bounds) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	FrameOval((Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_PaintOval(JNIEnv *env, jclass zz, jshortArray bounds) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	PaintOval((Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_FrameRoundRect(JNIEnv *env, jclass zz,
			jshortArray bounds, jshort ovalWidth, jshort ovalHeight) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	FrameRoundRect((Rect*) sa, ovalWidth, ovalHeight);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_PaintRoundRect(JNIEnv *env, jclass zz,
			jshortArray bounds, jshort ovalWidth, jshort ovalHeight) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	PaintRoundRect((Rect*) sa, ovalWidth, ovalHeight);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_NormalizeThemeDrawingState(JNIEnv *env, jclass zz) {
	NormalizeThemeDrawingState();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TextFont(JNIEnv *env, jclass zz, jshort fontID) {
	TextFont(fontID);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TextSize(JNIEnv *env, jclass zz, jshort size) {
	TextSize(size);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TextFace(JNIEnv *env, jclass zz, jshort face) {
	TextFace(face);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TextMode(JNIEnv *env, jclass zz, jshort mode) {
	TextMode(mode);
}

static ATSUTextLayout fgTextLayout;
static ATSUStyle fgStyle;
static int fgTextLayoutInitialized;
static ATSUAttributeTag fgATSUAttributeTag[4];
static ByteCount fgByteCount[4];
static ATSUAttributeValuePtr fgATSUAttributeValuePtr[4];

#ifdef USE_ATSUI
static void initATSUI(ATSUFontID font, short size, short face) {
	ATSUFontID f= 0; 
	Fixed s= size << 16;
	Boolean isBold= false;
	Boolean isItalic= false;
	int n= 3;
	
	if ((face & bold) != 0)
		isBold= true;
	if ((face & italic) != 0)
		isItalic= true;
	
	if (ATSUFONDtoFontID(font, (Style) normal, &f) != 0)
		fprintf(stderr, "ATSUFONDtoFontID: error\n");
	else if (f != 0)
		n++;
	
	if (fgTextLayoutInitialized == 0) {
		if (ATSUCreateTextLayout(&fgTextLayout) != 0)
		
		if (ATSUSetTransientFontMatching(fgTextLayout, true) != 0)
			fprintf(stderr, "ATSUSetTransientFontMatching1: error\n");

		if (ATSUCreateStyle(&fgStyle) != 0)
			fprintf(stderr, "ATSUCreateStyle: error\n");
						
		fgATSUAttributeTag[0]= kATSUSizeTag;
		fgATSUAttributeTag[1]= kATSUQDBoldfaceTag;
		fgATSUAttributeTag[2]= kATSUQDItalicTag;
		fgATSUAttributeTag[3]= kATSUFontTag;

		fgByteCount[0]= sizeof(Fixed);
		fgByteCount[1]= sizeof(Boolean);
		fgByteCount[2]= sizeof(Boolean);
		fgByteCount[3]= sizeof(ATSUFontID);
	
		fgTextLayoutInitialized= 1;
	} else {
		if (ATSUSetTransientFontMatching(fgTextLayout, true) != 0)
			fprintf(stderr, "ATSUSetTransientFontMatching2: error\n");
	}
	
	fgATSUAttributeValuePtr[0]= &s;
	fgATSUAttributeValuePtr[1]= &isBold;
	fgATSUAttributeValuePtr[2]= &isItalic;
	fgATSUAttributeValuePtr[3]= &f;
	
	if (ATSUSetAttributes(fgStyle, n, fgATSUAttributeTag, fgByteCount, fgATSUAttributeValuePtr) != 0)
		fprintf(stderr, "ATSUSetAttributes: error\n");
}
#endif

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawText(JNIEnv *env, jclass zz,
			jstring s, jshort font, jshort size, jshort face) {
#ifdef USE_ATSUI
	
	if (s !=  NULL) {
		const jchar *ss= (*env)->GetStringChars(env, s, NULL);
		int l= (*env)->GetStringLength(env, s);
		
		initATSUI(font, size, face);
	
		if (ATSUSetTextPointerLocation(fgTextLayout, (ConstUniCharArrayPtr)ss, kATSUFromTextBeginning, kATSUToTextEnd, l) != 0)
			fprintf(stderr, "ATSUSetTextPointerLocation: error\n");
		
		if (ATSUSetRunStyle(fgTextLayout, fgStyle, (UniCharArrayOffset) 0, (UniCharCount) l) != 0)
			fprintf(stderr, "ATSUSetRunStyle: error\n");		

		if (ATSUDrawText(fgTextLayout, kATSUFromTextBeginning,  kATSUToTextEnd , kATSUUseGrafPortPenLoc, kATSUUseGrafPortPenLoc) != 0)
			fprintf(stderr, "ATSUDrawText: error\n");

		(*env)->ReleaseStringChars(env, s, ss);
	}
#else

	const char *ss= (*env)->GetStringUTFChars(env, s, NULL);
	DrawText(ss, 0, (short)strlen(ss));
	(*env)->ReleaseStringUTFChars(env, s, ss);
#endif
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_TextWidth(JNIEnv *env, jclass zz,
			jstring s, jshort font, jshort size, jshort face) {
	jshort width= 0;
	
#ifdef USE_ATSUI
	if (s !=  NULL) {
		ATSTrapezoid trap;
		ItemCount n;
		const jchar *ss= (*env)->GetStringChars(env, s, NULL);
		int l= (*env)->GetStringLength(env, s);
		
		initATSUI(font, size, face);

		if (ATSUSetTextPointerLocation(fgTextLayout, (ConstUniCharArrayPtr)ss, kATSUFromTextBeginning, kATSUToTextEnd, l) != 0)
			fprintf(stderr, "ATSUSetTextPointerLocation: error\n");
		
		if (ATSUSetRunStyle(fgTextLayout, fgStyle, (UniCharArrayOffset) 0, (UniCharCount) l) != 0)
			fprintf(stderr, "ATSUSetRunStyle: error\n");		
		
		if (ATSUGetGlyphBounds(fgTextLayout, (ATSUTextMeasurement)0, (ATSUTextMeasurement)0,
					kATSUFromTextBeginning, kATSUToTextEnd, kATSUseDeviceOrigins, 1, &trap, &n)  != 0)
			fprintf(stderr, "ATSUGetGlyphBounds: error\n");
			
		(*env)->ReleaseStringChars(env, s, ss);

		width= HiWord(trap.lowerRight.x);
	}
#else
	const char *ss= (*env)->GetStringUTFChars(env, s, NULL);
	width= TextWidth(ss, 0, (short)strlen(ss));
	(*env)->ReleaseStringUTFChars(env, s, ss);
#endif
	return width;
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_CharWidth(JNIEnv *env, jclass zz, jbyte c) {
	return (jshort) CharWidth(c);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetFontInfo(JNIEnv *env, jclass zz, jshortArray info) {
    jshort *sa= (*env)->GetShortArrayElements(env, info, 0);
	GetFontInfo((FontInfo*) sa);
	(*env)->ReleaseShortArrayElements(env, info, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetFractEnable(JNIEnv *env, jclass zz, jboolean enable) {
	SetFractEnable(enable);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_PenSize(JNIEnv *env, jclass zz, jshort h, jshort v) {
	PenSize(h, v);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_MoveTo(JNIEnv *env, jclass zz, jshort h, jshort v) {
	MoveTo(h, v);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_LineTo(JNIEnv *env, jclass zz, jshort h, jshort v) {
	LineTo(h, v);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ClipRect(JNIEnv *env, jclass zz, jshortArray clip) {
    jshort *sa= (*env)->GetShortArrayElements(env, clip, 0);
	ClipRect((Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, clip, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ScrollRect(JNIEnv *env, jclass zz, jshortArray r, jshort dh, jshort dv, jint rgn) {
    jshort *sa= (*env)->GetShortArrayElements(env, r, 0);
	ScrollRect((Rect*) sa, dh, dv, (RgnHandle) rgn);
	(*env)->ReleaseShortArrayElements(env, r, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_QDFlushPortBuffer(JNIEnv *env, jclass zz, jint port, jint rgnHandle) {
	QDFlushPortBuffer((GrafPtr) port, (RgnHandle) rgnHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPortVisibleRegion(JNIEnv *env, jclass zz, jint pHandle,
				jint rgnHandle) {
	return (jint) GetPortVisibleRegion((CGrafPtr) pHandle, (RgnHandle) rgnHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetPortVisibleRegion(JNIEnv *env, jclass zz, jint pHandle,
				jint rgnHandle) {
	SetPortVisibleRegion((CGrafPtr) pHandle, (RgnHandle) rgnHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_QDGetDirtyRegion(JNIEnv *env, jclass zz, jint pHandle,
				jint rgnHandle) {
	return (jint) RC(QDGetDirtyRegion((CGrafPtr) pHandle, (RgnHandle) rgnHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_LockPortBits(JNIEnv *env, jclass zz, jint pHandle) {
	return (jint) RC(LockPortBits((GrafPtr) pHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_UnlockPortBits(JNIEnv *env, jclass zz, jint pHandle) {
	return (jint) RC(UnlockPortBits((GrafPtr) pHandle));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_RGBForeColor(JNIEnv *enc, jclass zz, jint rgb) {
	struct RGBColor c;
	setColor(&c, rgb);
	RGBForeColor(&c);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_RGBBackColor(JNIEnv *enc, jclass zz, jint rgb) {
	struct RGBColor c;
	setColor(&c, rgb);
	RGBBackColor(&c);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowFromPort(JNIEnv *env, jclass zz, jint pHandle) {
	return (jint) GetWindowFromPort((GrafPtr)pHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InvertRect(JNIEnv *env, jclass zz, jshort x, jshort y, jshort w, jshort h) {
	Rect r;
	SetRect(&r, x, y, x+w, y+h);
	InvertRect(&r);
}

//---- Regions

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewRgn(JNIEnv *env, jclass zz) {
	return (jint) NewRgn();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetEmptyRgn(JNIEnv *env, jclass zz, jint rgnHandle) {
	SetEmptyRgn((RgnHandle)rgnHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_RectRgn(JNIEnv *env, jclass zz, jint rgnHandle, jshortArray rect) {
    jshort *sa= (*env)->GetShortArrayElements(env, rect, 0);
	RectRgn((RgnHandle) rgnHandle, (Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, rect, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetRegionBounds(JNIEnv *env, jclass zz, jint rgnHandle,
				jshortArray bounds) {
    jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	GetRegionBounds((RgnHandle) rgnHandle, (Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetRectRgn(JNIEnv *env, jclass zz, jint rgnHandle,
		jshort left, jshort top, jshort right, jshort bottom) {
	SetRectRgn((RgnHandle)rgnHandle, left, top, right, bottom);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SectRgn(JNIEnv *env, jclass zz, jint ra, jint rb, jint dest) {
	SectRgn((RgnHandle) ra, (RgnHandle) rb, (RgnHandle) dest);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_UnionRgn(JNIEnv *env, jclass zz, jint ra, jint rb, jint dest) {
	UnionRgn((RgnHandle) ra, (RgnHandle) rb, (RgnHandle) dest);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DiffRgn(JNIEnv *env, jclass zz, jint ra, jint rb, jint dest) {
	DiffRgn((RgnHandle) ra, (RgnHandle) rb, (RgnHandle) dest);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_PtInRgn(JNIEnv *env, jclass zz, jshortArray pt, jint rgnHandle) {
	return (jboolean) PtInRgn(point(env, pt), (RgnHandle) rgnHandle);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_RectInRgn(JNIEnv *env, jclass zz, jshortArray rect, jint rgnHandle) {
    jshort *sa= (*env)->GetShortArrayElements(env, rect, 0);
	jboolean status= (jboolean) RectInRgn((struct Rect*)sa, (RgnHandle) rgnHandle);
	(*env)->ReleaseShortArrayElements(env, rect, sa, 0);
	return status;
}	

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyRgn(JNIEnv *env, jclass zz, jint src, jint dest) {
	CopyRgn((RgnHandle) src, (RgnHandle) dest);
}
	
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetClip(JNIEnv *env, jclass zz, jint rgnHandle) {
	GetClip((RgnHandle)rgnHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetClip(JNIEnv *env, jclass zz, jint rgnHandle) {
	SetClip((RgnHandle)rgnHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetOrigin(JNIEnv *env, jclass zz, jshort h, jshort v) {
	SetOrigin(h, v);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeRgn(JNIEnv *env, jclass zz, jint rgnHandle) {
	DisposeRgn((RgnHandle)rgnHandle);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_EmptyRgn(JNIEnv *env, jclass zz, jint rgnHandle) {
	return (jboolean) EmptyRgn((RgnHandle)rgnHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_OffsetRgn(JNIEnv *env, jclass zz,
				jint rgnHandle, jshort dh, jshort dv) {
	OffsetRgn((RgnHandle)rgnHandle, dh, dv);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_EraseRgn(JNIEnv *env, jclass zz, jint rgnHandle) {
	EraseRgn((RgnHandle) rgnHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InvertRgn(JNIEnv *env, jclass zz, jint rgnHandle) {
	InvertRgn((RgnHandle) rgnHandle);
}

//---- Polygons

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_OpenPoly(JNIEnv *env, jclass zz) {
	return (jint) OpenPoly();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ClosePoly(JNIEnv *env, jclass zz) {
	ClosePoly();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_OffsetPoly(JNIEnv *env, jclass zz, jint polyHandle, jshort dx, jshort dy) {
	OffsetPoly((PolyPtr *)polyHandle, dx, dy);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_FramePoly(JNIEnv *env, jclass zz, jint polyHandle) {
	FramePoly((PolyPtr *)polyHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_PaintPoly(JNIEnv *env, jclass zz, jint polyHandle) {
	PaintPoly((PolyPtr *)polyHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_KillPoly(JNIEnv *env, jclass zz, jint polyHandle) {
	KillPoly((PolyPtr *)polyHandle);
}

//---- PixMap

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewPixMap(JNIEnv *env, jclass zz,
			jshort width, jshort height, jshort depth, jshort pad, jshortArray reds, jshortArray greens, jshortArray blues) {
			
	int i, n, rowbytes, dataSize;
	PixMapHandle pmh;
	PixMap *pm;
	ColorTable *ct;
	void *data;

	rowbytes= (((width*depth-1)/(8*pad))+1)*pad;
	
	//fprintf(stderr, "OS.NewPixMap: w:%d h:%d d:%d row:%d pad:%d\n", width, height, depth, rowbytes, pad);
	
	if (rowbytes >= 0x4000)
		fprintf(stderr, "**** OS.NewPixMap: rowBytes >= 0x4000\n");
		
	pmh= NewPixMap();
	HLock((Handle)pmh);
	
	pm= *pmh;

	dataSize= rowbytes * height;
	data= NewPtr(dataSize);
	
	if (depth >= 16) {
		memset(data, 0xff, dataSize);	// white
	} else
		memset(data, 0x00, dataSize);	

	pm->baseAddr= data;
	pm->rowBytes= rowbytes;
	//if (depth > 1)
		pm->rowBytes|= 0x8000;	// mark as PixMap
	pm->bounds.top= 0;
	pm->bounds.left= 0;
	pm->bounds.bottom= height;
	pm->bounds.right= width;
	
	if ((pm->rowBytes & 0x8000) == 0) {	// is Bitmap??
		HUnlock((Handle)pmh);
		return (jint) pmh;
	}
	
	pm->pmVersion= baseAddr32;	// 32 Bit clean
	pm->packType= 0;
	pm->packSize= 0;
	pm->hRes= 0x00480000;
	pm->vRes= 0x00480000;
	
	if (depth > 8) {
		pm->pixelType= RGBDirect;
		pm->pixelSize= depth;
		if (depth == 24 || depth == 32) {
			pm->cmpSize= 8;
		} else {
			pm->cmpSize= 5;
		}
		pm->cmpCount= 3;
		pm->pixelFormat= depth;
	
		pm->pmTable= NULL;
	} else {
		pm->pixelType= 0;
		pm->pixelSize= depth;
		pm->cmpCount= 1;
		pm->cmpSize= depth;
		pm->pixelFormat= depth;
		
		n= 1 << depth;
		
		pm->pmTable= (ColorTable**) NewHandleClear(sizeof(ColorTable)+sizeof(ColorSpec)*n);
		ct= *pm->pmTable;
		ct->ctSize= n;
		ct->ctFlags= 0;
		ct->ctSeed= GetCTSeed();
		
		if (reds != 0 && greens != 0 && blues != 0) {
		
		    jsize length= (*env)->GetArrayLength(env, reds);

			jshort *r= (*env)->GetShortArrayElements(env, reds, 0);
			jshort *g= (*env)->GetShortArrayElements(env, greens, 0);
			jshort *b= (*env)->GetShortArrayElements(env, blues, 0);

			for (i= 0; i < length; i++) {
				ColorSpec *cs= &ct->ctTable[i];
				cs->value= i;
				cs->rgb.red= r[i]*257;
				cs->rgb.green= g[i]*257;
				cs->rgb.blue= b[i]*257;
			}

			(*env)->ReleaseShortArrayElements(env, reds, r, 0);
			(*env)->ReleaseShortArrayElements(env, greens, g, 0);
			(*env)->ReleaseShortArrayElements(env, blues, b, 0);
		}
	}
	
	pm->pmExt= NULL;

	HUnlock((Handle)pmh);

	return (jint) pmh;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_duplicatePixMap(JNIEnv *env, jclass zz, jint srcPixmap) {

	PixMapHandle srch, dsth;
	PixMap *src, *dst;
	
	srch= (PixMapHandle) srcPixmap;
	dsth= NewPixMap();
	
	HLock((Handle)srch);
	HLock((Handle)dsth);
	
	src= *srch;
	dst= *dsth;
	
	*dst= *src;
	dst->pmExt= NULL;
	
	if (src->baseAddr != NULL) {
		Size dataSize= GetPtrSize(src->baseAddr);
		//fprintf(stderr, "duplicatePixMap: data %ld\n", dataSize);
		dst->baseAddr= NewPtr(dataSize);
		memcpy(dst->baseAddr, src->baseAddr, dataSize);
	}
	
	if ((dst->rowBytes & 0x8000) != 0) {
		if (src->pmTable != NULL) {
			ColorTable *ct;
			Size dataSize= GetHandleSize((Handle)src->pmTable);
			//fprintf(stderr, "duplicatePixMap: ctab %ld\n", dataSize);
			dst->pmTable= (ColorTable**) NewHandle(dataSize);
			ct= *dst->pmTable;
			memcpy(ct, *src->pmTable, dataSize);
			//fprintf(stderr, "duplicatePixMap: ctab size %d\n", ct->ctSize);
		}
	}
		
	HUnlock((Handle)srch);
	HUnlock((Handle)dsth);
	
	return (jint) dsth;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_copyPixmpaData(JNIEnv *env, jclass zz,
		jbyteArray data, jint pixmap, jint length) {
		
	PixMapHandle srch;
	PixMap *src;
	jbyte *sa;

	srch= (PixMapHandle) pixmap;
	HLock((Handle)srch);
	src= *srch;
	
    sa= (*env)->GetByteArrayElements(env, data, 0);
	
	if (src->baseAddr != NULL)
		memcpy(sa, src->baseAddr, length);
	
	(*env)->ReleaseByteArrayElements(env, data, sa, 0);
	
	HUnlock((Handle)srch);
	
	return 0;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposePixMap(JNIEnv *env, jclass zz, jint pixMapHandle) {
	PixMapHandle ph= (PixMapHandle) pixMapHandle;
	void *data= (**ph).baseAddr;
	if (data != NULL)
		DisposePtr(data);
	(**ph).baseAddr= NULL;
	DisposePixMap(ph);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPixBounds(JNIEnv *env, jclass zz, jint pHandle, jshortArray bounds) {
    jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	GetPixBounds((PixMapHandle) pHandle, (Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPixDepth(JNIEnv *env, jclass zz, jint pHandle) {
	return GetPixDepth((PixMapHandle) pHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyBits(JNIEnv *env, jclass zz, jint srcBits, jint dstBits,
		jshortArray srcRect, jshortArray dstRect, jshort mode, jint maskRgn) {
    jshort *sa= (*env)->GetShortArrayElements(env, srcRect, 0);
    jshort *sb= (*env)->GetShortArrayElements(env, dstRect, 0);
	CopyBits((BitMap*)srcBits, (BitMap*)dstBits, (Rect*) sa, (Rect*) sb, mode, (RgnHandle) maskRgn);
	(*env)->ReleaseShortArrayElements(env, srcRect, sa, 0);
	(*env)->ReleaseShortArrayElements(env, dstRect, sb, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyMask(JNIEnv *env, jclass zz, jint srcBits,
		jint maskBits, jint dstBits, jshortArray srcRect, jshortArray maskRect, jshortArray dstRect) {
    jshort *sa= (*env)->GetShortArrayElements(env, srcRect, 0);
    jshort *sb= (*env)->GetShortArrayElements(env, maskRect, 0);
    jshort *sc= (*env)->GetShortArrayElements(env, dstRect, 0);
	CopyMask((BitMap*)srcBits, (BitMap*)maskBits, (BitMap*)dstBits, (Rect*) sa, (Rect*) sb, (Rect*) sc);
	(*env)->ReleaseShortArrayElements(env, srcRect, sa, 0);
	(*env)->ReleaseShortArrayElements(env, maskRect, sb, 0);
	(*env)->ReleaseShortArrayElements(env, dstRect, sc, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyDeepMask(JNIEnv *env, jclass zz, jint srcBits,
		jint maskBits, jint dstBits, jshortArray srcRect, jshortArray maskRect, jshortArray dstRect, jshort mode, jint maskRgn) {
    jshort *sa= (*env)->GetShortArrayElements(env, srcRect, 0);
    jshort *sb= (*env)->GetShortArrayElements(env, maskRect, 0);
    jshort *sc= (*env)->GetShortArrayElements(env, dstRect, 0);
	CopyDeepMask((BitMap*)srcBits, (BitMap*)maskBits, (BitMap*)dstBits, (Rect*) sa, (Rect*) sb, (Rect*) sc, mode, (RgnHandle) maskRgn);
	(*env)->ReleaseShortArrayElements(env, srcRect, sa, 0);
	(*env)->ReleaseShortArrayElements(env, maskRect, sb, 0);
	(*env)->ReleaseShortArrayElements(env, dstRect, sc, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetPortBitMapForCopyBits(JNIEnv *env, jclass zz, jint grafPort) {
	return (jint) GetPortBitMapForCopyBits((CGrafPtr)grafPort);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_getBitMapForCopyBits(JNIEnv *env, jclass zz, jint pixMapHandle) {
	PixMapHandle ph= (PixMapHandle) pixMapHandle;
	return (jint) (*ph);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_setPixMapData(JNIEnv *env, jclass zz,
				jint pixMapHandle, jbyteArray pixMapData) {
	PixMapHandle ph= (PixMapHandle) pixMapHandle;
	
	jbyte *sa= (*env)->GetByteArrayElements(env, pixMapData, 0);
        jsize dataSize= (*env)->GetArrayLength(env, pixMapData);
	
	void *data= (**ph).baseAddr;
	if (data != NULL)
		DisposePtr(data);
	data= NewPtr(dataSize);	
	(**ph).baseAddr= data;
	memcpy(data, (const void*)sa, dataSize);
	
	(*env)->ReleaseByteArrayElements(env, pixMapData, sa, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewCIcon(JNIEnv *env, jclass zz, jint pixMapHandle, jint maskHandle) {
	CIcon *icon;
	CIconHandle cih;
	PixMap *ph;
	BitMap *mh;
	int pixmapRowbytes, pixmapWidth, pixmapHeight, pixmapSize;
	int maskRowbytes, maskHeight, maskSize;
	Size ctsize;
	int pad= 4;
			
	if (pixMapHandle == 0) {
		fprintf(stderr, "**** NewCIcon: pixmap handle == 0\n");
		return 0;
	}

	if (maskHandle == 0) {
		// fprintf(stderr, "**** NewCIcon: mask handle == 0\n");
		return 0;
	}

	ph= *((PixMap**) pixMapHandle);
	if (ph == NULL) {
		fprintf(stderr, "**** OS.NewCIcon: pixmap == NULL\n");
		return 0;
	}
		
	if (ph->pixelSize > 8 || ph->pmTable == 0) {
		fprintf(stderr, "**** OS.NewCIcon: can't create CIcon from Pixmap with depth > 8\n");
		return 0;
	}

	pixmapRowbytes= ph->rowBytes & 0x3fff;
	pixmapHeight= ph->bounds.bottom - ph->bounds.top;
	pixmapWidth= ph->bounds.right - ph->bounds.left;
	pixmapSize= pixmapRowbytes * pixmapHeight;

	if (maskHandle == 0) {
	
		maskRowbytes= (((pixmapWidth*1-1)/(8*pad))+1)*pad;
		maskHeight= pixmapHeight;
		maskSize= maskRowbytes * maskHeight;
			
	} else {
		mh= *((BitMap**) maskHandle);
		if (mh == NULL) {
			fprintf(stderr, "**** OS.NewCIcon: mask bitmap == NULL\n");
			return 0;
		}
		maskRowbytes= mh->rowBytes;
		maskHeight= mh->bounds.bottom - mh->bounds.top;
		maskSize= maskRowbytes * maskHeight;
	}
				
	cih= (CIconHandle) NewHandleClear(sizeof(CIcon) + maskSize);
	if (cih == NULL) {
		fprintf(stderr, "**** OS.NewCIcon: cih is 0\n");
		return 0;
	}
	
	icon= *cih;
	if (icon == NULL) {
		fprintf(stderr, "**** OS.NewCIcon: icon is NULL\n");
		return 0;
	}
		
	memcpy(&icon->iconPMap, ph, sizeof(PixMap));

	icon->iconData= NewHandle(pixmapSize);
	if (icon->iconData == 0) {
		fprintf(stderr, "**** OS.NewCIcon: iconData handle is NULL\n");
		return 0;
	}

	memcpy(*icon->iconData, ph->baseAddr, pixmapSize);
	icon->iconPMap.baseAddr= 0;	// this is documented nowhere!
	
	// copy ctable
	ctsize= GetHandleSize((Handle)ph->pmTable);
	if (ctsize > 0) {
		Handle h= NewHandle(ctsize);
		memcpy(*h, *ph->pmTable, ctsize);
		ph->pmTable= (ColorTable**) h;
	}

	if (mh != NULL) {
		memcpy(&icon->iconMask, mh, sizeof(BitMap));
		// copy mask data to end of CIcon
		memcpy(&icon->iconMaskData, icon->iconMask.baseAddr, maskSize);
	} else {
		icon->iconMask.rowBytes= maskRowbytes;
		icon->iconMask.bounds.left= 0;
		icon->iconMask.bounds.top= 0;
		icon->iconMask.bounds.right= pixmapWidth;
		icon->iconMask.bounds.bottom= pixmapHeight;
		memset(&icon->iconMaskData, 0xff, maskSize);
	}
	icon->iconMask.baseAddr= 0;
	
	return (jint) cih;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeCIcon(JNIEnv *env, jclass zz, jint cIconHandle) {
	//DisposeCIcon((CIconHandle) cIconHandle);
	//fprintf(stderr, "OS.DisposeCIcon: disabled\n");  FIXME
}

//---- GWorlds

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewGWorldFromPtr(JNIEnv *env, jclass zz,
					jintArray offscreenGWorld, jint pixMapHandle) {
		
	PixMapHandle pm= (PixMapHandle) pixMapHandle;
        jint *sa= (*env)->GetIntArrayElements(env, offscreenGWorld, 0);
	
	jint status= (jint) RC(NewGWorldFromPtr(
				(GWorldPtr*) sa,
				(*pm)->pixelFormat,
				&((*pm)->bounds),
				(*pm)->pmTable,
				(GDHandle) NULL,
				(GWorldFlags) 0,
				(*pm)->baseAddr,
				(*pm)->rowBytes & 0x3FFF));

	(*env)->ReleaseIntArrayElements(env, offscreenGWorld, sa, 0);
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeGWorld(JNIEnv *env, jclass zz, jint offscreenGWorld) {
	DisposeGWorld((GWorldPtr)offscreenGWorld);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetGWorld(JNIEnv *env, jclass zz, jint portHandle, jint gdHandle) {
	SetGWorld((CGrafPtr)portHandle, (GDHandle)gdHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetGWorld(JNIEnv *env, jclass zz, jintArray portHandle, jintArray gdHandle) {
    jint *sa= (*env)->GetIntArrayElements(env, portHandle, 0);
    jint *sb= (*env)->GetIntArrayElements(env, gdHandle, 0);
	GetGWorld((CGrafPtr*)sa, (GDHandle*)sb);
	(*env)->ReleaseIntArrayElements(env, portHandle, sa, 0);
	(*env)->ReleaseIntArrayElements(env, gdHandle, sb, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetGDevice(JNIEnv *env, jclass zz) {
	return (jint) GetGDevice();
}

//---- Window Manager

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateNewWindow(JNIEnv *env, jclass zz,
				jint windowClass, jint windowAttributes, jshortArray bounds, jintArray wHandle) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
        jint *sb= (*env)->GetIntArrayElements(env, wHandle, 0);
	jint status= (jint) RC(CreateNewWindow((WindowClass)windowClass, (WindowAttributes)windowAttributes, (const Rect*)sa, (WindowRef*)sb));
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
	(*env)->ReleaseIntArrayElements(env, wHandle, sb, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowPort(JNIEnv *env, jclass zz, jint wHandle) {
	return (jint) GetWindowPort((WindowRef) wHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_BeginUpdate(JNIEnv *env, jclass zz, jint wHandle) {
	BeginUpdate((WindowRef)wHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GlobalToLocal(JNIEnv *env, jclass zz, jshortArray point) {
    jshort *sa= (*env)->GetShortArrayElements(env, point, 0);
	GlobalToLocal((Point*)sa);
	(*env)->ReleaseShortArrayElements(env, point, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_LocalToGlobal(JNIEnv *env, jclass zz, jshortArray point) {
    jshort *sa= (*env)->GetShortArrayElements(env, point, 0);
	LocalToGlobal((Point*)sa);
	(*env)->ReleaseShortArrayElements(env, point, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_EndUpdate(JNIEnv *env, jclass zz, jint wHandle) {
	EndUpdate((WindowRef)wHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawControls(JNIEnv *env, jclass zz, jint wHandle) {
	DrawControls((WindowRef)wHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_UpdateControls(JNIEnv *env, jclass zz, jint wHandle, jint rgnHandle) {
	UpdateControls((WindowRef)wHandle, (RgnHandle)rgnHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawGrowIcon(JNIEnv *env, jclass zz, jint wHandle) {
	DrawGrowIcon((WindowRef)wHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_FrontWindow(JNIEnv *env, jclass class) {
	return (jint) FrontWindow();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_FrontNonFloatingWindow(JNIEnv *env, jclass class) {
	return (jint) FrontNonFloatingWindow();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SelectWindow(JNIEnv *env, jclass zz, jint wHandle) {
	SelectWindow((WindowRef)wHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_BringToFront(JNIEnv *env, jclass zz, jint wHandle) {
	BringToFront((WindowRef)wHandle);
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_FindWindow(JNIEnv *env, jclass zz, jshortArray where, jintArray warr) {
	jint *body= NULL;
	jshort part= 0;
	if (warr != 0)
    	body= (*env)->GetIntArrayElements(env, warr, 0);
	part= FindWindow(point(env, where), (WindowRef*)body);
	if (body != NULL)
		(*env)->ReleaseIntArrayElements(env, warr, body, 0);
	return part;
}

/*
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_ResizeWindow(JNIEnv *env, jclass zz, jint wHandle,
						jshortArray startPt, jshortArray sizeConstraints, jshortArray newContentRect) {
	jboolean b;
	jshort *sa= NULL;
	if (newContentRect != NULL)
		sa= (*env)->GetShortArrayElements(env, newContentRect, 0);
	b= ResizeWindow((WindowRef) wHandle, point(env, startPt), NULL, (Rect*) sa);
	if (sa != NULL)
		(*env)->ReleaseShortArrayElements(env, newContentRect, sa, 0);
	return b;
}
*/
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DragWindow(JNIEnv *env, jclass zz, jint wHandle,
							jshortArray startPt, jshortArray boundsRect) {
	DragWindow((WindowRef)wHandle, point(env, startPt), NULL);
}
*/
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowPortBounds(JNIEnv *env, jclass zz, jint wHandle, jshortArray bounds) {
    jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	GetWindowPortBounds((WindowRef)wHandle, (Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}
*/
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowBounds(JNIEnv *env, jclass zz, jint wHandle, jshort region, jshortArray bounds) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	GetWindowBounds((WindowRef)wHandle, region, (Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowBounds(JNIEnv *env, jclass zz, jint wHandle, jshort region, jshortArray bounds) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	SetWindowBounds((WindowRef)wHandle, region, (Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsValidWindowPtr(JNIEnv *env, jclass zz, jint port) {
	return (jboolean) IsValidWindowPtr((void*)port);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWRefCon(JNIEnv *env, jclass zz, jint wHandle) {
	return (jint) GetWRefCon((WindowRef)wHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyWindowTitleAsCFString(JNIEnv *env, jclass zz,
					jint wHandle, jintArray sHandle) {
        jint *sa= (*env)->GetIntArrayElements(env, sHandle, 0);
	jint status= (jint) RC(CopyWindowTitleAsCFString((WindowRef)wHandle, (CFStringRef*) sa));
	(*env)->ReleaseIntArrayElements(env, sHandle, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowTitleWithCFString(JNIEnv *env, jclass zz, jint wHandle, jint sHandle) {
	return RC(SetWindowTitleWithCFString((WindowRef)wHandle, (CFStringRef)sHandle));
}
  
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SizeWindow(JNIEnv *env, jclass zz, jint wHandle, jshort w, jshort h, jboolean update) {
	SizeWindow((WindowRef)wHandle, w, h, update);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_MoveWindow(JNIEnv *env, jclass zz, jint wHandle, jshort w, jshort h, jboolean toFront) {
	MoveWindow((WindowRef)wHandle, w, h, toFront);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ScrollWindowRect(JNIEnv *env, jclass zz, jint wHandle,
						jshortArray rect, jshort dx, jshort dy, jint options, jint exposureRgn) {
        jshort *sa= (*env)->GetShortArrayElements(env, rect, 0);
	ScrollWindowRect((WindowRef)wHandle, (Rect*)sa, dx, dy, options, (RgnHandle) exposureRgn);
	(*env)->ReleaseShortArrayElements(env, rect, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWRefCon(JNIEnv *env, jclass zz, jint wHandle, jint data) {
	SetWRefCon((WindowRef)wHandle, data);
}

/*
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_TrackGoAway(JNIEnv *env, jclass zz, jint wHandle, jshortArray startPt) {
	return TrackGoAway((WindowRef)wHandle, point(env, startPt));
}
*/
/*
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_TrackBox(JNIEnv *env, jclass zz, jint wHandle, jshortArray startPt, jshort part) {
	return TrackBox((WindowRef)wHandle, point(env, startPt), part);
}
*/
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeWindow(JNIEnv *env, jclass class, jint wHandle) {
	DisposeWindow((WindowRef)wHandle);
}
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ZoomWindow(JNIEnv *env, jclass zz, jint wHandle, jshort part, jboolean toFront) {
	ZoomWindow((WindowRef)wHandle, part, toFront);
}
*/
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InvalWindowRect(JNIEnv *env, jclass zz, jint wHandle, jshortArray bounds) {
    jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	InvalWindowRect((WindowRef)wHandle, (Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InvalWindowRgn(JNIEnv *env, jclass zz, jint wHandle, jint rgnHandle) {
	InvalWindowRgn((WindowRef)wHandle, (RgnHandle)rgnHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ShowWindow(JNIEnv *env, jclass zz, jint wHandle) {
	ShowWindow((WindowRef) wHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_HideWindow(JNIEnv *env, jclass zz, jint wHandle) {
	HideWindow((WindowRef) wHandle);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsWindowVisible(JNIEnv *env, jclass zz, jint wHandle) {
	return IsWindowVisible((WindowRef) wHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowDefaultButton(JNIEnv *env, jclass zz,
				jint wHandle, jint cHandle) {
	return RC(SetWindowDefaultButton((WindowRef) wHandle, (ControlRef) cHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowDefaultButton(JNIEnv *env, jclass zz,
				jint wHandle, jintArray cHandle) {
        jint *sa= (*env)->GetIntArrayElements(env, cHandle, 0);
	int status= RC(GetWindowDefaultButton((WindowRef) wHandle, (ControlRef*) sa));
	(*env)->ReleaseIntArrayElements(env, cHandle, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetWindowModality(JNIEnv *env, jclass zz,
				jint wHandle, jintArray windowModality, jintArray parentWindowHandle) {
        jint *sa= (*env)->GetIntArrayElements(env, windowModality, 0);
        jint *sb= NULL;
	jint status= 0;
	if (parentWindowHandle != 0)
		sb= (*env)->GetIntArrayElements(env, parentWindowHandle, 0);
	status= (jint) RC(GetWindowModality((WindowRef) wHandle, (WindowModality*) sa, (WindowRef*) sb));
	(*env)->ReleaseIntArrayElements(env, windowModality, sa, 0);
	if (sb != NULL)
		(*env)->ReleaseIntArrayElements(env, parentWindowHandle, sb, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetWindowModality(JNIEnv *env, jclass zz,
				jint wHandle, jint modalityKind, jint parentWindow) {
	return (jint) RC(SetWindowModality((WindowRef) wHandle, (WindowModality) modalityKind, (WindowRef) parentWindow));
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsWindowActive(JNIEnv *env, jclass zz, jint wHandle) {
	return (jboolean) IsWindowActive((WindowRef) wHandle);
}

//---- Menu Manager
   
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InitContextualMenus(JNIEnv *env, jclass zz) {
	return (jint) RC(InitContextualMenus());
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateNewMenu(JNIEnv *env, jclass zz,
				jint menuId, jint menuAttributes, jintArray menuRef) {
        jint *sa= (*env)->GetIntArrayElements(env, menuRef, 0);
	jint status= (jint) RC(CreateNewMenu((MenuID)menuId, (MenuAttributes)menuAttributes, (MenuRef*)sa));
	(*env)->ReleaseIntArrayElements(env, menuRef, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_MenuSelect(JNIEnv *env, jclass zz, jshortArray where) {
	return MenuSelect(point(env, where));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_HiliteMenu(JNIEnv *env, jclass zz, jshort menuID) {
	HiliteMenu(menuID);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InvalMenuBar(JNIEnv *env, jclass zz) {
	InvalMenuBar();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DrawMenuBar(JNIEnv *env, jclass zz) {
	DrawMenuBar();
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_CountMenuItems(JNIEnv *env, jclass zz, jint mHandle) {
	return CountMenuItems((MenuRef)mHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DeleteMenuItems(JNIEnv *env, jclass zz,
			jint mHandle, jshort firstItem, jint numItems) {
	return RC(DeleteMenuItems((MenuRef)mHandle, firstItem, numItems));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeMenu(JNIEnv *env, jclass zz, jint mHandle) {
	DisposeMenu((MenuRef) mHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InsertMenu(JNIEnv *env, jclass zz, jint mHandle, jshort index) {
	InsertMenu((MenuRef) mHandle, index);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DeleteMenu(JNIEnv *env, jclass zz, jshort menuId) {
	DeleteMenu(menuId);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ClearMenuBar(JNIEnv *env, jclass zz) {
	ClearMenuBar();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuItemRefCon(JNIEnv *env, jclass zz, jint mHandle,
			jshort index, jintArray refCon) {
        jint *sa= (*env)->GetIntArrayElements(env, refCon, 0);
	int status= RC(GetMenuItemRefCon((MenuRef) mHandle, index, sa));
	(*env)->ReleaseIntArrayElements(env, refCon, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemRefCon(JNIEnv *env, jclass zz, jint mHandle,
			jshort index, jint refCon) {
	return RC(SetMenuItemRefCon((MenuRef) mHandle, index, refCon));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemTextWithCFString(JNIEnv *env, jclass zz, jint mHandle,
			jshort index, jint sHandle) {
	return RC(SetMenuItemTextWithCFString((MenuRef) mHandle, index, (CFStringRef) sHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CopyMenuItemTextAsCFString(JNIEnv *env, jclass zz,
			jint mHandle, jshort index, jintArray sHandle) {
			
	jint *sa= (*env)->GetIntArrayElements(env, sHandle, 0);
	jint status= (jint) RC(CopyMenuItemTextAsCFString((MenuRef) mHandle, index, (CFStringRef*) sa));
	(*env)->ReleaseIntArrayElements(env, sHandle, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemCommandKey(JNIEnv *env, jclass zz,
		jint mHandle, jshort index, jboolean virtual, jchar key) {
	return RC(SetMenuItemCommandKey((MenuRef) mHandle, index, virtual, key));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemModifiers(JNIEnv *env, jclass zz,
		jint mHandle, jshort index, jbyte modifiers) {
	return RC(SetMenuItemModifiers((MenuRef) mHandle, index, modifiers));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemKeyGlyph(JNIEnv *env, jclass zz,
		jint mHandle, jshort index, jshort glyph) {
	return RC(SetMenuItemKeyGlyph((MenuRef) mHandle, index, glyph));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InvalidateMenuItems(JNIEnv *env, jclass zz,
		jint mHandle, jshort index, jint numItems) {
	return RC(InvalidateMenuItems((MenuRef) mHandle, index, numItems));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_InsertMenuItemTextWithCFString(JNIEnv *env, jclass zz,
			jint mHandle, jint sHandle, jshort index, jint attributes, jint commandID) {
	return RC(InsertMenuItemTextWithCFString((MenuRef) mHandle, (CFStringRef) sHandle, index, attributes, commandID));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AppendMenuItemTextWithCFString(JNIEnv *env, jclass zz,
			jint mHandle, jint sHandle, jint attributes, jint commandID, jshortArray outItemIndex) {
		
	int status;
        jshort *sa= NULL;
	if (outItemIndex != 0)
		(*env)->GetShortArrayElements(env, outItemIndex, 0);
	status= (jint) RC(AppendMenuItemTextWithCFString((MenuRef) mHandle, (CFStringRef) sHandle, attributes, commandID, (MenuItemIndex*) sa));
	if (sa != NULL)
		(*env)->ReleaseShortArrayElements(env, outItemIndex, sa, 0);
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_EnableMenuCommand(JNIEnv *env, jclass zz, jint mHandle, jint commandId) {
	EnableMenuCommand((MenuRef) mHandle, commandId);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisableMenuCommand(JNIEnv *env, jclass zz, jint mHandle, jint commandId) {
	DisableMenuCommand((MenuRef) mHandle, commandId);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsMenuCommandEnabled(JNIEnv *env, jclass zz, jint mHandle, jint commandId) {
	return (jboolean) IsMenuCommandEnabled((MenuRef) mHandle, commandId);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetIndMenuItemWithCommandID(JNIEnv *env, jclass zz, jint mHandle,
			jint commandId, jint index, jintArray outMenu, jshortArray outIndex) {
	jint status;
        jint *sa= NULL;
        jshort *sb= NULL;
	if (outMenu != NULL)
		sa= (*env)->GetIntArrayElements(env, outMenu, 0);
	if (outIndex != NULL) 
		sb= (*env)->GetShortArrayElements(env, outIndex, 0);
	status= (jint) RC(GetIndMenuItemWithCommandID((MenuRef) mHandle, commandId, index, (MenuRef*)sa, sb));
	if (sa != NULL)
		(*env)->ReleaseIntArrayElements(env, outMenu, sa, 0);
	if (sb != NULL)
		(*env)->ReleaseShortArrayElements(env, outIndex, sb, 0);
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DeleteMenuItem(JNIEnv *env, jclass zz, jint mHandle, jshort index) {
	DeleteMenuItem((MenuRef) mHandle, index);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuItemCommandID(JNIEnv *env, jclass zz, jint mHandle, jshort index,
			jintArray commandId) {
	jint *sa= (*env)->GetIntArrayElements(env, commandId, 0);
	jint status= (jint) RC(GetMenuItemCommandID((MenuRef) mHandle, index, (MenuCommand*)sa));
	(*env)->ReleaseIntArrayElements(env, commandId, sa, 0);
	return status;
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuID(JNIEnv *env, jclass zz, jint mHandle) {
	return (jshort) GetMenuID((MenuRef) mHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuHandle(JNIEnv *env, jclass zz, jshort menuId) {
	return (jint) GetMenuHandle(menuId);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_PopUpMenuSelect(JNIEnv *env, jclass zz, jint mHandle,
		jshort x, jshort y, jshort index) {
	return PopUpMenuSelect((MenuRef) mHandle, x, y, index);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetRootMenu(JNIEnv *env, jclass zz, jint mHandle) {
	return (jint) RC(SetRootMenu((MenuRef) mHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RetainMenu(JNIEnv *env, jclass zz, jint mHandle) {
	return (jint) RC(RetainMenu((MenuRef) mHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ReleaseMenu(JNIEnv *env, jclass zz, jint mHandle) {
	return (jint) RC(ReleaseMenu((MenuRef) mHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuTitleWithCFString(JNIEnv *env, jclass zz, jint mHandle, jint sHandle) {
	return (jint) RC(SetMenuTitleWithCFString((MenuRef) mHandle, (CFStringRef) sHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuItemHierarchicalMenu(JNIEnv *env, jclass zz, jint mHandle, 
		jshort index, jintArray outHierMenuHandle) {
        jint *sa= (*env)->GetIntArrayElements(env, outHierMenuHandle, 0);
	jint status= (jint) RC(GetMenuItemHierarchicalMenu((MenuRef) mHandle, index, (MenuRef*)sa));
	(*env)->ReleaseIntArrayElements(env, outHierMenuHandle, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuItemHierarchicalMenu(JNIEnv *env, jclass zz, jint mHandle,
			jshort index, jint hierMenuHandle) {
	return (jint) RC(SetMenuItemHierarchicalMenu((MenuRef) mHandle, index, (MenuRef) hierMenuHandle));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InsertMenuItem(JNIEnv *env, jclass zz, jint mHandle,
			jbyteArray text, jshort index) {
        jbyte *sa= (*env)->GetByteArrayElements(env, text, 0);
	InsertMenuItem((MenuRef) mHandle, (ConstStr255Param) sa, index);
	(*env)->ReleaseByteArrayElements(env, text, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_AppendMenu(JNIEnv *env, jclass zz, jint mHandle, jbyteArray text) {
        jbyte *sa= (*env)->GetByteArrayElements(env, text, 0);
	AppendMenu((MenuRef) mHandle, (ConstStr255Param) sa);
	(*env)->ReleaseByteArrayElements(env, text, sa, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_ChangeMenuItemAttributes(JNIEnv *env, jclass zz, jint mHandle,
		jshort index, jint setAttributes, jint clearAttributes) {
	return RC(ChangeMenuItemAttributes((MenuRef) mHandle, index, setAttributes, clearAttributes));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CheckMenuItem(JNIEnv *env, jclass zz, jint mHandle,
		jshort index, jboolean checked) {
	CheckMenuItem((MenuRef) mHandle, index, checked);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuCommandMark(JNIEnv *env, jclass zz, jint mHandle,
		jint commandId, jshortArray markCharacter) {
        jchar *sa= (*env)->GetCharArrayElements(env, markCharacter, 0);
	jint status= (jint) RC(GetMenuCommandMark((MenuRef) mHandle, commandId, (UniChar*) sa));
	(*env)->ReleaseCharArrayElements(env, markCharacter, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuCommandMark(JNIEnv *env, jclass zz, jint mHandle,
		jint commandId, jchar markCharacter) {
	return (jint) RC(SetMenuCommandMark((MenuRef) mHandle, commandId, (UniChar) markCharacter));
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsValidMenu(JNIEnv *env, jclass zz, jint mHandle) {
	return (jboolean) IsValidMenu((MenuRef) mHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuID(JNIEnv *env, jclass zz, jint mHandle, jshort id) {
	SetMenuID((MenuRef) mHandle, id);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsMenuItemEnabled(JNIEnv *env, jclass zz, jint mHandle, jshort index) {
	return (jboolean) IsMenuItemEnabled((MenuRef) mHandle, index);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisableMenuItem(JNIEnv *env, jclass zz, jint mHandle, jshort index) {
	DisableMenuItem((MenuRef) mHandle, index);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_EnableMenuItem(JNIEnv *env, jclass zz, jint mHandle, jshort index) {
	EnableMenuItem((MenuRef) mHandle, index);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuFont(JNIEnv *env, jclass zz,
				jint mHandle, jshortArray fontID, jshortArray size) {
	jshort *sa= (*env)->GetShortArrayElements(env, fontID, 0);
	jshort *sb= (*env)->GetShortArrayElements(env, size, 0);
	jint status= (jint) RC(GetMenuFont((MenuRef) mHandle, (SInt16*) sa, (UInt16*) sb));
	(*env)->ReleaseShortArrayElements(env, fontID, sa, 0);
	(*env)->ReleaseShortArrayElements(env, size, sb, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetMenuFont(JNIEnv *env, jclass zz,
				jint mHandle, jshort fontID, jshort size) {
	return (jint) RC(SetMenuFont((MenuRef) mHandle, (SInt16) fontID, (UInt16) size));
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMenuWidth(JNIEnv *env, jclass zz, jint mHandle) {
	return (jshort) GetMenuWidth((MenuRef) mHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CalcMenuSize(JNIEnv *env, jclass zz, jint mHandle) {
	CalcMenuSize((MenuRef) mHandle);
}

//---- Control Manager

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlAction(JNIEnv *env, jclass zz, jint cHandle, jint actionProc) {
	SetControlAction((ControlRef)cHandle, (ControlActionUPP) actionProc);
}

/* not for primetime use */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_setControlToolTipText(JNIEnv *env, jclass zz,
				jint cHandle, jshortArray bounds, jint sHandle) {
  
	HMHelpContentRec help;
	jint status;
	Rect *r= (Rect*) (*env)->GetShortArrayElements(env, bounds, 0);
	
	HMSetHelpTagsDisplayed(true);

	help.version= 0;
	help.absHotRect.left= r->left;
	help.absHotRect.top= r->top;
	help.absHotRect.right= r->right;
	help.absHotRect.bottom= r->bottom;
	help.tagSide= kHMDefaultSide;
	help.content[0].contentType= kHMCFStringContent;
	help.content[0].u.tagCFString= (CFStringRef) sHandle;
	help.content[1].contentType= kHMNoContent;
	help.content[1].u.tagCFString= 0;
  
	status= RC(HMSetControlHelpContent((ControlRef) cHandle, &help));
	
	(*env)->ReleaseShortArrayElements(env, bounds, (jshort*) r, 0);
	
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewControl(JNIEnv *env, jclass zz,
			jint wHandle, jboolean visible, jshort initialValue, jshort minValue, jshort maxValue, jshort procID) {
	return (jint) NewControl((WindowRef) wHandle, &NULL_RECT, "\0", visible, initialValue, minValue, maxValue, procID, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeControl(JNIEnv *env, jclass zz, jint cHandle) {
	DisposeControl((ControlRef) cHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateUserPaneControl(JNIEnv *env, jclass zz,
			jint wHandle, jshortArray bounds, jint features, jintArray cHandle) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
        jint *sb= (*env)->GetIntArrayElements(env, cHandle, 0);
	jint status= (jint) RC(CreateUserPaneControl((WindowRef) wHandle, (const Rect*) sa, features, (ControlRef*) sb));
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
	(*env)->ReleaseIntArrayElements(env, cHandle, sb, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetRootControl(JNIEnv *env, jclass zz, jint wHandle, jintArray cHandle) {
        jint *sa= (*env)->GetIntArrayElements(env, cHandle, 0);
	jint status= (jint) RC(GetRootControl((WindowRef) wHandle, (ControlRef*)sa));
	(*env)->ReleaseIntArrayElements(env, cHandle, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateRootControl(JNIEnv *env, jclass zz, jint wHandle, jintArray cHandle) {
        jint *sa= (*env)->GetIntArrayElements(env, cHandle, 0);
	jint status= (jint) RC(CreateRootControl((WindowRef) wHandle, (ControlRef*)sa));
	(*env)->ReleaseIntArrayElements(env, cHandle, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_EmbedControl(JNIEnv *env, jclass zz, jint cHandle, jint parentHandle) {
	return (jint) RC(EmbedControl((ControlRef) cHandle, (ControlRef) parentHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlOwner(JNIEnv *env, jclass zz, jint cHandle) {
	return (jint) GetControlOwner((ControlRef) cHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_FindControlUnderMouse(JNIEnv *env, jclass zz, jshortArray where,
													 jint wHandle, jshortArray cHandle) {
        jshort *sb= (*env)->GetShortArrayElements(env, cHandle, 0);
	ControlRef c= FindControlUnderMouse(point(env, where), (WindowRef) wHandle, sb);
	(*env)->ReleaseShortArrayElements(env, cHandle, sb, 0);
	return (jint) c;
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_TestControl(JNIEnv *env, jclass zz,
				jint cHandle, jshortArray where) {
	return (jshort) TestControl((ControlRef)cHandle, point(env, where));
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_HandleControlClick(JNIEnv *env, jclass zz, jint cHandle,
													jshortArray where, jint modifiers, jint action) {
	return HandleControlClick((ControlRef)cHandle, point(env, where), modifiers, (ControlActionUPP) action);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_MoveControl(JNIEnv *env, jclass zz, jint cHandle, jshort x, jshort y) {
	MoveControl((ControlRef) cHandle, x, y);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SizeControl(JNIEnv *env, jclass zz, jint cHandle, jshort w, jshort h) {
	SizeControl((ControlRef) cHandle, w, h);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ShowControl(JNIEnv *env, jclass zz, jint cHandle) {
	ShowControl((ControlRef) cHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_HideControl(JNIEnv *env, jclass zz, jint cHandle) {
	HideControl((ControlRef) cHandle);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsControlVisible(JNIEnv *env, jclass zz, jint cHandle) {
	return (jboolean) IsControlVisible((ControlRef) cHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlVisibility(JNIEnv *env, jclass zz, jint cHandle,
                jboolean isVisible, jboolean doDraw) {
	return (jint) RC(SetControlVisibility((ControlRef)cHandle, isVisible, doDraw));
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsControlActive(JNIEnv *env, jclass zz, jint cHandle) {
	return (jboolean) IsControlActive((ControlRef) cHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlBounds(JNIEnv *env, jclass zz, jint cHandle, jshortArray bounds) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	Rect *r= (Rect*) sa;
	if (r->bottom - r->top < 0 || r->right - r->left < 0) {
		// fprintf(stdout, "-*-*-*-*-*_*-*_* SetControlBounds: %d %d %d %d\n", r->left, r->top, r->right-r->left, r->bottom-r->top);
	} else
		SetControlBounds((ControlRef)cHandle, r);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlBounds(JNIEnv *env, jclass zz, jint cHandle, jshortArray bounds) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	GetControlBounds((ControlRef)cHandle, (Rect*) sa);
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlRegion(JNIEnv *env, jclass zz,
				jint cHandle, jshort part, jint region) {
	return (jint) RC(GetControlRegion((ControlRef)cHandle, (ControlPartCode) part, (RgnHandle) region));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CountSubControls(JNIEnv *env, jclass zz, jint cHandle, jshortArray count) {
        jshort *sa= (*env)->GetShortArrayElements(env, count, 0);
	jint status= (jint) RC(CountSubControls((ControlRef)cHandle, sa));
	(*env)->ReleaseShortArrayElements(env, count, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetIndexedSubControl(JNIEnv *env, jclass zz,
				jint cHandle, jshort index, jintArray outCHandle) {
        jint *sa= (*env)->GetIntArrayElements(env, outCHandle, 0);
	jint status= (jint) RC(GetIndexedSubControl((ControlRef) cHandle, index, (ControlRef*) sa));
	(*env)->ReleaseIntArrayElements(env, outCHandle, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetSuperControl(JNIEnv *env, jclass zz, jint cHandle, jintArray parentHandle) {
        jint *sa= (*env)->GetIntArrayElements(env, parentHandle, 0);
	jint status= (jint) RC(GetSuperControl((ControlRef)cHandle, (ControlRef*) sa));
	(*env)->ReleaseIntArrayElements(env, parentHandle, sa, 0);
	return status;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsValidControlHandle(JNIEnv *env, jclass zz, jint cHandle) {
	return (jboolean) IsValidControlHandle((ControlRef)cHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlReference(JNIEnv *env, jclass zz, jint cHandle, jint data) {
	SetControlReference((ControlRef)cHandle, data);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlReference(JNIEnv *env, jclass zz, jint cHandle) {
	return (jint) GetControlReference((ControlRef)cHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlTitleAsCFString(JNIEnv *env, jclass zz,
				jint cHandle, jintArray sHandle) {
        jint *sa= (*env)->GetIntArrayElements(env, sHandle, 0);
	jint status= (jint) RC(CopyControlTitleAsCFString((ControlRef)cHandle, (CFStringRef*) sa));
	(*env)->ReleaseIntArrayElements(env, sHandle, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlTitleWithCFString(JNIEnv *env, jclass zz, jint cHandle, jint sHandle) {
	return (jint) RC(SetControlTitleWithCFString((ControlRef)cHandle, (CFStringRef) sHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_EnableControl(JNIEnv *env, jclass zz, jint cHandle) {
	return (jint) RC(EnableControl((ControlRef)cHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisableControl(JNIEnv *env, jclass zz, jint cHandle) {
	return (jint) RC(DisableControl((ControlRef)cHandle));
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS_IsControlEnabled(JNIEnv *env, jclass zz, jint cHandle) {
	return (jboolean) IsControlEnabled((ControlRef)cHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControl32BitMaximum(JNIEnv *env, jclass zz, jint cHandle) {
	return GetControl32BitMaximum((ControlRef)cHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControl32BitMaximum(JNIEnv *env, jclass zz, jint cHandle, jint max) {
	SetControl32BitMaximum((ControlRef)cHandle, max);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControl32BitMinimum(JNIEnv *env, jclass zz, jint cHandle) {
	return GetControl32BitMinimum((ControlRef)cHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlMinimum(JNIEnv *env, jclass zz, jint cHandle, jshort min) {
	SetControlMinimum((ControlRef)cHandle, min);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControl32BitMinimum(JNIEnv *env, jclass zz, jint cHandle, jint min) {
	SetControl32BitMinimum((ControlRef)cHandle, min);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControl32BitValue(JNIEnv *env, jclass zz, jint cHandle) {
	return GetControl32BitValue((ControlRef)cHandle);
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlValue(JNIEnv *env, jclass zz, jint cHandle) {
	return GetControlValue((ControlRef)cHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControl32BitValue(JNIEnv *env, jclass zz, jint cHandle, jint value) {
	SetControl32BitValue((ControlRef)cHandle, value);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlViewSize(JNIEnv *env, jclass zz, jint cHandle) {
	return (jint) GetControlViewSize((ControlRef)cHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlViewSize(JNIEnv *env, jclass zz, jint cHandle, jint viewSize) {
	SetControlViewSize((ControlRef)cHandle, viewSize);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_IdleControls(JNIEnv *env, jclass zz, jint wHandle) {
	IdleControls((WindowRef)wHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetBestControlRect(JNIEnv *env, jclass zz, jint cHandle,
		jshortArray rect, jshortArray base) {
        jshort *sa= (*env)->GetShortArrayElements(env, rect, 0);
        jshort *sb= (*env)->GetShortArrayElements(env, base, 0);
	jint status= (jint) RC(GetBestControlRect((ControlRef)cHandle, (Rect*) sa, (short*) sb));
	(*env)->ReleaseShortArrayElements(env, rect, sa, 0);
	(*env)->ReleaseShortArrayElements(env, base, sb, 0);
        return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlKind(JNIEnv *env, jclass zz, jint cHandle, jintArray kind) {
        jint *sa= (*env)->GetIntArrayElements(env, kind, 0);
	jint status= (jint) RC(GetControlKind((ControlRef)cHandle, (ControlKind*) sa));
	(*env)->ReleaseIntArrayElements(env, kind, sa, 0);
        return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlData__ISI_3I(JNIEnv *env, jclass zz, jint cHandle,
		jshort partCode, jint tag, jintArray data) {
	Size outSize;
        jint *sa= (*env)->GetIntArrayElements(env, data, 0);
	jsize length= (*env)->GetArrayLength(env, data);
	OSErr error= RC(GetControlData((ControlRef) cHandle, partCode, tag, sizeof(int)*length, sa, &outSize));
	(*env)->ReleaseIntArrayElements(env, data, sa, 0);
	return error;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetControlData__ISI_3S(JNIEnv *env, jclass zz, jint cHandle,
		jshort partCode, jint tag, jshortArray data) {
	Size outSize;
        jshort *sa= (*env)->GetShortArrayElements(env, data, 0);
	jsize length= (*env)->GetArrayLength(env, data);
	OSErr error= RC(GetControlData((ControlRef) cHandle, partCode, tag, sizeof(short)*length, sa, &outSize));
	(*env)->ReleaseShortArrayElements(env, data, sa, 0);
	return error;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlData__ISII(JNIEnv *env, jclass zz,
		jint cHandle, jshort partCode, jint tag, jint data) {
	return RC(SetControlData((ControlRef) cHandle, partCode, tag, sizeof(int), (Ptr) &data));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetControlData__ISI_3S(JNIEnv *env, jclass zz,
		jint cHandle, jshort partCode, jint tag, jshortArray data) {
        jshort *sa= (*env)->GetShortArrayElements(env, data, 0);
	jsize length= (*env)->GetArrayLength(env, data);
	jint status= (jint) RC(SetControlData((ControlRef) cHandle, partCode, tag, sizeof(short)*length, (Ptr) sa));
	(*env)->ReleaseShortArrayElements(env, data, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_SetBevelButtonContentInfo(JNIEnv *env, jclass zz,
		jint cHandle, jshort controlContentType, jint controlContent) {
		
	ControlButtonContentInfo info;
	
	info.contentType= (ControlContentType) controlContentType;
	info.u.cIconHandle= (CIconHandle) controlContent;
	
	return RC(SetBevelButtonContentInfo((ControlRef) cHandle, &info));
}

// ControlDraw callback
static struct Closure *gControlDrawClosure;
static pascal void userPaneDrawProc(ControlRef theControl, SInt16 thePart) {
	if (gControlDrawClosure != NULL) {
		JNIEnv *env= gControlDrawClosure->env;
		(*env)->CallVoidMethod(env, gControlDrawClosure->target, gControlDrawClosure->action, (jint)theControl, (jshort)thePart);
	}
}
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewControlUserPaneDrawUPP(JNIEnv *env, jclass zz, jobject target, jstring method) {
	if (gControlDrawClosure != NULL) {
		fprintf(stderr, "InstallControlDrawHandler: can't install more than one callback\n");
		exit(-1);
	}
	gControlDrawClosure= createClosure(env, target, method, "(IS)V");
	return (jint) NewControlUserPaneDrawUPP(userPaneDrawProc);
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_HandleControlKey(JNIEnv *env, jclass zz,
			jint cHandle, jshort inKeyCode, jchar inCharCode, jint modifiers) {
	return (ControlPartCode) HandleControlKey((ControlRef)cHandle, (SInt16)inKeyCode, (SInt16)inCharCode, (EventModifiers) modifiers);
}

// ActionProc callback
static struct Closure *gControlActionClosure;
static void controlActionProc(ControlRef theControl, ControlPartCode partCode) {
	//fprintf(stderr, "callback: %ld %d\n", theControl, partCode);
	if (gControlActionClosure != NULL) {
		JNIEnv *env= gControlActionClosure->env;
		(*env)->CallVoidMethod(env, gControlActionClosure->target, gControlActionClosure->action, (jint)theControl, (jshort)partCode);
	}
}
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewControlActionUPP(JNIEnv *env, jclass zz, jobject target, jstring method) {
	if (gControlActionClosure != NULL) {
		fprintf(stderr, "NewControlActionUPP: can't install more than one callback\n");
		exit(-1);
	}
	gControlActionClosure= createClosure(env, target, method, "(IS)V");
	return (jint) NewControlActionUPP(controlActionProc);
}

// Quit callback
static pascal OSErr QuitAppleEventHandler(const AppleEvent *appleEvt, AppleEvent *reply, long refcon) {
	struct Closure *closure= (struct Closure*) refcon;
	JNIEnv *env= closure->env;
	(*env)->CallVoidMethod(env, closure->target, closure->action);
	return noErr;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_installQuitHandler(JNIEnv *env, jclass zz, jobject target, jstring method) {
	AEInstallEventHandler(kCoreEventClass, kAEQuitApplication, NewAEEventHandlerUPP(QuitAppleEventHandler),
					   (long)createClosure(env, target, method, "()V"), false);
}

// Timer CallBack

static struct Closure *gTimerClosure;
static void EventLoopTimerProc(EventLoopTimerRef inTimer, void *inUserData) {
	if (gTimerClosure != NULL) {
		JNIEnv *env= gTimerClosure->env;
		(*env)->CallIntMethod(env, gTimerClosure->target, gTimerClosure->action, (jint)inUserData, (jint)inTimer);
	}
}
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewEventLoopTimerUPP(JNIEnv *env, jclass zz, jobject target, jstring method) {
	if (gTimerClosure != NULL) {
		fprintf(stderr, "NewEventLoopTimerUPP: can't install more than one callback\n");
		exit(-1);
	}
	gTimerClosure= createClosure(env, target, method, "(II)I");
	return (jint) NewEventLoopTimerUPP(EventLoopTimerProc);
}

// Caret Timer CallBack

static struct Closure *gTimerClosure2;
static void EventLoopTimerProc2(EventLoopTimerRef inTimer, void *inUserData) {
	if (gTimerClosure2 != NULL) {
		JNIEnv *env= gTimerClosure2->env;
		(*env)->CallIntMethod(env, gTimerClosure2->target, gTimerClosure2->action, (jint)inUserData, (jint)inTimer);
	}
}
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewEventLoopTimerUPP2(JNIEnv *env, jclass zz, jobject target, jstring method) {
	if (gTimerClosure2 != NULL) {
		fprintf(stderr, "NewEventLoopTimerUPP2: can't install more than one callback\n");
		exit(-1);
	}
	gTimerClosure2= createClosure(env, target, method, "(II)I");
	return (jint) NewEventLoopTimerUPP(EventLoopTimerProc2);
}

// Caret Timer CallBack

static struct Closure *gTimerClosure3;
static void EventLoopTimerProc3(EventLoopTimerRef inTimer, void *inUserData) {
	if (gTimerClosure3 != NULL) {
		JNIEnv *env= gTimerClosure3->env;
		(*env)->CallIntMethod(env, gTimerClosure3->target, gTimerClosure3->action, (jint)inUserData, (jint)inTimer);
	}
}
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewEventLoopTimerUPP3(JNIEnv *env, jclass zz, jobject target, jstring method) {
	if (gTimerClosure3 != NULL) {
		fprintf(stderr, "NewEventLoopTimerUPP3: can't install more than one callback\n");
		exit(-1);
	}
	gTimerClosure3= createClosure(env, target, method, "(II)I");
	return (jint) NewEventLoopTimerUPP(EventLoopTimerProc3);
}

// Menu Callback
static struct Closure *gMenuClosure;
static OSStatus MenuCallbackProc(EventHandlerCallRef nextHandler, EventRef theEvent, void *userData) {
	if (gMenuClosure != NULL) {
		JNIEnv *env= gMenuClosure->env;
		return (*env)->CallIntMethod(env, gMenuClosure->target, gMenuClosure->action, (jint)theEvent, (jint)userData);
	}
	return noErr;	 // Report success      
} 
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewMenuCallbackUPP(JNIEnv *env, jclass zz, jobject target, jstring method) {
	if (gMenuClosure != NULL) {
		fprintf(stderr, "NewMenuCallbackUPP: can't install more than one callback\n");
		exit(-1);
	}
	gMenuClosure= createClosure(env, target, method, "(II)I");
	return (jint) NewEventHandlerUPP(MenuCallbackProc);
}

// Control Callback
static struct Closure *gControlClosure;
static OSStatus ControlCallbackProc(EventHandlerCallRef nextHandler, EventRef theEvent, void *userData) {
	if (gControlClosure != NULL) {
		JNIEnv *env= gControlClosure->env;
		return (*env)->CallIntMethod(env, gControlClosure->target, gControlClosure->action, (jint)theEvent, (jint)userData);
	}
	return noErr;	 // Report success      
} 
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewControlCallbackUPP(JNIEnv *env, jclass zz, jobject target, jstring method) {
	if (gControlClosure != NULL) {
		fprintf(stderr, "NewControlCallbackUPP: can't install more than one callback\n");
		exit(-1);
	}
	gControlClosure= createClosure(env, target, method, "(II)I");
	return (jint) NewEventHandlerUPP(ControlCallbackProc);
}

// User Pane hit test Callback
static struct Closure *gUserPaneHitTestClosure;
static pascal ControlPartCode UserPaneHitTestProc(ControlRef theControl, Point where) {
	if (gUserPaneHitTestClosure != NULL) {
		JNIEnv *env= gUserPaneHitTestClosure->env;
		return (*env)->CallIntMethod(env, gUserPaneHitTestClosure->target, gUserPaneHitTestClosure->action, (jint)theControl,
                                (jint)where.h, (jint)where.v);
	}
	return 0;
}
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewUserPaneHitTestUPP(JNIEnv *env, jclass zz,
					jobject target, jstring method) {
	if (gUserPaneHitTestClosure != NULL) {
		fprintf(stderr, "NewUserPaneHitTestUPP: can't install more than one callback\n");
		exit(-1);
	}
	gUserPaneHitTestClosure= createClosure(env, target, method, "(III)I");
	return (jint) NewControlUserPaneHitTestUPP(UserPaneHitTestProc);
}

// Text Callback
static struct Closure *gTextClosure;
static OSStatus TextCallbackProc(EventHandlerCallRef nextHandler, EventRef eventRef, void *userData) {
	if (gTextClosure != NULL) {
		JNIEnv *env= gTextClosure->env;
		return (*env)->CallIntMethod(env, gTextClosure->target, gTextClosure->action, (jint)nextHandler, (jint)eventRef);
	}
	return 0;
}
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewTextCallbackUPP(JNIEnv *env, jclass zz,
					jobject target, jstring method) {
	if (gTextClosure != NULL) {
		fprintf(stderr, "NewTextCallbackUPP: can't install more than one callback\n");
		exit(-1);
	}
	gTextClosure= createClosure(env, target, method, "(II)I");
	return (jint) NewEventHandlerUPP(TextCallbackProc);
}

// Mouse Moved Callback
static struct Closure *gMouseMovedClosure;
static OSStatus MouseMovedCallbackProc(EventHandlerCallRef nextHandler, EventRef eventRef, void *userData) {
	if (gMouseMovedClosure != NULL) {
		JNIEnv *env= gMouseMovedClosure->env;
		return (*env)->CallIntMethod(env, gMouseMovedClosure->target, gMouseMovedClosure->action, (jint)nextHandler, (jint)eventRef);
	}
	return 0;
}
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewMouseMovedCallbackUPP(JNIEnv *env, jclass zz,
					jobject target, jstring method) {
	if (gMouseMovedClosure != NULL) {
		fprintf(stderr, "NewMouseMovedCallbackUPP: can't install more than one callback\n");
		exit(-1);
	}
	gMouseMovedClosure= createClosure(env, target, method, "(II)I");
	return (jint) NewEventHandlerUPP(MouseMovedCallbackProc);
}

// Window Callback
static struct Closure *gWindowClosure;
static OSStatus WindowCallbackProc(EventHandlerCallRef nextHandler, EventRef eventRef, void *userData) {
	if (gWindowClosure != NULL) {
		JNIEnv *env= gWindowClosure->env;
		return (*env)->CallIntMethod(env, gWindowClosure->target, gWindowClosure->action, (jint)nextHandler, (jint)eventRef, (jint)userData);
	}
	return 0;
}
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewWindowCallbackUPP(JNIEnv *env, jclass zz,
					jobject target, jstring method) {
	if (gWindowClosure != NULL) {
		fprintf(stderr, "NewWindowCallbackUPP: can't install more than one callback\n");
		exit(-1);
	}
	gWindowClosure= createClosure(env, target, method, "(III)I");
	return (jint) NewEventHandlerUPP(WindowCallbackProc);
}

// Application Callback
static struct Closure *gApplicationClosure;
static OSStatus ApplicationCallbackProc(EventHandlerCallRef nextHandler, EventRef eventRef, void *userData) {
	if (gWindowClosure != NULL) {
		JNIEnv *env= gApplicationClosure->env;
		return (*env)->CallIntMethod(env, gApplicationClosure->target, gApplicationClosure->action, (jint)nextHandler,
                                (jint)eventRef, (jint)userData);
	}
	return 0;
}
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NewApplicationCallbackUPP(JNIEnv *env, jclass zz,
					jobject target, jstring method) {
	if (gApplicationClosure != NULL) {
		fprintf(stderr, "NewApplicationCallbackUPP: can't install more than one callback\n");
		exit(-1);
	}
	gApplicationClosure= createClosure(env, target, method, "(III)I");
	return (jint) NewEventHandlerUPP(ApplicationCallbackProc);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_RunApplicationEventLoop(JNIEnv *env, jclass zz) {
	RunApplicationEventLoop();
}


//---- standard dialogs

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_PickColor(JNIEnv *env, jclass zz,
			jshortArray rgb, jshortArray where, jbyteArray title, jbooleanArray success) {

	jint status;
	ColorPickerInfo info;

    jshort *sa= (*env)->GetShortArrayElements(env, rgb, 0);
    jshort *sb= (*env)->GetShortArrayElements(env, where, 0);
    jbyte *sc= (*env)->GetByteArrayElements(env, title, 0);
    jboolean *sd= (*env)->GetBooleanArrayElements(env, success, 0);
	
	info.theColor.profile= NULL;
	info.theColor.color.rgb.red= sa[0];
	info.theColor.color.rgb.green= sa[1];
	info.theColor.color.rgb.blue= sa[2];
	info.dstProfile= NULL;
	info.flags= kColorPickerDialogIsMoveable | kColorPickerDialogIsModal;
	info.placeWhere= kAtSpecifiedOrigin;
	info.dialogOrigin.v= sb[0];
	info.dialogOrigin.h= sb[1];
	info.pickerType= 0;
	info.eventProc= NULL;
	info.colorProc= NULL;
	info.colorProcData= 0;
	memcpy(info.prompt, sc, (size_t) sc[0]);
	info.mInfo.editMenuID= 0;
	info.mInfo.cutItem= 0;
	info.mInfo.copyItem= 0;
	info.mInfo.pasteItem= 0;
	info.mInfo.clearItem= 0;
	info.mInfo.undoItem= 0;
	
	status= (jint) RC(PickColor(&info));
	
	sd[0]= info.newColorChosen;
	
	if (info.newColorChosen) {
		sa[0]= info.theColor.color.rgb.red;
		sa[1]= info.theColor.color.rgb.green;
		sa[2]= info.theColor.color.rgb.blue;
	}
	
	(*env)->ReleaseShortArrayElements(env, rgb, sa, 0);
	(*env)->ReleaseShortArrayElements(env, where, sb, 0);
	(*env)->ReleaseByteArrayElements(env, title, sc, 0);
	(*env)->ReleaseBooleanArrayElements(env, success, sd, 0);

	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavCreateGetFileDialog(JNIEnv *env, jclass zz,
				jint optionFlags, jint windowTitle, jint whandle, jintArray dialogHandle) {
	
	NavDialogCreationOptions options;
	jint status;
        jint *sa= (*env)->GetIntArrayElements(env, dialogHandle, 0);
	
	NavGetDefaultDialogCreationOptions(&options);
	options.optionFlags |= optionFlags;
	options.parentWindow= (WindowRef) whandle;
	
	status= RC(NavCreateGetFileDialog(&options, NULL, NULL, NULL, NULL, NULL, (NavDialogRef*)sa));
	
	(*env)->ReleaseIntArrayElements(env, dialogHandle, sa, 0);
	
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavCreatePutFileDialog(JNIEnv *env, jclass zz,
				jint fileCreator, jint windowTitle, jint whandle, jintArray dialogHandle, 
				jint optionFlags, jint fileType) {
	
	NavDialogCreationOptions options;
	jint status;
        jint *sa= (*env)->GetIntArrayElements(env, dialogHandle, 0);
	
	NavGetDefaultDialogCreationOptions(&options);
	options.optionFlags |= optionFlags;
	options.parentWindow= (WindowRef) whandle;
	options.windowTitle= (CFStringRef) windowTitle;
	
	status= RC(NavCreatePutFileDialog(&options, fileType, fileCreator, NULL, NULL, (NavDialogRef*)sa));
	
	(*env)->ReleaseIntArrayElements(env, dialogHandle, sa, 0);
	
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavCreateChooseFolderDialog(JNIEnv *env, jclass zz,
				jint optionFlags, jint windowTitle, jint messageHandle, jint whandle, jintArray dialogHandle) {
	
	NavDialogCreationOptions options;
	jint status;
        jint *sa= (*env)->GetIntArrayElements(env, dialogHandle, 0);
	
	NavGetDefaultDialogCreationOptions(&options);
	options.optionFlags |= optionFlags;
	options.parentWindow= (WindowRef) whandle;
	options.windowTitle= (CFStringRef) windowTitle;
	options.message= (CFStringRef) messageHandle;
	
	status= RC(NavCreateChooseFolderDialog(&options, NULL, NULL, NULL, (NavDialogRef*)sa));
	
	(*env)->ReleaseIntArrayElements(env, dialogHandle, sa, 0);
	
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogSetSaveFileName(JNIEnv *env, jclass zz,
			jint dialogHandle, jint fileName) {
	return (jint) RC(NavDialogSetSaveFileName((NavDialogRef) dialogHandle, (CFStringRef) fileName));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogGetSaveFileName(JNIEnv *env, jclass zz,
			jint dialogHandle) {
	return (jint) NavDialogGetSaveFileName((NavDialogRef) dialogHandle);	
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogRun(JNIEnv *env, jclass zz,
				jint dialogHandle) {	
	return (jint) RC(NavDialogRun((NavDialogRef) dialogHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogGetUserAction(JNIEnv *env, jclass zz,
				jint dialogHandle) {	
	return (jint) RC(NavDialogGetUserAction((NavDialogRef) dialogHandle));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogGetReply(JNIEnv *env, jclass zz,
				jint dialogHandle, jintArray replyHandle) {
	NavReplyRecord *reply= (NavReplyRecord*) malloc(sizeof(NavReplyRecord));
        jint *sa= (*env)->GetIntArrayElements(env, replyHandle, 0);
	sa[0]= (jint) reply;
	(*env)->ReleaseIntArrayElements(env, replyHandle, sa, 0);

	return (jint) RC(NavDialogGetReply((NavDialogRef) dialogHandle, reply));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogDisposeReply(JNIEnv *env, jclass zz,
				jint replyHandle) {
	free((NavReplyRecord*) replyHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavReplyRecordGetSelection(JNIEnv *env, jclass zz,
				jint replyHandle) {
	NavReplyRecord *reply= (NavReplyRecord*) replyHandle;
	return (jint) &reply->selection;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AECountItems(JNIEnv *env, jclass zz,
				jint aeDescList, jintArray count) {
        jint *sa= (*env)->GetIntArrayElements(env, count, 0);
	int status= (jint) RC(AECountItems((const AEDescList*)aeDescList, (long*)sa));
	(*env)->ReleaseIntArrayElements(env, count, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_AEGetNthPtr(JNIEnv *env, jclass zz,
				jint aeDescList, jint index, jintArray sHandle) {
	AEKeyword keyWord;
	DescType returnedType;
	FSRef fileSpec;
	Size actualSize;
        jint *sa= (*env)->GetIntArrayElements(env, sHandle, 0);
	
	jint status= RC(AEGetNthPtr((const AEDescList*)aeDescList, index, typeFSRef, &keyWord, &returnedType,
                            &fileSpec, sizeof(fileSpec), &actualSize));

	if (status == 0) {
		CFURLRef url= CFURLCreateFromFSRef(kCFAllocatorDefault, &fileSpec);
		sa[0]= (jint) CFURLCopyFileSystemPath(url, kCFURLPOSIXPathStyle);
	}
	
	(*env)->ReleaseIntArrayElements(env, sHandle, sa, 0);
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_NavDialogDispose(JNIEnv *env, jclass zz,
				jint dialogHandle) {	
	NavDialogDispose((NavDialogRef) dialogHandle);
}

//---- Misc

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_InitCursor(JNIEnv *env, jclass zz) {
	InitCursor();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_ExitToShell(JNIEnv *env, jclass zz) {
	ExitToShell();
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_HiWord(JNIEnv *env, jclass zz, jint i) {
	return HiWord(i);
}

JNIEXPORT jshort JNICALL Java_org_eclipse_swt_internal_carbon_OS_LoWord(JNIEnv *env, jclass zz, jint i) {
	return LoWord(i);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_SysBeep(JNIEnv *env, jclass zz, jshort duration) {
	SysBeep((short)duration);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetDblTime(JNIEnv *env, jclass zz) {
	return GetDblTime();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetCaretTime(JNIEnv *env, jclass zz) {
	return GetCaretTime();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetMainDevice(JNIEnv *env, jclass zz) {
	return (jint) GetMainDevice();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetAvailableWindowPositioningBounds(JNIEnv *env,
				jclass zz, jint gHandle, jshortArray bounds) {
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
	OSErr error= RC(GetAvailableWindowPositioningBounds((GDHandle) gHandle, (Rect*)sa));
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
	return error;
}

// Strings

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFStringCreateWithCharacters(JNIEnv *env, jclass zz, jstring s) {
	const jchar *buffer= (*env)->GetStringChars(env, s, NULL);
	CFIndex length= (*env)->GetStringLength(env, s);
	CFStringRef sref= CFStringCreateWithCharacters(NULL, (const UniChar*) buffer, length);
	(*env)->ReleaseStringChars(env, s, buffer);
	return (jint) sref;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFRelease(JNIEnv *env, jclass zz, jint sHandle) {
	CFRelease((CFStringRef)sHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFStringGetLength(JNIEnv *env, jclass zz, jint sHandle) {
	return (jint) CFStringGetLength((CFStringRef)sHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_CFStringGetCharacters(JNIEnv *env, jclass zz,
					jint sHandle, jint start, jint length, jcharArray buffer) {
					
        jchar *sa= (*env)->GetCharArrayElements(env, buffer, 0);
 	CFStringGetCharacters((CFStringRef)sHandle, CFRangeMake(start, length), (UniChar*) sa);
	(*env)->ReleaseShortArrayElements(env, buffer, sa, 0);
}

//---- Alerts

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_CreateStandardAlert(JNIEnv *env, jclass zz,
						jshort alertType, jint messageHandle, jint explanationHandle, jint param, jintArray dialogHandle) {
	
        jint *sa= (*env)->GetIntArrayElements(env, dialogHandle, 0);
	jint status= RC(CreateStandardAlert((AlertType)alertType, (CFStringRef)messageHandle, (CFStringRef)explanationHandle,
				(const AlertStdCFStringAlertParamRec*) param, (DialogRef*)sa));
	(*env)->ReleaseIntArrayElements(env, dialogHandle, sa, 0);
	return status;
}	

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_RunStandardAlert(JNIEnv *env, jclass zz,
						jint alertHandle, jint filterProc, jshortArray itemHit) {
        jshort *sa= (*env)->GetShortArrayElements(env, itemHit, 0);
	jint status= (jint) RC(RunStandardAlert((DialogRef)alertHandle, (ModalFilterUPP)filterProc, (DialogItemIndex*)sa));
	(*env)->ReleaseShortArrayElements(env, itemHit, sa, 0);
	return status;
}

//---- MLTE Text

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNInitTextension(JNIEnv *env, jclass zz) {
	return (jint) RC(TXNInitTextension(NULL, 0, 0));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNNewObject(JNIEnv *env, jclass zz,
			jint fileSpec, jint wHandle, jshortArray bounds, jint frameOptions, jint frameType, jint fileType,
					jint permanentEncoding, jintArray txHandle, jintArray frameID, jint refcon) {
			
        jshort *sa= (*env)->GetShortArrayElements(env, bounds, 0);
        jint *sb= (*env)->GetIntArrayElements(env, txHandle, 0);
        jint *sc= (*env)->GetIntArrayElements(env, frameID, 0);
				
	jint status= (jint) RC(TXNNewObject((const FSSpec*)fileSpec, (WindowRef)wHandle, (Rect*)bounds, (TXNFrameOptions)frameOptions,
				(TXNFrameType)frameType, (TXNFileType)fileType, (TXNPermanentTextEncodingType)permanentEncoding,
					(TXNObject*)sb, (TXNFrameID*)sc, (TXNObjectRefcon)refcon));
	
	(*env)->ReleaseShortArrayElements(env, bounds, sa, 0);
	(*env)->ReleaseIntArrayElements(env, txHandle, sb, 0);
	(*env)->ReleaseIntArrayElements(env, frameID, sc, 0);
	
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNDeleteObject(JNIEnv *env, jclass zz, jint txHandle) {
	TXNDeleteObject((TXNObject)txHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSetFrameBounds(JNIEnv *env, jclass zz,
			jint txHandle, jint top, jint left, jint bottom, jint right, jint frameID) {
	TXNSetFrameBounds((TXNObject)txHandle, top, left, bottom, right, (TXNFrameID) frameID);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNDraw(JNIEnv *env, jclass zz, jint txHandle, jint gDevice) {
	TXNDraw((TXNObject)txHandle, (GWorldPtr)gDevice);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetData(JNIEnv *env, jclass zz,
					jint txHandle, jint startOffset, jint endOffset, jintArray dataHandle) {
        jint *sa= (*env)->GetIntArrayElements(env, dataHandle, 0);
	jint status= (jint) RC(TXNGetData((TXNObject)txHandle, startOffset, endOffset, (Handle*)sa));
	(*env)->ReleaseIntArrayElements(env, dataHandle, sa, 0);
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_DisposeHandle(JNIEnv *env, jclass zz, jint handle) {
	DisposeHandle((Handle)handle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_GetHandleSize(JNIEnv *env, jclass zz, jint handle) {
	return GetHandleSize((Handle)handle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_getHandleData(JNIEnv *env, jclass zz,
				jint hndl, jcharArray data) {
	Handle handle= (Handle)hndl;
        jchar *sa= (*env)->GetCharArrayElements(env, data, 0);
	int length= (*env)->GetArrayLength(env, data);
	memcpy(sa, *handle, length*sizeof(jchar));
	(*env)->ReleaseCharArrayElements(env, data, sa, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSetData(JNIEnv *env, jclass zz,
					jint txHandle, jcharArray unicodeChars, jint startOffset, jint endOffset) {
        jchar *sa= (*env)->GetCharArrayElements(env, unicodeChars, 0);
        jsize length= (*env)->GetArrayLength(env, unicodeChars);
	jint status= (jint) RC(TXNSetData((TXNObject)txHandle, kTXNUnicodeTextData, (void*) sa, length*sizeof(jchar), startOffset, endOffset));
	(*env)->ReleaseCharArrayElements(env, unicodeChars, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetLineCount(JNIEnv *env, jclass zz,
					jint txHandle, jintArray lineCount) {
        jint *sa= (*env)->GetIntArrayElements(env, lineCount, 0);
	jint status= (jint) RC(TXNGetLineCount((TXNObject)txHandle, (ItemCount*) sa));
	(*env)->ReleaseIntArrayElements(env, lineCount, sa, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNDataSize(JNIEnv *env, jclass zz, jint txHandle) {
	return (jint) TXNDataSize((TXNObject)txHandle);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetSelection(JNIEnv *env, jclass zz,
			jint txHandle, jintArray start, jintArray end) {
        jint *sa= (*env)->GetIntArrayElements(env, start, 0);
        jint *sb= (*env)->GetIntArrayElements(env, end, 0);
	TXNGetSelection((TXNObject)txHandle, (TXNOffset*)sa, (TXNOffset*)sb);	
	(*env)->ReleaseIntArrayElements(env, start, sa, 0);
	(*env)->ReleaseIntArrayElements(env, end, sb, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSetSelection(JNIEnv *env, jclass zz,
			jint txHandle, jint start, jint end) {
	return (jint) RC(TXNSetSelection((TXNObject)txHandle, (TXNOffset)start, (TXNOffset)end));	
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSelectAll(JNIEnv *env, jclass zz, jint txHandle) {
	TXNSelectAll((TXNObject)txHandle);	
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNShowSelection(JNIEnv *env, jclass zz,
			jint txHandle, jboolean showEnd) {
	TXNShowSelection((TXNObject)txHandle, (Boolean)showEnd);	
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNKeyDown(JNIEnv *env, jclass zz,	
					jint txHandle, jintArray eventData) {
	EventRecord event;
	copyEventData(env, &event, eventData);
	TXNKeyDown((TXNObject)txHandle, &event);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNClick(JNIEnv *env, jclass zz,	
					jint txHandle, jintArray eventData) {
	EventRecord event;
	copyEventData(env, &event, eventData);
	TXNClick((TXNObject)txHandle, &event);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNFocus(JNIEnv *env, jclass zz,
			jint txHandle, jboolean becomingFocused) {
	TXNFocus((TXNObject)txHandle, (Boolean)becomingFocused);	
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNCut(JNIEnv *env, jclass zz, jint txHandle) {
	return (jint) RC(TXNCut((TXNObject)txHandle));	
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNCopy(JNIEnv *env, jclass zz, jint txHandle) {
	return (jint) RC(TXNCopy((TXNObject)txHandle));	
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNPaste(JNIEnv *env, jclass zz, jint txHandle) {
	return (jint) RC(TXNPaste((TXNObject)txHandle));	
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetRectBounds(JNIEnv *env, jclass zz,
			jint txHandle, jshortArray viewRect, jintArray destRect, jintArray textRect) {
        jshort *sa= NULL;
        jint *sb= NULL;
        jint *sc= NULL;
	jint status;
	if (viewRect != NULL)
		sa= (*env)->GetShortArrayElements(env, viewRect, 0);
	if (destRect != NULL)
		sb= (*env)->GetIntArrayElements(env, destRect, 0);
	if (textRect != NULL)
		sc= (*env)->GetIntArrayElements(env, textRect, 0);
	status= (jint) RC(TXNGetRectBounds((TXNObject)txHandle, (Rect*)sa, (TXNLongRect*)sb, (TXNLongRect*)sc));
	if (sa != NULL)
		(*env)->ReleaseShortArrayElements(env, viewRect, sa, 0);
	if (sb != NULL)
		(*env)->ReleaseIntArrayElements(env, destRect, sb, 0);
	if (sc != NULL)
		(*env)->ReleaseIntArrayElements(env, textRect, sc, 0);
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSetRectBounds(JNIEnv *env, jclass zz,
			jint txHandle, jshortArray viewRect, jintArray destRect, jboolean update) {
        jshort *sa= NULL;
        jint *sb= NULL;
	if (viewRect != NULL)
		sa= (*env)->GetShortArrayElements(env, viewRect, 0);
	if (destRect != NULL)
		sb= (*env)->GetIntArrayElements(env, destRect, 0);
	TXNSetRectBounds((TXNObject)txHandle, (Rect*)sa, (TXNLongRect*)sb, update);
	if (sa != NULL)
		(*env)->ReleaseShortArrayElements(env, viewRect, sa, 0);
	if (sb != NULL)
		(*env)->ReleaseIntArrayElements(env, destRect, sb, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNActivate(JNIEnv *env, jclass zz,
				jint txHandle, jint frameID, jboolean scrollBarState) {
	return (jint) RC(TXNActivate((TXNObject)txHandle, (TXNFrameID)frameID, (TXNScrollBarState)scrollBarState));	
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNEchoMode(JNIEnv *env, jclass zz,
				jint txHandle, jchar echoCharacter, jint encoding, jboolean on) {
	return (jint) RC(TXNEchoMode((TXNObject)txHandle, echoCharacter, encoding, on));	
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNOffsetToPoint(JNIEnv *env, jclass zz,
				jint txHandle, jint offset, jshortArray location) {
	jshort *sa= (*env)->GetShortArrayElements(env, location, 0);
	jint status= (jint) RC(TXNOffsetToPoint((TXNObject)txHandle, offset, (Point*) sa));	
	(*env)->ReleaseShortArrayElements(env, location, sa, 0);
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNResizeFrame(JNIEnv *env, jclass zz,
				jint txHandle, jint width, jint height, jint frameID) {
	TXNResizeFrame((TXNObject)txHandle, width, height, frameID);	
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetViewRect(JNIEnv *env, jclass zz,
				jint txHandle, jshortArray viewRect) {
	jshort *sa= (*env)->GetShortArrayElements(env, viewRect, 0);
	TXNGetViewRect((TXNObject)txHandle, (Rect*) sa);	
	(*env)->ReleaseShortArrayElements(env, viewRect, sa, 0);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNGetLineMetrics(JNIEnv *env, jclass zz,
				jint txHandle, jint lineNumber, jintArray lineWidth, jintArray lineHeight) {
	jint *sa= (*env)->GetIntArrayElements(env, lineWidth, 0);
	jint *sb= (*env)->GetIntArrayElements(env, lineHeight, 0);
	jint status= (jint) RC(TXNGetLineMetrics((TXNObject)txHandle, (UInt32)lineNumber, (Fixed*)sa, (Fixed*)sb));	
	(*env)->ReleaseIntArrayElements(env, lineWidth, sa, 0);
	(*env)->ReleaseIntArrayElements(env, lineHeight, sb, 0);
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNForceUpdate(JNIEnv *env, jclass zz, jint txHandle) {
	TXNForceUpdate((TXNObject)txHandle);	
}

/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS_setTXNWordWrap(JNIEnv *env, jclass zz,
				jint txHandle, jboolean wrap) {
    TXNControlTag controlTag[1];
    TXNControlData controlData[1];
    controlTag[0]= kTXNWordWrapStateTag;
	controlData[0].uValue= wrap ? kTXNAutoWrap : kTXNNoAutoWrap; 
	TXNSetTXNObjectControls((TXNObject)txHandle, false, 1, controlTag, controlData);
}
*/

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS_TXNSetTXNObjectControls(JNIEnv *env, jclass zz,
			jint txHandle, jboolean clearAll, jint controlCount, jintArray controlTags, jintArray controlData) {
	jint *sa= (*env)->GetIntArrayElements(env, controlTags, 0);
	jint *sb= (*env)->GetIntArrayElements(env, controlData, 0);
	jint status= RC(TXNSetTXNObjectControls((TXNObject)txHandle, (Boolean)clearAll, (ItemCount) controlCount,
								(TXNControlTag*)sa, (TXNControlData*)sb));
	(*env)->ReleaseIntArrayElements(env, controlTags, sa, 0);
	(*env)->ReleaseIntArrayElements(env, controlData, sb, 0);
	return status;
}

//---- utilities

static Point point(JNIEnv *env, jshortArray a) {
	Point p;
	jshort *sa= (*env)->GetShortArrayElements(env, a, 0);
	p.v= sa[0];
	p.h= sa[1];
	(*env)->ReleaseShortArrayElements(env, a, sa, 0);
	return p;
}

static void copyEvent(JNIEnv *env, jintArray eData, EventRecord *event) {
	if (eData != NULL) {
		jint *sa= (*env)->GetIntArrayElements(env, eData, 0);
		sa[0]= (int) event->what;
		sa[1]= (int) event->message;
		sa[2]= (int) event->when;
		sa[3]= (int) event->where.v;
		sa[4]= (int) event->where.h;
		sa[5]= (int) event->modifiers;
		(*env)->ReleaseIntArrayElements(env, eData, sa, 0);
	}
}

static void copyEventData(JNIEnv *env, EventRecord *event, jintArray eData) {
	if (eData != NULL) {
		jint *sa= (*env)->GetIntArrayElements(env, eData, 0);
		event->what= (short) sa[0];
		event->message= sa[1];
		event->when= sa[2];
		event->where.v= (short) sa[3];
		event->where.h= (short) sa[4];
		event->modifiers= sa[5];
		(*env)->ReleaseIntArrayElements(env, eData, sa, 0);	
	}
}

static void setColor(struct RGBColor *c, int rgb) {
	c->red= (rgb >> 16) * 257;
	c->green= ((rgb >> 8) & 0xff) * 257;
	c->blue= (rgb & 0xff) * 257;
}
