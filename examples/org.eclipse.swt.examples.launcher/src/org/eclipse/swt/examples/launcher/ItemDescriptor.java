package org.eclipse.swt.examples.launcher;/* * (c) Copyright IBM Corp. 2000, 2001, 2002. * All Rights Reserved */import java.net.*;import org.eclipse.swt.graphics.*;
/** * ItemDescriptor collects information about a launch item. */
class ItemDescriptor {
	private String id;
	private String name;
	private String description;	private Image  icon;	private String view;	private String mainType;	private String pluginId;	
	
	/**
	 * Constructs an ItemDescriptor.
	 *
	 * @param id the id
	 * @param name the name
	 * @param description the description	 * @param icon the icon
	 * @param view the host view may be null if it is a standalone application	 * @param mainType the fully qualified class name to run may be null if it is a view	 * @param pluginId the name of the plugin which contains the main class
	 */
	public ItemDescriptor(String id, String name, String description,
		Image icon, String view, String mainType, String pluginId) {
		this.id = id;
		this.name = name;
		this.description = description;		this.icon = icon;
		this.view = view;		this.mainType = mainType;		this.pluginId = pluginId;
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
	/**	 * Returns an icon for this descriptor	 * 	 * @returns an icon, null if the item is a folder	 */	public Image getIcon() {		return icon;	}	
	/**
	 * Returns the host view for the program.
	 * 
	 * @return the host view, null if the item is a standalone program.
	 */
	public String getView () {		return view;	}		/**	 * Returns the fully qualified class to run	 * for the program.	 * 	 * @return the class to run for the program.	 */	public String getMainType () {		return mainType;	}		/**	 * Returns the name of the plugin that contains the program.	 * 	 * @return the name of the plugin that contains the program.	 */	public String getPluginId () {		return pluginId;	}		/**	 * Determines if an item is a folder.	 *	 * @return true if the item is a folder	 */	public boolean isFolder() {		return (mainType == null && view == null);	}
	
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
