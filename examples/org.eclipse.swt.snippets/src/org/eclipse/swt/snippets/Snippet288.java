/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Create a ToolBar containing animated GIFs
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.io.File;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet288 {
	
	static Display display;
	static Shell shell;
	static GC shellGC;
	static Color shellBackground;
	static ImageLoader[] loader;
	static ImageData[][] imageDataArray;
	static Thread animateThread[];
	static Image[][] image;
	private static ToolItem item[];
	static final boolean useGIFBackground = false;

	public static void main (String [] args) {
		display = new Display();
		Shell shell = new Shell (display);
		shellBackground = shell.getBackground();
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		dialog.setText("Select Multiple Animated GIFs");
		dialog.setFilterExtensions(new String[] {"*.gif"});
		String filename = dialog.open();
		String filenames[] = dialog.getFileNames();
		int numToolBarItems = filenames.length;
		if (numToolBarItems > 0) {
			try {
				loadAllImages(new File(filename).getParent(), filenames);
			} catch (SWTException e) {
				System.err.println("There was an error loading an image.");
				e.printStackTrace();
			}
			ToolBar toolBar = new ToolBar (shell, SWT.FLAT | SWT.BORDER | SWT.WRAP);
			item = new ToolItem[numToolBarItems];
			for (int i = 0; i < numToolBarItems; i++) {
				item[i] = new ToolItem (toolBar, SWT.PUSH);
				item[i].setImage(image[i][0]);
			}
			toolBar.pack ();
			shell.open ();
			
			startAnimationThreads();
			
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			
			for (int i = 0; i < numToolBarItems; i++) {
				for (int j = 0; j < image[i].length; j++) {
					image[i][j].dispose();
				}
			}
		}
		display.dispose ();
	}

	private static void loadAllImages(String directory, String[] filenames) throws SWTException {
		int numItems = filenames.length;
		loader = new ImageLoader[numItems];
		imageDataArray = new ImageData[numItems][];
		image = new Image[numItems][];
		for (int i = 0; i < numItems; i++) {
			loader[i] = new ImageLoader();
			int fullWidth = loader[i].logicalScreenWidth;
			int fullHeight = loader[i].logicalScreenHeight;
			imageDataArray[i] = loader[i].load(directory + File.separator + filenames[i]);
			int numFramesOfAnimation = imageDataArray[i].length;
			image[i] = new Image[numFramesOfAnimation];
			for (int j = 0; j < numFramesOfAnimation; j++) {
				if (j == 0) {
					//for the first frame of animation, just draw the first frame
					image[i][j] = new Image(display, imageDataArray[i][j]);
					fullWidth = imageDataArray[i][j].width;
					fullHeight = imageDataArray[i][j].height;
				}
				else {
					//after the first frame of animation, draw the background or previous frame first, then the new image data 
					image[i][j] = new Image(display, fullWidth, fullHeight);
					GC gc = new GC(image[i][j]);
					gc.setBackground(shellBackground);
					gc.fillRectangle(0, 0, fullWidth, fullHeight);
					ImageData imageData = imageDataArray[i][j];
					switch (imageData.disposalMethod) {
					case SWT.DM_FILL_BACKGROUND:
						/* Fill with the background color before drawing. */
						Color bgColor = null;
						if (useGIFBackground && loader[i].backgroundPixel != -1) {
							bgColor = new Color(display, imageData.palette.getRGB(loader[i].backgroundPixel));
						}
						gc.setBackground(bgColor != null ? bgColor : shellBackground);
						gc.fillRectangle(imageData.x, imageData.y, imageData.width, imageData.height);
						if (bgColor != null) bgColor.dispose();
						break;
					default:
						/* Restore the previous image before drawing. */
						gc.drawImage(
							image[i][j-1],
							0,
							0,
							fullWidth,
							fullHeight,
							0,
							0,
							fullWidth,
							fullHeight);
						break;
					}
					Image newFrame = new Image(display, imageData);
					gc.drawImage(newFrame,
							0,
							0,
							imageData.width,
							imageData.height,
							imageData.x,
							imageData.y,
							imageData.width,
							imageData.height);
					newFrame.dispose();
					gc.dispose();
				}
			}
		}
	}

	private static void startAnimationThreads() {
		animateThread = new Thread[image.length];
		for (int ii = 0; ii < image.length; ii++) {
			final int i = ii;
			animateThread[i] = new Thread("Animation "+i) {
				int imageDataIndex = 0;
				public void run() {
					try {
						int repeatCount = loader[i].repeatCount;
						while (loader[i].repeatCount == 0 || repeatCount > 0) {
							imageDataIndex = (imageDataIndex + 1) % imageDataArray[i].length;
							if (!display.isDisposed()) {
								display.asyncExec(new Runnable() {
									public void run() {
										if (!item[i].isDisposed())
											item[i].setImage(image[i][imageDataIndex]);
									}
								});
							}
							
							/* Sleep for the specified delay time (adding commonly-used slow-down fudge factors). */
							try {
								int ms = imageDataArray[i][imageDataIndex].delayTime * 10;
								if (ms < 20) ms += 30;
								if (ms < 30) ms += 10;
								Thread.sleep(ms);
							} catch (InterruptedException e) {
							}

							/* If we have just drawn the last image, decrement the repeat count and start again. */
							if (imageDataIndex == imageDataArray[i].length - 1) repeatCount--;
						}
					} catch (SWTException ex) {
						System.out.println("There was an error animating the GIF");
						ex.printStackTrace();
					}
				}
			};
			animateThread[i].setDaemon(true);
			animateThread[i].start();
		}
	}
}
