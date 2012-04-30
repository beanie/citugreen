package citu

class ElecReadingController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def scaffold = true
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [elecReadingInstanceList: ElecReading.list(params), elecReadingInstanceTotal: ElecReading.count()]
    }

    def create = {
        def elecReadingInstance = new ElecReading()
        elecReadingInstance.properties = params
        return [elecReadingInstance: elecReadingInstance]
    }

    def save = {
        def elecReadingInstance = new ElecReading(params)
        if (elecReadingInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'elecReading.label', default: 'ElecReading'), elecReadingInstance.id])}"
            redirect(action: "show", id: elecReadingInstance.id)
        }
        else {
            render(view: "create", model: [elecReadingInstance: elecReadingInstance])
        }
    }

    def show = {
        def elecReadingInstance = ElecReading.get(params.id)
        if (!elecReadingInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'elecReading.label', default: 'ElecReading'), params.id])}"
            redirect(action: "list")
        }
        else {
            [elecReadingInstance: elecReadingInstance]
        }
    }

    def edit = {
        def elecReadingInstance = ElecReading.get(params.id)
        if (!elecReadingInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'elecReading.label', default: 'ElecReading'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [elecReadingInstance: elecReadingInstance]
        }
    }

    def update = {
        def elecReadingInstance = ElecReading.get(params.id)
        if (elecReadingInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (elecReadingInstance.version > version) {
                    
                    elecReadingInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'elecReading.label', default: 'ElecReading')] as Object[], "Another user has updated this ElecReading while you were editing")
                    render(view: "edit", model: [elecReadingInstance: elecReadingInstance])
                    return
                }
            }
            elecReadingInstance.properties = params
            if (!elecReadingInstance.hasErrors() && elecReadingInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'elecReading.label', default: 'ElecReading'), elecReadingInstance.id])}"
                redirect(action: "show", id: elecReadingInstance.id)
            }
            else {
                render(view: "edit", model: [elecReadingInstance: elecReadingInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'elecReading.label', default: 'ElecReading'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def elecReadingInstance = ElecReading.get(params.id)
        if (elecReadingInstance) {
            try {
                elecReadingInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'elecReading.label', default: 'ElecReading'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'elecReading.label', default: 'ElecReading'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'elecReading.label', default: 'ElecReading'), params.id])}"
            redirect(action: "list")
        }
    }
}
