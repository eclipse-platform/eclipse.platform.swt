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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug271404_DnDCCombo
{
	
	

	public static void main( String[] args )
	{
		Shell shell = new Shell( );
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		shell.setLayout( gl );
		
		final Label text = new Label( shell, SWT.BORDER );
		GridData gd = new GridData( );
		gd.widthHint = 100;
		text.setLayoutData( gd );
		text.setText( "Drag Drop" );


		DragSource ds = new DragSource( text, DND.DROP_MOVE );
		ds.setTransfer( new Transfer[]{
			SimpleTextTransfer.getInstance( )
		} );
		ds.addDragListener( new DragSourceAdapter( ) {

			@Override
			public void dragSetData( DragSourceEvent event )
			{
				event.data = text.getText( );
			}
		} );

		final CCombo combo = new CCombo( shell, SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.widthHint = 150;
		combo.setLayoutData( gd );
		

		DropTarget dt = new DropTarget( combo, DND.DROP_MOVE );
		dt.setTransfer( new Transfer[]{
			SimpleTextTransfer.getInstance( )
		} );
		dt.addDropListener( new DropTargetAdapter( ) {

			@Override
			public void drop( DropTargetEvent event )
			{
				combo.setText( (String) event.data );
			}
		} );

		shell.pack( );
		shell.open( );
		Display display = Display.getDefault( );
		while ( !shell.isDisposed( ) )
			if ( !display.readAndDispatch( ) )
				display.sleep( );
		display.dispose( );
	}

}
