package citu

import java.util.ArrayList;

class EnergyItem {
	
	String collector
	String type
	
	ArrayList energyReadings = new ArrayList()


	static transients = ['energyReadings']
	
	
    static constraints = {
    }
}
