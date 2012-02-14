package citu

class ContentItemController extends BaseController {

	def beforeInterceptor = [action:this.&auth]

	def scaffold = true

}
