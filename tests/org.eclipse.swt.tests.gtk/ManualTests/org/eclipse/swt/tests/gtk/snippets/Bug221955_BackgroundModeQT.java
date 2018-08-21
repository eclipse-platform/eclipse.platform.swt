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
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;


public class Bug221955_BackgroundModeQT {

  public static void main( final String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setText( "BackgroundMode" );
    createContent( shell );
    shell.pack();
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() )
        display.sleep();
    }
    display.dispose();
  }

  private static void createContent( final Composite parent ) {
    parent.setLayout( new GridLayout( 4, false ) );
    Color bgColor = parent.getDisplay().getSystemColor( SWT.COLOR_RED );
    
    Group group1 = new Group( parent, SWT.NONE );
    group1.setBackground( bgColor );
    group1.setBackgroundMode( SWT.INHERIT_NONE );
    group1.setText( "INHERIT_NONE" );
    createControls( group1 );
    
    Group group2 = new Group( parent, SWT.NONE );
    group2.setBackground( bgColor );
    group2.setBackgroundMode( SWT.INHERIT_DEFAULT );
    group2.setText( "INHERIT_DEFAULT" );
    createControls( group2 );
    
    Group group3 = new Group( parent, SWT.NONE );
    group3.setBackground( bgColor );
    group3.setBackgroundMode( SWT.INHERIT_FORCE );
    group3.setText( "INHERIT_FORCE" );
    createControls( group3 );
  }

  private static void createControls( final Composite parent ) {
    parent.setLayout( new GridLayout() );
    Label label = new Label( parent, SWT.NONE );
    label.setText( "Test Label" );
    Label blueLabel = new Label( parent, SWT.NONE );
    blueLabel.setText( "Blue Label" );
    blueLabel.setBackground( parent.getDisplay().getSystemColor( SWT.COLOR_BLUE ) );
    CLabel clabel = new CLabel( parent, SWT.NONE );
    clabel.setText( "Test CLabel" );
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Test Button" );
    Button checkbox = new Button( parent, SWT.CHECK );
    checkbox.setText( "Test Checkbox" );
    List list = new List( parent, SWT.BORDER );
    list.add( "Test List" );
    list.add( "" );
  }
}
