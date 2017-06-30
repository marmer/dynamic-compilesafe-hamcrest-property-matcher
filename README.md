[![Build Status](https://travis-ci.org/marmer/java-test-helper-generator.svg?branch=master)](https://travis-ci.org/marmer/java-test-helper-generator)
[![codebeat badge](https://codebeat.co/badges/670d9178-beb7-438d-9823-53943c7fdf95)](https://codebeat.co/projects/github-com-marmer-java-test-helper-generator-master)
[![Coverage Status](https://coveralls.io/repos/github/marmer/java-test-helper-generator/badge.svg?branch=master)](https://coveralls.io/github/marmer/java-test-helper-generator?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/59415a87368b0800700df4a2/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/59415a87368b0800700df4a2)

dynamic-compilesafe-hamcrest-property-matcher
=============================================
This library (hopefully) provides a collection of helper generators for tests.

Bean Property Matcher
---------------------

Using Hamcrest's Matcher HasPropertyWithValue, better known as Matchers.hasProperty, is awesome if you don't have created the class you want to test yet. The Problem with that kind of Matcher is, the more test you have matching the same property the harder it is to change the property name.

### Negative example

Lets assume you have a bean with a property named `myFancyProperty`. The following line would match if the value is equal to `Fancy value`.

`assertThat(someFancyBean, hasProperty("myFancyProperty", equalTo("Fancy value")));`

Now assume, you have tests all over your project with a line like this one testinh the `myFancyProperty` and you want to change its name to `myAwesomeProperty`. After renaming the property you may not find all the places in your tests to adjust. Now you only have to wait until the related tests fail to find the other places and fix the property name.

This strategy works pretty well, if you have only changed the property name and the test suite runs fast. Otherwise you'll get your feedback late or it get's really confusing and error prone.

### Solution

The idea of this helper is to get your feedback allredy at compile time. So your favorite IDE or build tool can show you all the places you have to change as well in a way like the following:

`assertThat(someFancyBean, is(fancyBean().withMyFancyProperty(equalTo("Fancy Value))));`

You only have to generate the matchers in advance with this tool, probably with a Plugin of your favorite IDE using this lib or with your favorite build tool.

### How-To

TODO

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


java-test-helper-generator-dependencies
---------------------------------------
The generated sources need some classes 

	<dependency>
		<groupId>io.github.marmer.testutils</groupId>
		<artifactId>java-test-helper-generator-dependencies</artifactId>
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