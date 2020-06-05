package org.onap.ccsdk.apps.ms.sliboot.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TestResultsConfigRepository extends CrudRepository<TestResultConfig, Long> {

    List<TestResultConfig> findByTestIdentifier(String testIdentifier);


}
