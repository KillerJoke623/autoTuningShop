package com.auto.data.services;

import com.auto.data.models.Users;
import com.auto.data.repositories.TuningOrdersRepository;
import com.auto.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private TuningOrdersRepository tuningOrdersRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(Users user) {
        // Дополнительные проверки или обработка данных перед сохранением пользователя
        userRepository.save(user);
    }

    public Users getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    public Users getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
            // OAuth2 User
            String email = oidcUser.getEmail();

            return getUserByEmail(email);
        } else {
            // Standard Form Login User
            String userName = authentication.getName();
            return getUserByEmail(userName);
        }
    }

    public boolean isUserCredentialsValid(String email, String password) {
        Users user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    public Long getTotalUsersCount() {
        return userRepository.count();
    }

    public Long getActiveUsersCount() {
        return userRepository.countDistinctByTuningOrdersesIsNotNull();
    }

}
