/*******************************************************************************
 * Copyright (c) 2007, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.ole.win32;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

/**
 * OLEExample is an example that uses <code>org.eclipse.swt</code> 
 * libraries to implement a simple SWT window that can host different Active X
 * controls.
 *
 * @since 3.3
 */ 
public class OLEExample {

	OleClientSite clientSite;
	OleFrame oleFrame;
	Button closeButton;
	boolean createClientSite = true; // to create OleControlSite, set to false.
	
	public static void main(String[] args) {
		Display display = new Display();
		OLEExample example = new OLEExample();
		example.open(display);
		display.dispose();
	}

	/** 
	 * Create a file Exit menu item 
	 */
	void addFileMenu(OleFrame frame) {
		final Shell shell = frame.getShell();
		Menu menuBar = shell.getMenuBar();
		if (menuBar == null) {
			menuBar = new Menu(shell, SWT.BAR);
			shell.setMenuBar(menuBar);
		}
		MenuItem fileMenu = new MenuItem(menuBar, SWT.CASCADE);
		fileMenu.setText("&File");
		Menu menuFile = new Menu(fileMenu);
		fileMenu.setMenu(menuFile);
		frame.setFileMenus(new MenuItem[] { fileMenu });
		
		MenuItem menuFileExit = new MenuItem(menuFile, SWT.CASCADE);
		menuFileExit.setText("Exit");
		menuFileExit.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> shell.dispose()));
	}

	OleClientSite createSite(OleFrame frame, String progID) {
		if (createClientSite) {
			return new OleClientSite(frame, SWT.NONE, progID);
		} else {
			return new OleControlSite(frame, SWT.NONE, progID);
		}
	}
	
	OleClientSite createSite(OleFrame frame, String progID, File file) {
		if (createClientSite) {
			return new OleClientSite(frame, SWT.NONE, progID, file);
		} else {
			return new OleControlSite(frame, SWT.NONE, progID, file);
		}
	}
	
	void disposeClient() {
		if (clientSite != null)
			clientSite.dispose();
		clientSite = null;
	}

	/**
	 * Prompt the user for a file and try to open it with some known ActiveX controls.
	 */
	void fileOpen() {
		Shell shell = oleFrame.getShell();
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		String fileName = dialog.open();
		if (fileName == null) return;

		disposeClient();

		// try opening a .doc file using Word
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("doc") || 
						fileExtension.equalsIgnoreCase("rtf") ||
						fileExtension.equalsIgnoreCase("txt")) {
					try {
						clientSite = createSite(oleFrame, "Word.Document", new File(fileName));
					} catch (SWTException error2) {
						disposeClient();
					}
				}
			}
		}

		// try opening a xls file with Excel
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("xls")) {
					try {
						clientSite = createSite(oleFrame, "Excel.Sheet", new File(fileName));
					} catch (SWTException error2) {
						disposeClient();
					}
				}
			}
		}

		// try opening a media file with MPlayer
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("mpa")) {
					try {
						clientSite = createSite(oleFrame, "MPlayer", new File(fileName));
					} catch (SWTException error2) {
						disposeClient();
					}
				}
			}
		}

		// try opening with wmv, mpg, mpeg, avi, asf, wav with WMPlayer
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("wmv")
						|| fileExtension.equalsIgnoreCase("mpg")
						|| fileExtension.equalsIgnoreCase("mpeg")
						|| fileExtension.equalsIgnoreCase("avi")
						|| fileExtension.equalsIgnoreCase("asf")
						|| fileExtension.equalsIgnoreCase("wav")) {
					try {
						clientSite = createSite(oleFrame, "WMPlayer.OCX");
						OleAutomation player = new OleAutomation(clientSite);
						int playURL[] = player.getIDsOfNames(new String[] { "URL" });
						if (playURL != null) {
							boolean suceeded = player.setProperty(playURL[0], new Variant(fileName));
							if (!suceeded)
								disposeClient();
						} else {
							disposeClient();
						}
						player.dispose();
					} catch (SWTException error2) {
						disposeClient();
					}
				}
			}
		}

		// try opening a PDF file with Acrobat reader
		if (clientSite == null) {
			int index = fileName.lastIndexOf('.');
			if (index != -1) {
				String fileExtension = fileName.substring(index + 1);
				if (fileExtension.equalsIgnoreCase("pdf")) {
					try {
						clientSite = createSite(oleFrame, "PDF.PdfCtrl.5");
						clientSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
					    OleAutomation pdf = new OleAutomation (clientSite);
					    int loadFile[] = pdf.getIDsOfNames (new String [] {"LoadFile"});
					    if (loadFile != null) {
					    	Variant result = pdf.invoke(loadFile[0], new Variant[] {new Variant(fileName)});
							if (result == null)
								disposeClient();
							else
								result.dispose();
					    } else {
							disposeClient();
					    }
					    pdf.dispose();
					} catch (SWTException error2) {
						disposeClient();
					}
				}
			}
		}

		// try opening with Explorer
		if (clientSite == null) {
			try {
				clientSite = createSite(oleFrame, "Shell.Explorer");
				OleAutomation explorer = new OleAutomation(clientSite);
				int[] navigate = explorer.getIDsOfNames(new String[]{"Navigate"}); 
				
				if (navigate != null) {
					Variant result = explorer.invoke(navigate[0], new Variant[] {new Variant(fileName)});
					if (result == null)
						disposeClient();
					else
						result.dispose();
				} else {
					disposeClient();
				}
				explorer.dispose();
			} catch (SWTException error2) {
				disposeClient();
			}
		}

		if (clientSite != null){
			clientSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
		}
	}

	void newClientSite(String progID) {
		disposeClient();
		try {
			clientSite = createSite(oleFrame, progID);
		} catch (SWTException error) {

		}
		if (clientSite != null)
			clientSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
	}

	public void open(Display display) {
		Shell shell = new Shell(display);
		shell.setText("OLE Example");
		shell.setLayout(new FillLayout());

		Composite parent = new Composite(shell, SWT.NONE);
		parent.setLayout(new GridLayout(4, true));
		
		Composite buttons = new Composite(parent, SWT.NONE);
		buttons.setLayout(new GridLayout());
		GridData gridData = new GridData(SWT.BEGINNING, SWT.FILL, false, false);
		buttons.setLayoutData(gridData);
		
		Composite displayArea = new Composite(parent, SWT.BORDER);
		displayArea.setLayout(new FillLayout());
		displayArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

		Button excelButton = new Button(buttons, SWT.RADIO);
		excelButton.setText("New Excel Sheet");
		excelButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			if (((Button) e.widget).getSelection())
				newClientSite("Excel.Sheet");
		}));
		Button mediaPlayerButton = new Button(buttons, SWT.RADIO);
		mediaPlayerButton.setText("New MPlayer");
		mediaPlayerButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			if (((Button) e.widget).getSelection())
				newClientSite("MPlayer");
		}));
		Button powerPointButton = new Button(buttons, SWT.RADIO);
		powerPointButton.setText("New PowerPoint Slide");
		powerPointButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			if (((Button) e.widget).getSelection())
				newClientSite("PowerPoint.Slide");
		}));
		Button wordButton = new Button(buttons, SWT.RADIO);
		wordButton.setText("New Word Document");
		wordButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			if (((Button) e.widget).getSelection())
				newClientSite("Word.Document");
		}));
		new Label(buttons, SWT.NONE);
		Button openButton = new Button(buttons, SWT.RADIO);
		openButton.setText("Open file...");
		openButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			if (((Button) e.widget).getSelection())
				fileOpen();
		}));
		new Label(buttons, SWT.NONE);
		closeButton = new Button(buttons, SWT.RADIO);
		closeButton.setText("Close file");
		closeButton.addSelectionListener(SelectionListener.widgetSelectedAdapter(e ->{
				if (((Button) e.widget).getSelection())
					disposeClient();
			}
		));	
		closeButton.setSelection(true);
		
		oleFrame = new OleFrame(displayArea, SWT.NONE);
		addFileMenu(oleFrame);

		shell.setSize(800, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
