/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Syntevo - initial implementation
 *******************************************************************************/
#include <gtk/gtk.h>
#include <cstring>
#include <string>
#include <string.h>

#pragma GCC diagnostic ignored "-Wdeprecated-declarations"

std::string format_GdkRGBA(GdkRGBA a_Color)
{
	char buffer[64];

	sprintf
	(
		buffer,
		"{%3d,%3d,%3d,%3d}",
		(unsigned char)(255 * a_Color.red),
		(unsigned char)(255 * a_Color.green),
		(unsigned char)(255 * a_Color.blue),
		(unsigned char)(255 * a_Color.alpha)
	);

	return buffer;
}

void testColors()
{
	for (int testBits = 0; testBits < (1 << 4); testBits++)
	{
		GdkRGBA colorFore;
		{
			GtkWidgetPath* widgetPath = gtk_widget_path_new();
			gint pathPos = gtk_widget_path_append_type(widgetPath, gtk_tooltip_get_type());

			#if GTK_CHECK_VERSION(3,20,0)
			if (testBits & 1)
				gtk_widget_path_iter_set_object_name(widgetPath, pathPos, "tooltip");
			#else
			// Silence the warning
			(void)pathPos;
			#endif

			if (testBits & 2)
				gtk_widget_path_append_type(widgetPath, gtk_label_get_type());

			GtkStyleContext* styleContext = gtk_style_context_new();
			gtk_style_context_set_path(styleContext, widgetPath);
			gtk_widget_path_free(widgetPath);

			if (testBits & 4)
				gtk_style_context_add_class(styleContext, GTK_STYLE_CLASS_BACKGROUND);

			if (testBits & 8)
				gtk_style_context_add_class(styleContext, GTK_STYLE_CLASS_TOOLTIP);

			gtk_style_context_get_color(styleContext, GTK_STATE_FLAG_NORMAL, &colorFore);

			g_object_unref(styleContext);
		}

		GdkRGBA colorBack;
		{
			GtkWidgetPath* widgetPath = gtk_widget_path_new();
			gint pathPos = gtk_widget_path_append_type(widgetPath, gtk_tooltip_get_type());

			#if GTK_CHECK_VERSION(3,20,0)
			if (testBits & 1)
				gtk_widget_path_iter_set_object_name(widgetPath, pathPos, "tooltip");
			#else
			// Silence the warning
			(void)pathPos;
			#endif

			if (testBits & 2)
				gtk_widget_path_append_type(widgetPath, gtk_label_get_type());

			GtkStyleContext* styleContext = gtk_style_context_new();
			gtk_style_context_set_path(styleContext, widgetPath);
			gtk_widget_path_free(widgetPath);

			if (testBits & 4)
				gtk_style_context_add_class(styleContext, GTK_STYLE_CLASS_BACKGROUND);

			if (testBits & 8)
				gtk_style_context_add_class(styleContext, GTK_STYLE_CLASS_TOOLTIP);

			gtk_style_context_get_background_color(styleContext, GTK_STATE_FLAG_NORMAL, &colorBack);

			g_object_unref(styleContext);
		}

		printf
		(
			"%c%c%c%c fore=%s back=%s\n",
			(testBits & 1) ? '1' : '-',
			(testBits & 2) ? '2' : '-',
			(testBits & 4) ? '3' : '-',
			(testBits & 8) ? '4' : '-',
			format_GdkRGBA(colorFore).c_str(),
			format_GdkRGBA(colorBack).c_str()
		);
	}
}

void styleContext_getForeColor(GtkStyleContext* a_StyleContext, GdkRGBA* a_ForeColor)
{
	// gtk_style_context_save(a_StyleContext);
	// gtk_style_context_set_state(a_StyleContext, GTK_STATE_FLAG_NORMAL);
	gtk_style_context_get_color(a_StyleContext, GTK_STATE_FLAG_NORMAL, a_ForeColor);
	// gtk_style_context_restore(a_StyleContext);
}

bool styleContext_hasBackgroundImage(GtkStyleContext* a_StyleContext)
{
	GValue valueBackgroundImage = G_VALUE_INIT;
	gtk_style_context_get_property(a_StyleContext, GTK_STYLE_PROPERTY_BACKGROUND_IMAGE, GTK_STATE_FLAG_NORMAL, &valueBackgroundImage);

	const bool result = (NULL != g_value_get_boxed(&valueBackgroundImage));

	g_value_unset(&valueBackgroundImage);

	return result;
}

int inversePremultipliedColor(int color, int alpha) {
	if (alpha == 0) return 0;
	return (255*color + alpha-1) / alpha;
}

void renderAllBackgrounds(GtkStyleContext* a_StyleContext, cairo_t* a_Cairo)
{
	GtkStyleContext* parentStyleContext = gtk_style_context_get_parent(a_StyleContext);
	if (parentStyleContext)
		renderAllBackgrounds(parentStyleContext, a_Cairo);

	gtk_render_background(a_StyleContext, a_Cairo, -50, -50, 100, 100);
}

