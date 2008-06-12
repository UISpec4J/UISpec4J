package org.uispec4j.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileTestUtils {
  private static final File TMP_DIR = new File("tmp");

  public static File getFile(String filename) {
    return new File(TMP_DIR, filename);
  }

  public static File dumpStringToFile(String filename, String content) throws Exception {
    File file = getFile(filename);
    dumpStringToFile(file, content);
    return file;
  }

  public static String loadTextFileToString(File file) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    try {
      return readerToString(reader);
    }
    finally {
      close(reader);
    }
  }

  private static void dumpStringToFile(File file, String content) throws Exception {
    prepareDir(file);
    file.createNewFile();

    // Necessary to ensure the file is fully created before writing on it ('createNewFile()' being asynchronous)
    Thread.sleep(10);

    Writer writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(file));
      writer.write(content);
      writer.flush();
    }
    finally {
      close(writer);
    }
  }

  private static String readerToString(Reader anyReader) throws Exception {
    BufferedReader reader = null;
    StringBuffer buffer;
    try {
      reader = new BufferedReader(anyReader);
      buffer = new StringBuffer();
      String line;
      boolean firstLine = true;
      while ((line = reader.readLine()) != null) {
        if (!firstLine) {
          buffer.append(Utils.LINE_SEPARATOR);
        }
        else {
          firstLine = false;
        }
        buffer.append(line);
      }
    }
    finally {
      close(reader, 10);
    }
    return buffer.toString();
  }

  private static void close(Writer writer) {
    if (writer != null) {
      try {
        writer.close();
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static void close(Reader reader) throws Exception {
    close(reader, 5);
  }

  private static void close(Reader reader, int delay) throws Exception {
    if (reader != null) {
      Thread.sleep(delay);
      reader.close();
    }
  }

  private static void prepareDir(File file) {
    File parentFile = file.getParentFile();
    if (parentFile != null) {
      parentFile.mkdirs();
    }
  }

  public static File createZipArchive(File archiveFile, File[] files) throws IOException {
    ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(archiveFile));
    byte data[] = new byte[2048];
    try {
      for (int i = 0; i < files.length; i++) {
        File file = files[i];
        FileInputStream inputStream = new FileInputStream(file);
        try {
          ZipEntry e = new ZipEntry(file.getName());
          outputStream.putNextEntry(e);
          int count;
          while ((count = inputStream.read(data, 0, 2048)) != -1) {
            outputStream.write(data, 0, count);
          }
          outputStream.closeEntry();
        }
        finally {
          inputStream.close();
        }
      }
    }
    finally {
      outputStream.close();
    }
    return archiveFile;
  }
}
