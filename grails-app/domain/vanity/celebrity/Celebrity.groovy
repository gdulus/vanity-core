package vanity.celebrity

import vanity.article.Tag
import vanity.location.Country
import vanity.user.Gender

class Celebrity {

    String firstName

    String lastName

    String nickName

    String description

    Gender gender

    Integer height

    Date birthDate

    String birthLocation

    Boolean dead = false

    Date deathDate

    String deathLocation

    SortedSet<Job> jobs

    SortedSet<Quotation> quotations

    SortedSet<Country> countries

    Date dateCreated

    Date lastUpdated

    static belongsTo = [
        tag: Tag
    ]

    static hasMany = [
        jobs: Job,
        quotations: Quotation,
        countries: Country
    ]

    static constraints = {
        firstName(nullable: true, blank: true)
        lastName(nullable: true, blank: true)
        nickName(nullable: true, blank: true)
        description(nullable: true, blank: true)
        height(nullable: true)
        birthDate(nullable: true)
        birthLocation(nullable: true)
        deathDate(nullable: true)
        deathLocation(nullable: true)
        tag(nullable: false, unique: true, validator: { return it?.root })
        jobs(maxSize: 5)
    }

    static mapping = {
        description(type: 'text')
        quotations(sort: 'dateCreated', order: 'asc')
    }

    static embedded = [
        'avatar',
        'birth',
        'death'
    ]

    static transients = [
        'getImagePath',
        'hasImage',
        'fullName',
        'age',
        'zodiacSign'
    ]

    public def getAge() {
        use(groovy.time.TimeCategory) {
            def duration = dead ? deathDate - birthDate : new Date() - birthDate
            return (int) (duration.days / 365)
        }
    }

    public ZodiacSign getZodiacSign() {
        return ZodiacSign.findByDate(birthDate)
    }


    String getFullName() {
        return "${firstName} ${lastName}"
    }

    @Override
    public String toString() {
        return "Celebrity{" +
            "tag='" + tag + '\'' +
            '}';
    }

}
