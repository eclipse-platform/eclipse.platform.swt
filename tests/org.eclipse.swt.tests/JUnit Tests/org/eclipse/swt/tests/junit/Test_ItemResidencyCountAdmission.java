// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.ItemResidencyFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.eclipse.swt.SWTError;

import org.junit.jupiter.api.Test;

/** Logical capacity admission precedes all cancellation, lane, directory and identity mutations. */
public class Test_ItemResidencyCountAdmission {
	@Test
	public void rejectsMaximumCountBeforeWraparound () {
		Object e = engine (0, 1, 8, null, null, null);
		call (e, "setCount", Integer.MAX_VALUE);
		assertThrows (SWTError.class, () -> call (e, "insert", Integer.MAX_VALUE),
				() -> "logical count after rejected insert: " + i (e, "count"));
		assertEquals (Integer.MAX_VALUE, i (e, "count"));
	}

	@Test
	public void rejectedInsertPreservesPendingRequestsDirtyDataSelectionAndPinnedIdentity () {
		Model model = new Model (2, true);
		Object e = engine (0, 1, 8, model.source, null, null);
		call (e, "setCount", Integer.MAX_VALUE);
		call (e, "setViewport", 0, 1, 0);
		call (e, "setBit", k ("LANE_SELECTED"), 1, true);
		call (e, "setText", 1, 0, "caller dirty text");
		Object data = new Object ();
		call (e, "setData", 1, data);
		List<int []> handles = new ArrayList<> ();
		call (e, "setHandleFactory", handleFactory (handles));
		Object pinned = call (e, "handle", 1);
		assertTrue (b (e, "bit", k ("LANE_DATA_REQUESTED"), 0));
		assertThrows (SWTError.class, () -> call (e, "insert", 0));
		assertEquals (Integer.MAX_VALUE, i (e, "count"));
		assertTrue (model.cancels.isEmpty (), "refused insert never cancels the accepted request");
		assertTrue (b (e, "bit", k ("LANE_DATA_REQUESTED"), 0));
		assertTrue (b (e, "bit", k ("LANE_SELECTED"), 1));
		assertEquals ("caller dirty text", call (e, "getText", 1, 0));
		assertSame (data, call (e, "getData", 1));
		assertSame (pinned, call (e, "handle", 1));
		assertArrayEquals (new int [] {1, 0}, (int []) pinned);
		assertTrue (b (e, "isCurrent", pinned, 1, 0));
	}

	@Test
	public void lastRepresentableAppendStillSucceeds () {
		Object e = engine (0, 1, 8, null, null, null);
		call (e, "setCount", Integer.MAX_VALUE - 1);
		call (e, "setBit", k ("LANE_SELECTED"), Integer.MAX_VALUE - 2, true);
		call (e, "insert", Integer.MAX_VALUE - 1);
		assertEquals (Integer.MAX_VALUE, i (e, "count"));
		assertTrue (b (e, "bit", k ("LANE_SELECTED"), Integer.MAX_VALUE - 2));
		assertFalse (b (e, "bit", k ("LANE_SELECTED"), Integer.MAX_VALUE - 1));
		call (e, "remove", Integer.MAX_VALUE - 1);
		assertEquals (Integer.MAX_VALUE - 1, i (e, "count"));
	}
}
