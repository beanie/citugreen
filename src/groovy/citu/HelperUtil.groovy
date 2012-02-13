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

	static Map createHeatMap(Premise premiseInstance) {
		ArrayList heatReadings = new ArrayList()
		premiseInstance.heatReadings.each { reading ->
			heatReadings.add([readingValue:reading.readingValueHeat, dateTime:reading.dateCreated])
		}
		def heat = [:]
		heat.put("heatReadings", heatReadings)
		heat.put("heatTotalUsage", BillUtil.calcTotal(premiseInstance.heatReadings.readingValueHeat))
		heat.put("heatTotalCost", BillUtil.calcTotalElecCost(premiseInstance.heatReadings.readingValueHeat))
		return heat
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
		electricity.put("elecAverageUsage", BillUtil.calcTotal(premiseInstance.elecReadings.readingValueElec)/electricityReadings.size())
		return electricity
	}

	static Map createWaterMap(Premise premiseInstance) {
		ArrayList waterReadings = new ArrayList()
		premiseInstance.waterReadings.each { reading ->
			waterReadings.add([readingValueHot:reading.readingValueHot, readingValueCold:reading.readingValueCold, readingValueGrey:reading.readingValueGrey, dateTime:reading.fileDate])
		}
		def water = [:]
		water.put("waterReadings", waterReadings)
		water.put("waterTotalUsageHot", BillUtil.calcTotal(premiseInstance.waterReadings.readingValueHot))
		water.put("waterTotalUsageCold", BillUtil.calcTotal(premiseInstance.waterReadings.readingValueCold))
		water.put("waterTotalUsageGrey", BillUtil.calcTotal(premiseInstance.waterReadings.readingValueGrey))
		water.put("waterTotalCostHot", BillUtil.calcTotalElecCost(premiseInstance.waterReadings.readingValueHot))
		water.put("waterTotalCostCold", BillUtil.calcTotalElecCost(premiseInstance.waterReadings.readingValueCold))
		water.put("waterTotalCostGrey", BillUtil.calcTotalElecCost(premiseInstance.waterReadings.readingValueGrey))
		water.put("waterTotalCost", (water.waterTotalCostHot + water.waterTotalCostCold + water.waterTotalCostGrey))
		return water
	}

}
