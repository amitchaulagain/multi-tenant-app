package com.antuansoft.herospace;

import com.antuansoft.mongodb.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by amit on 5/11/16.
 */



public class AuthenticationUtil {

    public static final User getCurrentUser() {
        UserDetailsWrapper userDetailsWrapper = getUserDetailsWrapper();

        if (userDetailsWrapper != null) {
            return userDetailsWrapper.getUser();
        }

        return null;
    }

    private static UserDetailsWrapper getUserDetailsWrapper() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        UserDetailsWrapper userDetailsWrapper = null;
        if (principal instanceof UserDetailsWrapper) {
            userDetailsWrapper = ((UserDetailsWrapper) principal);
        }
        return userDetailsWrapper;
    }

    public static final Object getServiceResponse() {
        UserDetailsWrapper userDetails = getUserDetailsWrapper();
        return userDetails.getServiceResponse();
    }

    public static final void setServiceResponse(Object serviceResponse) {
        UserDetailsWrapper userDetails = getUserDetailsWrapper();
        userDetails.setServiceResponse(serviceResponse);
    }
}