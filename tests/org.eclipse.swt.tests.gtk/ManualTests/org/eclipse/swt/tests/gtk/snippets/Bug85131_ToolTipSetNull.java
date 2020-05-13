/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This code reproduce GTK's setToolTipText(null) problem. 
 */
public class Bug85131_ToolTipSetNull 
{
	public static void main(String[]v)
	{
		Display disp = Display.getDefault();
		final Shell shell = new Shell(disp, SWT.DIALOG_TRIM|SWT.RESIZE );
		shell.setText( "Test Tooltip" );
		
		shell.setLayout( new FillLayout() );
		
		final Canvas canvas = new Canvas( shell, SWT.NONE );
		canvas.addPaintListener( new PaintListener() {
		@Override
		public void paintControl(PaintEvent e)
		{
			Rectangle r = canvas.getClientArea();
			int wm = r.width/8, w2 = r.width/2;
			int hm = r.height/8;
			int rw = w2-wm, rh = r.height-2*hm;
			
			e.gc.setBackground( new Color(200,0,0) );
			e.gc.fillRectangle( 0,hm,w2,rh);
			e.gc.setBackground( new Color(0,0,200) );
			e.gc.fillRectangle( w2,hm+hm,rw+wm,rh-hm);
		}
		});
		canvas.addMouseMoveListener( new MouseMoveListener() {
			@Override
			public void mouseMove(org.eclipse.swt.events.MouseEvent 
e) {
				int mx = e.x;
				int my = e.y;
				
				Rectangle r = canvas.getClientArea();
				int wm = r.width/8, w2 = r.width/2;
				int hm = r.height/8;
				int rw = w2-wm, rh = r.height-2*hm;
				
				String tip = null;
				if( mx>=0 && mx <= 0+w2 && my >= hm && my <= hm+rh )
				{
					tip = "Red Rectangle";
				}
				else if( mx>=w2 && mx <= w2+rw+wm && my >= hm+hm && my <= 
hm+rh )
				{
					tip = "Blue Rectangle";
				}
System.out.println("setToolTipText("+tip+")"); 
				canvas.setToolTipText( tip );
			}
		});

		shell.setSize( 300, 200 );
		shell.open();
		
		while (!shell.isDisposed())
			if (!disp.readAndDispatch())
				disp.sleep();
	}
}