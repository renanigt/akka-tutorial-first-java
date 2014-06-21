package akka.tutorial.first.java;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.turorial.first.message.Calculate;
import akka.tutorial.first.actor.Listener;
import akka.tutorial.first.actor.Master;

public class Pi {

	public static void main(String[] args) {
		Pi pi = new Pi();
		pi.calculate(4, 10000, 10000);
	}

	public void calculate(final int nrOfWorkers, final int nrOfElements,
			final int nrOfMessages) {
		// Create an Akka system
		ActorSystem system = ActorSystem.create("PiSystem");

		// create the result listener, which will print the result and shutdown
		// the system
		final ActorRef listener = system.actorOf(new Props(Listener.class),
				"listener");

		// create the master
		ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new Master(nrOfWorkers, nrOfMessages, nrOfElements,
						listener);
			}
		}), "master");

		// start the calculation
		master.tell(new Calculate());

	}
}