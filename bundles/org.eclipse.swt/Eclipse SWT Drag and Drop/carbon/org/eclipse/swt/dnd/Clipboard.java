package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.widgets.*;

/**
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 */
public class Clipboard {

    public Clipboard(Display display) {
    }

    public void dispose()  {
    }

    public void setContents(String[] s, Transfer[] t) {
    }

    public Object getContents(Transfer t) {
        return null;
    }
}
