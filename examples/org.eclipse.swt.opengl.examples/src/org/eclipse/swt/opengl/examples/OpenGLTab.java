/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
package org.eclipse.swt.opengl.examples;


import org.eclipse.opengl.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.opengl.*;

/**
 * <code>OpenGLTab</code> is the abstract superclass of every page
 * in the example's tab folder.  Each page in the tab folder
 * displays a different example.
 *
 * An OpenGLTab itself is not a control but instead provides a hierarchy
 * with which to share code that is common to every page in the folder.
 */
abstract class OpenGLTab {
	private GLCanvas glCanvas;
	private Composite tabFolderPage;
	private boolean stencilSupport;
	private final static int DEFAULT_SLEEP_LENGTH = 100;

	/**
	 * Creates this tab's controls.  Subclasses must override.
	 *
	 * @param composite the parent composite
	 */
	abstract void createControls(Composite composite);

	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder the parent tab folder
	 * @return the new page
	 */
	Composite createTabFolderPage(TabFolder tabFolder) {
		tabFolderPage = new Composite(tabFolder, SWT.NONE);
		tabFolderPage.setLayout(new GridLayout(2, false));

		GridData gridData = new GridData();
		gridData.heightHint = 400;
		gridData.widthHint = 400;
		gridData.verticalAlignment = GridData.BEGINNING;
		GLData data = new GLData();
		data.doubleBuffer = true;
		data.stencilSize = 8;
		glCanvas = new GLCanvas(tabFolderPage, SWT.NO_BACKGROUND, data);
		glCanvas.setLayout(new GridLayout());
		glCanvas.setLayoutData(gridData);
		glCanvas.setSize(400, 400);		// needed for windows

		gridData = new GridData();
		gridData.verticalAlignment = GridData.BEGINNING;
		Composite controlComposite = new Composite(tabFolderPage, SWT.NONE);
		controlComposite.setLayout(new GridLayout());
		controlComposite.setLayoutData(gridData);

		// create the OpenGL Screen and controls
		setCurrent();
		setupViewingArea();

		// determine if native stencil support is available
		int[] param = new int[1];
		GL.glGetIntegerv(GL.GL_STENCIL_BITS, param);
		stencilSupport = param[0] != 0;

		init();

		if (!isStencilSupportNeeded() || hasStencilSupport()) {
			createControls(controlComposite);
		} else {
			Label label = new Label(controlComposite, SWT.NONE);
			label.setText("This tab requires native stencil support.");
		}

		return tabFolderPage;
	}

	/**
	 * Disposes all resources allocated by this tab.
	 */
	void dispose() {
	}

	/**
	 * Returns the glCanvas for this tab.
	 * 
	 * @return Canvas
	 */
	GLCanvas getGlCanvas() {
		return glCanvas;
	}

	/**
	 * Returns the length of time in milliseconds that the example
	 * should sleep between animation redraws.  As this length
	 * increases, user responsiveness increases and the frequency of
	 * animation redraws decreases.  Subclasses with moving animations
	 * may wish to override this default implementation to return a
	 * smaller value if their animations do not occur frequently enough. 
	 *
	 * @return the length of time in milliseconds to sleep between redraws
	 */
	int getSleepLength() {
		return DEFAULT_SLEEP_LENGTH;	
	}

	/**
	 * Returns the text for this tab.  Subclasses must override.
	 *
	 * @return the text for the tab item
	 */
	abstract String getTabText();

	/**
	 * Returns whether this machine has native stencils support.
	 * 
	 * @return boolean
	 */
	boolean hasStencilSupport() {
		return stencilSupport;
	}

	/**
	 * Initialize OpenGL resources for this tab.  Subclasses must override.
	 */
	abstract void init();

	/**
	 * Loads a texture.
	 * 
	 * @param context
	 * @param fileName
	 * @param index
	 * @param texture[]
	 */
	static void loadTexture(GLCanvas context, String fileName, int index, int[] texture) {
		GL.glBindTexture(GL.GL_TEXTURE_2D, texture[index]);
		ImageData source =
			new ImageData(OpenGLTab.class.getResourceAsStream(fileName));
		Image image = new Image(Display.getCurrent(), source);
		Image newImage = new Image(Display.getCurrent(), 256, 256);
		GC gc = new GC(newImage);
		gc.drawImage(image, 0, 0, source.width, source.height, 0, 0, 256, 256);
		source = newImage.getImageData();
		gc.dispose();
		source = ImageDataUtil.convertImageData(source);
		newImage.dispose();
		image.dispose();
		GL.glTexImage2D(
			GL.GL_TEXTURE_2D, 0, 3, 
			source.width, source.height, 0,
			GL.GL_RGB, GL.GL_UNSIGNED_BYTE, source.data);
		GL.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		GL.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
	}

	/**
	 * Renders this tab.
	 */
	void render() {
		setCurrent();
		if (!isStencilSupportNeeded() || hasStencilSupport()) {
			renderScene();
		} else {
			GL.glClear(GL.GL_COLOR_BUFFER_BIT);
		}
	}

	/**
	 * Renders the scene for this tab.  Subclasses must override.
	 */
	abstract void renderScene();

	/**
	 * Returns whether this tab requires stencil support in order to display
	 * properly. Subclasses may wish to override this method.
	 * 
	 * @return boolean
	 */
	boolean isStencilSupportNeeded() {
		return false;
	}

	/**
	 * Sets this rendering context to be current.
	 */
	void setCurrent() {
		glCanvas.setCurrent();
	}
	
	/**
	 * Sets up the viewing area for the OpenGL screen.  The default
	 * behavior is to use a perspective view, but there also exist frustrum
	 * and ortho views.  Subclasses may wish to override this method.
	 */
	void setupViewingArea() {
		Rectangle rect = glCanvas.getClientArea();
		int width = rect.width;
		int height = rect.height;
		height = Math.max(height, 1);
		GL.glViewport(0, 0, width, height);
		GL.glMatrixMode(GL.GL_PROJECTION);	// select the projection matrix
		GL.glLoadIdentity();				// reset the projection matrix
		float fAspect = (float) width / (float) height;
		GLU.gluPerspective(45.0f, fAspect, 0.5f, 400.0f);
		GL.glMatrixMode(GL.GL_MODELVIEW);	// select the modelview matrix
		GL.glLoadIdentity();
	}

	/**
	 * Swaps the buffers.
	 */
	void swap() {
		glCanvas.swapBuffers();
	}
}
