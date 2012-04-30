package citu

class WaterReadingController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def scaffold = true
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [waterReadingInstanceList: WaterReading.list(params), waterReadingInstanceTotal: WaterReading.count()]
    }

    def create = {
        def waterReadingInstance = new WaterReading()
        waterReadingInstance.properties = params
        return [waterReadingInstance: waterReadingInstance]
    }

    def save = {
        def waterReadingInstance = new WaterReading(params)
        if (waterReadingInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'waterReading.label', default: 'WaterReading'), waterReadingInstance.id])}"
            redirect(action: "show", id: waterReadingInstance.id)
        }
        else {
            render(view: "create", model: [waterReadingInstance: waterReadingInstance])
        }
    }

    def show = {
        def waterReadingInstance = WaterReading.get(params.id)
        if (!waterReadingInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waterReading.label', default: 'WaterReading'), params.id])}"
            redirect(action: "list")
        }
        else {
            [waterReadingInstance: waterReadingInstance]
        }
    }

    def edit = {
        def waterReadingInstance = WaterReading.get(params.id)
        if (!waterReadingInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waterReading.label', default: 'WaterReading'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [waterReadingInstance: waterReadingInstance]
        }
    }

    def update = {
        def waterReadingInstance = WaterReading.get(params.id)
        if (waterReadingInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (waterReadingInstance.version > version) {
                    
                    waterReadingInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'waterReading.label', default: 'WaterReading')] as Object[], "Another user has updated this WaterReading while you were editing")
                    render(view: "edit", model: [waterReadingInstance: waterReadingInstance])
                    return
                }
            }
            waterReadingInstance.properties = params
            if (!waterReadingInstance.hasErrors() && waterReadingInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'waterReading.label', default: 'WaterReading'), waterReadingInstance.id])}"
                redirect(action: "show", id: waterReadingInstance.id)
            }
            else {
                render(view: "edit", model: [waterReadingInstance: waterReadingInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waterReading.label', default: 'WaterReading'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def waterReadingInstance = WaterReading.get(params.id)
        if (waterReadingInstance) {
            try {
                waterReadingInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'waterReading.label', default: 'WaterReading'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'waterReading.label', default: 'WaterReading'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'waterReading.label', default: 'WaterReading'), params.id])}"
            redirect(action: "list")
        }
    }
}
