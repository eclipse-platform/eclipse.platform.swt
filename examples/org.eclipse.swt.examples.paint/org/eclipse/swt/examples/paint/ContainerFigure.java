package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * Container for Figure objects with stacking preview mechanism.
 */
public class ContainerFigure extends Figure {
	private static final int INITIAL_ARRAY_SIZE = 16;
	
	Figure[]   objectStack = null;
	int      nextIndex = 0;

	/**
	 * Constructs an empty Container
	 */
	public ContainerFigure() {
	}
	/**
	 * Adds an object to the container for later drawing.
	 * 
	 * @param object the object to add to the drawing list
	 */
	public void add(Figure object) {
		if (objectStack == null) {
			objectStack = new Figure[INITIAL_ARRAY_SIZE];
		} else if (objectStack.length <= nextIndex) {
			Figure[] newObjectStack = new Figure[objectStack.length * 2];
			System.arraycopy(objectStack, 0, newObjectStack, 0, objectStack.length);
			objectStack = newObjectStack;
		}
		objectStack[nextIndex] = object;
		++nextIndex;
	}
	/**
	 * Adds an object to the container and draws its preview then updates the supplied preview state.
	 * 
	 * @param object the object to add to the drawing list
	 * @param gc the GC to draw on
	 * @param offset the offset to add to virtual coordinates to get display coordinates
	 * @param rememberedState the state returned by a previous drawPreview() or addAndPreview()
	 *        using this Container, may be null if there was no such previous call
	 * @return object state that must be passed to erasePreview() later to erase this object
	 */
	public Object addAndPreview(Figure object, GC gc, Point offset, Object rememberedState) {
		Object[] stateStack = (Object[]) rememberedState;
		if (stateStack == null) {
			stateStack = new Object[INITIAL_ARRAY_SIZE];
		} else if (stateStack.length <= nextIndex) {
			Object[] newStateStack = new Object[stateStack.length * 2];
			System.arraycopy(stateStack, 0, newStateStack, 0, stateStack.length);
			stateStack = newStateStack;
		}
		add(object);
		stateStack[nextIndex - 1] = object.drawPreview(gc, offset);
		return stateStack;
	}
	/**
	 * Clears the container.
	 * <p>
	 * Note that erasePreview() cannot be called after this point to erase any previous
	 * drawPreview()'s.
	 * </p>
	 */
	public void clear() {
		while (--nextIndex > 0) objectStack[nextIndex] = null;
		nextIndex = 0;
	}
	public void draw(GC gc, Point offset) {
		for (int i = 0; i < nextIndex; ++i) objectStack[i].draw(gc, offset);
	}
	public Object drawPreview(GC gc, Point offset) {
		if (nextIndex == 0) return null;
		
		Object[] stateStack = new Object[nextIndex];
		for (int i = 0; i < nextIndex; ++i) stateStack[i] = objectStack[i].drawPreview(gc, offset);
		return stateStack;
	}
	public void erasePreview(GC gc, Point offset, Object rememberedState) {
		if (rememberedState == null) return;
		
		final Object[] stateStack = (Object[]) rememberedState;
		int i = nextIndex;
		while (--i >= 0) {
			objectStack[i].erasePreview(gc, offset, stateStack[i]);
			stateStack[i] = null;
		}
	}	
}
