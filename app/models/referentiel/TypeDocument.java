package models.referentiel;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name = "ref_document")
@Indexed
public class TypeDocument extends Model {

	@MaxSize(value=15)
	@Required
	@Column(length=15, nullable=false, updatable=false)
	@Field(index = org.hibernate.search.annotations.Index.UN_TOKENIZED)
	public String code;

	@MaxSize(value = 1000)
	@Field(index = org.hibernate.search.annotations.Index.TOKENIZED, store = Store.YES)
	public String description;

	@OneToMany(cascade = CascadeType.ALL)
	@MapKey(name = "code")
	@IndexedEmbedded
	private Map<String, TypeIndex> ficheIndex = new HashMap<String, TypeIndex>();

	public Map<String, TypeIndex> getFicheIndex() {
		return ficheIndex;
	}

	public TypeDocument(String code) {
		super();
		this.code = code;
	}

	public void ajouterTypeIndex(TypeIndex typeIndex) {
		ficheIndex.put(typeIndex.code, typeIndex);
	}

	public void supprimerTypeIndex(TypeIndex typeIndex) {
		ficheIndex.remove(typeIndex.code);
		// TODO Impacts sur les documents existants ?
	}

}
