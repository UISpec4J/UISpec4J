package org.uispec4j.xml;

import org.uispec4j.utils.Utils;

import java.io.IOException;
import java.io.Writer;

public class XmlWriter {

  public static Tag startTag(Writer writer, String rootTag) {
    try {
      writer.write('<');
      writer.write(rootTag);
      return new Tag(writer, NULL, rootTag);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static class Tag {
    private String tagValue;
    private Writer writer;
    private Tag parent;
    private boolean closed = false;

    private Tag(Writer writer, Tag parent, String tag) {
      this.writer = writer;
      this.parent = parent;
      this.tagValue = normalize(tag);
    }

    public Tag start(String tagName) {
      try {
        closeSup();
        writer.write(Utils.LINE_SEPARATOR);
        writer.write("<");
        writer.write(tagName);
        return new Tag(writer, this, tagName);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public Tag addAttribute(String attrName, String attrValue) {
      try {
        if (attrValue == null) {
          return this;
        }
        if (closed) {
          throw new RuntimeException("Bad use of 'addAttribute' method after tag closure");
        }
        writer.write(' ');
        writer.write(normalize(attrName));
        writer.write("=\"");
        writer.write(normalize(attrValue));
        writer.write('"');
        return this;
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public Tag end() {
      try {
        if (closed) {
          writer.write(Utils.LINE_SEPARATOR);
          writer.write("</");
          writer.write(tagValue);
          writer.write(">");
        }
        else {
          writer.write("/>");
        }
        return parent;
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    public Tag addValue(String value) {
      try {
        closeSup();
        writer.write(normalize(value));
        return this;
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private void closeSup() {
      try {
        if (!closed) {
          closed = true;
          writer.write(">");
        }
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private static String normalize(String value) {
      return XmlEscape.convertToXmlWithEntities(value);
    }
  }

  private static Tag NULL = new Tag(null, null, "") {
    public Tag addAttribute(String attrName, String attrValue) {
      return this;
    }

    public Tag end() {
      return this;
    }

    public Tag start(String tag) {
      return this;
    }
  };

  private XmlWriter() {
  }
}
