package vanity.stats

import vanity.article.Tag

class TagPopularity extends Popularity {

    static belongsTo = [tag: Tag]

}
