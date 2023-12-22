package de.uko.takeaway.persistence.messaging;

import de.uko.takeaway.persistence.event.EmployeeEvent;

public interface MessagingGateway {

    void send(EmployeeEvent payload);

}
