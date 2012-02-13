package citu

class AdminController extends BaseController {
	
	def beforeInterceptor = [action:this.&auth]

    def index = { }
}
