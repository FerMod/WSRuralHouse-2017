package configuration.util;

import java.text.NumberFormat;
import java.util.Locale;

public enum CurrencyLocale {
	EN("en", "US"),
	ES("es", "ES"),
	EUS("es", "ES");

	private final Locale locale;

	CurrencyLocale(String language, String country) {
		this.locale = new Locale(language, country);
	}

	public Locale getCurrencyLocale() {
		return locale;
	}

	public NumberFormat getNumberFormatter() {
		return NumberFormat.getCurrencyInstance(getCurrencyLocale());
	}

}
