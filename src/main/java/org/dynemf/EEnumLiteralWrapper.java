/*******************************************************************************
 * Copyright (c) 2015-2016 Vincent Aranega
 *
 * See the file LICENSE for copying permission.
 *******************************************************************************/
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
