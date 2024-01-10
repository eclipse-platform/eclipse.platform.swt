/*******************************************************************************
 * Copyright (c) 2023 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.tests.manual;

import java.lang.reflect.*;
import java.util.List;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Issue0932_StyledText_FixedLineHeight {
	record Landmark(int line, String text) {
	}

	static void setText(StyledText styledText, String text, boolean useBackground) {
		styledText.setText(text);

		if (useBackground) {
			Color clrLineBackOdd = new Color(0xE8, 0xE8, 0xE8);

			for (int iLine = 0; iLine < styledText.getLineCount(); iLine++) {
				if (0 != (iLine % 2)) {
					styledText.setLineBackground(iLine, 1, clrLineBackOdd);
				}
			}
		}
	}

	static void main() {
		final String[] scriptTexts = {
			"Arabic",       "البرية ومحاولة واندونيسيا، من مما, لمّ مع شمال اكتوبر, شيء عن يذكر ك",
			"Armenian",     "լոռեմ իպսում դոլոռ սիթ ամեթ, նե մեա գռաեծե ինեռմիս պառթիենդո, մել նե",
			"Chinese",      "尾新山替様地治中退阪載飯編体。点決括寿週無社馬韓入整連言著短心理化。当年倒港況血旅未旅下年合人航一。続王護業発各判題軽投質向真良世。復高",
			"Cyrillic",     "Лорем ипсум долор сит амет, пер велит доценди ут. Яуаеяуе платонем л",
			"Georgian",     "ლორემ იფსუმ დოლორ სით ამეთ, ვის ეი დელენით ფაცილისის, ვიხ უბიყუე ეფი",
			"Greek",        "Λορεμ ιπσθμ δολορ σιτ αμετ, vισ σθσcιπιτ μαλθισσετ νεγλεγεντθρ ατ, ν",
			"Hebrew",       "תנך או העיר אחרים חבריכם, רבה על מרצועת שימושי מועמדים. מושגי ביולי ",
			"Hindi",        "मुखय पुस्तक एकत्रित प्रव्रुति सहयोग डाले। आजपर पुस्तक वैश्विक यायेका",
			"Japanese",     "覚月ゅ企止理ぞ運61大もちほー道者ぐ審北むルこみ管聞アエ進2人セケヒシ者台ネメク事測反じ籍自ユヱチ扱剣免需残ほ。記シ一4影様語ば口絡ムネ",
			"Korean",       "선거에 관한 경비는 법률이 정하는 경우를 제외하고는 정당 또는 후보자에게 부담시킬 수 없다, 국회의원은 법률이 정하는 직을",
			"Latin",        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eius",
			"Thai",         "ในออกแบบต่างๆ ถ้าข้อความสามารถอ่านได้ ผู้ดูจะสนใจข้อความจนไม่สนใจ กา"
		};

		Landmark[] landmarks = new Landmark[]{
			new Landmark(300, "  hieroglyphs: 覚 新 山\n"),
			new Landmark(500, "  emojis: ❤️ 😊\n"),
			new Landmark(700, "  tall: ༼꧅ ꧁ ꧂ ꦇ က\n"),
		};

		final StringBuilder exampleText = new StringBuilder();
		{
			for (int iLine = 0; iLine < 1000; ) {
				exampleText.append(iLine);
				exampleText.append("  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eius\n");
				iLine++;

				for (Landmark landmark : landmarks) {
					if (iLine == landmark.line) {
						exampleText.append(iLine);
						exampleText.append(landmark.text);
						iLine++;
					}
				}
			}

			exampleText.append("\n");
		}

		for (int iScript = 0; iScript*2 < scriptTexts.length; iScript++)
		{
			exampleText.append(scriptTexts[2*iScript + 0]);
			exampleText.append("\t\t");
			exampleText.append(scriptTexts[2*iScript + 1]);
			exampleText.append("\n");
		}

		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		final Composite cmpHintOptions = new Composite(shell, 0);
		cmpHintOptions.setLayout(new GridLayout(2, false));
		cmpHintOptions.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		final Label hint = new Label(cmpHintOptions, 0);
		hint.setText(
			"1) Press 'Create StyledText' button\n" +
			"2) Scroll StyledText below to line " + landmarks[0].line + "\n" +
			"3) Issue #932: Line height will suddenly increase\n" +
			"   On Linux, the increase is significant, wasting much screen space\n" +
			"\n" +
			"Example text landmarks:\n" +
			"* Line " + landmarks[1].line + " - emojis\n" +
			"* Line " + landmarks[2].line + " - tall characters\n" +
			"* bottom   - various scripts"
		);

		final FontData[] selectedFontData = display.getSystemFont().getFontData();
		final Font[] selectedFont = new Font[1];

		final Group cmpOptions = new Group(cmpHintOptions, 0);
		cmpOptions.setText("Create test StyledText");
		cmpOptions.setLayout(new RowLayout(SWT.VERTICAL));
		cmpOptions.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		final Composite cmpFont = new Composite(cmpOptions, 0);
		cmpFont.setLayout(new GridLayout(2, false));

		final Label lblFont = new Label(cmpFont, 0);

		final Button btnChooseFont = new Button(cmpFont, 0);
		btnChooseFont.setText("...");

		final Button btnUseFixedHeight = new Button(cmpOptions, SWT.CHECK);
		btnUseFixedHeight.setText("Use fixed line height");

		final Button btnUseBackgrounds = new Button(cmpOptions, SWT.CHECK);
		btnUseBackgrounds.setText("Use background colors");

		final Button btnUseAdvancedGC = new Button(cmpOptions, SWT.CHECK);
		btnUseAdvancedGC.setText("Use advanced GC");

		final Button btnCreate = new Button(cmpOptions, 0);
		btnCreate.setText("Create StyledText");

		final TabFolder tabMain = new TabFolder(shell, 0);
		tabMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Tab page with character gallery
		final StyledText txtGallery;
		{
			final TabItem tabItem = new TabItem(tabMain, 0);
			tabItem.setText("Gallery");

			final Composite tabPage = new Composite(tabMain, 0);
			tabPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			tabPage.setLayout(new GridLayout(1, true));

			tabItem.setControl(tabPage);

			txtGallery = new StyledText(tabPage, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			txtGallery.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			final Composite cmpControls = new Composite(tabPage, 0);
			cmpControls.setLayout(new GridLayout(1, true));

			final Button btnPopulate = new Button(cmpControls, SWT.PUSH);
			btnPopulate.setText("Populate (may be slow)");
			btnPopulate.addListener(SWT.Selection, e -> {
				class CharInfo {
					public char character;
					public int  ascent;
					public int  descent;
				}

				final List<CharInfo> charInfos = new ArrayList<>();
				{
					// Populate map.

					// That's rather slow on Windows. I tried putting multiple chars into TextLayout
					// and calculating them together - that's a lot faster, but characters merge into
					// runs and entire run seems to report same height for all chars.
					System.out.println("Measuring characters, have patience! Takes ~10 sec on Windows");

					for (char iChar = 0x30; iChar < 0xFFFF; iChar++) {
						final TextLayout textLayout = new TextLayout(display);
						textLayout.setFont(selectedFont[0]);
						textLayout.setText(String.valueOf(iChar));
						// GC.textExtent() seems to skip font substitution on Windows.
						// Workaround: calculate with TextLayout, similar to how StyledText itself does it.
						FontMetrics metrics = textLayout.getLineMetrics(0);
						final int charDescent = metrics.getDescent();
						final int charAscent  = metrics.getAscent() + metrics.getLeading(); // StyledText considers it this way
						textLayout.dispose();

						final CharInfo charInfo = new CharInfo();
						charInfo.character = iChar;
						charInfo.ascent    = charAscent;
						charInfo.descent   = charDescent;
						charInfos.add(charInfo);
					}

					charInfos.sort((lhs, rhs) -> {
						final int lhsHeight = lhs.ascent + lhs.descent;
						final int rhsHeight = rhs.ascent + rhs.descent;
						if (lhsHeight != rhsHeight)
							return lhsHeight - rhsHeight;

						if (lhs.ascent != rhs.ascent)
							return lhs.ascent - rhs.ascent;

						return lhs.descent - rhs.descent;
					});
				}

				final StringBuilder galleryText = new StringBuilder();

				// Font metrics
				{
					final GC gc = new GC(shell);
					gc.setFont(selectedFont[0]);
					FontMetrics metrics = gc.getFontMetrics();
					gc.dispose();

					galleryText.append("Height=");
					galleryText.append(metrics.getHeight());
					galleryText.append(" Ascent=");
					galleryText.append(metrics.getAscent() + metrics.getLeading());
					galleryText.append(" Descent=");
					galleryText.append(metrics.getDescent());
					galleryText.append("\t\t");
					galleryText.append("{FontMetrics}");
					galleryText.append("\n");
				}

				galleryText.append("\n");

				// Script texts
				for (int iScript = 0; iScript*2 < scriptTexts.length; iScript++)
				{
					final String scriptName = scriptTexts[2*iScript + 0];
					final String scriptText = scriptTexts[2*iScript + 1];

					final TextLayout textLayout = new TextLayout(display);
					textLayout.setFont(selectedFont[0]);
					textLayout.setText(scriptText);
					final FontMetrics metrics = textLayout.getLineMetrics(0);
					textLayout.dispose();

					galleryText.append("Height=");
					galleryText.append(metrics.getHeight());
					galleryText.append(" Ascent=");
					galleryText.append(metrics.getAscent() + metrics.getLeading());
					galleryText.append(" Descent=");
					galleryText.append(metrics.getDescent());
					galleryText.append("\t\t");

					galleryText.append(scriptName);
					galleryText.append("\t\t");
					galleryText.append(scriptText);
					galleryText.append("\n");
				}

				// Individual characters
				{
					int lastAscent    = 0;
					int lastDescent   = 0;
					int lastPlane     = 0;
					int numCharsPlane = 0;
					for (CharInfo charInfo : charInfos) {
						if ((lastAscent != charInfo.ascent) || (lastDescent != charInfo.descent)) {
							lastAscent = charInfo.ascent;
							lastDescent = charInfo.descent;

							galleryText.append("\n");
							galleryText.append("Height=");
							galleryText.append(charInfo.ascent + charInfo.descent);
							galleryText.append(" Ascent=");
							galleryText.append(charInfo.ascent);
							galleryText.append(" Descent=");
							galleryText.append(charInfo.descent);
							galleryText.append("\t\t");
						}

						final int charPlane = charInfo.character & 0xFF00;
						if (lastPlane != charPlane) {
							lastPlane = charPlane;
							numCharsPlane = 0;
						}

						if (numCharsPlane++ >= 8)
							continue;

						galleryText.append(charInfo.character);
						// Prevent characters from merging into surrogate pairs etc
						galleryText.append(' ');
					}
				}

				txtGallery.setText(galleryText.toString());
			});
		}

		Runnable onChosenFont = () -> {
			final Font oldFont = selectedFont[0];
			selectedFont[0] = new Font(display, selectedFontData[0]);

			final String info = selectedFontData[0].getName() + " " + selectedFontData[0].getHeight();
			lblFont.setText("Use font: " + info);
			txtGallery.setFont(selectedFont[0]);
			txtGallery.setText("Click button below to populate");

			if (null != oldFont) {
				oldFont.dispose();
			}
		};
		onChosenFont.run();

		btnChooseFont.addListener(SWT.Selection, e -> {
			final FontDialog fontDialog = new FontDialog(shell);
			FontData newFontData = fontDialog.open();
			if (null != newFontData) {
				selectedFontData[0] = newFontData;
				onChosenFont.run();
			}
		});

		btnCreate.addListener(SWT.Selection, e -> {
			final boolean useFixedHeight = btnUseFixedHeight.getSelection();
			final boolean useBackgrounds = btnUseBackgrounds.getSelection();

			final Composite tabPage = new Composite(tabMain, 0);
			tabPage.setLayout(new GridLayout(1, true));

			final TabItem tabItem = new TabItem(tabMain, 0);
			tabItem.setControl(tabPage);
			tabItem.setText(
				selectedFontData[0].getName() +
				" : " + selectedFontData[0].getHeight() +
				(useFixedHeight ? " (fixH)" : "")
			);

			final Composite textControls = new Composite(tabPage, 0);
			textControls.setLayout(new GridLayout(9, false));
			textControls.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

			final StyledText styledText = new StyledText(tabPage, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			// Controls "toolbar"
			{
				final Button btnBold = new Button(textControls, SWT.CHECK);
				btnBold.setText("B");

				final Button btnItalic = new Button(textControls, SWT.CHECK);
				btnItalic.setText("I");

				final Button btnUnderline = new Button(textControls, SWT.CHECK);
				btnUnderline.setText("U");

				final Button btnStrikeout = new Button(textControls, SWT.CHECK);
				btnStrikeout.setText("S");

				final Button btnForeground = new Button(textControls, SWT.CHECK);
				btnForeground.setText("Fg");

				final Button btnBackground = new Button(textControls, SWT.CHECK);
				btnBackground.setText("Bk");

				final Button btnSetStyles = new Button(textControls, SWT.PUSH);
				btnSetStyles.setText("Set styles");
				btnSetStyles.addListener(SWT.Selection, e2 -> {
					StyleRange style = new StyleRange();
					style.start = styledText.getSelection().x;
					style.length = styledText.getSelection().y - styledText.getSelection().x;

					if (btnBold.getSelection()) {
						style.fontStyle |= SWT.BOLD;
					}

					if (btnItalic.getSelection()) {
						style.fontStyle |= SWT.ITALIC;
					}

					if (btnUnderline.getSelection()) {
						style.underline = true;
					}

					if (btnStrikeout.getSelection()) {
						style.strikeout = true;
					}

					if (btnForeground.getSelection()) {
						style.foreground = new Color(0x80, 0x00, 0x00);
					}

					if (btnBackground.getSelection()) {
						style.background = new Color(0xC0, 0xFF, 0xC0);
					}

					styledText.setStyleRange(style);
				});

				final Button btnResetLineHeight = new Button(textControls, SWT.PUSH);
				btnResetLineHeight.setText("Reset line height");
				btnResetLineHeight.addListener(SWT.Selection, e2 -> {
					String oldText = styledText.getText();
					// Hack: setText() + setFont() causes StyledText to forget cached line height.
					// Setting empty text is needed, because 'StyledText.resetCache()' recalculates
					// a line at index 'renderer.maxWidthLineIndex', which may happen to be a line with
					// non-default height.
					styledText.setText("");
					styledText.setFont(styledText.getFont());
					// Restore old text
					setText(styledText, oldText, useBackgrounds);
				});

				final Label lblCurHeight = new Label(textControls, 0);
				lblCurHeight.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
				styledText.addListener(SWT.Paint, e2 -> {
					final int lineH = styledText.getLineHeight();
					final int lineA = styledText.getBaseline();
					final int lineD = lineH - lineA;
					lblCurHeight.setText("Height=" + lineH + " Ascent=" + lineA + " Descent=" + lineD);
				});
			}

			// Set selected font
			final Font font = new Font(display, selectedFontData[0]);
			styledText.setFont(font);
			styledText.addListener(SWT.Dispose, e2 -> font.dispose());

			if (btnUseAdvancedGC.getSelection()) {
				// Our listener must be first to trigger GDI+ mode in GC
				// before 'StyledText' handles painting. The workaround
				// is to remove old listeners, add ours, re-add old ones.

				final Listener[] oldListeners = styledText.getListeners(SWT.Paint);
				for (Listener listener : oldListeners) {
					styledText.removeListener(SWT.Paint, listener);
				}

				styledText.addListener(SWT.Paint, e2 -> {
					e2.gc.setAdvanced(true);
				});

				for (Listener listener : oldListeners) {
					styledText.addListener(SWT.Paint, listener);
				}
			}

			// If option is selected, set fixed line height
			if (useFixedHeight) {
				try {
					final Method StyledText_setFixedLineMetrics = StyledText.class.getDeclaredMethod("setFixedLineMetrics", FontMetrics.class);

					final TextLayout textLayout = new TextLayout(display);
					textLayout.setText("//0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
					final FontMetrics metrics = textLayout.getLineMetrics(0);
					textLayout.dispose();

					StyledText_setFixedLineMetrics.invoke(styledText, metrics);
				} catch (NoSuchMethodException ex) {
					// Running with older SWT
					System.out.println("Method 'StyledText.setFixedLineHeight()' not found");
				} catch (InvocationTargetException | IllegalAccessException ex) {
					// Unexpected
					ex.printStackTrace();
				}
			}

			// Layout BEFORE setting text, or otherwise it will pre-calculate
			// height of all lines, hiding the problem.
			tabPage.layout();

			// Finally, set example text
			setText(styledText, exampleText.toString(), useBackgrounds);
		});

		shell.setSize(1000, 700);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		if (null != selectedFont[0]) {
			selectedFont[0].dispose();
		}

		display.dispose();
	}

	public static void main(String[] args) {
		System.setProperty("org.eclipse.swt.graphics.Resource.reportNonDisposed", "true");

		main();

		// Detect possible resource leaks.
		// For some reason, 1 invocation is not enough to detect leaks.
		for (int i = 0; i < 10; i++) {
			System.gc();
		}
	}
}
