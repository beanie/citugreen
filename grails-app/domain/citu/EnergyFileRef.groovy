package citu

class EnergyFileRef {

    URL urlPath
	String category
	
	static constraints = {
		category inList: ["Water", "Electricity", "Heat", "energyIn"]
    }
}
