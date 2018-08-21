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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug261288_TreeBGColor {

  public static void main( final String[] args ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new FillLayout() );

    Tree tree = new Tree( shell, SWT.BORDER | SWT.SINGLE );
    TreeItem rootItem = new TreeItem( tree, SWT.NONE );
    rootItem.setText( "Root" );
    for( int i = 0; i < 5; i++ ) {
      TreeItem item = new TreeItem( rootItem, SWT.NONE );
      item.setText( "Node " + ( i + 1 ) );
    }
    tree.setBackground( display.getSystemColor( SWT.COLOR_RED ) );

    shell.setSize( 400, 300 );
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() )
        display.sleep();
    }
    display.dispose();
  }
}