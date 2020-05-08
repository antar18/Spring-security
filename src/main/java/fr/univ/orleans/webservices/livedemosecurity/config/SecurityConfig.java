package fr.univ.orleans.webservices.livedemosecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("fred").password("{noop}fred").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("USER","ADMIN");
    }*/


    //pas raisonable du tout ya antar ya chebbah ya antar ya chebbah
    /*@Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails fred = User.builder()
                .username("fred").password("{noop}fred").roles("USER").build();
        UserDetails admin = User.builder()
                .username("admin").password("{noop}admin").roles("USER","ADMIN").build();
        return new InMemoryUserDetailsManager(fred, admin);
    }*/

    @Bean
    @Override
    protected UserDetailsService userDetailsService(){
        return new CustomUserDetail();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/api/messages").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/**").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST,"/api/utilisateurs").hasRole("ADMIN")

                .anyRequest().hasRole("USER")
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
   }


}
