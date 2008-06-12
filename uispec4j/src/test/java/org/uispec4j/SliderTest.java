package org.uispec4j;

import org.uispec4j.assertion.Assertion;
import org.uispec4j.utils.Functor;
import org.uispec4j.xml.XmlAssert;

import javax.swing.*;
import java.util.Hashtable;

public class SliderTest extends UIComponentTestCase {
  private JSlider jComponent = createThermometer();
  private Slider thermometer = new Slider(jComponent);

  public void testGetComponentTypeName() throws Exception {
    assertEquals("slider", thermometer.getDescriptionTypeName());
  }

  public void testGetDescription() throws Exception {
    XmlAssert.assertEquivalent("<slider name='my thermometer'/>", thermometer.getDescription());
  }

  public void testFactory() throws Exception {
    checkFactory(createThermometer(), Slider.class);
  }

  protected UIComponent createComponent() {
    return thermometer;
  }

  public void testLabels() throws Exception {
    checkAssertionFails(thermometer.labelsEqual(new String[]{"1", "2", "3", "4"}),
                        "4 elements instead of 6\n" +
                            "Expected: [1,2,3,4],\n" +
                            "but was: [-10,0,10,20,30,40]");

    assertTrue(thermometer.labelsEqual(new String[]{"-10", "0", "10", "20", "30", "40"}));

    changeLabels();

    checkAssertionFails(thermometer.labelsEqual(new String[]{"1", "2", "3", "4", "5"}),
                        "Unexpected element 'Very Cold'\n" +
                            "Expected: [1,2,3,4,5],\n" +
                            "but was: [Very Cold,Cold,Cool,Warm,Hot]");
    checkAssertionFails(thermometer.labelsEqual(new String[]{"Very Cold", "Cold", "Cool", "Hot", "Warm"}),
                        "Unexpected order in the collection\n" +
                            "Expected: [Very Cold,Cold,Cool,Hot,Warm],\n" +
                            "but was: [Very Cold,Cold,Cool,Warm,Hot]");
    assertTrue(thermometer.labelsEqual(new String[]{"Very Cold", "Cold", "Cool", "Warm", "Hot"}));

  }

  public void testPositionCheckBasedOnLabels() throws Exception {
    changeLabels();

    jComponent.setValue(10);
    checkPosition(10, "Cool", "Warm");

    thermometer.setPosition("Warm");
    checkPosition(20, "Warm", "Cool");

    thermometer.setPosition("Cold");
    checkPosition(0, "Cold", "Hot");

    checkException(new Functor() {
      public void run() throws Exception {
        thermometer.setPosition("unexisting");
      }
    }, "No label 'unexisting' has been found");
    checkPosition(0, "Cold", "Hot");
  }

  public void testRelativePosition() throws Exception {
    jComponent.setValue(16);
    Assertion positionIsMiddle = thermometer.relativePositionEquals(50);
    assertTrue(positionIsMiddle);

    jComponent.setValue(17);
    checkAssertionFails(positionIsMiddle, "Expected 50 but was 54");

    thermometer.setPrecision(5);
    assertTrue(positionIsMiddle);
  }

  private void checkPosition(int intValue, String correctLabel, String wrongLabel) throws Exception {
    assertEquals(intValue, jComponent.getValue());
    assertTrue(thermometer.positionEquals(correctLabel));
    checkAssertionFails(thermometer.positionEquals(wrongLabel), "expected:<" + wrongLabel + "> but was:<" + correctLabel + ">");
  }

  private static JSlider createThermometer() {
    JSlider thermometer = new JSlider(JSlider.HORIZONTAL, -10, 40, 0);
    thermometer.setName("my thermometer");
    thermometer.setMajorTickSpacing(10);
    thermometer.setPaintLabels(true);
    thermometer.setPaintTicks(true);
    return thermometer;
  }

  private void changeLabels() {
    Hashtable table = new Hashtable();
    table.put(new Integer(-10), new JLabel("Very Cold"));
    table.put(new Integer(0), new JLabel("Cold"));
    table.put(new Integer(10), new JLabel("Cool"));
    table.put(new Integer(20), new JLabel("Warm"));
    table.put(new Integer(40), new JLabel("Hot"));
    jComponent.setLabelTable(table);
  }
}