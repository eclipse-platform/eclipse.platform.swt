package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;

/**
 * Superinterface for all drawing objects.
 * All drawing objects know how to render themselved to the screen and can draw a
 * temporary version of themselves for previewing the general appearance of the
 * object onscreen before it gets committed.
 */
public abstract class Meta {
	/**
	 * Draws an object to the specified GC
	 * <p>
	 * The GC will be set up as follows (and must be returned to this state before returning)<br>
	 * &nbsp; setXORMode(false)
	 * </p>
	 * 
	 * @param gc the GC to draw on
	 * @param offset the offset to add to virtual coordinates to get display coordinates
	 */
	public abstract void draw(GC gc, Point offset);

	/**
	 * Draws a preview copy of the object to the specified GC
	 * <p>
	 * The GC will be set up as follows (and must be returned to this state before returning)<br>
	 * &nbsp; setXORMode(true)
	 * </p>
	 * 
	 * @param gc the GC to draw on
	 * @param offset the offset to add to virtual coordinates to get display coordinates
	 * @return object state that must be passed to erasePreview() later to erase this object
	 */
	public abstract Object drawPreview(GC gc, Point offset);

	/**
	 * Erases a preview copy of the object to the specified GC
	 * <p>
	 * Note that erasures are guaranteed to occur in the reverse order to the original drawing
	 * order and that the GC's contents will be as they were when the drawPreview() that supplied
	 * <code>rememberedState</code>returned.
	 * </p><p>
	 * The GC will be set up as follows (and must be returned to this state before returning)<br>
	 * &nbsp; setXORMode(true)
	 * </p>
	 * 
	 * @param gc the GC to draw on
	 * @param offset the offset to add to virtual coordinates to get display coordinates
	 * @param rememberedState the state returned by a previous drawPreview() using this instance
	 */
	public abstract void erasePreview(GC gc, Point offset, Object rememberedState);

	/**
	 * Container for Meta objects with stacking preview mechanism.
	 */
	public static class Container extends Meta {
		private static final int INITIAL_ARRAY_SIZE = 16;
		
		Meta[]   objectStack = null;
		int      nextIndex = 0;

		/**
		 * Constructs a <code>Meta.Container</code>
		 */
		public Container() {
		}
		/**
		 * Adds an object to the container for later drawing.
		 * 
		 * @param object the object to add to the drawing list
		 */
		public void add(Meta object) {
			if (objectStack == null) {
				objectStack = new Meta[INITIAL_ARRAY_SIZE];
			} else if (objectStack.length <= nextIndex) {
				Meta[] newObjectStack = new Meta[objectStack.length * 2];
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
		public Object addAndPreview(Meta object, GC gc, Point offset, Object rememberedState) {
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
			while (--i >= 0) objectStack[i].erasePreview(gc, offset, stateStack[i]);
		}	
	}

	/**
	 * Superclass for all Meta objects that do not need to store state about preview copies
	 * and have self-complementary preview drawing and erasing operations
	 * e.g. Those that use XOR drawing operations
	 */
	public static abstract class StatelessXORHelper extends Meta {
		public Object drawPreview(GC gc, Point offset) {
			gcDraw(gc, offset);
			return null;
		}
		public void erasePreview(GC gc, Point offset, Object rememberedData) {
			gcDraw(gc, offset);
		}
		protected abstract void gcDraw(GC gc, Point offset);
	}

	/**
	 * 2D Line object
	 */
	public static class Line extends StatelessXORHelper {
		private Color color;
		private int x1, y1, x2, y2;
		/**
		 * Constructs a <code>Meta.Line</code>
		 * These objects are defined by their two end-points.
		 * 
		 * @param color the color for this object
		 * @param x1 the virtual X coordinate of the first end-point
		 * @param y1 the virtual Y coordinate of the first end-point
		 * @param x2 the virtual X coordinate of the second end-point
		 * @param y2 the virtual Y coordinate of the second end-point
		 */
		public Line(Color color, int x1, int y1, int x2, int y2) {
			this.color = color; this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
		}
		public void draw(GC gc, Point offset) {
			gc.setForeground(color);
			gcDraw(gc, offset);
		}
		protected void gcDraw(GC gc, Point offset) {
			gc.drawLine(x1 + offset.x, y1 + offset.y, x2 + offset.x, y2 + offset.y);
		}			
	}

	/**
	 * 2D Rectangle object
	 */
	public static class Rectangle extends StatelessXORHelper {
		private Color color;
		private int x1, y1, x2, y2;
		/**
		 * Constructs a <code>Meta.Rectangle</code>
		 * These objects are defined by any two diametrically opposing corners.
		 * 
		 * @param color the color for this object
		 * @param x1 the virtual X coordinate of the first corner
		 * @param y1 the virtual Y coordinate of the first corner
		 * @param x2 the virtual X coordinate of the second corner
		 * @param y2 the virtual Y coordinate of the second corner
		 */
		public Rectangle(Color color, int x1, int y1, int x2, int y2) {
			this.color = color; this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
		}
		public void draw(GC gc, Point offset) {
			gc.setForeground(color);
			gcDraw(gc, offset);
		}
		protected void gcDraw(GC gc, Point offset) {
			gc.drawRectangle(Math.min(x1, x2) + offset.x, Math.min(y1, y2) + offset.y,
				Math.abs(x2 - x1), Math.abs(y2 - y1));
		}			
	}

	/**
	 * 2D SolidRectangle object
	 */
	public static class SolidRectangle extends StatelessXORHelper {
		private Color color;
		private int x1, y1, x2, y2;
		/**
		 * Constructs a <code>Meta.SolidRectangle</code>
		 * These objects are defined by any two diametrically opposing corners.
		 * 
		 * @param color the color for this object
		 * @param x1 the virtual X coordinate of the first corner
		 * @param y1 the virtual Y coordinate of the first corner
		 * @param x2 the virtual X coordinate of the second corner
		 * @param y2 the virtual Y coordinate of the second corner
		 */
		public SolidRectangle(Color color, int x1, int y1, int x2, int y2) {
			this.color = color; this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
		}
		public void draw(GC gc, Point offset) {
			gc.setBackground(color);
			gcDraw(gc, offset);
		}
		protected void gcDraw(GC gc, Point offset) {
			gc.fillRectangle(Math.min(x1, x2) + offset.x, Math.min(y1, y2) + offset.y,
				Math.abs(x2 - x1) + 1, Math.abs(y2 - y1) + 1);
		}			
	}

	/**
	 * 2D Ellipse object
	 */
	public static class Ellipse extends StatelessXORHelper {
		private Color color;
		private int x1, y1, x2, y2;
		/**
		 * Constructs a <code>Meta.Ellipse</code>
		 * These objects are defined by any two diametrically opposing corners of a 
		 * 
		 * @param color the color for this object
		 * @param x1 the virtual X coordinate of the first point
		 * @param y1 the virtual Y coordinate of the first point
		 * @param x2 the virtual X coordinate of the second point
		 * @param y2 the virtual Y coordinate of the second point
		 */
		public Ellipse(Color color, int x1, int y1, int x2, int y2) {
			this.color = color; this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
		}
		public void draw(GC gc, Point offset) {
			gc.setForeground(color);
			gcDraw(gc, offset);
		}
		protected void gcDraw(GC gc, Point offset) {
			gc.drawOval(Math.min(x1, x2) + offset.x, Math.min(y1, y2) + offset.y,
				Math.abs(x2 - x1) + 1, Math.abs(y2 - y1) + 1);
		}			
	}
}
