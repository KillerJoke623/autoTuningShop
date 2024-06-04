package com.auto.data.services;

import com.auto.data.models.Users;
import com.auto.data.repositories.TuningOrdersRepository;
import com.auto.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
