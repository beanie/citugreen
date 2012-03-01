package citu

import groovyx.net.http.HTTPBuilder;
import static groovyx.net.http.Method.GET;
import static groovyx.net.http.ContentType.TEXT;
import static groovyx.net.http.ContentType.XML;
import groovy.util.XmlSlurper;
import java.text.*;

class EnergyReadingService {
	
	static transactional = true
	
	def frig() {
	
		def now = new Date()
		
		Premise p = Premise.findByFlatNo("215")
		Premise p1 = Premise.findByFlatNo("606")
		Premise p2 = Premise.findByFlatNo("106")
		Premise p3 = Premise.findByFlatNo("608")
		Premise p4 = Premise.findByFlatNo("610")
	
		def random = new Random()
	
		ElecReading tmpReading = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p1).save()
		ElecReading tmpReading1 = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p2).save()
		ElecReading tmpReading2 = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p).save()
		ElecReading tmpReading3 = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p3).save()
		ElecReading tmpReading4 = new ElecReading(readingValueElec:random.nextInt(8), fileDate:now, premise:p4).save()
		WaterReading tmpWater = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p1).save()
		WaterReading tmpWater1 = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p2).save()
		WaterReading tmpWater2 = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p3).save()
		WaterReading tmpWater3 = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p4).save()
		WaterReading tmpWater4 = new WaterReading(fileDate:now, readingValueCold:random.nextInt(30), readingValueHot:random.nextInt(30) + 2, readingValueGrey:random.nextInt(30) + 1, premise:p).save()

	}
	
	def processElec() {
		def elecUrls = EnergyFileRef.findAllByCategory('Electricity')
		processXml(elecUrls)
	}
	
	def processWater() {
		def waterUrls = EnergyFileRef.findAllByCategory('Water')
		processXml(waterUrls)
	}
	
	def processHeat() {
		def f = new File("c:\\files\\")
		if( f.exists() ){
			f.eachFile(){ file->
				if( !file.isDirectory() )
					if (file.name.endsWith("CSV")) {
						log.info("Processing CSV file : "+ file.name)
						fileRef.splitEachLine(",") {fields ->
							def premise = Premise.findByFlatNo(fields[0])
							if (premise){
								def tmpReading = new HeatReading(readingValueHeat:fields[1].toString(), premise:premise).save()
							} else {
								log.warn("Premise not found: "+ fields[0])
							}
						}
						file.renameTo(new File("c:\\files\\processed", file.name))
						log.info("Processed CSV file : "+ file.name)
					}
			}
		}
	}
	
	def processXml(ArrayList urlList) {
		
		urlList.each { urlEntry ->
			
			def http = new HTTPBuilder(urlEntry.urlPath)
			
			log.info("Processing XML file : "+ urlEntry.urlPath)
			
			try {
				
				http.request( GET, TEXT ) {
					
					headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
					
					// 30 second timeout
					request.getParams().setParameter("http.connection.timeout", new Integer(15000));
					request.getParams().setParameter("http.socket.timeout", new Integer(15000));
					
					// response handler for a success response code:
					response.success = { resp, reader ->
						def readingsXML = new XmlSlurper().parse(reader)	
						def readings = readingsXML.group
						def fileInfo = readingsXML.project
						
						log.info("Parsing "+ urlEntry.category +" info. for: "+ fileInfo.name +" Created: "+ fileInfo.creationdate)
						
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
						def tmpFileDate = df.parse(fileInfo.creationdate.toString())
						
						readings.element.each { reading ->
							def premise = Premise.findByFlatNo(reading.name.toString())
							if (premise){
								if (urlEntry.category.equals("Electricity")) {
									ArrayList tmp = ElecReading.findAllByPremise(premise)
									def last
									if (tmp) {
										last = tmp.first().readingValueElec
									} else {
										last = 0
									}
									def realValue = (reading.item.rawvalue.toInteger() - last)
									def tmpReading = new ElecReading(readingValueElec:realValue, fileDate:tmpFileDate, premise:premise).save()
								} else if (urlEntry.category.equals("Water")) {
									def tmpReading = new WaterReading(fileDate:tmpFileDate, readingValueCold:reading.item[0].valuelong.toString(), readingValueHot:reading.item[1].valuelong.toString(), readingValueGrey:reading.item[2].valuelong.toString(), premise:premise).save()
								}
							} else {
								log.warn("Premise not found: "+ reading.name.toString())
							}
						}
						log.info("Processed XML file : "+ urlEntry.urlPath)
					}
					
					// handler for any failure status code:
					response.failure = { resp ->
						log.error("Unexpected error: ${resp.status} : ${resp.statusLine.reasonPhrase}")
					}
				}
			} catch (Exception e) {
				log.error(e)
			}
		}
	}
	
	/*
	 * No longer needed - does not handle timeouts
	 */
	def processXmlOld(ArrayList urlList) {
		
		urlList.each { urlEntry ->
			try {
				
				log.info("Processing XML file : "+ urlEntry.urlPath)
				
				urlEntry.urlPath.withReader { reader ->
					
					def readingsXML = new XmlSlurper().parse(reader)	
					def readings = readingsXML.group
					def fileInfo = readingsXML.project
					
					log.info("Parsing "+ urlEntry.category +" info. for: "+ fileInfo.name +" Created: "+ fileInfo.creationdate)
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
					def tmpFileDate = df.parse(fileInfo.creationdate.toString())
					
					readings.element.each { reading ->
						def premise = Premise.findByFlatNo(reading.name.toString())
						if (premise){
							if (urlEntry.category.equals("Electricity")) {
								ArrayList tmp = ElecReading.findAllByPremise(premise)
								log.info("TEMP SIZE"+ tmp.size())
								def last
								// TODO re-add this - check with phil
								if (tmp) {
									last = tmp.first().readingValueElec
									if (tmp.size() == 1) {
										ElecReading er = tmp.first()
										log.info("ER + "+ er)
										er.setPremise(null)
										log.info("delete")
										er.delete()
										// er.delete()
									}
								} else {
									last = 0
								}
								def realValue = (reading.item.rawvalue.toInteger() - last)
								def tmpReading = new ElecReading(readingValueElec:realValue, fileDate:tmpFileDate, premise:premise).save()
							} else if (urlEntry.category.equals("Water")) {
								def tmpReading = new WaterReading(fileDate:tmpFileDate, readingValueCold:reading.item[0].valuelong.toString(), readingValueHot:reading.item[1].valuelong.toString(), readingValueGrey:reading.item[2].valuelong.toString(), premise:premise).save()
							}
						} else {
							log.warn("Premise not found: "+ reading.name.toString())
						}
					}
				}
				log.info("Processed XML file : "+ urlEntry.urlPath)
			} catch (Exception e) {
				log.error(e)
			}
		}
	}
}
