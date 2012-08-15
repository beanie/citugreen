package citu

class DayJob {

		EnergyReadingService energyReadingService
		
		def cronExpression = "0 10 8 1/1 * ? *" // run on 0 second 10 minute once a day at 8 am
	
		def execute() {
			log.info("starting Heat reading download")
			energyReadingService.processHeat()

	}
}
