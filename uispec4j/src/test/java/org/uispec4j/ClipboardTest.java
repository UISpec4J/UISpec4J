package org.uispec4j;

import org.uispec4j.utils.UnitTestCase;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class ClipboardTest extends UnitTestCase {
  private static final String CONTENT_STRING = "content";

  public void testSimplePutText() throws Exception {
    checkSimplePutText(CONTENT_STRING);
    checkSimplePutText("hello\t world\n bye!");
    checkSimplePutText("1\t34");
  }

  public void testPutTextWithHtmlUtf8MimeTypeAndAnInputStreamTransferClass() throws Exception {
    checkPutTextWithMimeType(Clipboard.HTML, Clipboard.UTF8, Clipboard.INPUT_STREAM,
                             "text/html; charset=UTF-8; class=java.io.InputStream",
                             new ContentChecker() {
                               public void check(Object content) throws Exception {
                                 InputStream stream = (InputStream)content;
                                 DataInputStream data = new DataInputStream(stream);
                                 assertEquals(CONTENT_STRING, data.readLine());
                                 assertEquals(-1, data.read());
                                 data.close();
                               }
                             });
  }

  public void testPutTextWithPlainAsciiMimeTypeAndACharBufferTransferClass() throws Exception {
    checkPutTextWithMimeType(Clipboard.PLAIN, Clipboard.US_ASCII, Clipboard.CHAR_BUFFER,
                             "text/plain; charset=US-ASCII; class=java.nio.CharBuffer",
                             new ContentChecker() {
                               public void check(Object content) throws Exception {
                                 CharBuffer buffer = (CharBuffer)content;
                                 assertEquals(CONTENT_STRING, buffer.toString());
                               }
                             });
  }

  public void testPutTextWithPlainUnicodeMimeTypeAndAReadaerTransferClass() throws Exception {
    checkPutTextWithMimeType(Clipboard.PLAIN, Clipboard.UNICODE, Clipboard.READER,
                             "text/plain; charset=unicode; class=java.io.Reader",
                             new ContentChecker() {
                               public void check(Object content) throws Exception {
                                 Reader reader = (Reader)content;
                                 char[] chars = new char[CONTENT_STRING.length()];
                                 reader.read(chars);
                                 assertEquals(CONTENT_STRING, new String(chars));
                                 assertEquals(-1, reader.read());
                                 reader.close();
                               }
                             });
  }

  public void testPutTextWithHtmlUtf16MimeTypeAndAByteBufferTransferClass() throws Exception {
    checkPutTextWithMimeType(Clipboard.HTML, Clipboard.UTF16, Clipboard.BYTE_BUFFER,
                             "text/html; charset=UTF-16; class=java.nio.ByteBuffer",
                             new ContentChecker() {
                               public void check(Object content) throws Exception {
                                 ByteBuffer buffer = (ByteBuffer)content;
                                 assertEquals(CONTENT_STRING, new String(buffer.array()));
                               }
                             });
  }

  private void checkPutTextWithMimeType(Clipboard.TextType type,
                                        Clipboard.Charset charset,
                                        Clipboard.TransferType transferType,
                                        String expectedMimeType,
                                        ContentChecker contentChecker) throws Exception {
    Clipboard.putText(type, charset, transferType, CONTENT_STRING);
    Transferable transferable = getSystemClipboard().getContents(null);
    DataFlavor[] flavors = transferable.getTransferDataFlavors();
    assertEquals(1, flavors.length);
    DataFlavor flavor = flavors[0];
    if (!flavor.isMimeTypeEqual(expectedMimeType)) {
      assertEquals(expectedMimeType, flavor.getMimeType());
    }
    Object content = transferable.getTransferData(flavor);
    assertTrue(transferType.getDataClass().isInstance(content));
    contentChecker.check(content);
  }

  private interface ContentChecker {
    void check(Object content) throws Exception;
  }

  private void checkSimplePutText(String data) throws Exception {
    Clipboard.putText(data);
    String actual = (String)getSystemClipboard().getContents(this).getTransferData(DataFlavor.stringFlavor);
    assertEquals(data, actual);
  }

  private java.awt.datatransfer.Clipboard getSystemClipboard() {
    return Toolkit.getDefaultToolkit().getSystemClipboard();
  }
}