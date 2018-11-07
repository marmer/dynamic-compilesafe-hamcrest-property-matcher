[![Build Status](https://travis-ci.org/marmer/hamcrest-matcher-generator.svg?branch=master)](https://travis-ci.org/marmer/hamcrest-matcher-generator)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.marmer.testutils/hamcrest-matcher-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.marmer.testutils/hamcrest-matcher-generator)
 
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=io.github.marmer.testutils:hamcrest-matcher-generator&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.github.marmer.testutils:hamcrest-matcher-generator)
[![Code Coverage](https://sonarcloud.io/api/project_badges/measure?project=io.github.marmer.testutils:hamcrest-matcher-generator&metric=coverage)](https://sonarcloud.io/component_measures?id=io.github.marmer.testutils:hamcrest-matcher-generator&metric=Coverage)
[![Technical Dept](https://sonarcloud.io/api/project_badges/measure?project=io.github.marmer.testutils:hamcrest-matcher-generator&metric=sqale_index)](https://sonarcloud.io/project/issues?facetMode=effort&id=io.github.marmer.testutils:hamcrest-matcher-generator&resolved=false&types=CODE_SMELL)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.marmer.testutils:hamcrest-matcher-generator&metric=security_rating)](https://sonarcloud.io/component_measures?id=io.github.marmer.testutils:hamcrest-matcher-generator&metric=Security)
[![Maintainability](https://sonarcloud.io/api/project_badges/measure?project=io.github.marmer.testutils:hamcrest-matcher-generator&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=io.github.marmer.testutils:hamcrest-matcher-generator&metric=Maintainability)
[![Reliability](https://sonarcloud.io/api/project_badges/measure?project=io.github.marmer.testutils:hamcrest-matcher-generator&metric=reliability_rating)](https://sonarcloud.io/component_measures?id=io.github.marmer.testutils:hamcrest-matcher-generator&metric=Reliability)

hamcrest-matcher-generator
==========================
This library provides the generation of hamcrest matchers without the need to pollute the production code. 

Bean Property Matcher
---------------------

Using Hamcrest's Matcher HasPropertyWithValue, better known as Matchers.hasProperty, is nice if you don't have created the class you want to test yet. The Problem with that kind of Matcher is, the more test you have matching the same property the harder it is to change the property name.

### Negative example

Lets assume you have a bean with a property named `myFancyProperty`. The following line would match if the value is equal to `Fancy value`.

`assertThat(someFancyBean, hasProperty("myFancyProperty", equalTo("Fancy value")));`

Now assume, you have tests all over your project with a line like this one testinh the `myFancyProperty` and you want to change its name to `myAwesomeProperty`. After renaming the property you may not find all the places in your tests to adjust. Now you only have to wait until the related tests fail to find the other places and fix the property name.

This strategy works pretty well, if you have only changed the property name and the test suite runs fast. Otherwise you'll get your feedback late or it get's really confusing and error prone.

### Solution

The idea of this helper is to get your feedback allredy at compile time. So your favorite IDE or build tool can show you all the places you have to change as well in a way like the following:

	assertThat(new SimpleModel("someValue"), isSimpleModel().withSomeProperty(equalTo("someValue")));

You only have to generate the matchers in advance with this tool, probably with a Plugin of your favorite IDE using this lib or with your favorite build tool.

### Atomic Error Messages

In addition, when you use the "both" or "allOf" matchers with a lot of properties, it's hard to read and only the "expected" part of the error message is complete with all properties. 

The expectation part is non atomic. It contains only the first failing matcher result.

This tool will show you all non matching properties in an atomic way (if the given matchers don't fail with an exception) so you don't have to run your tests over and over again for each non matching property.

### How-To

Currently there is only a maven plugin for the matcher generation. But a gradle extension is planed as well. If you want to see how to configure your project, read the Requirements section and have a look at the [projects used for testing the plugin](hamcrest-matcher-generator-maven-plugin/src/test/projects)

Requirements
============
Here you can see what's needed for your project to work properly with the plugin and its generated sources.

JDK
---

At least JDK6 is required to use the generated source code but the plugin requires JDK8 to create it.

Generated sources may also work with JDK5. But it is and will not be tested so there is no guarantee!

Hamcrest
--------
Because hamcrest matchers are generated, you will need a dependency to hamcrest to be able to use the generated sources. In General you are free to choose your version of hamcrest as long ...

Your Project should be at least of Java version 1.6 and use a hamcrest version of 1.2. The resulting code will not work without hamcrest.

For JDK7+ projects you should (but don't have to) use the following hamcrest version

	<dependency>
		<groupId>org.hamcrest</groupId>
		<artifactId>java-hamcrest</artifactId>
		<version>2.0.0.0</version>
		<scope>test</scope>
	</dependency>	

For JDK6 you may use:

	<dependency>
		<groupId>org.hamcrest</groupId>
		<artifactId>hamcrest-all</artifactId>
		<version>1.3</version>
		<scope>test</scope>
	</dependency>`

plugin setup
------------
````
            <plugin>
                <groupId>io.github.marmer.testutils</groupId>
                <artifactId>hamcrest-matcher-generator-maven-plugin</artifactId>
                <version>${version.hamcrest-matcher-generator}</version>
                <configuration>
                    <matcherSources>
                        <matcherSource>com.some.package</matcherSource>
                        <matcherSource>com.some.full.qualified.ClassName</matcherSource>
                    </matcherSources>
                    <ignoreClassesWithoutProperties>true</ignoreClassesWithoutProperties>
                    <outputDir>target/generated-test-sources</outputDir>
                </configuration>
                <executions>
                    <execution>
                        <id>matchers</id>
                        <goals>
                            <goal>matchers</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

````

hamcrest-matcher-generator-dependencies
---------------------------------------
The generated sources need the folowing dependency to work correctly

	<dependency>
		<groupId>io.github.marmer.testutils</groupId>
		<artifactId>hamcrest-matcher-generator-dependencies</artifactId>
		<version>#INSERT_CURRENT_PROJECT_VERSION#</version>
		<scope>test</scope>
	</dependency>
	
Test sources
------------
The build will work fine already if the requirements above are fulfilled. Depending on your IDE you'll get errors when you only import the project here you have two options. First, you can explicitely set the generated directory as source-folder, or you use the maven build helper plugin (if your IDE is aware of it) like as follows:


	<plugin>
		<groupId>org.codehaus.mojo</groupId>
		<artifactId>build-helper-maven-plugin</artifactId>
		<version>1.12</version>
		<executions>
			<execution>
				<id>add-test-source</id>
				<phase>generate-test-sources</phase>
				<goals>
					<goal>add-test-source</goal>
				</goals>
				<configuration>
					<sources>
						<source>${project.build.directory}/generated-test-sources</source>
					</sources>
				</configuration>
			</execution>
		</executions>
	</plugin>
	

run test phase once (or a later one)
------------------------------------
The generation happens at phase generate-test-sources by default because it need's the compiled model classes. If you don't, your IDE won't be able to use the model classes in your ide, because they simply don't exist ;)

### Changelog
This project uses semantic versioning. See https://semver.org/

#### 1.3.0
* Support for Matcher-Generation for Interfaces

### 2.0.0
* Support for multiple inner classes with the same name added by using different naming strategies. For backwart compatibility use <namingStrategy>PLAIN</namingStrategy> which is the old strategy

### 3.0.0 
* Matchers generated with the package naming strategy will and with the postfix "Matcher" again.

### 3.0.1
* Bigfix: useless class compilation at the end of the generation process removed.

### Planed for future releases and known issues
* Naming Strategy for classnames instead of package names
* Better hanling of private properties and members
* Bugfix when running the build without cleaning before
* Errorlogging
* Bug: does not produce a matcher anything for :
```
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
@JacksonXmlRootElement(localName = "fehler")
public class FehlerDTO extends DefaultErrorAttributes {
    private int status;
    private String fehler;
    private String stacktrace;

    @Override
    public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final boolean includeStackTrace) {
        final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        errorAttributes.computeIfAbsent("fehler", s ->
                errorAttributes.getOrDefault("error",
                        errorAttributes.getOrDefault("message",
                                "Unbekannter Fehler ist aufgetreten")));
        return errorAttributes;
    }
}
```