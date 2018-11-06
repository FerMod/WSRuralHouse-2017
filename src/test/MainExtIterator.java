package test;

import businessLogic.ApplicationFacadeFactory;
import businessLogic.ApplicationFacadeInterface;
import configuration.Config;
import configuration.ConfigXML;
import domain.RuralHouse;
import domain.util.ExtendedIterator;

public class MainExtIterator {

	public static void main(String[] args) {
		
		Config config = ConfigXML.getInstance();
		ApplicationFacadeInterface facadeInterface = ApplicationFacadeFactory.createApplicationFacade(config);
		ExtendedIterator<RuralHouse> i = facadeInterface.ruralHouseIterator();

		RuralHouse rh;
		
		System.out.println("**Casas rurales mostradas en orden por defecto**");
		i.goFirst();
		while (i.hasNext()){
			rh=(RuralHouse) i.next();
			System.out.println(rh.toString());
		}
		
		System.out.println("**Casas rurales mostradas en orden inverso**");
		i.goLast();
		while (i.hasPrevious()){
			rh=(RuralHouse) i.previous();
			System.out.println(rh.toString());
		}

	}
}
