package ms.kotlin.battleships.app.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.thymeleaf.ITemplateEngine
import org.thymeleaf.TemplateEngine
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver

@Configuration
class ThyMeLeafConfiguration {

    @Bean
    @Scope(value = "singleton")
    fun getTemplateResolver(): ITemplateResolver {
        val resolver = ClassLoaderTemplateResolver()
        resolver.templateMode = TemplateMode.HTML
        resolver.prefix = "templates/"
        resolver.suffix = ".html"
        resolver.characterEncoding = "UTF-8"

        return resolver
    }

    @Bean
    @Scope(value = "singleton")
    fun getTemplateEngine(): ITemplateEngine {
        val engine = TemplateEngine()
        engine.setTemplateResolver(getTemplateResolver())
        engine.addDialect(Java8TimeDialect())

        return engine
    }

}