package org.dynemf;

/**
 * Represents a "null" value.
 * 
 * @author Vincent Aranega
 *
 */
public class NullValueWrapper extends ValueWrapper<Void> {
	public static final NullValueWrapper NULL = new NullValueWrapper();

	private NullValueWrapper() {
		super(null);
	}

}
