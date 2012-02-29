package citu

import java.text.*
import java.util.Map;

import org.joda.time.*

import grails.converters.*

class PremiseController extends BaseController {

	def beforeInterceptor = [action:this.&auth, except:["getReadingsByDate"]]

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

		def user = [firstName:premiseInstance.user.firstName, lastName:premiseInstance.user.lastName, userName:premiseInstance.user.userName, contactEmail:premiseInstance.user.contactEmail, userId: premiseInstance.user.id]
		def premise = [flatNo:premiseInstance.flatNo, addressLine1:premiseInstance.addressLine1, addressLine1:premiseInstance.addressLine2, postCode:premiseInstance.postCode, bedrooms:premiseInstance.bedrooms, squareArea:premiseInstance.squareArea, user:user]
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
			log.info("ELEC LOW : "+ elecReadings[0])
			log.info("ELEC HIGH : "+ elecReadings[elecReadings.size() - 1])
		}
		if (heatReadings.size() > 0) {
			heatReadings.sort()
			log.info("HEAT LOW : "+ heatReadings[0])
			log.info("HEAT HIGH : "+ heatReadings[heatReadings.size() - 1])
		}
		if (waterGreyReadings.size() > 0) {
			// TODO not finished the water readings sort for high and low
			waterHotReadings.sort()
			waterHotReadings.sort()
			waterHotReadings.sort()
			log.info("GREY LOW : "+ waterGreyReadings[0])
			log.info("GREY HIGH : "+ waterGreyReadings[waterGreyReadings.size() - 1])
			log.info("HOT LOW : "+ waterHotReadings[0])
			log.info("HOT HIGH : "+ waterHotReadings[waterHotReadings.size() - 1])
			log.info("COLD LOW : "+ waterColdReadings[0])
			log.info("COLD HIGH : "+ waterColdReadings[waterColdReadings.size() - 1])
		}
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
