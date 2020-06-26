package org.onap.ccsdk.apps.services;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.LinkedList;
import java.util.List;

@JsonRootName(value="errors")
public class RestErrors {
    List<RestError> errors;

    public RestErrors()
    {
        this.errors = new LinkedList<RestError>();
    }

    public RestErrors(RestError error) {
        this.errors = new LinkedList<RestError>();
        errors.add(error);
    }
    public void addError(RestError error) {
        errors.add(error);
    }

    public List<RestError> getErrors() {
        return errors;
    }
}
