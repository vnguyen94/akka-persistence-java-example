package no.jansoren.akka.persistence.eventsourcing;

import akka.japi.pf.ReceiveBuilder;
import akka.japi.pf.UnitPFBuilder;
import akka.persistence.AbstractPersistentActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public abstract class EventSourcedPersistenceActor extends AbstractPersistentActor {

    private static final Logger LOG = LoggerFactory.getLogger(EventSourcedPersistenceActor.class);

    private String persistenceId;

    public EventSourcedPersistenceActor(String persistenceId) {
        this.persistenceId = persistenceId;
    }

    @Override
    public String persistenceId() {
        return persistenceId;
    }

    @Override
    public Receive createReceiveRecover() {
        LOG.debug("receiveRecover");
        ReceiveBuilder defaultMatches = getDefaultMatches();
        return buildReceiver(defaultMatches);
    }

    @Override
    public Receive createReceive() {
        LOG.debug("receiveCommand");
        ReceiveBuilder defaultMatches = getDefaultMatches();
        return buildReceiver(defaultMatches);
    }

    protected abstract Receive buildReceiver(ReceiveBuilder defaultMatches);

    private ReceiveBuilder getDefaultMatches() {
        return receiveBuilder()
                .match(IsRunning.class, this::handleCommand)
                .match(Shutdown.class, this::handleCommand);
    }

    private void handleCommand(IsRunning command) {
        LOG.info("Checking if actor system is running");
        sender().tell(new Yes(), self());
    }

    private void handleCommand(Shutdown command) {
        LOG.info("Shutting down actor system");
        context().stop(self());
    }
}
