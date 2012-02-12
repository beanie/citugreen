package citu

class Premise {
	
	String flatNo
	String addressLine1
	String addressLine2
	String postCode
	int rooms
	int squareArea
	
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
