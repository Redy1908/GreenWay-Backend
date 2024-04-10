package dev.redy1908.greenway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.redy1908.greenway.app.web.models.ErrorResponseDTO;
import dev.redy1908.greenway.delivery_man.domain.IDeliveryManService;
import dev.redy1908.greenway.delivery_man.domain.exceptions.models.DeliveryManAlreadyExistsException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DeliveryManFilter extends GenericFilterBean {

    private final IDeliveryManService deliveryManService;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            Jwt owner = (Jwt) authentication.getPrincipal();

            try {
                if (httpRequest.isUserInRole("GREEN_WAY_DELIVERY_MAN") && !httpRequest.isUserInRole("GREEN_WAY_ADMIN")) {
                    String username = owner.getClaim("preferred_username").toString();
                    deliveryManService.save(username);
                }
            }catch (DeliveryManAlreadyExistsException ex){

                ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                        "/",
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT,
                        "Deliveryman already exists, no further action required.",
                        LocalDateTime.now()
                );

                HttpServletResponse httpResponse = (HttpServletResponse) response;

                httpResponse.setContentType("application/json");
                httpResponse.setStatus(HttpServletResponse.SC_CONFLICT);

                httpResponse.getWriter().write(objectMapper.writeValueAsString(errorResponseDTO));
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
