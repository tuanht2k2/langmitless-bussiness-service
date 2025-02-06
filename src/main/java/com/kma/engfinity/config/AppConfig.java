package com.kma.engfinity.config;

import com.kma.common.entity.Account;
import com.kma.engfinity.service.AuthService;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AppConfig {
    @Bean
    public ModelMapper modelMapper () {
        return new ModelMapper();
    }

    @Bean
    public AuditorAware<Account> auditorAware(AuthService authService) {
        return new AuditorAwareImpl(authService);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static class AuditorAwareImpl implements AuditorAware<Account> {

        private final AuthService authService;

        public AuditorAwareImpl(AuthService authService) {
            this.authService = authService;
        }

        @NotNull
        @Override
        public Optional<Account> getCurrentAuditor() {
            Account currentAccount = authService.getCurrentAccount();
            return Optional.ofNullable(currentAccount);
        }
    }

}
