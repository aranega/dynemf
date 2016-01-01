/*******************************************************************************
 * Copyright (c) 2015-2016 Vincent Aranega
 *
 * See the file LICENSE for copying permission.
 *******************************************************************************/
package org.dynemf;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Wraps an {@link EObject} and provides facilities to handle it.
 * 
 * @author Vincent Aranega
 *
 * @param <T> The EObject type wrapped (basis is EObject).
 */
public class EObjectWrapper<T extends EObject> extends ValueWrapper<T> {

	EObjectWrapper(T eobj) {
		super(eobj);
	}

	/**
	 * Sets a value to a feature. The value can be a "basic" element (Integer,
	 * Boolean...) an {@link EObject} or an {@link EObjectWrapper}.
	 * 
	 * @param name the name of the feature
	 * @param value the value of the feature
	 * @return this instance of EObjectWrapper
	 */
	public EObjectWrapper<T> set(String name, Object value) {
		EStructuralFeature feature = result().eClass().getEStructuralFeature(name);
		if (value instanceof EObjectWrapper) {
			result().eSet(feature, ((EObjectWrapper<?>) value).result());
		} else {
			result().eSet(feature, value);
		}
		return this;
	}

	/**
	 * Unsets the feature.
	 * 
	 * @param name the feature to unset
	 * @return this instance of EObjectWrapper.
	 */
	public EObjectWrapper<T> unset(String name) {
		result().eUnset(result().eClass().getEStructuralFeature(name));
		return this;
	}

	/**
	 * Adds elements to a collection. The element can be an {@link EObject}
	 * instance or an {@link EObjectWrapper}.
	 * 
	 * @param name the name of the collection
	 * @param values the elements to add to the collection
	 * @return this instance of EObjectWrapper
	 */
	@SuppressWarnings("unchecked")
	public EObjectWrapper<T> add(String name, Object... values) {
		EStructuralFeature feature = result().eClass().getEStructuralFeature(name);
		if (feature == null) {
			throw new RuntimeException("Feature '" + name + "' does not exist");
		}
		if (!feature.isMany()) {
			throw new RuntimeException("Feature '" + name + "' is not many, you must use set() instead of add().");
		}
		List<Object> list = (List<Object>) result().eGet(feature);
		for (Object o : values) {
			if (o instanceof EObjectWrapper) {
				list.add(((EObjectWrapper<?>) o).result());
			} else {
				list.add(o);
			}
		}
		return this;
	}

	/**
	 * Removes elements from a collection. The elemnent can be an
	 * {@link EObject} instance or an {@link EObjectWrapper}.
	 * 
	 * @param name the name of the collection
	 * @param value the elements to remove
	 * @return this instance of EObjectWrapper
	 */
	@SuppressWarnings("unchecked")
	public EObjectWrapper<T> remove(String name, Object... values) {
		EStructuralFeature feature = result().eClass().getEStructuralFeature(name);
		List<Object> list = (List<Object>) result().eGet(feature);
		for (Object o : values) {
			if (o instanceof EObjectWrapper) {
				list.remove(((EObjectWrapper<?>) o).result());
			} else {
				list.remove(o);
			}
		}
		return this;
	}

	/**
	 * Removes the i-th element from a collection.
	 * 
	 * @param name the collection from which the element must be removed
	 * @param i the position of the element to remove
	 * @return this instance of EObjectWrapper
	 */
	@SuppressWarnings("unchecked")
	public EObjectWrapper<T> removeAt(String name, int i) {
		EStructuralFeature feature = result().eClass().getEStructuralFeature(name);
		List<Object> list = (List<Object>) result().eGet(feature);
		list.remove(i);
		return this;
	}

	/**
	 * Gets the {@link EStructuralFeature} of an element (its meta-feature)
	 * wrapped in a {@link EFeatureWrapper}.
	 * 
	 * @param name the name of the feature.
	 * @return a new instance of {@link EFeatureWrapper} wrapping the feature.
	 */
	public EFeatureWrapper eFeature(String name) {
		EStructuralFeature feature = result().eClass().getEStructuralFeature(name);
		return new EFeatureWrapper(feature, this);
	}

	/**
	 * Gets the value of an attribute/reference wrapped in a
	 * {@link ValueWrapper}.
	 * 
	 * @param name the name of the attribute/reference to navigate
	 * @return a new instance of ValueWrapper wrapping the result
	 */
	@SuppressWarnings("unchecked")
	public ValueWrapper<?> property(String name) {
		if (obj == null) {
			throw new RuntimeException("Object has not been loaded (no model given)");
		}
		EFeatureWrapper efeature = eFeature(name);
		EStructuralFeature feature = efeature.result();
		Object o = obj.eGet(feature);

		if (o == null) {
			return NullValueWrapper.NULL;
		}

		if (o instanceof EObject) {
			return obj((EObject) o);
		} else if (o instanceof List<?>) {
			List<EObjectWrapper<?>> values = new LinkedList<>();
			for (EObject e : (List<EObject>) o) {
				values.add(obj(e));
			}
			return new ListWrapper((EList<EObject>) o, values);
		} else if (o instanceof EEnumLiteral) {
			return new EEnumLiteralWrapper((EEnumLiteral) o);
		} else {
			return new ValueWrapper<Object>(o);
		}
	}

	/**
	 * Casts an {@link EObjectWrapper<EObject>} to a more refine
	 * {@link EObjectWrapper}.
	 * 
	 * <i>e.g.</g>:
	 * 
	 * <pre>
	 * EObjectWrapper&ltEObject> a = resource.root();
	 * EObjectWrapper&ltEPackage> b = a.&ltEPackage>as();
	 * </pre>
	 * 
	 * @return the casted EObjectWrapper
	 */
	@SuppressWarnings("unchecked")
	public <X extends EObject> EObjectWrapper<X> as() {
		return (EObjectWrapper<X>) this;
	}

	/*
	 * Static constructor
	 */

	/**
	 * Builds an {@link EObjectWrapper} from an {@link EObject}.
	 * 
	 * @param o the {@link EObject} to wrap
	 * @return a new instance of EObjectWrapper
	 */
	public static EObjectWrapper<EObject> obj(EObject o) {
		return new EObjectWrapper<EObject>(o);
	}
}
