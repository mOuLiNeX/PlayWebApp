package models.typeDeDonnees;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import models.exception.FormatException;

import org.apache.commons.lang.time.FastDateFormat;
import org.junit.Test;

import play.test.UnitTest;

public class FormatTest extends UnitTest {
	@Test
	public void conversionInteger() throws FormatException {
		Integer donneeDeTest = 12;
		assertEquals(donneeDeTest, Format.ENTIER.convertir(Format.ENTIER
				.convertirPourStockage(donneeDeTest)));

	}

	@Test
	public void conversionFloat() throws FormatException {
		Float donneeDeTest = 2.5F;
		assertEquals(donneeDeTest, Format.DECIMAL.convertir(Format.DECIMAL
				.convertirPourStockage(donneeDeTest)));
	}

	@Test
	public void conversionDate() throws FormatException {
		Calendar cal = new GregorianCalendar(1976, Calendar.NOVEMBER, 13);
		assertEquals(cal.getTime(), Format.DATE.convertir(Format.DATE
				.convertirPourStockage(cal.getTime())));
	}

	@Test
	public void conversionDatetime() throws FormatException {
		Calendar cal = new GregorianCalendar(1976, Calendar.NOVEMBER, 13, 12,
				12, 12);
		assertEquals(cal.getTime(),
				Format.DATE_HEURE.convertir(Format.DATE_HEURE
						.convertirPourStockage(cal.getTime())));
	}

	@Test
	public void conversionAmount() throws FormatException {
		BigDecimal donneeDeTest = new BigDecimal(4095.1);
		assertEquals(donneeDeTest, Format.MONTANT.convertir(Format.MONTANT
				.convertirPourStockage(donneeDeTest)));
	}
}
