package org.dynemf;

import static org.dynemf.EPackageWrapper.ePackage;
import static org.dynemf.ResourceSetWrapper.rset;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;

public class Main {

	public static void main(String[] args) throws IOException {
		ResourceSetWrapper rset = rset().register("src/test/resources/metamodels/simple.ecore");
		// rset.register(SimplePackage.eINSTANCE);

		System.out.println(rset.open("src/test/resources/models/01.xmi").result().getContents());

		EPackageWrapper simplemm = rset.ePackage("http://DynEMF/simple/1.0");

		ResourceWrapper r = rset.create(URI.createURI("target/gen/gen01.xmi"));
		System.out.println(r.save());

		r.add(simplemm.create("A").set("name", "test").add("a", simplemm.create("A").set("name", "innerA")))
				.saveAs("target/gen/gen02.xmi");

		System.out.println(r.root().property("a").asList().at(0).asEObject().property("name").result());

		r.root(0).removeAt("a", 0);
		r.saveAs("target/gen/gen04.xmi");

		EPackageWrapper ecore = ePackage(EcorePackage.eINSTANCE);
		// ResourceWrapper newmm =
		// open("src/test/resources/metamodels/simple.ecore");

		System.out.println(simplemm.asEObj().add("eClassifiers", ecore.create(EClass.class).set("name", "B")));
		// newmm.saveAs("src/test/resources/metamodels/simple-gen.ecore");

		// rset.register(newmm.root().<EPackage>as().result());
		r.add(simplemm.create("B"));
		r.saveAs("target/gen/gen05.xmi");

		// rset.register(SimplePackage.eNS_URI, SimplePackage.eINSTANCE);
	}
}
