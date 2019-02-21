package edu.netcracker.backend.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.netcracker.backend.security.UserInformationHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserInformationHolderService {

    private static final Logger logger = LoggerFactory.getLogger(UserInformationHolderService.class);

    @Autowired
    private ObjectMapper objectMapper;

    public String convertAsString(UserDetails userPrincipal)  {
        try {
            return objectMapper.writeValueAsString(UserInformationHolder.
                    createUserInformationHolder(userPrincipal));
        } catch (JsonProcessingException e) {
            logger.error("Can't parse userInfo as string " + e);
        }

        return "";
    }

    public UserInformationHolder convertAsUserInfo(String userInfo)  {
        UserInformationHolder userInformationHolder = null;
        try {
            userInformationHolder = objectMapper.readValue(userInfo, UserInformationHolder.class);
        } catch (IOException e) {
            logger.error("Can't parse userInfo as object " + e);
        }

        return userInformationHolder;
    }

}
