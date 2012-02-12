package citu

class Reading {

	Date fileDate
	Date dateCreated
	
	static belongsTo = [premise:Premise]
	
	String toString() {
		return premise.flatNo +" on "+ dateCreated
	}

    static constraints = {
		premise(blank:true, nullable: true)
		fileDate(blank:true, nullable: true)
    }
}
