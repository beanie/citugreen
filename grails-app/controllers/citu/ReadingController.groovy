package citu

class ReadingController {

	EnergyReadingService energyReadingService
	
	def scaffold = true
	
	def processElecReadings = {
		energyReadingService.processElec()
		render("Manually processed Electricity Readings")
	}
	
	def processWaterReadings = {
		energyReadingService.processWater()
	}
	
	def processHeatReadings = {
		energyReadingService.processHeat()
	}
}
