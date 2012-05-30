package citu

import java.util.Date;

class EnergyReading {
	
	Date fileDate
	Date dateCreated
	Float readingValueIn
	Float realReadingIn
	
	static belongsTo = [energyitem:EnergyItem]
	

    static constraints = {
    }
}
