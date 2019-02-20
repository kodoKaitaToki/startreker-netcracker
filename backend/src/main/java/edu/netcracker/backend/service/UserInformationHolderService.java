package edu.netcracker.backend.service;

import edu.netcracker.backend.security.jwt.UserInformationHolder;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserInformationHolderService {
    String convertAsString(UserDetails userPrincipal);
    UserInformationHolder convertAsUserInfo(String userInfo);
}
