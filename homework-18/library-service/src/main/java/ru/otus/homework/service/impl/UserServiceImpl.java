package ru.otus.homework.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.User;
import ru.otus.homework.repository.UserRepository;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
            @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "10")
    })
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        sleepRandomly();


        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("Username not present");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User with username: %s not found", username)));

        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    private void sleepRandomly() {
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        if(randomNum == 3) {
            System.out.println("It is a chance for demonstrating Hystrix action");
            try {
                System.out.println("Start sleeping...." + System.currentTimeMillis());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Hystrix thread interupted...." + System.currentTimeMillis());
                e.printStackTrace();
            }
        }
    }
}
