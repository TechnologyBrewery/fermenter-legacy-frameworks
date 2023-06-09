import { BrowserModule, DomSanitizer } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SimpleDomainComponent } from './simple-domain/simple-domain.component';
import { MatIconRegistry } from '@angular/material/icon';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { GlobalErrorHandler } from './shared/global-error-handler.service';
import { SimpleDomainMaintenanceService } from './generated/service/maintenance/simple-domain-maintenance.service';
import { GlobalErrorHandlerComponent } from './shared/global-error-handler/global-error-handler.component';
import { ErrorDialogComponent } from './shared/error-dialog/error-dialog.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { E2eTestsPageComponent } from './e2e-tests-page/e2e-tests-page.component';
import { BeerExampleComponent } from './beer-example-page/beer-example-page.component';
import { E2eTestRunnerComponent } from './e2e-tests-page/e2e-test-runner/e2e-test-runner.component';
import { BeerExampleEntityBeerTypeFormComponent } from './generated/ui/beer-example-entity/beer-type/form/beer-example-entity-beer-type-form.component';
import { SimpleDomainTheLong1FormComponent } from './generated/ui/simple-domain/the-long1/form/simple-domain-the-long1-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SimpleDomainTheDate1FormComponent } from './generated/ui/simple-domain/the-date1/form/simple-domain-the-date1-form.component';
@NgModule({
  declarations: [
    AppComponent,
    SimpleDomainComponent,
    GlobalErrorHandlerComponent,
    ErrorDialogComponent,
    E2eTestsPageComponent,
    BeerExampleComponent,
    E2eTestRunnerComponent,
    BeerExampleEntityBeerTypeFormComponent,
    SimpleDomainTheLong1FormComponent,
    SimpleDomainTheDate1FormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    HttpClientModule,
    FlexLayoutModule,
    ReactiveFormsModule
  ],
  providers: [SimpleDomainMaintenanceService, GlobalErrorHandler],
  bootstrap: [AppComponent],
  entryComponents: [ErrorDialogComponent]
})
export class AppModule {
  constructor(matIconRegistry: MatIconRegistry, domSanitizer: DomSanitizer) {
    matIconRegistry.addSvgIconSet(domSanitizer.bypassSecurityTrustResourceUrl('./assets/mdi.svg'));
    // Note: for the error icons to appear, developers will need to add the following
    matIconRegistry.addSvgIcon('error', domSanitizer.bypassSecurityTrustResourceUrl('./assets/error-icon.svg'));
  }
}
// TODO: https://alligator.io/angular/providers-shared-modules/
