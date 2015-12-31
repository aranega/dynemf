package org.dynemf;

import org.eclipse.emf.ecore.EEnumLiteral;

/**
 * Wraps an {@link EEnumLiteral} and provides facilities to handle them.
 * 
 * @author Vincent Aranega
 *
 */
public class EEnumLiteralWrapper extends ValueWrapper<EEnumLiteral> {

	EEnumLiteralWrapper(EEnumLiteral obj) {
		super(obj);
	}

}
