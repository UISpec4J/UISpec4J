module uispec4j {
  requires java.desktop;
  requires java.base;
  requires junit;
  requires asm;
  requires testng;

  exports org.uispec4j;
  exports org.uispec4j.utils;
  exports org.uispec4j.assertion;
  exports org.uispec4j.interception;
  exports org.uispec4j.xml;
  exports org.uispec4j.extension;
  exports org.uispec4j.finder;
  exports org.uispec4j.interception.ui;
}