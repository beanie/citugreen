package citu

import java.text.*
import java.util.Calendar;
import java.util.Map;

import grails.converters.*

class PremiseController extends BaseController {

	def beforeInterceptor = [action:this.&auth, except:["getReadingsByDate", "oddTest"]]

	def scaffold = true

	def summary = {
		
				def Premise premiseInstance = HelperUtil.getPremise(params)
		
				if (!premiseInstance) {
					render("invalid premise ID")
				} else {
					[simpleView: simpleView(params)]
				}
			}
	
	def summary5 = {

		def Premise premiseInstance = HelperUtil.getPremise(params)

		if (!premiseInstance) {
			render("invalid premise ID")
		} else {
			[simpleView: simpleView(params)]
		}
	}

	def getReadingsByDate = {

		def Premise premiseInstance = HelperUtil.getPremise(params)

		if (!premiseInstance) {
			render("invalid premise ID")
		} else {
			render simpleView(params) as JSON
		}
	}

	Map simpleView(Map params) {

		def Premise premiseInstance = HelperUtil.getPremise(params)

		def now = Calendar.getInstance()
		def view

		if (params.day && params.month && params.year && !params.week) {
			log.info("simpleMapView - Day View")
			now.set(params.int("year"), params.int("month")-1, params.int("day"))
			view = createViewJsonObject(premiseInstance, now, "day")
		} else if (params.day && params.month && params.year && params.week) {
			log.info("simpleMapView - Week View")
			now.set(params.int("year"), params.int("month")-1, params.int("day"))
			view = createViewJsonObject(premiseInstance, now, "week")
		} else if (params.month && params.year) {
			log.info("simpleMapView - Month View")
			now.set(params.int("year"), params.int("month")-1, 1)
			view = createViewJsonObject(premiseInstance, now, "month")
		} else if (params.year) {
			log.info("simpleMapView - Year View")
			now.set(params.int("year"), 0, 1)
			view = createViewJsonObject(premiseInstance, now, "year")
		} else if (params.daysBack ){
			log.info(params.daysBack +" days back view")
			def nowDate = new Date() - params.int("daysBack")
			now.setTime(nowDate)
		} else {
			log.error("Invalid date params sent")
		}

		return view
	}

