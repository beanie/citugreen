package citu

class EnergyItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [energyItemInstanceList: EnergyItem.list(params), energyItemInstanceTotal: EnergyItem.count()]
    }

    def create = {
        def energyItemInstance = new EnergyItem()
        energyItemInstance.properties = params
        return [energyItemInstance: energyItemInstance]
    }

    def save = {
        def energyItemInstance = new EnergyItem(params)
        if (energyItemInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'energyItem.label', default: 'EnergyItem'), energyItemInstance.id])}"
            redirect(action: "show", id: energyItemInstance.id)
        }
        else {
            render(view: "create", model: [energyItemInstance: energyItemInstance])
        }
    }

    def show = {
        def energyItemInstance = EnergyItem.get(params.id)
        if (!energyItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'energyItem.label', default: 'EnergyItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            [energyItemInstance: energyItemInstance]
        }
    }

    def edit = {
        def energyItemInstance = EnergyItem.get(params.id)
        if (!energyItemInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'energyItem.label', default: 'EnergyItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [energyItemInstance: energyItemInstance]
        }
    }

    def update = {
        def energyItemInstance = EnergyItem.get(params.id)
        if (energyItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (energyItemInstance.version > version) {
                    
                    energyItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'energyItem.label', default: 'EnergyItem')] as Object[], "Another user has updated this EnergyItem while you were editing")
                    render(view: "edit", model: [energyItemInstance: energyItemInstance])
                    return
                }
            }
            energyItemInstance.properties = params
            if (!energyItemInstance.hasErrors() && energyItemInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'energyItem.label', default: 'EnergyItem'), energyItemInstance.id])}"
                redirect(action: "show", id: energyItemInstance.id)
            }
            else {
                render(view: "edit", model: [energyItemInstance: energyItemInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'energyItem.label', default: 'EnergyItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def energyItemInstance = EnergyItem.get(params.id)
        if (energyItemInstance) {
            try {
                energyItemInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'energyItem.label', default: 'EnergyItem'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'energyItem.label', default: 'EnergyItem'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'energyItem.label', default: 'EnergyItem'), params.id])}"
            redirect(action: "list")
        }
    }
}
