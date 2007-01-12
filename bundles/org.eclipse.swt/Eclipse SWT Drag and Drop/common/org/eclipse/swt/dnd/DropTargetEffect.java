/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


/**
 * This class provides a default drag under effect during a drag and drop. 
 * The current implementation does not provide any visual feedback.
 * 
 * <p>The drop target effect has the same API as the 
 * <code>DropTargetAdapter</code> so that it can provide custom visual 
 * feedback when a <code>DropTargetEvent</code> occurs. 
 * </p>
 * 
 * <p>Classes that wish to provide their own drag under effect
 * can extend the <code>DropTargetEffect</code> and override any applicable methods 
 * in <code>DropTargetAdapter</code> to display their own drag under effect.</p>
 *
 * <p>The feedback value is either one of the FEEDBACK constants defined in 
 * class <code>DND</code> which is applicable to instances of this class, 
 * or it must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>DND</code> effect constants. 
 * </p>
 * <p>
 * <dl>
 * <dt><b>Feedback:</b></dt>
 * <dd>FEEDBACK_EXPAND, FEEDBACK_INSERT_AFTER, FEEDBACK_INSERT_BEFORE, 
 * FEEDBACK_NONE, FEEDBACK_SELECT, FEEDBACK_SCROLL</dd>
 * </dl>
 * </p>
 * 
 * @see DropTargetAdapter
 * @see DropTargetEvent
 * 
 * @since 3.3
 */
public class DropTargetEffect extends DropTargetAdapter {}
