/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.printing.*;
import org.eclipse.swt.events.*;

class DialogTab extends Tab {
	/* Example widgets and groups that contain them */
	Group dialogStyleGroup, resultGroup;
	Text textWidget;
	
	/* Style widgets added to the "Style" group */
	Combo dialogCombo;
	Button createButton;
	Button okButton, cancelButton;
	Button yesButton, noButton;
	Button retryButton;
	Button abortButton, ignoreButton;
	Button iconErrorButton, iconInformationButton, iconQuestionButton;
	Button iconWarningButton, iconWorkingButton, noIconButton;
	Button primaryModalButton, applicationModalButton, systemModalButton;
	Button sheetButton;
	Button saveButton, openButton, multiButton;

	static String [] FilterExtensions	= {"*.txt", "*.bat", "*.doc", "*"};
	static String [] FilterNames		= {ControlExample.getResourceString("FilterName_0"),
										   ControlExample.getResourceString("FilterName_1"),
										   ControlExample.getResourceString("FilterName_2"),
										   ControlExample.getResourceString("FilterName_3")};

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	DialogTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Handle a button style selection event.
	 *
	 * @param event the selection event
	 */
	void buttonStyleSelected(SelectionEvent event) {
		/*
		 * Only certain combinations of button styles are
		 * supported for various dialogs.  Make sure the
		 * control widget reflects only valid combinations.
		 */
		boolean ok = okButton.getSelection ();
		boolean cancel = cancelButton.getSelection ();
		boolean yes = yesButton.getSelection ();
		boolean no = noButton.getSelection ();
		boolean abort = abortButton.getSelection ();
		boolean retry = retryButton.getSelection ();
		boolean ignore = ignoreButton.getSelection ();
		
		okButton.setEnabled (!(yes || no || retry || abort || ignore));
		cancelButton.setEnabled (!(abort || ignore || (yes != no)));
		yesButton.setEnabled (!(ok || retry || abort || ignore || (cancel && !yes && !no)));
		noButton.setEnabled (!(ok || retry || abort || ignore || (cancel && !yes && !no)));
		retryButton.setEnabled (!(ok || yes || no));
		abortButton.setEnabled (!(ok || cancel || yes || no));
		ignoreButton.setEnabled (!(ok || cancel || yes || no));
		
		createButton.setEnabled (
				!(ok || cancel || yes || no || retry || abort || ignore) ||
				ok ||
				(ok && cancel) ||
				(yes && no) ||
				(yes && no && cancel) ||
				(retry && cancel) ||
				(abort && retry && ignore));
		

	}
	
