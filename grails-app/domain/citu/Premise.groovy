package citu

class Premise {

    // TODO extend a premise to flat / office etc.
	String flatNo
	String addressLine1
	String addressLine2
	String postCode
	String core
	String premiseType
	int bedrooms
	int bathrooms
	int squareArea
	
	boolean occupied

	ArrayList elecReadings = new ArrayList()
	ArrayList waterReadings = new ArrayList()
	ArrayList heatReadings = new ArrayList()

	User user

	String toString() {
		return flatNo
	}

	static hasMany=[setTobBoxes:SetTopBox]

    static constraints = {
		addressLine1(blank:false, nullable: false)
		addressLine2(blank:false, nullable: false)
    }

	static transients = ['elecReadings', 'waterReadings', 'heatReadings']
}
