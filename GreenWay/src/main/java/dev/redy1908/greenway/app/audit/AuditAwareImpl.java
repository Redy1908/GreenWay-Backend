package dev.redy1908.greenway.app.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Nonnull
    @Override
    public Optional<String> getCurrentAuditor() {
        Jwt owner = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.of(owner.getClaims().get("preferred_username").toString());
    }
}