	/**
	 * Handle the create button selection event.
	 *
	 * @param event org.eclipse.swt.events.SelectionEvent
	 */
	void createButtonSelected(SelectionEvent event) {
	
		/* Compute the appropriate dialog style */
		int style = getDefaultStyle();
		if (okButton.getEnabled () && okButton.getSelection ()) style |= SWT.OK;
		if (cancelButton.getEnabled () && cancelButton.getSelection ()) style |= SWT.CANCEL;
		if (yesButton.getEnabled () && yesButton.getSelection ()) style |= SWT.YES;
		if (noButton.getEnabled () && noButton.getSelection ()) style |= SWT.NO;
		if (retryButton.getEnabled () && retryButton.getSelection ()) style |= SWT.RETRY;
		if (abortButton.getEnabled () && abortButton.getSelection ()) style |= SWT.ABORT;
		if (ignoreButton.getEnabled () && ignoreButton.getSelection ()) style |= SWT.IGNORE;
		if (iconErrorButton.getEnabled () && iconErrorButton.getSelection ()) style |= SWT.ICON_ERROR;
		if (iconInformationButton.getEnabled () && iconInformationButton.getSelection ()) style |= SWT.ICON_INFORMATION;
		if (iconQuestionButton.getEnabled () && iconQuestionButton.getSelection ()) style |= SWT.ICON_QUESTION;
		if (iconWarningButton.getEnabled () && iconWarningButton.getSelection ()) style |= SWT.ICON_WARNING;
		if (iconWorkingButton.getEnabled () && iconWorkingButton.getSelection ()) style |= SWT.ICON_WORKING;
		if (primaryModalButton.getEnabled () && primaryModalButton.getSelection ()) style |= SWT.PRIMARY_MODAL;
		if (applicationModalButton.getEnabled () && applicationModalButton.getSelection ()) style |= SWT.APPLICATION_MODAL;
		if (systemModalButton.getEnabled () && systemModalButton.getSelection ()) style |= SWT.SYSTEM_MODAL;
		if (sheetButton.getSelection ()) style |= SWT.SHEET;
		if (saveButton.getEnabled () && saveButton.getSelection ()) style |= SWT.SAVE;
		if (openButton.getEnabled () && openButton.getSelection ()) style |= SWT.OPEN;
		if (multiButton.getEnabled () && multiButton.getSelection ()) style |= SWT.MULTI;
	
		/* Open the appropriate dialog type */
		String name = dialogCombo.getText ();
		
		if (name.equals (ControlExample.getResourceString("ColorDialog"))) {
			ColorDialog dialog = new ColorDialog (shell ,style);
			dialog.setRGB (new RGB (100, 100, 100));
			dialog.setText (ControlExample.getResourceString("Title"));
			RGB result = dialog.open ();
			textWidget.append (ControlExample.getResourceString("ColorDialog") + Text.DELIMITER);
			textWidget.append (ControlExample.getResourceString("Result", new String [] {"" + result}) + Text.DELIMITER);
			textWidget.append ("getRGB() = " + dialog.getRGB() + Text.DELIMITER + Text.DELIMITER);
			return;
		}
		
		if (name.equals (ControlExample.getResourceString("DirectoryDialog"))) {
			DirectoryDialog dialog = new DirectoryDialog (shell, style);
			dialog.setMessage (ControlExample.getResourceString("Example_string"));
			dialog.setText (ControlExample.getResourceString("Title"));
			String result = dialog.open ();
			textWidget.append (ControlExample.getResourceString("DirectoryDialog") + Text.DELIMITER);
			textWidget.append (ControlExample.getResourceString("Result", new String [] {"" + result}) + Text.DELIMITER + Text.DELIMITER);
			return;
		}
		
		if (name.equals (ControlExample.getResourceString("FileDialog"))) {
			FileDialog dialog = new FileDialog (shell, style);
			dialog.setFileName (ControlExample.getResourceString("readme_txt"));
			dialog.setFilterNames (FilterNames);
			dialog.setFilterExtensions (FilterExtensions);
			dialog.setText (ControlExample.getResourceString("Title"));
			String result = dialog.open();
			textWidget.append (ControlExample.getResourceString("FileDialog") + Text.DELIMITER);
			textWidget.append (ControlExample.getResourceString("Result", new String [] {"" + result}) + Text.DELIMITER);
			textWidget.append ("getFileNames() =" + Text.DELIMITER);
			if ((dialog.getStyle () & SWT.MULTI) != 0) {
				String [] files = dialog.getFileNames ();
				for (int i=0; i<files.length; i++) {
					textWidget.append ("\t" + files [i] + Text.DELIMITER);
				}
			}
			textWidget.append ("getFilterIndex() = " + dialog.getFilterIndex() + Text.DELIMITER + Text.DELIMITER);
			return;
		}
		
		if (name.equals (ControlExample.getResourceString("FontDialog"))) {
			FontDialog dialog = new FontDialog (shell, style);
			dialog.setText (ControlExample.getResourceString("Title"));
			FontData result = dialog.open ();
			textWidget.append (ControlExample.getResourceString("FontDialog") + Text.DELIMITER);
			textWidget.append (ControlExample.getResourceString("Result", new String [] {"" + result}) + Text.DELIMITER);
			textWidget.append ("getFontList() =" + Text.DELIMITER);
			FontData [] fonts = dialog.getFontList ();
			if (fonts != null) {
				for (int i=0; i<fonts.length; i++) {
					textWidget.append ("\t" + fonts [i] + Text.DELIMITER);
				}
			}
			textWidget.append ("getRGB() = " + dialog.getRGB() + Text.DELIMITER + Text.DELIMITER);
			return;
		}
		
		if (name.equals (ControlExample.getResourceString("PrintDialog"))) {
			PrintDialog dialog = new PrintDialog (shell, style);
			dialog.setText(ControlExample.getResourceString("Title"));
			PrinterData result = dialog.open ();
			textWidget.append (ControlExample.getResourceString("PrintDialog") + Text.DELIMITER);
			textWidget.append (ControlExample.getResourceString("Result", new String [] {"" + result}) + Text.DELIMITER);
			textWidget.append ("getScope() = " + dialog.getScope() + Text.DELIMITER);
			textWidget.append ("getStartPage() = " + dialog.getStartPage() + Text.DELIMITER);
			textWidget.append ("getEndPage() = " + dialog.getEndPage() + Text.DELIMITER);
			textWidget.append ("getPrintToFile() = " + dialog.getPrintToFile() + Text.DELIMITER + Text.DELIMITER);
			return;
		}
	
		if (name.equals(ControlExample.getResourceString("MessageBox"))) {
			MessageBox dialog = new MessageBox (shell, style);
			dialog.setMessage (ControlExample.getResourceString("Example_string"));
			dialog.setText (ControlExample.getResourceString("Title"));
			int result = dialog.open ();
			textWidget.append (ControlExample.getResourceString("MessageBox") + Text.DELIMITER);
			/*
			 * The resulting integer depends on the original
			 * dialog style.  Decode the result and display it.
			 */
			switch (result) {
				case SWT.OK:
					textWidget.append (ControlExample.getResourceString("Result", new String [] {"SWT.OK"}));
					break;
				case SWT.YES:
					textWidget.append (ControlExample.getResourceString("Result", new String [] {"SWT.YES"}));
					break;
				case SWT.NO:
					textWidget.append (ControlExample.getResourceString("Result", new String [] {"SWT.NO"}));
					break;
				case SWT.CANCEL:
					textWidget.append (ControlExample.getResourceString("Result", new String [] {"SWT.CANCEL"}));
					break;
				case SWT.ABORT: 
					textWidget.append (ControlExample.getResourceString("Result", new String [] {"SWT.ABORT"}));
					break;
				case SWT.RETRY:
					textWidget.append (ControlExample.getResourceString("Result", new String [] {"SWT.RETRY"}));
					break;
				case SWT.IGNORE:
					textWidget.append (ControlExample.getResourceString("Result", new String [] {"SWT.IGNORE"}));
					break;
				default:
					textWidget.append(ControlExample.getResourceString("Result", new String [] {"" + result}));
					break;
			}
			textWidget.append (Text.DELIMITER + Text.DELIMITER);
		}
	}
	
