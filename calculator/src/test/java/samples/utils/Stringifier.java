package samples.utils;

public interface Stringifier {
  String toString(Object obj);

  Stringifier NULL = new Stringifier() {
    public String toString(Object obj) {
      return obj.toString();
    }
  };
}
