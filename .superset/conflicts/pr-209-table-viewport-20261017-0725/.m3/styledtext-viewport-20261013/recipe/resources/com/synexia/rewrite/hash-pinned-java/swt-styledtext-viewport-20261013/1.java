/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Portions (the balanced piece tree: split/join/balance, subtree char and
 * line-break counts, CR/LF-aware prefix and select) are adapted from
 * com.synexia.mindex.viewer.MIndexStyledTextDocument (hsoliwal/com.synexia,
 * develop 0894a5c1eea717f8758967e2e1a4ba4c2dedd58c), licensed under the
 * Apache License, Version 2.0 (https://www.apache.org/licenses/LICENSE-2.0).
 * Modified: SWT DefaultContent edit semantics (no CR/LF merge across an edit
 * boundary), lazy per-piece delimiter offsets in a bounded residency cache,
 * parallel page counting, SWT error codes and listener protocol.
 *
 * SPDX-License-Identifier: EPL-2.0 AND Apache-2.0
 *******************************************************************************/
package org.eclipse.swt.custom;

import java.util.*;
import java.util.List;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Paged, line-indexed {@link StyledTextContent} for very long text.
 * <p>
 * The text is a balanced tree of immutable pieces (windows into the
 * <code>String</code>s given to {@link #setText(String)} and
 * {@link #replaceTextRange(int, int, String)}; nothing is copied on
 * <code>setText</code>). Every node keeps its char and line-break counts, so the
 * logical extent (char count, line count) and line/offset queries cost
 * O(log pieces) without a per-line table. Per-piece delimiter offsets are the
 * materialized residency: they are computed on demand for the pieces that are
 * actually read (normally the visible lines) and kept in a bounded LRU.
 * </p><p>
 * Line-break semantics are those of {@link DefaultContent}: a CR and an LF that
 * become adjacent through an edit stay two delimiters, and the
 * <code>TextChanging</code> line counts are computed the same way.
 * </p>
 */
class PagedContent implements StyledTextContent {
	//FLLDCIM: VIEWPORT - logical extent (counts) is O(1); residency (offsets) is bounded
	static final int PAGE_CHARS = 1 << 16;
	static final int PIECE_CHARS = 1 << 12;
	static final int RESIDENT_PIECES = 512;
	static final int PARALLEL_PIECES = 64;

	private static final String LineDelimiter = System.lineSeparator();

	final List<StyledTextListener> textListeners = new ArrayList<>();
	Node root;
	final Map<Node, int[]> resident = new LinkedHashMap<>(64, 0.75f, true) {
		private static final long serialVersionUID = 1L;
		@Override
		protected boolean removeEldestEntry(Map.Entry<Node, int[]> eldest) {
			return size() > RESIDENT_PIECES;
		}
	};

PagedContent() {
	setText("");
}

@Override
public void addTextChangeListener(TextChangeListener listener) {
	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	textListeners.add(new StyledTextListener(listener));
}

@Override
@SuppressWarnings("removal")
public void removeTextChangeListener(TextChangeListener listener) {
	if (listener == null) {
		error(SWT.ERROR_NULL_ARGUMENT);
	}
	for (int i = 0; i < textListeners.size(); i++) {
		TypedListener typedListener = textListeners.get(i);
		if (typedListener.getEventListener() == listener) {
			textListeners.remove(i);
			break;
		}
	}
}

@Override
public int getCharCount() {
	return size(root);
}

@Override
public int getLineCount() {
	return breaks(root) + 1;
}

@Override
public String getLineDelimiter() {
	return LineDelimiter;
}

@Override
public String getLine(int index) {
	int lineCount = getLineCount();
	if (index >= lineCount || index < 0) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int start = getOffsetAtLine(index);
	int end = index + 1 < lineCount ? getOffsetAtLine(index + 1) : getCharCount();
	int length = end - start;
	while (length > 0 && isDelimiter(at(root, start + length - 1))) {
		length--;
	}
	return getTextRange(start, length);
}

@Override
public int getLineAtOffset(int charPosition) {
	int charCount = getCharCount();
	if (charPosition > charCount || charPosition < 0) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, ". charPosition=" + charPosition + " charCount=" + charCount);
	}
	int result = prefix(root, charPosition);
	return insideMergedCrLf(charPosition) ? result - 1 : result;
}

@Override
public int getOffsetAtLine(int lineIndex) {
	if (lineIndex == 0) {
		return 0;
	}
	if (lineIndex >= getLineCount() || lineIndex < 0) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	return selectBreakEnd(root, lineIndex);
}

@Override
public String getTextRange(int start, int length) {
	if (length == 0) {
		return "";
	}
	Objects.checkFromIndexSize(start, length, getCharCount());
	Node leaf = root;
	int offset = start;
	while (leaf.text == null) {
		if (offset < leaf.left.length) {
			leaf = leaf.left;
		} else {
			offset -= leaf.left.length;
			leaf = leaf.right;
		}
	}
	if (offset + length <= leaf.length) {
		return leaf.text.substring(leaf.from + offset, leaf.from + offset + length);
	}
	StringBuilder result = new StringBuilder(length);
	append(root, start, start + length, result);
	return result.toString();
}

@Override
public void replaceTextRange(int start, int replaceLength, String newText) {
	validateReplace(start, replaceLength);
	StyledTextEvent event = new StyledTextEvent(this);
	event.type = ST.TextChanging;
	event.start = start;
	// a rescan would merge an adjacent lone \r and \n into one delimiter
	event.replaceLineCount = getLineAtOffset(start + replaceLength) - getLineAtOffset(start);
	event.text = newText;
	event.newLineCount = countBreaks(newText, 0, newText.length());
	event.replaceCharCount = replaceLength;
	event.newCharCount = newText.length();
	sendTextEvent(event);

	Pair first = split(root, start);
	Pair second = split(first.right, replaceLength);
	Node inserted = build(newText, PIECE_CHARS);
	Node left = first.left;
	if (inserted != null) {
		inserted = edge(left, inserted);
		left = join(left, inserted);
	}
	root = join(left, edge(left, second.right));

	event = new StyledTextEvent(this);
	event.type = ST.TextChanged;
	sendTextEvent(event);
}

@Override
public void setText(String text) {
	root = build(text, PAGE_CHARS);
	resident.clear();
	StyledTextEvent event = new StyledTextEvent(this);
	event.type = ST.TextSet;
	event.text = "";
	sendTextEvent(event);
}

void sendTextEvent(StyledTextEvent event) {
	for (StyledTextListener textListener : textListeners) {
		textListener.handleEvent(event);
	}
}

void error(int code) {
	SWT.error(code);
}

static boolean isDelimiter(char ch) {
	return ch == SWT.CR || ch == SWT.LF;
}

/** Same contract as DefaultContent: an edit must not split a CR LF delimiter. */
private boolean isInsideCRLF(int pos) {
	if (pos == 0 || pos == getCharCount()) {
		return false;
	}
	if (at(root, pos - 1) != '\r' || at(root, pos) != '\n') {
		return false;
	}
	// \r and \n that are separate line endings may be edited in between (bug 568033)
	return getLineAtOffset(pos - 1) == getLineAtOffset(pos);
}

private void validateReplace(int start, int replaceLength) {
	if (replaceLength == 0) {
		if (isInsideCRLF(start)) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [0: start=" + start + " len=" + replaceLength + "]");
		}
	} else {
		if (isInsideCRLF(start)) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [1: start=" + start + " len=" + replaceLength + "]");
		}
		if (isInsideCRLF(start + replaceLength)) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [2: start=" + start + " len=" + replaceLength + "]");
		}
	}
}

