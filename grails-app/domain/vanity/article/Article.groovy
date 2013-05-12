package vanity.article

class Article implements ReviewNecessityAware {

    String externalId

    String hash

    ContentSource source

    String title

    String body

    String url

    Date publicationDate

    Integer rank = 0

    Date dateCreated

    Date lastUpdated

    static hasMany = [
        tags: Tag
    ]

    static transients = [
        'shouldBeReviewed'
    ]

    static constraints = {
        externalId(nullable:false, unique: 'source')
        hash(nullable:false, blank: false, unique: true)
        source(nullable: false)
        body(nullable: false, blank: false)
        title(nullable: false, blank: false)
        url(nullable: false, blank: false, url: true)
        publicationDate(nullable: false)
        tags(nullable: false, minSize: 1)
    }

    static mapping = {
        version false
        body(type: 'text')
    }

    def beforeValidate(){
        setUpHash()
    }

    def beforeInsert() {
        setUpHash()
    }

    @Override
    public String toString() {
        return "Article{" +
                "url='" + url + '\'' +
                '}';
    }

    @Override
    boolean shouldBeReviewed() {
        // this operation can be performed only on instance with tags
        if (!tags){
            throw new IllegalStateException('Seems that this instance has no tags. Cant determine review necessity.')
        }
        // if there is no tas with state to be reviewed then all is ok
        return (tags.find({it.shouldBeReviewed()}) != null)
    }

    private void setUpHash(){
        if (url && !hash){
            hash = "${url}vanity-article".encodeAsMD5()
        }
    }
}
