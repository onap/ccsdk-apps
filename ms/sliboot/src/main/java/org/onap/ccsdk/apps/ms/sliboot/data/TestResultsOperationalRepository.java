package org.onap.ccsdk.apps.ms.sliboot.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestResultsOperationalRepository extends CrudRepository<TestResultOperational, Long> {

    List<TestResultOperational> findByTestIdentifier(String testIdentifier);


}
