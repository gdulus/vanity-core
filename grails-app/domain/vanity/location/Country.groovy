package vanity.location

class Country implements Comparable<Country> {

    String name

    String isoCode

    Date dateCreated

    Date lastUpdated

    static constraints = {
        name(nullable: false, blank: false, maxSize: 250)
        isoCode(nullable: false, blank: false, maxSize: 10, unique: true)
    }

    @Override
    int compareTo(Country o) {
        return name.compareTo(o.name)
    }
}
