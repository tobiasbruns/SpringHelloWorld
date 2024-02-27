package de.com.bruns.sample.sampleapp;

import org.springframework.stereotype.Component;

import io.micrometer.observation.Observation;
import io.micrometer.observation.Observation.Context;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.java.Log;

@Component
@Log
public class HelloWorldObservationHandler implements ObservationHandler<Observation.Context> {

	public void onStart(Observation.Context context) {
		LOG.info("Observation started");
	}

	public void onStop(Observation.Context context) {
		LOG.info("Observation ended");
	}

	public void onError(Observation.Context context) {
		var t = context.getError();
		LOG.info("T: " + t.getLocalizedMessage());
	}

	@Override
	public boolean supportsContext(Context context) {
		// TODO Auto-generated method stub
		return true;
	}
}
