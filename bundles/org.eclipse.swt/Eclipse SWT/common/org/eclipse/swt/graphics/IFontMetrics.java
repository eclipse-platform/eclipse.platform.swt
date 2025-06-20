package org.eclipse.swt.graphics;

public interface IFontMetrics {
 int getAscent();
 int getDescent();
 int getHeight();
 int getLeading();
double getAverageCharacterWidth();
/**
 * Returns the average character width, measured in points,
 * of the font described by the receiver.
 *
 * @return the average character width of the font
 * @deprecated Use getAverageCharacterWidth() instead
 */
@Deprecated
int getAverageCharWidth();
}