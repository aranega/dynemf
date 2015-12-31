package org.dynemf;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * Wraps an EMF {@link ResourceSet} and provides facilities to register
 * metamodels/factories and to create/open resources.
 * 
 * @author Vincent Aranega
 * 
 */
public class ResourceSetWrapper extends DynEMFWrapper<ResourceSet> {
	/**
	 * Binding between URI and produced EPackageWrapper (EPACKageWrapperS)
	 */
	protected final Map<String, EPackageWrapper> epackws = new HashMap<>();

	ResourceSetWrapper() {
		super(createResourceSet());
	}

	ResourceSetWrapper(ResourceSet rset) {
		super(rset);
	}

	/**
	 * Creates a prepared {@link ResourceSet} with some registered factories.
	 * Produced resources by extensions are:
	 * <ul>
	 * <li>.xmi uses {@link XMIResourceImpl}</li>
	 * <li>.bin uses {@link BinaryResourceImpl}</li>
	 * <li>.ecore uses {@link XMIResourceImpl}</li>
	 * <li>.* uses {@link XMIResourceImpl}</li>
	 * </ul>
	 * This method can be override in order to change the basic ResourceSet used
	 * by this wrapper.
	 * 
	 * @return a configured ResourceSet
	 */
	protected static ResourceSet createResourceSet() {
		ResourceSet result = new ResourceSetImpl();
		result.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		result.getResourceFactoryRegistry().getExtensionToFactoryMap().put("bin", new ResourceFactoryImpl() {
			@Override
			public Resource createResource(URI uri) {
				return new BinaryResourceImpl(uri);
			}
		});
		result.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		result.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		return result;
	}

	/**
	 * Copies the package registry from an existing {@link ResourceSet} to this
	 * instance.
	 * 
	 * @param rset an existing ResourceSet
	 * @return this ResourceSetWrapper instance
	 */
	public ResourceSetWrapper copyPackageRegistry(ResourceSet rset) {
		result().getPackageRegistry().putAll(rset.getPackageRegistry());
		return this;
	}

	/**
	 * Registers a factory for an extension.
	 * 
	 * @param extension the extension to register without the leading "." e.g.:
	 *            "xmi"
	 * @param factory the factory to bind to the extension
	 * @return this ResourceSetWrapper instance
	 */
	public ResourceSetWrapper factory(String extension, EFactory factory) {
		result().getResourceFactoryRegistry().getExtensionToFactoryMap().put(extension, factory);
		return this;
	}

	/**
	 * Gets a {@link EPackageWrapper} representing the {@link EPackage}
	 * associated to the metamodel registered at the nsUri URI.
	 * 
	 * @param nsUri the metamodel nsURI
	 * @return an EPackageWrapper
	 */
	public EPackageWrapper ePackage(String nsUri) {
		if (this.epackws.containsKey(nsUri)) {
			return this.epackws.get(nsUri);
		}
		EPackageWrapper epackw = new EPackageWrapper(result().getPackageRegistry().getEPackage(nsUri));
		this.epackws.put(nsUri, epackw);
		return epackw;
	}

	/**
	 * Registers a metamodel into the {@link EPackage.Registry} of the
	 * ResourceSet of this instance.
	 * 
	 * @param pack the EPackage (root) of the metamodel
	 * @return this ResourceSetWrapper instance
	 */
	public ResourceSetWrapper register(EPackage pack) {
		return register(pack.getNsURI(), pack);
	}

	/**
	 * Registers a metamodel into the {@link EPackage.Registry} of the
	 * ResourceSet of this instance.
	 * 
	 * @param nsUri the metamodel nsURI
	 * @param pack the EPackage (root) of the metamodel
	 * @return this ResourceSetWrapper instance
	 */
	public ResourceSetWrapper register(String nsUri, EPackage pack) {
		result().getPackageRegistry().put(nsUri, pack);
		update(pack.getNsURI(), pack);
		return this;
	}