private boolean insideMergedCrLf(int offset) {
	return offset > 0 && offset < size(root) && at(root, offset - 1) == '\r'
			&& at(root, offset) == '\n' && !noMergeAt(root, offset);
}

/** Marks an edit boundary: an LF after a CR across the boundary stays its own delimiter. */
private static Node edge(Node before, Node after) {
	if (before == null || after == null || before.last != '\r' || after.first != '\n') {
		return after;
	}
	return withFirstNoMerge(after);
}

private static Node withFirstNoMerge(Node n) {
	if (n.text != null) {
		return new Node(n.text, n.from, n.length, n.breaks, true);
	}
	return new Node(withFirstNoMerge(n.left), n.right);
}

private static boolean noMergeAt(Node n, int offset) {
	while (n.text == null) {
		if (offset < n.left.length) {
			n = n.left;
		} else {
			offset -= n.left.length;
			n = n.right;
		}
	}
	return offset == 0 && n.firstNoMerge;
}

private static boolean merges(Node left, Node right) {
	return left.last == '\r' && right.first == '\n' && !right.firstNoMerge;
}

/** Number of delimiters an isolated scan of text[from, to) finds (CR LF counts once). */
static int countBreaks(String text, int from, int to) {
	int count = 0;
	for (int i = text.indexOf('\n', from, to); i >= 0; i = text.indexOf('\n', i + 1, to)) {
		count++;
	}
	for (int i = text.indexOf('\r', from, to); i >= 0; i = text.indexOf('\r', i + 1, to)) {
		if (i + 1 >= to || text.charAt(i + 1) != '\n') {
			count++;
		}
	}
	return count;
}

