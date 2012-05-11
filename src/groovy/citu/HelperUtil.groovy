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
	
	static Double nullCheck(Double dbl) {
		return (!dbl) ? 0 : dbl
	}
	
	static Map generateElecSummary(Double sum, Map highlows, Double avg, Double est) {
		def electricity = [:]
		electricity.put("currentCost", BillUtil.calcElecPriceByVolume(nullCheck(sum)))
		electricity.put("averageCost", BillUtil.calcElecPriceByVolume(nullCheck(avg)*24))
		electricity.put("estimateCost", BillUtil.calcElecPriceByVolume(nullCheck(est)))
		electricity.put("swingLow", BillUtil.calcElecPriceByVolume(nullCheck(highlows?.elec?.low)))
		electricity.put("swingHigh", BillUtil.calcElecPriceByVolume(nullCheck(highlows?.elec?.high)))
		return electricity
	}
	
	static Map generateHeatSummary(Double sum, Map highlows, Double avg, Double est) {
		def heat = [:]
		heat.put("currentCost", BillUtil.calcHeatPriceByVolume(nullCheck(sum)))
		heat.put("averageCost", BillUtil.calcHeatPriceByVolume(nullCheck(avg)*24))
		heat.put("estimateCost", BillUtil.calcHeatPriceByVolume(nullCheck(est)))
		heat.put("swingLow", BillUtil.calcHeatPriceByVolume(nullCheck(highlows?.heat?.low)))
		heat.put("swingHigh", BillUtil.calcHeatPriceByVolume(nullCheck(highlows?.heat?.high)))
		return heat
	}

	
	static Map generateColdWaterSummary(Double sum, Map highlows, Double avg, Double est) {
		def coldWater = [:]
		coldWater.put("currentCost", BillUtil.calcColdWaterPriceByVolume(nullCheck(sum)))
		coldWater.put("averageCost", BillUtil.calcColdWaterPriceByVolume(nullCheck(avg)*24))
		coldWater.put("estimateCost", BillUtil.calcColdWaterPriceByVolume(nullCheck(est)))
		coldWater.put("swingLow", BillUtil.calcColdWaterPriceByVolume(nullCheck(highlows?.coldWater?.low)))
		coldWater.put("swingHigh", BillUtil.calcColdWaterPriceByVolume(nullCheck(highlows?.coldWater?.high)))
		return coldWater
	}
	
	static Map generateHotWaterSummary(Double sum, Map highlows, Double avg, Double est) {
		def hotWater = [:]		
		hotWater.put("currentCost", BillUtil.calcHotWaterPriceByVolume(nullCheck(sum)))
		hotWater.put("averageCost", BillUtil.calcHotWaterPriceByVolume(nullCheck(avg)*24))
		hotWater.put("estimateCost", BillUtil.calcHotWaterPriceByVolume(nullCheck(est)))
		hotWater.put("swingLow", BillUtil.calcHotWaterPriceByVolume(nullCheck(highlows?.hotWater?.low)))
		hotWater.put("swingHigh", BillUtil.calcHotWaterPriceByVolume(nullCheck(highlows?.hotWater?.high)))
		return hotWater
	}
	
	static Map generateGreyWaterSummary(Double sum, Map highlows, Double avg, Double est) {
		def greyWater = [:]
		greyWater.put("currentCost", BillUtil.calcGreyWaterPriceByVolume(nullCheck(sum)))
		greyWater.put("averageCost", BillUtil.calcGreyWaterPriceByVolume(nullCheck(avg)*24))
		greyWater.put("estimateCost", BillUtil.calcGreyWaterPriceByVolume(nullCheck(est)))
		greyWater.put("swingLow", BillUtil.calcGreyWaterPriceByVolume(nullCheck(highlows?.greyWater?.low)))
		greyWater.put("swingHigh", BillUtil.calcGreyWaterPriceByVolume(nullCheck(highlows?.greyWater?.high)))
		return greyWater
	}
	
	static Map createElectricityMap(Premise premiseInstance, Map premise, Map swingData) {
		ArrayList electricityReadings = new ArrayList()
		premiseInstance.elecReadings.each { reading ->
			electricityReadings.add([readingValue:reading.readingValueElec, dateTime:reading.dateCreated])
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
	
	static Map createHeatMap(Premise premiseInstance, Map premise, Map swingData) {
		ArrayList heatReadings = new ArrayList()
		premiseInstance.heatReadings.each { reading ->
			heatReadings.add([readingValue:reading.readingValueHeat, dateTime:reading.dateCreated])
			
		}
		premise.put("readings", heatReadings)
		premise.put("heatTotalUsage", BillUtil.calcTotal(premiseInstance.heatReadings.readingValueHeat)) //done
		premise.put("heatTotalCost", BillUtil.calcTotalHeatCost(premiseInstance.heatReadings.readingValueHeat)) //done
		premise.put("heatAverageCost", BillUtil.calcHeatPriceByVolume((premise.heatTotalUsage/premiseInstance.heatReadings.size())*premiseInstance.heatReadings.size()))
		premise.put("heatSwingLow", swingData.swingLow) //done
		premise.put("heatSwingHigh", swingData.swingHigh) //done
		premise.put("heaEstimatedCost", "TODO")
		premise.put("heatAvgUsage", premise.heatTotalUsage/premiseInstance.heatReadings.size()) //done
		premise.put("heatPeerAvg", swingData.peerAvg/premiseInstance.heatReadings.size())
		return premise
	}
	
	static Map createWaterMap(Premise premiseInstance, Map premise, Map swingData) {
		ArrayList waterReadings = new ArrayList()
		premiseInstance.waterReadings.each { reading ->
			waterReadings.add([readingValueHot:reading.readingValueHot, readingValueCold:reading.readingValueCold, readingValueGrey:reading.readingValueGrey, dateTime:reading.dateCreated])
		}
		
		
		
		premise.put("readings", waterReadings)
		premise.put("waterTotalUsageHot", BillUtil.calcTotal(premiseInstance.waterReadings.readingValueHot))
		premise.put("waterTotalUsageCold", BillUtil.calcTotal(premiseInstance.waterReadings.readingValueCold))
		premise.put("waterTotalUsageGrey", BillUtil.calcTotal(premiseInstance.waterReadings.readingValueGrey))
		premise.put("waterTotalCostHot", BillUtil.calcTotalHotWaterCost(premiseInstance.waterReadings.readingValueHot))
		premise.put("waterTotalCostCold", BillUtil.calcTotalColdWaterCost(premiseInstance.waterReadings.readingValueCold))
		premise.put("waterTotalCostGrey", BillUtil.calcTotalGreyWaterCost(premiseInstance.waterReadings.readingValueGrey))
		premise.put("waterAverageUsageHot", BillUtil.calcHotWaterPriceByVolume((premise.waterTotalUsageHot/premiseInstance.waterReadings.size())*premiseInstance.waterReadings.size()))
		premise.put("waterAverageUsageCold", BillUtil.calcColdWaterPriceByVolume((premise.waterTotalUsageCold/premiseInstance.waterReadings.size())*premiseInstance.waterReadings.size()))
		premise.put("waterAverageUsageGrey", BillUtil.calcGreyWaterPriceByVolume((premise.waterTotalUsageGrey/premiseInstance.waterReadings.size())*premiseInstance.waterReadings.size()))
		premise.put("waterAvgUsageHot", premise.waterTotalUsageHot/premiseInstance.waterReadings.size())
		premise.put("waterAvgUsageCold", premise.waterTotalUsageCold/premiseInstance.waterReadings.size())
		premise.put("waterAvgUsageGrey", premise.waterTotalUsageHot/premiseInstance.waterReadings.size())
		premise.put("waterPeerAvg", swingData.peerAvg/premiseInstance.waterReadings.size())

		return premise
	}
	
}
