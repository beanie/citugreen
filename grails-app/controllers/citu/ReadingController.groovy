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
		render("Manually processed Water Readings")
	}
	
	def processHeatReadings = {
		energyReadingService.processHeat()
		render("Manually processed Heat Readings")
	}
}
