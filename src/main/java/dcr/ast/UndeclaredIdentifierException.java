package dcr.ast;

public class UndeclaredIdentifierException extends Exception {

	public UndeclaredIdentifierException(String identifier) {
		super(identifier);
	}

}
