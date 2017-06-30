package org.java.test.helper.generator.maven.plugin;

import org.hamcrest.io.FileMatchers;

import org.java.test.helper.generator.maven.plugin.testutils.TestProjectResource;

import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

import static org.hamcrest.Matchers.is;

import static org.hamcrest.io.FileMatchers.anExistingFile;

import static org.junit.Assert.assertThat;


public class MatchersMojoJDK8AndNewHamcrestSystemTest {
	@Rule
	public TestProjectResource testproject = new TestProjectResource(
			"testprojectJava8AndCurrentHamcrestVersion");

	@Test
	public void testTestprojectShouldHavePom() throws Exception {
		assertThat(testproject.pomFile(), FileMatchers.aFileNamed(equalTo("pom.xml")));
	}

	@Test
	public void testTestprojectShouldBeBuildableWithoutMojoExecution() throws Exception {
		// Preparation

		// Execution
		final int exitStatus = testproject.executeGoals("clean", "compile");

		// Expectation
		assertThat("Execution exit status", exitStatus, is(0));
	}

	@Test
	public void testPluginExecutionShouldWorkWithoutAnyErrors() throws Exception {
		// Preparation

		// Execution
		final int exitStatus = testproject.executeGoals("testHelperGenerator:matchers");

		// Expectation
		assertThat("Execution exit status", exitStatus, is(0));
	}

	@Test
	public void testPhaseTestShouldStillWorkAfterPluginExecutionWithoutAnyErrors() throws Exception {
		// Preparation

		// Execution
		final int exitStatus = testproject.executeGoals("testHelperGenerator:matchers", "test");

		// Expectation
		assertThat("Execution exit status", exitStatus, is(0));
	}

	@Test
	public void testPluginRunHasCreatedMatcherSourceOnCallingPluginGoalDirectly() throws Exception {

		// Preparation
		assertThat("For this test required file is missing",
			testproject.srcMainJava("some/pck/model/SimpleModel.java"),
			is(anExistingFile()));

		// Execution
		testproject.executeGoals("testHelperGenerator:matchers");

		// Expectation
		assertThat(
			"Should have been generated: " +
			testproject.generatedTestSourcesDir("some/pck/model/SimpleModelMatcher.java"),
			testproject.generatedTestSourcesDir("some/pck/model/SimpleModelMatcher.java"),
			is(anExistingFile()));
	}

	@Test
	public void testPluginRunHasCreatedMatcherSourceOnTestGoal() throws Exception {

		// Preparation
		assertThat("For this test required file is missing",
			testproject.srcMainJava("some/pck/model/SimpleModel.java"),
			is(anExistingFile()));

		// Execution
		testproject.executeGoals("test");

		// Expectation
		assertThat(
			"Should have been generated: " +
			testproject.generatedTestSourcesDir("some/pck/model/SimpleModelMatcher.java"),
			testproject.generatedTestSourcesDir("some/pck/model/SimpleModelMatcher.java"),
			is(anExistingFile()));
	}
}
