/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * StyledText (and subclasses such as Nebula's ReadOnlyStyledText) over a
 * {@link GeneratedContent} of N lines (kind STYLEDTEXT). "Lines measured" is
 * counted through a LineStyleListener, which StyledText calls once per line it
 * lays out. Style ranges are not part of the round-trip mix because StyledText
 * ignores setStyleRange while a LineStyleListener is installed; line
 * background, alignment and indent are used instead.
 */
final class StyledTextDemo extends SupersetDemo {

	private final GeneratedContent content = new GeneratedContent();
	private StyledText styled;
	private UiState ui;

	StyledTextDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION;
		styled = "StyledText".equals(spec.simpleName()) ? new StyledText(parent, style)
				: (StyledText) Reflect.make(spec.widgetClass(), parent, style);
		styled.setEditable(false);
		styled.setContent(content);
		styled.addLineStyleListener(e -> counters.inc(Counters.MATERIALIZE));
		return control = styled;
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		content.setLines(n);
	}

	@Override
	public void updateData() {
		rows++;
		content.setLines(rows);
	}

	@Override
	public void scrollTo(int row) {
		styled.setTopIndex(row);
	}

	@Override
	public int visibleRows() {
		return fit(styled.getLineHeight());
	}

	@Override
	public String applyState() {
		if (rows < 10) return super.applyState();
		ui = new UiState(styled);
		styled.setSelection(GeneratedContent.STRIDE + 5, GeneratedContent.STRIDE * 2 + 40);
		styled.setLineBackground(3, 1, ui.yellow);
		styled.setLineAlignment(4, 1, SWT.RIGHT);
		styled.setLineIndent(5, 1, 24);
		return "selection+caret+line_background+line_alignment+line_indent+" + super.applyState();
	}

	@Override
	public String captureState() {
		if (ui == null) return super.captureState();
		return "sel=" + styled.getSelection() + "|caret=" + styled.getCaretOffset() + "|bg3=" + UiState.rgb(styled.getLineBackground(3))
				+ "|align4=" + styled.getLineAlignment(4) + "|indent5=" + styled.getLineIndent(5) + "|line2=" + styled.getLine(2) + "|"
				+ super.captureState();
	}

	@Override
	public String notes() {
		return "line_height=" + styled.getLineHeight() + " chars=" + content.getCharCount();
	}
}
