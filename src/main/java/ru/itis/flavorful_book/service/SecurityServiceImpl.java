package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.exception.AuthenticationException;
import ru.itis.flavorful_book.exception.IllegalUserArgumentException;
import ru.itis.flavorful_book.entity.Session;
import ru.itis.flavorful_book.entity.User;
import ru.itis.flavorful_book.repository.SessionRepository;
import ru.itis.flavorful_book.repository.UserRepository;
import ru.itis.flavorful_book.util.validation.Validator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    private final Base64.Encoder base64Encoder;

    private final Duration sessionDuration;

    private final Validator validator;

    public SecurityServiceImpl(UserRepository userRepository, SessionRepository sessionRepository, Validator validator) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.base64Encoder = Base64.getEncoder();
        this.sessionDuration = Duration.ofMinutes(30);
        this.validator = validator;
    }

    @Override
    public String signupUser(String username, String email, String password, String passwordRepeat) {
        validateUserData(username, email, password, passwordRepeat);

        String salt = UUID.randomUUID().toString();
        String saltedPassword = password + salt;
        String passwordHash = getPasswordHash(saltedPassword);

        User user = new User(null, username, email, passwordHash, salt, null, LocalDateTime.now());
        Long userId = userRepository.save(user).getId();
        String sessionId = UUID.randomUUID().toString();

        sessionRepository.save(new Session(
                sessionId,
                userId,
                LocalDateTime.now().plus(sessionDuration)
        ));

        return sessionId;
    }

    @Override
    public String loginUser(String email, String password) {
        User user;
        try {
            user = userRepository.findByEmail(email);
        } catch (RuntimeException e) {
            throw new AuthenticationException("Пользователя с таким email нет");
        }

        String salt = user.getSalt();
        String saltedPassword = password + salt;
        String passwordHash = getPasswordHash(saltedPassword);

        if (!passwordHash.equals(user.getPassword())) {
            throw new AuthenticationException("Неверный пароль");
        }

        String sessionId = UUID.randomUUID().toString();
        if (!sessionRepository.save(new Session(
                sessionId,
                user.getId(),
                LocalDateTime.now().plus(sessionDuration)
        )))
            throw new RuntimeException("Не получилось сохранить сессию");

        return sessionId;
    }

    @Override
    public User getUser(String sessionId) {
        Session session;
        try {
            session = sessionRepository.findById(sessionId);
        } catch (RuntimeException e) {
            throw new AuthenticationException("Сессия не найдена");
        }
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Время сессии истекло");
        }
        return userRepository.findById(session.getUserId());
    }

    @Override
    public boolean logoutUser(String sessionId) {
        return sessionRepository.deleteBySessionId(sessionId);
    }

    private String getPasswordHash(String saltedPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordHashBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return base64Encoder.encodeToString(passwordHashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void validateUserData(String username, String email, String password, String passwordRepeat) {
        IllegalUserArgumentException exception = new IllegalUserArgumentException("Ошибка при сохранении данных");

        if (password == null || !password.equals(passwordRepeat))
            exception.setPasswordState("Пароль не совпадает");
        else
            exception.setPasswordState(validator.checkString(password, 4, 64));

        if (username == null || userRepository.existsByUsername(username))
            exception.setUsernameState("Это имя пользователя уже используется");
        else
            exception.setUsernameState(validator.checkString(username, 100));

        if (email == null || userRepository.existsByEmail(email))
            exception.setEmailState("Пользователь с такой электронной почтой уже существует");
        else
            exception.setEmailState(validator.checkString(email, 255));

        if (exception.isShouldThrow())
            throw exception;
    }
}
