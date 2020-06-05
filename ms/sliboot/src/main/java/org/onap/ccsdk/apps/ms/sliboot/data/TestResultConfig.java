package org.onap.ccsdk.apps.ms.sliboot.data;

import javax.persistence.*;

@Entity(name = "TEST_RESULT_CONFIG")
@Table(name = "TEST_RESULT_CONFIG")
public class TestResultConfig {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String testIdentifier;
    private String results;

    public TestResultConfig()
    {

    }
    public TestResultConfig(String testIdentifier, String results) {
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
