package io.github.marmer.testutils.generators.beanmatcher.processing;

import io.github.marmer.testutils.generators.beanmatcher.dependencies.BeanPropertyMatcher;

import lombok.Value;

import static org.hamcrest.CoreMatchers.containsString;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import org.hamcrest.StringDescription;

import static org.junit.Assert.assertThat;

import org.junit.Test;


public class BeanPropertyMatcherTest {
    @Test
    public void testMatches_OnlyWithMatchingTypeInitialized_ShouldNotMatchInstanceOfDifferentType()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        // Execution
        final boolean matches = classUnderTest.matches(
                new AnotherClassWithSingleProperty("someValue"));

        // Assertion
        assertThat("matches", matches, is(false));
    }

    @Test
    public void testMatches_OnlyWithMatchingTypeInitialized_ShouldMatchInstanceOfSameType()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        // Execution
        final boolean matches = classUnderTest.matches(new ClassWithSingleProperty("someValue"));

        // Assertion
        assertThat("matches", matches, is(true));
    }

    @Test
    public void testDescribeTo_OnlyWithMatchingTypeInitialized_DescriptionTextShouldContainInstanceOfDescriptionText()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);
        final Description description = new StringDescription();

        // Execution
        classUnderTest.describeTo(description);

        // Assertion
        assertThat("Matcher description Text",
            description.toString(),
            containsString(instanceOfDescriptionText(ClassWithSingleProperty.class)));
    }

    @Test
    public void testMatches_InitializedWithDynamicPropertyAndCalledWithMatchingProperty_ShouldMatch()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        // Execution
        final boolean matches = classUnderTest.with("someProperty", is(equalTo("someValue")))
                                              .matches(new ClassWithSingleProperty("someValue"));

        // Assertion
        assertThat("matches", matches, is(true));
    }

    @Test
    public void testMatches_InitializedWithDynamicPropertyAndCalledWithNotMatchingProperty_ShouldNotMatch()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        // Execution
        final boolean matches = classUnderTest.with("someProperty", is(equalTo("someValue")))
                                              .matches(
                                                  new ClassWithSingleProperty(
                                                      "someDifferentValue"));

        // Assertion
        assertThat("matches", matches, is(false));
    }

    @Test
    public void testMatches_InitializedWithDynamicPropertyAndCallWithNotExistingProperty_ShouldNotMatch()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        // Execution
        final boolean matches = classUnderTest.with("differentProperty").matches(
                new ClassWithSingleProperty("someValue"));

        // Assertion
        assertThat("matches", matches, is(false));
    }

    @Test
    public void testMatches_InitializedWithDynamicPropertyAndCallWithExistingProperty_ShouldMatch()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        // Execution
        final boolean matches = classUnderTest.with("someProperty").matches(
                new ClassWithSingleProperty("someValue"));

        // Assertion
        assertThat("matches", matches, is(true));
    }

    @Test
    public void testDescribeTo_InitializedWithDynamicPropertyAndCallWithExistingPropertyAndInnerMatcher_ShouldContainIsInstanceDescription()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        final Description description = new StringDescription();

        // Execution
        classUnderTest.with("someProperty", equalTo("someValue")).describeTo(description);

        // Assertion
        assertThat("Matcher description Text",
            description.toString(),
            containsString(instanceOfDescriptionText(ClassWithSingleProperty.class)));
    }

    @Test
    public void testDescribeTo_InitializedWithDynamicPropertyAndCallWithExistingPropertyAndInnerMatcher_ShouldContainHasPropertyDescription()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        final Description description = new StringDescription();

        // Execution
        classUnderTest.with("someProperty", equalTo("someValue")).describeTo(description);

        // Assertion
        assertThat("Matcher description Text",
            description.toString(),
            containsString(hasPropertyDescriptionText("someProperty", equalTo("someValue"))));
    }

    @Test
    public void testDescribeTo_InitializedWithDynamicPropertyAndCallWithExistingProperty_ShouldContainIsInstanceDescription()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        final Description description = new StringDescription();

        // Execution
        classUnderTest.with("someProperty").describeTo(description);

        // Assertion
        assertThat("Matcher description Text",
            description.toString(),
            containsString(instanceOfDescriptionText(ClassWithSingleProperty.class)));
    }

    @Test
    public void testDescribeTo_InitializedWithDynamicPropertyAndCallWithExistingProperty_ShouldContainHasPropertyDescription()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassWithSingleProperty> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        final Description description = new StringDescription();

        // Execution
        classUnderTest.with("someProperty").describeTo(description);

        // Assertion
        assertThat("Matcher description Text",
            description.toString(),
            containsString(hasPropertyDescriptionText("someProperty")));
    }

    @Test
    public void testMatches_InitializedWithDynamicPropertyWithInnerMatcherAndCallWithMotExistingPropertiesAtSecondPlace_ShouldMatch()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassTwoProperties> classUnderTest =
            new BeanPropertyMatcher<ClassTwoProperties>(ClassTwoProperties.class);

        // Execution
        final boolean matches = classUnderTest.with("firstProperty", equalTo("firstPropertyValue"))
                                              .with("notExistingProperty").matches(
                                                  new ClassTwoProperties("firstPropertyValue",
                                                      "secondPropertyValue"));

        // Assertion
        assertThat("matches", matches, is(false));
    }

    @Test
    public void testMatches_InitializedWithDynamicPropertyWithoutInnerMatcherAndCallWithTwoExistingProperties_ShouldMatch()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassTwoProperties> classUnderTest =
            new BeanPropertyMatcher<ClassTwoProperties>(ClassTwoProperties.class);

        // Execution
        final boolean matches = classUnderTest.with("secondProperty")
                                              .with("firstProperty", equalTo("firstPropertyValue"))
                                              .matches(
                                                  new ClassTwoProperties("firstPropertyValue",
                                                      "secondPropertyValue"));

        // Assertion
        assertThat("matches", matches, is(true));
    }

    @Test
    public void testMatches_InitializedWithDynamicPropertyWithoutInnerMatcherAndCallWithNotExistingPropertiesAtSecondPlace_ShouldNotMatch()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassTwoProperties> classUnderTest =
            new BeanPropertyMatcher<ClassTwoProperties>(ClassTwoProperties.class);

        // Execution
        final boolean matches = classUnderTest.with("secondProperty")
                                              .with("notExistingProperty",
                                                  equalTo("firstPropertyValue")).matches(
                                                  new ClassTwoProperties("firstPropertyValue",
                                                      "secondPropertyValue"));

        // Assertion
        assertThat("matches", matches, is(false));
    }

    @Test
    public void testMatches_InitializedWithDynamicPropertyWithoutInnerMatcherAndCallWithExistingPropertiesAtSecondPlaceButNotMatching_ShouldNotMatch()
        throws Exception {
        // Preparation
        final BeanPropertyMatcher<ClassTwoProperties> classUnderTest =
            new BeanPropertyMatcher<ClassTwoProperties>(ClassTwoProperties.class);

        // Execution
        final boolean matches = classUnderTest.with("secondProperty")
                                              .with("firstProperty", equalTo("notMatchingValue"))
                                              .matches(
                                                  new ClassTwoProperties("firstPropertyValue",
                                                      "secondPropertyValue"));

        // Assertion
        assertThat("matches", matches, is(false));
    }

    @Test
    public void testDescribeMissmatch_WrongTypeGiven_DescriptionShuoldContainTypeInformationOfGivenWrongType()
        throws Exception {
        // Preparation
        final Matcher<?> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class);

        final Description description = new StringDescription();
        final AnotherClassWithSingleProperty modelClass =
            new AnotherClassWithSingleProperty("someProperty");

        // Execution
        classUnderTest.describeMismatch(modelClass, description);

        // Expectation

        assertThat(description.toString(),
            startsWith("Is an instance of " + AnotherClassWithSingleProperty.class));
    }

    @Test
    public void testDescribeMissmatch_InstanceWithNotMatchingPropertyGiven_ShouldContainNotMatchingPropertyText()
        throws Exception {
        // Preparation
        final String propertyName = "someProperty";
        final Matcher<String> propertyMatcher = equalTo("expectedPropertyValue");
        final Matcher<?> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class).with(
                propertyName,
                propertyMatcher);

        final Description description = new StringDescription();
        final ClassWithSingleProperty modelClass = new ClassWithSingleProperty("realPropertyValue");

        // Execution
        classUnderTest.describeMismatch(modelClass, description);

        // Expectation
        final String propertyMissmatchDescriptionText = getHasPropertyMissmatchDescriptionFor(
                modelClass,
                propertyName,
                propertyMatcher);

        assertThat(description.toString(),
            containsString(" and " + propertyMissmatchDescriptionText));
    }

    @Test
    public void testDescribeMissmatch_InstanceWithNotExistingPropertyGiven_ShouldContainNotMatchingPropertyText()
        throws Exception {
        // Preparation
        final String propertyName = "someNotExistingProperty";
        final Matcher<String> propertyMatcher = equalTo("expectedPropertyValue");
        final Matcher<?> classUnderTest =
            new BeanPropertyMatcher<ClassWithSingleProperty>(ClassWithSingleProperty.class).with(
                propertyName,
                propertyMatcher);

        final Description description = new StringDescription();
        final ClassWithSingleProperty modelClass = new ClassWithSingleProperty("realPropertyValue");

        // Execution
        classUnderTest.describeMismatch(modelClass, description);

        // Expectation
        final String propertyMissmatchDescriptionText = getHasPropertyMissmatchDescriptionFor(
                modelClass,
                propertyName,
                propertyMatcher);

        assertThat(description.toString(),
            containsString(" and " + propertyMissmatchDescriptionText));
    }

    private String getHasPropertyMissmatchDescriptionFor(final Object modelClass,
        final String propertyName,
        final Matcher<String> propertyMatcher) {
        final Description propertyMissmatchDescription = new StringDescription();
        hasProperty(propertyName, equalTo(propertyMatcher)).describeMismatch(modelClass,
            propertyMissmatchDescription);

        return propertyMissmatchDescription.toString();
    }

    private String hasPropertyDescriptionText(final String propertyName) {
        final Description instanceOfDescription = new StringDescription();
        Matchers.hasProperty(propertyName).describeTo(instanceOfDescription);

        return instanceOfDescription.toString();
    }

    private String hasPropertyDescriptionText(final String propertyName,
        final Matcher<String> innerMatcher) {
        final Description instanceOfDescription = new StringDescription();
        Matchers.hasProperty(propertyName, innerMatcher).describeTo(instanceOfDescription);

        return instanceOfDescription.toString();
    }

    private String instanceOfDescriptionText(final Class<ClassWithSingleProperty> type) {
        final Description instanceOfDescription = new StringDescription();
        Matchers.instanceOf(type).describeTo(instanceOfDescription);

        return instanceOfDescription.toString();
    }

    @Value
    public static class ClassWithSingleProperty {
        private String someProperty;
    }

    @Value
    public static class AnotherClassWithSingleProperty {
        private String someProperty;
    }

    @Value
    public static class ClassTwoProperties {
        private String firstProperty;
        private String secondProperty;
    }
}
