/**
 *  Copyright (c) 2020 Red Hat Inc., and others
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.swt.snippets;

/*
 * Example snippet: multi-carets/cursors/selection in StyledText
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * @since 3.2.200
 */
import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet378 {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet 376 - multi-carets in StyledText");
		shell.setLayout(new GridLayout(1, false));

		final StringBuilder sb = new StringBuilder();
		final Random random = new Random(2546);
		for (int i = 0; i < 200; i++) {
			sb.append("Very very long text about ").append(random.nextInt(2000)).append("\t");
			if (i % 10 == 0) {
				sb.append("\n");
			}
		}

		final Label label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
		label.setText("Right-click or Alt+click to place additional caret at offset <undefined>");
		final StyledText styledText = new StyledText(shell, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		styledText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		styledText.setText(sb.toString());
		final Label selectionSummaryLabel = new Label(shell, SWT.WRAP);

		AtomicInteger mouseOffset = new AtomicInteger();
		styledText.addMouseMoveListener(e -> {
			mouseOffset.set(styledText.getOffsetAtPoint(new Point(e.x, e.y)));
			label.setText("Right-click to place additional caret at offset " + mouseOffset.get());
		});
		styledText.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> refreshSelection(styledText.getSelectionRanges(), selectionSummaryLabel)));
		styledText.addCaretListener(e -> styledText.getDisplay().asyncExec(() -> refreshSelection(styledText.getSelectionRanges(), selectionSummaryLabel)));

		styledText.setMenu(new Menu(styledText));
		MenuItem addCaretMenuItem = new MenuItem(styledText.getMenu(), SWT.PUSH);
		addCaretMenuItem.setText("Place caret at current mouse location");
		addCaretMenuItem.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> addCaretAtOffset(styledText, mouseOffset.get())));

		shell.setSize(800, 600);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static void refreshSelection(int[] selectedRanges, Label selectionSummaryLabel) {
		int count = selectedRanges.length / 2;
		StringBuilder summary = new StringBuilder();
		summary.append(count);
		summary.append(" selected ranges: ");
		for (int i = 0; i < count; i++) {
			int offset = selectedRanges[2 * i];
			int length = selectedRanges[2 * i + 1];
			if (length == 0) {
				summary.append(" {");
				summary.append(offset);
				summary.append("},");
			} else {
				summary.append(" [");
				summary.append(offset);
				summary.append("..");
				summary.append(offset + length);
				summary.append("],");
			}
		}
		summary.deleteCharAt(summary.length() - 1);
		selectionSummaryLabel.setText(summary.toString());
		selectionSummaryLabel.pack(true);
		selectionSummaryLabel.redraw();
	}

	private static void addCaretAtOffset(final StyledText styledText, int offset) {
		int[] initialRanges = styledText.getSelectionRanges();
		int[] newRanges = Arrays.copyOf(initialRanges, initialRanges.length + 2);
		newRanges[newRanges.length - 2] = offset;
		newRanges[newRanges.length - 1] = 10;
		styledText.setSelectionRanges(newRanges);
	}
}