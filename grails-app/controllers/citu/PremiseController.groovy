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
			
			def now = new DateTime()
			now = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0, 0)
			def endOfDay = now.plusDays(1).minusSeconds(1)	
			
			def premise = HelperUtil.createPremiseSkeletonMap(premiseInstance)
			
			def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate(), date2:endOfDay.toDate()])
			def sumWater = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate(), date2:endOfDay.toDate()])
			
			def avgElec = ElecReading.executeQuery("select avg(reading.readingValueElec) from ElecReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate()-7, date2:now.toDate()])
			def avgWater = WaterReading.executeQuery("select avg(reading.readingValueHot), avg(reading.readingValueCold), avg(reading.readingValueGrey) from WaterReading as reading where reading.premise.flatNo = "+ premiseInstance.flatNo +" and reading.fileDate between:date1 AND :date2 ", [date1:now.toDate()-7, date2:now.toDate()])
			def highlows = getHighLow(premiseInstance.bedrooms, now.toDate(), endOfDay.toDate())
			
			premise.put("electricity", HelperUtil.generateElecSummary(sumElec[0], highlows, avgElec[0]))
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

		def averages = [:]

		if (viewType.equals("day"))	{

			def endOfDay = now.plusDays(1).minusSeconds(1)

			if (utilType.equals("elec")) {
				premiseInstance.elecReadings = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
			} else if (utilType.equals("water")) {
				premiseInstance.waterReadings = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
			} else if (utilType.equals("water")) {
				// TODO heat stub
			}
		
		} else {
		
			ArrayList electricityReadings = new ArrayList()
			ArrayList waterReadings = new ArrayList()
		
			if (viewType.equals("week")) {
				
				now = now.withDayOfWeek(DateTimeConstants.MONDAY)
				
				7.times {
					def endOfDay = now.plusDays(1).minusSeconds(1)
					def elecDay = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecDay.readingValueElec), fileDate:now.toDate()))
					def waterDay = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfDay.toDate(), [sort:"fileDate", order:"desc"])
					waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterDay.readingValueHot), readingValueCold:BillUtil.calcTotal(waterDay.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterDay.readingValueGrey), fileDate:now.toDate()))
					now = now.plusDays(1)
				}
			
			} else if (viewType.equals("month")) {
				
				def monthDays = now.dayOfMonth().getMaximumValue()
				//averages = getTrueAverage(premiseInstance.bedrooms, now.toDate(), now.plusMonths(1).minusSeconds(1).toDate())
				//getMaxMinAverage(premiseInstance.bedrooms, now.toDate(), now.plusMonths(1).minusSeconds(1).toDate())
				4.times {
					def endOfWeek = now.plusDays(7).minusSeconds(1)
					def elecWeek = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfWeek.toDate(), [sort:"fileDate", order:"desc"])
					def waterWeek = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), endOfWeek.toDate(), [sort:"fileDate", order:"desc"])
					log.debug("Dates : "+ now.toDate() +" : "+ endOfWeek.toDate() +" values : "+ BillUtil.calcTotal(elecWeek.readingValueElec))
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecWeek.readingValueElec), fileDate:now.toDate()))
					waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterWeek.readingValueHot), readingValueCold:BillUtil.calcTotal(waterWeek.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterWeek.readingValueGrey), fileDate:now.toDate()))
					now = now.plusDays(7)
				}
	
				if (monthDays > 28) {
					def diff = now.plusDays((monthDays - now.getDayOfMonth())+1).minusSeconds(1)
					def elecWeek = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), diff.toDate(), [sort:"fileDate", order:"desc"])
					def waterWeek = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), diff.toDate(), [sort:"fileDate", order:"desc"])
					log.debug("Dates : "+ now.toDate() +" : "+ diff.toDate() +" values : "+ BillUtil.calcTotal(elecWeek.readingValueElec))
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecWeek.readingValueElec), fileDate:now.toDate()))
					waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterWeek.readingValueHot), readingValueCold:BillUtil.calcTotal(waterWeek.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterWeek.readingValueGrey), fileDate:now.toDate()))
				}
				
			} else if (viewType.equals("year")) {
				12.times {
					def monthDays = now.dayOfMonth().getMaximumValue()
					def monthEnd = now.plusDays((monthDays - now.getDayOfMonth())+1).minusSeconds(1)
					def elecMonth = ElecReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), monthEnd.toDate(), [sort:"fileDate", order:"desc"])
					def waterMonth = WaterReading.findAllByPremiseAndFileDateBetween(premiseInstance, now.toDate(), monthEnd.toDate(), [sort:"fileDate", order:"desc"])
					log.debug("Dates : "+ now.toDate() +" : "+ monthEnd.toDate())
					electricityReadings.add(new ElecReading(readingValueElec:BillUtil.calcTotal(elecMonth.readingValueElec), fileDate:now.toDate()))
					waterReadings.add(new WaterReading(readingValueHot:BillUtil.calcTotal(waterMonth.readingValueHot), readingValueCold:BillUtil.calcTotal(waterMonth.readingValueCold), readingValueGrey:BillUtil.calcTotal(waterMonth.readingValueGrey), fileDate:now.toDate()))
					now = now.plusMonths(1)
				}
				
			}
			if (utilType.equals("elec")) {
				premiseInstance.elecReadings = electricityReadings
				premise.put("electricity", HelperUtil.createElectricityMap(premiseInstance))
			} else if (utilType.equals("water")) {
				premiseInstance.waterReadings = waterReadings
				premise.put("water", HelperUtil.createWaterMap(premiseInstance))
			} else if (utilType.equals("heat")) {
				// TODO heat stub
			}
		}
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
