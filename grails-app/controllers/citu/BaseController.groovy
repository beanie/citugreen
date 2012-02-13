package citu

import javax.servlet.http.*;

class BaseController {

     def auth() {

        if (!session.user) {
            redirect(controller: "user", action: "login")
        }
    }
}
