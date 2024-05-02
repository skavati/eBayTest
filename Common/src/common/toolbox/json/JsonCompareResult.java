
package common.toolbox.json;

import java.util.List;


public class JsonCompareResult {

  private final boolean isEqual;
  private final List<String> successMessages;
  private final List<String> inequalityMessages;

  private JsonCompareResult(
      boolean isEqual, List<String> successMessages, List<String> inequalityMessages) {
    this.isEqual = isEqual;
    this.successMessages = successMessages;
    this.inequalityMessages = inequalityMessages;
  }

  public static JsonCompareResult of(
      boolean isEqual, List<String> successMessages, List<String> inequalityMessages) {
    return new JsonCompareResult(isEqual, successMessages, inequalityMessages);
  }

  public List<String> getSuccessMessages() {
    return successMessages;
  }

  public List<String> getInequalityMessages() {
    return inequalityMessages;
  }

  public boolean isEqual() {
    return isEqual;
  }

  public int getSuccessCount() {
    return successMessages.size();
  }

  public int getInequalityCount() {
    return inequalityMessages.size();
  }

  public int getTotalMessageCount() {
    return successMessages.size() + inequalityMessages.size();
  }
}
