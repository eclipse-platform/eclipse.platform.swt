/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.gtk.*;

/**
 * The <code>TransferData</code> class is a platform specific data structure for
 * describing the type and the contents of data being converted by a transfer agent.
 *
 * <p>As an application writer, you do not need to know the specifics of
 * TransferData.  TransferData instances are passed to a subclass of Transfer
 * and the Transfer object manages the platform specific issues.
 * You can ask a Transfer subclass if it can handle this data by calling
 * Transfer.isSupportedType(transferData).</p>
 *
 * <p>You should only need to become familiar with the fields in this class if you
 * are implementing a Transfer subclass and you are unable to subclass the
 * ByteArrayTransfer class.</p>
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class TransferData {
	/**
	 * The type is a unique identifier of a system format or user defined format.
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public long type;

	/**
	 * @noreference This class is not intended to be referenced by clients.
	 * @noinstantiate This class is not intended to be instantiated by clients.
	 * @noextend This class is not intended to be subclassed by clients.
	 * @since 3.132
	 */
	public static class TransferDataGTK3 {
		/**
		 * Specifies the number of units in pValue.
		 * (Warning: This field is platform dependent)
		 * <p>
		 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
		 * public API. It is marked public only so that it can be shared
		 * within the packages provided by SWT. It is not available on all
		 * platforms and should never be accessed from application code.
		 * </p>
		 *
		 * @see TransferDataGTK3#format for the size of one unit
		 *
		 * @noreference This field is not intended to be referenced by clients.
		 */
		public int length;
		/**
		 * Specifies the size in bits of a single unit in pValue.
		 * (Warning: This field is platform dependent)
		 * <p>
		 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
		 * public API. It is marked public only so that it can be shared
		 * within the packages provided by SWT. It is not available on all
		 * platforms and should never be accessed from application code.
		 * </p>
		 *
		 * This is most commonly 8 bits.
		 *
		 * @noreference This field is not intended to be referenced by clients.
		 */
		public int format;
		/**
		 * Pointer to the data being transferred.
		 * (Warning: This field is platform dependent)
		 * <p>
		 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
		 * public API. It is marked public only so that it can be shared
		 * within the packages provided by SWT. It is not available on all
		 * platforms and should never be accessed from application code.
		 * </p>
		 *
		 * @noreference This field is not intended to be referenced by clients.
		 */
		public long pValue;
		/**
		 * The result field contains the result of converting a
		 * java data type into a platform specific value.
		 * (Warning: This field is platform dependent)
		 * <p>
		 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
		 * public API. It is marked public only so that it can be shared
		 * within the packages provided by SWT. It is not available on all
		 * platforms and should never be accessed from application code.
		 * </p>
		 * <p>The value of result is 1 if the conversion was successful.
		 * The value of result is 0 if the conversion failed.</p>
		 *
		 * @noreference This field is not intended to be referenced by clients.
		 */
		public int result;

		private TransferDataGTK3() {}
	}

	/**
	 * @noreference This class is not intended to be referenced by clients.
	 * @noinstantiate This class is not intended to be instantiated by clients.
	 * @noextend This class is not intended to be subclassed by clients.
	 * @since 3.132
	 */
	public static class TransferDataGTK4 {
		// GTK4 specific fields will be introduced here

		private TransferDataGTK4() {}
	}

	private TransferDataGTK3 gtk3;

	private TransferDataGTK4 gtk4;

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public TransferDataGTK3 gtk3() {
		if (gtk3 != null) {
			return gtk3;
		}
		if (!GTK.GTK4) {
			gtk3 = new TransferDataGTK3();
			return gtk3;
		}
		throw new UnsupportedOperationException("Illegal attempt to use GTK3 TransferData on GTK4");
	}
	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public TransferDataGTK4 gtk4() {
		if (gtk4 != null) {
			return gtk4;
		}
		if (GTK.GTK4) {
			gtk4 = new TransferDataGTK4();
			return gtk4;
		}
		throw new UnsupportedOperationException("Illegal attempt to use GTK4 TransferData on GTK3");
	}
}
