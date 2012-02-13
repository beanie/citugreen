package citu

// import com.lucastex.grails.fileuploader.UFile

class ContentItem {

    String contentTitle
	String contentBody

	Boolean published

	Date dateCreated

	/*
	static hasMany=[images:UFile]
	*/

	static constraints = {
		contentBody(nullable:false, maxSize:100000)
    }
}
