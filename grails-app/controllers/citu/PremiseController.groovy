package citu

import java.text.*
import java.util.Date;
import java.util.Map;

import net.sourceforge.openforecast.*
import net.sourceforge.openforecast.models.*
import org.joda.time.*

import grails.converters.*

class PremiseController extends BaseController {

	RankService rankService

	def beforeInterceptor = [action:this.&auth, except:[
			"getReadingsByDate",
			"getBuildingReadingsSummary",
			"getReadingsSummary",
			"forecast"
		]]

	def scaffold = true

	def summary = {

		def Premise premiseInstance = HelperUtil.getPremise(params)

		if (!premiseInstance) {
			render("200":"invalid premise id")
		} else {
			[simpleView: simpleViewMap(params)]
		}
	}


	def getReadingsByDate = {

		def Premise premiseInstance = HelperUtil.getPremise(params)

		log.info ("Requesting premise "+premiseInstance)

		if (!premiseInstance) {
			render("200":"invalid premise id")
		} else {
			render simpleViewMap(params) as JSON
		}
	}

	/*
	 * Get the reading summary view for the landing page (test commit)
	 */


	def getBuildingReadingsSummary = {

		def Premise premiseInstance = HelperUtil.getPremise(params)
		def premise = HelperUtil.createPremiseSkeletonMap(premiseInstance)


		def now = new DateTime()
		now = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0, 0)

