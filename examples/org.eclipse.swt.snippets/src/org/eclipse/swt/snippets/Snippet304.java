/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * UI Automation (for testing tools) snippet: post key events to simulate
 * moving the I-beam to the end of a text control
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet304 {
    static Display display = null; 

    public static void main(String[] args) {
        display = new Display();
        Shell shell = new Shell(display);

        shell.setLayout(new GridLayout());
        Text text = new Text(shell, SWT.MULTI | SWT.BORDER);
        text.setText("< cursor was there\na\nmulti\nline\ntext\nnow it's here >");

        text.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                System.out.println("KeyDown " + e);
            }
            public void keyReleased(KeyEvent e) {
                System.out.println("KeyUp   " + e);
            }
        });

        shell.pack();
        shell.open();

        /*
        * Simulate the (platform specific) key sequence
        * to move the I-beam to the end of a text control.
        */
    	new Thread(){
    		public void run(){
    			int key = SWT.END;
    			String platform = SWT.getPlatform();
    			if (platform.equals("carbon") || platform.equals("cocoa") ) {
        			key = SWT.ARROW_DOWN;
    			}
    	        postEvent(SWT.MOD1, SWT.KeyDown);
    	        postEvent(key, SWT.KeyDown);
    	        postEvent(key, SWT.KeyUp);
    	        postEvent(SWT.MOD1, SWT.KeyUp);
    		}
    	}.start();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    public static void postEvent(int keyCode, int type) {
        Event event = new Event();
        event.type = type;
        event.keyCode = keyCode;
        display.post(event);
    }
}