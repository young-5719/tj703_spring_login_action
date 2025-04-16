package com.tj703.l09_spring_login.service;

import com.tj703.l09_spring_login.entity.User;
import com.tj703.l09_spring_login.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> login(String id, String pw) {
        return userRepository.findByIdAndPw(id, pw);
    }
    // 진영 , 1234
    // 진영 , $2a$10$5vSedueg3Pi6cvgg.jKG4OhizEkkgIgq7bL9IZ7KKFl7H4/93tFG2
    @Override
    public boolean loginHash(User user) {
        Optional<User> userOpt = userRepository.findById(user.getId()); // 찾아서
        if(userOpt.isPresent()) {
            User loginUser = userOpt.get();
            return BCrypt.checkpw(user.getPw(), loginUser.getPw()); // 평문 비밀번호와 해시비밀번호 비교

        }
        return false;
    }
}
