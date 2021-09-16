package com.artuhin.sproject.config;


import com.artuhin.sproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurator extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Autowired
    RefererRedirectionAuthenticationSuccessHandler refererRedirectionAuthenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .authorizeRequests()
                //Доступ только для не зарегистрированных пользователей
//                .antMatchers("/registration", "/main", "/api/procedures/table", "/api/procedures/table/**").not().fullyAuthenticated()
                //Доступ только для пользователей с ролью Администратор
                .antMatchers("/api/appointments/get/admin/**", "/api/appointments/get/admin").hasAuthority("ADMIN")
                //Доступ разрешен всем пользователей
                .antMatchers("/main", "/main/**", "/assets/**", "/api/procedures/table", "/api/procedures/table/**").permitAll()
                //Все остальные страницы требуют аутентификации
                .anyRequest().fullyAuthenticated()
                .and()
                //Настройка для входа в систему
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/main")
                //Перенарпавление на главную страницу после успешного входа
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/login?logout");
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }
}
