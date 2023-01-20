package de.com.bruns.sample.sampleapp.callscope;

import java.util.Optional;

import org.springframework.core.NamedThreadLocal;

public class CallScopeHolder {

	private static final ThreadLocal<CallScope> CALL_SCOPE_HOLDER = new NamedThreadLocal<>("Call Scope");

	public static CallScope startScope() {
		CallScope newScope = new CallScope();
		newScope.start();
		CALL_SCOPE_HOLDER.set(newScope);
		return newScope;
	}

	public static void setScope(CallScope scope) {
		scope.start();
		CALL_SCOPE_HOLDER.set(scope);
	}

	public static void clearScope() {
		CALL_SCOPE_HOLDER.get().stop();
		CALL_SCOPE_HOLDER.remove();
	}

	public static CallScope getScope() {
		return Optional.ofNullable(CALL_SCOPE_HOLDER.get()).orElseGet(CallScopeHolder::startScope);
	}
}
