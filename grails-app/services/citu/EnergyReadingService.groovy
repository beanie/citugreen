package citu

import au.com.bytecode.opencsv.CSVReader;

import groovyx.net.http.HTTPBuilder;
import static groovyx.net.http.Method.GET;
import static groovyx.net.http.ContentType.TEXT;
import static groovyx.net.http.ContentType.XML;
import groovy.util.XmlSlurper;
import java.text.*;


class EnergyReadingService {
	
	static transactional = true
	
	def processElec() {
		def elecUrls = EnergyFileRef.findAllByCategory('Electricity')
		processXml(elecUrls)
		log.info ("Processing Elec")
	}
	
	def processWater() {
		def waterUrls = EnergyFileRef.findAllByCategory('Water')
		processXml(waterUrls)
		log.info ("Processing Water")
	}
	
	def processHeat() {
		def f = new File("c:\\files\\")
		log.info ("Processing Heat")
		
		if( f.exists() ){
			f.eachFile(){ file->
				if( !file.isDirectory() )
					if (file.name.endsWith(".csv")) {
						log.info("Processing CSV file : "+ file.name)
						
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd")
						def tmpFileDate = df.parse(file.name)

											
						file.splitEachLine(",") {fields ->
							
							def tmpflatID = fields[0].toString()
							def flatID = tmpflatID.replace(/"/, '')
							def premise = null
							
							if (flatID) {
								premise = Premise.findByFlatNo(flatID)
							}
							
							if (premise) {
							
								def tmpHeatReading = fields[1]
								def heatReadingt = tmpHeatReading.replaceAll('"', '')
								def heatReading = heatReadingt.toFloat()
								
								def tmpHeatRead = new HeatReading(premise:premise, readingValueHeat:heatReading, fileDate:tmpFileDate).save()
												
							//	log.info ("premise found")
							} else {
								log.warn("Premise not found: "+ fields[0])
							}
						}
						file.renameTo(new File("c:\\files\\processed", file.name))
						file.delete()
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
						//	println ("premise "+premise)
							
							if (premise){
								if (urlEntry.category.equals("Electricity")) {
									ArrayList tmp = ElecReading.findAllByPremise(premise)
									def last
									if (tmp) {
										last = tmp.last().realReadingElec	
									} else {
										last = 0
									}
									
									// realValue is the difference in Value, readingValue is the raw value
									
									def realValue = (reading.item.rawvalue.toInteger()- last)
									def readingValue = reading.item.rawvalue.toInteger()		
										
											
									def tmpReading = new ElecReading(readingValueElec:realValue, realReadingElec:readingValue, fileDate:tmpFileDate, premise:premise).save()
																	
								} else if (urlEntry.category.equals("Water")) {
								
									ArrayList tmp = WaterReading.findAllByPremise(premise)
									
									def lastCold = 0
									def lastGrey = 0
									def lastHot = 0
									
									if (tmp) {
										lastCold = tmp.last().realValueCold
										lastHot = tmp.last().realValueHot
										lastGrey = tmp.last().realValueGrey
										
									}
									
									// realValue is the difference in Value, readingValue is the raw value
									
									def diffValueCold = ((reading.item[0].rawvalue.toInteger())*10- lastCold)
									def rawValueCold = (reading.item[0].rawvalue.toInteger())*10
									def diffValueHot = ((reading.item[1].rawvalue.toInteger())*10- lastHot)
									def rawValueHot = (reading.item[1].rawvalue.toInteger())*10
									def diffValueGrey = ((reading.item[2].rawvalue.toInteger())*10- lastGrey)
									def rawValueGrey = (reading.item[2].rawvalue.toInteger())*10
	
									
									def tmpReading = new WaterReading(fileDate:tmpFileDate, readingValueCold:diffValueCold, realValueCold:rawValueCold, readingValueHot:diffValueHot, realValueHot:rawValueHot, readingValueGrey:diffValueGrey, realValueGrey:rawValueGrey, premise:premise).save()
									
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
						Date tmpDate = new Date()
							
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
