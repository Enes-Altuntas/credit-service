package org.colendi.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class MdcFilter implements Filter {

  public static final String UUID_KEY = "UUID";

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

    String uuid = httpServletRequest.getHeader(UUID_KEY);

    if (uuid == null || uuid.isEmpty()) {
      uuid = UUID.randomUUID().toString();
    }

    MDC.put(UUID_KEY, uuid);
    httpServletResponse.setHeader(UUID_KEY, uuid);

    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      MDC.remove(UUID_KEY);
    }
  }
}
