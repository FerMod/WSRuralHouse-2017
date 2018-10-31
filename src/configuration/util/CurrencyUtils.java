package configuration.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

public class CurrencyUtils implements Serializable {

	/**
	 * Without <code>volatile</code> modifier, its possible for another 
	 * thread to see half initialized state of <code>instance</code> variable, 
	 * but with <code>volatile</code> variable guarantees that all the write will 
	 * happen in volatile before any read of the variable.
	 */
	private volatile static CurrencyUtils instance;

	private static SortedMap<Currency, Locale> currencyLocaleMap;

	private CurrencyUtils(){
		
		// Prevent from the reflection api.
		if (instance != null){
			throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
		}

		initClass();
		
	}

	private static void initClass() {

		currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
			public int compare(Currency c1, Currency c2){
				return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
			}
		});

		for (Locale locale : Locale.getAvailableLocales()) {
			try {
				Currency currency = Currency.getInstance(locale);
				currencyLocaleMap.put(currency, locale);
			}catch (Exception e){
			}
		}

	}

	public synchronized static CurrencyUtils getInstance(){
	
		// Double check locking pattern
		if (instance == null) { // First check
	
			synchronized (CurrencyUtils.class) { // Second check
				// If there is no instance available, create new one
				if(instance == null) {
					instance = new CurrencyUtils();
				}
			}
	
		}
	
		return instance;
	}
	
	public String getCurrencySymbol(Locale locale) {
		Currency currency = Currency.getInstance(locale);
		System.out.println(locale+ ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
		return currency.getSymbol(currencyLocaleMap.get(currency));
	}

	public String getCurrencySymbol(String currencyCode) {
		Currency currency = Currency.getInstance(currencyCode);
		System.out.println(currencyCode+ ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
		return currency.getSymbol(currencyLocaleMap.get(currency));
	}

	// Ensure that nobody can create another instance by serializing and deserializing the singleton.
	protected CurrencyUtils readResolve() {
		return getInstance();
	}

	/**
	 * Auto-generated serial version ID
	 */
	private static final long serialVersionUID = 4626542650827076777L;

}
