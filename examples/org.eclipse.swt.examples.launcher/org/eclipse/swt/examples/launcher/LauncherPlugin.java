package org.eclipse.swt.examples.launcher;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.io.*;import java.net.*;import java.text.*;import java.util.*;import org.eclipse.core.runtime.*;import org.eclipse.swt.graphics.*;import org.eclipse.ui.plugin.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class LauncherPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static LauncherPlugin plugin;
	private static ResourceBundle resourceBundle;

	private static final String
		LAUNCH_ITEMS_POINT_ID = "org.eclipse.swt.examples.launcher.launchItems",
		LAUNCH_ITEMS_XML_CATEGORY = "category",
		LAUNCH_ITEMS_XML_ITEM = "item",
		LAUNCH_ITEMS_XML_ITEM_ICON = "icon",
		LAUNCH_ITEMS_XML_ITEM_DESCRIPTION = "description",
		LAUNCH_ITEMS_XML_PROGRAM = "program",
		LAUNCH_ITEMS_XML_PROGRAM_PLUGIN = "pluginId",
		LAUNCH_ITEMS_XML_PROGRAM_CLASS = "mainClass",
		LAUNCH_ITEMS_XML_VIEW = "view",
		LAUNCH_ITEMS_XML_VIEW_VIEWID = "viewId",
		LAUNCH_ITEMS_XML_SOURCE = "source",
		LAUNCH_ITEMS_XML_SOURCE_ZIP = "zip",
		LAUNCH_ITEMS_XML_ATTRIB_ID = "id",
		LAUNCH_ITEMS_XML_ATTRIB_NAME = "name",
		LAUNCH_ITEMS_XML_ATTRIB_ENABLED = "enabled",
		LAUNCH_ITEMS_XML_ATTRIB_CATEGORY = "category",
		LAUNCH_ITEMS_XML_VALUE_TRUE = "true",
		LAUNCH_ITEMS_XML_VALUE_FALSE = "false";		

	static final int
		liClosedFolder = 0,
		liOpenFolder = 1,
		liGenericExample = 2;
	static final String[] imageLocations = {
		"icons/closedFolder.gif",
		"icons/openFolder.gif",
		"icons/generic_example.gif" };
	static Image images[];

	/**
	 * Constructs the LauncherPlugin.
	 */
	public LauncherPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		resourceBundle = descriptor.getResourceBundle();
	}

	/**
	 * Clean up
	 */
	public void shutdown() throws CoreException {
		super.shutdown();
		freeResources();
	}

	/**
	 * Returns the shared instance.
	 */
	public static LauncherPlugin getDefault() {
		return plugin;
	}

	/**
	 * Loads the resources
	 */
	public static void initResources() {
		if (images == null) {
			images = new Image[imageLocations.length];
				
			for (int i = 0; i < imageLocations.length; ++i) {
				images[i] = getImageFromPlugin(plugin.getDescriptor(), imageLocations[i]);
				if (images[i] == null) {
					freeResources();
					logError(getResourceString("error.CouldNotLoadResources"), null);
					throw new IllegalStateException();
				}
			}
		}	
	}

	/**
	 * Frees the resources
	 */
	public static void freeResources() {
		if (images != null) {
			for (int i = 0; i < images.length; ++i) {
				final Image image = images[i];
				if (image != null) image.dispose();
			}
			images = null;
		}
	}

	/**
	 * Log an error to the ILog for this plugin
	 * 
	 * @param message the localized error message text
	 * @param exception the associated exception, or null
	 */
	public static void logError(String message, Throwable exception) {
		plugin.getLog().log(new Status(IStatus.ERROR, plugin.getDescriptor().getUniqueIdentifier(),
			0, message, exception));
	}

	/**
	 * Returns a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	public static String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}			
	}

	/**
	 * Returns a string from the resource bundle and binds it
	 * with the given arguments. If the key is not found,
	 * return the key.
	 */
	public static String getResourceString(String key, Object[] args) {
		try {
			return MessageFormat.format(getResourceString(key), args);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}

	/**
     * Constructs a list of available programs from registered extensions.
     * 
     * @return an ItemTreeNode representing the root of a tree of items (the root is not to be displayed)
	 */
	public static ItemTreeNode getLaunchItemTree() {
		ItemTreeNode categoryTree =
			new ItemTreeNode(new ItemDescriptor("<<Root>>", "<<Root>>", null, null, null, null));

		// get the platform's public plugin registry
		IPluginRegistry pluginRegistry = Platform.getPluginRegistry();
		// retrieve all configuration elements registered at our launchItems extension-point
		IConfigurationElement[] configurationElements =
			pluginRegistry.getConfigurationElementsFor(LAUNCH_ITEMS_POINT_ID);
			
		if (configurationElements == null || configurationElements.length == 0) {
			logError("Could not find registered extensions", null);
			return categoryTree;
		}
		
		/* Collect all launch categories -- coalesce those with same ID */
		HashMap idMap = new HashMap();
		for (int i = 0; i < configurationElements.length; ++i) {
			final IConfigurationElement ce = configurationElements[i];
			final String ceName = ce.getName();
			final String attribId = getItemAttribute(ce, LAUNCH_ITEMS_XML_ATTRIB_ID, null);
			
			if (idMap.containsKey(attribId)) continue;
			if (ceName.equalsIgnoreCase(LAUNCH_ITEMS_XML_CATEGORY)) {
				final String attribName = getItemName(ce); 
				ItemDescriptor theDescriptor = new ItemDescriptor(attribId, attribName,
					getItemDescription(ce), null, null, null);
				idMap.put(attribId, new ItemTreeNode(theDescriptor));
			}
		}
		
		/* Generate launch category hierarchy */
		Set tempIdSet = new HashSet(); // used to prevent duplicates from being entered into the tree
		for (int i = 0; i < configurationElements.length; ++i) {
			final IConfigurationElement ce = configurationElements[i];
			final String ceName = ce.getName();
			final String attribId = getItemAttribute(ce, LAUNCH_ITEMS_XML_ATTRIB_ID, null);
			
			if (tempIdSet.contains(attribId)) continue;
			if (ceName.equalsIgnoreCase(LAUNCH_ITEMS_XML_CATEGORY)) {
				final ItemTreeNode theNode = (ItemTreeNode) idMap.get(attribId);
				addItemByCategory(ce, categoryTree, theNode, idMap);
				tempIdSet.add(attribId);
			}
		}
		
		/* Generate program tree */
		for (int i = 0; i < configurationElements.length; ++i) {
			final IConfigurationElement ce = configurationElements[i];
			final String ceName = ce.getName();
			final String attribId = getItemAttribute(ce, LAUNCH_ITEMS_XML_ATTRIB_ID, null);

			if (idMap.containsKey(attribId)) continue;
			if (ceName.equalsIgnoreCase(LAUNCH_ITEMS_XML_CATEGORY)) {
				// ignore
			} else if (ceName.equalsIgnoreCase(LAUNCH_ITEMS_XML_ITEM)) {
				final String enabled = getItemAttribute(ce, LAUNCH_ITEMS_XML_ATTRIB_ENABLED, 
					LAUNCH_ITEMS_XML_VALUE_TRUE);
				if (enabled.equalsIgnoreCase(LAUNCH_ITEMS_XML_VALUE_FALSE)) continue;
				ItemDescriptor theDescriptor = createItemDescriptor(ce, attribId);				
			
				if (theDescriptor != null) {
					final ItemTreeNode theNode = new ItemTreeNode(theDescriptor);
					addItemByCategory(ce, categoryTree, theNode, idMap);
					idMap.put(attribId, theNode);
				}
			}
		}
		return categoryTree;
	}

				
	/**
	 * Adds an item to the category tree.
	 */
	private static void addItemByCategory(IConfigurationElement ce, ItemTreeNode root,
		ItemTreeNode theNode, HashMap idMap) {
		final String attribCategory = getItemAttribute(ce, LAUNCH_ITEMS_XML_ATTRIB_CATEGORY, null);
				
		// locate the parent node
		ItemTreeNode parentNode = null;
		if (attribCategory != null) {
			parentNode = (ItemTreeNode) idMap.get(attribCategory);
		}
		if (parentNode == null) parentNode = root;
				
		// add the item
		parentNode.addSortedNode(theNode);
	}

	/**
	 * Creates an ItemDescriptor from an XML definition.
	 * 
	 * @param ce the IConfigurationElement describing the item
	 * @param attribId the attribute id
	 * @return a new ItemDescriptor, or null if an error occurs
	 */
	private static ItemDescriptor createItemDescriptor(IConfigurationElement ce, String attribId) {
		final String attribName = getItemName(ce);
		final Image  attribIcon = getItemIcon(ce);
		final String attribDescription = getItemDescription(ce);
		final URL attribSourceZip;
		final LaunchDelegate launchDelegate;

		IConfigurationElement sourceCE = getItemElement(ce, LAUNCH_ITEMS_XML_SOURCE);
		attribSourceZip = (sourceCE != null) ? getSourceCodePath(sourceCE) : null;

		IConfigurationElement viewCE = getItemElement(ce, LAUNCH_ITEMS_XML_VIEW);
		if (viewCE != null) {
			final String attribView = getItemAttribute(viewCE, LAUNCH_ITEMS_XML_VIEW_VIEWID, null);		
			if (attribView == null) {
				logError(getResourceString("error.IncompleteViewLaunchItem",
					new Object[] { attribId } ), null);
				return null;
			}
			launchDelegate = new ViewLaunchDelegate(attribView);
		} else {
			IConfigurationElement programCE = getItemElement(ce, LAUNCH_ITEMS_XML_PROGRAM);
			if (programCE != null) {
				final String attribPluginId = getItemAttribute(programCE, LAUNCH_ITEMS_XML_PROGRAM_PLUGIN, null);
				final String attribClass    = getItemAttribute(programCE, LAUNCH_ITEMS_XML_PROGRAM_CLASS, null);
						
				if (attribClass == null || attribPluginId == null) {
					logError(getResourceString("error.IncompleteProgramLaunchItem",
					new Object[] { attribId } ), null);
					return null;
				}
				launchDelegate = new ProgramLaunchDelegate(attribPluginId, attribClass);
			} else {
				logError(getResourceString("error.IncompleteLaunchItem",
					new Object[] { attribId } ), null);
				return null;
			}
		}
		return new ItemDescriptor(attribId, attribName, attribDescription, attribSourceZip,
			attribIcon, launchDelegate);
	}

	/**
	 * Returns the first instance of a particular child XML element.
	 * 
	 * @param ce the IConfigurationElement parent
	 * @param element the name of the element to fetch
	 * @return the element's IConfigurationElement, or null if not found
	 */
	private static IConfigurationElement getItemElement(IConfigurationElement ce, String element) {
		IConfigurationElement[] elementCEs = ce.getChildren(element);
		return (elementCEs != null && elementCEs.length != 0) ? elementCEs[0] : null;
	}

	/**
	 * Returns the value of an XML attribute for an item.
	 * 
	 * @param ce the IConfigurationElement describing the item
	 * @param attribute the attribute to fetch
	 * @param defaultValue the value to return if the attribute is not found
	 * @return the attribute value
	 */
	private static String getItemAttribute(IConfigurationElement ce, String attribute, String defaultValue) {
		String value = ce.getAttribute(attribute);
		return (value != null) ? value : defaultValue;
	}

	/**
	 * Returns the description string given the IConfigurationElement for an item.
	 * 
	 * @param ce the IConfigurationElement describing the item
	 * @return a newline-delimited string that describes this item, or null if none
	 */
	private static String getItemDescription(IConfigurationElement ce) {
		String description = getItemAttribute(ce, LAUNCH_ITEMS_XML_ITEM_DESCRIPTION, "");
		return (description.length() == 0) ? null : description;
	}

	/**
	 * Returns the name of an item.
	 * 
	 * @param ce the IConfigurationElement describing the item
	 * @return the attribute value
	 */
	private static String getItemName(IConfigurationElement ce) {
		return getItemAttribute(ce, LAUNCH_ITEMS_XML_ATTRIB_NAME,
			getResourceString("launchitem.Missing.name"));
	}


	/**
	 * Returns the icon for an item.
	 * 
	 * @param ce the IConfigurationElement describing the item
	 * @return an icon
	 */
	private static Image getItemIcon(IConfigurationElement ce) {
		String iconPath = getItemAttribute(ce, LAUNCH_ITEMS_XML_ITEM_ICON, "");
		if (iconPath.length() != 0) {
			Image icon = getImageFromPlugin(ce.getDeclaringExtension().getDeclaringPluginDescriptor(),
				iconPath);
			if (icon != null) {
				Image[] newImages = new Image[images.length + 1];
				System.arraycopy(images, 0, newImages, 0, images.length);
				newImages[images.length] = icon;
				images = newImages;
				return icon;
			}
		}
		return images[liGenericExample];
	}

	/**
	 * Gets an image from a path relative to the plugin install directory.
	 *
	 * @param pd the plugin descriptor for the plugin with the image
	 * @param iconPath the path relative to the install directory
	 * @return the image, or null if not found
	 */
	private static Image getImageFromPlugin(IPluginDescriptor pd, String iconPath) {
		InputStream is = null;
		try {
			URL installUrl = pd.getInstallURL();
			URL url = new URL(installUrl, iconPath);
			is = url.openConnection().getInputStream();
			ImageData source = new ImageData(is);
			ImageData mask = source.getTransparencyMask();
			Image image = new Image(null, source, mask);
			return image;
		} catch (Throwable ex) {
			return null;
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Returns the path of the source code for an item.
	 * 
	 * @param ce the IConfigurationElement describing the item
	 * @return a URL containing the source code path, or null if none is available
	 */
	private static URL getSourceCodePath(IConfigurationElement ce) {
		String sourcePath = getItemAttribute(ce, LAUNCH_ITEMS_XML_SOURCE_ZIP, "");
		if (sourcePath.length() != 0) {
			try {
				// Extract the URL
				IPluginDescriptor pd = ce.getDeclaringExtension().getDeclaringPluginDescriptor();
				URL sourceUrl = new URL(pd.getInstallURL(), sourcePath);
				
				// Test the URL to make sure there really is a resource here
				// (this helps to prevent the user from seeing "Add to Workspace" if no
				//  source bundle is available but one has been specified)
				try {
					InputStream is = sourceUrl.openStream();
					is.close();
				} catch (IOException e) {
					return null;
				}
				return sourceUrl;
			} catch (MalformedURLException e) {
			}
		}
		return null;
	}
}