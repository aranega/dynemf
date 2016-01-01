/*******************************************************************************
 * Copyright (c) 2015-2016 Vincent Aranega
 *
 * See the file LICENSE for copying permission.
 *******************************************************************************/
package org.dynemf;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * Wraps an {@link EPackage} and provides the "root" for creating various
 * EObjects regarding of the used {@link EPackage}.
 * 
 * @author Vincent Aranega
 *
 */
public class EPackageWrapper extends DynEMFWrapper<EPackage> {
	protected EFactory factory;

	EPackageWrapper(EPackage pack) {
		super(pack);
		this.factory = pack.getEFactoryInstance();
	}

	/**
	 * Creates a new meta-class instance wrapped in an {@link EObjectWrapper}.
	 * 
	 * @param ename the name of the meta-class
	 * @return a new EObjectWrapper with the new created "ename" instance
	 */
	public EObjectWrapper<EObject> create(String ename) {
		return new EObjectWrapper<EObject>(factory.create((EClass) result().getEClassifier(ename)));
	}

	/**
	 * Creates a new meta-class instance wrapped in an {@link EObjectWrapper}.
	 * 
	 * @param clazz the class of the meta-class (<i>e.g.</i>: A.class, where A
	 *            comes from the metamodel wrapped by this object)
	 * @return a new EObjectWrapper wit the new created "clazz" instance
	 */
	@SuppressWarnings("unchecked")
	public <T extends EObject> EObjectWrapper<T> create(Class<T> clazz) {
		return new EObjectWrapper<T>((T) factory.create((EClass) result().getEClassifier(clazz.getSimpleName())));
	}

	void update(EPackage pack) {
		this.obj = pack;
		this.factory = pack.getEFactoryInstance();
	}

	/**
	 * Wraps the wrapped {@link EPackage} in an {@link EObjectWrapper}.
	 * 
	 * @return a new {@link EObjectWrapper}
	 */
	public EObjectWrapper<EPackage> asEObj() {
		return new EObjectWrapper<EPackage>(result());
	}

	/*
	 * Static constructor
	 */

	/**
	 * Builds a new {@link EPackageWrapper} from an existing {@link EPackage}.
	 * 
	 * @param pack the EPackage to wrap
	 * @return a new instance of {@link EPackageWrapper}
	 */
	public static EPackageWrapper ePackage(EPackage pack) {
		return new EPackageWrapper(pack);
	}

}
