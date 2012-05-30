package citu

import org.joda.time.*

class RankService {

	def sessionFactory  // injected
	def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
	
	static transactional = true
	
	private void cleanUpGorm()  {
		def session = sessionFactory.currentSession
		session.flush()
		session.clear()
		propertyInstanceMap.get().clear()
	}

    def generateRankings() {	
		
		// get all the occupied properites
		def premises = Premise.findAllByOccupied(true)
		log.info("Occupied Properties: "+ premises.size())
		
		// get the the data for the last week
		def now = new DateTime()
		def weekStart = now.withDayOfWeek(DateTimeConstants.MONDAY)
		
		log.debug("weekStart : "+ weekStart.toDate())
		log.debug("now : "+ now.toDate())
		
		// spin through and get all the data
		for (i in premises) {
			def sum = 0
			def sumElec = ElecReading.executeQuery("select sum(reading.readingValueElec) from ElecReading as reading where reading.dateCreated between:date1 AND :date2 ", [date1:weekStart.toDate(), date2:now.toDate()])
			def sumWater = WaterReading.executeQuery("select sum(reading.readingValueHot), sum(reading.readingValueCold), sum(reading.readingValueGrey) from WaterReading as reading where reading.dateCreated between:date1 AND :date2 ", [date1:weekStart.toDate(), date2:now.toDate()])
			def sumHeat = HeatReading.executeQuery("select sum(reading.readingValueHeat) from HeatReading as reading where reading.fileDate between:date1 AND :date2 ", [date1:weekStart.toDate(), date2:now.toDate()])
			if (sumElec[0]) sum = sum + sumElec[0].toFloat()
			if (sumHeat[0]) sum = sum + sumHeat[0].toFloat()
			if (sumWater[0][0]) sum = sum + sumWater[0][0].toFloat()
			if (sumWater[0][1]) sum = sum + sumWater[0][1].toFloat()
			if (sumWater[0][2]) sum = sum + sumWater[0][2].toFloat()
			
			i.rankValue = sum / i.bedrooms
			i.save()
		}
		
		premises = Premise.findAllByOccupied(true, [sort:"rankValue", order:"desc"])
		
		def x = 0
		for (i in premises) {
			x ++
			i.rank = x +" of "+ premises.size()
			i.save()
		}
		
		cleanUpGorm()
		
		// if its the last day of the week, archive them
		if (now.dayOfWeek().getAsShortText().equals('Sun')) {
			for (i in premises) {
				i.prevWeekRank = i.rank
				i.save()
			}
			cleanUpGorm()
		}
		
    }
	
}
