package ch.zli.m223.punchclock.service;

import ch.zli.m223.punchclock.domain.ApplicationUser;
import ch.zli.m223.punchclock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository applicationUserRepository;

    @Autowired
    public UserService(UserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public ApplicationUser save(ApplicationUser user) {
        if (user.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID has to be null");
        }
        return this.applicationUserRepository.saveAndFlush(user);
    }

    public List<ApplicationUser> findAll() {
        return this.applicationUserRepository.findAll();
    }

    public void delete(Long id) {
        this.applicationUserRepository.deleteById(id);
    }

    public ApplicationUser loadApplicationUserByUsername(String username) {
        return this.applicationUserRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }

    public ApplicationUser whoAmI() {
        return loadApplicationUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public ApplicationUser update(ApplicationUser user) {
        if (!this.applicationUserRepository.existsById(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with ID " + user.getId() + " not found :(.");
        }
        return this.applicationUserRepository.saveAndFlush(user);
    }
}