void styleContext_estimateBackColor(GtkStyleContext* a_StyleContext, GdkRGBA* a_BackColor)
{
	GtkStateFlags state = GTK_STATE_FLAG_NORMAL;

	unsigned char imageBytes[4];
	{
		gtk_style_context_save(a_StyleContext);
		gtk_style_context_set_state(a_StyleContext, state);
		cairo_surface_t* surface = cairo_image_surface_create(CAIRO_FORMAT_ARGB32, 1, 1);
		cairo_t* cairo = cairo_create(surface);

		renderAllBackgrounds(a_StyleContext, cairo);
		cairo_surface_flush(surface);
		memcpy(imageBytes, cairo_image_surface_get_data(surface), sizeof(imageBytes));

		cairo_surface_destroy(surface);
		cairo_destroy(cairo);
		gtk_style_context_restore(a_StyleContext);
	}

	const unsigned char a = imageBytes[3];
	const unsigned char r = imageBytes[2];
	const unsigned char g = imageBytes[1];
	const unsigned char b = imageBytes[0];

	a_BackColor->alpha = a / 255.0;
	a_BackColor->red   = inversePremultipliedColor(r, a) / 255.0;
	a_BackColor->green = inversePremultipliedColor(g, a) / 255.0;
	a_BackColor->blue  = inversePremultipliedColor(b, a) / 255.0;
}

void styleContext_getBackColor(GtkStyleContext* a_StyleContext, GdkRGBA* a_BackColor)
{
	gtk_style_context_get_background_color(a_StyleContext, GTK_STATE_FLAG_NORMAL, a_BackColor);
}

std::string dumpStyleContext(GtkStyleContext* a_StyleContext)
{
	// char* contextDump = gtk_style_context_to_string(a_StyleContext, GTK_STYLE_CONTEXT_PRINT_RECURSE);

	const GtkWidgetPath* widgetPath = gtk_style_context_get_path(a_StyleContext);
	char* contextDump = gtk_widget_path_to_string(widgetPath);
	std::string result = contextDump;
	g_free(contextDump);

	return result;
}

void printTooltipColors(GtkStyleContext* a_StyleContextLabel, GtkStyleContext* a_StyleContextTooltip, const char* a_Caption)
{
	const std::string dumpFore = dumpStyleContext(a_StyleContextLabel);
	const std::string dumpBack = dumpStyleContext(a_StyleContextTooltip);

	GdkRGBA colorFore;
	styleContext_getForeColor(a_StyleContextLabel, &colorFore);

	const bool hasBackgroundImage = styleContext_hasBackgroundImage(a_StyleContextTooltip);

	GdkRGBA colorBackEstim;
	styleContext_estimateBackColor(a_StyleContextLabel, &colorBackEstim);

	GdkRGBA colorBackValue;
	styleContext_getBackColor(a_StyleContextTooltip, &colorBackValue);

	printf
	(
		"With %s:\n"
		"\tlabel   = %s\n"
		"\ttooltip = %s\n"
		"\t%s = Fore color (via gtk_style_context_get_color(label))\n"
		"\t%s = Back color (via gtk_render_background(label))\n"
		"\t%s = Back color (via gtk_style_context_get_background_color(tooltip))\n"
		"\tbackground-image=%c\n",
		a_Caption,
		dumpFore.c_str(),
		dumpBack.c_str(),
		format_GdkRGBA(colorFore).c_str(),
		format_GdkRGBA(colorBackEstim).c_str(),
		format_GdkRGBA(colorBackValue).c_str(),
		hasBackgroundImage ? 'Y' : 'N'
	);
}

void getTooltipColors_GtkWidgetPath()
{
#if GTK_CHECK_VERSION(3,20,0)
	if (0 != gtk_check_version(3, 20, 0))
	{
		// 'gtk_widget_path_iter_set_object_name' was introduced in 3.20.0
		printf("With GtkWidgetPath:    Requires 3.20.0\n");
		return;
	}

	// Foreground color is taken from 'tooltip label' css node
	GtkStyleContext* styleContextLabel = gtk_style_context_new();
	{
		GtkWidgetPath* widgetPath = gtk_widget_path_new();
		gtk_widget_path_append_type(widgetPath, 0);
		gtk_widget_path_iter_set_object_name(widgetPath, -1, "tooltip");
		gtk_widget_path_iter_add_class(widgetPath, -1, GTK_STYLE_CLASS_BACKGROUND);
		gtk_widget_path_append_type(widgetPath, GTK_TYPE_LABEL);
		gtk_style_context_set_path(styleContextLabel, widgetPath);
		gtk_widget_path_free(widgetPath);
	}

	// Background color is taken from 'tooltip.background' css node
	GtkStyleContext* styleContextTooltip = gtk_style_context_new();
	{
		GtkWidgetPath* widgetPath = gtk_widget_path_new();
		gtk_widget_path_append_type(widgetPath, 0);
		gtk_widget_path_iter_set_object_name(widgetPath, -1, "tooltip");
		gtk_widget_path_iter_add_class(widgetPath, -1, GTK_STYLE_CLASS_BACKGROUND);
		gtk_style_context_set_path(styleContextTooltip, widgetPath);
		gtk_widget_path_free(widgetPath);
	}

	// Print
	printTooltipColors(styleContextLabel, styleContextTooltip, "GtkWidgetPath");

	// Destroy temporary style contexts
	g_object_unref(styleContextLabel);
	g_object_unref(styleContextTooltip);
#endif // GTK_CHECK_VERSION(3,20,0)
}

