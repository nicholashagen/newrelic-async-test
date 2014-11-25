package sample.servlet;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan({ "sample.servlet" })
public class SampleServletApplication extends SpringBootServletInitializer {

	private static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(10);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleServletApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SampleServletApplication.class);
	}

	@Bean
	public ServletRegistrationBean registerAsyncEndpoint() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new HttpServlet() {

			@Override
			public void service(final ServletRequest req, final ServletResponse res)
					throws ServletException, IOException {

				final AsyncContext contxt = req.startAsync();
				EXECUTORS.submit(new Runnable() {
					@Override
					public void run() {

						try {
							Thread.sleep(500);
							if (req.getParameterMap().size() == 0) {
								res.getWriter().print("FAIL");
							}
							else {
								res.getWriter().print("SUCCESS");
							}
							contxt.complete();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}

		}, "/async");

		bean.setName("async");
		return bean;
	}

	@Bean
	public ServletRegistrationBean registerSyncEndpoint() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new HttpServlet() {

			@Override
			public void service(final ServletRequest req, final ServletResponse res)
					throws ServletException, IOException {

				try {
					Thread.sleep(500);
					if (req.getParameterMap().size() == 0) {
						res.getWriter().print("FAIL");
					}
					else {
						res.getWriter().print("SUCCESS");
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, "/sync");

		bean.setName("sync");
		return bean;
	}
}
