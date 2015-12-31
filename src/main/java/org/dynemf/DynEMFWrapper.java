package org.dynemf;

/**
 * Base class with the basic behavior of every wrappers.
 * 
 * @author Vincent Aranega
 *
 * @param <T> The wrapped object type (e.g.: EObject, Resource...)
 */
public abstract class DynEMFWrapper<T> {
	protected T obj;
	
	DynEMFWrapper(T obj) {
		this.obj = obj;
	}

	/**
	 * Returns the wrapped object.
	 * @return The wrapped object.
	 */
	public T result() {
		return this.obj;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof DynEMFWrapper) {
			return obj.equals(((DynEMFWrapper<?>)other).result());
		} else {
			return obj.equals(other);
		}
	}
	
	@Override
	public int hashCode() {
		return obj.hashCode();
	}
	
}
