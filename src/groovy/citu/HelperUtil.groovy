package citu

class HelperUtil {
	
	static Premise getPremise(Map params) {
		Premise premiseInstance
		if (params.flatNo) {
			premiseInstance = Premise.findByFlatNo(params.flatNo)
		} else if (params.macAddress) {
			def stbInstance = SetTopBox.findByMacAddress(params.macAddress)
			premiseInstance = stbInstance.premise
		} else {
			return premiseInstance
		}
	}
	
	static Map createElectricityMap(Premise premiseInstance) {
		ArrayList electricityReadings = new ArrayList()
		premiseInstance.elecReadings.each { reading ->
			electricityReadings.add([readingValue:reading.readingValueElec, dateTime:reading.fileDate])
		}
		def electricity = [:]
		electricity.put("elecReadings", electricityReadings)
		electricity.put("elecTotalUsage", BillUtil.calcTotal(premiseInstance.elecReadings.readingValueElec))
		electricity.put("elecTotalCost", BillUtil.calcTotalElecCost(premiseInstance.elecReadings.readingValueElec))
		return electricity
	}	
}
