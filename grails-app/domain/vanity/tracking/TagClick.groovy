package vanity.tracking

import vanity.article.Tag

class TagClick extends Click {

    static belongsTo = [tag: Tag]

}
