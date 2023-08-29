package com.boot.jx.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.boot.jx.swagger.MockParamBuilder;
import com.boot.jx.swagger.MockParamBuilder.MockParam;
import com.boot.utils.ArgUtil;
import com.boot.utils.StringUtils;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Component
public class DemoSecurityConfig {

	@Configuration
	@EnableWebSecurity
	@Order(90)
	public static class StatelessWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private AuthenticationSuccessHandler successHandler;

		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {
			httpSecurity.antMatcher("/panel/**").sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
					// Permit all
					// Publics Calls
					.and().authorizeRequests().antMatchers("/panel/pub/**").permitAll() // Public URLs
					.and().authorizeRequests().antMatchers("/panel/ext/**").permitAll() // External URLS
					.and().authorizeRequests().antMatchers("/panel/int/**").permitAll() // Internal URLs
					.and().authorizeRequests().antMatchers("/panel/swagger-ui.html").permitAll() // Swagger UI
					// Login Calls
					.and().authorizeRequests().antMatchers("/panel/auth/**").permitAll()
					// API Calls
					.and().authorizeRequests().antMatchers("/panel/api/**").authenticated()
					// App Pages
					.and().authorizeRequests().antMatchers("/panel/app/**").authenticated()
					// App Request
					.and().authorizeRequests().antMatchers("/panel/**").authenticated() //
					.and().authorizeRequests().antMatchers("/panel/.**").authenticated()
					// Login Forms
					.and().formLogin().loginPage("/panel/auth/login").successHandler(successHandler).permitAll()
					.failureUrl("/panel/auth/login?error").permitAll()
					// .loginProcessingUrl("/auth/login/submit").permitAll()
					// Logout Pages
					.and().logout().permitAll().logoutUrl("/panel/auth/logout")
					.logoutSuccessUrl("/panel/auth/login?logout")
					.deleteCookies("JSESSIONID", "JXSESSIONID", "CONTAKSESSIONID").invalidateHttpSession(true)
					.permitAll().and().exceptionHandling().accessDeniedPage("/403")
					// Gen stuff
					.and().csrf().disable().headers().disable();
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/assets/**",
					"/v2/**", "/configuration/ui", "/swagger-resources/**", "/configuration/security",
					"/swagger-ui.html", "/webjars/**", "/favicon.ico");
		}

	}

	@Configuration
	@EnableWebSecurity
	@Order(95)
	public static class StompWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {
			httpSecurity.antMatcher("/stomp-tunnel/**")
					// .addFilterBefore(new SameSiteFilter(),
					// UsernamePasswordAuthenticationFilter.class)
					// filter that adds Same-Site cookie attribute (must be added in right place )
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
					// Permit all
					.and().authorizeRequests().antMatchers("/**").permitAll()
					// CSRF
					.and().csrf().disable().headers().disable();
		}

	}

	@Configuration
	@EnableWebSecurity
	@Order(99)
	public static class DefaultWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {
			httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					// Permit all
					.and().authorizeRequests().antMatchers("/**").permitAll()
					// CSRF
					.and().csrf().disable().headers().disable();
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/assets/**",
					"/v2/**", "/configuration/ui", "/swagger-resources/**", "/configuration/security",
					"/swagger-ui.html", "/webjars/**", "/favicon.ico");
		}
	}

	@Bean
	public AuthenticationSuccessHandler successHandler() {
		SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
		handler.setUseReferer(true);
		return handler;
	}

	@Bean
	public MockParam swaggerApiKeyParam() {
		return new MockParamBuilder().id("X_API_KEY").name("x-api-key").description("API Key").defaultValue("")
				.parameterType(MockParamBuilder.MockParamType.HEADER).securityScheme("X_API_KEY").build();
	}

	@Bean
	public Docket api2() {
		List<SecurityScheme> securitySchemes = new ArrayList<SecurityScheme>();
		securitySchemes.add(new ApiKey("X_API_KEY", "x-api-key",
				StringUtils.toLowerCase(ArgUtil.parseAsString(MockParamBuilder.MockParamType.HEADER))));
		return new Docket(DocumentationType.SWAGGER_2).groupName("clientdocs").select()
				.apis(RequestHandlerSelectors.basePackage("com.boot.jx.contak.nodedocs"))
				// .paths(PathSelectors.ant("/api/products/**"))
				.build().securitySchemes(securitySchemes);
	}

}
