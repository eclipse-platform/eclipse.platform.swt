/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
#include <jni.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <utime.h>
#include "core.h"


/*
 * Get a null-terminated byte array from a java byte array.
 * The returned bytearray needs to be freed whe not used
 * anymore. Use free(result) to do that.
 */
jbyte* getByteArray(JNIEnv *env, jbyteArray target) {
	jsize n;
	jbyte *temp, *result;
	
	temp = (*env)->GetByteArrayElements(env, target, 0);
	n = (*env)->GetArrayLength(env, target);
	result = malloc((n+1) * sizeof(jbyte));
	memcpy(result, temp, n);
	result[n] = '\0';
	(*env)->ReleaseByteArrayElements(env, target, temp, 0);
	return result;
}

/*
 * Class:     org_eclipse_core_internal_localstore_CoreFileSystemLibrary
 * Method:    internalGetStat
 * Signature: ([B)J
 */
JNIEXPORT jlong JNICALL Java_org_eclipse_core_internal_localstore_CoreFileSystemLibrary_internalGetStat
   (JNIEnv *env, jclass clazz, jbyteArray target) {

	struct stat info;
	jlong result;
	jint code;
	jbyte *name;

	/* get stat */
	name = getByteArray(env, target);
	code = stat(name, &info);
	free(name);

	/* test if an error occurred */
	if (code == -1)
	  return 0;

	/* filter interesting bits */
	/* lastModified */
	result = ((jlong) info.st_mtime) * 1000; /* lower bits */
	/* valid stat */
	result |= STAT_VALID;
	/* is folder? */
	if ((info.st_mode & S_IFDIR) == S_IFDIR)
		result |= STAT_FOLDER;
	/* is read-only? */
	if ((info.st_mode & S_IWRITE) != S_IWRITE)
		result |= STAT_READ_ONLY;

	return result;
}

/*
 * Class:     org_eclipse_core_internal_localstore_CoreFileSystemLibrary
 * Method:    internalSetReadOnly
 * Signature: ([BZ)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_core_internal_localstore_CoreFileSystemLibrary_internalSetReadOnly
   (JNIEnv *env, jclass clazz, jbyteArray target, jboolean readOnly) {

	int mask;
	struct stat info;
	jbyte *name;
   	jint code;

	name = getByteArray(env, target);
	code = stat(name, &info);
	mask = S_IRUSR |
	       S_IWUSR |
	       S_IXUSR |
           S_IRGRP |
           S_IWGRP |
           S_IXGRP |
           S_IROTH |
           S_IWOTH |
           S_IXOTH;
 
        mask &= info.st_mode;

	if (readOnly)
	  mask &= ~(S_IWUSR | S_IWGRP | S_IWOTH);
	else
	  mask |= (S_IRUSR | S_IWUSR);

	code = chmod(name, mask);

	free(name);
	return code != -1;
}

/*
 * Class:     org_eclipse_core_internal_localstore_CoreFileSystemLibrary
 * Method:    internalCopyAttributes
 * Signature: ([B[BZ)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_core_internal_localstore_CoreFileSystemLibrary_internalCopyAttributes
(JNIEnv *env, jclass clazz, jbyteArray source, jbyteArray destination, jboolean copyLastModified) {

	struct stat info;
	jbyte *sourceFile, *destinationFile;
	jint code;

	sourceFile = getByteArray(env, source);
	destinationFile = getByteArray(env, destination);

	code = stat(sourceFile, &info);
	if (code == 0) {
		code = chmod(destinationFile, info.st_mode);
		if (code == 0 && copyLastModified) {
			struct utimbuf utbuf;
		
			utbuf.actime= info.st_atime;
			utbuf.modtime= info.st_mtime;

			code = utime(destinationFile, &utbuf);
		}
	}

	free(sourceFile);
	free(destinationFile);
	return code != -1;
}

/*
 * Class:     org_eclipse_ant_core_EclipseProject
 * Method:    internalCopyAttributes
 * Signature: ([B[BZ)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_ant_core_EclipseFileUtils_internalCopyAttributes
   (JNIEnv *env, jclass clazz, jbyteArray source, jbyteArray destination, jboolean copyLastModified) {

  /* use the same implementation for both methods */
  return Java_org_eclipse_core_internal_localstore_CoreFileSystemLibrary_internalCopyAttributes
    (env, clazz, source, destination, copyLastModified);
}
