package vanity.stats

import org.springframework.transaction.annotation.Transactional
import vanity.article.*

class PopularityService {

    TagService tagService

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
    public List<Article> findTopArticlesByTag(final Tag tag, final Integer max, final Integer maxDays) {
        return (List<Article>) ArticlePopularity.executeQuery('''
                select
                    article
                from
                    ArticlePopularity pop
                    join pop.article article
                where
                    pop.day >= :startDay
                    and article.status = :status
                    and :tag in elements(article.tags)
                group by
                    article
                order by
                    max(pop.rank) desc
                ''',
            [
                startDay: (new Date() - maxDays).clearTime(),
                status: ArticleStatus.ACTIVE,
                tag: tag,
                max: max
            ]
        )
    }

    @Transactional(readOnly = true)
    public List<Article> getTopArticlesFromDate(final Date fromDate, final Integer max, final Integer offset = 0) {
        if (!fromDate || !max) {
            return Collections.emptyList()
        }

        List<Long> result = (List<Long>) ArticlePopularity.executeQuery('''
                select
                   article.id
                from
                    ArticlePopularity pop
                    join pop.article article
                where
                    pop.day >= :fromDate
                    and article.status = :articleStatus
                group by
                    article.id
                order by
                    article.publicationDate desc,
                    max(pop.rank) desc
                ''',
            [
                fromDate: fromDate.clearTime(),
                articleStatus: ArticleStatus.ACTIVE
            ],
            [
                max: max,
                offset: offset
            ])

        return result.collect { Article.read(it) }
    }

    @Transactional(readOnly = true)
    public Integer countTopArticlesFromDate(final Date fromDate) {
        if (!fromDate) {
            return 0
        }

        return ArticlePopularity.executeQuery('''
                select
                    count(distinct article.id)
                from
                    ArticlePopularity pop
                    join pop.article article
                where
                    pop.day >= :fromDate
                    and article.status = :articleStatus
                ''',
            [
                fromDate: fromDate.clearTime(),
                articleStatus: ArticleStatus.ACTIVE
            ]
        )[0]
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
                statuses: TagStatus.OPEN_STATUSES
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
                statuses: TagStatus.OPEN_STATUSES
            ],
            [
                max: max
            ])

        return result.collect { PopularityDTO.valueOf(it) }
    }

    @Transactional(readOnly = true)
    public Map<Tag, List<Article>> findOtherPopular(final Article article, final Integer max, final Integer maxDays) {
        Map<Tag, List<Article>> results = [:]
        List<Tag> rootTags = article.getPublicTags().sum { Tag it -> tagService.findAllRootParents(it.id) }
        for (final Tag rootTag : rootTags) {
            List<Article> articles = findTopArticlesByTag(rootTag, max, maxDays)
            articles.removeAll { it.id == article.id }
            if (articles) {
                results[rootTag] = articles
            }
        }

        return results
    }
}
