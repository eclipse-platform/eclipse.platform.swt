package org.eclipse.swt.internal;

public class EventObjectCompatability  {
	/**
	 * The event source.
	 */
	protected transient Object source;
/**
 * Constructs a new instance of this class.
 *
 * @author		OTI
 * @version		initial
 *
 * @param		source		the object which fired the event
 */
public EventObjectCompatability(Object source) {
	if (source != null) this.source = source;
	else throw new IllegalArgumentException();
}

/**
 * Answers the event source.
 *
 * @author		OTI
 * @version		initial
 *
 * @return		the object which fired the event
 */
public Object getSource() {
	return source;
}
/**
 * Answers the string representation of this EventObject.
 *
 * @author		OTI
 * @version		initial
 *
 * @return		the string representation of this EventObject
 */
public String toString() {
	return getClass().getName() + "[source=" + String.valueOf(source) + ']';
}
}
