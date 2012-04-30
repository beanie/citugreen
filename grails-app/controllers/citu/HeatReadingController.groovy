package citu

class HeatReadingController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def scaffold = true
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [heatReadingInstanceList: HeatReading.list(params), heatReadingInstanceTotal: HeatReading.count()]
    }

    def create = {
        def heatReadingInstance = new HeatReading()
        heatReadingInstance.properties = params
        return [heatReadingInstance: heatReadingInstance]
    }

    def save = {
        def heatReadingInstance = new HeatReading(params)
        if (heatReadingInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'heatReading.label', default: 'HeatReading'), heatReadingInstance.id])}"
            redirect(action: "show", id: heatReadingInstance.id)
        }
        else {
            render(view: "create", model: [heatReadingInstance: heatReadingInstance])
        }
    }

    def show = {
        def heatReadingInstance = HeatReading.get(params.id)
        if (!heatReadingInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'heatReading.label', default: 'HeatReading'), params.id])}"
            redirect(action: "list")
        }
        else {
            [heatReadingInstance: heatReadingInstance]
        }
    }

    def edit = {
        def heatReadingInstance = HeatReading.get(params.id)
        if (!heatReadingInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'heatReading.label', default: 'HeatReading'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [heatReadingInstance: heatReadingInstance]
        }
    }

    def update = {
        def heatReadingInstance = HeatReading.get(params.id)
        if (heatReadingInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (heatReadingInstance.version > version) {
                    
                    heatReadingInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'heatReading.label', default: 'HeatReading')] as Object[], "Another user has updated this HeatReading while you were editing")
                    render(view: "edit", model: [heatReadingInstance: heatReadingInstance])
                    return
                }
            }
            heatReadingInstance.properties = params
            if (!heatReadingInstance.hasErrors() && heatReadingInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'heatReading.label', default: 'HeatReading'), heatReadingInstance.id])}"
                redirect(action: "show", id: heatReadingInstance.id)
            }
            else {
                render(view: "edit", model: [heatReadingInstance: heatReadingInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'heatReading.label', default: 'HeatReading'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def heatReadingInstance = HeatReading.get(params.id)
        if (heatReadingInstance) {
            try {
                heatReadingInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'heatReading.label', default: 'HeatReading'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'heatReading.label', default: 'HeatReading'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'heatReading.label', default: 'HeatReading'), params.id])}"
            redirect(action: "list")
        }
    }
}
