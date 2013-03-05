package vanity.article

class Tag implements ReviewNecessityAware {

    String name

    Tag parentTag

    Status status

    Date dateCreated

    Date lastUpdated

    static transients = [
        'shouldBeReviewed'
    ]

    static constraints = {
        name(nullable: false, unique: true)
        parentTag(nullable: true)
        status(nullable: false)
    }

    static mapping = {
        version false
        name(index:true, indexAttributes: [unique:true, dropDups:false])
    }

    @Override
    boolean shouldBeReviewed() {
        return status == Status.TO_BE_REVIEWED
    }
}
