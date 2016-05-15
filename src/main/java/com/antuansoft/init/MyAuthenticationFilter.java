package com.antuansoft.init;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    protected boolean requiresAuthentication(final HttpServletRequest request, final HttpServletResponse response) {

        if (!super.requiresAuthentication(request, response)) {

            return false;
        }

        final SecurityContext securityContext = SecurityContextHolder.getContext();

        Assert.notNull(securityContext);

        final Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {

            return true;
        }

        if (!authentication.isAuthenticated()) {

            return true;
        }

        final String currentUsername = authentication.getName();

        if (currentUsername == null) {

            return true;
        }

        final String requestUsername = getRequestUsername(request);

        if (requestUsername.equals(currentUsername)) {

            return false;
        }

        return true;
    }

    private String getRequestUsername(final HttpServletRequest request) {

        final String requestUsername = obtainUsername(request);

        return (requestUsername == null) ? "" : requestUsername.trim();
    }
}