
// Referer to sample project at "https://github.com/kvnxiao/jsonequals"
package common.toolbox.json;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import common.toolbox.json.JsonCompareResult;
import common.toolbox.json.JsonEquals;
import common.toolbox.json.JsonRoot;

public class IgnoreAndPruneTest {

  private static final String ignoreAndPruneA = "tests/ignore_prune_a.json";
  private static final String ignoreAndPruneB = "tests/ignore_prune_b.json";
  private static final String pruneA = "tests/prune_a.json";
  private static final String pruneb = "tests/prune_b.json";

  @Before
  public void enableDebug() {
    JsonEquals.setDebugMode(true);
  }

  //@Test
  public void pruneTest() throws IOException {
    String rawA = new String(Files.readAllBytes(Paths.get(pruneA)));
    String rawB = new String(Files.readAllBytes(Paths.get(pruneb)));
    JsonRoot jsonA = JsonRoot.from(rawA);
    JsonRoot jsonB = JsonRoot.from(rawB);

    Map<String, String> pruneFields = new HashMap<>();
    pruneFields.put("$.array[*]:id.isValid", "false");

    JsonCompareResult result = jsonA.compareToWithPrune(jsonB, pruneFields);
    //result.getInequalityMessages().forEach(System.out::println);

    assertTrue(result.isEqual());
  }

  @Test
  public void ignoreAndPruneTest() throws IOException {
   String rawA = new String(Files.readAllBytes(Paths.get(ignoreAndPruneA)));
    String rawB = new String(Files.readAllBytes(Paths.get(ignoreAndPruneB)));
    JsonRoot jsonA = JsonRoot.from(rawA);
    JsonRoot jsonB = JsonRoot.from(rawB);

    Set<String> ignoreFields = new HashSet<>();
    Map<String, String> pruneFields = new HashMap<>();

    ignoreFields.add("$[*].data.last_updated");
    pruneFields.put("$[*].data.identities[*]:installed", "false");

    JsonCompareResult result = jsonA.compareTo(jsonB, ignoreFields, pruneFields);
   // result.getInequalityMessages().forEach(System.out::println);

    assertTrue(result.isEqual());
  }
  
  // ************ Sarath added ***************
 // @Test
  public void ignoreFieldTest() throws IOException {
	 String ignoreAndPruneA = "C:\\Users\\c70032.CLIENT\\Automation\\JSON_Compare\\Expected.json";
	 String ignoreAndPruneB = "C:\\Users\\c70032.CLIENT\\Automation\\JSON_Compare\\Actual.json";
     String rawA = new String(Files.readAllBytes(Paths.get(ignoreAndPruneA)));
    String rawB = new String(Files.readAllBytes(Paths.get(ignoreAndPruneB)));
    JsonRoot jsonA = JsonRoot.from(rawA);
    JsonRoot jsonB = JsonRoot.from(rawB);

    Set<String> ignoreFields = new HashSet<>();
    Map<String, String> pruneFields = new HashMap<>();

    ignoreFields.add("$.successResponseSegment.referenceNumberCLAS");
   // pruneFields.put("$[*].data.identities[*]:installed", "false");

    //JsonCompareResult result = jsonA.compareTo(jsonB, ignoreFields, pruneFields);
    JsonCompareResult result = jsonA.compareToWithIgnore(jsonB, ignoreFields);
    result.getInequalityMessages().forEach(System.out::println);

   // assertTrue(result.isEqual());
  }
}
