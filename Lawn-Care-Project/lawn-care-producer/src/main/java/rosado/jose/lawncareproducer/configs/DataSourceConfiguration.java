package rosado.jose.lawncareproducer.configs;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    /**
     * Defines the environment variables used to connect to our database.  These variables need to be set:
     *
     * 1. In your configuration for lawn-care-2-application - if you run the project locally
     * 2. In your configuration for lawn-care-2-tests - if you run the tests locally
     * 3. In the lawn-care-app service in docker-compose.yml - when you deploy your app to Docker
     */
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dsb = DataSourceBuilder.create();
        dsb.url("jdbc:mysql://" + System.getenv("DATABASE_HOST") + "/" + System.getenv("DATABASE_NAME"));
        dsb.username(System.getenv("DATABASE_USERNAME"));
        dsb.password(System.getenv("DATABASE_PASSWORD"));
        return dsb.build();
    }
}
