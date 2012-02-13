package citu

class User {
	
	String firstName
	String lastName
	
	String userType
	String password
	
	String userName
	String contactEmail
	Long vmUserId
	
	Date dateCreated 
	
	String toString() {
		return userName
	}

    static constraints = {
		password(blank:true, nullable: true)
		vmUserId(blank:false, nullable: false)		
		userName(blank:false, nullable: false, size:3..80)
		contactEmail(blank:false, nullable: false, email:true, unique:true)
		userType inList: ["user", "admin"]
    }
}
