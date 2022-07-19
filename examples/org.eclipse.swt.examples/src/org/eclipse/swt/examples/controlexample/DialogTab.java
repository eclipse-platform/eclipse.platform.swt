/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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
package org.eclipse.swt.examples.controlexample;


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

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
	Button effectsVisibleButton, usePreviousResultButton;
	Button saveButton, openButton, multiButton;
	RGB colorDialogResult, fontDialogColorResult;
	RGB[] colorDialogCustomColors;
	String directoryDialogResult;
	String fileDialogResult;
	int fileDialogIndexResult;
	FontData[] fontDialogFontListResult;
	PrinterData printDialogResult;

	static String [] FilterExtensions	= {"*.txt", "*.bat", "*.doc;*.rtf", "*"};
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
			if (usePreviousResultButton.getSelection()) {
				dialog.setRGB (colorDialogResult);
				dialog.setRGBs(colorDialogCustomColors);
			}
			dialog.setText (ControlExample.getResourceString("Title"));
			RGB result = dialog.open ();
			textWidget.append (ControlExample.getResourceString("ColorDialog") + Text.DELIMITER);
			textWidget.append (ControlExample.getResourceString("Result", "" + result) + Text.DELIMITER);
			textWidget.append ("getRGB() = " + dialog.getRGB() + Text.DELIMITER);
			textWidget.append ("getRGBs() =" + Text.DELIMITER);
			RGB[] rgbs = dialog.getRGBs();
			if (rgbs != null) {
				for (RGB rgbColor : rgbs) {
					textWidget.append ("\t" + rgbColor + Text.DELIMITER);
				}
			}
			textWidget.append (Text.DELIMITER);
			colorDialogResult = result;
			colorDialogCustomColors = rgbs;
			return;
		}

		if (name.equals (ControlExample.getResourceString("DirectoryDialog"))) {
			DirectoryDialog dialog = new DirectoryDialog (shell, style);
			if (usePreviousResultButton.getSelection()) {
				dialog.setFilterPath (directoryDialogResult);
			}
			dialog.setMessage (ControlExample.getResourceString("Example_string"));
			dialog.setText (ControlExample.getResourceString("Title"));
			String result = dialog.open ();
			textWidget.append (ControlExample.getResourceString("DirectoryDialog") + Text.DELIMITER);
			textWidget.append (ControlExample.getResourceString("Result", "" + result) + Text.DELIMITER + Text.DELIMITER);
			directoryDialogResult = result;
			return;
		}

		if (name.equals (ControlExample.getResourceString("FileDialog"))) {
			FileDialog dialog = new FileDialog (shell, style);
			if (usePreviousResultButton.getSelection()) {
				dialog.setFileName (fileDialogResult);
				dialog.setFilterIndex(fileDialogIndexResult);
			}
			dialog.setFilterNames (FilterNames);
			dialog.setFilterExtensions (FilterExtensions);
			dialog.setText (ControlExample.getResourceString("Title"));
			String result = dialog.open();
			textWidget.append (ControlExample.getResourceString("FileDialog") + Text.DELIMITER);
			textWidget.append (ControlExample.getResourceString("Result", "" + result) + Text.DELIMITER);
			textWidget.append ("getFilterIndex() =" + dialog.getFilterIndex() + Text.DELIMITER);
			textWidget.append ("getFilterPath() =" + dialog.getFilterPath() + Text.DELIMITER);
			textWidget.append ("getFileName() =" + dialog.getFileName() + Text.DELIMITER);
			textWidget.append ("getFileNames() =" + Text.DELIMITER);
			String [] files = dialog.getFileNames ();
			for (String file : files) {
				textWidget.append ("\t" + file + Text.DELIMITER);
			}
			textWidget.append (Text.DELIMITER);
			fileDialogResult = result;
			fileDialogIndexResult = dialog.getFilterIndex();
			return;
		}

		if (name.equals (ControlExample.getResourceString("FontDialog"))) {
			FontDialog dialog = new FontDialog (shell, style);
			if (usePreviousResultButton.getSelection()) {
				dialog.setFontList (fontDialogFontListResult);
				dialog.setRGB(fontDialogColorResult);
			}
			dialog.setEffectsVisible(effectsVisibleButton.getSelection());
			dialog.setText (ControlExample.getResourceString("Title"));
			FontData result = dialog.open ();
			textWidget.append (ControlExample.getResourceString("FontDialog") + Text.DELIMITER);
			textWidget.append (ControlExample.getResourceString("Result", "" + result) + Text.DELIMITER);
			textWidget.append ("getFontList() =" + Text.DELIMITER);
			FontData [] fonts = dialog.getFontList ();
			if (fonts != null) {
				for (FontData font : fonts) {
					textWidget.append ("\t" + font + Text.DELIMITER);
				}
			}
			textWidget.append ("getEffectsVisible() = " + dialog.getEffectsVisible() + Text.DELIMITER);
			textWidget.append ("getRGB() = " + dialog.getRGB() + Text.DELIMITER + Text.DELIMITER);
			fontDialogFontListResult = dialog.getFontList ();
			fontDialogColorResult = dialog.getRGB();
			return;
		}

		if (name.equals (ControlExample.getResourceString("PrintDialog"))) {
			PrintDialog dialog = new PrintDialog (shell, style);
			if (usePreviousResultButton.getSelection()) {
				dialog.setPrinterData(printDialogResult);
			}
			dialog.setText(ControlExample.getResourceString("Title"));
			PrinterData result = dialog.open ();
			textWidget.append (ControlExample.getResourceString("PrintDialog") + Text.DELIMITER);
			textWidget.append (ControlExample.getResourceString("Result", "" + result) + Text.DELIMITER);
			if (result != null) {
				textWidget.append ("printerData.scope = " + (result.scope == PrinterData.PAGE_RANGE ? "PAGE_RANGE" : result.scope == PrinterData.SELECTION ? "SELECTION" : "ALL_PAGES") + Text.DELIMITER);
				textWidget.append ("printerData.startPage = " + result.startPage + Text.DELIMITER);
				textWidget.append ("printerData.endPage = " + result.endPage + Text.DELIMITER);
				textWidget.append ("printerData.printToFile = " + result.printToFile + Text.DELIMITER);
				textWidget.append ("printerData.fileName = " + result.fileName + Text.DELIMITER);
				textWidget.append ("printerData.orientation = " + (result.orientation == PrinterData.LANDSCAPE ? "LANDSCAPE" : "PORTRAIT") + Text.DELIMITER);
				textWidget.append ("printerData.copyCount = " + result.copyCount + Text.DELIMITER);
				textWidget.append ("printerData.collate = " + result.collate + Text.DELIMITER);
				textWidget.append ("printerData.duplex = " + (result.duplex == PrinterData.DUPLEX_LONG_EDGE ? "DUPLEX_LONG_EDGE" : result.duplex == PrinterData.DUPLEX_SHORT_EDGE ? "DUPLEX_SHORT_EDGE" : "NONE") + Text.DELIMITER);
			}
			textWidget.append (Text.DELIMITER);
			printDialogResult = result;
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
					textWidget.append (ControlExample.getResourceString("Result", "SWT.OK"));
					break;
				case SWT.YES:
					textWidget.append (ControlExample.getResourceString("Result", "SWT.YES"));
					break;
				case SWT.NO:
					textWidget.append (ControlExample.getResourceString("Result", "SWT.NO"));
					break;
				case SWT.CANCEL:
					textWidget.append (ControlExample.getResourceString("Result", "SWT.CANCEL"));
					break;
				case SWT.ABORT:
					textWidget.append (ControlExample.getResourceString("Result", "SWT.ABORT"));
					break;
				case SWT.RETRY:
					textWidget.append (ControlExample.getResourceString("Result", "SWT.RETRY"));
					break;
				case SWT.IGNORE:
					textWidget.append (ControlExample.getResourceString("Result", "SWT.IGNORE"));
					break;
				default:
					textWidget.append(ControlExample.getResourceString("Result", "" + result));
					break;
			}
			textWidget.append (Text.DELIMITER + Text.DELIMITER);
		}
	}

	/**
	 * Creates the "Control" group.
	 */
	@Override
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
	@Override
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

		/* Create a group for other style and setting controls */
		Group otherGroup = new Group (controlGroup, SWT.NONE);
		otherGroup.setLayout (new GridLayout ());
		otherGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		otherGroup.setText (ControlExample.getResourceString("Other"));

		/* Create the other style and setting controls */
		sheetButton = new Button(otherGroup, SWT.CHECK);
		sheetButton.setText("SWT.SHEET");
		usePreviousResultButton = new Button(otherGroup, SWT.CHECK);
		usePreviousResultButton.setText(ControlExample.getResourceString("Use_Previous_Result"));
		effectsVisibleButton = new Button(otherGroup, SWT.CHECK);
		effectsVisibleButton.setText("FontDialog.setEffectsVisible");

		/* Add the listeners */
		dialogCombo.addSelectionListener (widgetSelectedAdapter(this::dialogSelected));
		createButton.addSelectionListener (widgetSelectedAdapter(this::createButtonSelected));
		SelectionListener buttonStyleListener = widgetSelectedAdapter(this::buttonStyleSelected);
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
		effectsVisibleButton.setEnabled(false);
		effectsVisibleButton.setSelection(true);
	}

	/**
	 * Creates the "Example" group.
	 */
	@Override
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
	@Override
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
	@Override
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
		boolean isFontDialog = name.equals (ControlExample.getResourceString("FontDialog"));
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
		effectsVisibleButton.setEnabled (isFontDialog);
		usePreviousResultButton.setEnabled (!isMessageBox);

		/* Deselect the buttons */
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
	@Override
	Widget [] getExampleWidgets () {
		return new Widget [0];
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	@Override
	String getTabText () {
		return "Dialog";
	}

	/**
	 * Recreates the "Example" widgets.
	 */
	@Override
	void recreateExampleWidgets () {
		if (textWidget == null) {
			super.recreateExampleWidgets ();
		}
	}
}
