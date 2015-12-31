package org.dynemf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * Wraps a {@link Resource} and provides facilities for opening/loading/saving
 * and reading a {@link Resource}.
 * 
 * @author Vincent Aranega
 *
 */
public class ResourceWrapper extends DynEMFWrapper<Resource> {

	ResourceWrapper(Resource res) {
		super(res);
	}

	/**
	 * Saves the wrapped resource with dedicated options.
	 * 
	 * @param options a map of the options to use during saving
	 * @return this instance of ResourceWrapper
	 * @throws IOException if an exception occurred during the saving operation.
	 */
	public ResourceWrapper save(Map<?, ?> options) throws IOException {
		result().save(options);
		return this;
	}

	/**
	 * Saves the wrapped resource without options.
	 * 
	 * @return this instance of ResourceWrapper
	 * @throws IOException if an exception occurred during the saving operation.
	 */
	public ResourceWrapper save() throws IOException {
		return save(null);
	}

	/**
	 * Saves (serializes) the wrapped resource on the file system at a specific
	 * location.
	 * 
	 * @param path the path where the resource should be serialized
	 * @return this instance of ResourceWrapper
	 * @throws IOException if an exception occurred during the saving operation.
	 */
	public ResourceWrapper saveAs(String path) throws IOException {
		return saveAs(path, null);
	}

	/**
	 * Saves (serializes) the wrapped ressource on the file system at a specific
	 * location with dedicated options.
	 * 
	 * @param path the path where the resource should be serialized
	 * @param options a map of the options to use during the saving operation
	 * @return this instance of ResourceWrapper
	 * @throws IOException if an exception occurred during the saving operation.
	 */
	public ResourceWrapper saveAs(String path, Map<?, ?> options) throws IOException {
		result().setURI(URI.createFileURI(path));
		return save(options);
	}

	/**
	 * Loads a file at a specific location into the wrapped resource.
	 * 
	 * @param path the path to the file to load
	 * @return this instance of ResourceWrapper
	 * @throws FileNotFoundException if the path to the file does not exist.
	 * @throws IOException if an exception occurred during the loading
	 *             operation.
	 */
	public ResourceWrapper load(String path) throws FileNotFoundException, IOException {
		return load(new FileInputStream(path));
	}

	/**
	 * Loads a file at a specific location into the wrapped resource with
	 * dedicated options.
	 * 
	 * @param path the path to the file to load
	 * @param options a map with the options to use during the loading operation
	 * @return this instance of ResourceWrapper
	 * @throws FileNotFoundException if the path to the file does not exist.
	 * @throws IOException if an exception occurred during the loading
	 *             operation.
	 */
	public ResourceWrapper load(String path, Map<?, ?> options) throws FileNotFoundException, IOException {
		return load(new FileInputStream(path), options);
	}

	/**
	 * Loads an {@link InputStream} into the wrapped resource.
	 * 
	 * @param stream the stream to be loaded
	 * @return this instance of ResourceWrapper
	 * @throws IOException if an exception occurred during the loading
	 *             operation.
	 */
	public ResourceWrapper load(InputStream stream) throws IOException {
		return load(stream, null);
	}

	/**
	 * Loads an {@link InputStream} into the wrapped resource using dedicated
	 * options.
	 * 
	 * @param stream the stream to be loaded
	 * @param options a map of options to use during the loading operation
	 * @return this instance of ResourceWrapper
	 * @throws IOException if an exception occurred during the loading
	 *             operation.
	 */
	public ResourceWrapper load(InputStream stream, Map<?, ?> options) throws IOException {
		result().load(stream, options);
		return this;
	}

	/**
	 * Checks if the wrapped resource is empty (it contains NO elements).
	 * 
	 * @return true if no elements are contained by the resource
	 */
	public boolean isEmpty() {
		return result().getContents().isEmpty();
	}

	/**
	 * Checks if the resource contains many root elements.
	 * 
	 * @return true if many root elements are found
	 */
	public boolean isMultiRoot() {
		return result().getContents().size() > 1;
	}

	/**
	 * Adds elements to the resource root.
	 * 
	 * @param objs the {@link EObject} to add to the root
	 * @return this instance of ResourceWrapper
	 */
	public ResourceWrapper add(EObject... objs) {
		for (EObject o : objs) {
			result().getContents().add(o);
		}
		return this;
	}

	/**
	 * Gets the first root element.
	 * 
	 * @return the first root element wrapped in an {@link EObjectWrapper}
	 */
	public EObjectWrapper<?> root() {
		return root(0);
	}

	/**
	 * Gets the i-th root element.
	 * 
	 * @param i the root number.
	 * @return the i-th root element wrapped in an {@link EObjectWrapper}
	 */
	public EObjectWrapper<?> root(int i) {
		return new EObjectWrapper<EObject>(result().getContents().get(i));
	}

	/**
	 * Adds elements to the resource root.
	 * 
	 * @param objs the {@link EObjectWrapper} to add to the root
	 * @return this instance of ResourceWrapper
	 */
	public ResourceWrapper add(EObjectWrapper<?>... objs) {
		for (EObjectWrapper<?> o : objs) {
			result().getContents().add(o.result());
		}
		return this;
	}

	/*
	 * Static constructors
	 */

	/**
	 * Opens a file and binds it to a resource.
	 * 
	 * @param path the path to the file to open.
	 * @return a new ResourceWrapper instance wrapping the loaded file into a
	 *         Resource
	 * @throws IOException if an exception occurred during the loading
	 *             operation.
	 */
	public static ResourceWrapper open(String path) throws IOException {
		return open(URI.createFileURI(path));
	}

	/**
	 * Opens an URI and binds it to a resource.
	 * 
	 * @param uri the uri to the resource to load.
	 * @return a new ResourceWrapper instance wrapping the loaded URI into a
	 *         Resource.
	 * @throws IOException if an exception occurred during the loading
	 *             operation.
	 */
	public static ResourceWrapper open(URI uri) throws IOException {
		String extension = uri.fileExtension();
		Resource r = null;
		if ("bin".equalsIgnoreCase(extension)) {
			r = new BinaryResourceImpl(uri);
		} else {
			r = new XMIResourceImpl(uri);
		}
		r.load(null);
		return new ResourceWrapper(r);
	}
}
