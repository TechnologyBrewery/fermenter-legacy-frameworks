package org.bitbucket.fermenter.mda.element;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.bitbucket.fermenter.mda.util.MessageTracker;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MessageTrackerSteps {

    private MessageTracker messageTracker = MessageTracker.getInstance();
    
    @Before
    public void clearMessageTracker() {
        messageTracker.clear();
    }
    
    @Given("^multiple error messages \"([^\"]*)\"$")
    public void multiple_error_messages(List<String> errors) throws Throwable {
        for (String error : errors) {
            messageTracker.addErrorMessage(error);
        }
    }

    @When("^the message tracker is asked for messages$")
    public void the_message_tracker_is_asked_for_messages() throws Throwable {
    }

    @Then("^the tracker reports that errors were encountered$")
    public void the_tracker_reports_that_errors_were_encountered() throws Throwable {
        assertTrue("Expected errors to have been tracked!", messageTracker.hasErrors());
    }

    @Given("^multiple warning messages \"([^\"]*)\"$")
    public void multiple_warning_messages(List<String> warnings) throws Throwable {
        for (String warning : warnings) {
            messageTracker.addWarningMessage(warning);
        }
    }

    @Then("^the tracker reports that no errors were encountered$")
    public void the_tracker_reports_that_no_errors_were_encountered() throws Throwable {
        assertFalse("Expected NO errors to have been tracked!", messageTracker.hasErrors());
    }

}
