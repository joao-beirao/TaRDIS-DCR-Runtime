package dcr1.model;

public abstract class GenericElement
    implements ModelElement {
  private final String elementId;

  // TODO seal

  public GenericElement(String elementId) {
    this.elementId = elementId;
  }

  @Override
  public String getElementId() {
    return elementId;
  }
}
