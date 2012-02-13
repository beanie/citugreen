package citu

//import com.lucastex.grails.fileuploader.UFile

class ContentItemController extends BaseController {

	def beforeInterceptor = [action:this.&auth]

	def scaffold = true

    /*
	def add = {
		log.debug "Uploaded file with id=${params.ufileId}"
		[files: UFile.list(), params:params]
	}


	def delete = {
		def ufile = UFile.get(params.id)
		ufile.delete()
		redirect action:index
	}
	*/
}