	/**
	 * Creates the "Control" group. 
	 */
	void createControlGroup () {
		/*
		 * Create the "Control" group.  This is the group on the
		 * right half of each example tab.  It consists of the
		 * style group, the display group and the size group.
		 */			
		controlGroup = new Group (tabFolderPage, SWT.NONE);
		GridLayout gridLayout= new GridLayout ();
		controlGroup.setLayout(gridLayout);
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		controlGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		controlGroup.setText (ControlExample.getResourceString("Parameters"));
		
		/*
		 * Create a group to hold the dialog style combo box and
		 * create dialog button.
		 */
		dialogStyleGroup = new Group (controlGroup, SWT.NONE);
		dialogStyleGroup.setLayout (new GridLayout ());
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		gridData.horizontalSpan = 2;
		dialogStyleGroup.setLayoutData (gridData);
		dialogStyleGroup.setText (ControlExample.getResourceString("Dialog_Type"));
	}
	
	/**
	 * Creates the "Control" widget children.
	 */
	void createControlWidgets () {
	
		/* Create the combo */
		String [] strings = {
			ControlExample.getResourceString("ColorDialog"), 
			ControlExample.getResourceString("DirectoryDialog"),
			ControlExample.getResourceString("FileDialog"),
			ControlExample.getResourceString("FontDialog"),
			ControlExample.getResourceString("PrintDialog"),
			ControlExample.getResourceString("MessageBox"),
		};
		dialogCombo = new Combo (dialogStyleGroup, SWT.READ_ONLY);
		dialogCombo.setItems (strings);
		dialogCombo.setText (strings [0]);
		dialogCombo.setVisibleItemCount(strings.length);
	
		/* Create the create dialog button */
		createButton = new Button(dialogStyleGroup, SWT.NONE);
		createButton.setText (ControlExample.getResourceString("Create_Dialog"));
		createButton.setLayoutData (new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
	
		/* Create a group for the various dialog button style controls */
		Group buttonStyleGroup = new Group (controlGroup, SWT.NONE);
		buttonStyleGroup.setLayout (new GridLayout ());
		buttonStyleGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		buttonStyleGroup.setText (ControlExample.getResourceString("Button_Styles"));
	
		/* Create the button style buttons */
		okButton = new Button (buttonStyleGroup, SWT.CHECK);
		okButton.setText ("SWT.OK");
		cancelButton = new Button (buttonStyleGroup, SWT.CHECK);
		cancelButton.setText ("SWT.CANCEL");
		yesButton = new Button (buttonStyleGroup, SWT.CHECK);
		yesButton.setText ("SWT.YES");
		noButton = new Button (buttonStyleGroup, SWT.CHECK);
		noButton.setText ("SWT.NO");
		retryButton = new Button (buttonStyleGroup, SWT.CHECK);
		retryButton.setText ("SWT.RETRY");
		abortButton = new Button (buttonStyleGroup, SWT.CHECK);
		abortButton.setText ("SWT.ABORT");
		ignoreButton = new Button (buttonStyleGroup, SWT.CHECK);
		ignoreButton.setText ("SWT.IGNORE");
	
		/* Create a group for the icon style controls */
		Group iconStyleGroup = new Group (controlGroup, SWT.NONE);
		iconStyleGroup.setLayout (new GridLayout ());
		iconStyleGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		iconStyleGroup.setText (ControlExample.getResourceString("Icon_Styles"));
	
		/* Create the icon style buttons */
		iconErrorButton = new Button (iconStyleGroup, SWT.RADIO);
		iconErrorButton.setText ("SWT.ICON_ERROR");
		iconInformationButton = new Button (iconStyleGroup, SWT.RADIO);
		iconInformationButton.setText ("SWT.ICON_INFORMATION");
		iconQuestionButton = new Button (iconStyleGroup, SWT.RADIO);
		iconQuestionButton.setText ("SWT.ICON_QUESTION");
		iconWarningButton = new Button (iconStyleGroup, SWT.RADIO);
		iconWarningButton.setText ("SWT.ICON_WARNING");
		iconWorkingButton = new Button (iconStyleGroup, SWT.RADIO);
		iconWorkingButton.setText ("SWT.ICON_WORKING");
		noIconButton = new Button (iconStyleGroup, SWT.RADIO);
		noIconButton.setText (ControlExample.getResourceString("No_Icon"));
		
		/* Create a group for the modal style controls */
		Group modalStyleGroup = new Group (controlGroup, SWT.NONE);
		modalStyleGroup.setLayout (new GridLayout ());
		modalStyleGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		modalStyleGroup.setText (ControlExample.getResourceString("Modal_Styles"));
	
		/* Create the modal style buttons */
		primaryModalButton = new Button (modalStyleGroup, SWT.RADIO);
		primaryModalButton.setText ("SWT.PRIMARY_MODAL");
		applicationModalButton = new Button (modalStyleGroup, SWT.RADIO);
		applicationModalButton.setText ("SWT.APPLICATION_MODAL");
		systemModalButton = new Button (modalStyleGroup, SWT.RADIO);
		systemModalButton.setText ("SWT.SYSTEM_MODAL");
	
		/* Create a group for other style controls */
		Group otherStyleGroup = new Group (controlGroup, SWT.NONE);
		otherStyleGroup.setLayout (new GridLayout ());
		otherStyleGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		otherStyleGroup.setText (ControlExample.getResourceString("Other_Styles"));
	
		/* Create the other style buttons */
		sheetButton = new Button(otherStyleGroup, SWT.CHECK);
		sheetButton.setText("SWT.SHEET");

		/* Create a group for the file dialog style controls */
		Group fileDialogStyleGroup = new Group (controlGroup, SWT.NONE);
		fileDialogStyleGroup.setLayout (new GridLayout ());
		fileDialogStyleGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		fileDialogStyleGroup.setText (ControlExample.getResourceString("File_Dialog_Styles"));
	
		/* Create the file dialog style buttons */
		openButton = new Button(fileDialogStyleGroup, SWT.RADIO);
		openButton.setText("SWT.OPEN");
		saveButton = new Button (fileDialogStyleGroup, SWT.RADIO);
		saveButton.setText ("SWT.SAVE");
		multiButton = new Button(fileDialogStyleGroup, SWT.CHECK);
		multiButton.setText("SWT.MULTI");
	
		/* Create the orientation group */
		if (RTL_SUPPORT_ENABLE) {
			createOrientationGroup();
		}
		
		/* Add the listeners */
		dialogCombo.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				dialogSelected (event);
			}
		});
		createButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				createButtonSelected (event);
			}
		});
		SelectionListener buttonStyleListener = new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				buttonStyleSelected (event);
			}
		};
		okButton.addSelectionListener (buttonStyleListener);
		cancelButton.addSelectionListener (buttonStyleListener);
		yesButton.addSelectionListener (buttonStyleListener);
		noButton.addSelectionListener (buttonStyleListener);
		retryButton.addSelectionListener (buttonStyleListener);
		abortButton.addSelectionListener (buttonStyleListener);
		ignoreButton.addSelectionListener (buttonStyleListener);
	
		/* Set default values for style buttons */
		okButton.setEnabled (false);
		cancelButton.setEnabled (false);
		yesButton.setEnabled (false);
		noButton.setEnabled (false);
		retryButton.setEnabled (false);
		abortButton.setEnabled (false);
		ignoreButton.setEnabled (false);
		iconErrorButton.setEnabled (false);
		iconInformationButton.setEnabled (false);
		iconQuestionButton.setEnabled (false);
		iconWarningButton.setEnabled (false);
		iconWorkingButton.setEnabled (false);
		noIconButton.setEnabled (false);
		saveButton.setEnabled (false);
		openButton.setEnabled (false);
		openButton.setSelection (true);
		multiButton.setEnabled (false);
		noIconButton.setSelection (true);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		exampleGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		
		/*
		 * Create a group for the text widget to display
		 * the results returned by the example dialogs.
		 */
		resultGroup = new Group (exampleGroup, SWT.NONE);
		resultGroup.setLayout (new GridLayout ());
		resultGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		resultGroup.setText (ControlExample.getResourceString("Dialog_Result"));
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		/*
		 * Create a multi lined, scrolled text widget for output.
		 */
		textWidget = new Text(resultGroup, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData gridData = new GridData (GridData.FILL_BOTH);
		textWidget.setLayoutData (gridData);	
	}
	
	/**
	 * The platform dialogs do not have SWT listeners.
	 */
	void createListenersGroup () {
	}

	/**
	 * Handle a dialog type combo selection event.
	 *
	 * @param event the selection event
	 */
	void dialogSelected (SelectionEvent event) {
	
		/* Enable/Disable the buttons */
		String name = dialogCombo.getText ();
		boolean isMessageBox = name.equals (ControlExample.getResourceString("MessageBox"));
		boolean isFileDialog = name.equals (ControlExample.getResourceString("FileDialog"));
		okButton.setEnabled (isMessageBox);
		cancelButton.setEnabled (isMessageBox);
		yesButton.setEnabled (isMessageBox);
		noButton.setEnabled (isMessageBox);
		retryButton.setEnabled (isMessageBox);
		abortButton.setEnabled (isMessageBox);
		ignoreButton.setEnabled (isMessageBox);
		iconErrorButton.setEnabled (isMessageBox);
		iconInformationButton.setEnabled (isMessageBox);
		iconQuestionButton.setEnabled (isMessageBox);
		iconWarningButton.setEnabled (isMessageBox);
		iconWorkingButton.setEnabled (isMessageBox);
		noIconButton.setEnabled (isMessageBox);
		saveButton.setEnabled (isFileDialog);
		openButton.setEnabled (isFileDialog);
		multiButton.setEnabled (isFileDialog);
	
		/* Unselect the buttons */
		if (!isMessageBox) {
			okButton.setSelection (false);
			cancelButton.setSelection (false);
			yesButton.setSelection (false);
			noButton.setSelection (false);
			retryButton.setSelection (false);
			abortButton.setSelection (false);
			ignoreButton.setSelection (false);
		}
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [0];
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Dialog";
	}
	
	/**
	 * Recreates the "Example" widgets.
	 */
	void recreateExampleWidgets () {
		if (textWidget == null) {
			super.recreateExampleWidgets ();
		} 
	}
}
