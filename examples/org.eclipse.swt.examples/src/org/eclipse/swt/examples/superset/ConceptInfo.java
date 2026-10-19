/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

/**
 * One row of {@code superset-catalog.tsv}, projected from the Synexia
 * SUPERSET_WIDGET_CATALOG.tsv and WIDGET_IMPLEMENTATION_LEDGER.tsv.
 */
public record ConceptInfo(String concept, String category, boolean large, String gapAction,
		String viewportStatus, String swtOwner, String nebulaOwner, String ledgerOwner,
		String ledgerStatus, int priority) {

	static ConceptInfo parse(String line) {
		String[] f = line.split("\t", -1);
		return new ConceptInfo(f[0], f[1], "y".equals(f[2]), f[3], f[4], f[5], f[6], f[7], f[8],
				f[9].isEmpty() ? 0 : Integer.parseInt(f[9]));
	}

	/** @return one line naming the ledger row, owner and status */
	public String ledgerLine() {
		return "ledger: " + concept + " | gap_action=" + gapAction + " | owner=" + ledgerOwner
				+ " | status=" + ledgerStatus + " | viewport=" + viewportStatus;
	}
}
