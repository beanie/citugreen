package citu
import grails.converters.*


@Grab(group='org.ccil.cowan.tagsoup', module='tagsoup', version='1.2' )


class TrainController {

    def  getTrainDepartureTimes = {

		def slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser())
		def url = new URL("http://ojp.nationalrail.co.uk/service/ldbboard/dep/LDS")

		def timeTable = new ArrayList()
		def map

		url.withReader { reader ->
			def html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse(reader)

			def rows = html.'**'.findAll {it.name() == "tr"}

			rows[1..rows.size()-1].each {row ->
				
	
				map = [destination:row.td[1].toString().replaceAll('[\n\r]', '').trim(),trainDeparture:row.td[0].toString(), trainStatus:row.td[2].toString(), trainPlatform:row.td[3].toString()]
				timeTable.add(map)

			}
		}
		
			Date tmpDate = new Date()
			def tmpStat = new Stats(logCode:'train', dateNow:tmpDate, logMessage:'depart', messageType:'info').save()


		render timeTable as JSON 
}

def  getTrainArrivalTimes = {

		def slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser())
		def url = new URL("http://ojp.nationalrail.co.uk/service/ldbboard/arr/LDS")

		def timeTable = new ArrayList()
		def map


		url.withReader { reader ->
			def html = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser()).parse(reader)

			def rows = html.'**'.findAll {it.name() == "tr"}

			rows[1..rows.size()-1].each {row ->

				map = [origin:row.td[1].toString().replaceAll('[\n\r]', '').trim(), trainArrival:row.td[0].toString(), trainDue:row.td[2].toString(), trainPlatform:row.td[3].toString()]
				timeTable.add(map)

			}
		}
		
			Date tmpDate = new Date()
			def tmpStat = new Stats(logCode:'train', dateNow:tmpDate, logMessage:'arrival', messageType:'info').save()

		render timeTable as JSON
	}
}
