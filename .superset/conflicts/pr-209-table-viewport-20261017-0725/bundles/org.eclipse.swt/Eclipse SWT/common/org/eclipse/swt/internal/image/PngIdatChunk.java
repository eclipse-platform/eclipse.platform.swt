/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.image;


import org.eclipse.swt.*;

class PngIdatChunk extends PngChunk {

PngIdatChunk(byte[] reference) {
	super(reference);
}

@Override
int getChunkType() {
	return CHUNK_IDAT;
}

/**
 * Answer whether the chunk is a valid IDAT chunk.
 */
@Override
void validate(PngFileReadState readState, PngIhdrChunk headerChunk) {
	if (!readState.readIHDR
		|| (headerChunk.getMustHavePalette() && !readState.readPLTE)
		|| readState.readIEND)
	{
		SWT.error(SWT.ERROR_INVALID_IMAGE);
	} else {
		readState.readIDAT = true;
	}

	super.validate(readState, headerChunk);
}

}
