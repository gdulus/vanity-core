package vanity.i18n

import org.springframework.transaction.annotation.Transactional
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean
import vanity.pagination.PaginationParams

class MessageService implements PaginationAware<Message> {

    Message read(final Long id) {
        Message.read(id)
    }

    @Override
    PaginationBean<Message> listWithPagination(final PaginationParams params) {
        if (params.queryParams?.query) {
            String likeStatement = "%${params.queryParams.query}%"
            List<Message> result = Message.executeQuery(
                    'from Message where code like :query or text like :query order by :sort',
                    [
                            query : likeStatement,
                            max   : params.max,
                            offset: params.offset ?: 0,
                            sort  : params.sort
                    ]
            )
            int count = Message.executeQuery('select count(*) from Message where code like :query or text like :query', [query: likeStatement])[0]
            new PaginationBean<Message>(result, count)

        } else {
            new PaginationBean<Message>(Message.list(max: params.max, offset: params.offset, sort: params.sort), Message.count())
        }
    }

    @Transactional
    Message save(final String code, final Locale locale, final String text) {
        Message message = new Message(code: code, locale: locale, text: text)

        if (!message.validate()) {
            return message
        }

        message.save()
        return message
    }

    @Transactional
    Message update(final Long id, final String code, final Locale locale, final String text) {
        Message message = Message.get(id)

        if (!message) {
            return null
        }

        message.code = code
        message.locale = locale
        message.text = text

        if (!message.validate()) {
            return message
        }

        message.save()
        return message
    }

    @Transactional
    void delete(final Long id) {
        Message message = Message.get(id)

        if (message) {
            message.delete()
        }
    }
}
