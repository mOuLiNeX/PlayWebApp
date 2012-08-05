package models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import models.exception.DocumentException;
import models.exception.FormatException;
import models.referentiel.TypeDocument;
import models.referentiel.TypeIndex;

import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name = "tbl_document")
@Indexed
public class Document extends Model {

	@Required
	public String nom;

	@MaxSize(value = 1000)
	@Field(index = org.hibernate.search.annotations.Index.TOKENIZED, store = Store.YES)
	public String description;

	private Date dateCreation;
	private Date dateModification;

	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "code")
	public Map<String, Index> ficheIndex = new HashMap<String, Index>();

	@ManyToOne
	@Required
	public TypeDocument type;

	private Document() {
		super();
		dateCreation = new Date();
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public Date getDateModification() {
		return dateModification;
	}

	public static Document creerAVide(TypeDocument typeDocument) {
		Document doc = new Document();
		doc.type = typeDocument;

		for (Map.Entry<String, TypeIndex> typeIndex : typeDocument
				.getFicheIndex().entrySet()) {
			Index idx = new Index(typeIndex.getValue());
			doc.ficheIndex.put(typeIndex.getKey(), idx);
		}

		return doc;
	}

	public void valoriserIndex(String codeTypeIndex, Object valeur) {
		if (codeTypeIndex == null) {
			throw new DocumentException("Code type index nul");
		}
		TypeIndex typeIndex = TypeIndex.find("byCode", codeTypeIndex).first();
		if (typeIndex == null) {
			throw new DocumentException("Code type index " + codeTypeIndex
					+ " non trouvé");
		}

		try {
			ficheIndex.put(codeTypeIndex, new Index(typeIndex, valeur));
		} catch (FormatException fe) {
			throw new DocumentException(fe);
		}
	}

	public Object getValeurIndex(String codeTypeIndex) {
		if (codeTypeIndex == null) {
			throw new DocumentException("Code type index nul");
		}

		if (!ficheIndex.containsKey(codeTypeIndex)) {
			throw new DocumentException(
					"Code type index non présent dans la fiche document");
		}
		try {
			return ficheIndex.get(codeTypeIndex).restituerValeur();
		} catch (FormatException fe) {
			throw new DocumentException(fe);
		}
	}

	public static List<Document> rechercherParDescription(final String critere)
			throws ParseException {

		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
				.getFullTextEntityManager(JPA.em());

		// create native Lucene query unsing the query DSL
		QueryBuilder qb = fullTextEntityManager.getSearchFactory()
				.buildQueryBuilder().forEntity(Document.class).get();
		org.apache.lucene.search.Query query = qb.keyword()
				.onFields("description").matching(critere).createQuery();

		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query persistenceQuery = fullTextEntityManager
				.createFullTextQuery(query, Document.class);

		// execute search
		return persistenceQuery.getResultList();
	}
}
