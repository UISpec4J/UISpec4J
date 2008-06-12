package samples.calculator.functests;

import org.uispec4j.UISpec4J;
import org.uispec4j.interception.MainClassAdapter;
import samples.calculator.CalculatorPanel;

public class Adapter extends MainClassAdapter {

  static {
    UISpec4J.init();
  }

  public Adapter() {
    super(CalculatorPanel.class);
  }
}
