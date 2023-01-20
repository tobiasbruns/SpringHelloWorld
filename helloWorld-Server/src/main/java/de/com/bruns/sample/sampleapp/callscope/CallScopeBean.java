package de.com.bruns.sample.sampleapp.callscope;

import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Scope(scopeName = CallScope.NAME, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CallScopeBean {

	@Getter
	private String id = UUID.randomUUID().toString();
}
