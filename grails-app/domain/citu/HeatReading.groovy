package citu

class HeatReading extends Reading {
	
	Float readingValueHeat
	Float realReadingHeat

    static constraints = {
		readingValueHeat(nullable: true)
		realReadingHeat(nullable: true)
    }
}
