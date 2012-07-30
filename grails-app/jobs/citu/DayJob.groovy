package citu


class DayJob {

	RankService rankService

	def cronExpression = "0 10 23 ? * WED" // run on 0 second 0 minute all the time

    def execute() {
        log.info("starting Day quartz job")
		rankService.generateRankings()
    }
}

class HeatReadingsJob {
	
		EnergyReadingService energyReadingService
	
		def cronExpression = "0 10 8 * * *" // run on 0 second 0 minute all the time
	
		def execute() {
			log.info("starting Heat reading download")
			energyReadingService.cleanUpHeatFiles()
		}
	}
	
