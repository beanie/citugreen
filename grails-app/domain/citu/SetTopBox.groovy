package citu

class SetTopBox {

    String macAddress
	String TSMid
	
	Premise premise
	
	String toString() {
		return macAddress
	}
	
	static constraints = {
		macAddress(blank:true, nullable: true)
		TSMid(blank:true, nullable: true)
    }
}