	/**
	 * Registers a metamodel into the {@link EPackage.Registry} of the
	 * ResourceSet of this instance from a physical location onto the file
	 * system.
	 * 
	 * @param path the path to the ".ecore" metamodel
	 * @return this ResourceSetWrapper instance
	 */
	public ResourceSetWrapper register(String path) {
		return register(new File(path));
	}

	/**
	 * Registers a metamodel into the {@link EPackage.Registry} of the
	 * ResourceSet of this instance from a physical location onto the file
	 * system.
	 * 
	 * @param f the File instance to the ".ecore" metamodel
	 * @return this ResourceSetWrapper instance
	 */
	public ResourceSetWrapper register(File f) {
		return register(URI.createFileURI(f.getAbsolutePath()));
	}

	/**
	 * Registers a metamodel into the {@link EPackage.Registry} of the
	 * ResourceSet of this instance from a URI (logical or not). This method
	 * loads the metamodel at the URI, then register it.
	 * 
	 * @param uri the URI to the metamodel that should be registered
	 * @return this ResourceSetWrapper instance
	 */
	public ResourceSetWrapper register(URI uri) {
		Resource metam = result().getResource(uri, true);
		for (EObject root : metam.getContents()) {
			if (root instanceof EPackage) {
				EPackage pack = (EPackage) root;
				result().getPackageRegistry().put(pack.getNsURI(), pack);
				update(pack.getNsURI(), pack);
			}
		}
		return this;
	}

	/**
	 * Updates the {@link EPackageWrapper} created from this ResourceWrapper in
	 * case a metamodel is dynamically reloaded.
	 * 
	 * @param uri the metamodel nsURI.
	 * @param pack the new EPackage that must be passed to the
	 *            {@link EPackageWrapper} instances.
	 */
	protected void update(String uri, EPackage pack) {
		if (this.epackws.containsKey(uri)) {
			this.epackws.get(uri).update(pack);
		}
	}

	/**
	 * Creates a new file (model) on the disk (represented by a
	 * {@link ResourceWrapper}). If the directory hierarchy does not exists on
	 * the disk, it will be created.
	 * 
	 * @param path the path to the file that should be created
	 * @return a new {@link ResourceWrapper}
	 */
	public ResourceWrapper create(String path) {
		return create(URI.createFileURI(path));
	}

	/**
	 * Creates a new model (represented by a {@link ResourceWrapper}).
	 * 
	 * @param model the model URI
	 * @return a new {@link ResourceWrapper}
	 */
	public ResourceWrapper create(URI model) {
		return new ResourceWrapper(result().createResource(model));
	}

	/**
	 * Opens an existing model and gets the associated {@link ResourceWrapper}
	 * to handle it.
	 * 
	 * @param path the path to the model.
	 * @return A new {@link ResourceWrapper}
	 */
	public ResourceWrapper open(String path) {
		return open(URI.createFileURI(path));
	}

	/**
	 * Opens an existing model and gets the associated {@link ResourceWrapper}
	 * to handle it.
	 * 
	 * @param model the model URI
	 * @return a new {@link ResourceWrapper}
	 */
	public ResourceWrapper open(URI model) {
		return new ResourceWrapper(result().getResource(model, true));
	}

	/*
	 * Static constructors
	 */

	/**
	 * Creates an empty {@link ResourceSetWrapper} already initialized.
	 * 
	 * @return a new ResourceSetWrapper
	 */
	public static ResourceSetWrapper rset() {
		return new ResourceSetWrapper();
	}

	/**
	 * Creates a new {@link ResourceSetWrapper} from an existing ResourceSet
	 * (wraps it).
	 * 
	 * @param existingRSet the existing ResourceSet
	 * @return a new ResourceSetWrapper
	 */
	public static ResourceSetWrapper rset(ResourceSet existingRSet) {
		return new ResourceSetWrapper(existingRSet);
	}

}
