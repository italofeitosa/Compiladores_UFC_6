package symbolTable;

public class Util {
	public static String getDefinedName(String label) {
		return label.split("@")[1].trim();
	}
}