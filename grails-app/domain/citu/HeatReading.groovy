package citu

class HeatReading extends Reading {
	
	Float readingValueHeat

    static constraints = {
		readingValueHeat(nullable: true)
    }
}
