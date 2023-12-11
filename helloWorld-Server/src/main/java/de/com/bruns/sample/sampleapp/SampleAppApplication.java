package de.com.bruns.sample.sampleapp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.filter.ForwardedHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.com.bruns.sample.sampleapp.callscope.CallScope;
import de.com.bruns.sample.sampleapp.callscope.CallScopeHolder;
import de.com.bruns.sample.sampleapp.callscope.ScopeAwareAsyncTaskExecutor;

@SpringBootApplication
@EnableAsync
public class SampleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleAppApplication.class, args);
	}

	@Bean
	FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
		FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new ForwardedHeaderFilter());
		return bean;
	}

	@Bean
	@Primary
	ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		return builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
	}

	@Bean
	ScopeAwareAsyncTaskExecutor scopeAwareExecutor() {
		return new ScopeAwareAsyncTaskExecutor(taskExecutor());
	}

	@Bean
	static BeanFactoryPostProcessor beanFactoryPostProcessor() {
		return new BeanFactoryPostProcessor() {

			@Override
			public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
				beanFactory.registerScope(CallScope.NAME, CallScopeHolder.getScope());
			}
		};
	}

	private AsyncTaskExecutor taskExecutor() {
		var scheduler = new ThreadPoolTaskScheduler();
		scheduler.initialize();
		return scheduler;
	}
}
