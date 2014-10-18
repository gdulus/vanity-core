package vanity.tracking

import vanity.article.Article

class ArticleClick extends Click {

    static belongsTo = [article: Article]

}
