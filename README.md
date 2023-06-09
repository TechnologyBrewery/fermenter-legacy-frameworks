# Fermenter #
[![Maven Central](https://img.shields.io/maven-central/v/org.technologybrewery.fermenter/root.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.technologybrewery.fermenter%22%20AND%20a%3A%22root%22)
[![License](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/mit)

In brewing, a fermenter is a vessel in which unfinished ingredients become nearly finished beer. In Model Driven Architecture, Fermenter is a project that converts functional concepts into nearly finished applications. This approach allows for the quick definition and assembly of applications with the focus on functional concepts rather than technical underpinnings.

## Approach ##
The Fermenter approach is simple - it is solely intended to generate source code based on simple, developer targeted model. This is the key difference between Fermenter and other MDA tools and approaches is that it puts a premium on developer productivity rather than architectural concepts or diagrams.

![fermenter-high-level-concept.png](https://bitbucket.org/repo/rg8odx/images/2347847741-fermenter-high-level-concept.png)

### Step 1 - Define Your Model & Select a Framework ###
To get started, you define the model elements you need and select a framework to use. The model describes what business concepts you need in your application. Fermenter can model entities, services, composites, or enumerations. The framework can be anything you'd like (e.g., JEE, Spring, something home grown or legacy).

### Step 2 - Run Fermenter ###
Add our Maven plugin to your build and run your build like you normally do.

### Step 3 - Update Generated Source with Business Logic ###
Fermenter will generate source into the folder structure appropriate for the type of project being generated (i.e.  `src/generated/<appropriate sub-folder>` for Java-based projects), representing the concepts and framework you have configured.  Update modifiable stubs with business logic, which are similarly generated into the appropriate location (i.e. `src/main/<appropriate sub-folder>` for Java-based projects) and you're done!

## Stout ##
Fermenter's default framework, called `stout`, blends best practices from Spring, Jackson, JPA, and other libraries to construct cohesive Java Servlet compatible web applications.  The resulting generated source uses object oriented concepts to stub out locations for business logic and provides key extension points to modify default functionality where necessary.  Please see the `stout-cookbook-domain` module for examples of all the model concepts in action.  Additionally, you can use the `integration-test` Maven profile to automatically download and execute the examples in a live Tomcat container.

## Ale ##

Ale is a Fermenter framework that makes it easier for developers to utilize [Angular](https://angular.io/) best practices and leverage a consistent frontend base layer. Generated base layer support includes entity models, services, and some basic UI components which allows for less time to be spent writing scaffolding and more time to be spent on complex high-value visualization areas.  See the `ale-cookbook-angular` module for a demonstrative example of how Ale may be used.

## Brett ##

Contrary to how Brettanomyces (i.e. "Brett") is a yeast that can yield unpredictable results when brewing, the `brett` Fermenter framework helps developers institute enterprise development and configuration management best practices to the often unpredictable world of Python development.  `brett` facilitates the generation of Python projects that align with [Habushu](https://github.com/TechnologyBrewery/habushu/) standards.  Due to the Habushu's usage of Python build tools that currently must be manually installed, Fermenter developers must opt-in to build `brett` modules by using  `with-python-support` Maven profile (i.e. `mvn clean install -Pwith-python-support`). See the `brett-cookbook` for an example of how `brett` combines Fermenter-driven Python module generation with an automated DevOps lifecycle managed through Habushu.

# Distribution Channel

Want Fermenter in your project? As demonstrated in the `stout-cookbook-domain` project, add the following Maven plugin declaration and dependency to your project from Maven Central: 

```xml
<properties>
	<fermenter.version>2.5.0</fermenter.version>
</properties>
<build>
	<plugins>
		<plugin>
			<groupId>org.technologybrewery.fermenter</groupId>
			<artifactId>fermenter-mda</artifactId>
			<version>${fermenter.version}</version>
			<configuration>
				<basePackage>com.your.domain.model</basePackage>
				<profile>your-domain-model</profile>
			</configuration>
			<dependencies>
				<dependency>
					<groupId>com.your.domain.model</groupId>
					<artifactId>your-fermenter-profile-name</artifactId>						
					<version>${project.version}</version>
				</dependency>		
			</dependencies>
		</plugin>
	</plugins>
</build>
...
<dependency>
    <groupId>org.technologybrewery.fermenter</groupId>
    <artifactId>stout-spring</artifactId>
    <version>${fermenter.version}</version>
</dependency>
```

## Releasing to Maven Central Repository

Fermenter uses both the `maven-release-plugin` and the `nexus-staging-maven-plugin` to facilitate the release and deployment of new Fermenter builds. In order to perform a release, you must:

1. Obtain a [JIRA](https://issues.sonatype.org/secure/Dashboard.jspa) account with Sonatype OSSRH and access to the `org.technologybrewery` project group

2. Ensure that your Sonatype OSSRH JIRA account credentials are specified in your `settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>your-jira-id</username>
      <password>your-jira-pwd</password>
    </server>
  </servers>
</settings>
```

3. Install `gpg` and distribute your key pair - see [here](http://central.sonatype.org/pages/working-with-pgp-signatures.html).  OS X users may need to execute:

```
#!bash
export GPG_TTY=`tty`;
```

4. As [Habushu](https://github.com/TechnologyBrewery/habushu/) modules that support `brett` are built as a part of the release process, ensure that all of prerequisite tools needed by [Habushu](https://github.com/TechnologyBrewery/habushu/) are installed.

5. Execute `mvn -Pwith-python-support release:clean release:prepare`, answer the prompts for the versions and tags, and perform `mvn release:perform`

## Licensing
Fermenter is available under the [MIT License](http://opensource.org/licenses/mit-license.php).
