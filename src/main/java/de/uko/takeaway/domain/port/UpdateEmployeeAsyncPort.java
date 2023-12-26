package de.uko.takeaway.domain.port;

import de.uko.takeaway.domain.domain.Employee;

public interface UpdateEmployeeAsyncPort {
        void sendUpdateEmployeeMessage(Employee employee);
}
