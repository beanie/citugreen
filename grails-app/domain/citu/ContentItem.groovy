package citu

class ContentItem {

    String contentTitle
	String contentBody
	String messageType
	
	String image1
	String image2

	Boolean published

	Date dateCreated

	static constraints = {
		contentBody(nullable:false, maxSize:100000)
		image1(blank:true, nullable:true)
		image2(blank:true, nullable:true)
		messageType(inList:['usageTip', 'Local Amenities', 'Energy Saving Devices','Facilities','Managing Agent','Introduction','Useful Contacts','Concierge Caretaker','Apt Services','Safety Security'],blank:false)
    }
}
