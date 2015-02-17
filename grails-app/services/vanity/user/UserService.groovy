package vanity.user

import grails.transaction.Transactional

class UserService {

    def springSecurityService

    @Transactional
    public User create(final String username, final String password, final @DelegatesTo(Profile) Closure profileBinder) {
        Profile profile = new Profile()
        profile.with profileBinder
        profile.save(failOnError: true)
        User user = new User(username: username, password: springSecurityService.encodePassword(password), profile: profile)
        UserRole.create(user, Role.findByAuthority(Authority.ROLE_PORTAL_USER))
        user.save(failOnError: true)
    }

    @Transactional
    public User update(final Long id, final @DelegatesTo(Profile) Closure profileBinder) {
        User user = User.get(id)
        user.profile.with profileBinder
        user.save(failOnError: true)
    }

}
