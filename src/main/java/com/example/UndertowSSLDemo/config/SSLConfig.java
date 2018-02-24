package com.example.UndertowSSLDemo.config;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：YangJx
 * @Description：同时支持http和https的SSL配置
 * @DateTime：2018/2/24 13:00
 */
@Configuration
public class SSLConfig {

    /**
     * http1.1端口号
     */
    @Value("${http.port}")
    private int httpPort;

    /**
     * https端口号
     */
    @Value("${server.port}")
    private int httpsPort;

    /**
     * 针对Undertow容器
     * 配置多端口
     * 配置http2.0支持
     * 配置http端口重定向到https端口
     *
     * @return
     */
    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory undertow = new UndertowEmbeddedServletContainerFactory();
        //这段就可以可以转换为http2
        undertow.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
        //这段可以增加http重定向，如果只需要http2的话下面的代码可以去掉
        undertow.addBuilderCustomizers((Undertow.Builder builder) -> builder.addHttpListener(httpPort, "0.0.0.0"));
        //下面这段是将http的8080端口重定向到https的9090端口上
        undertow.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addSecurityConstraint(
                new SecurityConstraint().addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
                        .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                        .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT)
        ).setConfidentialPortManager(exchange -> this.httpsPort));
        return undertow;
    }

    /*@Bean
    public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void postProcessContext(CallbackGenerator.Context context) {
				SecurityConstraint constraint = new SecurityConstraint();
				constraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				constraint.addCollection(collection);
				context.addConstraint(constraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(httpConnector());
		return tomcat;
	}

	@Bean
	public Connector httpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");  //Connector监听http请求
		connector.setPort(8080);  //Connector监听http的端口号
		connector.setSecure(false);  //关闭http监听的安全功能
		connector.setRedirectPort(9090); //监听到http的端口号后，重定向到https的端口号
		return connector;
	}*/

}
