package vanity.stats

import vanity.article.Article

class ArticlePopularity extends Popularity {

    static belongsTo = [article: Article]

}
