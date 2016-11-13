package vanity.celebrity

class CelebrityImageService {

    public CelebrityImage getMain(final Celebrity celebrity) {
        if (!celebrity) {
            return null
        }

        List<CelebrityImage> result = CelebrityImage.executeQuery('''
            select
                c
            from
                CelebrityImage c
            where
                c.celebrity = :celebrity
                and c.state = :state
            order by
                c.dateCreated desc
         ''',
                [
                        celebrity: celebrity,
                        state    : CelebrityImageStatus.REVIEWED
                ])

        result ? result.first() : null
    }


}
