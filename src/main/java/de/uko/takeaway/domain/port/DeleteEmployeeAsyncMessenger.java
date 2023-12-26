package de.uko.takeaway.domain.port;

import java.util.UUID;

public interface DeleteEmployeeAsyncMessenger {
        void sendDeleteEmployeeMessage(UUID employeeId);
}
