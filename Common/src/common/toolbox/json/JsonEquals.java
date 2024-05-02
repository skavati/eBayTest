
package common.toolbox.json;

import static com.github.kvnxiao.jsonequals.Constants.BEGIN_BRACKET;
import static com.github.kvnxiao.jsonequals.Constants.END_BRACKET;
import static com.github.kvnxiao.jsonequals.Constants.NODE_SEPARATOR;
import static com.github.kvnxiao.jsonequals.Constants.PREDICATE_SEPARATOR;
import static com.github.kvnxiao.jsonequals.Constants.ROOT_NAME;
import static com.github.kvnxiao.jsonequals.Constants.SEPARATOR_REGEX;
import static com.github.kvnxiao.jsonequals.Constants.WILDCARD;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.doubledutch.lazyjson.LazyArray;
import me.doubledutch.lazyjson.LazyElement;
import me.doubledutch.lazyjson.LazyObject;
import me.doubledutch.lazyjson.LazyType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonEquals {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonEquals.class);
	private static boolean debugMode = false;

	private final LazyType rootType;
	private LazyElement source = null;
	private LazyElement comparate = null;
	private Map<String, String> pruneFields;
	private Set<String> ignoreFields;
	private List<String> successMessages;
	private List<String> inequalityMessages;

	private JsonEquals(LazyType rootType) {
		this.rootType = rootType;
		this.successMessages = new ArrayList<>();
		this.inequalityMessages = new ArrayList<>();
	}

	public static JsonEquals ofType(LazyType rootType) {
		return new JsonEquals(rootType);
	}

	public static JsonEquals between(LazyObject source, LazyObject comparate) {
		return new JsonEquals(LazyType.OBJECT).withSource(source).withComparate(comparate);
	}

	public static JsonEquals between(LazyArray source, LazyArray comparate) {
		return new JsonEquals(LazyType.ARRAY).withSource(source).withComparate(comparate);
	}

	public JsonEquals withSource(LazyElement source) {
		this.source = source;
		return this;
	}

	public JsonEquals withComparate(LazyElement comparate) {
		this.comparate = comparate;
		return this;
	}

	public JsonEquals withIgnoreFields(Set<String> ignoreFields) {
		this.ignoreFields = ignoreFields;
		return this;
	}

	public JsonEquals withPruneFields(Map<String, String> pruneFields) {
		this.pruneFields = pruneFields;
		return this;
	}

	public JsonCompareResult compare() {
		if (rootType == LazyType.OBJECT) {
			compareNode((LazyObject) source, (LazyObject) comparate);
		} else {
			compareNode((LazyArray) source, (LazyArray) comparate);
		}
		return JsonCompareResult.of(inequalityMessages.isEmpty(), successMessages, inequalityMessages);
	}

	public void compareNode(LazyObject a, LazyObject b) {
		compareNode(a, b, ROOT_NAME);
	}

	public void compareNode(LazyArray a, LazyArray b) {
		compareNode(a, b, ROOT_NAME);
	}

	public void compareNode(LazyObject a, LazyObject b, String currentPath) {
		if (pathIsIgnoreField(currentPath)) {
			return;
		}

		Set<String> fieldsA = a.keySet();
		Set<String> fieldsB = b.keySet();
		if (fieldsA.equals(fieldsB)) {
			for (String fieldName : fieldsA) {
				if (childIsObject(a, fieldName) && childIsObject(b, fieldName)) {
					compareNode(a.getJSONObject(fieldName), b.getJSONObject(fieldName),
							getChildPath(currentPath, fieldName));
				} else if (childIsArray(a, fieldName) && childIsArray(b, fieldName)) {
					compareNode(a.getJSONArray(fieldName), b.getJSONArray(fieldName),
							getChildPath(currentPath, fieldName));
				} else {
					compareValues(a, b, fieldName, getChildPath(currentPath, fieldName));
				}
			}
		} else {
			inequalityMessages.add("JSON objects do not have the same child key names! " + fieldsA + " vs. " + fieldsB);
		}
	}

	public void compareNode(LazyArray a, LazyArray b, String currentPath) {
		if (pathIsIgnoreField(currentPath)) {
			return;
		}

		JsonChildren childrenA = getChildList(a);
		JsonChildren childrenB = getChildList(b);
		if (pruneFields != null && !pruneFields.isEmpty()) {
			prune(childrenA, currentPath, "source");
			prune(childrenB, currentPath, "comparate");
		}
		if (!childrenA.isEmpty() && !childrenB.isEmpty()) {
			if (childrenA.objectCount() == childrenB.objectCount() && childrenA.arrayCount() == childrenB.arrayCount()
					&& childrenA.valueCount() == childrenB.valueCount()) {
				for (int i = 0; i < childrenA.size(); i++) {

					if (childrenA.getType(i) == JsonChildren.Type.OBJECT
							&& childrenB.getType(i) == JsonChildren.Type.OBJECT) {
						// Compare child objects
						compareNode(childrenA.getObj(i), childrenB.getObj(i),
								currentPath + BEGIN_BRACKET + i + END_BRACKET);

					} else if (childrenA.getType(i) == JsonChildren.Type.ARRAY
							&& childrenB.getType(i) == JsonChildren.Type.ARRAY) {
						// Compare child arrays
						compareNode(childrenA.getArr(i), childrenB.getArr(i),
								currentPath + BEGIN_BRACKET + i + END_BRACKET);

					} else if (childrenA.getType(i) == JsonChildren.Type.VALUE
							&& childrenB.getType(i) == JsonChildren.Type.VALUE) {
						// Compare primitive values
						if (debugMode) {
							LOGGER.debug("Checking array value: {}", currentPath + BEGIN_BRACKET + i + END_BRACKET);
						}
						if (!childrenA.get(i).equals(childrenB.get(i))) {
							inequalityMessages.add(currentPath + " JSON array value expected to be " + childrenA.get(i)
									+ " but got " + childrenB.get(i));
						} else {
							successMessages
									.add(currentPath + BEGIN_BRACKET + i + END_BRACKET + "==" + childrenA.get(i));
						}
					} else {
						inequalityMessages.add(
								currentPath + BEGIN_BRACKET + i + END_BRACKET + " types were not the same! Expected "
										+ childrenA.getType(i) + " but got " + childrenB.getType(i));
					}
				}
			} else {
				inequalityMessages.add(currentPath + " JSON array not equal in length! " + childrenA.size() + " vs "
						+ childrenB.size());
			}
		}
	}

	public void compareValues(LazyObject a, LazyObject b, String fieldName, String currentPath) {
		if (pathIsIgnoreField(currentPath)) {
			return;
		}

		if (debugMode) {
			LOGGER.debug("Checking leaf object: {}", currentPath);
		}

		if (a.getType(fieldName) == LazyType.STRING && b.getType(fieldName) == LazyType.STRING) {
			if (a.getString(fieldName).equals(b.getString(fieldName))) {
				successMessages.add(currentPath + "==" + a.getString(fieldName));
			} else {
				logInequality(a.getString(fieldName), b.getString(fieldName), currentPath);
			}
		} else if (a.getType(fieldName) == LazyType.INTEGER && b.getType(fieldName) == LazyType.INTEGER) {
			if (a.getInt(fieldName) == b.getInt(fieldName)) {
				successMessages.add(currentPath + "==" + a.getString(fieldName));
			} else {
				logInequality(a.getString(fieldName), b.getString(fieldName), currentPath);
			}
		} else if (a.getType(fieldName) == LazyType.BOOLEAN && b.getType(fieldName) == LazyType.BOOLEAN) {
			if (a.getBoolean(fieldName) == b.getBoolean(fieldName)) {
				successMessages.add(currentPath + "==" + a.getString(fieldName));
			} else {
				logInequality(a.getString(fieldName), b.getString(fieldName), currentPath);
			}
		} else if (a.getType(fieldName) == LazyType.FLOAT && b.getType(fieldName) == LazyType.FLOAT) {

			DecimalFormat df = new DecimalFormat("0.00");
			LOGGER.info("Expected node value: " + df.format(Float.valueOf(a.getString(fieldName))));
			LOGGER.info("Actual node value: " + (b.getString(fieldName)));

			if (df.format(Float.valueOf(a.getString(fieldName)))
					.equals(df.format(Float.valueOf(b.getString(fieldName))))) {
				successMessages.add(currentPath + "==" + a.getString(fieldName));
			} else {
				logInequality(a.getString(fieldName), b.getString(fieldName), currentPath);
			}
		} else if (a.getType(fieldName) == LazyType.NULL && b.getType(fieldName) == LazyType.NULL) {
			successMessages.add(currentPath + "==" + a.getString(fieldName));
			// Below coded added by Sarath to compare expected node type "Integer" with
			// actual "Float"
		} else if (a.getType(fieldName) == LazyType.INTEGER && b.getType(fieldName) == LazyType.FLOAT) {
			DecimalFormat df = new DecimalFormat("0.00");
			LOGGER.info("Expected node value: " + df.format(Float.valueOf(a.getString(fieldName))));
			LOGGER.info("Actual node value: " + (b.getString(fieldName)));

			if (df.format(Float.valueOf(a.getString(fieldName)))
					.equals(df.format(Float.valueOf(b.getString(fieldName))))) {
				successMessages.add(currentPath + "==" + a.getString(fieldName));
			} else {
				logInequality(a.getString(fieldName), b.getString(fieldName), currentPath);
			}
		} else {
			inequalityMessages.add(currentPath + " were not of the same type! Expected type " + a.getType(fieldName)
					+ " but got type " + b.getType(fieldName));
		}
	}

	private JsonChildren getChildList(LazyArray parent) {
		JsonChildren jsonChildren = JsonChildren.create();
		for (int i = 0; i < parent.length(); i++) {
			switch (parent.getType(i)) {
			case OBJECT:
				jsonChildren.addChildObject(parent.getJSONObject(i));
				break;
			case ARRAY:
				jsonChildren.addChildArray(parent.getJSONArray(i));
				break;
			case STRING:
				jsonChildren.addChildValue(parent.getString(i));
				break;
			case INTEGER:
				jsonChildren.addChildValue(parent.getInt(i));
				break;
			case BOOLEAN:
				jsonChildren.addChildValue(parent.getBoolean(i));
				break;
			case FLOAT:
				jsonChildren.addChildValue(parent.getDouble(i));
				break;
			default:
				jsonChildren.addChildValue(null);
				break;
			}
		}
		return jsonChildren;
	}

	private void prune(JsonChildren children, String currentPath, String identifier) {
		List<Object> childNodes = children.getChildren();
		List<JsonChildren.Type> childTypes = children.getChildrenTypes();
		Iterator<Object> childIterator = childNodes.iterator();
		Iterator<JsonChildren.Type> childTypesIterator = childTypes.iterator();
		int i = 0;
		for (; childIterator.hasNext() && childTypesIterator.hasNext();) {
			Object child = childIterator.next();
			JsonChildren.Type childType = childTypesIterator.next();
			if (childType == JsonChildren.Type.OBJECT
					&& pathIsPruneField(currentPath + BEGIN_BRACKET + i + END_BRACKET, (LazyObject) child)) {
				if (debugMode) {
					LOGGER.debug("Pruning");
				}
				childIterator.remove();
				childTypesIterator.remove();
				children.decrementObjCount();
			}
			i++;
		}
	}

	private boolean pathIsPruneField(String currentPath, LazyObject node) {
		for (Map.Entry<String, String> pruneEntry : pruneFields.entrySet()) {
			String key = pruneEntry.getKey();
			String value = pruneEntry.getValue();

			String[] fields = key.split(PREDICATE_SEPARATOR);
			if (fields.length == 0 || fields.length > 2) {
				// Incorrect formatting
				return false;
			}
			if (pathEquals(currentPath, fields[0])) {
				LazyObject currentNode = node;
				String[] childNodes = fields[1].split(SEPARATOR_REGEX);
				for (int i = 0; i < childNodes.length - 1; i++) {
					if (currentNode != null && currentNode.getType() == LazyType.OBJECT) {
						currentNode = currentNode.getJSONObject(childNodes[i]);
					} else {
						return false;
					}
				}
				if (currentNode != null) {
					return getValueAsString(currentNode, childNodes[childNodes.length - 1]).equals(value);
				}
			}
		}
		return false;
	}

	private String getValueAsString(LazyObject jsonNode, String fieldName) {
		switch (jsonNode.getType(fieldName)) {
		case STRING:
			return jsonNode.getString(fieldName);
		case INTEGER:
			return Integer.toString(jsonNode.getInt(fieldName));
		case BOOLEAN:
			return Boolean.toString(jsonNode.getBoolean(fieldName));
		case FLOAT:
			return Double.toString(jsonNode.getDouble(fieldName));
		default:
			return "null";
		}
	}

	private String getValueAsString(LazyArray jsonNode, int i) {
		switch (jsonNode.getType(i)) {
		case STRING:
			return jsonNode.getString(i);
		case INTEGER:
			return Integer.toString(jsonNode.getInt(i));
		case BOOLEAN:
			return Boolean.toString(jsonNode.getBoolean(i));
		case FLOAT:
			return Double.toString(jsonNode.getDouble(i));
		default:
			return "null";
		}
	}

	private boolean pathIsIgnoreField(String currentPath) {
		if (ignoreFields != null && !ignoreFields.isEmpty()) {
			for (String ignoreField : ignoreFields) {
				if (pathEquals(currentPath, ignoreField)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean pathEquals(String path, String patternedPath) {
		String[] pathArr = path.split(SEPARATOR_REGEX);
		String[] ignorePathArr = patternedPath.split(SEPARATOR_REGEX);

		if (pathArr.length != ignorePathArr.length) {
			return false;
		}

		for (int i = 0; i < pathArr.length; i++) {
			String nodePath = pathArr[i];
			String ignoreNodeArr = ignorePathArr[i];
			boolean nodeIsArray = nodePath.endsWith(END_BRACKET);
			boolean ignoreNodeIsArray = ignoreNodeArr.endsWith(END_BRACKET);
			if (nodeIsArray && ignoreNodeIsArray) {
				String pathIndex = pathArr[i].substring(pathArr[i].lastIndexOf(BEGIN_BRACKET) + 1,
						pathArr[i].length() - 1);
				String ignoreIndex = ignorePathArr[i].substring(ignorePathArr[i].lastIndexOf(BEGIN_BRACKET) + 1,
						ignorePathArr[i].length() - 1);
				if (!pathIndex.equals(ignoreIndex) && !ignoreIndex.equals(WILDCARD)) {
					return false;
				}
				nodePath = nodePath.substring(0, nodePath.length() - pathIndex.length() - 2);
				ignoreNodeArr = ignoreNodeArr.substring(0, ignoreNodeArr.length() - ignoreIndex.length() - 2);
			}
			if (!nodePath.equals(ignoreNodeArr)) {
				return false;
			}
		}
		return true;
	}

	private void logInequality(String valueA, String valueB, String currentPath) {
		inequalityMessages.add(currentPath + " values were not the same! Expected " + valueA + " but got " + valueB);
	}

	private static String getChildPath(String currentPath, String childName) {
		return currentPath + NODE_SEPARATOR + childName;
	}

	private static boolean childIsObject(LazyObject parent, String fieldName) {
		return parent.getType(fieldName) == LazyType.OBJECT;
	}

	private static boolean childIsObject(LazyArray parent, int index) {
		return parent.getType(index) == LazyType.OBJECT;
	}

	private static boolean childIsArray(LazyObject parent, String fieldName) {
		return parent.getType(fieldName) == LazyType.ARRAY;
	}

	private static boolean childIsArray(LazyArray parent, int index) {
		return parent.getType(index) == LazyType.ARRAY;
	}

	public static void setDebugMode(boolean debugMode) {
		JsonEquals.debugMode = debugMode;
	}
}
