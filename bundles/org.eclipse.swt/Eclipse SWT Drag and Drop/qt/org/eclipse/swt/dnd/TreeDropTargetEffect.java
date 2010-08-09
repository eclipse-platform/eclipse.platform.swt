/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * This class provides a default drag under effect (eg. select, insert, scroll
 * and expand) when a drag occurs over a <code>Tree</code>.
 * 
 * <p>
 * Classes that wish to provide their own drag under effect for a
 * <code>Tree</code> can extend the <code>TreeDropTargetEffect</code> class and
 * override any applicable methods in <code>TreeDropTargetEffect</code> to
 * display their own drag under effect.
 * </p>
 * 
 * Subclasses that override any methods of this class must call the
 * corresponding <code>super</code> method to get the default drag under effect
 * implementation.
 * 
 * <p>
 * The feedback value is either one of the FEEDBACK constants defined in class
 * <code>DND</code> which is applicable to instances of this class, or it must
 * be built by <em>bitwise OR</em>'ing together (that is, using the
 * <code>int</code> "|" operator) two or more of those <code>DND</code> effect
 * constants.
 * </p>
 * <p>
 * <dl>
 * <dt><b>Feedback:</b></dt>
 * <dd>FEEDBACK_SELECT, FEEDBACK_INSERT_BEFORE, FEEDBACK_INSERT_AFTER,
 * FEEDBACK_EXPAND, FEEDBACK_SCROLL</dd>
 * </dl>
 * </p>
 * <p>
 * Note: Only one of the styles FEEDBACK_SELECT, FEEDBACK_INSERT_BEFORE or
 * FEEDBACK_INSERT_AFTER may be specified.
 * </p>
 * 
 * @see DropTargetAdapter
 * @see DropTargetEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * 
 * @since 3.3
 */
public class TreeDropTargetEffect extends DropTargetEffect {
	/**
	 * Creates a new <code>TreeDropTargetEffect</code> to handle the drag under
	 * effect on the specified <code>Tree</code>.
	 * 
	 * @param tree
	 *            the <code>Tree</code> over which the user positions the cursor
	 *            to drop the data
	 */
	public TreeDropTargetEffect(Tree tree) {
		super(tree);
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		System.out.println("TreeDropTargetEffect.dragEnter: " + event);
		super.dragEnter(event);
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		System.out.println("TreeDropTargetEffect.dragLeave: " + event);
		super.dragLeave(event);
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		System.out.println("TreeDropTargetEffect.dragOver: " + event);
		Tree tree = (Tree) control;
		//int effect = checkEffect(event.feedback);

		TreeItem item = tree.getItem(tree.toControl(new Point(event.x, event.y)));

		tree.highlightItem(item);
	}

	@Override
	public void drop(DropTargetEvent event) {
		System.out.println("TreeDropTargetEffect.drop: " + event);
		super.drop(event);
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		System.out.println("TreeDropTargetEffect.dropAccept: " + event);
		super.dropAccept(event);
	}

	@Override
	public Widget getItem(int x, int y) {
		//System.out.println("TreeDropTargetEffect.getItem: " + x + " " + y);
		Tree tree = (Tree) control;
		TreeItem item = tree.getItem(tree.toControl(new Point(x, y)));
		//System.out.println("item: " + item);
		return item;
	}

	private int checkEffect(int effect) {
		// Some effects are mutually exclusive.  Make sure that only one of the mutually exclusive effects has been specified.
		if ((effect & DND.FEEDBACK_SELECT) != 0) {
			effect = effect & ~DND.FEEDBACK_INSERT_AFTER & ~DND.FEEDBACK_INSERT_BEFORE;
		}
		if ((effect & DND.FEEDBACK_INSERT_BEFORE) != 0) {
			effect = effect & ~DND.FEEDBACK_INSERT_AFTER;
		}
		return effect;
	}

}
