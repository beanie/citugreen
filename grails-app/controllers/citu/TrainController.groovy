package citu
import grails.converters.*


@Grab(group='org.ccil.cowan.tagsoup', module='tagsoup', version='1.2' )


class TrainController {

    def  getTrainDepartureTimes = {
				
		def tagSoupParser = new org.ccil.cowan.tagsoup.Parser()
		def slurper = new XmlSlurper(tagSoupParser)
		def timeTable = new ArrayList()
		def map
		
		def htmlParser = slurper.parse("http://www.eastcoast.co.uk/travel-information/live-train-times/leeds-departures/")
			
			String destination = ""
			String trainDeparture = ""
			
			def timeList = []
			def trainList = []
			 
		
			htmlParser.'**'.findAll{ it.@class == 'trainstatus'
			}.each {
				
				timeList.add(htmlParser.'**'.find{ it.@class == 'servicetime'
				})			
				trainList.add(htmlParser.'**'.find{ it.@class == 'stationname'
				})
			
				println timeList[5]
				println trainList[5]
			}
			
			

	//		def x = 0
	//		for ( i in trainStatus ) {
			//	destination = mainDestinations[x].toString()
			//	trainDeparture = trainDepartures[x].toString()	
			//	map = [destination:destination, trainDeparture:trainDeparture]
			//	timeTable.add(map)
			//	x++
		//	}
		
		render timeTable as JSON
	}

def  getTrainArrivalTimes = {
	
	def slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser())
	def url = new URL("http://www.eastcoast.co.uk/travel-information/live-train-times/leeds-arrivals/")
	def timeTable = new ArrayList()
	def map

	url.withReader { reader ->

		def html = slurper.parse(reader)

			String origin = ""
			String trainArrival = ""

			def trainStatus = html.'**'.findAll{ it.@class == 'trainstatus'
			}
			def trainArrivals = html.'**'.findAll{ it.@class == 'servicetime'
			}
			def origins = html.'**'.findAll{ it.@class == 'stationname'
			}

			def x = 0
			for ( i in trainStatus ) {
				origin  = origins[x].toString()
				trainArrival = trainArrivals[x].toString()
				map = [origin:origin, trainArrival:trainArrival]
				timeTable.add(map)
				x++
				}
		}
	render timeTable as JSON
	}
}

