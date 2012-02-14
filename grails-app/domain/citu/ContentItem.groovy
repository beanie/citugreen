package citu

class ContentItem {

    String contentTitle
	String contentBody
	
	String image1
	String image2

	Boolean published

	Date dateCreated

	static constraints = {
		contentBody(nullable:false, maxSize:100000)
		image1(blank:true, nullable:true)
		image2(blank:true, nullable:true)
    }
}
