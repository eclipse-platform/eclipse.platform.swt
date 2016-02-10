/*******************************************************************************
 * Copyright (c) 2014, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.mozilla;

import java.util.HashMap;

import org.eclipse.swt.internal.C;

public abstract class IIDStore {
	static HashMap<Class<?>,nsID[]> IIDs = new HashMap<> ();

	public static nsID GetIID (Class<?> clazz) {
		return GetIID (clazz, MozillaVersion.GetCurrentVersion ());
	}

	public static nsID GetIID (Class<?> clazz, int version) {
		return GetIID (clazz, version, false);
	}

	public static nsID GetIID (Class<?> clazz, int version, boolean exact) {
		if (version <= MozillaVersion.GetLatestVersion ()) {
			nsID[] table = IIDs.get (clazz);
			if (table == null) {
				/* The nsI* class has not been loaded, so attempt to create a default instance to make this happen */
				try {
					Class<?> argType = C.PTR_SIZEOF == 4 ? Integer.TYPE : Long.TYPE;
					clazz.getConstructor (new Class<?>[] {argType}).newInstance (new Object[] {0});
					table = IIDs.get (clazz);
				} catch (Exception e) {
					/* clazz appears to not be an nsI* class, which is not valid */
				}
			}
			if (table != null) {
				if (exact) {
					return table[version];
				}
				int defaultIIDIndex = MozillaVersion.GetLatestVersion () + 1;
				if (version == MozillaVersion.GetCurrentVersion () && table[defaultIIDIndex] != null) {
					return table[defaultIIDIndex]; /* the cached value */
				}
				for (int i = version; MozillaVersion.VERSION_BASE <= i; i--) {
					if (table[i] != null) {
						if (version == MozillaVersion.GetCurrentVersion ()) {
							table[defaultIIDIndex] = table[i]; /* cache for future reference */
						}
						return table[i];
					}
				}
			}
		}
		return null;
	}

	protected static void RegisterIID (Class<?> clazz, int version, nsID iid) {
		if (version <= MozillaVersion.GetLatestVersion ()) {
			nsID[] table = IIDs.get (clazz);
			if (table == null) {
				/*
				 * Note that the table's final slot is used to cache the iid for
				 * the current version since this is the most frequently used one.
				 */
				table = new nsID[MozillaVersion.GetLatestVersion () + 2];
				IIDs.put (clazz, table);
			}
			table[version] = iid;
		}
	}
}
