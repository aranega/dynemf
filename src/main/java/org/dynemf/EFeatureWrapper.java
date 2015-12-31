package org.dynemf;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Wraps an {@link EStructuralFeature} and provides facilities to handle them.
 * 
 * @author Vincent Aranega
 *
 */
public class EFeatureWrapper extends DynEMFWrapper<EStructuralFeature> {
	protected EObjectWrapper<?> container;

	EFeatureWrapper(EStructuralFeature feature, EObjectWrapper<?> container) {
		super(feature);
		this.container = container;
	}

	/**
	 * Gets the {@link EStructuralFeature} container.
	 * 
	 * @return the wrapped EStructuralFeature container
	 */
	public EObjectWrapper<?> container() {
		return this.container;
	}

	/**
	 * Checks if the wrapped feature is an {@link EReference} and is
	 * containment.
	 * 
	 * @return true if the feature is containment
	 */
	public boolean isContainment() {
		return result() instanceof EReference && ((EReference) result()).isContainment();
	}

	/**
	 * Checks if the wrapped feature has [x-*] cardinality or not.
	 * 
	 * @return true if the cardinality of the wrapped feature is > 1
	 */
	public boolean isMany() {
		return result().isMany();
	}

	/**
	 * Checks if the wrapped feature is ordered.
	 * 
	 * @return true if the wrapped feature is ordered.
	 */
	public boolean isOrdered() {
		return result().isOrdered();
	}

}
