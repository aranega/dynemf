/*******************************************************************************
 * Copyright (c) 2015-2016 Vincent Aranega
 *
 * See the file LICENSE for copying permission.
 *******************************************************************************/
package org.dynemf;

import java.util.List;

import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;

/**
 * Represents any type of values that can be stored by a feature.
 * 
 * @author Vincent Aranega
 *
 * @param <T> the type of the wrapped value.
 */
public class ValueWrapper<T> extends DynEMFWrapper<T> {
	ValueWrapper(T obj) {
		super(obj);
	}

	/**
	 * Checks if the value is a list.
	 * 
	 * @return true if the wrapped value is a list
	 */
	public boolean isList() {
		return obj instanceof List<?>;
	}

	/**
	 * Checks if the value is an enumeration literal.
	 * 
	 * @return true if the wrapped value is an enumeration literal
	 */
	public boolean isLiteral() {
		return obj instanceof EEnumLiteral;
	}

	/**
	 * Checks if the value is an EObject.
	 * 
	 * @return true if the wrapped value is an EObject
	 */
	public boolean isEObject() {
		return obj instanceof EObject;
	}

	/**
	 * Casts the {@link ValueWrapper} as a {@link ListWrapper}.
	 * 
	 * @return a {@link ListWrapper}
	 */
	public ListWrapper asList() {
		return (ListWrapper) this;
	}

	/**
	 * Casts the {@link ValueWrapper} as a {@link EEnumLiteralWrapper}.
	 * 
	 * @return an {@link EEnumLiteralWrapper}
	 */
	public EEnumLiteralWrapper asLiteral() {
		return (EEnumLiteralWrapper) this;
	}

	/**
	 * Casts the {@link ValueWrapper} as an {@link EObjectWrapper}
	 * 
	 * @return an {@link EObjectWrapper}
	 */
	public EObjectWrapper<?> asEObject() {
		return (EObjectWrapper<?>) this;
	}
}
