/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antuansoft.spring.security;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.antuansoft.herospace.*;
import com.antuansoft.init.Thing;
import com.antuansoft.mongodb.connection.MongoConnection;
import com.antuansoft.spring.mvc.MainController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import com.antuansoft.mongodb.repositories.UserRepositoryDao;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * @author anjana.m
 */
@Component
public class MongoUserDetailsService implements UserDetailsService, AuthenticationSuccessHandler {

    @Resource
    private UserRepositoryDao userRepositoryDao;
    @Autowired
    ApplicationContext ctx;
    private static final Logger logger = Logger.getLogger(MongoUserDetailsService.class);

    private org.springframework.security.core.userdetails.User userdetails;

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        com.antuansoft.mongodb.domain.User user = getUserDetail(username);
        System.out.println(username);
        System.out.println(user.getPassword());
        System.out.println(user.getUsername());
        System.out.println(user.getRole());

        userdetails = new User(user.getUsername(),
                user.getPassword(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                getAuthorities(user.getRole()));
//        return userdetails;
        return new UserDetailsWrapper(user, getAuthorities(user.getRole()), "hey you");
    }

    public List<GrantedAuthority> getAuthorities(String[] roles) {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

        for (String role : roles) {
            authList.add(new SimpleGrantedAuthority(role));
        }


        System.out.println(authList);
        return authList;
    }

    public com.antuansoft.mongodb.domain.User getUserDetail(String username) {
        com.antuansoft.mongodb.domain.User user = userRepositoryDao.findByUsername(username);
        //System.out.println(user.toString());

        return user;
    }

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Autowired
    ApplicationContext appCtx;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //MongoConnection.dbConfig = userRepositoryDao.findByUsername(authentication.getName()).getClient_schema();

       /* Thing thing = appCtx.getBean(Thing.class);
        thing.name = "testing";
        thing.getName();
        PersonDAO personDAO = appCtx.getBean(PersonDAO.class);
        com.antuansoft.init.MongoConnection mc= new com.antuansoft.init.MongoConnection();
        mc.dbName=AuthenticationUtil.getCurrentUser().getClient_schema();
        mc.collectionName="person";
        mc.connectionURL="localhost:27017";
        personDAO.collectionName="jkjkjkj";
       // mc.collectionName
        personDAO.collectionName=mc.connectionURL;*/
        redirectStrategy.sendRedirect(request, response, "/menu");

    }
}