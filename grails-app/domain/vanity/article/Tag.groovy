package vanity.article

class Tag implements ReviewNecessityAware {

    String name

    Status status

    Date dateCreated

    Date lastUpdated

    Boolean root = false

    static hasMany = [
        childTags: Tag
    ]

    static transients = [
        'shouldBeReviewed',
        'isPromoted'
    ]

    static constraints = {
        name(nullable: false, unique: true)
        status(nullable: false, validator: {val, obj ->
            if(val == Status.PROMOTED && !obj.root){
                return 'tag.status.aliasTagAsPromoted'
            } else {
                return true
            }
        })
    }

    static mapping = {
        version false
        name(index:true, indexAttributes: [unique:true, dropDups:false])
        status(index:true)
    }

    @Override
    boolean shouldBeReviewed() {
        return status == Status.TO_BE_REVIEWED
    }

    boolean isPromoted(){
        return status == Status.PROMOTED
    }
}
