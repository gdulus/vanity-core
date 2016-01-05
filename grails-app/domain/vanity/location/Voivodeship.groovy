package vanity.location

class Voivodeship {

    String name

    Date dateCreated

    Date lastUpdated

    static belongsTo = [coutry: Country]

    static hasMany = [cities: City]

    static constraints = {
        name(nullable: false, blank: false, maxSize: 250)
    }

    static mapping = {
        version false
    }
}
