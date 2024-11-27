package ca.mcmaster.cas735.acmepark.lot_management.technical;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("Lot Management") private String applicationName;
    @Value("V0") private String applicationVersion;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title(applicationName).version(applicationVersion));
    }
}
