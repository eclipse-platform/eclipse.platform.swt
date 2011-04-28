/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * 
 * All rights reserved.  Redistribution and use in source and binary forms, with
 * or without modification, are permitted provided that the following conditions
 * are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the Eclipse Foundation, Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


package org.eclipse.swt.snippets;

/*
 * draw an image scaled to half size and double size
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet355 {
	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display, SWT.SHELL_TRIM);
		final Image image = display.getSystemImage (SWT.ICON_QUESTION);
		shell.addListener (SWT.Paint, new Listener () {
			public void handleEvent (Event e) {
				Rectangle rect = image.getBounds ();
				int width = rect.width;
				int height = rect.height;
				GC gc = e.gc;
				int x = 10, y = 10;
				gc.drawImage (image, 0, 0, width, height, x, y, width, height);
				gc.drawImage (image, 0, 0, width, height, x + width, y, (int)Math.round(width * 0.5), (int)Math.round(height * 0.5));
				gc.drawImage (image, 0, 0, width, height, x+width+(int)Math.round(width * 0.5), y, width * 2, height * 2);
			}
		});
		shell.setSize (600, 400);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ())
				display.sleep ();
		}
		display.dispose ();
	}
}

