package vanity.location

class Country {

    String name

    Date dateCreated

    Date lastUpdated

    static constraints = {
        name(nullable: false, blank: false, maxSize: 250, unique: true)
    }
}
