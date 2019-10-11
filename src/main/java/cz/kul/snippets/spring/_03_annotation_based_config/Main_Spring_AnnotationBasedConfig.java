package cz.kul.snippets.spring._03_annotation_based_config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TODO distinguish "annotation based config" and "component scan"
 *
 * TODO component scan does not allow you to have two beans with the same name
 *
 *
 * You can mark beans by annotations and spring then automatically find them.
 * 
 * You does not have to create xml configuration. It is more comfortable.
 * 
 * Notes:
 * <ul>
 * <li>Annotation configuration is done before xml config. So if you mix xml and
 * annotation based config, xml config can override annotation based config</li>
 * </ul>
 * 
 */
public class Main_Spring_AnnotationBasedConfig {

    public static void main(String[] args) {
        String cfgFile = "_03_spring.xml";
        try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(cfgFile)) {
            fieldIndection(ctx);
            constructorInjection(ctx);
            setterInjection(ctx);
            postConstructAndPreDestroy(ctx);
            autowireByCustomMethod(ctx);
            autowireCollection(ctx);
            // TODO
            // is it possible to switch on the default lazy autowiring?
            // method injection- can be used when you inject prototype into singleton
            // how to inject request scope bean into longer lived scope - it is done through a proxy, then spring set the right bean for the particular request

            // You can also use @Autowired for interfaces that are well-known resolvable
            //            dependencies: BeanFactory, ApplicationContext, Environment, ResourceLoader,
            //            ApplicationEventPublisher, and MessageSource. These interfaces and their extended
            //            interfaces, such as ConfigurableApplicationContext or ResourcePatternResolver, are
            //            automatically resolved, with no special setup necessary.

            // @Qualifier annotation

            // property source

        }

    }

    private static void autowireCollection(ClassPathXmlApplicationContext ctx) {
        InjectedCollection bean = (InjectedCollection) ctx.getBean("injectedCollection");
        assertTrue(bean.getBazInstances().size() >= 2);
    }

    private static void autowireByCustomMethod(ClassPathXmlApplicationContext ctx) {
        // You can add autowire also to custom method. So it is another way how to
        // initialize your bans.
        CustomMethodInitialization bean = (CustomMethodInitialization) ctx.getBean("customMethodInitialization");
        assertNotNull(bean.getFoo());
        assertNotNull(bean.getBar());
    }

    private static void postConstructAndPreDestroy(ClassPathXmlApplicationContext ctx) {
        LifecycleBean service = (LifecycleBean) ctx.getBean("lifecycleBean");
        assertEquals("_postConstruct_", service.toString());
        // NOTE: I do not know how to test @PreDestroy
    }

    private static void setterInjection(ClassPathXmlApplicationContext ctx) {
        SetterInjectionService service = (SetterInjectionService) ctx.getBean("setterInjectionService");
        assertNotNull(service.getInjected());
    }

    private static void fieldIndection(ApplicationContext ctx) {
        // field injection is NOT preffered way how to inject
        //
        // disadvantages:
        //  * difficult to test. You need di framework or reflection magic to test it.
        //  * it is not clear which components the class depends on. You must check implementation.
        //
        // Preffered way is to use constructor injection for mandatory dependencies and setter injection
        // foroptional dependencies.
        FieldInjectionService service = (FieldInjectionService) ctx.getBean("fieldInjectionService");
        assertNotNull(service.getInjected());
    }

    private static void constructorInjection(ApplicationContext ctx) {
        ConstructorInjectionService service = (ConstructorInjectionService) ctx.getBean("constructorInjectionService");
        assertNotNull(service.getInjected());
    }

}
