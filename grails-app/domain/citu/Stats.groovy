package citu

class Stats {
		
	String logCode
	String logMessage
	String messageType
	Date dateNow
	
	Premise premise

    static constraints = {
		premise(nullable: true)
    }
}
