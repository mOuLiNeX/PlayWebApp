package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.exception.FormatException;
import models.referentiel.TypeIndex;
import models.typeDeDonnees.Format;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name = "tbl_index")
public class Index extends Model {
	@Required
	@MaxSize(value = 15)
	public String code;

	@Required
	public String valeurStockage;

	@Required
	@ManyToOne
	public TypeIndex type;

	Index(TypeIndex type) {
		this.type = type;
	}

	Index(TypeIndex type, Object valeur) throws FormatException {
		this(type);
		valeurStockage = type.typeDonnee.convertirPourStockage(valeur);
	}

	public Object restituerValeur() throws FormatException {
		return type.typeDonnee.convertir(valeurStockage);
	}
}
