    package cinema.cinema_app.config;

    import cinema.cinema_app.filter.AuthFilter;
    import org.springframework.boot.web.servlet.FilterRegistrationBean;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class FilterConfig {

        @Bean
        public FilterRegistrationBean<AuthFilter> authFilterRegistration(AuthFilter authFilter) {
            FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(authFilter);
            registrationBean.addUrlPatterns("/*");
            registrationBean.setOrder(1);
            return registrationBean;
        }
    }