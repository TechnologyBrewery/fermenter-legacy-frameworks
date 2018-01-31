package org.bitbucket.fermenter.stout.exceptions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.bitbucket.fermenter.stout.exception.ExceptionHandler;
import org.bitbucket.fermenter.stout.exception.RecoverableException;
import org.bitbucket.fermenter.stout.exception.UnrecoverableException;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.fail;

public class TestExceptions {

    private ExceptionHandler fermenterExceptionHandler = new ExceptionHandler();
    private Exception exception = new Exception();


    @Test
    @When("^given an exception of type \"([^\"]*)\"$")
    public void createExceptionOfType(String exceptionType){

        try {
            // Get the class type from the string
            Class<?> exceptionClass = Class.forName(exceptionType.trim());

            // Get the constructor of the given Exception class
            Constructor<?> exceptionConstruction = exceptionClass.getConstructor();

            exception = (Exception) exceptionConstruction.newInstance(new Object[]{});

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException exception) {

            // If any of the exceptions are caught, the reflection failed
            fail("Exception Handler test failed to find exception of type: " + exceptionType);
        }
    }


    @Test(expected = RecoverableException.class)
    @Then("the exception handler should throw a RecoverableException")
    public void testRecoverableException() {

        // Handle the given exception
        fermenterExceptionHandler.handleException(exception);
    }


    @Test(expected = UnrecoverableException.class)
    @Then("the exception handler should throw an UnrecoverableException")
    public void testUnrecoverableExceptions(){

        // Handle the given exception
        fermenterExceptionHandler.handleException(exception);
    }
}
