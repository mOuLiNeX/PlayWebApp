package models;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import models.exception.DocumentException;
import models.referentiel.TypeDocument;
import models.referentiel.TypeIndex;
import models.typeDeDonnees.Format;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class DocumentTest extends UnitTest {
	private TypeDocument typeDoc;

	@Before
	public void init() {
		Fixtures.deleteDatabase();
		typeDoc = new TypeDocument("TYPEDOC");
		typeDoc.ajouterTypeIndex(new TypeIndex("TYPEIDX1", Format.ENTIER));
		typeDoc.ajouterTypeIndex(new TypeIndex("TYPEIDX2", Format.DECIMAL));
		typeDoc.ajouterTypeIndex(new TypeIndex("TYPEIDX3", Format.MONTANT));
		typeDoc.ajouterTypeIndex(new TypeIndex("TYPEIDX4", Format.DATE));
		typeDoc.save();

	}

	@Test
	public void creationDocumentVide() {
		Document docTest = Document.creerAVide(typeDoc);
		assertNotNull(docTest.getDateCreation());
		assertNotNull(docTest.ficheIndex);
		assertEquals(4, docTest.ficheIndex.size());

		assertTrue(docTest.ficheIndex.containsKey("TYPEIDX1"));
		assertTrue(docTest.ficheIndex.containsKey("TYPEIDX2"));
		assertTrue(docTest.ficheIndex.containsKey("TYPEIDX3"));
		assertTrue(docTest.ficheIndex.containsKey("TYPEIDX4"));
	}

	@Test
	public void creationDocumentRempli() {
		Integer indexValeur1 = 10;
		Float indexValeur2 = 2.3F;
		BigDecimal indexValeur3 = BigDecimal.ONE;
		Date indexValeur4 = (new GregorianCalendar(1976, Calendar.NOVEMBER, 13)).getTime();
		
		Document docTest = Document.creerAVide(typeDoc);
		docTest.valoriserIndex("TYPEIDX1", indexValeur1);
		docTest.valoriserIndex("TYPEIDX2", indexValeur2);
		docTest.valoriserIndex("TYPEIDX3", indexValeur3);
		docTest.valoriserIndex("TYPEIDX4", indexValeur4);
		
		assertEquals(indexValeur1, docTest.getValeurIndex("TYPEIDX1"));
		assertEquals(indexValeur2, docTest.getValeurIndex("TYPEIDX2"));
		assertEquals(indexValeur3, docTest.getValeurIndex("TYPEIDX3"));
		assertEquals(indexValeur4, docTest.getValeurIndex("TYPEIDX4"));
	}
	
	@Test(expected=DocumentException.class)
	public void creationDocumentTypeIndexInexistant() {
		Document docTest = Document.creerAVide(typeDoc);
		docTest.valoriserIndex("TOTO", "");
		
	}
}