		/*
		 * Pull back the sums for the summary
		 */
		def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(7).toDate(), date2:now.toDate()])
		def sumWater = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(7).toDate(), date2:now.toDate()])
		def sumHeat = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(7).toDate(), date2:now.toDate()])
		def sumSolar = EnergyReading.executeQuery("select sum(reading.readingValueIn) from EnergyReading as reading where reading.energyItem.type='solar' and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(7).toDate(), date2:now.toDate()])
		def sumWind = EnergyReading.executeQuery("select sum(reading.readingValueIn) from EnergyReading as reading where reading.energyItem.type='wind' and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(7).toDate(), date2:now.toDate()])
		def sumLift = EnergyReading.executeQuery("select sum(reading.readingValueIn) from EnergyReading as reading where reading.energyItem.type='lift' and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(7).toDate(), date2:now.toDate()])
		
		
		
		def sumElecLW = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(14).toDate(), date2:now.minusDays(7).toDate()])
		def sumWaterLW = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(14).toDate(), date2:now.minusDays(7).toDate()])
		def sumHeatLW = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(14).toDate(), date2:now.minusDays(7).toDate()])
		def sumLiftLW = EnergyReading.executeQuery("select sum(reading.readingValueIn) from EnergyReading as reading where reading.energyItem.type='lift' and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(14).toDate(), date2:now.minusDays(7).toDate()])
		def sumSolarLW = EnergyReading.executeQuery("select sum(reading.readingValueIn) from EnergyReading as reading where reading.energyItem.type='solar' and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(14).toDate(), date2:now.minusDays(7).toDate()])
		def sumWindLW = EnergyReading.executeQuery("select sum(reading.readingValueIn) from EnergyReading as reading where reading.energyItem.type='wind' and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(14).toDate(), date2:now.minusDays(7).toDate()])
		def sumLift2W = EnergyReading.executeQuery("select sum(reading.readingValueIn) from EnergyReading as reading where reading.energyItem.type='lift' and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(21).toDate(), date2:now.minusDays(14).toDate()])
		def sumSolar2W = EnergyReading.executeQuery("select sum(reading.readingValueIn) from EnergyReading as reading where reading.energyItem.type='solar' and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(21).toDate(), date2:now.minusDays(14).toDate()])
		def sumWind2W = EnergyReading.executeQuery("select sum(reading.readingValueIn) from EnergyReading as reading where reading.energyItem.type='wind' and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(21).toDate(), date2:now.minusDays(14).toDate()])


		Float tmp = new Float(0.15)
		
		Float co2 = new Float(0.43)
		
		def boiledKettles = 0
		def co2Saved = 0
		
		if (sumElec[0]) { boiledKettles = sumElec[0].toFloat() }
		if (sumHeat[0]) { boiledKettles = (boiledKettles + sumHeat[0].toFloat())/tmp }
		
		if (sumSolar[0]) { co2Saved = sumSolar[0].toFloat() }
		if (sumWind[0]) { co2Saved = (co2Saved + sumWind[0].toFloat())*co2 }

		def heat = [:]

		heat.put("thisWeek", sumHeat[0])
		heat.put("lastWeek", sumHeatLW[0])

		def electricity = [:]
		electricity.put("thisWeek", sumElec[0])
		electricity.put("lastWeek", sumElecLW[0])

		def hotWater = [:]
		hotWater.put("thisWeek", (sumWater[0][0]))
		hotWater.put("lastWeek", (sumWaterLW[0][0]))

		def coldWater = [:]
		coldWater.put("thisWeek", (sumWater[0][1]))
		coldWater.put("lastWeek", (sumWaterLW[0][1]))

		def greyWater = [:]
		greyWater.put("thisWeek", (sumWater[0][2]))
		greyWater.put("lastWeek", (sumWaterLW[0][2]))

		def solarIn = [:]
		solarIn.put("thisWeek", sumSolar[0])
		solarIn.put("lastWeek", sumSolarLW[0])
		solarIn.put("2Week", sumSolar2W[0])
		
		def windIn = [:]
		windIn.put("thisWeek", sumWind[0])
		windIn.put("lastWeek", sumWindLW[0])
		windIn.put("2Week", sumWind2W[0])
		
		def liftOut= [:]
		liftOut.put("thisWeek", sumLift[0])
		liftOut.put("lastWeek", sumLiftLW[0])
		liftOut.put("2Week", sumLift2W[0])
		

		premise.put ("boiledKettles", boiledKettles)
		premise.put ("co2Saved", co2Saved)
		premise.put ("windIn", windIn)
		premise.put ("solarIn", solarIn)
		premise.put ("liftOut", liftOut)
		premise.put("electricity", electricity )
		premise.put("heat",  heat)
		premise.put("hotWater", hotWater)
		premise.put("coldWater", coldWater)
		premise.put("greyWater", greyWater)

		render premise as JSON
	}



	def getReadingsSummary = {

		def Premise premiseInstance = HelperUtil.getPremise(params)

		if (!premiseInstance) {

			render("200":"invalid premise ID")
		
		} else {

			def now = new DateTime()
			now = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0, 0)
			def endOfDay = now.plusDays(1).minusSeconds(1)

			def premise = HelperUtil.createPremiseSkeletonMap(premiseInstance)

			/*
			 * Pull back the sums for the summary
			 */
			def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:now.toDate(), date2:endOfDay.toDate()])
			def sumWater = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:now.toDate(), date2:endOfDay.toDate()])
			def sumHeat = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(1).toDate(), date2:endOfDay.minusDays(1).toDate()])


			// changed here

			/*
			 * Pull back the avg for the summary from today minus 7 days
			 */
			def avgElec = ElecReading.executeQuery("select avg(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:now.minusDays(7).toDate(), date2:now.toDate()])
			def avgWater = WaterReading.executeQuery("select avg(reading.readingValueHot), avg(reading.readingValueCold), avg(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:now.minusDays(7).toDate(), date2:now.toDate()])
			def avgHeat = HeatReading.executeQuery("select avg(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.fileDate between:date1 AND :date2 ", [date1:now.minusDays(8).toDate(), date2:now.minusDays(1).toDate()])


			def highlows = getHighLow(premiseInstance.bedrooms, now.toDate(), endOfDay.toDate())
			log.info("elec avg Reading : "+ avgElec[0])
			log.info("elec Reading : "+ sumElec[0])
			log.info("water avg Reading : "+ avgWater[0][0])
			log.info("water Reading : "+ sumWater[0][0])
			log.info("heat avg Reading : "+ avgHeat[0])
			log.info("heat Reading : "+ sumHeat[0])

			premise.put("electricity", HelperUtil.generateElecSummary(sumElec[0], highlows, avgElec[0], getDayPrediction(premise.flatNo, "elec")))
			premise.put("heat", HelperUtil.generateHeatSummary(sumHeat[0], highlows, avgHeat[0], getDayPrediction(premise.flatNo, "heat")))
			premise.put("hotWater", HelperUtil.generateHotWaterSummary(sumWater[0][0], highlows, avgWater[0][0], getDayPrediction(premise.flatNo, "hotWater")))
			premise.put("coldWater", HelperUtil.generateColdWaterSummary(sumWater[0][1], highlows, avgWater[0][1], getDayPrediction(premise.flatNo, "coldWater")))
			premise.put("greyWater", HelperUtil.generateGreyWaterSummary(sumWater[0][2], highlows, avgWater[0][2], getDayPrediction(premise.flatNo, "greyWater")))
			
			render premise as JSON
		}
	}

	Map simpleViewMap(Map params) {

		def now
		def view
		def logM

		def Premise premiseInstance = HelperUtil.getPremise(params)

		if (params.day && params.month && params.year) {

			now = new DateTime(params.int("year"), params.int("month"), params.int("day"), 0, 0, 0, 0)

			if (params.week) {
				log.info("simpleViewMap - Week View - with date : "+ now)
				logM = "week"

				view = createViewJsonObject(premiseInstance, now, "week", params.utilType)
			} else {
				log.info("simpleViewMap - Day View - with date : "+ now)
				logM = "Day"

				view = createViewJsonObject(premiseInstance, now, "day", params.utilType)
			}
		} else if (params.month && params.year) {

			now = new DateTime(params.int("year"), params.int("month"), 1, 0, 0, 0, 0)

			log.info("simpleViewMap - Month View - with date : "+ now)
			logM = "Month"

			view = createViewJsonObject(premiseInstance, now, "month", params.utilType)
		} else if (params.year) {

			now = new DateTime(params.int("year"), 1, 1, 0, 0, 0, 0)
			log.info("simpleViewMap - Year View - with date : "+ now)
			logM = "Year"

			view = createViewJsonObject(premiseInstance, now, "year", params.utilType)
		} else {
			log.warn("Invalid date params sent")
			render("300":"invalid date requested")
			return null
		}

		Date tmpDate = new Date()

		def tmpStat = new Stats(logCode:params.utilType, dateNow:tmpDate, logMessage:logM, messageType:'energy',premise:premiseInstance, ).save()

		return view
	}

	Map createViewJsonObject(Premise premiseInstance, DateTime now, String viewType, String utilType) {

		def premise = HelperUtil.createPremiseSkeletonMap(premiseInstance)

		if (viewType.equals("day"))	{

			def endOfDay = now.plusDays(1).minusSeconds(1)
			def swingData = [:]

			if (utilType.equals("elec")) {
				premiseInstance.elecReadings = ElecReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"dateCreated", order:"desc"])
				swingData = getElecSwingometer(premiseInstance.bedrooms, now.toDate(), endOfDay.toDate())
				if (premiseInstance.elecReadings.size() > 0) {
					HelperUtil.createElectricityMap(premiseInstance, premise, swingData)
				} else {
					premise = ["310":"No Elec Data for specified period"]
				}
			} else if (utilType.equals("water")) {
				premiseInstance.waterReadings = WaterReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"dateCreated", order:"desc"])
				swingData = getWaterSwingometer(premiseInstance.bedrooms, now.toDate(), endOfDay.toDate())
				if (premiseInstance.waterReadings.size() > 0) {
					HelperUtil.createWaterMap(premiseInstance, premise, swingData)
				} else {
					premise = ["311":"No Water Data for specified period"]
				}
			} else if (utilType.equals("heat")) {
				premiseInstance.heatReadings = HeatReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
				swingData = getHeatSwingometer(premiseInstance.bedrooms, now.toDate(), endOfDay.toDate())
				if (premiseInstance.heatReadings.size() > 0) {
					HelperUtil.createHeatMap(premiseInstance, premise, swingData)
				} else {
					premise = ["312":"No Heat Data for specified period"]
				}
			}
		} else {

			ArrayList electricityReadings = new ArrayList()
			ArrayList waterReadings = new ArrayList()
			ArrayList heatReadings = new ArrayList()
			def swingData = [:]

			if (viewType.equals("week")) {

				def endODay = now.plusDays(1).minusSeconds(1)
				now = endODay.minusWeeks(1)
				if (utilType.equals("elec")) {
					swingData = getElecSwingometer(premiseInstance.bedrooms, now.toDate(), now.plusWeeks(1).minusSeconds(1).toDate())
				} else if (utilType.equals("water")) {
					swingData = getWaterSwingometer(premiseInstance.bedrooms, now.toDate(), now.plusWeeks(1).minusSeconds(1).toDate())
				} else if (utilType.equals("heat")) {
					swingData = getHeatSwingometer(premiseInstance.bedrooms, now.toDate(), now.plusWeeks(1).minusSeconds(1).toDate())
				}


				7.times {
					def endOfDay = now.plusDays(1).minusSeconds(1)
					def elecDay = ElecReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"dateCreated", order:"desc"])
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecDay.readingValueElec), dateCreated:now.toDate()))
					def heatDay = HeatReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
					heatReadings.add(new HeatReading(readingValueHeat:BillUtil.calcTotal(heatDay.readingValueHeat), dateCreated:now.toDate()))
					def waterDay = WaterReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"dateCreated", order:"desc"])
					waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterDay.readingValueHot), readingValueCold:BillUtil.calcTotal(waterDay.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterDay.readingValueGrey), dateCreated:now.toDate()))
					now = now.plusDays(1)
				}
			} else if (viewType.equals("month")) {

				def monthDays = now.dayOfMonth().getMaximumValue()
				if (utilType.equals("elec")) {
					swingData = getElecSwingometer(premiseInstance.bedrooms, now.toDate(), now.plusMonths(1).minusSeconds(1).toDate())
				} else if (utilType.equals("water")) {
					swingData = getWaterSwingometer(premiseInstance.bedrooms, now.toDate(), now.plusMonths(1).minusSeconds(1).toDate())
				} else if (utilType.equals("heat")) {
					swingData = getHeatSwingometer(premiseInstance.bedrooms, now.toDate(), now.plusMonths(1).minusSeconds(1).toDate())
				}
				4.times {
					def endOfWeek = now.plusDays(7).minusSeconds(1)
					def elecWeek = ElecReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), endOfWeek.toDate(), [sort:"dateCreated", order:"desc"])
					def waterWeek = WaterReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), endOfWeek.toDate(), [sort:"dateCreated", order:"desc"])
					def heatWeek = HeatReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfWeek.toDate(), [sort:"fileDate", order:"desc"])

					log.debug("Dates : "+ now.toDate() +" : "+ endOfWeek.toDate() +" values : "+ BillUtil.calcTotal(heatWeek.readingValueHeat))
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecWeek.readingValueElec), dateCreated:now.toDate()))
					heatReadings.add(new HeatReading(readingValueHeat:BillUtil.calcTotal(heatWeek.readingValueHeat), dateCreated:now.toDate()))
					waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterWeek.readingValueHot), readingValueCold:BillUtil.calcTotal(waterWeek.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterWeek.readingValueGrey), dateCreated:now.toDate()))
					now = now.plusDays(7)
				}

				if (monthDays > 28) {
					def diff = now.plusDays((monthDays - now.getDayOfMonth())+1).minusSeconds(1)
					def elecWeek = ElecReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), diff.toDate(), [sort:"dateCreated", order:"desc"])
					def waterWeek = WaterReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), diff.toDate(), [sort:"dateCreated", order:"desc"])
					def heatWeek = HeatReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), diff.toDate(), [sort:"fileDate", order:"desc"])

					// changed sort from fileDate to dateCreated


					log.debug("Dates : "+ now.toDate() +" : "+ diff.toDate() +" values : "+ BillUtil.calcTotal(elecWeek.readingValueElec))
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecWeek.readingValueElec), dateCreated:now.toDate()))
					waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterWeek.readingValueHot), readingValueCold:BillUtil.calcTotal(waterWeek.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterWeek.readingValueGrey), dateCreated:now.toDate()))
					heatReadings.add(new HeatReading(readingValueHeat:BillUtil.calcTotal(heatWeek.readingValueHeat), dateCreated:now.toDate()))
				}
			} else if (viewType.equals("year")) {
				if (utilType.equals("elec")) {
					swingData = getElecSwingometer(premiseInstance.bedrooms, now.toDate(), now.plusYears(1).minusSeconds(1).toDate())
				} else if (utilType.equals("water")) {
					swingData = getWaterSwingometer(premiseInstance.bedrooms, now.toDate(), now.plusYears(1).minusSeconds(1).toDate())
				} else if (utilType.equals("heat")) {
					swingData = getHeatSwingometer(premiseInstance.bedrooms, now.toDate(), now.plusYears(1).minusSeconds(1).toDate())
				}
				12.times {
					def monthDays = now.dayOfMonth().getMaximumValue()
					def monthEnd = now.plusDays((monthDays - now.getDayOfMonth())+1).minusSeconds(1)
					def elecMonth = ElecReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), monthEnd.toDate(), [sort:"dateCreated", order:"desc"])
					def waterMonth = WaterReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), monthEnd.toDate(), [sort:"dateCreated", order:"desc"])
					def heatMonth = HeatReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), monthEnd.toDate(), [sort:"fileDate", order:"desc"])
					log.debug("Dates : "+ now.toDate() +" : "+ monthEnd.toDate())
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecMonth.readingValueElec), dateCreated:now.toDate()))
					waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterMonth.readingValueHot), readingValueCold:BillUtil.calcTotal(waterMonth.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterMonth.readingValueGrey), dateCreated:now.toDate()))
					heatReadings.add(new HeatReading(readingValueHeat:BillUtil.calcTotal(heatMonth.readingValueHeat), dateCreated:now.toDate()))
					now = now.plusMonths(1)
				}
			}
			if (utilType.equals("elec")) {
				premiseInstance.elecReadings = electricityReadings
				if (premiseInstance.elecReadings.size() > 0) {
					HelperUtil.createElectricityMap(premiseInstance, premise, swingData)
				} else {
					premise = ["310":"No elec Data for specified period"]
				}
			} else if (utilType.equals("water")) {
				premiseInstance.waterReadings = waterReadings
				if (premiseInstance.waterReadings.size() > 0) {
					HelperUtil.createWaterMap(premiseInstance, premise, swingData)
				} else {
					premise = ["311":"No water Data for specified period"]
				}
			} else if (utilType.equals("heat")) {
				premiseInstance.heatReadings = heatReadings
				if (premiseInstance.heatReadings.size() > 0) {
					HelperUtil.createHeatMap(premiseInstance, premise, swingData)
				} else {
					premise = ["312":"No heat Data for specified period"]
				}
			}
		}
		return premise
	}

	Map getElecSwingometer (int noOfRooms, Date startDate, Date endDate) {
		def premises = Premise.executeQuery("select p.flatNo from Premise p where bedrooms = "+ noOfRooms)
		def tmpElecFloat = 0
		def elecReadings = new ArrayList()
		def swingometer = [:]
		for (i in premises) {
			def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ i +" and reading.dateCreated between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			// ignore Elec for empty values
			if (sumElec[0]) {
				tmpElecFloat = (tmpElecFloat + sumElec[0])
				elecReadings.add(sumElec[0])
			}
		}
		if (elecReadings.size() > 0) {
			elecReadings.sort()
			swingometer.put("swingLow", BillUtil.calcElecPriceByVolume(elecReadings[0]))
			swingometer.put("swingHigh", BillUtil.calcElecPriceByVolume(elecReadings[elecReadings.size() - 1]))
			swingometer.put("peerAvg", (tmpElecFloat / elecReadings.size()))
		} else {
			swingometer.put("swingLow", 0)
			swingometer.put("swingHigh", 0)
			swingometer.put("peerAvg", 0)
		}

		return swingometer
	}

	Map getHeatSwingometer (int noOfRooms, Date startDate, Date endDate) {
		def premises = Premise.executeQuery("select p.flatNo from Premise p where bedrooms = "+ noOfRooms)
		def tmpHeatFloat = 0
		def heatReadings = new ArrayList()
		def swingometer = [:]
		for (i in premises) {
			def sumHeat = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			// ignore Heat for empty values
			if (sumHeat[0]) {
				tmpHeatFloat = (tmpHeatFloat + sumHeat[0])
				heatReadings.add(sumHeat[0])
			}
		}
		if (heatReadings.size() > 0) {
			heatReadings.sort()
			swingometer.put("swingLow", BillUtil.calcHeatPriceByVolume(heatReadings[0]))
			swingometer.put("swingHigh", BillUtil.calcHeatPriceByVolume(heatReadings[heatReadings.size() - 1]))
			swingometer.put("peerAvg", (tmpHeatFloat / heatReadings.size()))
		} else {
			swingometer.put("swingLow", 0)
			swingometer.put("swingHigh", 0)
			swingometer.put("peerAvg", 0)
		}

		return swingometer
	}

	Map getWaterSwingometer (int noOfRooms, Date startDate, Date endDate) {
		def premises = Premise.executeQuery("select p.flatNo from Premise p where bedrooms = "+ noOfRooms)
		def tmpWaterFloat = 0
		def waterReadings = new ArrayList()
		def swingometer = [:]
		for (i in premises) {
			def sumWater = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ i +" and reading.dateCreated between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			// ignore Water for empty values
			if (sumWater[0][1]) {
				def tmpSum = sumWater[0][0]+sumWater[0][1]+sumWater[0][2]
				waterReadings.add(tmpSum)
				tmpWaterFloat = (tmpWaterFloat + tmpSum)
			}
		}
		log.info(tmpWaterFloat)

		if (waterReadings.size() > 0) {
			waterReadings.sort()
			swingometer.put("swingLow", BillUtil.calcWaterPriceByVolume(waterReadings[0]))
			swingometer.put("swingHigh", BillUtil.calcWaterPriceByVolume(waterReadings[waterReadings.size()-1]))
			swingometer.put("peerAvg", (tmpWaterFloat / waterReadings.size()))
		} else {
			swingometer.put("swingLow", 0)
			swingometer.put("swingHigh", 0)
			swingometer.put("peerAvg", 0)
		}

		return swingometer
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
			def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ i +" and reading.dateCreated between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			def sumHeat = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			def sumWater = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ i +" and reading.dateCreated between:date1 AND :date2 ", [date1:startDate, date2:endDate])
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
			log.debug("Elec HIGH : "+ elecReadings[elecReadings.size() - 1])
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

	Float getDayPrediction(String flatNo, String estType) {

		// get 2 days previous data
		def now = new DateTime()
		now = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0, 0)
		def yesterday = now.minusDays(1)

		log.info("Yesterday : "+ yesterday +" to "+ yesterday.plusDays(1).minusSeconds(1))

		def lastWeek = now.minusWeeks(1)

		log.info("Last Week : "+ lastWeek +" to "+ lastWeek.plusDays(1).minusSeconds(1))

		def predDataSet = new DataSet()

		def sum2WeeksPrev
		def sumWeekPrev
		def sum2DaysPrev
		def sumDayPrev

		if (estType.equals("elec")) {
			sum2WeeksPrev = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:lastWeek.toDate(), date2:lastWeek.plusDays(1).minusSeconds(1).toDate()])
			sumWeekPrev = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:lastWeek.minusWeeks(1).toDate(), date2:lastWeek.minusWeeks(1).plusDays(1).minusSeconds(1).toDate()])
			sum2DaysPrev = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:yesterday.minusDays(1).toDate(), date2:yesterday.minusSeconds(1).toDate()])
			sumDayPrev = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:yesterday.toDate(), date2:yesterday.plusDays(1).minusSeconds(1).toDate()])
		} else if (estType.equals("heat")) {
			sum2WeeksPrev = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.fileDate between:date1 AND :date2 ", [date1:lastWeek.toDate(), date2:lastWeek.plusDays(1).minusSeconds(1).toDate()])
			sumWeekPrev = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.fileDate between:date1 AND :date2 ", [date1:lastWeek.minusWeeks(1).toDate(), date2:lastWeek.minusWeeks(1).plusDays(1).minusSeconds(1).toDate()])
			sum2DaysPrev = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.fileDate between:date1 AND :date2 ", [date1:yesterday.minusDays(1).toDate(), date2:yesterday.minusSeconds(1).toDate()])
			sumDayPrev = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.fileDate between:date1 AND :date2 ", [date1:yesterday.toDate(), date2:yesterday.plusDays(1).minusSeconds(1).toDate()])
		} else if (estType.equals("hotWater")) {
			sum2WeeksPrev = WaterReading.executeQuery("select sum(reading.readingValueHot) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:lastWeek.toDate(), date2:lastWeek.plusDays(1).minusSeconds(1).toDate()])
			sumWeekPrev = WaterReading.executeQuery("select sum(reading.readingValueHot) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:lastWeek.minusWeeks(1).toDate(), date2:lastWeek.minusWeeks(1).plusDays(1).minusSeconds(1).toDate()])
			sum2DaysPrev = WaterReading.executeQuery("select sum(reading.readingValueHot) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:yesterday.minusDays(1).toDate(), date2:yesterday.minusSeconds(1).toDate()])
			sumDayPrev = WaterReading.executeQuery("select sum(reading.readingValueHot) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:yesterday.toDate(), date2:yesterday.plusDays(1).minusSeconds(1).toDate()])
		} else if (estType.equals("coldWater")) {
			sum2WeeksPrev = WaterReading.executeQuery("select sum(reading.readingValueCold) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:lastWeek.toDate(), date2:lastWeek.plusDays(1).minusSeconds(1).toDate()])
			sumWeekPrev = WaterReading.executeQuery("select sum(reading.readingValueCold) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:lastWeek.minusWeeks(1).toDate(), date2:lastWeek.minusWeeks(1).plusDays(1).minusSeconds(1).toDate()])
			sum2DaysPrev = WaterReading.executeQuery("select sum(reading.readingValueCold) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:yesterday.minusDays(1).toDate(), date2:yesterday.minusSeconds(1).toDate()])
			sumDayPrev = WaterReading.executeQuery("select sum(reading.readingValueCold) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:yesterday.toDate(), date2:yesterday.plusDays(1).minusSeconds(1).toDate()])
		} else if (estType.equals("greyWater")) {
			sum2WeeksPrev = WaterReading.executeQuery("select sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:lastWeek.toDate(), date2:lastWeek.plusDays(1).minusSeconds(1).toDate()])
			sumWeekPrev = WaterReading.executeQuery("select sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:lastWeek.minusWeeks(1).toDate(), date2:lastWeek.minusWeeks(1).plusDays(1).minusSeconds(1).toDate()])
			sum2DaysPrev = WaterReading.executeQuery("select sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:yesterday.minusDays(1).toDate(), date2:yesterday.minusSeconds(1).toDate()])
			sumDayPrev = WaterReading.executeQuery("select sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:yesterday.toDate(), date2:yesterday.plusDays(1).minusSeconds(1).toDate()])
		}

		if (sum2WeeksPrev[0]) {
			Observation observation = new Observation(sum2WeeksPrev[0])
			observation.setIndependentValue("date", 1)
			predDataSet.add(observation)
		}

		if (sumWeekPrev[0]) {
			Observation observation1 = new Observation(sumWeekPrev[0])
			observation1.setIndependentValue("date", 2)
			predDataSet.add(observation1)
		}

		if (sum2DaysPrev[0]) {
			Observation observation2 = new Observation(sum2DaysPrev[0])
			observation2.setIndependentValue("date", 3)
			predDataSet.add(observation2)
		}

		if (sumDayPrev[0]) {
			Observation observation3 = new Observation(sumDayPrev[0])
			observation3.setIndependentValue("date", 4)
			predDataSet.add(observation3)
		}

		/*
		 * Do we need to break it down further by dates like this?
		 */
		// Create Observation for temperature measured on 2009/12/20
		//Observation observation1 = new Observation(13.0)
		//observation1.setIndependentValue("year",2009)
		//observation1.setIndependentValue("month", 12)
		//observation1.setIndependentValue("date", 20)

		log.info(estType+" : "+ predDataSet.size())

		if (predDataSet.size() > 3) {
			ForecastingModel model = Forecaster.getBestForecast(predDataSet)
			log.info("going to init")
			model.init(predDataSet)
			log.info("init ok")
			DataPoint fcDataPoint4 = new Observation(0.0)
			fcDataPoint4.setIndependentValue("date", 5)

			// Create forecast data set and add these DataPoints
			DataSet fcDataSet = new DataSet();
			fcDataSet.add(fcDataPoint4);

			Iterator itt = fcDataSet.iterator();
			Double value=0.0;
			log.info("going to itt....")
			while (itt.hasNext()) {
				DataPoint dp = (DataPoint) itt.next();
				double forecastValue = dp.getDependentValue();
				value = forecastValue;
			}

			model.forecast(fcDataPoint4)
			log.info("estimated value:")
			log.debug(fcDataPoint4.getDependentValue())
			return fcDataPoint4.getDependentValue()
		} else {
			return new Float(0)
		}

	}

}
