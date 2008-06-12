package org.uispec4j.xml;

public class XmlEscape {
  private static final int AMP_CHARACTER = '&';
  private static final int LT_CHARACTER = '<';
  private static final int GT_CHARACTER = '>';
  private static final int QUOT_CHARACTER = '"';
  private static final int APOS_CHARACTER = '\'';

  private static final String AMP_ENTITY = "&amp;";
  private static final String LT_ENTITY = "&lt;";
  private static final String GT_ENTITY = "&gt;";
  private static final String QUOT_ENTITY = "&quot;";
  private static final String APOS_ENTITY = "&apos;";

  public static String convertToXmlWithEntities(String s) {
    if (s.indexOf(AMP_CHARACTER) != -1) {
      s = s.replaceAll("&", AMP_ENTITY);
    }
    if (s.indexOf(LT_CHARACTER) != -1) {
      s = s.replaceAll("<", LT_ENTITY);
    }
    if (s.indexOf(GT_CHARACTER) != -1) {
      s = s.replaceAll(">", GT_ENTITY);
    }
    if (s.indexOf(QUOT_CHARACTER) != -1) {
      s = s.replaceAll("\"", QUOT_ENTITY);
    }
    if (s.indexOf(APOS_CHARACTER) != -1) {
      s = s.replaceAll("'", APOS_ENTITY);
    }
    return s;
  }

  public static String convertXmlEntitiesToText(String s) {
    if (s.indexOf(AMP_ENTITY) != -1) {
      s = s.replaceAll(AMP_ENTITY, "&");
    }
    if (s.indexOf(LT_ENTITY) != -1) {
      s = s.replaceAll(LT_ENTITY, "<");
    }
    if (s.indexOf(GT_ENTITY) != -1) {
      s = s.replaceAll(GT_ENTITY, ">");
    }
    if (s.indexOf(QUOT_ENTITY) != -1) {
      s = s.replaceAll(QUOT_ENTITY, "\"");
    }
    if (s.indexOf(APOS_ENTITY) != -1) {
      s = s.replaceAll(APOS_ENTITY, "'");
    }
    return s;
  }
}
