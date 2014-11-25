NewRelic Test
============

This test contains a synchronous servlet endpoint (/sync) and an async servlet endpoint (/async).  Both do
the same except one uses servlet async contexts.  When the NewRelic JAR is used, the async endpoint will
intermittently fail.  The endpoint writes either SUCCESS if the query parameterMap is size() > 0 or FAIL
otherwise.  The JMeter test always passes in query params so it should always pass.

1. Build the JAR (`mvn clean package`)
2. Update the newrelic.yml to have proper license key
3. Run the JAR: ``java -javaagent:./newrelic.jar -Dnewrelic.config.app_name=LOCAL.SampleServlet -jar target/newrelic-async-test-1.0.0.jar``
4. Open the ``newrelic-test.jmx`` in JMeter
5. Run the test (should start intermittently failing after a minute)
6. Now Disable the Async test and Enable the Sync Test
7. Run the test and it should not fail
