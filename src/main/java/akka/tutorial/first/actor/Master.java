package akka.tutorial.first.actor;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
import akka.turorial.first.message.Calculate;
import akka.turorial.first.message.PiApproximation;
import akka.turorial.first.message.Result;
import akka.turorial.first.message.Work;
import akka.util.Duration;

public class Master extends UntypedActor {
	private final int nrOfMessages;
	private final int nrOfElements;

	private double pi;
	private int nrOfResults;
	private final long start = System.currentTimeMillis();

	private final ActorRef listener;
	private final ActorRef workerRouter;

	public Master(final int nrOfWorkers, int nrOfMessages, int nrOfElements,
			ActorRef listener) {
		this.nrOfMessages = nrOfMessages;
		this.nrOfElements = nrOfElements;
		this.listener = listener;

		workerRouter = this.getContext().actorOf(
				new Props(Worker.class).withRouter(new RoundRobinRouter(
						nrOfWorkers)), "workerRouter");
	}

	public void onReceive(Object message) {
		if (message instanceof Calculate) {
			for (int start = 0; start < nrOfMessages; start++) {
				workerRouter.tell(new Work(start, nrOfElements), getSelf());
			}
		} else if (message instanceof Result) {
			Result result = (Result) message;
			pi += result.getValue();
			nrOfResults += 1;
			if (nrOfResults == nrOfMessages) {
				// Send the result to the listener
				Duration duration = Duration.create(System.currentTimeMillis()
						- start, TimeUnit.MILLISECONDS);
				listener.tell(new PiApproximation(pi, duration), getSelf());
				// Stops this actor and all its supervised children
				getContext().stop(getSelf());
			}
		} else {
			unhandled(message);
		}
	}
}