package cinema.cinema_app.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
public class AuthFilter implements Filter {


    private static final String ADMIN_EMAIL = "admin@cinema.com";
    private static final String ADMIN_PASSWORD = "mypassword123";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        if (isPublicEndpoint(path)) {
            chain.doFilter(request, response);
            return;
        }
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            sendUnauthorized(httpResponse);
            return;
        }
        String credentials = new String(Base64.getDecoder().decode(authHeader.substring(6)));
        String[] parts = credentials.split(":");

        if (parts.length != 2) {
            sendUnauthorized(httpResponse);
            return;
        }
        String email = parts[0];
        String password = parts[1];
        if (ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password)) {
            chain.doFilter(request, response);
        } else {
            sendUnauthorized(httpResponse);
        }
    }

    private boolean isPublicEndpoint(String path) {
        return
                path
                        .matches("^/(all/movies|comingSoon/movies|buy/ticket|today/movies|cinema/movies/.*|movies/.*/cinema/.*|cinema/movies/by/type/.*/.*)$")
                        || path.startsWith("/swagger-ui")      // Swagger UI
                        || path.startsWith("/v3/api-docs")     // OpenAPI JSON
                        || path.startsWith("/swagger-resources") // Swagger resources
                        || path.equals("/swagger-ui.html")     // Swagger HTML
                        || path.equals("/favicon.ico");
    }
    private void sendUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
         response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Protected, invalid password or email.\"}");
    }
}