package samples.calculator.functests;

import static org.testng.Assert.fail;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.uispec4j.assertion.UISpecAssert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Stage 3 in the evolution of a tests suite: data driven testing.
 */
public class CalculatorStage3Test {
  private Adapter adapter = new Adapter();
  private BufferedReader reader;


    @BeforeMethod()
    public void readTestData() throws Exception {
    InputStream inputStream = getClass().getResourceAsStream("testdata.txt");
    reader = new BufferedReader(new InputStreamReader(inputStream));
  }


    @AfterMethod()
    protected void closeReader() throws Exception {
    if (reader != null) {
      reader.close();
    }
  }


    @Test
    public void dataFromFile() throws Exception {
    int lineCount = 0;
    while (reader.ready()) {
      String line = reader.readLine();
      int commaIndex = line.indexOf(',');
      if (commaIndex == -1) {
        throw new RuntimeException("Error at line " + lineCount + ": no comma separator found");
      }
      String input = line.substring(0, commaIndex);
      String display = line.substring(commaIndex + 1, line.length());
      try {
        enterSequence(input);
        checkDisplay(display);
      }
      catch (AssertionError e) {
        fail("Error at line " + lineCount + " for input sequence <" + input + ">: "
             + e.getMessage());
      }
      lineCount++;
    }
  }

  public void enterSequence(String sequence) throws Exception {
    for (int i = 0, max = sequence.length(); i < max; i++) {
      adapter.getMainWindow().getButton(sequence.substring(i, i + 1)).click();
    }
  }

  public void checkDisplay(String display) throws Exception {
    assertTrue(adapter.getMainWindow().getTextBox().textEquals(display));
    adapter.getMainWindow().getButton("C").click();
  }
}
