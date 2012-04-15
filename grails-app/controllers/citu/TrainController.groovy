package citu
import grails.converters.*


class TrainController {

    def  getTrainTimes = {
		
			
		def slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser())
		
		def url = new URL("http://www.eastcoast.co.uk/travel-information/live-train-times/leeds-departures/")
		
		def timeTable = new ArrayList()
		def map
		
		url.withReader { reader ->
			
			def html = slurper.parse(reader)
			
			String destination = ""
			String trainDeparture = ""

			def trainStatus = html.'**'.findAll{ it.@class == 'trainstatus'
			}
			def trainDepartures = html.'**'.findAll{ it.@class == 'servicetime'
			}
			def destinations = html.'**'.findAll{ it.@class == 'stationname'
			}
		
	
			def x = 0
			for ( i in trainStatus ) {
				destination = destinations[x].toString()
				trainDeparture = trainDepartures[x].toString()
				
				println (" Trains "+trainDeparture)
				println (" Destination "+destination)
				
				map = [Destination:destination, TrainDepartures:trainDeparture]
				timeTable.add(map)
				x++
			}
		}
		
		render timeTable as JSON
		}
	}
