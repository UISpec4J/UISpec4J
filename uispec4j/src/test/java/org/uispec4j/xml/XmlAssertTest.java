package org.uispec4j.xml;

import org.uispec4j.utils.AssertionFailureNotDetectedError;
import org.uispec4j.utils.UnitTestCase;

public class XmlAssertTest extends UnitTestCase {
  public void testNoDiff() throws Exception {
    String xml = "<root>" +
                 "  <child attr='1'>" +
                 "    <subChild attr='1.1'/>" +
                 "  </child>" +
                 "</root>";
    checkXml(xml, xml, true, true);
  }

  public void testDifferent() throws Exception {
    String xml1 = "<root>" +
                  "  <child attr='1'>" +
                  "    <subChild attr='1.1'/>" +
                  "  </child>" +
                  "</root>";
    String xml2 = "<root>" +
                  "  <child attr='1'>" +
                  "    <subChild attr='1.2'/>" +
                  "  </child>" +
                  "</root>";
    checkXml(xml1, xml2, false, false);
  }

  public void testChildrenInDifferentOrderAreEquivalentButNotEquals() throws Exception {
    String xml1 = "<hello><tag1/><tag2/><tag3/></hello>";
    String xml2 = "<hello><tag3/><tag1/><tag2/></hello>";
    checkXml(xml1, xml2, true, false);
  }

  public void testAttributeDifference() throws Exception {
    String xml1 = "<hello><tag1/><tag2 a=\"A\"/><tag3/></hello>";
    String xml2 = "<hello><tag3/><tag1/><tag2/></hello>";
    checkXml(xml1, xml2, false, false);
  }

  public void testAttributeInDifferentOrder() throws Exception {
    String xml1 = "<hello><tag1 a=\"A\" b=\"B\"/></hello>";
    String xml2 = "<hello><tag1 b=\"B\" a=\"A\"/></hello>";
    checkXml(xml1, xml2, true, true);
  }

  public void testAgainDifferentOrders() throws Exception {
    String xml1 =
      "<struct>" +
      "  <val1>0</val1>" +
      "  <val2>Paris-Tokyo</val2>" +
      "  <val1>1</val1>" +
      "</struct>";
    String xml2 =
      "<struct>" +
      "  <val2>Paris-Tokyo</val2>" +
      "  <val1>0</val1>" +
      "  <val1>1</val1>" +
      "</struct>";

    checkXml(xml1, xml2, true, false);
  }

  public void testIdenticalTagCountAreTakenIntoAccount() throws Exception {
    String xml1 =
      "<root>" +
      "  <child/>" +
      "  <child/>" +
      "</root>";
    String xml2 =
      "<root>" +
      "  <child/>" +
      "  <child/>" +
      "  <child/>" +
      "</root>";
    checkXml(xml1, xml2, false, false);
  }

  public void testSameCharacters() throws Exception {
    String xml1 = "<hello><tag1>XYZ</tag1></hello>";
    String xml2 = "<hello><tag1>XYZ</tag1></hello>";
    checkXml(xml1, xml2, true, true);
  }

  public void testWhitespace() throws Exception {
    String xml1 =
      "<hello>" +
      "  <list>" +
      "    <tag1>XYZ</tag1>" +
      "    <tag2>PQR</tag2>" +
      "    <tag3>" +
      "      <tag4/>" +
      "    </tag3>" +
      "  </list>" +
      "</hello>";
    String xml2 = "<hello><list><tag1>XYZ</tag1><tag2>PQR</tag2><tag3><tag4/></tag3></list></hello>";
    checkXml(xml1, xml2, true, true);
    checkXml(xml2, xml1, true, true);
  }

  public void testNotSameCharacters() throws Exception {
    String xml1 = "<hello><tag1>ABC</tag1></hello>";
    String xml2 = "<hello><tag1>XYZ</tag1></hello>";
    checkXml(xml1, xml2, false, false);
  }

  public void testSame() throws Exception {
    String xml1 = "<hello></hello>";
    String xml2 = "<hello/>";
    checkXml(xml1, xml2, true, true);
  }

  private void checkXml(String xmlA, String xmlB, boolean isEquivalent, boolean isEqual) throws Exception {
    if (isEqual) {
      assertTrue(isEquivalent);
      XmlAssert.assertEquivalent(xmlA, xmlB);
      XmlAssert.assertEquals(xmlA, xmlB);
    }
    else {
      if (!isEquivalent) {
        checkAssertEquivalentFailure(xmlA, xmlB);
      }
      else {
        XmlAssert.assertEquivalent(xmlA, xmlB);
      }
      checkAssertEqualsFailure(xmlA, xmlB);
    }
  }

  private void checkAssertEquivalentFailure(String xmlA, String xmlB) throws Exception {
    try {
      XmlAssert.assertEquivalent(xmlA, xmlB);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
    }
  }

  private void checkAssertEqualsFailure(String xmlA, String xmlB) throws Exception {
    try {
      XmlAssert.assertEquals(xmlA, xmlB);
      throw new AssertionFailureNotDetectedError();
    }
    catch (AssertionError e) {
    }
  }
}