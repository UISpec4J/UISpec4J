module uispec4j {
  requires java.desktop;
  requires java.base;
  requires junit;
  requires asm;
  requires testng;

  exports org.uispec4j to junit;
  exports org.uispec4j.utils to junit;
  exports org.uispec4j.assertion to junit;
  exports org.uispec4j.interception to junit;
  exports org.uispec4j.xml to junit;
  exports org.uispec4j.extension to junit;
  exports org.uispec4j.finder to junit;
}