package citu

import java.util.Date;
import java.util.Map;

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

	static Map createPremiseSkeletonMap(Premise premiseInstance) {
		def user = [firstName:premiseInstance.user.firstName, lastName:premiseInstance.user.lastName, userName:premiseInstance.user.userName, contactEmail:premiseInstance.user.contactEmail, userId: premiseInstance.user.id]
		def premise = [flatNo:premiseInstance.flatNo, addressLine1:premiseInstance.addressLine1, addressLine1:premiseInstance.addressLine2, postCode:premiseInstance.postCode, bedrooms:premiseInstance.bedrooms, squareArea:premiseInstance.squareArea, user:user]
		return premise
	}
	
	static Map generateElecSummary(Double sum, Map highlows, Double avg) {
		def electricity = [:]
		electricity.put("currentCost", BillUtil.calcElecPriceByVolume(sum))
		electricity.put("averageCost", BillUtil.calcElecPriceByVolume(avg*24))
		electricity.put("estimateCost", BillUtil.calcElecPriceByVolume(sum+2))
		electricity.put("swingLow", BillUtil.calcElecPriceByVolume(highlows.elec.low))
		electricity.put("swingHigh", BillUtil.calcElecPriceByVolume(highlows.elec.high))
		return electricity
	}
	
	static Map generateColdWaterSummary(Double sum, Map highlows, Double avg) {
		def coldWater = [:]
		coldWater.put("currentCost", BillUtil.calcColdWaterPriceByVolume(sum))
		coldWater.put("averageCost", BillUtil.calcColdWaterPriceByVolume(avg*24))
		coldWater.put("estimateCost", BillUtil.calcColdWaterPriceByVolume(sum+1))
		coldWater.put("swingLow", BillUtil.calcColdWaterPriceByVolume(highlows.coldWater.low))
		coldWater.put("swingHigh", BillUtil.calcColdWaterPriceByVolume(highlows.coldWater.high))
		return coldWater
	}
	
	static Map generateHotWaterSummary(Double sum, Map highlows, Double avg) {
		def hotWater = [:]
		hotWater.put("currentCost", BillUtil.calcHotWaterPriceByVolume(sum))
		hotWater.put("averageCost", BillUtil.calcHotWaterPriceByVolume(avg*24))
		hotWater.put("estimateCost", BillUtil.calcHotWaterPriceByVolume(sum+1))
		hotWater.put("swingLow", BillUtil.calcHotWaterPriceByVolume(highlows.hotWater.low))
		hotWater.put("swingHigh", BillUtil.calcHotWaterPriceByVolume(highlows.hotWater.high))
		return hotWater
	}
	
	static Map generateGreyWaterSummary(Double sum, Map highlows, Double avg) {
		def greyWater = [:]
		greyWater.put("currentCost", BillUtil.calcGreyWaterPriceByVolume(sum))
		greyWater.put("averageCost", BillUtil.calcGreyWaterPriceByVolume(avg*24))
		greyWater.put("estimateCost", BillUtil.calcGreyWaterPriceByVolume(sum+1))
		greyWater.put("swingLow", BillUtil.calcGreyWaterPriceByVolume(highlows.greyWater.low))
		greyWater.put("swingHigh", BillUtil.calcGreyWaterPriceByVolume(highlows.greyWater.high))
		return greyWater
	}
	
	static Map createElectricityMap(Premise premiseInstance, Map premise, Map swingData) {
		ArrayList electricityReadings = new ArrayList()
		premiseInstance.elecReadings.each { reading ->
			electricityReadings.add([readingValue:reading.readingValueElec, dateTime:reading.fileDate])
		}
		premise.put("readings", electricityReadings)
		premise.put("elecTotalUsage", BillUtil.calcTotal(premiseInstance.elecReadings.readingValueElec)) //done
		premise.put("elecTotalCost", BillUtil.calcTotalElecCost(premiseInstance.elecReadings.readingValueElec)) //done
		premise.put("elecAverageCost", BillUtil.calcElecPriceByVolume((premise.elecTotalUsage/premiseInstance.elecReadings.size())*premiseInstance.elecReadings.size()))
		premise.put("elecSwingLow", swingData.swingLow) //done
		premise.put("elecSwingHigh", swingData.swingHigh) //done
		premise.put("elecEstimatedCost", "TODO")
		premise.put("elecAvgUsage", premise.elecTotalUsage/premiseInstance.elecReadings.size()) //done
		premise.put("elecPeerAvg", swingData.peerAvg/premiseInstance.elecReadings.size())
		return premise
	}

	
	
	
	static Map createWaterMap(Premise premiseInstance, Map premise, Map swingData) {
		ArrayList waterReadings = new ArrayList()
		premiseInstance.waterReadings.each { reading ->
			waterReadings.add([readingValueHot:reading.readingValueHot, readingValueCold:reading.readingValueCold, readingValueGrey:reading.readingValueGrey, dateTime:reading.fileDate])
		}
		premise.put("readings", waterReadings)
		//water.put("waterTotalUsageHot", BillUtil.calcTotal(premiseInstance.waterReadings.readingValueHot))
		//water.put("waterTotalUsageCold", BillUtil.calcTotal(premiseInstance.waterReadings.readingValueCold))
		//water.put("waterTotalUsageGrey", BillUtil.calcTotal(premiseInstance.waterReadings.readingValueGrey))
		//water.put("waterTotalCostHot", BillUtil.calcTotalElecCost(premiseInstance.waterReadings.readingValueHot))
		//water.put("waterTotalCostCold", BillUtil.calcTotalElecCost(premiseInstance.waterReadings.readingValueCold))
		//water.put("waterTotalCostGrey", BillUtil.calcTotalElecCost(premiseInstance.waterReadings.readingValueGrey))
		//water.put("waterAverageUsageHot", (water.waterTotalUsageHot/waterReadings.size()))
		//water.put("waterAverageUsageCold", (water.waterTotalUsageCold/waterReadings.size()))
		//water.put("waterAverageUsageGrey", (water.waterTotalUsageGrey/waterReadings.size()))
		//water.put("waterAverageCostHot", (water.waterTotalCostHot/waterReadings.size()))
		//water.put("waterAverageCostCold", (water.waterTotalCostCold/waterReadings.size()))
		//water.put("waterAverageCostGrey", (water.waterTotalCostGrey/waterReadings.size()))
		//water.put("waterCombinedTotalCost", (water.waterTotalCostHot + water.waterTotalCostCold + water.waterTotalCostGrey))
		return premise
	}
	
	static Map createHeatMap(Premise premiseInstance) {
		ArrayList heatReadings = new ArrayList()
		premiseInstance.heatReadings.each { reading ->
			heatReadings.add([readingValue:reading.readingValueHeat, dateTime:reading.dateCreated])
		}
		def heat = [:]
		heat.put("heatReadings", heatReadings)
		heat.put("heatTotalUsage", BillUtil.calcTotal(premiseInstance.heatReadings.readingValueHeat))
		heat.put("heatTotalCost", BillUtil.calcTotalHeatCost(premiseInstance.heatReadings.readingValueHeat))
		heat.put("heatAverageUsage", heat.heatTotalUsage/heatReadings.size())
		heat.put("heatAverageCost", heat.heatTotalCost/heatReadings.size())
		return heat
	}

}
