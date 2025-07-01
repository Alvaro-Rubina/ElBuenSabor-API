package org.spdgrupo.elbuensaborapi.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${auth0.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${web.cors.allowed-origins}")
    private String corsAllowedOrigins;

    @Value("${spring.websecurity.debug:false}")
    boolean webSecurityDebug;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults()) //por defecto spring va a buscar un bean con el nombre "corsConfigurationSource".
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Endpoints públicos
                                .requestMatchers("/api/clientes/email/**").permitAll()
                                .requestMatchers("/api/empleados/email/**").permitAll()
                                .requestMatchers("/api/mercado-pago/webhook/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/rubroinsumos/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/rubroproductos/**").permitAll()

                                // Roles - GET para todos los empleados (Cajero, Delivery, Cocinero, Admin)
                                .requestMatchers(HttpMethod.GET, "/api/admin/roles/**").hasAnyAuthority("Cajero", "Delivery", "Cocinero", "Admin")

                                // Pedidos - GET y PUT para Cajero, Delivery, Cocinero, Admin
                                .requestMatchers(HttpMethod.GET, "/api/pedidos/**").hasAnyAuthority("Cajero", "Delivery", "Cocinero", "Admin")
                                .requestMatchers(HttpMethod.PUT, "/api/pedidos/**").hasAnyAuthority("Cajero", "Delivery", "Cocinero", "Admin")
                                .requestMatchers(HttpMethod.POST, "/api/pedidos/**").hasAuthority("Admin")
                                .requestMatchers(HttpMethod.DELETE, "/api/pedidos/**").hasAuthority("Admin")

                                // Empleados - GET y PUT para Cajero, Delivery, Cocinero, Admin (excepto email que ya es público)
                                .requestMatchers(HttpMethod.GET, "/api/empleados/**").hasAnyAuthority("Cajero", "Delivery", "Cocinero", "Admin")
                                .requestMatchers(HttpMethod.PUT, "/api/empleados/**").hasAnyAuthority("Cajero", "Delivery", "Cocinero", "Admin")
                                .requestMatchers("/api/empleados/**").hasAuthority("Admin")

                                // Productos - Cocinero y Admin
                                .requestMatchers("/api/productos/**").hasAnyAuthority("Cocinero", "Admin")

                                // Insumos - Cocinero y Admin
                                .requestMatchers("/api/insumos/**").hasAnyAuthority("Cocinero", "Admin")

                                // Promociones - Cocinero y Admin
                                .requestMatchers("/api/promociones/**").hasAnyAuthority("Cocinero", "Admin")

                                // Rubros - Resto de operaciones solo para Admin (GET ya es público)
                                .requestMatchers("/api/rubroinsumos/**").hasAuthority("Admin")
                                .requestMatchers("/api/rubroproductos/**").hasAuthority("Admin")

                                // Estadísticas - Solo Admin
                                .requestMatchers("/api/estadisticas/**").hasAuthority("Admin")

                                // Clientes - Solo Admin (excepto email que ya es público)
                                .requestMatchers("/api/clientes/**").hasAuthority("Admin")

                                // Resto de endpoints - Solo Admin
                                .requestMatchers("/api/domicilios/**").hasAuthority("Admin")
                                .requestMatchers("/api/detalledomicilios/**").hasAuthority("Admin")
                                .requestMatchers("/api/facturas/**").hasAuthority("Admin")
                                .requestMatchers("/api/mercado-pago/**").hasAuthority("Admin")
                                .requestMatchers("/api/admin/**").hasAuthority("Admin")

                                // Usuario autenticado para endpoints de usuario
                                .requestMatchers("/api/usuarios/**").authenticated()

                                .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(jwt ->
                                        jwt
                                                .decoder(jwtDecoder())
                                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                )
                );
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(corsAllowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("X-Get-Header"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthoritiesClaimName(audience + "/roles");
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(webSecurityDebug);
    }

}
