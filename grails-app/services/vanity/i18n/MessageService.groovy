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
        new PaginationBean<Message>(Message.list(max: params.max, offset: params.offset, sort: params.sort), Message.count())
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
