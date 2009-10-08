package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.assertion.UISpecAssert;
import org.uispec4j.assertion.testlibrairies.AssertAdapter;
import org.uispec4j.utils.Utils;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TextBoxHandlerForHtmlTextComponent extends AbstractTextBoxHandlerForTextComponent {
  public static final Pattern HYPERLINK_PATTERN = Pattern
    .compile("<(a|A)[ ]+([a-zA-Z0-9]+=\"[^\"]+\"[ ]+)*href=\"([^\"]+)\"([ ]+[a-zA-Z0-9]+=\"[^\"]+\"[ ]*)*>([^\"]+)</(a|A)>");

  public static TextBox.Handler init(JTextComponent textComponent) {
    return (accept(textComponent)) ? new TextBoxHandlerForHtmlTextComponent(textComponent) : null;
  }

  private TextBoxHandlerForHtmlTextComponent(JTextComponent textComponent) {
    super(textComponent);
  }

  public Assertion textIsEmpty() {
    return new Assertion() {
      public void check() {
        String actualText = jTextComponent.getText();
        JTextPane dummyPane = createHtmlTextPane();
        if (isEquivalent(dummyPane, actualText)) {
          return;
        }

        dummyPane.setText("<html><p></html>");
        dummyPane.setText("");
        if (isEquivalent(dummyPane, actualText)) {
          return;
        }

        dummyPane.setText("<p></p>");
        if (isEquivalent(dummyPane, actualText)) {
          return;
        }

        AssertAdapter.fail("Text should be empty but contains: " + actualText);
      }
    };
  }

  private boolean isEquivalent(JTextPane dummyPane, String actualText) {
    try {
      XmlAssert.assertEquivalent(dummyPane.getText(), actualText);
      return true;
    }
    catch (Error e) {
      return false;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Assertion textEquals(final String text) {
    return new Assertion() {
      public void check() {
        String actual = Utils.cleanupHtml(jTextComponent.getText());
        AssertAdapter.assertEquals(text, actual);
      }
    };
  }

  public Assertion htmlEquals(final String html) {
    return new Assertion() {
      public void check() {
        if (html.equals("")) {
          textIsEmpty();
          return;
        }
        JTextPane dummyPane = createHtmlTextPane();
        dummyPane.setText(html);
        AssertAdapter.assertEquals(dummyPane.getText(), jTextComponent.getText());
      }
    };
  }

  public void clickOnHyperlink(String link) {
    HyperLinkFoundAssertion hyperLinkAssertion = new HyperLinkFoundAssertion(link);
    UISpecAssert.assertTrue(hyperLinkAssertion);
    dispatchHyperlinkEvent(hyperLinkAssertion.getHref());
  }

  private void dispatchHyperlinkEvent(String href) {
    URL url;
    try {
      JEditorPane editorPane = (JEditorPane)jTextComponent;
      url = new URL(editorPane.getPage(), href);
    }
    catch (MalformedURLException e) {
      url = null;
    }
    HyperlinkEvent event =
      new HyperlinkEvent(jTextComponent,
                         HyperlinkEvent.EventType.ACTIVATED,
                         url,
                         href);
    JEditorPane editorPane = (JEditorPane)jTextComponent;
    HyperlinkListener[] listeners = editorPane.getHyperlinkListeners();
    for (HyperlinkListener listener : listeners) {
      listener.hyperlinkUpdate(event);
    }
  }

  private JTextPane createHtmlTextPane() {
    JTextPane dummyPane = new JTextPane();
    dummyPane.setContentType("text/html; charset=UTF-8");
    return dummyPane;
  }

  private static boolean accept(JTextComponent jTextComponent) {
    if (!JEditorPane.class.isInstance(jTextComponent)) {
      return false;
    }
    JEditorPane editorPane = (JEditorPane)jTextComponent;
    return editorPane.getEditorKit().getContentType().startsWith("text/html");
  }

  private class HyperLinkFoundAssertion extends Assertion {
    private String link;
    private String href;

    public HyperLinkFoundAssertion(String link) {
      this.link = link;
    }

    public String getHref() {
      return href;
    }

    public void check() {
      href = getHyperLink(link);
    }

    private String getHyperLink(String link) {
      String text = jTextComponent.getText();
      Matcher matcher = HYPERLINK_PATTERN.matcher(text);
      String lowerCaseLink = link.toLowerCase();
      List<String> exactResults = new ArrayList<String>();
      List<String> approximativeResults = new ArrayList<String>();
      while (matcher.find()) {
        String label = matcher.group(5)
          .replaceAll(Utils.LINE_SEPARATOR, "")
          .replaceAll("\n", "")
          .toLowerCase()
          .replaceAll("[ ]+", " ")
          .trim();
        String href = matcher.group(3);
        if (label.equals(lowerCaseLink)) {
          exactResults.add(href);
        }
        else if (label.indexOf(lowerCaseLink) != -1) {
          approximativeResults.add(href);
        }
      }
      if ((exactResults.isEmpty()) && (approximativeResults.isEmpty())) {
        AssertAdapter.fail("Hyperlink '" + link + "' not found");
      }
      if ((exactResults.size() > 1) || (approximativeResults.size() > 1)) {
        AssertAdapter.fail("Ambiguous command - found several hyperlinks matching '" + link + "'");
      }
      return (exactResults.size() == 1) ? exactResults.get(0) : approximativeResults.get(0);
    }
  }
}
