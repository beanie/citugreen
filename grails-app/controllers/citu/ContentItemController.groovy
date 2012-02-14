package citu

import grails.converters.*

class ContentItemController extends BaseController {

	def beforeInterceptor = [action:this.&auth, except:["getContent"]]
	
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [contentItemInstanceList: ContentItem.list(params), contentItemInstanceTotal: ContentItem.count()]
    }

    def create = {
        def contentItemInstance = new ContentItem()
        contentItemInstance.properties = params
        return [contentItemInstance: contentItemInstance]
    }

    def save = {
        def contentItemInstance = new ContentItem(params)
		
        if (contentItemInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'contentItem.label', default: 'ContentItem'), contentItemInstance.id])}"
            
			
			def webRootDir = servletContext.getRealPath("/")
			def userDir = new File(webRootDir, "/contentImages/${contentItemInstance.id}")
			userDir.mkdirs()
			//handle uploaded files
			
			def uploadedFile = request.getFile('tmpImage1')
			if(!uploadedFile.empty){			  
			  uploadedFile.transferTo(new File(userDir, uploadedFile.originalFilename))
			  contentItemInstance.image1 = uploadedFile.originalFilename
			  contentItemInstance.save()
			}
			
			//handle uploaded files
			def uploadedFile1 = request.getFile('tmpImage2')
			if(!uploadedFile1.empty){
			  uploadedFile1.transferTo(new File(userDir, uploadedFile1.originalFilename))
			  contentItemInstance.image2 = uploadedFile1.originalFilename
			  contentItemInstance.save()
			}			
			redirect(action: "show", id: contentItemInstance.id)
        }
        else {
            render(view: "create", model: [contentItemInstance: contentItemInstance])
        }
    }

    def show = {
        def contentItemInstance = ContentItem.get(params.id)
        if (!contentItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'contentItem.label', default: 'ContentItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            [contentItemInstance: contentItemInstance]
        }
    }

    def edit = {
        def contentItemInstance = ContentItem.get(params.id)
        if (!contentItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'contentItem.label', default: 'ContentItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [contentItemInstance: contentItemInstance]
        }
    }

    def update = {
        def contentItemInstance = ContentItem.get(params.id)
        if (contentItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (contentItemInstance.version > version) {
                    
                    contentItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'contentItem.label', default: 'ContentItem')] as Object[], "Another user has updated this ContentItem while you were editing")
                    render(view: "edit", model: [contentItemInstance: contentItemInstance])
                    return
                }
            }
            contentItemInstance.properties = params
            if (!contentItemInstance.hasErrors() && contentItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'contentItem.label', default: 'ContentItem'), contentItemInstance.id])}"
                redirect(action: "show", id: contentItemInstance.id)
            }
            else {
                render(view: "edit", model: [contentItemInstance: contentItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'contentItem.label', default: 'ContentItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def contentItemInstance = ContentItem.get(params.id)
        if (contentItemInstance) {
            try {
                contentItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'contentItem.label', default: 'ContentItem'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'contentItem.label', default: 'ContentItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'contentItem.label', default: 'ContentItem'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def getContent = {
		ArrayList contentList = new ArrayList()
		def webRootDir = servletContext.getRealPath("/")
		def contentItems = ContentItem.findAllByPublished(new Boolean("True"), [sort:"dateCreated", order:"desc"])
		contentItems.each { ci ->
			def map = [title:ci.contentTitle, body:ci.contentBody, createdDate:ci.dateCreated]
			if (ci.image1) {
				map.put("image1", "contentImages/"+ ci.id +"/"+ ci.image1)
			}
			if (ci.image2) {
				map.put("image2", "contentImages/"+ ci.id +"/"+ ci.image2)
			}			
			contentList.add(map)
		}
		render contentList as JSON
	}
}
