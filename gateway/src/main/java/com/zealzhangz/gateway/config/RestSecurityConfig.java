package com.zealzhangz.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zealzhangz.auth.service.AuthorityService;
import com.zealzhangz.auth.token.JwtAuthenticationProvider;
import com.zealzhangz.auth.token.JwtTokenHelper;
import com.zealzhangz.auth.token.LoginProcessingFilter;
import com.zealzhangz.auth.verify.ApiAuthenticationProcessingFilter;
import com.zealzhangz.auth.verify.ApiAuthenticationProvider;
import com.zealzhangz.auth.verify.SkipPathRequestMatcher;
import com.zealzhangz.common.model.ApiAuthority;
import com.zealzhangz.gateway.filter.CustomCorsFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

import static com.zealzhangz.common.constant.Constant.API_ROOT_URL;
import static com.zealzhangz.common.constant.Constant.AUTHENTICATION_URL;

/**
 * @author Created by ao.zhang/ao.zhang@iluvatar.ai.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/28 10:01:00<br/>
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired
    private ApiAuthenticationProvider apiAuthenticationProvider;
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private AuthorityService authorityService;

    /**
     * 注意不要忘记注入这个 Bean ，否则做具体认证时加载不到实现认证的Provider，比如这里的 jwtAuthenticationProvider
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Filter for login API
     * @param loginEntryPoint
     * @return
     * @throws Exception
     */
    protected LoginProcessingFilter buildLoginProcessingFilter(String loginEntryPoint) throws Exception {
        LoginProcessingFilter filter = new LoginProcessingFilter(loginEntryPoint, objectMapper,successHandler, failureHandler );
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    /**
     * Filter for resource request API
     * @param pathsToSkip
     * @param pattern
     * @return
     * @throws Exception
     */
    protected ApiAuthenticationProcessingFilter buildApiAuthenticationProcessingFilter(List<String> pathsToSkip, List<String> pattern) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        ApiAuthenticationProcessingFilter filter
                = new ApiAuthenticationProcessingFilter(failureHandler, matcher, jwtTokenHelper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
        auth.authenticationProvider(apiAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(AUTHENTICATION_URL);

        List<ApiAuthority> authorities = authorityService.getAllAuthority();
        for(ApiAuthority rule : authorities){
            http
                    .authorizeRequests()
                    .antMatchers(HttpMethod.resolve(rule.getMethod()), rule.getApiUrl()).hasAuthority(rule.getAuthority());
        }
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()])).permitAll()
            .and()
                .authorizeRequests()
                .antMatchers(API_ROOT_URL).authenticated()
            .and()
                .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildLoginProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildApiAuthenticationProcessingFilter(permitAllEndpointList,Arrays.asList(API_ROOT_URL)), UsernamePasswordAuthenticationFilter.class);
    }
}
