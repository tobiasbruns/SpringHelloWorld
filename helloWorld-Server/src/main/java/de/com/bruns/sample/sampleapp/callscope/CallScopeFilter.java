package de.com.bruns.sample.sampleapp.callscope;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

@Component
@Log
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
