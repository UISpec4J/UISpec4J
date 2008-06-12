package org.uispec4j.extension;

import org.uispec4j.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class StreamRecorder extends Thread {

  private InputStream inputStream;
  private StringBuffer buffer = new StringBuffer();

  public static StreamRecorder run(InputStream stream) {
    StreamRecorder recorder = new StreamRecorder(stream);
    recorder.start();
    return recorder;
  }

  private StreamRecorder(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public void run() {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(inputStream));
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (buffer.length() > 0) {
          buffer.append(Utils.LINE_SEPARATOR);
        }
        buffer.append(line);
      }
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
    finally {
      if (reader != null) {
        try {
          reader.close();
        }
        catch (IOException e) {
        }
      }
    }
  }

  public String getResult() throws InterruptedException {
    join();
    return buffer.toString();
  }
}
