package com.difinite.oauth.security;

import com.difinite.oauth.tokenstore.MyTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(SecurityConfiguration.class)
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    //values for data source
    @Value("${spring.jpa.datasource.url}")
    private String dataSourceProperties;

    @Value("${spring.jpa.datasource.username}")
    private String dbUserNameProperties;

    @Value("${spring.jpa.datasource.password}")
    private String dbPasswordProperties;

    @Autowired
    public MyTokenStore tokenStore;

    @Autowired
    public AuthenticationManager auth;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                .authenticationManager(auth)
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()").passwordEncoder
                (passwordEncoder).tokenKeyAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource());
    }

    @Bean
    public MyTokenStore tokenStore(){
        return new MyTokenStore(dataSource());
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(dataSourceProperties);
        driverManagerDataSource.setSchema("users");
        driverManagerDataSource.setUsername(dbUserNameProperties);
        driverManagerDataSource.setPassword(dbPasswordProperties);
        return driverManagerDataSource;
    }

}

