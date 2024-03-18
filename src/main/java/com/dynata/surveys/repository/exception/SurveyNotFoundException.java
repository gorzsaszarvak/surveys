package com.dynata.surveys.repository.exception;

public class SurveyNotFoundException extends RuntimeException {
    public SurveyNotFoundException(Long surveyId) {
        super("Survey not found with id: " + surveyId);
    }
}
