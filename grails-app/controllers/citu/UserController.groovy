package citu

class UserController extends BaseController {
	
	def beforeInterceptor = [action:this.&auth, except:["login","doLogin"]]
	
	def scaffold = true
	
	def login = {
	}
	
	def doLogin = {
		//def user = User.findByContactEmailAndPassword(params.email, params.password.encodeAsHash());
		
		def user = User.findByContactEmailAndPassword(params.email, params.password);
		
		if (user != null) {
			session.user = user
			redirect(controller: "admin")
		} else {
			flash.message = "Sorry - couldn't find matching details"
			redirect(action: "login")
		}
	}
	
	def logout = {
		session.user = null;
		redirect(uri:"/")
	}
}