/** Delimiter ends of a piece; a CR LF records the end of its CR (see selectBreakEnd). */
static int[] delimiterEnds(String text, int from, int to, int count) {
	int[] ends = new int[count];
	int n = 0;
	for (int i = from; i < to; i++) {
		char c = text.charAt(i);
		if (c == '\r' || (c == '\n' && (i == from || text.charAt(i - 1) != '\r'))) {
			ends[n++] = i - from + 1;
		}
	}
	return ends;
}

private int[] ends(Node leaf) {
	int[] ends = resident.get(leaf);
	if (ends == null) {
		ends = delimiterEnds(leaf.text, leaf.from, leaf.from + leaf.length, leaf.breaks);
		resident.put(leaf, ends);
	}
	return ends;
}

private int prefix(Node n, int offset) {
	if (n == null || offset == 0) {
		return 0;
	}
	if (offset == n.length) {
		return n.breaks;
	}
	if (n.text != null) {
		return n.breaks == 0 ? 0 : upperBound(ends(n), offset);
	}
	if (offset <= n.left.length) {
		return prefix(n.left, offset);
	}
	int count = n.left.breaks + prefix(n.right, offset - n.left.length);
	return merges(n.left, n.right) ? count - 1 : count;
}

private static int upperBound(int[] sorted, int offset) {
	int low = 0, high = sorted.length;
	while (low < high) {
		int middle = (low + high) >>> 1;
		if (sorted[middle] <= offset) {
			low = middle + 1;
		} else {
			high = middle;
		}
	}
	return low;
}

private int selectBreakEnd(Node n, int rank) {
	int base = 0;
	while (n.text == null) {
		boolean joined = merges(n.left, n.right);
		int completeInLeft = n.left.breaks - (joined ? 1 : 0);
		if (rank <= completeInLeft) {
			n = n.left;
		} else {
			rank = rank - n.left.breaks + (joined ? 1 : 0);
			base += n.left.length;
			n = n.right;
		}
	}
	int end = ends(n)[rank - 1];
	if (end < n.length && n.text.charAt(n.from + end - 1) == '\r' && n.text.charAt(n.from + end) == '\n') {
		end++;
	}
	return base + end;
}

private static char at(Node n, int offset) {
	while (n.text == null) {
		if (offset < n.left.length) {
			n = n.left;
		} else {
			offset -= n.left.length;
			n = n.right;
		}
	}
	return n.text.charAt(n.from + offset);
}

private static void append(Node n, int from, int to, StringBuilder result) {
	if (n == null || from == to) {
		return;
	}
	if (n.text != null) {
		result.append(n.text, n.from + from, n.from + to);
		return;
	}
	if (from < n.left.length) {
		append(n.left, from, Math.min(to, n.left.length), result);
	}
	if (to > n.left.length) {
		append(n.right, Math.max(0, from - n.left.length), to - n.left.length, result);
	}
}

