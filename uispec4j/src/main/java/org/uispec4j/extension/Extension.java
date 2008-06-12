package org.uispec4j.extension;

class Extension {
  private String componentName;
  private String componentClassName;

  public Extension(String componentName, String componentClassName) {
    this.componentName = componentName;
    this.componentClassName = componentClassName;
  }

  public String getComponentName() {
    return componentName;
  }

  public String getComponentClassName() {
    return componentClassName;
  }
}
