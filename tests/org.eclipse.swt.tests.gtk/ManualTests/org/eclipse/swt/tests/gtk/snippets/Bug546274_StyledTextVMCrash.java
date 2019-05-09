/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug546274_StyledTextVMCrash {
	private static class MyStyledText extends StyledText {
		int 	cairoCacheSize = 16;	// see MAX_FREED_POOL_SIZE in cairo
		Image 	tempImages[] = new Image[cairoCacheSize];
		GC 		tempGCs[] = new GC[cairoCacheSize];

		public MyStyledText(Composite parent, int style) {
			super(parent, style);
			allocTempGCs();
		}

		void allocTempGCs() {
			for (int i = 0; i < cairoCacheSize; i++) {
				tempImages[i] = new Image(this.getDisplay(), 1, 1);
				tempGCs[i] = new GC(tempImages[i]);
			}
		}

		@Override
		public void scroll(int destX, int destY, int x, int y, int width, int height, boolean all) {
			// Evaluate with debugger: (cachedCairo.handle == this.cachedCairo);
			// This already shows that 'this.cachedCairo' can't be used.
			Image tempImage = new Image(this.getDisplay(), 1, 1);
			GC cachedCairo = new GC(tempImage);

			// Evict 'cachedCairo' from Cairo's cache 'context_pool' by
			// 1) Populating cache with free objects
			// 2) Releasing 'cachedCairo' and it will be truly deleted
			{
				if (tempGCs != null) {
					for (int i = 0; i < tempGCs.length; i++) {
						tempGCs[i].dispose();
						tempImages[i].dispose();
					}
				}

				cachedCairo.dispose();
				tempImage.dispose();
			}

			// Make sure the same malloc() block can't be used again
			for (int i = 1; i < 1024; i++) {
				// Intentional leak
				C.malloc(i);
			}

			super.scroll(destX, destY, x, y, width, height, all);

			// Drain Cairo's cache 'context_pool' to force the next cairo to use malloc()
			allocTempGCs();
		}
	}

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(300, 200);

		String hintText = "Try to horz scroll this StyledText\n" +
						"\n" +
						"Horz scrollbar is forced by this long line of text xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

		final StyledText text = new MyStyledText(shell, SWT.BORDER | SWT.H_SCROLL);
		text.setText(hintText);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
