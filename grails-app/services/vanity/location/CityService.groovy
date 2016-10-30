package vanity.location

class CityService {

    static transactional = false

    public List<City> findByVoivodeship(final Long voivodeshipId) {
        return voivodeshipId ? City.findAllByVoivodeship(Voivodeship.load(voivodeshipId), [sort: 'name']) : Collections.emptyList()
    }

}
