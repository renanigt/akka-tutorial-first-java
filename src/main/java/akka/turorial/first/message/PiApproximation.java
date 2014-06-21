package akka.turorial.first.message;

import akka.util.Duration;

public class PiApproximation {
	private final double pi;
	private final Duration duration;

	public PiApproximation(double pi, Duration duration) {
		this.pi = pi;
		this.duration = duration;
	}

	public double getPi() {
		return pi;
	}

	public Duration getDuration() {
		return duration;
	}
}