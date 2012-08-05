package models.typeDeDonnees;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.exception.FormatException;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;

@TypeDef(typeClass = GenericEnumUserType.class, parameters = {
		@Parameter(name = "enumClass", value = "models.typeDeDonnees.Format"),
		@Parameter(name = "identifierMethod", value = "getFormatCode"),
		@Parameter(name = "valueOfMethod", value = "creerFormat") })
public enum Format {

	ENTIER("INT", new Convertisseur() {

		@Override
		public Object convertir(String valeurTextuelle) throws FormatException {
			// String -> Integer
			return Integer.parseInt(valeurTextuelle);
		}

		@Override
		public String convertirPourStockage(Object valeurReelle)
				throws FormatException {
			// Integer -> String
			return ((Integer) valeurReelle).toString();
		}

	}), DECIMAL("DEC", new Convertisseur() {

		@Override
		public Object convertir(String valeurTextuelle) throws FormatException {
			// String -> Float
			return Float.parseFloat(valeurTextuelle);
		}

		@Override
		public String convertirPourStockage(Object valeurReelle)
				throws FormatException {
			// Float -> String
			return ((Float) valeurReelle).toString();
		}

	}), DATE("DTE", new Convertisseur() {
		private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		@Override
		public Object convertir(String valeurTextuelle) throws FormatException {
			// String -> Date(JJ/MM/AAAA)
			try {
				return sdf.parse(valeurTextuelle);
			} catch (ParseException e) {
				throw new FormatException(e);
			}
		}

		@Override
		public String convertirPourStockage(Object valeurReelle)
				throws FormatException {
			// Date(JJ/MM/AAAA) -> String
			return sdf.format((Date) valeurReelle);
		}

	}), DATE_HEURE("DTE_HEURE", new Convertisseur() {
		private SimpleDateFormat sdf = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss");

		@Override
		public Object convertir(String valeurTextuelle) throws FormatException {
			// / String -> Date(JJ/MM/AAAA HH:MM:SS)
			try {
				return sdf.parse(valeurTextuelle);
			} catch (ParseException e) {
				throw new FormatException(e);
			}
		}

		@Override
		public String convertirPourStockage(Object valeurReelle)
				throws FormatException {
			// Date(JJ/MM/AAAA HH:MM:SS) -> String
			return sdf.format((Date) valeurReelle);
		}

	}), MONTANT("MONEY", new Convertisseur() {

		@Override
		public Object convertir(String valeurTextuelle) throws FormatException {
			// String -> BigDecimal
			return new BigDecimal(valeurTextuelle);
		}

		@Override
		public String convertirPourStockage(Object valeurReelle)
				throws FormatException {
			// BigDecimal -> String
			return ((BigDecimal) valeurReelle).toString();
		}

	});

	private Convertisseur convert;
	private String code;

	private static Map<String, Format> typesDeclares = new HashMap<String, Format>();

	static {
		typesDeclares.put("INT", ENTIER);
		typesDeclares.put("DEC", DECIMAL);
		typesDeclares.put("DTE", DATE);
		typesDeclares.put("DTE_HEURE", DATE_HEURE);
		typesDeclares.put("MONEY", MONTANT);
	}

	public String getCode() {
		return code;
	}

	private Format(String code, Convertisseur convert) {
		this.convert = convert;
		this.code = code;
	}

	public Object convertir(String valeurTextuelle) throws FormatException {
		if ((valeurTextuelle == null) || (valeurTextuelle.isEmpty())) {
			throw new FormatException(
					"Impossible de convertir une valeur nulle");
		}
		return convert.convertir(valeurTextuelle);
	}

	public String convertirPourStockage(Object valeurReelle)
			throws FormatException {
		if (valeurReelle == null) {
			throw new FormatException(
					"Impossible de convertir une valeur nulle");
		}
		return convert.convertirPourStockage(valeurReelle);
	}

	interface Convertisseur {
		Object convertir(String valeurTextuelle) throws FormatException;

		String convertirPourStockage(Object valeurReelle)
				throws FormatException;
	}

	public static Format creerFormat(String code) throws FormatException {
		Format format = typesDeclares.get(code);
		if (format == null) {
			throw new FormatException("Format " + code + " inconnu");
		}
		return format;
	}

}
