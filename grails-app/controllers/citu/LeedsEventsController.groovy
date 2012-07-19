package citu

import grails.converters.*

class LeedsEventsController {

	def  getEvents = {
		
		def slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser())
		def url = new URL("http://www.leedsinspired.co.uk/event-finder?query=categories:;date_ranges_string:dont_mind;where:59#search_result")
		def leedsEvents = new ArrayList()
		def map
		
		url.withReader { reader ->
			
			def html = slurper.parse(reader)	
			def events = html.'**'.findAll{ it.@class == 'search-result'}.each {item ->
			
				def title = item.h4.a
				def where = item.ul.li[0].a
				def when = item.ul.li[1]
				def what = item.p

				map = [title:title.toString(),where:where.toString(), when:when.toString(),what:what.toString().replaceAll('[\n\r]', ' ')]
				leedsEvents.add(map)

			}
		}
		render leedsEvents as JSON
	}
	
	def  getCinema = {
		
		def later = params.later
		
		def slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser())
		def url = new URL("http://www.google.co.uk/movies?hl=en&near=leeds&ei=4wLzT8nbOsmn0QW_44XpDw&tid=20a814d02a681d79&date="+ later +"")
		def leedsCine = new ArrayList()
		def map
		
		String schedule = ""
		String name = ""
		String info = ""
		
		url.withReader { reader ->
			
			def html = slurper.parse(reader)
				def events = html.'**'.findAll{ it.@class == 'movie'}
				
				def names = html.'**'.findAll{ it.@class == 'name'}
				def infos = html.'**'.findAll{ it.@class == 'info'}
				def schedules = html.'**'.findAll{ it.@class == 'times'}

				def x = 0
				for ( i in events ) {
					

					schedule = schedules[x].toString()
					name = names[x].toString()
					info = infos[x].toString().replaceAll(~/- Trailer - IMDb - : /, '')
					map = [title:name, schedule:schedule, info:info]
					leedsCine.add(map)
					x++
					
				}
		}
		render leedsCine as JSON
	}
	
}
