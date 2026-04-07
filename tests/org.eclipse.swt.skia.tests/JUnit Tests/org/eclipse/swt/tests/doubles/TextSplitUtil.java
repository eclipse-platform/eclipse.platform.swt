package org.eclipse.swt.tests.doubles;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.skia.cache.SplitsTextCache;

public class TextSplitUtil {

	public static String[] getTextSplits(String inputText, int flags, Map<SplitsTextCache, String[]> cachedTextSplits,
			boolean useTextImageCache) {
		final boolean replaceAmpersand = (flags & SWT.DRAW_MNEMONIC) != 0;
		final boolean delimiter = (flags & SWT.DRAW_DELIMITER) != 0;
		final boolean tabulatorExpansion = (flags & SWT.DRAW_TAB) != 0;

		String[] splits = null;

		if (useTextImageCache && cachedTextSplits != null) {
			splits = cachedTextSplits
					.get(new SplitsTextCache(inputText, replaceAmpersand, delimiter, tabulatorExpansion));
		}

		String workInputText = inputText;

		if (splits == null) {
			if (tabulatorExpansion) {
				workInputText = expandTabs(workInputText, 0);
			} else {
				workInputText = workInputText.replaceAll("\\t", ""); //$NON-NLS-1$ //$NON-NLS-2$
			}

			if (replaceAmpersand) {
				workInputText = replaceMnemonics(workInputText);
			}

			// replace form feed characters with "\u240C" this is the unicode standard sign
			// for form feed.
			// unfortunately skia does not even render these form feed characters...
			workInputText = workInputText.replace("\f", "\u240C"); //$NON-NLS-1$//$NON-NLS-2$

			if (delimiter) {
				splits = splitString(workInputText);
			} else {
				splits = new String[] { removeDelimiter(workInputText) };
			}
		}

		if (useTextImageCache && cachedTextSplits != null) {
			cachedTextSplits.put(new SplitsTextCache(inputText, replaceAmpersand, delimiter, tabulatorExpansion),
					splits);
		}
		return splits;
	}

	private static String removeDelimiter(String workInputText) {
		// Removes delimiter characters (\n, \r) from the string
		if (workInputText == null)
			return null;
		return workInputText.replace("\n", "").replace("\r", "");
	}

	private static String[] splitString(String workInputText) {
		// Splits the string by line breaks (\r?\n)
		if (workInputText == null)
			return null;
		return workInputText.split("\r?\n");
	}

	private static String replaceMnemonics(String workInputText) {
		// Replaces single '&' with '' (removes mnemonic markers)
		if (workInputText == null)
			return null;
		return workInputText.replace("&&", "&").replace("&", "");
	}

	private static String expandTabs(String workInputText, int tabSize) {
		// Replaces tab characters with spaces (tabSize or default 4)
		if (workInputText == null)
			return null;
		int size = tabSize > 0 ? tabSize : 4;
		return workInputText.replace("\t", " ".repeat(size));
	}

}