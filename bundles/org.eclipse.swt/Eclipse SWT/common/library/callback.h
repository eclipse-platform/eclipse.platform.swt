/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * Callback implementation.
 */
#ifndef INC_callback_H
#define INC_callback_H

int callback(int index, ...);

#ifdef WIN32
#define PLATFORM "win32"
#define RETURN_TYPE LRESULT CALLBACK
#define RETURN_CAST (LRESULT)
#endif

#ifdef MOTIF
#define PLATFORM "motif"
#endif

#ifdef GTK
#define PLATFORM "gtk"
#endif

#ifdef PHOTON
#define PLATFORM "photon"
#endif

#ifndef PLATFORM
#define PLATFORM "unknown"
#endif

#ifndef RETURN_TYPE
#define RETURN_TYPE int
#endif

#ifndef RETURN_CAST
#define RETURN_CAST
#endif

#define MAX_CALLBACKS 128
#define MAX_ARGS 12

typedef struct SWT_CALLBACKINFO {
    jobject callin;
    JNIEnv *env;
    jmethodID methodID;
} SWT_CALLBACKINFO;

#endif /* ifndef INC_callback_H */

