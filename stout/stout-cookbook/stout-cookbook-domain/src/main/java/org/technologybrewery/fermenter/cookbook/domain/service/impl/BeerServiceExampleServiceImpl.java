package org.technologybrewery.fermenter.cookbook.domain.service.impl;

import java.util.Random;

import org.technologybrewery.fermenter.cookbook.domain.bizobj.BeerExampleEntityBO;
import org.technologybrewery.fermenter.cookbook.domain.service.rest.BeerServiceExampleService;
import org.springframework.stereotype.Service;

/**
 * Service implementation class for the BeerServiceExample service.
 * @see org.technologybrewery.fermenter.cookbook.domain.service.rest.BeerServiceExampleService
 *
 * GENERATED STUB CODE - PLEASE *DO* MODIFY
 * Genereated from service.impl.java.vm
 */
@Service
public class BeerServiceExampleServiceImpl extends BeerServiceExampleBaseServiceImpl implements BeerServiceExampleService {

	private static final String any = "ANY";
	private static final String[] defaultTypes = {"ipa", "pilsner", "amber", "nitro", "light", "imperial stout"};
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BeerExampleEntityBO brewMeABeerImpl(String type) {
		BeerExampleEntityBO exampleBeer = new BeerExampleEntityBO();
		if (any.contentEquals(type)) {
			Random random = new Random();
			int index = random.nextInt(defaultTypes.length);
			exampleBeer.setBeerType(defaultTypes[index]);
		}
		else {
			exampleBeer.setBeerType(type);
		}
		exampleBeer.save();
		return exampleBeer;
	}

}
