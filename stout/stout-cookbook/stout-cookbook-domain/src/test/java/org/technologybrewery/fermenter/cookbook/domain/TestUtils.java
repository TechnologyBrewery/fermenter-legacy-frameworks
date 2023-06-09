package org.technologybrewery.fermenter.cookbook.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainBO;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainChildBO;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.SimpleDomainEagerChildBO;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.ValidationExampleBO;
import org.technologybrewery.fermenter.cookbook.domain.bizobj.ValidationExampleChildBO;
import org.technologybrewery.fermenter.cookbook.domain.enumeration.SimpleDomainEnumeration;
import org.technologybrewery.fermenter.cookbook.domain.enumeration.ValuedEnumerationExample;
import org.technologybrewery.fermenter.stout.messages.Message;
import org.technologybrewery.fermenter.stout.messages.Messages;
import org.technologybrewery.fermenter.stout.service.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains common integration test logic for this project.
 */
public final class TestUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

	private TestUtils() {
		// prevent instantiation
	}

    public static List<SimpleDomainBO> createAndPersistRandomSimpleDomains(int numEntitiesToCreate) {
        List<SimpleDomainBO> simpleDomains = new ArrayList<SimpleDomainBO>(numEntitiesToCreate);
        for (int iter = 0; iter < numEntitiesToCreate; iter++) {
            SimpleDomainBO newInstance = createRandomSimpleDomain().save();
            simpleDomains.add(newInstance);
        }
        return simpleDomains;
    }

	public static SimpleDomainBO createRandomSimpleDomain() {
		SimpleDomainBO domain = new SimpleDomainBO();
		domain.setName(RandomStringUtils.randomAlphanumeric(25));
		domain.setTheDate1(new Date());
		domain.setTheLong1(RandomUtils.nextLong(0L, 10000L));
		domain.setType(RandomStringUtils.random(5));
		domain.setAnEnumeratedValue(
				SimpleDomainEnumeration.values()[RandomUtils.nextInt(0, SimpleDomainEnumeration.values().length)]);
		domain.setAnotherEnumeratedValue(ValuedEnumerationExample.values()[RandomUtils.nextInt(0, SimpleDomainEnumeration.values().length)]);
		domain.setStandardBoolean(false);
		domain.setNumericBoolean(true);
		return domain;
	}

	public static SimpleDomainBO createRandomSimpleDomain(double bigDecimalAttributeValue) {
		SimpleDomainBO simpleDomain = createRandomSimpleDomain();
		simpleDomain.setBigDecimalValue(BigDecimal.valueOf(bigDecimalAttributeValue));
		return simpleDomain;
	}

	public static SimpleDomainBO createRandomSimpleDomain(int numChildEntities) {
		SimpleDomainBO domain = createRandomSimpleDomain();
		for (int iter = 0; iter < numChildEntities; iter++) {
			SimpleDomainChildBO lazyChild = new SimpleDomainChildBO();
			lazyChild.setName(RandomStringUtils.randomAlphabetic(10));
			domain.addSimpleDomainChild(lazyChild);
			
			SimpleDomainEagerChildBO eagerChild = new SimpleDomainEagerChildBO();
			lazyChild.setName(RandomStringUtils.randomAlphabetic(10));
			domain.addSimpleDomainEagerChild(eagerChild);
		}
		return domain;
	}

	public static ValidationExampleBO createRandomValidationExample() {
		ValidationExampleBO domain = new ValidationExampleBO();
		domain.setRequiredField(RandomStringUtils.randomAlphabetic(20));
		return domain;
	}

	public static ValidationExampleChildBO createRandomValidationExampleChild() {
		ValidationExampleChildBO domain = new ValidationExampleChildBO();
		domain.setRequiredField(RandomStringUtils.randomAlphabetic(20));
		return domain;
	}

	public static void assertNoErrorMessages(ServiceResponse response) {
		if (response != null) {
			Messages messages = response.getMessages();
			boolean hasErrorMessages = messages.hasErrors();
			if (hasErrorMessages) {
				for (Message message : messages.getErrors()) {
					LOGGER.error(message.getDisplayText());
				}
			}
			assertFalse(hasErrorMessages);
		}
	}

	public static void assertErrorMessagesInResponse(ServiceResponse response, int expectedNumErrorMessages) {
		assertNotNull("Service response wrapper was unexpectedly null", response);
		Messages messages = response.getMessages();
		assertNotNull("Messages object on service response wrapper was unexpected null", messages);
		assertEquals("An unexpected number of error messages were found", expectedNumErrorMessages,
				messages.getErrorCount());
	}
}
