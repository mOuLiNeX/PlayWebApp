package models;

import java.util.List;

import javax.persistence.EntityManager;

import models.referentiel.TypeDocument;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.junit.Before;
import org.junit.Test;

import play.db.jpa.JPA;
import play.test.Fixtures;
import play.test.UnitTest;

public class DocumentSearchTest extends UnitTest {

	@Before
	public void init() throws InterruptedException {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("models/documents.yml");
		EntityManager em = JPA.entityManagerFactory.createEntityManager();
		FullTextEntityManager fullTextEntityManager = Search
				.getFullTextEntityManager(em);
		fullTextEntityManager.createIndexer().startAndWait();
	}

	@Test
	public void rechercheParCritere() throws Exception {
		List<Document> docRecherches = Document
				.rechercherParDescription("description");
		assertEquals(1, docRecherches.size());
	}
	
//	@Test
//	public void rechercheApproximativeParCritere() throws Exception {
//		List<Document> docRecherches = Document
//				.rechercherParDescription("fescription");
//		assertEquals(1, docRecherches.size());
//	}

}
