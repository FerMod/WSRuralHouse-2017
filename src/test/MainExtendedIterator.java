package test;

import businessLogic.ApplicationFacadeFactory;
import businessLogic.ApplicationFacadeInterface;
import configuration.Config;
import configuration.ConfigXML;
import domain.RuralHouse;
import domain.util.ExtendedIterator;

public class MainExtendedIterator {

	public static void main(String[] args) {
		
		Config config = ConfigXML.getInstance();
		ApplicationFacadeInterface facadeInterface = ApplicationFacadeFactory.createApplicationFacade(config);
		ExtendedIterator<RuralHouse> it = facadeInterface.ruralHouseIterator();

		RuralHouse rh;
		
		System.out.println("** Casas rurales mostradas en orden por defecto **");
		it.goFirst();
		while (it.hasNext()){
			rh = it.next();
			System.out.println(rh.toString());
		}
		
		System.out.println("** Casas rurales mostradas en orden inverso **");
		it.goLast();
		while (it.hasPrevious()){
			rh = it.previous();
			System.out.println(rh.toString());
		}

	}
}
