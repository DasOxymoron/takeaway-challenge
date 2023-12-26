package de.uko.takeaway.domain.port;

import java.util.UUID;

public interface DeleteEmployeeAsyncPort {
        void sendDeleteEmployeeMessage(UUID employeeId);
}
