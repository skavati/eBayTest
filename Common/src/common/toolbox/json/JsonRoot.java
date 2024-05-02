
package common.toolbox.json;

import static com.github.kvnxiao.jsonequals.Constants.BEGIN_BRACKET;
import static com.github.kvnxiao.jsonequals.Constants.BEGIN_CURLY;

import java.util.Map;
import java.util.Set;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyElement;
import me.doubledutch.lazyjson.LazyObject;
import me.doubledutch.lazyjson.LazyType;


public class JsonRoot {

  
  private final LazyElement root;
  
  private final LazyType rootType;

  // ------------
  // Constructors
  // ------------

  private JsonRoot(LazyElement root) {
    this.root = root;
    this.rootType = root.getType();
  }

  private JsonRoot(String raw) {
    if (raw.startsWith(BEGIN_CURLY)) {
      this.rootType = LazyType.OBJECT;
      this.root = new LazyObject(raw);
    } else if (raw.startsWith(BEGIN_BRACKET)) {
      this.rootType = LazyType.ARRAY;
      this.root = new LazyArray(raw);
    } else {
      // This shouldn't happen unless the specified string is not a valid JSON string!
      // The root of a JSON node should always be either an object or array
      this.rootType = LazyType.NULL;
      this.root = null;
    }
  }

  
  public static JsonRoot from(LazyElement root) {
    return new JsonRoot(root);
  }

  
  public static JsonRoot from(String raw) {
    return new JsonRoot(raw);
  }

  // ------------------
  // Comparator Methods
  // ------------------

  
  public JsonCompareResult compareTo(
      JsonRoot other, Set<String> ignoreFields, Map<String, String> pruneFields) {
    if (other != null) {
      if (this.isRootObject() && other.isRootObject()) {
        // JSON object node
        return JsonEquals.ofType(LazyType.OBJECT)
            .withSource(this.getRoot())
            .withComparate(other.getRoot())
            .withIgnoreFields(ignoreFields)
            .withPruneFields(pruneFields)
            .compare();
      } else if (this.isRootArray() && other.isRootArray()) {
        // JSON array node
        return JsonEquals.ofType(LazyType.ARRAY)
            .withSource(this.getRoot())
            .withComparate(other.getRoot())
            .withIgnoreFields(ignoreFields)
            .withPruneFields(pruneFields)
            .compare();
      }
    }
    return null;
  }

  
  public JsonCompareResult compareTo(JsonRoot other) {
    return compareTo(other, null, null);
  }

  
  public JsonCompareResult compareToWithIgnore(JsonRoot other, Set<String> ignoreFields) {
    return compareTo(other, ignoreFields, null);
  }

  
  public JsonCompareResult compareToWithPrune(JsonRoot other, Map<String, String> pruneFields) {
    return compareTo(other, null, pruneFields);
  }

  // -----------------
  // Getters / Setters
  // -----------------

  
  public LazyElement getRoot() {
    return this.root;
  }

  
  public boolean isRootObject() {
    return this.rootType == LazyType.OBJECT;
  }

  
  public boolean isRootArray() {
    return this.rootType == LazyType.ARRAY;
  }

  // ---------
  // Overrides
  // ---------

  
  @Override
  public String toString() {
    return this.root.toString();
  }
}
