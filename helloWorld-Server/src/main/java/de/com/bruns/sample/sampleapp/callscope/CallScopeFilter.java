package de.com.bruns.sample.sampleapp.callscope;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
class CallScopeFilter extends OncePerRequestFilter {

	@Autowired
	private CallScopeBean scopeBean;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		CallScopeHolder.startScope();
		try {
			LOG.info("start request: " + scopeBean.getId());
			filterChain.doFilter(request, response);
		} finally {
			CallScopeHolder.clearScope();
		}

	}

}
