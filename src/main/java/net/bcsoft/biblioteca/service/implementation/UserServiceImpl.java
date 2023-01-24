package net.bcsoft.biblioteca.service.implementation;

import lombok.AllArgsConstructor;
import net.bcsoft.biblioteca.exception.BadRequestException;
import net.bcsoft.biblioteca.exception.NotFoundException;
import net.bcsoft.biblioteca.model.User;
import net.bcsoft.biblioteca.repository.UserRepository;
import net.bcsoft.biblioteca.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {

private final UserRepository userRepository;

    @Override
    public void registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("User with email already exists");
        }
        userRepository.save(user);
    }

    // Il metodo of() della classe Optional in Java crea un nuovo oggetto Optional che contiene un valore specifico.
    // Se il valore passato come argomento è null, verrà lanciata un'eccezione NullPointerException.
    @Override
    public List<User> getAllUsers() {
        return Optional.of(userRepository.findAll())
                .orElseThrow(() -> new NotFoundException("Users not found"));
    }
}
