package citu

class WeekJob {

	RankService rankService

	def cronExpression = "0 0 23 ? * SUN *" // run on 0 second 0 minute all the time

    def execute() {
        log.info("starting Week quartz job")
		rankService.generateRankings()
    }
}

	
