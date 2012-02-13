package citu

class EnergyFileRefController extends BaseController {

	def beforeInterceptor = [action:this.&auth]

	EnergyReadingService energyReadingService

	def scaffold = true

}
