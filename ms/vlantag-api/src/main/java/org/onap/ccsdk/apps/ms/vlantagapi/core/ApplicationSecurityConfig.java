/*******************************************************************************
 * Copyright © 2017-2018 AT&T Intellectual Property.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.onap.ccsdk.apps.ms.vlantagapi.core;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * ApplicationSecurityConfig.java Purpose: Configures and validates
 * Application Security configurations
 * 
 * @author Saurav Paira
 * @version 1.0
 */
@Configuration
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter{
	private Logger logger = LoggerFactory.getLogger(ApplicationSecurityConfig.class);
	 
	@Autowired
	private Environment environment;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		List<UserDetails> userDetails = new ArrayList<>();
		
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    	final User.UserBuilder userBuilder = User.builder().passwordEncoder(encoder::encode);

		String authString = environment.getProperty("application.authToken");
		String[] tokens = authString.split(";");
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			String[] cred = token.split("~"); 
			String[] uidpwdarr = decode(cred[0]);
			logger.info("------uid/pwd ----------------{}, {}",uidpwdarr[0],uidpwdarr[1]);	
			
			UserDetails user = userBuilder
     	            .username(uidpwdarr[0])
     	            .password(uidpwdarr[1])
     	            .roles(cred[1])
     	            .build();
			
			userDetails.add(user);
		}
		
		logger.info("-------------------------------{}",userDetails);
		auth.userDetailsService(inMemoryUserDetailsManager(userDetails));
	}
	
    
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(List<UserDetails> userDetails) {   	    
        return new InMemoryUserDetailsManager(userDetails);
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().fullyAuthenticated();
	    http.httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.csrf().disable();
	}
	
    private static String[] decode(String encoded) {
        final byte[] decodedBytes 
                = Base64.getDecoder().decode(encoded.getBytes());
        final String pair = new String(decodedBytes);
        return pair.split(":", 2);
    }

}
