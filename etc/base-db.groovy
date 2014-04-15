dataSource.driverClassName = 'org.postgresql.Driver'
dataSource.dialect = 'org.hibernate.dialect.PostgreSQLDialect'
dataSource.username = 'vanity'
dataSource.password = 'vanity'
dataSource.url = 'jdbc:postgresql://localhost:5432/vanity'
dataSource.dbCreate = 'update'
dataSource.pooled = true

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.provider_class = 'org.hibernate.cache.EhCacheProvider'
}
