package com.auto.web.services;

import com.auto.data.models.Users;
import com.auto.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserService extends OidcUserService { // Расширяем OidcUserService

    @Autowired
    private UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest); // Получаем OidcUser от Google

        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName(); // Или oidcUser.getGivenName() + " " + oidcUser.getFamilyName();

        Users user = userRepository.findByEmail(email); // Ищем пользователя в базе данных по email

        if (user == null) {
            // Если пользователь не найден, создаем нового пользователя
            user = new Users();
            user.setEmail(email);
            user.setName(name);
            user.setRoles("ROLE_USER"); // Стандартная роль для OAuth2 пользователей
            // Оставляем пароль, phoneNumber и address null, так как Google их не предоставляет
            userRepository.save(user); // Сохраняем нового пользователя в базу данных
        }

        // Теперь у нас есть объект Users, связанный с OAuth2 аккаунтом, возвращаем OidcUser
        return oidcUser; // Или можно вернуть кастомный UserDetails, если нужно больше контроля

    }
}