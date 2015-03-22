package vanity.celebrity

import org.springframework.transaction.annotation.Transactional
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean
import vanity.pagination.PaginationParams

class CelebrityJobService implements PaginationAware<Job> {

    @Transactional(readOnly = true)
    public Job read(final Long id) {
        return Job.read(id)
    }

    @Transactional(readOnly = true)
    PaginationBean<Job> listWithPagination(final PaginationParams params) {
        return new PaginationBean<Job>(Job.list(max: params.max, offset: params.offset, sort: params.sort), Job.count())
    }

    @Transactional
    Job save(final String name) {
        Job job = new Job(name: name)
        job.save()
        return job
    }

    @Transactional
    Job update(final Long id, final String name) {
        Job job = Job.get(id)

        if (!job) {
            return null
        }

        job.name = name
        job.save()
        return job
    }

    @Transactional
    void delete(final Long id) {
        Job job = Job.get(id)

        if (job) {
            job.delete()
        }
    }

}
