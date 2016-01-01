package vanity.user

class Profile {

    String email

    Gender gender

    Date dateOfBirth

    Date dateCreated

    Date lastUpdated

    static constraints = {
        gender nullable: true
        dateOfBirth nullable: true
        email nullable: false, blank: false, email: true, unique: true
    }

}
