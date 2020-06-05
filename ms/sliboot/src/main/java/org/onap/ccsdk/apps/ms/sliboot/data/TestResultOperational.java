package org.onap.ccsdk.apps.ms.sliboot.data;

import javax.persistence.*;

@Entity(name="TEST_RESULT_OPERATIONAL")
@Table(name="TEST_RESULT_OPERATIONAL")
public class TestResultOperational {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String testIdentifier;
    private String results;

    public TestResultOperational()
    {

    }

    public TestResultOperational(String testIdentifier, String results) {
        this.testIdentifier = testIdentifier;
        this.results = results;
    }

    public String getTestIdentifier() {
        return testIdentifier;
    }

    public void setTestIdentifier(String testIdentifier) {
        this.testIdentifier = testIdentifier;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }




}
