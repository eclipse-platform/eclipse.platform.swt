package org.eclipse.swt.examples.launcher;/* * (c) Copyright IBM Corp. 2000, 2001. * All Rights Reserved */import java.net.*;import org.eclipse.swt.graphics.*;
/** * ItemDescriptor collects information about a launch item. */
class ItemDescriptor {
	private String id;
	private String name;
	private String description;	private Image  icon;
	private URL sourceCodePath;
	private LaunchDelegate launchDelegate;
	
	/**
	 * Constructs an ItemDescriptor.
	 *
	 * @param id the id
	 * @param name the name
	 * @param description the description
	 * @param launchDelegate a launch delegate for the program to be launched using this descriptor,
	 *        may be null if item is not launchable
	 */
	public ItemDescriptor(String id, String name, String description, URL sourceCodePath,
		Image icon, LaunchDelegate launchDelegate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.sourceCodePath = sourceCodePath;		this.icon = icon;
		this.launchDelegate = launchDelegate;
	}

	/**
	 * Returns the ID for this program.
	 *
	 * @return the user-specified ID for this program
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the translated name for the program.
	 * 
	 * @return the name of the program
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a short description for the program.
	 * 
	 * @return a newline-delimited string describing the program, null if no description is available
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns source code path
	 * 
	 * @return a URL specifying the name of a zip/jar or the base of a directory where the source
	 *         code is stored, null if no source is available
	 */
	public URL getSourceCodePath() {
		return sourceCodePath;
	}
	/**	 * Returns an icon for this descriptor	 * 	 * @returns an icon, null if the item is a folder	 */	public Image getIcon() {		return icon;	}	
	/**
	 * Returns launch delegate for the program.
	 * 
	 * @return the launch delegate, null if the item is a folder
	 */
	public LaunchDelegate getLaunchDelegate() {
		return launchDelegate;
	}		/**	 * Determines if an item is a folder.	 *	 * @return true if the item is a folder	 */	public boolean isFolder() {		return launchDelegate == null;	}
	
	/**
	 * Determines the equality of descriptors.
	 * 
	 * @return true if this.getId().equalsIgnoreCase(other.getId())
	 */
	public boolean equals(Object other) {		if (other instanceof ItemDescriptor) {
			ItemDescriptor otherDescriptor = (ItemDescriptor) other;
			return getId().equalsIgnoreCase(otherDescriptor.getId());
		}
		return false;
	}		/**	 * Produces a hashcode.	 * 	 * @return the hashcode	 */	public int hashCode() {		return id.toUpperCase().hashCode();	}
}
