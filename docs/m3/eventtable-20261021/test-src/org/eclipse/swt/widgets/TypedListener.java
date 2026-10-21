// SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
package org.eclipse.swt.widgets;
import java.util.EventListener;
public class TypedListener implements Listener {
    public final EventListener eventListener;
    public TypedListener(EventListener source) { this.eventListener = source; }
    @Override public void handleEvent(Event event) { }
}
