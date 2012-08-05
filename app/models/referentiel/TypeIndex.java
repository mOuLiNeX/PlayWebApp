package models.referentiel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import models.typeDeDonnees.Format;

@Entity
@Table(name = "ref_index")
public class TypeIndex extends Model {
	@MaxSize(value = 15)
	@Required
	@Column(length = 15, nullable = false, updatable = false)
	@Field(index = org.hibernate.search.annotations.Index.UN_TOKENIZED)
	public final String code;

	@MaxSize(value = 1000)
	@Field(index = org.hibernate.search.annotations.Index.TOKENIZED, store = Store.YES)
	public String description;

	@Required
	public final Format typeDonnee;

	public TypeIndex(String code, Format typeDonnee) {
		super();
		this.code = code;
		this.typeDonnee = typeDonnee;
	}
}
