package org.uispec4j.xml;

import org.junit.Test;
import org.uispec4j.utils.UnitTestCase;

public class XmlEscapeTest extends UnitTestCase {
  @Test
  public void testConvertToXmlEntity() throws Exception {
    assertEquals("sdfsdf&amp;sdfsdf", XmlEscape.convertToXmlWithEntities("sdfsdf&sdfsdf"));
    assertEquals("sdfsdf&lt;sdf&gt;sdf", XmlEscape.convertToXmlWithEntities("sdfsdf<sdf>sdf"));
    assertEquals("sdf&quot;sdf&quot;sdfsdf", XmlEscape.convertToXmlWithEntities("sdf\"sdf\"sdfsdf"));
    assertEquals("sdfsdf&apos;sdfsdf", XmlEscape.convertToXmlWithEntities("sdfsdf'sdfsdf"));
    assertEquals("&amp;&lt;&gt;&apos;&quot;", XmlEscape.convertToXmlWithEntities("&<>'\""));
  }
}