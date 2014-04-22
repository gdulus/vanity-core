package vanity.utils

import groovy.util.logging.Slf4j
import vanity.article.ContentSource
import vanity.user.Authority
import vanity.user.Role
import vanity.user.User
import vanity.user.UserRole

@Slf4j
class BootstrapUtils {

    def springSecurityService

    public void init() {
        log.info('Initializing default DB values')
        initContentSource()
        initRoles()
        initUsers()
        log.info('Initialization done')
    }

    private void initRoles() {
        Authority.ALL.each {
            if (!Role.findByAuthority(it)) {
                log.info('Creating Role {}', it)
                new Role(authority: it).save(flush: true)
            }
        }
    }

    private void initUsers() {
        initUser('admin', Role.findByAuthority(Authority.ROLE_ADMIN))
        initUser('reviewer', Role.findByAuthority(Authority.ROLE_REVIEWER))
    }

    private void initUser(final String username, final Role role) {
        if (!User.findByUsername(username)) {
            log.info('Creating User {}', username)
            User user = new User(username: username, password: springSecurityService.encodePassword(username)).save(flush: true)
            UserRole.create(user, role, true)

        }
    }

    private void initContentSource() {
        ContentSource.Target.values().each {
            if (!ContentSource.findByTarget(it)) {
                log.info('Creating ContentSource {}', it)
                new ContentSource(target: it).save(flush: true)
            }
        }
    }
}
