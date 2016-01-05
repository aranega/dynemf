package org.dynemf;

import static org.dynemf.EPackageWrapper.ePackage;
import static org.dynemf.ResourceSetWrapper.rset;
import static org.dynemf.ResourceWrapper.use;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;

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

		ByteArrayOutputStream b1 = new ByteArrayOutputStream();
		// ResourceWrapper r2 =
		// rset.create("target/A.bin").add(simplemm.create("A").set("name",
		// "testA")).save(b1);

		ByteArrayOutputStream b2 = new ByteArrayOutputStream();
//		r2.clear().add(simplemm.create("A").set("name", "testB"), simplemm.create("A").set("name", "testA")).save(b2);

		BinaryResourceImpl bin1 = new BinaryResourceImpl();
		ResourceWrapper r3 = use(bin1);
		r3.add(simplemm.create("A").set("name", "testA")).save(b1);

		r3.clear().add(simplemm.create("A").set("name", "testB"), simplemm.create("A").set("name", "testA")).save(b2);

		for (byte b : b1.toByteArray()) {
			System.out.print(" " + b);
		}
		System.out.println();
		for (byte b : b2.toByteArray()) {
			System.out.print(" " + b);
		}
	}
}
