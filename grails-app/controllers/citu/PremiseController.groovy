package citu

import java.text.*
import java.util.Date;
import java.util.Map;

import net.sourceforge.openforecast.*
import net.sourceforge.openforecast.models.*
import org.joda.time.*

import grails.converters.*

class PremiseController extends BaseController {

	def beforeInterceptor = [action:this.&auth, except:["getReadingsByDate", "getReadingsSummary", "forecast"]]

	def scaffold = true
	
	def forecast = {
		getPredictedPrice()
	}

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
		
		log.info ("Requesting premise "+premiseInstance)

		if (!premiseInstance) {
			render("invalid premise ID")
		} else {
			render simpleViewMap(params) as JSON
		}
	}
	
	/*
	 * Get the reading summary view for the landing page
	 */
	def getReadingsSummary = {
		
		def Premise premiseInstance = HelperUtil.getPremise(params)
		
		if (!premiseInstance) {
			
			render("invalid premise ID")
			
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
			def sumHeat = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:now.toDate(), date2:endOfDay.toDate()])
			
		
			/*
			 * Pull back the avg for the summary from today minus 7 days
			 */
			def avgElec = ElecReading.executeQuery("select avg(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:now.toDate()-7, date2:now.toDate()])
			def avgWater = WaterReading.executeQuery("select avg(reading.readingValueHot), avg(reading.readingValueCold), avg(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:now.toDate()-7, date2:now.toDate()])
			def avgHeat = ElecReading.executeQuery("select avg(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.dateCreated between:date1 AND :date2 ", [date1:now.toDate()-7, date2:now.toDate()])
			
			
			def highlows = getHighLow(premiseInstance.bedrooms, now.toDate(), endOfDay.toDate())
			log.info("elec avg Reading : "+ avgElec[0])
			log.info("elec Reading : "+ sumElec[0])
			log.info("water avg Reading : "+ avgWater[0][0])
			log.info("water Reading : "+ sumWater[0][0])
			log.info("heat avg Reading : "+ avgHeat[0])
			log.info("heat Reading : "+ sumHeat[0])
			
			premise.put("electricity", HelperUtil.generateElecSummary(sumElec[0], highlows, avgElec[0]))
			premise.put("heat", HelperUtil.generateHeatSummary(sumHeat[0], highlows, avgHeat[0]))
			premise.put("hotWater", HelperUtil.generateHotWaterSummary(sumWater[0][0], highlows, avgWater[0][0]))
			premise.put("coldWater", HelperUtil.generateColdWaterSummary(sumWater[0][1], highlows, avgWater[0][1]))
			premise.put("greyWater", HelperUtil.generateGreyWaterSummary(sumWater[0][2], highlows, avgWater[0][2]))
			
			// TODO getPredictedPrice()
			
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
				view = createViewJsonObject(premiseInstance, now, "week", params.utilType)
			} else {
				log.info("simpleViewMap - Day View - with date : "+ now)
				view = createViewJsonObject(premiseInstance, now, "day", params.utilType)
			}
		} else if (params.month && params.year) {

			now = new DateTime(params.int("year"), params.int("month"), 1, 0, 0, 0, 0)
			log.info("simpleViewMap - Month View - with date : "+ now)
			view = createViewJsonObject(premiseInstance, now, "month", params.utilType)
		} else if (params.year) {

			now = new DateTime(params.int("year"), 1, 1, 0, 0, 0, 0)
			log.info("simpleViewMap - Year View - with date : "+ now)
			view = createViewJsonObject(premiseInstance, now, "year", params.utilType)
		} else {
			log.warn("Invalid date params sent")
			return null
		}

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
					premise = ["error":"No Elec Data for specified period"]
				}
			} else if (utilType.equals("water")) {
				premiseInstance.waterReadings = WaterReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"dateCreated", order:"desc"])
				swingData = getWaterSwingometer(premiseInstance.bedrooms, now.toDate(), endOfDay.toDate())
				if (premiseInstance.waterReadings.size() > 0) {
					HelperUtil.createWaterMap(premiseInstance, premise, swingData)
				} else {
					premise = ["error":"No Water Data for specified period"]
				}
			} else if (utilType.equals("heat")) {
			
					premise = ["error":"No Heat Data for specified day period"]
			}
		
		} else {
		
			ArrayList electricityReadings = new ArrayList()
			ArrayList waterReadings = new ArrayList()
			ArrayList heatReadings = new ArrayList()
			def swingData = [:]
		
			if (viewType.equals("week")) {
				
				now = now.withDayOfWeek(DateTimeConstants.MONDAY)
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
					def heatDay = HeatReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"dateCreated", order:"desc"])
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecDay.readingValueElec), dateCreated:now.toDate()))
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
					def heatWeek = HeatReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), endOfWeek.toDate(), [sort:"dateCreated", order:"desc"])				
					log.debug("Dates : "+ now.toDate() +" : "+ endOfWeek.toDate() +" values : "+ BillUtil.calcTotal(elecWeek.readingValueElec))
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecWeek.readingValueElec), dateCreated:now.toDate()))
					heatReadings.add(new HeatReading(readingValueHeat:BillUtil.calcTotal(heatWeek.readingValueHeat), dateCreated:now.toDate()))		
					waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterWeek.readingValueHot), readingValueCold:BillUtil.calcTotal(waterWeek.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterWeek.readingValueGrey), dateCreated:now.toDate()))
					now = now.plusDays(7)
				}
	
				if (monthDays > 28) {
					def diff = now.plusDays((monthDays - now.getDayOfMonth())+1).minusSeconds(1)
					def elecWeek = ElecReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), diff.toDate(), [sort:"dateCreated", order:"desc"])
					def waterWeek = WaterReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), diff.toDate(), [sort:"dateCreated", order:"desc"])
					def heatWeek = HeatReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), diff.toDate(), [sort:"dateCreated", order:"desc"])
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
					def heatMonth = HeatReading.findAllByPremiseAndDateCreatedBetween(premiseInstance, now.toDate(), monthEnd.toDate(), [sort:"dateCreated", order:"desc"])
					log.debug("Dates : "+ now.toDate() +" : "+ monthEnd.toDate())
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecMonth.readingValueElec), dateCreated:now.toDate()))
					waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterMonth.readingValueHot), readingValueCold:BillUtil.calcTotal(waterMonth.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterMonth.readingValueGrey), dateCreated:now.toDate()))
					heatReadings.add(new HeatReading(readingValueHeat:BillUtil.calcTotal(heatMonth.readingValueElec), dateCreated:now.toDate()))
					now = now.plusMonths(1)
				}
				
			}
			if (utilType.equals("elec")) {
				premiseInstance.elecReadings = electricityReadings
				if (premiseInstance.elecReadings.size() > 0) {
					HelperUtil.createElectricityMap(premiseInstance, premise, swingData)
				} else {
					premise = ["error":"No elec Data for specified period"]
				}
			} else if (utilType.equals("water")) {
				premiseInstance.waterReadings = waterReadings
				if (premiseInstance.waterReadings.size() > 0) {
					HelperUtil.createWaterMap(premiseInstance, premise, swingData)
				} else {
					premise = ["error":"No water Data for specified period"]
				}
			} else if (utilType.equals("heat")) {
				premiseInstance.heatReadings = heatReadings
				if (premiseInstance.heatReadings.size() > 0) {
					HelperUtil.createHeatMap(premiseInstance, premise, swingData)
				} else {
					premise = ["error":"No heat Data for specified period"]
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
			def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
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
		def tmpElecFloat = 0
		def heatReadings = new ArrayList()
		def swingometer = [:]
		for (i in premises) {
			def sumHeat = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			// ignore Elec for empty values
			if (sumHeat[0]) {
				tmpHeatFloat = (tmpElecFloat + sumHeat[0])
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
			def sumWater = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ i +" and reading.fileDate between:date1 AND :date2 ", [date1:startDate, date2:endDate])
			// ignore Water for empty values
			if (sumWater[0][1]) {
				def tmpSum = BillUtil.calcHotWaterPriceByVolume(sumWater[0][0])+BillUtil.calcColdWaterPriceByVolume(sumWater[0][1])+BillUtil.calcGreyWaterPriceByVolume(sumWater[0][2])
				waterReadings.add(tmpSum)
				tmpWaterFloat = (tmpWaterFloat + tmpSum)		
			}
		}
		log.info(tmpWaterFloat)
		/*
		if (waterReadings.size() > 0) {
			waterReadings.sort()
			swingometer.put("swingLow", BillUtil.calcWaterPriceByVolume(waterReadings[0]))
			swingometer.put("swingHigh", BillUtil.calcWaterPriceByVolume(waterReadings[waterReadings]))
			swingometer.put("peerAvg", (tmpWaterFloat / waterReadings.size()))
		} else {
			swingometer.put("swingLow", 0)
			swingometer.put("swingHigh", 0)
			swingometer.put("peerAvg", 0)
		}*/
		
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
	
	Map getPredictedPrice() {

		def now = new DateTime()
		now = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0, 0)
		def endOfDay = now.plusDays(1).minusSeconds(1)
		/*
		def x = 0
		def y = 7
		def elecDataSet = new DataSet();
		
		def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = 610 and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate(), date2:endOfDay.toDate()])
		def sumElec1 = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = 610 and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate()+1, date2:endOfDay.toDate()+1])
		def sumElec2 = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = 610 and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate()+2, date2:endOfDay.toDate()+2])
		def sumElec3 = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = 610 and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate()+3, date2:endOfDay.toDate()+3])
		
		log.info(sumElec)
		
		Observation elecObservation = new Observation(sumElec[0])
		elecObservation.setIndependentValue("date", x)
		elecDataSet.add(elecObservation)
		
		Observation elecObservation1 = new Observation(sumElec1[0])
		elecObservation1.setIndependentValue("date", x)
		elecDataSet.add(elecObservation1)
		
		Observation elecObservation2 = new Observation(sumElec2[0])
		elecObservation2.setIndependentValue("date", x)
		elecDataSet.add(elecObservation2)
		
		Observation elecObservation3 = new Observation(sumElec3[0])
		elecObservation3.setIndependentValue("date", x)
		elecDataSet.add(elecObservation3)
		
		log.info(elecDataSet)
		*/
		
		/*
		while ( y-- > 0 ) {
			def endOfDay = now.plusDays(1).minusSeconds(1)
			x++
			def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = 610 and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate(), date2:endOfDay.toDate()])
			def elecObservation = new Observation(sumElec[0])
			elecObservation.setIndependentValue("date", x)
			elecDataSet.add(elecObservation)
			now = now.plusDays(1)
		}
		
		ForecastingModel model = Forecaster.getBestForecast(elecDataSet)
		model.init(elecDataSet)
		
		DataPoint fcDataPoint4 = new Observation(0.0);
		fcDataPoint4.setIndependentValue("date", x++);
		
		// Create forecast data set and add these DataPoints
		DataSet fcDataSet = new DataSet();
		fcDataSet.add(fcDataPoint4);
		
		Iterator itt = fcDataSet.iterator();
		Double value=0.0;
		while (itt.hasNext()) {
			DataPoint dp = (DataPoint) itt.next();
			double forecastValue = dp.getDependentValue();
			value = forecastValue;
		}
		
		model.forecast(fcDataPoint4);
		log.info(BillUtil.calcElecPriceByVolume(fcDataPoint4.getDependentValue()));
		*/
		def x = 0
		def y = 7
		
		def tmpObj = "observation"
		
		while ( y-- > 0 ) {
			def tmpObj1 = tmpObj+x
			//log.info(tmpObj1)
			x++
		}
		
		/*
		def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = 610 and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate(), date2:endOfDay.toDate()])

		
		// Create Observation for temperature measured on 2009/12/20
		Observation observation1 = new Observation(13.0)
		observation1.setIndependentValue("year",2009)
		observation1.setIndependentValue("month", 12)
		observation1.setIndependentValue("date", 20)
		
		// Create Observation for temperature measured on 2009/12/21
		Observation observation2 = new Observation(39.0)
		observation2.setIndependentValue("year", 2009)
		observation2.setIndependentValue("month", 12)
		observation2.setIndependentValue("date", 21)
		
		// Create Observation for temperature measured on 2009/12/22
		Observation observation3 = new Observation(42.0)
		observation3.setIndependentValue("year", 2009)
		observation3.setIndependentValue("month", 12)
		observation3.setIndependentValue("date", 22)
		
		DataSet dataSet = new DataSet();
		
		// Add Observations to the DataSet
		dataSet.add(observation1)
		dataSet.add(observation2)
		dataSet.add(observation3)
		
		ForecastingModel model = Forecaster.getBestForecast(dataSet)
		model.init(dataSet)
		
		DataPoint fcDataPoint4 = new Observation(0.0);
		fcDataPoint4.setIndependentValue("year", 2009);
		fcDataPoint4.setIndependentValue("month", 12);
		fcDataPoint4.setIndependentValue("date", 23);
		
		// Create forecast data set and add these DataPoints
		DataSet fcDataSet = new DataSet();
		fcDataSet.add(fcDataPoint4);
		
		model.forecast(fcDataPoint4);
		//System.out.println(fcDataPoint4.getDependentValue());*/
			
	}

}
