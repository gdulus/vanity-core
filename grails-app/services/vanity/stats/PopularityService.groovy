package vanity.stats

import org.springframework.transaction.annotation.Transactional
import vanity.article.Article
import vanity.article.Status
import vanity.article.Tag

class PopularityService {

    @Transactional
    public void update(final Article article, final Date day, final Integer rankDelta) {
        if (!article) {
            return
        }

        Popularity popularity = ArticlePopularity.findByArticleAndDay(article, day) ?: new ArticlePopularity(article: article, day: day)
        popularity.rank += rankDelta
        popularity.save()
    }

    @Transactional
    public void update(final Tag tag, final Date day, final Integer rankDelta) {
        if (!tag) {
            return
        }

        Popularity popularity = TagPopularity.findByTagAndDay(tag, day) ?: new TagPopularity(tag: tag, day: day)
        popularity.rank += rankDelta
        popularity.save()
    }

    @Transactional(readOnly = true)
    public List<PopularityDTO> getTopArticlesFromDate(final Date fromDate, final Integer max) {
        if (!fromDate || !max) {
            return Collections.emptyList()
        }

        List<Object[]> result = (List<Object[]>) ArticlePopularity.executeQuery('''
            select
                article.id, max(pop.rank)
            from
                ArticlePopularity pop
                join pop.article article
            where
                pop.day >= :fromDate
            group by
                article.id
            order by
                max(pop.rank) desc
            ''',
            [
                fromDate: fromDate.clearTime()
            ],
            [
                max: max,
            ])

        return result.collect { PopularityDTO.valueOf(it) }
    }

    @Transactional(readOnly = true)
    public List<Article> getTopArticles(final Integer max, final Integer offset) {
        return (List<Article>) ArticlePopularity.executeQuery('''
            select
                article
            from
                ArticlePopularity pop
                join pop.article article
            group by
                article
            order by
                max(pop.rank) desc
            ''',
            [
                max: max,
                offset: offset
            ])
    }

    @Transactional(readOnly = true)
    public Integer countTopArticles() {
        return ArticlePopularity.count()
    }

    @Transactional(readOnly = true)
    public List<PopularityDTO> findTopTagsFromDate(final Date fromDate, final Integer max) {
        if (!fromDate || !max) {
            return Collections.emptyList()
        }

        List<Object[]> result = (List<Object[]>) TagPopularity.executeQuery('''
            select
                tag.id, max(pop.rank)
            from
                TagPopularity pop
                join pop.tag tag
            where
                pop.day >= :fromDate
                and tag.root = true
                and tag.status in :statuses
            group by
                tag.id
            order by
                max(pop.rank) desc
            ''',
            [
                fromDate: fromDate.clearTime(),
                statuses: Status.Tag.OPEN_STATUSES
            ],
            [
                max: max
            ])

        return result.collect { PopularityDTO.valueOf(it) }
    }

    @Transactional(readOnly = true)
    public List<PopularityDTO> findTopTags(final Integer max) {
        List<Object[]> result = (List<Object[]>) TagPopularity.executeQuery('''
            select
                tag.id, max(pop.rank)
            from
                TagPopularity pop
                join pop.tag tag
            where
                tag.root = true
                and tag.status in :statuses
            group by
                tag.id
            order by
                max(pop.rank) desc
            ''',
            [
                statuses: Status.Tag.OPEN_STATUSES
            ],
            [
                max: max
            ])

        return result.collect { PopularityDTO.valueOf(it) }
    }
}
