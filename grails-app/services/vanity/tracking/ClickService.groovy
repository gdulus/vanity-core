package vanity.tracking

import org.springframework.transaction.annotation.Transactional

class ClickService {

    @Transactional
    public Integer deleteAllByArticleIds(final Set<Long> ids) {
        if (!ids) {
            return 0
        }

        return ArticleClick.executeUpdate('''
            delete from
                ArticleClick
            where
                article.id in (:ids)
        ''',
            [
                ids: ids
            ])
    }

    @Transactional
    public Integer deleteAllByTagIds(final Set<Long> ids) {
        if (!ids) {
            return 0
        }

        return ArticleClick.executeUpdate('''
            delete from
                TagClick
            where
                tag.id in (:ids)
        ''',
            [
                ids: ids
            ])
    }
}