	Map createViewJsonObject(Premise premiseInstance, Calendar d1, String viewType) {

		def user = [firstName:premiseInstance.user.firstName, lastName:premiseInstance.user.lastName, userName:premiseInstance.user.userName, contactEmail:premiseInstance.user.contactEmail, userId: premiseInstance.user.id]
		def premise = [flatNo:premiseInstance.flatNo, addressLine1:premiseInstance.addressLine1, addressLine1:premiseInstance.addressLine2, postCode:premiseInstance.postCode, rooms:premiseInstance.rooms, squareArea:premiseInstance.squareArea, user:user]
		def averages = [:]
		
		if (viewType.equals("day"))	{

			premiseInstance.elecReadings = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, d1.getTime()-1, d1.getTime(), [sort:"fileDate", order:"desc"])
			premiseInstance.waterReadings = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, d1.getTime()-1, d1.getTime(), [sort:"fileDate", order:"desc"])

		} else if (viewType.equals("week")) {

			def now = d1.getTime()-(d1.get(Calendar.DAY_OF_WEEK))

			ArrayList electricityReadings = new ArrayList()
			ArrayList waterReadings = new ArrayList()

			7.times {
				def elecDay = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now, now+1, [sort:"fileDate", order:"desc"])
				def waterDay = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now, now+1, [sort:"fileDate", order:"desc"])
				electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecDay.readingValueElec), fileDate:now+1))
				waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterDay.readingValueHot), readingValueCold:BillUtil.calcTotal(waterDay.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterDay.readingValueGrey), fileDate:now+1))
				now = now+1
			}
			//TODO getAverage(premiseInstance.rooms, now - 7, now)
			premiseInstance.elecReadings = electricityReadings
			premiseInstance.waterReadings = waterReadings

		} else if (viewType.equals("month")) {

			ArrayList electricityReadings = new ArrayList()
			ArrayList waterReadings = new ArrayList()
			def now = d1.getTime()
			averages = getElecAverage(premiseInstance.rooms, now, now + (d1.getActualMaximum(Calendar.DAY_OF_MONTH)-1))

			4.times {
				def elecWeek = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now, now+6, [sort:"fileDate", order:"desc"])
				def waterWeek = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now, now+6, [sort:"fileDate", order:"desc"])
				electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecWeek.readingValueElec), fileDate:now))
				waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterWeek.readingValueHot), readingValueCold:BillUtil.calcTotal(waterWeek.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterWeek.readingValueGrey), fileDate:now))
				now = now+7
			}
			if (d1.getActualMaximum(Calendar.DAY_OF_MONTH) >= 28) {
				def diff = d1.getActualMaximum(Calendar.DAY_OF_MONTH) - 29
				def elecWeek = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now, now+diff, [sort:"fileDate", order:"desc"])
				def waterWeek = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now, now+diff, [sort:"fileDate", order:"desc"])
				electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecWeek.readingValueElec), fileDate:now))
				waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterWeek.readingValueHot), readingValueCold:BillUtil.calcTotal(waterWeek.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterWeek.readingValueGrey), fileDate:now))
			}
			premiseInstance.elecReadings = electricityReadings
			premiseInstance.waterReadings = waterReadings

		} else if (viewType.equals("year")) {

			ArrayList electricityReadings = new ArrayList()
			ArrayList waterReadings = new ArrayList()
			def now = d1.getTime()
			def monthStart
			def monthEnd
			def nextMonth
			12.times {
				monthStart = d1.getTime()
				monthEnd = monthStart+(d1.getActualMaximum(Calendar.DAY_OF_MONTH)-1)
				def elecMonth = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, monthStart, monthEnd, [sort:"fileDate", order:"desc"])
				def waterMonth = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, monthStart, monthEnd, [sort:"fileDate", order:"desc"])
				electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecMonth.readingValueElec), fileDate:monthStart))
				waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterMonth.readingValueHot), readingValueCold:BillUtil.calcTotal(waterMonth.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterMonth.readingValueGrey), fileDate:monthStart))
				nextMonth = (d1.get(Calendar.MONTH)+1)
				d1.set(Calendar.MONTH, nextMonth)
			}
			premiseInstance.elecReadings = electricityReadings
			premiseInstance.waterReadings = waterReadings

		}
		premise.put("electricity", HelperUtil.createElectricityMap(premiseInstance))
		premise.put("water", HelperUtil.createWaterMap(premiseInstance))
		premise.put("buildingTotalAveragesForPeriod", averages)
		return premise
	}
	
	Map getElecAverage(int noOfRooms, Date startDate, Date endDate) {
		def premises = Premise.executeQuery("select p.flatNo from Premise p where rooms = "+ noOfRooms)
		def averages = [:]
		def tmpElecFloat = 0
		def tmpHeatFloat = 0
		def tmpGreyFloat = 0
		def tmpColdFloat = 0
		def tmpHotFloat = 0
		def goodElecReadings = 0
		def goodHeatReadings = 0
		def goodWaterReadings = 0
		for (i in premises) {
			def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			def sumHeat = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ i +" and reading.dateCreated between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			def sumWater = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			// ignore Elec for empty values
			if (sumElec[0]) {
				tmpElecFloat = (tmpElecFloat + sumElec[0])
				goodElecReadings ++
			}
			// ignore Heat for empty values
			if (sumHeat[0]) {
				tmpHeatFloat = (tmpHeatFloat + sumHeat[0])
				goodHeatReadings ++
			}
			// ignore Water for empty values
			if (sumWater[0][2]) {
				tmpHotFloat = (tmpHotFloat + sumWater[0][0])
				tmpColdFloat = (tmpColdFloat + sumWater[0][1])
				tmpGreyFloat = (tmpGreyFloat + sumWater[0][2])
				goodWaterReadings ++
			}
		}
		if (goodElecReadings > 0) {
			averages.put("averageElec", (tmpElecFloat / goodElecReadings))
		}
		if (goodHeatReadings > 0) {
			averages.put("averageHeat", (tmpHeatFloat / goodHeatReadings))
		}
		if (goodWaterReadings > 0) {
			averages.put("averageWaterHot", (tmpHotFloat / goodWaterReadings))
			averages.put("averageColdHot", (tmpColdFloat / goodWaterReadings))
			averages.put("averageGreyHot", (tmpGreyFloat / goodWaterReadings))
		}
		return averages
	}
	
}
