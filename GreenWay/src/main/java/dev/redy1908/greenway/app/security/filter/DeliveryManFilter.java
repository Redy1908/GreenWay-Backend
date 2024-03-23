package dev.redy1908.greenway.app.security.filter;

import dev.redy1908.greenway.delivery_man.domain.IDeliveryManService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DeliveryManFilter extends GenericFilterBean {

    private final IDeliveryManService deliveryManService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            Jwt owner = (Jwt) authentication.getPrincipal();

            if (httpRequest.isUserInRole("GREEN_WAY_DELIVERY_MAN") && !httpRequest.isUserInRole("GREEN_WAY_ADMIN")) {
                String username = owner.getClaim("preferred_username").toString();
                deliveryManService.save(username);
            }
        }

        chain.doFilter(request, response);
    }
}
