package com.boot.jx.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.boot.jx.AppConfig;
import com.boot.jx.AppConstants;
import com.boot.jx.AppContextUtil;
import com.boot.jx.http.CommonHttpRequest;
import com.boot.jx.http.Kooky;
import com.boot.jx.scope.tnt.TenantContextHolder;
import com.boot.jx.swagger.MockParamBuilder.MockParam;
import com.boot.utils.ArgUtil;
import com.boot.utils.CollectionUtil;
import com.boot.utils.Constants;
import com.boot.utils.CryptoUtil.HashBuilder;
import com.boot.utils.StringUtils;
import com.boot.utils.UniqueID;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author lalittanwar
 *
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty("app.swagger")
public class DefaultSwaggerConfig {

	public static final String PARAM_STRING = "string";
	public static final String PARAM_HEADER = "header";
	public static final String SWGGER_SECRET_PARAM = "x-swagger-key";
	public static final String SWGGER_SECRET_VALUE = UniqueID.generateString();

	@Autowired(required = false)
	DocketWrapper docketWrapper;

	@Value("${swagger.package}")
	String swaggerPackage;

	@Value("${swagger.groupName}")
	String swaggerDefaultGroup;

	@Bean
	public Docket productApi(@Autowired(required = false) List<MockParam> mockParams) {

		if (docketWrapper != null && docketWrapper.getDocket() != null) {
			return docketWrapper.getDocket();
		}

		Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName(swaggerDefaultGroup).select()
				.apis(RequestHandlerSelectors.basePackage(swaggerPackage))
				// .paths(regex("/product.*"))
				.build();

		List<Parameter> operationParameters = new ArrayList<Parameter>();
		List<SecurityScheme> securitySchemes = new ArrayList<SecurityScheme>();

		if (ArgUtil.is(mockParams))
			for (MockParam mockParam : mockParams) {

				if (ArgUtil.is(mockParam.getSecurityScheme())) {
					securitySchemes.add(new ApiKey(mockParam.getSecurityScheme(), mockParam.getName(),
							StringUtils.toLowerCase(ArgUtil.parseAsString(mockParam.getType()))));
				} else {
					AllowableValues allowableValues = null;
					if (mockParam.getValues() != null) {
						allowableValues = new AllowableListValues(mockParam.getValues(), mockParam.getValueType());
					}
					Parameter parameter = new ParameterBuilder().name(mockParam.getName())
							.description(mockParam.getDescription()).defaultValue(mockParam.getDefaultValue())
							.modelRef(new ModelRef(PARAM_STRING))
							.parameterType(mockParam.getType().toString().toLowerCase())
							.allowableValues(allowableValues).required(mockParam.isRequired())
							.hidden(mockParam.isHidden()).build();
					operationParameters.add(parameter);

				}

			}
		AppContextUtil.getSessionId(true);
		AppContextUtil.getTraceId(true, true);

//		operationParameters.add(new ParameterBuilder().name(AppConstants.TRANX_ID_XKEY).description("Transaction Id")
//				.defaultValue(AppContextUtil.getTraceId()).modelRef(new ModelRef(PARAM_STRING))
//				.parameterType(PARAM_HEADER).required(false).build());
//		operationParameters.add(new ParameterBuilder().name(AppConstants.TRACE_ID_XKEY).description("Trace Id")
//				.defaultValue(AppContextUtil.getTraceId()).modelRef(new ModelRef(PARAM_STRING))
//				.parameterType(PARAM_HEADER).required(false).build());

		docket.globalOperationParameters(operationParameters);
		docket.apiInfo(metaData());
		docket.securitySchemes(securitySchemes);
		return docket;
	}

	@Bean
	@ConditionalOnProperty(value = "swagger.tranx.enabled", havingValue = "true")
	public MockParam tranxParam() {
		return new MockParamBuilder().name(AppConstants.TRANX_ID_XKEY).description("Transaction Id")
				.defaultValue(AppContextUtil.getTraceId()).parameterType(MockParamBuilder.MockParamType.HEADER)
				.required(false).build();
	}

	@Bean
	@ConditionalOnProperty(value = "swagger.tranx.enabled", havingValue = "true")
	public MockParam traceParam() {
		return new MockParamBuilder().name(AppConstants.TRACE_ID_XKEY).description("Trace Id")
				.defaultValue(AppContextUtil.getTraceId()).parameterType(MockParamBuilder.MockParamType.HEADER)
				.required(false).build();
	}

	@Bean
	@ConditionalOnProperty(value = "swagger.tenant.enabled", havingValue = "true")
	public MockParam tenantParam() {
		return new MockParamBuilder().name(TenantContextHolder.TENANT).description("Tenant Country")
				.defaultValue(Constants.BLANK).parameterType(MockParamBuilder.MockParamType.HEADER).required(false)
				.build();
	}

	@Bean
	@ConditionalOnProperty(value = "swagger.key.enabled", havingValue = "true")
	public MockParam swaggerParam() {
		return new MockParamBuilder().name(SWGGER_SECRET_PARAM).description(SWGGER_SECRET_PARAM)
				.defaultValue(SWGGER_SECRET_VALUE).parameterType(MockParamBuilder.MockParamType.HEADER)
				.allowableValues(CollectionUtil.getList(SWGGER_SECRET_VALUE), PARAM_STRING).required(true).hidden(true)
				.build();

	}

	@Autowired
	AppConfig appConfig;

	@Value("${swagger.title}")
	String swaggerTitle;

	@Value("${swagger.description}")
	String swaggerDescription;

	@Value("${swagger.contact.url}")
	String swaggerContactUrl;

	@Value("${swagger.contact.name}")
	String swaggerContactName;

	@Value("${swagger.contact.email}")
	String swaggerContactEmail;

	private ApiInfo metaData() {
		return new ApiInfo(ArgUtil.nonEmpty(swaggerTitle, appConfig.getAppName()),
				ArgUtil.nonEmpty(swaggerDescription,
						String.format("%s#%s#%s", appConfig.getAppEnv(), appConfig.getAppGroup(),
								appConfig.getAppId())),
				String.format("1.0 - %s", appConfig.getAppAppBuildStamp()), "Terms of service",
				new Contact(swaggerContactName, swaggerContactUrl, swaggerContactEmail), "Apache License Version 2.0",
				"https://www.apache.org/licenses/LICENSE-2.0", CollectionUtil.asList());
	}

	public static class DocketWrapper {
		Docket docket;

		public DocketWrapper(Docket docket) {
			this.docket = docket;
		}

		public Docket getDocket() {
			return docket;
		}
	}

	@Value("${swagger.auth.username}")
	String swaggerAuthUsername;

	@Value("${swagger.auth.password}")
	String swaggerAuthPassword;

	@Autowired
	CommonHttpRequest commonHttpRequest;

	public boolean isLoggedIn() {
		if (!ArgUtil.is(swaggerAuthPassword)) {
			return true;
		}
		String token = commonHttpRequest.get("swagger_auth_token");
		HashBuilder builder = new HashBuilder().interval(300).secret(swaggerAuthPassword).message(swaggerAuthUsername);
		if (ArgUtil.is(token) && builder.validate(token)) {
			return true;
		}
		String username = commonHttpRequest.get("swagger_auth_username");
		String password = commonHttpRequest.get("swagger_auth_password");

		if (ArgUtil.areEqual(username, swaggerAuthUsername) && ArgUtil.areEqual(password, swaggerAuthPassword)) {
			token = builder.toHmac().output();
			commonHttpRequest.setCookie(new Kooky().name("swagger_auth_token").value(token));
			return true;
		}

		return false;
	}

}
