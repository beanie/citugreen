package citu


class HourlyJob {
    
	EnergyReadingService energyReadingService

	def cronExpression = "0 0 * * * ?" // run on 0 second 0 minute all the time

    def execute() {
        log.info("starting quartz job")
		energyReadingService.processElec()
		energyReadingService.processWater()
    }
}
