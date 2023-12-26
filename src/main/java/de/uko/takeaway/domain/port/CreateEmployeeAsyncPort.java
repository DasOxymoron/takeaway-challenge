package de.uko.takeaway.domain.port;

import de.uko.takeaway.domain.domain.Employee;

public interface CreateEmployeeAsyncPort {
        void sendCreateEmployeeMessage(Employee employee);
}
