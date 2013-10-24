package vanity.celebrity

import vanity.article.Tag
import vanity.image.gorm.Image

class Celebrity {

    String firstName

    String lastName

    String description

    Image avatar

    static belongsTo = [
        tag: Tag
    ]

    static constraints = {
        firstName(nullable: true, blank: true)
        lastName(nullable: true, blank: true)
        description(nullable: true, blank: true)
        tag(nullable: false, unique: true, validator: {
            return it?.root
        })
        avatar(nullable: true)
    }

    static mapping = {
        description(type: 'text')
    }

    static embedded = [
        'avatar'
    ]

    @Override
    public String toString() {
        return "Celebrity{" +
            "tag='" + tag + '\'' +
            '}';
    }

}
