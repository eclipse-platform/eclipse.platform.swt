/* ***** BEGIN LICENSE BLOCK *****
* Version: MPL 1.1
*
* The contents of this file are subject to the Mozilla Public License Version
* 1.1 (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
*
* Software distributed under the License is distributed on an "AS IS" basis,
* WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
* for the specific language governing rights and limitations under the
* License.
*
* The Original Code is Mozilla Communicator client code, released March 31, 1998.
*
* The Initial Developer of the Original Code is
* Netscape Communications Corporation.
* Portions created by Netscape are Copyright (C) 1998-1999
* Netscape Communications Corporation.  All Rights Reserved.
*
* Contributor(s):
*
* IBM
* -  Binding to permit interfacing between Mozilla and SWT
* -  Copyright (C) 2003, 2004 IBM Corp.  All Rights Reserved.
*
* ***** END LICENSE BLOCK ***** */

#include "swt.h"
#include "xpcom_structs.h"
#include "xpcom_stats.h"
#include <stdio.h>
#include <elf.h>

extern "C" {

#define XPCOM_NATIVE(func) Java_org_eclipse_swt_internal_mozilla_XPCOM_##func

#ifndef NO_strlen_1PRUnichar
JNIEXPORT jint JNICALL XPCOM_NATIVE(strlen_1PRUnichar)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	XPCOM_NATIVE_ENTER(env, that, strlen_1PRUnichar_FUNC);
	{
	const PRUnichar* lparg0 = NULL;
	if (arg0) lparg0 = (const PRUnichar *)arg0;
	PRUint32 len = 0;
	if (lparg0 != NULL)	while (*lparg0++ != 0) len++;
	rc = (jint)len;
	}
	XPCOM_NATIVE_EXIT(env, that, strlen_1PRUnichar_FUNC);
	return rc;
}
#endif

int isDependent(char *filename, char *libname) {
	Elf32_Ehdr hdr;
	char *strdata = 0;
	FILE *fd;
	int i, result = 0;
	fd = fopen(filename, "r");
    if (fd == NULL) return 0;
    if (fread(&hdr, sizeof(Elf32_Ehdr), 1, fd) != 1) goto clean;
    
	/* Jump to the section header table. */
	if (fseek(fd, hdr.e_shoff, SEEK_SET) != 0) goto clean;

	/* Find and load the dynamic symbol strings. */
	for (i=0; i<hdr.e_shnum; i++) {
		Elf32_Shdr shdr;
		if (fseek(fd, hdr.e_shoff + (i * hdr.e_shentsize), SEEK_SET) != 0) goto clean;
		if (fread(&shdr, sizeof(Elf32_Shdr), 1, fd) != 1) goto clean;
		if (shdr.sh_type == SHT_DYNSYM) {
			Elf32_Shdr symstr;
			if (fseek(fd, hdr.e_shoff + (shdr.sh_link * hdr.e_shentsize), SEEK_SET) != 0) goto clean;
			if (fread(&symstr, sizeof(Elf32_Shdr), 1, fd) != 1) goto clean;
			if (fseek(fd, symstr.sh_offset, SEEK_SET) != 0) goto clean;
			strdata = (char *)malloc(symstr.sh_size);
			if (fread(strdata, symstr.sh_size, 1, fd) != 1) goto clean;
			break;
		}
	}
	if (strdata == NULL) goto clean;
    
	/* Now find the .dynamic section. */
	for(i=0; i<hdr.e_shnum; i++) {
		Elf32_Shdr shdr;
		if (fseek(fd, hdr.e_shoff+(i*hdr.e_shentsize), SEEK_SET) != 0) goto clean;
		if (fread(&shdr, sizeof( Elf32_Shdr ), 1, fd) != 1) goto clean;
		if(shdr.sh_type == SHT_DYNAMIC) {
			int j;
			/* Load its data and print all DT_NEEDED strings. */
			if (fseek(fd, shdr.sh_offset, SEEK_SET) != 0) goto clean;
			for (j=0; j<shdr.sh_size/sizeof(Elf32_Dyn); j++) {
				Elf32_Dyn cur;
				if (fread(&cur, sizeof(Elf32_Dyn), 1, fd) != 1) goto clean;
				if(cur.d_tag == DT_NEEDED) {
					char *name = strdata + cur.d_un.d_val;
					if (strcmp(name, libname) == 0) {
						result = 1;
						goto clean;			
					}
				}
			}
		}
	}
clean:
	if (fd != NULL) fclose(fd);
	if (strdata != NULL) free(strdata);
    return result;
}

#ifndef NO_isDependent
JNIEXPORT jboolean JNICALL XPCOM_NATIVE(isDependent)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jboolean rc = 0;
	XPCOM_NATIVE_ENTER(env, that, isDependent_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = env->GetByteArrayElements(arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)isDependent((char *)lparg0, (char *)lparg1);
fail:
	if (arg1 && lparg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	XPCOM_NATIVE_EXIT(env, that, isDependent_FUNC);
	return rc;
}
#endif
}
