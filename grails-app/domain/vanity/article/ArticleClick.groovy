package vanity.article

class ArticleClick {

    Article article

    static belongsTo = [Article]

    static mapping = {
        version: false
    }
}
