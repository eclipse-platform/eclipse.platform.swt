/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * SWT OS custom natives implementation.
 */ 

//#define USE_ATSUI 1

static const Rect NULL_RECT;

// forward declarations
static Point point(JNIEnv *env, jshortArray a);
static void copyEvent(JNIEnv *env, jintArray eData, EventRecord *event);
static void copyEventData(JNIEnv *env, EventRecord *event, jintArray eData);

#ifdef DEBUG
#define RC(f) checkStatus(__LINE__, (f))

static int checkStatus(int line, int rc) {
    switch (rc) {
	case 0:
	case eventNotHandledErr:
	case eventLoopTimedOutErr:
	case errControlIsNotEmbedder:
		break;
	default:
        //fprintf(stderr, "OS: line: %d %d\n", line, rc);
		break;
	}
    return rc;
}
#else
#define RC(f) f
#endif

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

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_DrawThemeButton(JNIEnv *env, jclass zz,
				jobject bounds, jshort kind, jshortArray newInfoArray, jshortArray prevInfoArray, jint eraseProc,
					jint labelProc, jint userData) {
	ThemeButtonDrawInfo info;
	ThemeButtonDrawInfo newInfo, *prevInfo= NULL;	
	jint status= 0;
	Rect sa;
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
	
	getRectFields(env, bounds, &sa);

	status= (jint) RC(DrawThemeButton(&sa, (ThemeButtonKind)kind, &newInfo, prevInfo, (ThemeEraseUPP)eraseProc,
						(ThemeButtonDrawUPP) labelProc, (UInt32)userData));
						
	(*env)->ReleaseShortArrayElements(env, newInfoArray, sb, 0);
	if (sc != NULL)
		(*env)->ReleaseShortArrayElements(env, prevInfoArray, sc, 0);
	
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_setTabText(JNIEnv *env, jclass zz,
				jint cHandle, jint index, jint sHandle) {
	ControlTabInfoRecV1 tab;
			
	tab.version= kControlTabInfoVersionOne;
	tab.iconSuiteID= 0;
	tab.name= (CFStringRef) sHandle;
	return RC(SetControlData((ControlRef)cHandle, index, kControlTabInfoTag, sizeof(ControlTabInfoRecV1), &tab));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_setTabIcon(JNIEnv *env, jclass zz,
				jint cHandle, jint index, jint iconHandle) {
	ControlButtonContentInfo tab;
	CIconHandle ih= (CIconHandle) iconHandle;
			
	tab.contentType= kControlContentCIconHandle;
	tab.u.cIconHandle= ih;
	
	return RC(SetControlData((ControlRef)cHandle, index, kControlTabImageContentTag, sizeof(ControlButtonContentInfo), &tab));
}


JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_createDataBrowserControl(JNIEnv *env, jclass zz, jint wHandle) {
	ControlRef controlRef;
	DataBrowserCallbacks callbacks;
	
	CreateDataBrowserControl((WindowRef)wHandle, &NULL_RECT, kDataBrowserListView, &controlRef);
	
	callbacks.version= kDataBrowserLatestCallbacks;
	InitDataBrowserCallbacks(&callbacks);
	SetDataBrowserCallbacks(controlRef, &callbacks);
		
	return (jint) controlRef;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_setDataBrowserCallbacks(JNIEnv *env, jclass zz,
					jint cHandle, jint dataUPP, jint compareUPP, jint notificationUPP) {
	DataBrowserCallbacks callbacks;
	GetDataBrowserCallbacks((ControlRef) cHandle, &callbacks);
	callbacks.u.v1.itemDataCallback= (DataBrowserItemDataUPP) dataUPP;
	callbacks.u.v1.itemCompareCallback= (DataBrowserItemCompareUPP) compareUPP;
	callbacks.u.v1.itemNotificationCallback= (DataBrowserItemNotificationUPP) notificationUPP;
	SetDataBrowserCallbacks((ControlRef) cHandle, &callbacks);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_newColumnDesc(JNIEnv *env, jclass zz,
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
	columnDesc->headerBtnDesc.initialOrder= kDataBrowserOrderIncreasing;
	
	/*
	columnDesc.headerBtnDesc.titleAlignment= teCenter;
	columnDesc.headerBtnDesc.titleFontTypeID= kControlFontViewSystemFont;
	columnDesc.headerBtnDesc.btnFontStyle= normal;
	*/

	return (jint)columnDesc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_GetEventHICommand(JNIEnv *env, jclass zz,
			jint eRefHandle, jintArray outParamType) {
	jint status;
 	HICommand command;
	
	status= (jint) RC(GetEventParameter((EventRef)eRefHandle, kEventParamDirectObject, typeHICommand, 
			NULL, sizeof(HICommand), NULL, &command));
	
	if (outParamType != NULL) {
		jint *sa= (*env)->GetIntArrayElements(env, outParamType, 0);
		sa[0]= (jint) command.attributes;
		sa[1]= (jint) command.commandID;
		sa[2]= (jint) command.menu.menuRef;
		sa[3]= (jint) command.menu.menuItemIndex;
		(*env)->ReleaseIntArrayElements(env, outParamType, sa, 0);
	}

	return status;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS2_GetNextEvent(JNIEnv *env, jclass zz, jshort mask, jintArray eData) {
	EventRecord event;
	jboolean rc= GetNextEvent(mask, &event);
	copyEvent(env, eData, &event);
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_AEProcessAppleEvent(JNIEnv *env, jclass zz,
				jintArray eventData) {
	EventRecord event;
	copyEventData(env, &event, eventData);
	AEProcessAppleEvent(&event);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_MenuEvent(JNIEnv *env, jclass zz,
				jintArray eventData) {
	EventRecord event;
	copyEventData(env, &event, eventData);
	return (jint) MenuEvent(&event);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS2_IsShowContextualMenuClick(JNIEnv *env, jclass zz, jintArray eventData) {
	EventRecord event;
	copyEventData(env, &event, eventData);
	return (jboolean) IsShowContextualMenuClick(&event);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_carbon_OS2_ConvertEventRefToEventRecord(JNIEnv *env, jclass zz, jint eHandle, jintArray data) {
	EventRecord event;
	jboolean rc= ConvertEventRefToEventRecord((EventRef) eHandle, &event);
	copyEvent(env, data, &event);
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_NewCursor(JNIEnv *env, jclass zz,
		jshort hotX, jshort hotY, jshortArray data, jshortArray mask) {
	
	jshort *sa= (*env)->GetShortArrayElements(env, data, 0);
	jshort *sb= (*env)->GetShortArrayElements(env, mask, 0);

	Cursor *c= (Cursor*) NewPtrClear(sizeof(Cursor));
	memcpy(&c->data, sa, sizeof (Bits16));
	memcpy(&c->mask, sb, sizeof (Bits16));
	c->hotSpot.h= hotX;
	c->hotSpot.v= hotY;

	(*env)->ReleaseShortArrayElements(env, data, sa, 0);
	(*env)->ReleaseShortArrayElements(env, mask, sb, 0);
	
	return (jint) c;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_NewPixMap(JNIEnv *env, jclass zz,
			jshort width, jshort height, jshort rowbytes,
			jshort pixelType, jshort pixelSize, jshort cmpSize, jshort cmpCount, jshort pixelFormat) {
			
	PixMapHandle pmh= NewPixMap();
	PixMap *pm= *pmh;
	
	pm->baseAddr= NULL;
	pm->rowBytes= rowbytes | 0x8000;	// mark as PixMap
	pm->bounds.top= 0;
	pm->bounds.left= 0;
	pm->bounds.bottom= height;
	pm->bounds.right= width;
	pm->pmVersion= baseAddr32;	// 32 Bit clean
	pm->packType= 0;
	pm->packSize= 0;
	pm->hRes= 0x00480000;
	pm->vRes= 0x00480000;
	pm->pixelType= pixelType;
	pm->pixelSize= pixelSize;
	pm->cmpCount= cmpCount;
	pm->cmpSize= cmpSize;
	pm->pixelFormat= pixelFormat;
	pm->pmTable= NULL;
	pm->pmExt= NULL;

	return (jint) pmh;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getRowBytes(JNIEnv *env, jclass zz, jint pHandle) {
	BitMap **bmh= (BitMap**) pHandle;
	return (jint) (*bmh)->rowBytes;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_setRowBytes(JNIEnv *env, jclass zz,
				jint pHandle, jshort rowBytes) {
	BitMap **bmh= (BitMap**) pHandle;
	(*bmh)->rowBytes= rowBytes;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getBaseAddr(JNIEnv *env, jclass zz, jint pHandle) {
	BitMap **bmh= (BitMap**) pHandle;
	return (jint) (*bmh)->baseAddr;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getColorTableSize(JNIEnv *env, jclass zz,
		jint pixMapHandle) {
	PixMapHandle srch= (PixMapHandle) pixMapHandle;
	PixMap *src= *srch;
	if (src->pmTable != NULL) {
		ColorTable *ct= *src->pmTable;
		return ct->ctSize + 1;
	}
	return -1;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getColorTable(JNIEnv *env, jclass zz,
			jint pixMapHandle, jshortArray colorspec) {
	PixMapHandle srch= (PixMapHandle) pixMapHandle;
	PixMap *src= *srch;
	int n= (*env)->GetArrayLength(env, colorspec) / 4;
	if (src->pmTable != NULL) {
		ColorTable *ct= *src->pmTable;
		jshort *sa= (*env)->GetShortArrayElements(env, colorspec, 0);
		memcpy(sa, ct->ctTable, n * sizeof(ColorSpec));
		(*env)->ReleaseShortArrayElements(env, colorspec, sa, 0);
	}
}
			
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_setColorTable(JNIEnv *env, jclass zz,
			jint pixMapHandle, jshortArray colorspec) {
				
	PixMapHandle ph= (PixMapHandle) pixMapHandle;
	PixMap *pm= *ph;
	ColorTable *ct;
	jshort *sa;
	int n= (*env)->GetArrayLength(env, colorspec) / 4;
	
	if (pm->pmTable != NULL)
		DisposeHandle((Handle)pm->pmTable);
	pm->pmTable= (ColorTable**) NewHandle(sizeof(ColorTable)+sizeof(ColorSpec)*(n-1));
	ct= *pm->pmTable;
	ct->ctSize= (n-1);
	ct->ctFlags= 0;
	ct->ctSeed= GetCTSeed();
	
	sa= (*env)->GetShortArrayElements(env, colorspec, 0);
	memcpy(ct->ctTable, sa, n * sizeof(ColorSpec));
	(*env)->ReleaseShortArrayElements(env, colorspec, sa, 0);
}
		
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_setBaseAddr(JNIEnv *env, jclass zz,
				jint bitMapHandle, jint ptr) {
	BitMap **bmh= (BitMap**) bitMapHandle;
	(*bmh)->baseAddr= (void*) ptr;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_DisposePixMap(JNIEnv *env, jclass zz, jint pixMapHandle) {
	PixMapHandle ph= (PixMapHandle) pixMapHandle;
	PixMap *pm= *ph;
	
	if (pm->baseAddr != NULL) {
		DisposePtr(pm->baseAddr);
		pm->baseAddr= NULL;
	}
	
	if ((pm->rowBytes & 0x8000) != 0) {	// Pixmap
		DisposePixMap(ph);
	} else {	// Bitmap
		fprintf(stderr, "OS.DisposePixMap: warning: pixmap is bitmap\n");
	}
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_duplicatePixMap(JNIEnv *env, jclass zz, jint srcPixmap) {

	PixMapHandle srch= (PixMapHandle) srcPixmap;
	PixMapHandle dsth= NewPixMap();
		
	PixMap *src= *srch;
	PixMap *dst= *dsth;
	
	*dst= *src;
	dst->pmExt= NULL;
	
	if (src->baseAddr != NULL) {
		Size dataSize= GetPtrSize(src->baseAddr);
		//fprintf(stderr, "duplicatePixMap: data %ld\n", dataSize);
		dst->baseAddr= NewPtr(dataSize);
		memcpy(dst->baseAddr, src->baseAddr, dataSize);
	}
	
	if ((dst->rowBytes & 0x8000) != 0) {	// pixmap
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
			
	return (jint) dsth;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_setPixBounds(JNIEnv *env, jclass zz,
		jint pHandle, jshort top, jshort left, jshort bottom, jshort right) {
	BitMap **bmh= (BitMap**) pHandle;
	BitMap *bm= *bmh;
	bm->bounds.top= top;
	bm->bounds.left= left;
	bm->bounds.bottom= bottom;
	bm->bounds.right= right;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getPixHRes(JNIEnv *env, jclass zz, jint pHandle) {
	PixMapHandle pmh= (PixMapHandle) pHandle;
	return (jint) (*pmh)->hRes;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getPixVRes(JNIEnv *env, jclass zz, jint pHandle) {
	PixMapHandle pmh= (PixMapHandle) pHandle;
	return (jint) (*pmh)->vRes;
}

static jmp_buf jmpbuf;

static void jumper() {
	longjmp(jmpbuf, 1);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_CopyMask(JNIEnv *env, jclass zz, jint srcBits,
		jint maskBits, jint dstBits, jobject srcRect, jobject maskRect, jobject dstRect) {

    Rect ra;
    Rect rb;
    Rect rc;
      
	getRectFields(env, srcRect, &ra);
	getRectFields(env, maskRect, &rb);
	getRectFields(env, dstRect, &rc);
    
    if (setjmp(jmpbuf) != 0) {
    	SysBeep(100);
    	fprintf(stderr, "OS.CopyMask: signal %08x %08x %08x\n", srcBits, maskBits, dstBits);
    	return -1;
    }
    
	if (signal(SIGSEGV, jumper) == BADSIG)
 		fprintf(stderr, "OS.CopyMask: error in signal1\n");
	CopyMask((BitMap*)srcBits, (BitMap*)maskBits, (BitMap*)dstBits, &ra, &rb, &rc);
	if (signal(SIGSEGV, SIG_DFL) == BADSIG)
 		fprintf(stderr, "OS.CopyMask: error in signal2\n");
	return 0;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_NewCIcon(JNIEnv *env, jclass zz,
					jint pixMapHandle, jint maskHandle) {
	CIcon *icon;
	CIconHandle cih;
	PixMap *ph= NULL;
	BitMap *mh= NULL;
	int pixmapRowbytes, pixmapWidth, pixmapHeight, pixmapSize;
	int maskRowbytes, maskHeight, maskSize;
	Size ctsize;
	int size;
	
	if (pixMapHandle == 0)
		return 0;
	ph= *((PixMap**) pixMapHandle);
	if (ph == NULL)
		return 0;
	
	// calculate the CIcon size
	
	pixmapRowbytes= ph->rowBytes & 0x3fff;
	pixmapHeight= ph->bounds.bottom - ph->bounds.top;
	pixmapWidth= ph->bounds.right - ph->bounds.left;
	pixmapSize= pixmapRowbytes * pixmapHeight;

	mh= *((BitMap**) maskHandle);
	if (mh == NULL)
		return 0;

	maskRowbytes= mh->rowBytes & 0x3fff;
	maskHeight= mh->bounds.bottom - mh->bounds.top;
	maskSize= maskRowbytes * maskHeight;
				
	// allocate the CIcon
	cih= (CIconHandle) NewHandleClear(sizeof(CIcon) + maskSize);
	if (cih == NULL)
		return 0;
	icon= *cih;
	if (icon == NULL)
		return 0;
	
	// copy the pixmap
	memcpy(&icon->iconPMap, ph, sizeof(PixMap));
	icon->iconPMap.baseAddr= 0;	// this is documented nowhere!

	// allocate the handle for the pixmap's data
	icon->iconData= NewHandle(pixmapSize);
	if (icon->iconData == 0)
		return 0;
	
	// copy the pixmap's data
	memcpy(*icon->iconData, ph->baseAddr, pixmapSize);
	
	// copy ctable (if any)
	if (ph->pmTable != NULL) {
		ctsize= GetHandleSize((Handle)ph->pmTable);
		if (ctsize > 0) {
			Handle h= NewHandle(ctsize);
			memcpy(*h, *ph->pmTable, ctsize);
			icon->iconPMap.pmTable= (ColorTable**) h;
		}
	}

	memcpy(&icon->iconMask, mh, sizeof(BitMap));
	// copy mask data to end of CIcon
	memcpy(&icon->iconMaskData, icon->iconMask.baseAddr, maskSize);
	icon->iconMask.baseAddr= 0;
		
	return (jint) cih;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getCIconIconData(JNIEnv *env, jclass zz, jint cIconHandle) {
	CIcon *icon= *((CIconHandle) cIconHandle);
	return (jint) icon->iconData;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getCIconColorTable(JNIEnv *env, jclass zz, jint cIconHandle) {
	CIcon *icon= *((CIconHandle) cIconHandle);
	return (jint) icon->iconPMap.pmTable;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getgdPMap(JNIEnv *env, jclass zz, jint gdHandle) {
	GDHandle h= (GDHandle) gdHandle;
	return (jint) (*h)->gdPMap;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_NavDialogGetReply(JNIEnv *env, jclass zz,
				jint dialogHandle, jintArray replyHandle) {
	NavReplyRecord *reply= (NavReplyRecord*) malloc(sizeof(NavReplyRecord));
	jint *sa= (*env)->GetIntArrayElements(env, replyHandle, 0);
	sa[0]= (jint) reply;
	(*env)->ReleaseIntArrayElements(env, replyHandle, sa, 0);
	return (jint) RC(NavDialogGetReply((NavDialogRef) dialogHandle, reply));
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_NavDialogDisposeReply(JNIEnv *env, jclass zz,
				jint replyHandle) {
	free((NavReplyRecord*) replyHandle);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_NavReplyRecordGetSelection(JNIEnv *env, jclass zz,
				jint replyHandle) {
	NavReplyRecord *reply= (NavReplyRecord*) replyHandle;
	return (jint) &reply->selection;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_AEGetNthPtr(JNIEnv *env, jclass zz,
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


JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_DerefHandle(JNIEnv *env, jclass zz, jint handle) {
	Handle h= (Handle) handle;
	return (jint) *h;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getHandleData__I_3C(JNIEnv *env, jclass zz,
				jint hndl, jcharArray data) {
	Handle handle= (Handle)hndl;
	jchar *sa= (*env)->GetCharArrayElements(env, data, 0);
	int length= (*env)->GetArrayLength(env, data);
	memcpy(sa, *handle, length*sizeof(jchar));
	(*env)->ReleaseCharArrayElements(env, data, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_getHandleData__I_3I(JNIEnv *env, jclass zz,
				jint hndl, jintArray data) {
	Handle handle= (Handle)hndl;
	jint *sa= (*env)->GetIntArrayElements(env, data, 0);
	int length= (*env)->GetArrayLength(env, data);
	memcpy(sa, *handle, length*sizeof(jint));
	(*env)->ReleaseIntArrayElements(env, data, sa, 0);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_TXNClick(JNIEnv *env, jclass zz,	
					jint txHandle, jintArray eventData) {
	EventRecord event;
	copyEventData(env, &event, eventData);
	TXNClick((TXNObject)txHandle, &event);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_TXNGetRectBounds(JNIEnv *env, jclass zz,
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

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_setTXNMargins(JNIEnv *env, jclass zz,
				jint txHandle, jshort margin) {
    TXNControlTag controlTag[1];
    TXNControlData controlData[1];
    TXNMargins m;
    m.topMargin= margin;
    m.leftMargin= margin;
    m.bottomMargin= margin;
    m.rightMargin= margin;    
    controlTag[0]= kTXNMarginsTag;
	controlData[0].marginsPtr= &m; 
	TXNSetTXNObjectControls((TXNObject)txHandle, false, 1, controlTag, controlData);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_NewGWorldFromPtr(JNIEnv *env, jclass zz,
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

//////////////////////////////////////////
//  added by AW while merging HIView stuff

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_HIViewSetBoundsOrigin(JNIEnv *env, jclass zz,
			jint inView, jfloat x, jfloat y) {
	return RC(HIViewSetBoundsOrigin((HIViewRef) inView, x, y));
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_HIViewGetPartHit(JNIEnv *env, jclass zz,
			jint inView, jfloatArray where, jshortArray outPart) {
	jfloat *sa= (*env)->GetFloatArrayElements(env, where, 0);
	jshort *sb= (*env)->GetShortArrayElements(env, outPart, 0);
	jint status= RC(HIViewGetPartHit((HIViewRef) inView, (const HIPoint*) sa, (HIViewPartCode*) sb));
	(*env)->ReleaseFloatArrayElements(env, where, sa, 0);
	(*env)->ReleaseShortArrayElements(env, outPart, sb, 0);
	return status;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_carbon_OS2_HIViewGetSubviewHit(JNIEnv *env, jclass zz,
			jint inView, jfloatArray where, jboolean deep, jintArray outView) {
	jfloat *sa= (*env)->GetFloatArrayElements(env, where, 0);
	jint *sb= (*env)->GetIntArrayElements(env, outView, 0);
	jint status= RC(HIViewGetSubviewHit((HIViewRef) inView, (const HIPoint*) sa, deep, (HIViewRef*) sb));
	(*env)->ReleaseFloatArrayElements(env, where, sa, 0);
	(*env)->ReleaseIntArrayElements(env, outView, sb, 0);
	return status;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_CGContextBeginPath(JNIEnv *env, jclass zz,
			jint inContext) {
	CGContextBeginPath((CGContextRef)inContext);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_CGContextAddArc(JNIEnv *env, jclass zz,
			jint inContext, jfloat x, jfloat y, jfloat radius, jfloat startAngle, jfloat endAngle, jint clockwise) {
	CGContextAddArc((CGContextRef)inContext, x, y, radius, startAngle, endAngle, clockwise);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_CGContextClosePath(JNIEnv *env, jclass zz,
			jint inContext) {
	CGContextClosePath((CGContextRef)inContext);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_CGContextStrokePath(JNIEnv *env, jclass zz,
			jint inContext) {
	CGContextStrokePath((CGContextRef)inContext);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_carbon_OS2_CGContextFillPath(JNIEnv *env, jclass zz,
			jint inContext) {
	CGContextFillPath((CGContextRef)inContext);
}

