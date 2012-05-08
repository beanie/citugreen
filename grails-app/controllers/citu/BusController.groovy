package citu

import grails.converters.*

class BusController {
	
	def  getBusTimes = {
		
		def busStop = params.busStop
		def later = params.later
		
		def busRef
		
		switch (busStop) {
			case 'CouplandRoad':
			busRef = '45010086'
			break
			case 'HunsletHallRoad':
			busRef = '45010059'
			break
			case 'NorthecoteGreen':
			busRef = '45010088'
			break
			case 'BrettGardens':
			busRef = '45013205'
			break
		}
		
		def slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser())
		
		def url = new URL("http://wypte.acislive.com/pip/stop.asp?naptan="+ busRef +"&pscode=1&dest=&offset="+ later +"&textonly=1")
		
		def timeTable = new ArrayList()
		def map
		
		
		url.withReader { reader ->
			
			def html = slurper.parse(reader)
			
			String busNo = ""
			String destination = ""
			String busDue = ""
			
			def busNos = html.'**'.findAll{ it.@width == '25%'
			}
			def destinations = html.'**'.findAll{ it.@class == 'destination'
			}
			def busTimes = html.'**'.findAll{ it.@align == 'right'
			}
			
			def x = 0
			for ( i in busNos ) {
				busNo = busNos[x].toString()
				destination = destinations[x].toString()
				busDue = busTimes[x].toString()
				map = [busNo:busNo, busDetination:destination, busDue:busDue]
				timeTable.add(map)
				x++
			}
		}
		
			Date tmpDate = new Date()
			def tmpStat = new Stats(logCode:'bus', dateNow:tmpDate, logMessage:busStop, messageType:'info').save()


		render timeTable as JSON
	}
}
