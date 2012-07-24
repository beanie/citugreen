package citu

class ReadingController {
	
	EnergyReadingService energyReadingService
	
	def scaffold = true
	
	def cleanUpHeatFiles = {
		energyReadingService.cleanUpHeatFiles()
		render("cleaning files")
	}
	
	
	def frig = {
		energyReadingService.frig()
		render("Manually h4x3d")
	}
	
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
	
	def processEnergyReadings = {
		energyReadingService.processEnergy()
		render("Manually processed Energy Readings")
	}
}