/** Builds a balanced tree of pieces; large texts count their pieces on worker threads. */
static Node build(String text, int pieceChars) {
	int length = text.length();
	if (length == 0) {
		return null;
	}
	int count = (length + pieceChars - 1) / pieceChars;
	Node[] leaves = new Node[count];
	IntStream pieces = IntStream.range(0, count);
	if (count >= PARALLEL_PIECES) {
		pieces = pieces.parallel();
	}
	pieces.forEach(i -> {
		int from = i * pieceChars;
		int to = Math.min(length, from + pieceChars);
		leaves[i] = new Node(text, from, to - from, countBreaks(text, from, to), false);
	});
	return balanced(leaves, 0, count);
}

private static Node balanced(Node[] leaves, int from, int to) {
	if (to - from == 1) {
		return leaves[from];
	}
	int middle = (from + to) >>> 1;
	return new Node(balanced(leaves, from, middle), balanced(leaves, middle, to));
}

private record Pair(Node left, Node right) {
}

private Pair split(Node n, int offset) {
	if (n == null) {
		return new Pair(null, null);
	}
	if (offset == 0) {
		return new Pair(null, n);
	}
	if (offset == n.length) {
		return new Pair(n, null);
	}
	if (n.text != null) {
		int[] cached = resident.get(n);
		int leftBreaks = cached != null ? upperBound(cached, offset) : countBreaks(n.text, n.from, n.from + offset);
		return new Pair(new Node(n.text, n.from, offset, leftBreaks, n.firstNoMerge),
				new Node(n.text, n.from + offset, n.length - offset, n.breaks - leftBreaks, false));
	}
	if (offset < n.left.length) {
		Pair split = split(n.left, offset);
		return new Pair(split.left, join(split.right, n.right));
	}
	Pair split = split(n.right, offset - n.left.length);
	return new Pair(join(n.left, split.left), split.right);
}

private static Node join(Node left, Node right) {
	if (left == null) {
		return right;
	}
	if (right == null) {
		return left;
	}
	if (left.height > right.height + 1) {
		return balance(new Node(left.left, join(left.right, right)));
	}
	if (right.height > left.height + 1) {
		return balance(new Node(join(left, right.left), right.right));
	}
	return new Node(left, right);
}

private static Node balance(Node n) {
	int difference = n.left.height - n.right.height;
	if (difference > 1) {
		Node left = n.left;
		if (height(left.left) < height(left.right)) {
			left = rotateLeft(left);
		}
		return rotateRight(new Node(left, n.right));
	}
	if (difference < -1) {
		Node right = n.right;
		if (height(right.right) < height(right.left)) {
			right = rotateRight(right);
		}
		return rotateLeft(new Node(n.left, right));
	}
	return n;
}

private static Node rotateLeft(Node n) {
	Node child = n.right;
	return new Node(new Node(n.left, child.left), child.right);
}

private static Node rotateRight(Node n) {
	Node child = n.left;
	return new Node(child.left, new Node(child.right, n.right));
}

private static int size(Node n) {
	return n == null ? 0 : n.length;
}

private static int breaks(Node n) {
	return n == null ? 0 : n.breaks;
}

private static int height(Node n) {
	return n == null ? 0 : n.height;
}

/** Immutable piece (text != null) or inner node; never empty. */
static final class Node {
	final Node left, right;
	final String text;
	final int from;
	final int length, breaks, height;
	final char first, last;
	/** The first char is an LF that does not merge with a preceding CR (edit boundary). */
	final boolean firstNoMerge;

	Node(String text, int from, int length, int breaks, boolean firstNoMerge) {
		this.left = this.right = null;
		this.text = text;
		this.from = from;
		this.length = length;
		this.breaks = breaks;
		this.height = 1;
		this.first = text.charAt(from);
		this.last = text.charAt(from + length - 1);
		this.firstNoMerge = firstNoMerge && first == '\n';
	}

	Node(Node a, Node b) {
		this.left = a;
		this.right = b;
		this.text = null;
		this.from = 0;
		this.length = Math.addExact(a.length, b.length);
		this.breaks = a.breaks + b.breaks - (merges(a, b) ? 1 : 0);
		this.height = Math.max(a.height, b.height) + 1;
		this.first = a.first;
		this.last = b.last;
		this.firstNoMerge = a.firstNoMerge;
	}
}
}