void getTooltipColors_GtkTooltipWindow()
{
#if GTK_CHECK_VERSION(3,19,2)
	if (0 != gtk_check_version(3, 19, 2))
	{
		// 'GtkTooltipWindow' was introduced in 3.19.2
		printf("With GtkTooltipWindow: Requires 3.19.2\n");
		return;
	}

	// Force 'GtkTooltip' to register 'GtkTooltipWindow' class
	{
		GType typeTooltip = gtk_tooltip_get_type();
		GObject* tooltip = (GObject*)g_object_new(typeTooltip, NULL);
		g_object_unref(tooltip);
	}

	// Obtain GType of non-exported type 'GtkTooltipWindow'
	GType typeTooltipWindow = g_type_from_name("GtkTooltipWindow");
	if (0 == typeTooltipWindow)
	{
		printf("With GtkTooltipWindow: g_type_from_name() failed\n");
		return;
	}

	// Create temporary tooltip
	GtkWidget* tooltip = (GtkWidget*)g_object_new(typeTooltipWindow, NULL);

	// Create temporary label in tooltip
	GtkWidget* tooltipLabel = (GtkWidget*)g_object_new(GTK_TYPE_LABEL, NULL);
	gtk_widget_set_parent(tooltipLabel, tooltip);

	// Obtain style contexts
	GtkStyleContext* styleContextLabel   = gtk_widget_get_style_context(tooltipLabel);
	GtkStyleContext* styleContextTooltip = gtk_widget_get_style_context(tooltip);

	// Print
	printTooltipColors(styleContextLabel, styleContextTooltip, "GtkTooltipWindow");

	// Also destroys child label
	gtk_widget_destroy(tooltip);
#endif // GTK_CHECK_VERSION(3,19,2)
}

void getTooltipColors_TooltipSetCustom()
{
	// Create a temporary tooltip
	GType typeTooltip = gtk_tooltip_get_type();
	GtkTooltip* tooltip = (GtkTooltip*)g_object_new(typeTooltip, NULL);

	// Add temporary label as custom control into tooltip
	GtkWidget* customLabel = (GtkWidget*)g_object_new(GTK_TYPE_LABEL, NULL);
	gtk_tooltip_set_custom(tooltip, customLabel);

	// Find tooltip window, using custom widget as entry point
	GtkWidget* tooltipBox    = gtk_widget_get_parent(customLabel);
	GtkWidget* tooltipWindow = gtk_widget_get_parent(tooltipBox);

	// Obtain style contexts
	// For label, just use temporary label; this is easier then finding the original label
	GtkStyleContext* styleContextLabel   = gtk_widget_get_style_context(customLabel);
	GtkStyleContext* styleContextTooltip = gtk_widget_get_style_context(tooltipWindow);

	// Print
	printTooltipColors(styleContextLabel, styleContextTooltip, "TooltipSetCustom");

	// Cleanup
	// customLabel is owned by tooltip and will be destroyed automatically
	g_object_unref(tooltip);
}

bool isThemeAvailable()
{
	const char* themeName = g_getenv("GTK_THEME");

	if (!themeName)
		return true;

	if (0 == strcasecmp(themeName, "Adwaita"))
		return true;

	// When GTK doesn't find a theme, it simply loads Adwaita.
	GtkCssProvider* selectedTheme = gtk_css_provider_get_named(themeName, NULL);
	GtkCssProvider* adwaitaTheme = gtk_css_provider_get_named("Adwaita", NULL);

	char* selectedThemeString = gtk_css_provider_to_string(selectedTheme);
	char* adwaitaThemeString = gtk_css_provider_to_string(adwaitaTheme);
	const bool isAdwaita =(0 == strcmp(selectedThemeString, adwaitaThemeString));
	g_free(selectedThemeString);
	g_free(adwaitaThemeString);

	if (isAdwaita)
	{
		printf("Error: Theme %s not found\n", themeName);
		return false;
	}

	return true;
}

int main(int argc, char* argv[])
{
	gtk_init(&argc, &argv);

	if (!isThemeAvailable())
		return 0;

	printf
	(
		"GTK version: %d.%d.%d\n",
		gtk_get_major_version(),
		gtk_get_minor_version(),
		gtk_get_micro_version()
	);

	// testColors();
	getTooltipColors_GtkWidgetPath();
	getTooltipColors_GtkTooltipWindow();
	getTooltipColors_TooltipSetCustom();

	return 0;
}

