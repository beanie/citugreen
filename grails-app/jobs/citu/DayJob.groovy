package citu


class DayJob {

class HeatReadingsJob {
	
		EnergyReadingService energyReadingService
		
		def cronExpression = "0 10 8 * * *" // run on 0 second 10 minute once a day at 8 am
	
		def execute() {
			log.info("starting Heat reading download")
			energyReadingService.processHeat()
		}
	}
}
