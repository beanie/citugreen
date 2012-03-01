package citu

import java.text.*
import java.util.Date;
import java.util.Map;

import org.joda.time.*

import grails.converters.*

class PremiseController extends BaseController {

	def beforeInterceptor = [action:this.&auth, except:["getReadingsByDate", "getReadingsSummary"]]

	def scaffold = true

	def summary = {

		def Premise premiseInstance = HelperUtil.getPremise(params)

		if (!premiseInstance) {
			render("invalid premise ID")
		} else {
			[simpleView: simpleViewMap(params)]
		}
	}

	def summary5 = {

		def Premise premiseInstance = HelperUtil.getPremise(params)

		if (!premiseInstance) {
			render("invalid premise ID")
		} else {
			[simpleView: simpleViewMap(params)]
		}
	}

	def getReadingsByDate = {

		def Premise premiseInstance = HelperUtil.getPremise(params)

		if (!premiseInstance) {
			render("invalid premise ID")
		} else {
			render simpleViewMap(params) as JSON
		}
	}
	
	def getReadingsSummary = {
		
		def Premise premiseInstance = HelperUtil.getPremise(params)
		
		if (!premiseInstance) {
			render("invalid premise ID")
		} else {
			
		TarrifList tarrifList = TarrifList.get(1)
		
		def now = new DateTime()
			now = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0, 0)
			def endOfDay = now.plusDays(1).minusSeconds(1)	

			premiseInstance.elecReadings = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
			premiseInstance.waterReadings = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
			//TODO add Heat query
			
			def premise = HelperUtil.createPremiseSkeletonMap(premiseInstance)
			def highlows = getHighLow(premiseInstance.bedrooms, now.toDate(), endOfDay.toDate())
			
			//TODO neater way to get the sum figure>???
			//def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate(), date2:endOfDay.toDate()])

			def electricity = [:]
			electricity.put("currentCost", BillUtil.calcTotalElecCost(premiseInstance.elecReadings.readingValueElec))
			electricity.put("averageCost", (BillUtil.calcTotalElecCost(premiseInstance.elecReadings.readingValueElec)-2))
			electricity.put("estimateCost", (BillUtil.calcTotalElecCost(premiseInstance.elecReadings.readingValueElec)+5))
			electricity.put("swingLow", (highlows.elec.low*tarrifList.elecTarrif))
			electricity.put("swingHigh", (highlows.elec.high*tarrifList.elecTarrif))
			premise.put("electricity", electricity)
			
			def greyWater = [:]
			greyWater.put("currentCost", BillUtil.calcTotalGreyWaterCost(premiseInstance.waterReadings.readingValueGrey))
			greyWater.put("averageCost", (BillUtil.calcTotalGreyWaterCost(premiseInstance.waterReadings.readingValueGrey)/2))
			greyWater.put("estimateCost", (BillUtil.calcTotalGreyWaterCost(premiseInstance.waterReadings.readingValueGrey)+1))
			greyWater.put("swingLow", (highlows.greyWater.low*tarrifList.greyWaterTarrif))
			greyWater.put("swingHigh", (highlows.greyWater.high*tarrifList.greyWaterTarrif))
			premise.put("greyWater", greyWater)
			
			def coldWater = [:]
			coldWater.put("currentCost", BillUtil.calcTotalColdWaterCost(premiseInstance.waterReadings.readingValueCold))
			coldWater.put("averageCost", (BillUtil.calcTotalColdWaterCost(premiseInstance.waterReadings.readingValueCold)/2))
			coldWater.put("estimateCost", (BillUtil.calcTotalColdWaterCost(premiseInstance.waterReadings.readingValueCold)+1))
			coldWater.put("swingLow", (highlows.coldWater.low*tarrifList.coldWaterTarrif))
			coldWater.put("swingHigh", (highlows.coldWater.high*tarrifList.coldWaterTarrif))
			premise.put("coldWater", coldWater)
			
			def hotWater = [:]
			hotWater.put("currentCost", BillUtil.calcTotalHotWaterCost(premiseInstance.waterReadings.readingValueHot))
			hotWater.put("averageCost", (BillUtil.calcTotalHotWaterCost(premiseInstance.waterReadings.readingValueHot)/2))
			hotWater.put("estimateCost", (BillUtil.calcTotalHotWaterCost(premiseInstance.waterReadings.readingValueHot)+1))
			hotWater.put("swingLow", (highlows.hotWater.low*tarrifList.hotWaterTarrif))
			hotWater.put("swingHigh", (highlows.hotWater.high*tarrifList.hotWaterTarrif))
			premise.put("hotWater", hotWater)
			
			//premise.put("water", HelperUtil.createWaterMap(premiseInstance))
			//TODO add Heat map
			
			render premise as JSON
		}
		
	}

	Map simpleViewMap(Map params) {

		def now
		def view

		def Premise premiseInstance = HelperUtil.getPremise(params)

		if (params.day && params.month && params.year) {

			now = new DateTime(params.int("year"), params.int("month"), params.int("day"), 0, 0, 0, 0)

			if (params.week) {
				log.info("simpleViewMap - Week View - with date : "+ now)
				view = createViewJsonObject(premiseInstance, now, "week")
			} else {
				log.info("simpleViewMap - Day View - with date : "+ now)
				view = createViewJsonObject(premiseInstance, now, "day")
			}
		} else if (params.month && params.year) {

			now = new DateTime(params.int("year"), params.int("month"), 1, 0, 0, 0, 0)
			log.info("simpleViewMap - Month View - with date : "+ now)
			view = createViewJsonObject(premiseInstance, now, "month")
		} else if (params.year) {

			now = new DateTime(params.int("year"), 1, 1, 0, 0, 0, 0)
			log.info("simpleViewMap - Year View - with date : "+ now)
			view = createViewJsonObject(premiseInstance, now, "year")
		} else {
			log.warn("Invalid date params sent")
			return null
		}

		return view
	}

	Map createViewJsonObject(Premise premiseInstance, DateTime now, String viewType) {

		def premise = createPremiseSkeletonMap(premiseInstance)

		def averages = [:]

		if (viewType.equals("day"))	{

			def endOfDay = now.plusDays(1).minusSeconds(1)

			premiseInstance.elecReadings = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
			premiseInstance.waterReadings = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
		
		} else if (viewType.equals("week")) {

			now = now.withDayOfWeek(DateTimeConstants.MONDAY)
			ArrayList electricityReadings = new ArrayList()
			ArrayList waterReadings = new ArrayList()
			7.times {
				def endOfDay = now.plusDays(1).minusSeconds(1)
				def elecDay = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
				def waterDay = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
				electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecDay.readingValueElec), fileDate:now.toDate()))
				waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterDay.readingValueHot), readingValueCold:BillUtil.calcTotal(waterDay.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterDay.readingValueGrey), fileDate:now.toDate()))
				now = now.plusDays(1)
			}
			premiseInstance.elecReadings = electricityReadings
			premiseInstance.waterReadings = waterReadings
		
		} else if (viewType.equals("month")) {
			
			ArrayList electricityReadings = new ArrayList()
			ArrayList waterReadings = new ArrayList()
			def monthDays = now.dayOfMonth().getMaximumValue()
			averages = getTrueAverage(premiseInstance.bedrooms, now.toDate(), now.plusMonths(1).minusSeconds(1).toDate())
			//getMaxMinAverage(premiseInstance.bedrooms, now.toDate(), now.plusMonths(1).minusSeconds(1).toDate())
			4.times {
				def endOfWeek = now.plusDays(7).minusSeconds(1)
				def elecWeek = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfWeek.toDate(), [sort:"fileDate", order:"desc"])
				def waterWeek = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfWeek.toDate(), [sort:"fileDate", order:"desc"])
				log.info("Dates : "+ now.toDate() +" : "+ endOfWeek.toDate() +" values : "+ BillUtil.calcTotal(elecWeek.readingValueElec))
				electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecWeek.readingValueElec), fileDate:now.toDate()))
				waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterWeek.readingValueHot), readingValueCold:BillUtil.calcTotal(waterWeek.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterWeek.readingValueGrey), fileDate:now.toDate()))
				now = now.plusDays(7)
			}

			if (monthDays > 28) {
				def diff = now.plusDays((monthDays - now.getDayOfMonth())+1).minusSeconds(1)
				def elecWeek = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), diff.toDate(), [sort:"fileDate", order:"desc"])
				def waterWeek = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), diff.toDate(), [sort:"fileDate", order:"desc"])
				log.info("Dates : "+ now.toDate() +" : "+ diff.toDate() +" values : "+ BillUtil.calcTotal(elecWeek.readingValueElec))
				electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecWeek.readingValueElec), fileDate:now.toDate()))
				waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterWeek.readingValueHot), readingValueCold:BillUtil.calcTotal(waterWeek.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterWeek.readingValueGrey), fileDate:now.toDate()))
			}
			premiseInstance.elecReadings = electricityReadings
			premiseInstance.waterReadings = waterReadings
		} else if (viewType.equals("year")) {
			ArrayList electricityReadings = new ArrayList()
			ArrayList waterReadings = new ArrayList()
			12.times {
				def monthDays = now.dayOfMonth().getMaximumValue()
				def monthEnd = now.plusDays((monthDays - now.getDayOfMonth())+1).minusSeconds(1)
				def elecMonth = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), monthEnd.toDate(), [sort:"fileDate", order:"desc"])
				def waterMonth = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), monthEnd.toDate(), [sort:"fileDate", order:"desc"])
				log.info("Dates : "+ now.toDate() +" : "+ monthEnd.toDate())
				electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecMonth.readingValueElec), fileDate:now.toDate()))
				waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterMonth.readingValueHot), readingValueCold:BillUtil.calcTotal(waterMonth.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterMonth.readingValueGrey), fileDate:now.toDate()))
				now = now.plusMonths(1)
			}
			premiseInstance.elecReadings = electricityReadings
			premiseInstance.waterReadings = waterReadings
		}
		premise.put("electricity", HelperUtil.createElectricityMap(premiseInstance))
		premise.put("water", HelperUtil.createWaterMap(premiseInstance))
		premise.put("buildingTotalAveragesForPeriod", averages)
		return premise
	}
	
	Map getHighLow(int noOfRooms, Date startDate, Date endDate) {
		def premises = Premise.executeQuery("select p.flatNo from Premise p where bedrooms = "+ noOfRooms)
		def readings = [:]
		def heatReadings = new ArrayList()
		def waterHotReadings = new ArrayList()
		def waterColdReadings = new ArrayList()
		def waterGreyReadings = new ArrayList()
		def elecReadings = new ArrayList()
		for (i in premises) {
			def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			def sumHeat = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ i +" and reading.dateCreated between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			def sumWater = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			// ignore Elec for empty values
			if (sumElec[0]) {
				elecReadings.add(sumElec[0])
			}
			// ignore Heat for empty values
			if (sumHeat[0]) {
				heatReadings.add(sumHeat[0])
			}
			// ignore Water for empty values
			if (sumWater[0][2]) {
				waterHotReadings.add(sumWater[0][0])
				waterColdReadings.add(sumWater[0][1])
				waterGreyReadings.add(sumWater[0][2])
			}
		}
		if (elecReadings.size() > 0) {
			elecReadings.sort()
			def elec = [low:elecReadings[0], high:elecReadings[elecReadings.size() - 1]]
			readings.put("elec", elec)
			log.debug("ELEC LOW : "+ elecReadings[0])
			log.debug("ELEC HIGH : "+ elecReadings[elecReadings.size() - 1])
		}
		if (heatReadings.size() > 0) {
			heatReadings.sort()
			def heat = [low:heatReadings[0], high:heatReadings[heatReadings.size() - 1]]
			readings.put("heat", heat)
			log.debug("HEAT LOW : "+ heatReadings[0])
			log.debug("HEAT HIGH : "+ heatReadings[heatReadings.size() - 1])
		}
		if (waterGreyReadings.size() > 0) {
			waterGreyReadings.sort()			
			def greyWater = [low:waterGreyReadings[0], high:waterGreyReadings[waterGreyReadings.size() - 1]]
			readings.put("greyWater", greyWater)
			log.debug("GREY LOW : "+ waterGreyReadings[0])
			log.debug("GREY HIGH : "+ waterGreyReadings[waterGreyReadings.size() - 1])
			
			waterHotReadings.sort()
			def hotWater = [low:waterHotReadings[0], high:waterHotReadings[waterHotReadings.size() - 1]]
			readings.put("hotWater", hotWater)
			log.debug("HOT LOW : "+ waterHotReadings[0])
			log.debug("HOT HIGH : "+ waterHotReadings[waterHotReadings.size() - 1])
			
			waterColdReadings.sort()
			def coldWater = [low:waterColdReadings[0], high:waterColdReadings[waterColdReadings.size() - 1]]
			readings.put("coldWater", coldWater)
			log.debug("COLD LOW : "+ waterColdReadings[0])
			log.debug("COLD HIGH : "+ waterColdReadings[waterColdReadings.size() - 1])
		}
		return readings
	}

	Map getTrueAverage(int noOfRooms, Date startDate, Date endDate) {
		def premises = Premise.executeQuery("select p.flatNo from Premise p where bedrooms = "+ noOfRooms)
		def averages = [:]
		def tmpElecFloat = 0
		def tmpHeatFloat = 0
		def tmpGreyFloat = 0
		def tmpColdFloat = 0
		def tmpHotFloat = 0
		def heatReadings = new ArrayList()
		def waterHotReadings = new ArrayList()
		def waterColdReadings = new ArrayList()
		def waterGreyReadings = new ArrayList()
		def elecReadings = new ArrayList()
		for (i in premises) {
			def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			def sumHeat = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ i +" and reading.dateCreated between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			def sumWater = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			// ignore Elec for empty values			
			if (sumElec[0]) {
				tmpElecFloat = (tmpElecFloat + sumElec[0])
				elecReadings.add(sumElec[0])
			}
			// ignore Heat for empty values
			if (sumHeat[0]) {
				tmpHeatFloat = (tmpHeatFloat + sumHeat[0])
				heatReadings.add(sumHeat[0])
			}
			// ignore Water for empty values
			if (sumWater[0][2]) {
				tmpHotFloat = (tmpHotFloat + sumWater[0][0])
				tmpColdFloat = (tmpColdFloat + sumWater[0][1])
				tmpGreyFloat = (tmpGreyFloat + sumWater[0][2])
				waterHotReadings.add(sumWater[0][0])
				waterColdReadings.add(sumWater[0][1])
				waterGreyReadings.add(sumWater[0][2])
			}
		}
		if (elecReadings.size() > 0) {
			elecReadings.sort()
			log.info("ELEC LOW : "+ elecReadings[0])
			log.info("ELEC HIGH : "+ elecReadings[elecReadings.size() - 1])
			averages.put("averageElec", (tmpElecFloat / elecReadings.size()))
		}
		if (heatReadings.size() > 0) {
			heatReadings.sort()
			log.info("HEAT LOW : "+ heatReadings[0])
			log.info("HEAT HIGH : "+ heatReadings[heatReadings.size() - 1])
			averages.put("averageHeat", (tmpHeatFloat / heatReadings.size()))
		}
		if (waterGreyReadings.size() > 0) {
			// TODO not finished the water readings sort for high and low
			waterHotReadings.sort()
			waterHotReadings.sort()
			waterHotReadings.sort()
			averages.put("averageWaterHot", (tmpHotFloat / waterHotReadings.size()))
			averages.put("averageColdHot", (tmpColdFloat / waterColdReadings.size()))
			averages.put("averageGreyHot", (tmpGreyFloat / waterGreyReadings.size()))
		}
		return averages
	}

}
