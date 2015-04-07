package vanity.celebrity

class Quotation implements Comparable<Quotation> {

    String content

    String source

    Date dateCreated

    Date lastUpdated

    static belongsTo = [
        celebrity: Celebrity
    ]

    static constraints = {
        content(nullable: false, blank: false, maxSize: 500)
        source(nullable: false, blank: false, maxSize: 100)
    }

    static mapping = {
        content(type: 'text')
    }

    @Override
    int compareTo(Quotation o) {
        return dateCreated.compareTo(o.dateCreated)
    }
}
