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
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Bug225725_GCDrawing {

	public static void main(String[] s){
		Display display = new Display();
		
		Shell shell = new Shell( display );
		shell.setLayout( new GridLayout() );
		shell.setSize(300,400);
		
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem menuBarItem = new MenuItem(menuBar, SWT.CASCADE);
		menuBarItem.setText("File");
		
		Menu menu1 = new Menu(shell, SWT.DROP_DOWN);
		MenuItem menuItem = new MenuItem(menu1, SWT.PUSH);
		menuItem.setText("Exit");
		
		menuBarItem.setMenu(menu1);
		shell.setMenuBar( menuBar );
	    
		ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
	    
		ToolItem item1 = new ToolItem(toolBar, SWT.PUSH);
		item1.setText("Item1");

		ToolItem item2 = new ToolItem(toolBar, SWT.PUSH);
		item2.setText("Item1");

		final Composite composite = new Composite(shell , SWT.BORDER | SWT.DOUBLE_BUFFERED | SWT.H_SCROLL | SWT.V_SCROLL);
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		composite.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				ScrollBar xScroll = composite.getHorizontalBar();
				ScrollBar yScroll = composite.getVerticalBar();
				paintSomething( e.gc, -xScroll.getSelection(),  -yScroll.getSelection());
			}
		});
		
        final ScrollBar hBar = composite.getHorizontalBar();
        hBar.addListener(SWT.Selection, new Listener() {
        	@Override
			public void handleEvent(Event e) {        	  
        		composite.redraw();
        	}
        });
        
        final ScrollBar vBar = composite.getVerticalBar();
        vBar.addListener(SWT.Selection, new Listener() {
        	@Override
			public void handleEvent(Event e) {            
        		composite.redraw();
        	}
        });

        composite.addControlListener(new ControlAdapter() {		
        	@Override
			public void controlResized(ControlEvent arg0) {
        		Rectangle bounds = composite.getBounds();
        		Rectangle client = composite.getClientArea();        
                ScrollBar hBar = composite.getHorizontalBar();
                ScrollBar vBar = composite.getVerticalBar();
        		hBar.setMaximum(500);
        		vBar.setMaximum(500);
        		hBar.setThumb(Math.min(bounds.width, client.width));
        		vBar.setThumb(Math.min(bounds.height, client.height));
			}		
		});
		
		Button newGC = new Button( shell , SWT.PUSH );
		newGC.setText("Direct Paint");
		newGC.setLayoutData(new GridData(SWT.FILL,SWT.BOTTOM,true,false));
		newGC.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GC gc = new GC( composite );
				ScrollBar xScroll = composite.getHorizontalBar();
				ScrollBar yScroll = composite.getVerticalBar();
				paintSomething(gc, -xScroll.getSelection(),  -yScroll.getSelection());
				gc.dispose();
			}		
		});

		shell.open();
		while( !shell.isDisposed() ){
			if(display.readAndDispatch()){
				display.sleep();
			}
		}

		display.dispose();
		System.exit(0);
	}
	
	private static void paintSomething(GC gc, int fromX, int fromY){		
		int x = fromX + 20;
		int y = fromY + 20;
		
		String str = "Test";
		Point strSize = gc.stringExtent(str);
		
		
		gc.drawString( str ,x, y);

		gc.setLineStyle(SWT.LINE_DASH);
		gc.setLineWidth(1);
		Path path = new Path( gc.getDevice() );
		path.addRectangle( x + 110, y + strSize.y, 100, 100 );
		gc.drawPath( path );
		path.dispose();
		gc.setLineStyle(SWT.LINE_SOLID);
		
		path = new Path( gc.getDevice() );
		path.moveTo( x , 20 );
		path.addRectangle( x, y + strSize.y, 100, 100 );
		gc.drawPath( path );
		path.dispose();
		
		gc.drawString( str ,x, y + strSize.y + 101);
	}
}
