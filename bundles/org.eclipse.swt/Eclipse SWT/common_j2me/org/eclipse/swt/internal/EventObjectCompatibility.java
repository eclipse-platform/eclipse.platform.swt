package org.eclipse.swt.internal;
/**
 * This class is the cross-platform version of the
 * java.util.EventObject class.
 * <p>
 * It is part of our effort to provide support for both J2SE
 * and J2ME platforms. Under this scheme, classes need to 
 * extend EventObjectCompatibility instead of
 * java.util.EventObject.
 * </p>
 * <p>
 * Note: java.util.EventObject is not part of CDC and CLDC.
 * </p>
 */
public class EventObjectCompatibility implements SerializableCompatibility {
	
	/**
	 * The event source.
	 */
	protected transient Object source;

/**
 * Constructs a new instance of this class.
 *
 * @param source the object which fired the event
 */
public EventObjectCompatibility(Object source) {
	if (source != null) this.source = source;
	else throw new IllegalArgumentException();
}

/**
 * Answers the event source.
 *
 * @return the object which fired the event
 */
public Object getSource() {
	return source;
}

/**
 * Answers the string representation of this EventObjectCompatibility.
 *
 * @return the string representation of this EventObjectCompatibility
 */
public String toString() {
	return getClass().getName() + "[source=" + String.valueOf(source) + ']';
}

}
