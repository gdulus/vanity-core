package vanity.user

import vanity.location.City

class Profile {

    String email

    Gender gender

    String avatar

    City city

    Date dateOfBirth

    Date dateCreated

    Date lastUpdated

    static constraints = {
        gender nullable: true
        dateOfBirth nullable: true
        avatar nullable: true
        city nullable: true
        email nullable: false, blank: false, email: true, unique: true
    }

}
