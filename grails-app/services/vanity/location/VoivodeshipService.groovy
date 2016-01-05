package vanity.location

class VoivodeshipService {

    static transactional = false

    public List<Voivodeship> findByCountry(final Long countryId) {
        return Voivodeship.findAllByCoutry(Country.load(countryId), [sort: 'name'])
    }

}
