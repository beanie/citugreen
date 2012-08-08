package citu

import org.apache.commons.net.ftp.FTPClient;
import au.com.bytecode.opencsv.CSVReader;

import groovyx.net.http.HTTPBuilder;
import static groovyx.net.http.Method.GET;
import static groovyx.net.http.ContentType.TEXT;
import static groovyx.net.http.ContentType.XML;
import groovy.util.XmlSlurper;
import java.text.*;
import java.util.Date;

@Grab(group='commons-net', module='commons-net', version='2.0')

class EnergyReadingService {
	
	def sessionFactory  // injected
	def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP

	static transactional = true
	
	private void cleanUpGorm()  {
		def session = sessionFactory.currentSession
		session.flush()
		session.clear()
		propertyInstanceMap.get().clear()
	}

	def sendError(String text, String title) {
		sendMail {
			to "ben@chilling.co.uk","sonic@sellick.org"
			from "alerts@vmbunker.net"
			subject text
			body title
		 }
	}
	
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

	def processEnergy() {
		def energyUrls = EnergyFileRef.findAllByCategory('energyIn')
		processXmlEnergy(energyUrls)
		log.info ("Processing Energy")
	}
	
	def readDirToArray(String dir) {
		def p = new File(dir)
		def fileList = new ArrayList()
		if (p.exists()) {
			log.debug("directory exists")
			p.eachFile() { pfile ->
				fileList.add(pfile.name)
			}
		} else {
			log.debug("directory not exist")
		}
		return fileList
	}
	
	def processHeat() {
		
		def unProc = "c:\\files\\"
		def proc = "c:\\files\\processed\\"
		
		// def unProc = "/Users/beanie/tmp"
		// def proc = "/Users/beanie/tmp/processed"
		
		def host     = 'office.citu.co.uk'
		def path     = '/*'
		def user     = 'virgin'
		def password = 'virginftp1066'
		def port = '60100'
		def fles
				
		log.info ("clearing files")
		
		// grab the local processed list
		def myList = readDirToArray(proc)
			
		new FTPClient().with {
			setDefaultPort(60100)
			connect host
			enterLocalPassiveMode()
			log.info(replyString)
		 
			login user, password
			log.info(replyString)
			
			def fileCount=0
			
			fles = listNames()
			fles.each { fl ->
				
					if (fl.endsWith(".csv"))
					{
						if (myList.contains(fl)) {
							log.debug(fl + " in the list")
						} else {
							log.info(fl + " NOT in the list")
							def incomingFile = new File (fl)
							incomingFile.withOutputStream { ostream -> retrieveFile fl, ostream }
							incomingFile.renameTo(new File(unProc, fl))
							fileCount++
						}
					}
				}
			if (fileCount == 0) {
				sendError("Citu Error", "Heat readings count : "+ fileCount)
			}
			disconnect()
		}
		
		def f = new File(unProc)
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
								log.info("Premise not found: "+ fields[0])
							}
						}
						file.renameTo(new File(proc, file.name))
						file.delete()
						cleanUpGorm()
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

								log.info("Premise not found: "+ reading.name.toString())
							}
						}
						cleanUpGorm()
						log.info("Processed XML file : "+ urlEntry.urlPath)
					}

					// handler for any failure status code:
					response.failure = { resp ->
						log.error("Unexpected error: ${resp.status} : ${resp.statusLine.reasonPhrase}")
						Date tmpDate = new Date()

					}
				}
			} catch (Exception e) {
				sendError("Citu Error", "Processing XML with : "+ e)
				log.error(e)
			}
		}
	}

	/*
	 * No longer needed - does not handle timeouts
	 */
	def processXmlEnergy(ArrayList urlList) {

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
						def name = reading.name.toString()
						def escapedName = name.replaceAll(" ", "")
						def energyItem = EnergyItem.findByCollector(escapedName)

						if (energyItem) {

							log.info ("Processing energyIn element : "+ name)

							if (urlEntry.category.equals("energyIn")) {

								ArrayList tmp = EnergyReading.findAllByEnergyItem(energyItem)

								def last
								if (tmp) {
									last = tmp.last().realReadingIn
								} else {
									last = 0
								}

								def realValue = reading.item.rawvalue.toInteger()
								def readingValue = (reading.item.rawvalue.toInteger()- last)

								def tmpReading = new EnergyReading(fileDate:tmpFileDate, readingValueIn:readingValue, realReadingIn:realValue, energyItem:energyItem).save()

							}
						} else {
							log.info("energyIn element not found: "+ reading.name.toString())
						}

					}
				}
				cleanUpGorm()
				log.info("Processed XML file : "+ urlEntry.urlPath)
			} catch (Exception e) {
				sendError("Citu Error", "Processing XML with : "+ e)
				log.error(e)
			}
		}
	}
}
