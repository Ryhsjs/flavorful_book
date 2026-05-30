package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.exception.IllegalUserArgumentException;
import ru.itis.flavorful_book.entity.User;
import ru.itis.flavorful_book.repository.UserRepository;
import ru.itis.flavorful_book.util.validation.Validator;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final Validator validator;

    public UserServiceImpl(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public boolean update(Long id, String username, String avatarUrl) {
        validator.validateUserExistence(id);
        User oldUser = findById(id);

        validateUserData(username, oldUser.getUsername());

        oldUser.setUsername(username);
        oldUser.setAvatarUrl(avatarUrl);

        return userRepository.update(oldUser);
    }

    @Override
    public User findById(Long id) {
        validator.validateUserExistence(id);
        return userRepository.findById(id);
    }

    private void validateUserData(String newUsername, String oldUsername) {
        IllegalUserArgumentException exception = new IllegalUserArgumentException("Ошибка в имени пользователя");
        exception.setUsernameState(validator.checkString(newUsername));
        if (exception.getUsernameState() == null && !newUsername.equals(oldUsername)) {
            if (userRepository.existsByUsername(newUsername))
                exception.setUsernameState("Это имя пользователя уже используется");
            else
                exception.setUsernameState(validator.checkString(newUsername, 100));
        }


        if (exception.isShouldThrow())
            throw exception;
    }
}
