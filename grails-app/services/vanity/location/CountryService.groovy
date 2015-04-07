package vanity.location

import org.springframework.transaction.annotation.Transactional
import vanity.celebrity.Job
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean
import vanity.pagination.PaginationParams

class CountryService implements PaginationAware<Job> {

    public Country read(final Long id) {
        return Country.read(id)
    }

    List<Country> listAll() {
        return Country.list([sort: 'name'])
    }

    PaginationBean<Country> listWithPagination(final PaginationParams params) {
        return new PaginationBean<Country>(Country.list(max: params.max, offset: params.offset, sort: params.sort), Country.count())
    }

    @Transactional
    Country save(final String name, final String isoCode) {
        Country country = new Country(name: name, isoCode: isoCode)
        country.save()
        return country
    }

    @Transactional
    Country update(final Long id, final String name, final String isoCode) {
        Country country = Country.get(id)

        if (!country) {
            return null
        }

        if (name) {
            country.name = name
        }

        if (isoCode) {
            country.isoCode = isoCode
        }

        country.save()
        return country
    }

    @Transactional
    void delete(final Long id) {
        Country country = Country.get(id)

        if (country) {
            country.delete()
        }
    }
}
