package vanity.location

class City {

    String name

    Date dateCreated

    Date lastUpdated

    static belongsTo = [voivodeship: Voivodeship]

    static constraints = {
        name(nullable: false, blank: false, maxSize: 250)
    }

    static mapping = {
        version false
    }
}
