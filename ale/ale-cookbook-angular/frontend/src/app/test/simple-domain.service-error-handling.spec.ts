// Http testing module and mocking controller
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

// Other imports
import { TestBed, inject } from '@angular/core/testing';
import { HttpErrorResponse } from '@angular/common/http';

import { SimpleDomain } from '../shared/model/simple-domain.model';
import { Constants } from '../constants';
import { FermenterResponse } from '../shared/model/fermenter-response.model';
import { GlobalErrorHandler } from '../shared/global-error-handler.service';
import { FermenterMessage } from '../shared/model/fermenter-message.model';
import { SimpleDomainMaintenanceService } from '../generated/service/maintenance/simple-domain-maintenance.service';
import { FermenterMessages } from '../shared/model/fermenter-messages.model';

describe('Ale Simple Domain Maintenance Service Error Handling', () => {
  let httpTestingController: HttpTestingController;
  const constants = new Constants();
  const simpleDomainMaintUrl =
    constants.STOUT_COOKBOOK_DOMAIN_END_POINT + '/SimpleDomain';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SimpleDomainMaintenanceService, GlobalErrorHandler]
    });

    // Inject the http service and test controller for each test
    httpTestingController = TestBed.get(HttpTestingController);
  });
  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be able to handle a 404 error', inject(
    [SimpleDomainMaintenanceService, GlobalErrorHandler],
    (
      simpleDomainService: SimpleDomainMaintenanceService,
      globalErrorHandler: GlobalErrorHandler
    ) => {
      const emsg = 'deliberate 404 error';
      const testId = 'Test Id';
      spyOn(console, 'error');

      const globalErrorHandlerSpy = spyOn(
        globalErrorHandler,
        'handleServiceCallError'
      ).and.callThrough();

      simpleDomainService.get(testId).subscribe(
        data => fail('should have failed with the 404 error'),
        (error: HttpErrorResponse) => {
          // if the service call fails it should call the global
          // error handler to show a message to the end user
          expect(globalErrorHandlerSpy).toHaveBeenCalled();

          // check that the global error handler logged the error
          expect(console.error).toHaveBeenCalled();

          // check the error gets rethrown back to the component
          expect(error.status).toEqual(404, 'status');
          expect(error.error).toEqual(emsg, 'message');
        }
      );

      const req = httpTestingController.expectOne(
        simpleDomainMaintUrl + '/' + testId
      );

      // Respond with mock error
      req.flush(emsg, { status: 404, statusText: 'Not Found' });
    }
  ));

  it('should be able to handle a 403 error', inject(
    [SimpleDomainMaintenanceService, GlobalErrorHandler],
    (
      simpleDomainService: SimpleDomainMaintenanceService,
      globalErrorHandler: GlobalErrorHandler
    ) => {
      const emsg = 'deliberate 403 error';
      const testId = 'Test Id';
      spyOn(console, 'error');

      const globalErrorHandlerSpy = spyOn(
        globalErrorHandler,
        'handleServiceCallError'
      ).and.callThrough();

      simpleDomainService.get(testId).subscribe(
        data => fail('should have failed with the 403 error'),
        (error: HttpErrorResponse) => {
          // if the service call fails it should call the global
          // error handler to show a message to the end user
          expect(globalErrorHandlerSpy).toHaveBeenCalled();

          // check that the global error handler logged the error
          expect(console.error).toHaveBeenCalled();

          // check the error gets rethrown back to the component
          expect(error.status).toEqual(403, 'status');
          expect(error.error).toEqual(emsg, 'message');
        }
      );

      const req = httpTestingController.expectOne(
        simpleDomainMaintUrl + '/' + testId
      );

      // Respond with mock error
      req.flush(emsg, { status: 403, statusText: 'Not Found' });
    }
  ));

  it('should be able to handle a network error', inject(
    [SimpleDomainMaintenanceService, GlobalErrorHandler],
    (
      simpleDomainService: SimpleDomainMaintenanceService,
      globalErrorHandler: GlobalErrorHandler
    ) => {
      const emsg = 'simulated network error';
      const testId = 'Test Id';
      spyOn(console, 'error');

      const globalErrorHandlerSpy = spyOn(
        globalErrorHandler,
        'handleServiceCallError'
      ).and.callThrough();

      simpleDomainService.get(testId).subscribe(
        data => fail('should have failed with the network error'),
        (error: HttpErrorResponse) => {
          // if the service call fails it should call the global
          // error handler to show a message to the end user
          expect(globalErrorHandlerSpy).toHaveBeenCalled();

          // check that the global error handler logged the error
          expect(console.error).toHaveBeenCalled();

          expect(error.error.message).toEqual(emsg, 'message');
        }
      );

      const req = httpTestingController.expectOne(
        simpleDomainMaintUrl + '/' + testId
      );

      // Create mock ErrorEvent, raised when something goes wrong at the network level.
      // Connection timeout, DNS error, offline, etc
      const mockError = new ErrorEvent('Network error', {
        message: emsg,
        // The rest of this is optional and not used.
        // Just showing that you could provide this too.
        filename: 'HeroService.ts',
        lineno: 42,
        colno: 21
      });

      // Respond with mock error
      req.error(mockError);
    }
  ));

  it('should be able to handle a fermenter ERROR response when trying to GET a simple domain', inject(
    [SimpleDomainMaintenanceService, GlobalErrorHandler],
    (
      simpleDomainService: SimpleDomainMaintenanceService,
      globalErrorHandler: GlobalErrorHandler
    ) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const testSimpleDomain = new SimpleDomain();
      testSimpleDomain.name = testName;
      testSimpleDomain.id = testId;

      spyOn(console, 'error');

      const globalErrorHandlerSpy = spyOn(
        globalErrorHandler,
        'handleServiceCallError'
      ).and.callThrough();

      simpleDomainService.get(testId).subscribe(
        data => fail('it should throw an error'),
        err => {
          expect(globalErrorHandlerSpy).toHaveBeenCalled();
          expect(console.error).toHaveBeenCalled();
        }
      );

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        simpleDomainMaintUrl + '/' + testId
      );

      // Assert that the request is a GET.
      expect(req.request.method).toEqual('GET');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<SimpleDomain>();
      mockResponse.value = testSimpleDomain;
      mockResponse.messages = new FermenterMessages();
      const mockMessage = new FermenterMessage();
      mockMessage.name = 'something.invalid';
      mockMessage.severity = 'ERROR';
      mockResponse.messages.messages.push(mockMessage);

      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

  it('should be able to SKIP a fermenter ERROR response when trying to PUT a simple domain', inject(
    [SimpleDomainMaintenanceService, GlobalErrorHandler],
    (
      simpleDomainService: SimpleDomainMaintenanceService,
      globalErrorHandler: GlobalErrorHandler
    ) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const testSimpleDomain = new SimpleDomain();
      testSimpleDomain.name = testName;
      testSimpleDomain.id = testId;

      spyOn(console, 'error');

      const globalErrorHandlerSpy = spyOn(
        globalErrorHandler,
        'handleServiceCallError'
      ).and.callThrough();

      simpleDomainService.put(testId, testSimpleDomain, true).subscribe(
        fermenterResponse => {
          expect(globalErrorHandlerSpy).not.toHaveBeenCalled();
          expect(console.error).not.toHaveBeenCalled();
          expect(fermenterResponse.messages.hasErrorMessages()).toBeTruthy();
          expect(fermenterResponse.value).toBeFalsy();
        },
        err => fail('it should throw an error')
      );

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        simpleDomainMaintUrl + '/' + testId
      );

      // Assert that the request is a GET.
      expect(req.request.method).toEqual('PUT');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<SimpleDomain>();
      // there was an error so don't return a valid value
      mockResponse.value = undefined;
      mockResponse.messages = new FermenterMessages();
      const mockMessage = new FermenterMessage();
      mockMessage.name = 'something.invalid';
      mockMessage.severity = 'ERROR';
      mockResponse.messages.messages.push(mockMessage);

      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));

  it('should be able to handle a fermenter INFO message in the response when trying to GET a simple domain', inject(
    [SimpleDomainMaintenanceService, GlobalErrorHandler],
    (
      simpleDomainService: SimpleDomainMaintenanceService,
      globalErrorHandler: GlobalErrorHandler
    ) => {
      const testName = 'Test Name';
      const testId = 'Test Id';
      const testSimpleDomain = new SimpleDomain();
      testSimpleDomain.name = testName;
      testSimpleDomain.id = testId;

      spyOn(console, 'error');

      const globalErrorHandlerSpy = spyOn(
        globalErrorHandler,
        'handleServiceCallError'
      ).and.callThrough();

      simpleDomainService.get(testId).subscribe(
        (simpleDomainResponse: SimpleDomain) => {
          expect(globalErrorHandlerSpy).not.toHaveBeenCalled();
          expect(console.error).not.toHaveBeenCalled();
          expect(simpleDomainResponse.id).toEqual(testId);
        },
        (error: any) => {
          fail('it should NOT throw an error because the fermenter message was only info');
        }
      );

      // The following `expectOne()` will match the request's URL.
      // If no requests or multiple requests matched that URL
      // `expectOne()` would throw.
      const req = httpTestingController.expectOne(
        simpleDomainMaintUrl + '/' + testId
      );

      // Assert that the request is a GET.
      expect(req.request.method).toEqual('GET');

      // Respond with mock data, causing Observable to resolve.
      // Subscribe callback asserts that correct data was returned.
      const mockResponse = new FermenterResponse<SimpleDomain>();
      mockResponse.value = testSimpleDomain;
      mockResponse.messages = new FermenterMessages();
      const mockMessage = new FermenterMessage();
      mockMessage.name = 'something.INFORMATIONAL';
      mockMessage.severity = 'INFO';
      mockResponse.messages.messages = [mockMessage];

      req.flush(mockResponse);

      // Finally, assert that there are no outstanding requests.
      httpTestingController.verify();
    }
  ));
});
