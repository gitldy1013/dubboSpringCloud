package org.cmcc.service.hnzsh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerSpringMvcPlugin(ServletContext servletContext) {
        return new Docket(DocumentationType.SWAGGER_2)
                //生成文档的api对象定义
                .apiInfo(apiInfo())
                .select()
                //扫描生成文档的包路径
                .apis(RequestHandlerSelectors.basePackage("com.iwhalecloud.xjrh"))
                //.paths(PathSelectors.ant("/*Api/*"))//生成文档的类访问路径，就是controller类里@RequestMapping("orderApi")
                .paths(PathSelectors.any())
                .build();
        //.host(Host);//配置swagger前缀
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API文档")//文档标题
                .description("湖南中石化定制化对账文件推送到FTP服务器接口")//文档说明
                .licenseUrl("#")
                //版本号
                .version("2.0").build();
    }
}
