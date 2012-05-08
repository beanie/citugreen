package citu

class StatsController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [statsInstanceList: Stats.list(params), statsInstanceTotal: Stats.count()]
    }

    def create = {
        def statsInstance = new Stats()
        statsInstance.properties = params
        return [statsInstance: statsInstance]
    }

    def save = {
        def statsInstance = new Stats(params)
        if (statsInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'stats.label', default: 'Stats'), statsInstance.id])}"
            redirect(action: "show", id: statsInstance.id)
        }
        else {
            render(view: "create", model: [statsInstance: statsInstance])
        }
    }

    def show = {
        def statsInstance = Stats.get(params.id)
        if (!statsInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'stats.label', default: 'Stats'), params.id])}"
            redirect(action: "list")
        }
        else {
            [statsInstance: statsInstance]
        }
    }

    def edit = {
        def statsInstance = Stats.get(params.id)
        if (!statsInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'stats.label', default: 'Stats'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [statsInstance: statsInstance]
        }
    }

    def update = {
        def statsInstance = Stats.get(params.id)
        if (statsInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (statsInstance.version > version) {
                    
                    statsInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'stats.label', default: 'Stats')] as Object[], "Another user has updated this Stats while you were editing")
                    render(view: "edit", model: [statsInstance: statsInstance])
                    return
                }
            }
            statsInstance.properties = params
            if (!statsInstance.hasErrors() && statsInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'stats.label', default: 'Stats'), statsInstance.id])}"
                redirect(action: "show", id: statsInstance.id)
            }
            else {
                render(view: "edit", model: [statsInstance: statsInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'stats.label', default: 'Stats'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def statsInstance = Stats.get(params.id)
        if (statsInstance) {
            try {
                statsInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'stats.label', default: 'Stats'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'stats.label', default: 'Stats'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'stats.label', default: 'Stats'), params.id])}"
            redirect(action: "list")
        }
    }
}
