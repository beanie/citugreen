package citu

class Premise {

    String flatNo
	String addressLine1
	String addressLine2
	String postCode
	String core
	String premiseType
	int bedrooms
	int bathrooms
	int squareArea
	
	String rank
	String prevWeekRank
	Float rankValue
	
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
		rank(blank:true, nullable: true)
		prevWeekRank(blank:true, nullable: true)
		rankValue(blank:true, nullable: true)
    }

	static transients = ['elecReadings', 'waterReadings', 'heatReadings']
}
