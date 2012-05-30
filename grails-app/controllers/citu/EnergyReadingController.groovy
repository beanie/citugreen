package citu

class EnergyReadingController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [energyReadingInstanceList: EnergyReading.list(params), energyReadingInstanceTotal: EnergyReading.count()]
    }

    def create = {
        def energyReadingInstance = new EnergyReading()
        energyReadingInstance.properties = params
        return [energyReadingInstance: energyReadingInstance]
    }

    def save = {
        def energyReadingInstance = new EnergyReading(params)
        if (energyReadingInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'energyReading.label', default: 'EnergyReading'), energyReadingInstance.id])}"
            redirect(action: "show", id: energyReadingInstance.id)
        }
        else {
            render(view: "create", model: [energyReadingInstance: energyReadingInstance])
        }
    }

    def show = {
        def energyReadingInstance = EnergyReading.get(params.id)
        if (!energyReadingInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'energyReading.label', default: 'EnergyReading'), params.id])}"
            redirect(action: "list")
        }
        else {
            [energyReadingInstance: energyReadingInstance]
        }
    }

    def edit = {
        def energyReadingInstance = EnergyReading.get(params.id)
        if (!energyReadingInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'energyReading.label', default: 'EnergyReading'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [energyReadingInstance: energyReadingInstance]
        }
    }

    def update = {
        def energyReadingInstance = EnergyReading.get(params.id)
        if (energyReadingInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (energyReadingInstance.version > version) {
                    
                    energyReadingInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'energyReading.label', default: 'EnergyReading')] as Object[], "Another user has updated this EnergyReading while you were editing")
                    render(view: "edit", model: [energyReadingInstance: energyReadingInstance])
                    return
                }
            }
            energyReadingInstance.properties = params
            if (!energyReadingInstance.hasErrors() && energyReadingInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'energyReading.label', default: 'EnergyReading'), energyReadingInstance.id])}"
                redirect(action: "show", id: energyReadingInstance.id)
            }
            else {
                render(view: "edit", model: [energyReadingInstance: energyReadingInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'energyReading.label', default: 'EnergyReading'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def energyReadingInstance = EnergyReading.get(params.id)
        if (energyReadingInstance) {
            try {
                energyReadingInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'energyReading.label', default: 'EnergyReading'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'energyReading.label', default: 'EnergyReading'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'energyReading.label', default: 'EnergyReading'), params.id])}"
            redirect(action: "list")
        }
    }
}
