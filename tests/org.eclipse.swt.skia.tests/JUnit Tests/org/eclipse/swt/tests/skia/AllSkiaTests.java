package org.eclipse.swt.tests.skia;

import org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawString;
import org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawText;
import org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawText_NoCache;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ Test_org_eclipse_swt_skia_RectangleConverter.class, //
		Test_org_eclipse_swt_skia_RGBAEncoder.class, //
		Test_org_eclipse_swt_skia_SkijaToSwtImageConvert.class, //
		Test_org_eclipse_swt_skia_SkijaToSwtImageConverter.class, //
		org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawLine.class, //
		org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawArc.class, //
		org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawOval.class, //
		org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawPath.class, //
		org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawPoint.class, //
		org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawPolygon.class, //
		org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawPolyline.class, //
		org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawRectangle.class, //
		org.eclipse.swt.tests.doubles.Test_org_eclipse_swt_skia_drawRoundRectangle.class, //
		Test_org_eclipse_swt_skia_drawString.class, //
		Test_org_eclipse_swt_skia_drawText.class, //
		Test_org_eclipse_swt_skia_drawText_NoCache.class, //

})
public class AllSkiaTests {

}