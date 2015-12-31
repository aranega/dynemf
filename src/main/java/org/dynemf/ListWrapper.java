package org.dynemf;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * Represents a {@link EList} of {@link EObject}.
 * 
 * @author Vincent Aranega
 *
 */
public class ListWrapper extends ValueWrapper<EList<EObject>> {
	protected List<EObjectWrapper<?>> inner;

	ListWrapper(EList<EObject> obj, List<EObjectWrapper<?>> inner) {
		super(obj);
		this.inner = inner;
	}

	/**
	 * Checks if the list is empty.
	 * 
	 * @return true if the list is empty.
	 */
	public boolean isEmpty() {
		return this.inner.isEmpty();
	}

	/**
	 * Gets the i-th {@link EObjectWrapper} contained in the list.
	 * 
	 * @param i the position of the element
	 * @return the i-th {@link EObjectWrapper} element
	 */
	public EObjectWrapper<?> at(int i) {
		return this.inner.get(i);
	}

	/**
	 * Checks if an {@link EObjectWrapper} is contained in the list.
	 * 
	 * @param e
	 * @return
	 */
	public boolean includes(EObjectWrapper<?> e) {
		return this.inner.contains(e);
	}

}
