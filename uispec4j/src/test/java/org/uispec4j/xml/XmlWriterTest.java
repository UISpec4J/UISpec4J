package org.uispec4j.xml;

import org.uispec4j.utils.UnitTestCase;

import java.io.StringReader;
import java.io.StringWriter;

public class XmlWriterTest extends UnitTestCase {
  private StringWriter out = new StringWriter();

  public void testStartTag() throws Exception {
    XmlWriter.startTag(out, "Root")
      .addAttribute("toto", "titi")
      .start("Toto")
      .start("Titi").addAttribute("attr", "value").addValue("bbb").end()
      .start("A").end()
      .start("B").start("C").end().end()
      .end()
      .end()
      .end();

    SaxParser parser = new SaxParser();
    parser.parse(SilentNode.INSTANCE, new StringReader(out.toString()));
    String expectedXmlString =
      "<Root toto=\"titi\"> <Toto> <Titi attr=\"value\">bbb</Titi> <A/> <B> <C/> </B> </Toto> </Root>";
    XmlAssert.assertEquivalent(expectedXmlString, out.toString());
  }
}
