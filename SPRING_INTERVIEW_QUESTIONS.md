# Spring Framework Interview Questions & Answers

## Spring Core Concepts

### What is Loose Coupling?

Loose coupling refers to a design where components have minimal dependencies on each other. Each component is independent and can be modified without affecting others. In Spring, this is achieved through Dependency Injection and interfaces.

**Example:**
```java
// Tight Coupling - Bad
class UserService {
    private MySQLDatabase db = new MySQLDatabase();
}

// Loose Coupling - Good
class UserService {
    private Database db; // Injected
    public UserService(Database db) {
        this.db = db;
    }
}
```

### What is a Dependency?

A dependency is an object or resource that another object requires to function. If class A uses class B, then class B is a dependency of class A. Dependencies can be injected through constructors, setters, or field injection.

### What is IOC (Inversion of Control)?

IOC is a design principle where the control of object creation and lifecycle management is transferred from the application code to a container (Spring Container). Instead of your code creating objects, the framework creates and manages them.

**Key Benefits:**
- Reduced coupling between components
- Easier testing and maintenance
- Centralized object lifecycle management

### What is Dependency Injection?

Dependency Injection (DI) is the mechanism through which the IOC container injects dependencies into objects. Instead of objects creating their dependencies, the container provides them.

### Can you give few examples of Dependency Injection?

**1. Constructor Injection:**
```java
@Component
public class UserService {
    private UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

**2. Setter Injection:**
```java
@Component
public class UserService {
    private UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

**3. Field Injection:**
```java
@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
```

### What is Auto Wiring?

Autowiring is a feature where Spring automatically injects dependencies without explicit configuration. Spring scans for beans and matches them based on type, name, or qualifiers.

### What are the important roles of an IOC Container?

1. **Object Creation:** Creates instances of beans
2. **Dependency Resolution:** Identifies and injects dependencies
3. **Lifecycle Management:** Manages bean initialization and destruction
4. **Configuration Management:** Manages bean configurations
5. **Event Handling:** Handles application events
6. **Resource Management:** Manages application resources

### What are Bean Factory and Application Context?

**Bean Factory:**
- Basic IOC container in Spring
- Lazy initialization (creates beans on demand)
- Lower memory footprint
- Limited features

**Application Context:**
- Extended version of Bean Factory
- Eager initialization (creates beans at startup)
- Rich features (AOP, event handling, resource management)
- Preferred in most applications

### Can you compare Bean Factory with Application Context?

| Feature | Bean Factory | Application Context |
|---------|-------------|-------------------|
| Initialization | Lazy | Eager |
| Features | Basic | Advanced |
| AOP Support | Limited | Full |
| Event Handling | No | Yes |
| Resource Management | Manual | Automatic |
| Performance | Better for small apps | Better for large apps |

### How do you create an application context with Spring?

```java
// Using ClassPathXmlApplicationContext
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

// Using AnnotationConfigApplicationContext
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

// Using SpringApplication in Spring Boot
SpringApplication.run(DemoApplication.class, args);
```

### How does Spring know where to search for Components or Beans?

Spring uses **Component Scanning** to search for beans. It scans the classpath for classes annotated with `@Component`, `@Service`, `@Repository`, `@Controller`, etc.

### What is a Component Scan?

Component scanning is the process where Spring automatically detects and registers beans from the classpath based on annotations.

### How do you define a component scan in XML and Java Configurations?

**XML Configuration:**
```xml
<context:component-scan base-package="com.example" />

<context:component-scan base-package="package name"
```

**Java Configuration:**
```java
@Configuration
@ComponentScan("com.example")
public class AppConfig {
}

@Configuration
@ComponentScan("package")
public class
```

**Spring Boot:**
```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### How is it done with Spring Boot?

Spring Boot automatically enables component scanning for the package where the `@SpringBootApplication` annotation is placed and its sub-packages.

### What does @Component signify?

`@Component` is a generic stereotype annotation that indicates a class is a Spring-managed component. Spring will automatically detect and register it as a bean.

```java
@Component
public class UserService {
}
```

### What does @Autowired signify?

`@Autowired` indicates that Spring should automatically inject a dependency. It can be used on constructors, setters, and fields.

```java
@Autowired
private UserRepository userRepository;
```

### What's the difference Between @Controller, @Component, @Repository, and @Service Annotations in Spring?

| Annotation | Purpose |
|-----------|---------|
| @Component | Generic component |
| @Service | Business logic layer |
| @Repository | Data access layer; provides exception translation |
| @Controller | Presentation layer; handles HTTP requests |

### What is the default scope of a bean?

The default scope is **Singleton**. A single instance of the bean is created and reused throughout the application.

### Are Spring beans thread safe?

**No**, Spring beans are not inherently thread-safe. Singleton beans are shared across threads, so you must ensure thread safety if the bean maintains state. 
Stateless beans are naturally thread-safe.

### What are the other scopes available?

1. **Singleton:** Single instance for the entire application (default)
2. **Prototype:** New instance created for each request
3. **Request:** New instance for each HTTP request (web-aware)
4. **Session:** New instance for each session (web-aware)
5. **Application:** Single instance for the entire ServletContext
6. **WebSocket:** Single instance per WebSocket session

```java
@Component
@Scope("prototype")
public class UserService {
}
```

### How is Spring's singleton bean different from Gang of Four Singleton Pattern?

| Aspect | Spring Singleton | GoF Singleton |
|--------|-----------------|---------------|
| Instance Creation | Container manages | Class manages itself |
| Number of instances | One per container | One globally |
| Thread Safety | Container handles | Manual implementation |
| Testability | Easy to mock | Difficult to mock |
| Flexibility | Can have multiple containers | Single global instance |

### What are the different types of dependency injections?

1. **Constructor Injection:** Dependencies passed via constructor
2. **Setter Injection:** Dependencies set via setter methods
3. **Field Injection:** Dependencies injected directly to fields
4. **Method Injection:** Dependencies injected via any method

### What is setter injection?

Setter injection provides dependencies through setter methods after object creation.

```java
@Component
public class UserService {
    private UserRepository userRepository;
    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### What is constructor injection?

Constructor injection provides dependencies through the constructor during object creation.

```java
@Component
public class UserService {
    private UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### How do you choose between setter and constructor injections?

- **Constructor Injection:** Use for required dependencies; ensures complete initialization
- **Setter Injection:** Use for optional dependencies; allows flexibility

Constructor injection is generally preferred as it ensures all required dependencies are provided.

### What are the different options available to create Application Contexts for Spring?

1. `ClassPathXmlApplicationContext` - Loads from XML classpath
2. `FileSystemXmlApplicationContext` - Loads from file system
3. `GenericXmlApplicationContext` - Generic XML context

4. `AnnotationConfigApplicationContext` - Loads from Java config

5. `GenericGroovyApplicationContext` - Groovy configuration
6. `SpringApplication.run()` - Spring Boot applications

### What is the difference between XML and Java Configurations for Spring?

| Aspect | XML Configuration | Java Configuration |
|--------|------------------|------------------|
| Format | Declarative (XML) | Programmatic (Java) |
| Type Safety | No | Yes |
| Refactoring | Difficult | Easy |
| Compilation Check | No | Yes |
| Flexibility | Limited | High |

### How do you choose between XML and Java Configurations for Spring?

- Use **Java Configuration** for new projects (type-safe, easier to refactor)
- Use **XML Configuration** for legacy projects or external library configuration
- **Spring Boot** prefers Java configuration with annotations

### How does Spring do Autowiring?

Spring autowiring process:
1. Spring identifies all beans in the container
2. For each field/constructor/setter marked with `@Autowired`, it searches for a matching bean
3. If a match is found, it injects the bean
4. If no match or multiple matches, it raises an exception

### What are the different kinds of matching used by Spring for Autowiring?

1. **By Class Type:** Matches based on class type (default)
2. **By Bean Name:** Matches based on bean name
3. **By Qualifier:** Uses `@Qualifier` annotation for specific matching
4. **By Primary:** Uses `@Primary` to indicate preferred bean

### How do you debug problems with Spring Framework?

1. Enable debug logging: `logging.level.root=DEBUG`
2. Use Spring Boot Actuator endpoints
3. Check application context for bean availability
4. Use IDE debugging tools
5. Check application logs for exceptions

### How do you solve NoUniqueBeanDefinitionException?

This occurs when multiple beans match the autowiring criteria.

**Solutions:**
```java
// Use @Primary
@Primary
@Component
public class PrimaryImplementation implements Service {
}

// Use @Qualifier
@Component
@Qualifier("specific")
public class SpecificImplementation implements Service {
}

// Inject with qualifier
@Autowired
@Qualifier("specific")
private Service service;
```

### How do you solve NoSuchBeanDefinitionException?

This occurs when no bean matches the autowiring criteria.

**Solutions:**
```java
// Create the bean
@Component
public class MyService {
}

// Use @Qualifier with existing bean
@Component
@Qualifier("myService")
public class MyService {
}

// Make dependency optional
@Autowired(required = false)
private MyService myService;
```

### What is @Primary?

`@Primary` indicates that a bean should be preferred when multiple beans of the same type exist.

```java
@Component
@Primary
public class PrimaryService implements Service {
}
```

### What is @Qualifier?

`@Qualifier` specifies which bean to inject when multiple beans of the same type exist.

```java
@Autowired
@Qualifier("specificService")
private Service service;
```

### What is CDI (Contexts and Dependency Injection)?

CDI is a Java EE standard for dependency injection. 
It provides similar functionality to Spring DI but is part of the Java EE specification.

### Does Spring Support CDI?

Yes, Spring supports CDI annotations like `@Inject`, `@Named`, and `@Qualifier` alongside Spring annotations.

### Would you recommend to use CDI or Spring Annotations?

Spring annotations are recommended for Spring applications 
as they provide more features and better integration with Spring ecosystem.

### What are the major features in different versions of Spring?

**Spring 3.0:**
- Java 5 annotations
- SpEL (Spring Expression Language)

**Spring 4.0:**
- Java 8 support (Lambda, Functional interfaces)
- WebSocket support

**Spring 5.0:**
- Java 9+ support
- Reactive programming (Project Reactor)
- Functional web framework

**Spring 6.0:**
- Java 17+ support (LTS)
- Native compilation support
- GraalVM support

### What are new features in Spring Framework 4.0?

- Java 8 support with Lambda expressions
- SpEL enhancements
- WebSocket and STOMP support
- Retry and Circuit Breaker patterns
- Better testing support

### What are new features in Spring Framework 5.0?

- Project Reactor for reactive programming
- WebFlux framework
- Functional programming support
- Improved testing with TestContext
- Kotlin support

### What are important Spring Modules?

1. **Spring Core** - Core functionality
2. **Spring Beans** - Bean factory and IOC
3. **Spring Context** - Application context
4. **Spring AOP** - Aspect-Oriented Programming
5. **Spring DAO** - Data access abstraction
6. **Spring ORM** - ORM integration
7. **Spring Web** - Web application support
8. **Spring MVC** - Model-View-Controller
9. **Spring Security** - Authentication and authorization
10. **Spring Data** - Data access layer

### What are important Spring Projects?

1. **Spring Boot** - Rapid development
2. **Spring Cloud** - Microservices
3. **Spring Data** - Data access
4. **Spring Security** - Security
5. **Spring Integration** - Integration patterns
6. **Spring Batch** - Batch processing
7. **Spring WebFlux** - Reactive web

### What is the simplest way of ensuring that we are using single version of all Spring related dependencies?

Use **Spring Boot Starter Parent** as the parent POM:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.0</version>
</parent>
```

### Name some of the design patterns used in Spring Framework

1. **Singleton Pattern** - Single bean instance
2. **Factory Pattern** - Bean Factory
3. **Proxy Pattern** - AOP proxies
4. **Template Method** - JdbcTemplate
5. **Observer Pattern** - Event handling
6. **Dependency Injection** - Custom pattern
7. **Strategy Pattern** - Multiple implementations

### What do you think about Spring Framework?

Spring Framework is one of the most popular Java frameworks providing comprehensive infrastructure for enterprise development. 
It promotes good design practices through IOC and DI, making applications loosely coupled and testable.

### Why is Spring Popular?

1. Reduces boilerplate code
2. Promotes best practices
3. Comprehensive ecosystem
4. Excellent documentation
5. Community support
6. Backward compatibility
7. Flexibility and modularity

### Can you give a big picture of the Spring Framework?

Spring Framework provides:
- **IOC/DI Container** for object management
- **AOP** for cross-cutting concerns
- **Data Access** with Spring DAO, JDBC, ORM
- **Transaction Management** abstractions
- **Web Framework** (MVC, WebFlux)
- **Security** framework
- **Integration** with external systems

---

## Spring MVC

### What is Model 1 architecture?

Model 1 is a simple web application architecture where JSP pages directly process requests 
AND interact with the database. No separate controller exists.

**Flow:** Request → JSP → Database

**Limitations:** Not suitable for complex applications, poor separation of concerns.

### What is Model 2 architecture?

Model 2 is the MVC architecture where requests are processed by servlets (Controller), 
which interact with business logic (Model) 
AND return a view (JSP).

**Flow:** Request → Servlet → Model → View

**Benefits:** Better separation of concerns, more maintainable.

### What is Model 2 Front Controller architecture?

Model 2 Front Controller adds a central dispatcher servlet that routes all requests to appropriate handlers. 
This is the foundation of Spring MVC.

**Flow:** Request → Front Controller → Handler → Model → View

### Can you show an example controller method in Spring MVC?

```java
@Controller
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-detail";
    }
    
    @PostMapping
    public String createUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }
}
```

### Can you explain a simple flow in Spring MVC?

1. User sends HTTP request
2. DispatcherServlet receives the request
3. DispatcherServlet finds appropriate controller handler
4. Handler method executes business logic
5. Handler returns ModelAndView
6. ViewResolver resolves the view name to actual view
7. View is rendered with model data
8. Response is sent to user

### What is a ViewResolver?

ViewResolver converts logical view names returned by controllers into actual view objects. 
It determines which view technology to use (JSP, Thymeleaf, etc.).

```java
@Configuration
public class WebConfig {
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
```

### What is Model?

Model is a container for application data that needs to be displayed in the view. 
It's passed from controller to view.

```java
@GetMapping("/data")
public String getData(Model model) {
    model.addAttribute("key", "value");
    return "view";
}
```

### What is ModelAndView?

ModelAndView is a combined object containing both model data and view information. 
It's returned from controller methods.

```java
@GetMapping
public ModelAndView getAll() {
    ModelAndView mav = new ModelAndView("users-list");
    mav.addObject("users", userService.getAllUsers());
    return mav;
}
```

### What is a RequestMapping?

RequestMapping maps HTTP requests to controller methods. 
It supports various HTTP methods 
AND URL patterns.

```java
@RequestMapping(value = "/users", method = RequestMethod.GET)
public String getUsers() {
    return "users";
}

// Modern way
@GetMapping("/users")
public String getUsers() {
    return "users";
}
```

### What is Dispatcher Servlet?

DispatcherServlet is the central servlet in Spring MVC that receives all requests 
AND dispatches them to appropriate handlers. 
It's the front controller in the MVC architecture.

### How do you set up Dispatcher Servlet?

**XML Configuration:**
```xml
<servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring-mvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

**Spring Boot:** Automatically configured.

### What is a form backing object?

A form backing object is a POJO that holds form data. 
It's used in Spring MVC to bind form data to object properties.

```java
public class UserForm {
    private String firstName;
    private String lastName;
    private String email;
    // Getters and Setters
}

@PostMapping
public String submitForm(@ModelAttribute UserForm form) {
    // Process form
    return "success";
}
```

### How is validation done using Spring MVC?

```java
public class UserForm {
    @NotBlank(message = "First name required")
    private String firstName;
    
    @Email(message = "Invalid email")
    private String email;
}

@PostMapping
public String submitForm(@Valid @ModelAttribute UserForm form, 
                        BindingResult result) {
    if (result.hasErrors()) {
        return "form";
    }
    // Process valid form
    return "success";
}
```

### What is BindingResult?

BindingResult contains binding and validation errors from form submission. 
It's used to check and handle validation failures.

```java
@PostMapping
public String submit(@Valid UserForm form, BindingResult result) {
    if (result.hasErrors()) {
        return "form";
    }
    return "success";
}
```

### How do you map validation results to your view?

```html
<!-- In JSP or Thymeleaf -->
<form:form modelAttribute="userForm">
    <form:input path="firstName"/>
    <form:errors path="firstName" cssClass="error"/>
</form:form>

<!-- Or in Thymeleaf -->
<input th:field="*{firstName}">
<span th:if="${#fields.hasErrors('firstName')}" 
      th:errors="*{firstName}"></span>
```

### What are Spring Form Tags?

Spring Form Tags are JSP custom tags that simplify form handling 
BY automatically binding form fields to object properties.

```jsp
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form modelAttribute="user" method="post">
    <form:input path="firstName"/>
    <form:password path="password"/>
    <form:textarea path="bio"/>
    <form:checkbox path="active"/>
    <form:select path="role">
        <form:option value="ADMIN">Admin</form:option>
        <form:option value="USER">User</form:option>
    </form:select>
</form:form>
```

### What is a Path Variable?

Path Variable is a URL parameter extracted from the request path using `@PathVariable`.

```java
@GetMapping("/users/{id}")
public String getUser(@PathVariable Long id) {
    return "user";
}

// Named path variable
@GetMapping("/users/{userId}/posts/{postId}")
public String getPost(@PathVariable Long userId, 
                      @PathVariable Long postId) {
    return "post";
}
```

### What is a Model Attribute?

Model Attribute binds a method parameter or return value to a named model attribute.

```java
@GetMapping("/{id}")
public String getUser(@PathVariable Long id, Model model) {
    model.addAttribute("user", userService.getUser(id));
    return "user";
}

// Or use @ModelAttribute on return value
@ModelAttribute("statusList")
public List<String> getStatusList() {
    return Arrays.asList("ACTIVE", "INACTIVE");
}
```

### What is a Session Attribute?

Session Attribute stores attributes in the HTTP session across multiple requests.

```java
@Controller
@SessionAttributes("user")
public class UserController {
    
    @GetMapping
    public String getForm(Model model) {
        model.addAttribute("user", new User());
        return "form";
    }
}
```

### What is a init binder?

InitBinder customizes data binding for a controller. It's used for custom type conversion and formatting.

```java
@Controller
public class UserController {
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, 
            new CustomDateEditor(sdf, false));
    }
}
```

### How do you set default date format with Spring?

**In properties file:**
```properties
spring.mvc.date-format=yyyy-MM-dd
```

**With @InitBinder:**
```java
@InitBinder
public void initBinder(WebDataBinder binder) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    binder.registerCustomEditor(Date.class, 
        new CustomDateEditor(sdf, false));
}
```

### Why is Spring MVC so popular?

1. Clear separation of concerns (MVC)
2. Flexible request handling
3. Excellent validation support
4. Easy to test
5. Well-documented
6. Integrates seamlessly with other Spring modules
7. Supports multiple view technologies

---

## Spring Boot

### What is Spring Boot?

Spring Boot is a framework built on top of Spring 
THAT simplifies Spring application development 
BY providing auto-configuration, embedded servers, and production-ready features out of the box.

### What are the important Goals of Spring Boot?

1. Rapid development with minimal configuration
2. Provide production-ready features
3. Reduce boilerplate code
4. Enable microservices architecture
5. Provide opinionated defaults
6. Support multiple deployment options

### What are the important Features of Spring Boot?

1. Auto-configuration
2. Embedded servers (Tomcat, Jetty, Undertow)
3. Starter dependencies
4. Application properties externalization
5. Spring Boot Actuator (monitoring)
6. CLI (Command Line Interface)
7. Executable JAR packaging

### Compare Spring Boot vs Spring

| Aspect | Spring | Spring Boot |
|--------|--------|-----------|
| Configuration | Manual | Auto-configuration |
| Dependencies | Manual management | Starter POMs |
| Server | External (Tomcat, etc.) | Embedded |
| Setup Time | Long | Quick |
| Production Ready | Manual setup | Out of box |
| Deployment | WAR/EAR | Standalone JAR |

### Compare Spring Boot vs Spring MVC

Spring Boot is built on top of Spring and includes Spring MVC. Spring MVC is specifically for web applications, while Spring Boot is a complete framework for building any type of application.

| Aspect | Spring MVC | Spring Boot |
|--------|-----------|-----------|
| Scope | Web framework | Complete framework |
| Configuration | XML/Java | Auto-configuration |
| Server | External | Embedded |
| Use Case | Web apps | Any application type |

### What is the importance of @SpringBootApplication?

`@SpringBootApplication` is a convenience annotation that combines three annotations:
- `@Configuration` - Marks class as configuration
- `@EnableAutoConfiguration` - Enables auto-configuration
- `@ComponentScan` - Enables component scanning

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### What is Auto Configuration?

Auto-configuration automatically configures Spring application based on jar dependencies on the classpath. 
It uses `@ConditionalOn*` annotations to conditionally enable configurations.

```java
@Configuration
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(name = "spring.datasource.url")
public class DataSourceConfiguration {
    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }
}
```

### How can we find more information about Auto Configuration?

1. Check `spring-boot-autoconfigure` JAR in `META-INF/spring.factories`
2. Enable debug logging: `--debug` or `logging.level.org.springframework.boot.autoconfigure=DEBUG`
3. Check Spring Boot documentation
4. Use IDE auto-completion

### What is an embedded server? Why is it important?

An embedded server is a web server packaged within the application. 
Spring Boot includes embedded Tomcat, Jetty, or Undertow.

**Importance:**
- No external server deployment needed
- Simplified deployment (single JAR)
- Self-contained application
- Easier containerization and cloud deployment

### What is the default embedded server with Spring Boot?

**Tomcat** is the default embedded server in Spring Boot.

### What are the other embedded servers supported by Spring Boot?

1. **Jetty** - Lightweight, good for microservices
2. **Undertow** - High performance, low memory footprint
3. **Netty** - For reactive applications

```xml
<!-- Use Jetty instead of Tomcat -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

### What are Starter Projects?

Starter Projects are dependency descriptors that simplify Maven/Gradle configuration. 
They bundle commonly used dependencies together.

```xml
<!-- Instead of adding multiple spring-web, spring-mvc dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Can you give examples of important starter projects?

- `spring-boot-starter-web` - Web applications
- `spring-boot-starter-data-jpa` - JPA/Hibernate
- `spring-boot-starter-data-rest` - REST data services
- `spring-boot-starter-security` - Security
- `spring-boot-starter-test` - Testing
- `spring-boot-starter-actuator` - Monitoring
- `spring-boot-starter-webflux` - Reactive web

### What is Starter Parent?

Starter Parent is the parent POM that provides dependency and plugin management for Spring Boot projects. 
It simplifies configuration.

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.0</version>
</parent>
```

### What are the different things that are defined in Starter Parent?

1. **Dependency Management** - Versions for all dependencies
2. **Plugin Management** - Maven plugins configuration
3. **Properties** - Project encoding, Java version
4. **Default Plugin Execution** - Build configuration

### How does Spring Boot enforce common dependency management for all its Starter projects?

Spring Boot Starter Parent defines `<dependencyManagement>` with versions for all common dependencies. 
When you add starters or dependencies, they inherit these versions automatically.

### What is Spring Initializr?

Spring Initializr is a web-based tool (https://start.spring.io) that generates Spring Boot project structure 
with selected dependencies and build system.

### What is application.properties?

`application.properties` is the main configuration file for Spring Boot applications. 
It contains application-specific configuration in key-value format.

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.jpa.hibernate.ddl-auto=update
```

### What are some of the important things that can be customized in application.properties?

- Server port and servlet path
- Database connection settings
- JPA/Hibernate configuration
- Logging levels and format
- Jackson serialization settings
- Security configuration
- Cache settings
- Custom application properties

### How do you externalize configuration using Spring Boot?

1. **application.properties** - Default file
2. **application.yml** - YAML format
3. **Environment variables** - OS level
4. **Command-line arguments** - At startup
5. **System properties** - Java properties
6. **Profile-specific files** - application-{profile}.properties

### How can you add custom application properties using Spring Boot?

```properties
# In application.properties
app.name=MyApp
app.version=1.0
app.description=My Application

# In Java class
@Component
public class AppProperties {
    @Value("${app.name}")
    private String appName;
    
    @Value("${app.version}")
    private String appVersion;
}
```

### What is @ConfigurationProperties?

`@ConfigurationProperties` binds external configuration to a POJO with typed properties.

```java
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String name;
    private String version;
    private String description;
    
    // Getters and Setters
}
```

### What is a profile?

A profile is a way to group configurations for different environments (dev, test, prod). 
Each profile has its own properties file.

```
application.properties - Common
application-dev.properties - Development
application-test.properties - Testing
application-prod.properties - Production
```

### How do you define beans for a specific profile?

```java
@Configuration
@Profile("dev")
public class DevConfiguration {
    @Bean
    public DataSource dataSource() {
        // Development datasource
    }
}

@Configuration
@Profile("prod")
public class ProdConfiguration {
    @Bean
    public DataSource dataSource() {
        // Production datasource
    }
}
```

### How do you create application configuration for a specific profile?

```properties
# application.properties
spring.profiles.active=dev

# Or
# application-dev.properties
server.port=9090
spring.datasource.url=jdbc:mysql://localhost/devdb

# application-prod.properties
server.port=8080
spring.datasource.url=jdbc:mysql://prod-server/proddb
```

### How do you have different configuration for different environments?

1. Create profile-specific property files
2. Set `spring.profiles.active` property
3. Use `@Profile` annotation for profile-specific beans
4. Use command-line arguments: `--spring.profiles.active=prod`

### What is Spring Boot Actuator?

Spring Boot Actuator provides production-ready features like monitoring and management endpoints. 
It exposes metrics and allows application monitoring.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### How do you monitor web services using Spring Boot Actuator?

Access endpoints like:
- `/actuator/health` - Application health
- `/actuator/metrics` - Application metrics
- `/actuator/env` - Environment properties
- `/actuator/beans` - Registered beans
- `/actuator/threaddump` - Thread dump

### How do you find more information about your application environment using Spring Boot?

1. Use `/actuator/env` endpoint to see environment properties
2. Enable debug logging
3. Check application logs
4. Use Spring Boot Actuator endpoints

### What is a CommandLineRunner?

CommandLineRunner is an interface that executes code after Spring application startup. 
Useful for initialization tasks.

```java
@Component
public class DataLoader implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // Initialization logic
        System.out.println("Application started!");
    }
}
```

---

## Database Connectivity - JDBC, Spring JDBC & JPA

### What is Spring JDBC? How is different from JDBC?

Spring JDBC is a wrapper over JDBC that eliminates boilerplate code. 
It handles exceptions, resource management, and provides template classes.

| Aspect | JDBC | Spring JDBC |
|--------|------|-----------|
| Boilerplate | High | Low |
| Exception Handling | Manual | Automatic |
| Resource Management | Manual | Automatic |
| Ease of Use | Difficult | Easy |

### What is a JdbcTemplate?

JdbcTemplate is a Spring class that simplifies JDBC operations by providing methods 
for common database operations.

```java
@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    }
    
    public void saveUser(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail());
    }
}
```

### What is a RowMapper?

RowMapper is an interface that maps each row of a ResultSet to a Java object.

```java
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        return user;
    }
}
```

### What is JPA?

JPA (Java Persistence API) is a Java standard for object-relational mapping (ORM). 
It defines how Java objects map to database tables.

### What is Hibernate?

Hibernate is the most popular JPA implementation. 
It provides ORM functionality with advanced features 
LIKE caching, lazy loading, and query optimization.

### How do you define an entity in JPA?

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_name", nullable = false, unique = true)
    private String username;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Transient
    private String tempField; // Not persisted
}
```

### What is an Entity Manager?

EntityManager is the JPA interface for managing entities. 
It provides methods for CRUD operations and querying.

```java
@PersistenceContext
private EntityManager entityManager;

public void saveUser(User user) {
    entityManager.persist(user);
}

public User getUserById(Long id) {
    return entityManager.find(User.class, id);
}

public void deleteUser(Long id) {
    User user = entityManager.find(User.class, id);
    entityManager.remove(user);
}
```

### What is a Persistence Context?

Persistence Context is a managed environment where entities are tracked. 
It's responsible for detecting changes 
AND persisting them to the database.

### How do you map relationships in JPA?

JPA provides annotations for mapping relationships:
- `@OneToOne` - One-to-one relationship
- `@OneToMany` - One-to-many relationship
- `@ManyToOne` - Many-to-one relationship
- `@ManyToMany` - Many-to-many relationship

### What are the different types of relationships in JPA?

1. **One-to-One** - Single entity related to single entity
2. **One-to-Many** - Single entity related to multiple entities
3. **Many-to-One** - Multiple entities related to single entity
4. **Many-to-Many** - Multiple entities related to multiple entities

### How do you define One to One Mapping in JPA?

```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}

@Entity
public class Profile {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne(mappedBy = "profile")
    private User user;
}
```

### How do you define One to Many Mapping in JPA?

```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
}

@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
```

### How do you define Many to Many Mapping in JPA?

```java
@Entity
public class Student {
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();
}

@Entity
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();
}
```

### How do you define a datasource in a Spring Context?

**In application.properties:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**In Java Configuration:**
```java
@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .url("jdbc:mysql://localhost:3306/mydb")
            .username("root")
            .password("password")
            .build();
    }
}
```

### What is the use of persistence.xml?

`persistence.xml` is the JPA configuration file that defines persistence units. 
It's located in `META-INF` folder.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence">
    <persistence-unit name="myPU">
        <class>com.example.User</class>
        <properties>
            <property name="hibernate.dialect" 
                value="org.hibernate.dialect.MySQL8Dialect"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
        </properties>
    </persistence-unit>
</persistence>
```

### How do you configure Entity Manager Factory and Transaction Manager?

**In Java Configuration:**
```java
@Configuration
public class PersistenceConfig {
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = 
            new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.entity");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return emf;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
```

### How do you define transaction management for Spring – Hibernate integration?

```java
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    
    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}

// Use in service
@Service
public class UserService {
    @Transactional
    public void saveUser(User user) {
        // Database operation
    }
    
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        // Query only
    }
}
```

---

## Spring Data

### What is Spring Data?

Spring Data is a Spring project that provides abstraction over data access layers. 
It supports various data storage technologies through a unified API.

### What is the need for Spring Data?

1. Reduces boilerplate code for data access
2. Provides consistent API across technologies
3. Supports multiple data stores
4. Provides query methods and specifications

### What is Spring Data JPA?

Spring Data JPA provides repository abstraction for JPA. 
It automatically implements common CRUD operations 
AND query methods based on method naming conventions.

```java
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByEmailContaining(String email);
}
```

### What is a CrudRepository?

CrudRepository provides basic CRUD operations for an entity.

```java
public interface UserRepository extends CrudRepository<User, Long> {
    // Provides: save, findById, findAll, delete, etc.
}

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public User save(User user) {
        return userRepository.save(user);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
```

### What is a PagingAndSortingRepository?

PagingAndSortingRepository extends CrudRepository 
AND adds pagination and sorting capabilities.

```java
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    // Provides: findAll(Pageable), findAll(Sort)
}

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public Page<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return userRepository.findAll(pageable);
    }
}
```

---

## Unit Testing

### How does Spring Framework Make Unit Testing Easy?

Spring Framework provides:
1. Dependency injection makes mocking dependencies easy
2. Testing annotations for loading context
3. MockMvc for testing controllers
4. TestRestTemplate for integration testing
5. Mockito integration
6. Easy bean mocking with @MockBean

### What is Mockito?

Mockito is a mocking framework for unit testing. It allows creating mock objects and verifying interactions with them.

```java
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    public void testGetUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        User result = userService.getUserById(1L);
        
        assertNotNull(result);
        verify(userRepository).findById(1L);
    }
}
```

### What is your favorite mocking framework?

Mockito is the most popular and widely used mocking framework in Java  
IT HAS simple API and powerful features.

### How do you do mock data with Mockito?

```java
@Mock
private UserRepository userRepository;

@Test
public void testMockData() {
    // Create mock data
    User mockUser = new User();
    mockUser.setId(1L);
    mockUser.setName("John");
    
    // Set up mock behavior
    when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
    
    // Use mock
    Optional<User> result = userRepository.findById(1L);
    assertTrue(result.isPresent());
    assertEquals("John", result.get().getName());
}
```

### What are the different mocking annotations that you worked with?

1. `@Mock` - Creates a mock object
2. `@InjectMocks` - Injects mocks into a target object
3. `@Spy` - Creates a partial mock (real method calls)
4. `@Captor` - Captures arguments passed to mock methods
5. `@MockBean` - Spring-specific mock bean annotation

### What is MockMvc?

MockMvc is a testing framework for testing Spring MVC controllers without starting a server. 
It simulates HTTP requests and verifies responses.

```java
@WebMvcTest(UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void testGetUser() throws Exception {
        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"));
    }
}
```

### What is @WebMvcTest?

`@WebMvcTest` is an annotation that loads only Spring MVC layer for testing a specific controller. 
It doesn't load full application context.

```java
@WebMvcTest(UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    // Test methods
}
```

### What is @MockBean?

`@MockBean` replaces a bean in the application context with a mock. 
It's specific to Spring testing.

```java
@SpringBootTest
public class IntegrationTest {
    
    @MockBean
    private UserRepository userRepository;
    
    @Test
    public void test() {
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(new User()));
    }
}
```

### How do you write a unit test with MockMVC?

```java
@WebMvcTest(UserController.class)
public class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    public void testGetUserSuccess() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("John");
        
        when(userService.getUserById(1L)).thenReturn(user);
        
        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John"))
            .andDo(print());
    }
    
    @Test
    public void testCreateUser() throws Exception {
        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"John\"}"))
            .andExpect(status().isCreated());
    }
}
```

### What is JSONAssert?

JSONAssert is a library for asserting JSON responses in tests. 
It allows flexible JSON comparison.

```java
@Test
public void testJsonResponse() throws JSONException {
    String expected = "{\"name\": \"John\", \"age\": 30}";
    String actual = callApi();
    
    JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
}
```

### How do you write an integration test with Spring Boot?

```java
@SpringBootTest
public class IntegrationTest {
    
    @Autowired
    private WebApplicationContext context;
    
    @Autowired
    private UserRepository userRepository;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    
    @Test
    @Transactional
    public void testUserFlow() throws Exception {
        User user = new User();
        user.setName("John");
        
        mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(user)))
            .andExpect(status().isCreated());
    }
}
```

### What is @SpringBootTest?

`@SpringBootTest` loads the entire application context for integration testing. 
It starts the real server and allows end-to-end testing.

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    @Test
    public void testEndpoint() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:" + port + "/users";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

### What is @LocalServerPort?

`@LocalServerPort` injects the actual port number that the embedded server is running on during testing.

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PortTest {
    
    @LocalServerPort
    private int port;
    
    @Test
    public void test() {
        String url = "http://localhost:" + port + "/api/users";
        // Use the URL
    }
}
```

### What is TestRestTemplate?

TestRestTemplate is a convenience class for integration testing 
that provides a simple way to test REST endpoints.

```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestApiTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testGetUsers() {
        ResponseEntity<User[]> response = restTemplate
            .getForEntity("/users", User[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("John");
        
        ResponseEntity<User> response = restTemplate
            .postForEntity("/users", user, User.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
```

---

## AOP (Aspect-Oriented Programming)

### What are cross cutting concerns?

Cross-cutting concerns are functionality that affects multiple parts of an application 
AND is not part of the core business logic. Examples:

- Logging
- Security
- Transaction management
- Performance monitoring
- Exception handling

### How do you implement cross cutting concerns in a web application?

1. **AOP (Aspect-Oriented Programming)** - Using aspects and pointcuts
2. **Filters** - Servlet filters for HTTP concerns
3. **Interceptors** - HandlerInterceptors for Spring MVC
4. **Decorators** - Wrapper classes

### If you would want to log every request to a web application, what are the options you can think of?

1. **Servlet Filter** - Log at servlet level
2. **Interceptor** - Log at Spring MVC level
3. **AOP** - Create a pointcut for all controller methods
4. **Actuator** - Use Spring Boot Actuator HTTP Trace

### If you would want to track performance of every request, what options can you think of?

1. **Interceptor** - Track time between preHandle and postHandle
2. **AOP** - Create aspect around controller methods
3. **Filter** - Track time in servlet filter
4. **Actuator** - Use metrics endpoints

### What is an Aspect and Pointcut in AOP?

- **Aspect** - A module that encapsulates cross-cutting concerns
- **Pointcut** - An expression that matches join points (method executions, etc.)
- **Join Point** - A specific point in program execution

```java
@Aspect
@Component
public class LoggingAspect {
    
    // Pointcut expression
    @Pointcut("execution(* com.example.service.*.*(..))")
    public void serviceLayer() {}
    
    // Advice
    @Before("serviceLayer()")
    public void logBefore() {
        System.out.println("Method execution started");
    }
}
```

### What are the different types of AOP advices?

1. **@Before** - Executes before method execution
2. **@After** - Executes after method execution
3. **@AfterReturning** - Executes after successful return
4. **@AfterThrowing** - Executes if exception is thrown
5. **@Around** - Executes before and after method

```java
@Aspect
@Component
public class PerformanceAspect {
    
    @Around("execution(* com.example.service.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) 
            throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        System.out.println("Executed in: " + duration + "ms");
        return result;
    }
    
    @Before("execution(* com.example.service.*.save*(..))")
    public void beforeSave(JoinPoint joinPoint) {
        System.out.println("Saving: " + joinPoint.getSignature());
    }
    
    @AfterThrowing("execution(* com.example.service.*.*(..))")
    public void handleException() {
        System.out.println("Exception occurred");
    }
}
```

### What is weaving?

Weaving is the process of applying aspects to target objects to create advised objects. 
Spring uses runtime weaving with dynamic proxies.

### Compare Spring AOP vs AspectJ

| Aspect | Spring AOP | AspectJ |
|--------|-----------|---------|
| Implementation | Runtime proxies | Compile-time weaving |
| Performance | Slower | Faster |
| Complexity | Simple | Complex |
| Join Points | Method execution | All program points |
| Learning Curve | Easy | Steep |

```java
// Spring AOP - Runtime
@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.example.*.*(..))")
    public void log() {
        System.out.println("Logging");
    }
}

// AspectJ - Compile-time
public aspect LoggingAspect {
    pointcut logExecution(): execution(* com.example.*.*(..));
    
    before(): logExecution() {
        System.out.println("Logging");
    }
}
```

---

## SOAP Web Services

### What is a Web Service?

A web service is a software system designed to support interoperable communication over a network. It uses standardized protocols (HTTP, SOAP, REST) for communication.

### What is SOAP Web Service?

SOAP (Simple Object Access Protocol) is a protocol for exchanging structured information over web services. It uses XML for message format.

### What is SOAP?

SOAP is a lightweight protocol for exchanging structured information. Key characteristics:
- Based on XML
- Platform and language independent
- Uses HTTP/HTTPS transport
- Defines strict rules for message structure

### What is a SOAP Envelope?

SOAP Envelope is the root element of a SOAP message. It contains header and body elements.

```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap-envelope/">
    <soap:Header>
        <!-- Header content -->
    </soap:Header>
    <soap:Body>
        <!-- Body content -->
    </soap:Body>
</soap:Envelope>
```

### What is SOAP Header and SOAP Body?

- **SOAP Header** - Optional element containing metadata, authentication, routing information
- **SOAP Body** - Contains the actual SOAP message and data to be exchanged

### Can you give an example of SOAP Request and SOAP Response?

**SOAP Request:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap-envelope/">
    <soap:Body>
        <GetUser xmlns="http://example.com/service">
            <userId>123</userId>
        </GetUser>
    </soap:Body>
</soap:Envelope>
```

**SOAP Response:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap-envelope/">
    <soap:Body>
        <GetUserResponse xmlns="http://example.com/service">
            <user>
                <id>123</id>
                <name>John</name>
                <email>john@example.com</email>
            </user>
        </GetUserResponse>
    </soap:Body>
</soap:Envelope>
```

### What is a SOAP Header? What kind of information is sent in a SOAP Header?

SOAP Header contains metadata like:
- Authentication credentials
- Transaction IDs
- Routing information
- Timestamps
- Signature information

### Can you give an example of a SOAP Header with Authentication information?

```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap-envelope/">
    <soap:Header>
        <Authentication xmlns="http://example.com/service">
            <Username>user@example.com</Username>
            <Password>password123</Password>
            <Timestamp>2024-02-10T10:30:00Z</Timestamp>
        </Authentication>
    </soap:Header>
    <soap:Body>
        <!-- Body content -->
    </soap:Body>
</soap:Envelope>
```

### What is WSDL (Web Service Definition Language)?

WSDL is an XML-based language for describing web services. It defines:
- Operations provided by service
- Message formats
- Binding protocols
- Service endpoints

### What are the different parts of a WSDL?

1. **Types** - Data types used in messages
2. **Message** - Abstract definition of data
3. **PortType** - Collection of operations
4. **Binding** - Protocol and data format specification
5. **Service** - Collection of endpoints
6. **Port** - Individual endpoint

### What is Contract First Approach?

Contract First approach means defining the WSDL contract before implementing the service. 
It ensures the service contract is stable and clients can code against it immediately.

**Steps:**
1. Design WSDL
2. Generate server stubs from WSDL
3. Implement business logic
4. Clients generate stubs from same WSDL

### What is an XSD?

XSD (XML Schema Definition) is an XML standard for defining structure and constraints of XML documents. 
It defines data types used in WSDL messages.

```xml
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <xs:element name="User">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="id" type="xs:integer"/>
                <xs:element name="name" type="xs:string"/>
                <xs:element name="email" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
</xs:schema>
```

### Can you give an example of an XSD?

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" 
           targetNamespace="http://example.com/user"
           xmlns:tns="http://example.com/user">
    
    <xs:complexType name="User">
        <xs:sequence>
            <xs:element name="id" type="xs:long"/>
            <xs:element name="firstName" type="xs:string" minOccurs="1" maxOccurs="1"/>
            <xs:element name="lastName" type="xs:string" minOccurs="1" maxOccurs="1"/>
            <xs:element name="email" type="xs:string" minOccurs="1" maxOccurs="1"/>
        </xs:sequence>
    </xs:complexType>
    
    <xs:element name="GetUserRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="userId" type="xs:long"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>
</xs:schema>
```

### What is JAXB?

JAXB (Java Architecture for XML Binding) is a technology that binds XML to Java classes. 
It provides tools to convert XML to Java objects and vice versa.

### How do you configure a JAXB Plugin?

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>jaxb2-maven-plugin</artifactId>
    <version>2.5.0</version>
    <executions>
        <execution>
            <id>xjc</id>
            <goals>
                <goal>xjc</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <sources>
            <source>src/main/resources/schema</source>
        </sources>
        <packageName>com.example.generated</packageName>
    </configuration>
</plugin>
```

### What is an Endpoint?

In SOAP services, an Endpoint is the service implementation class that handles SOAP requests and returns responses.

### Can you show an example endpoint written with Spring Web Services?

```java
@Endpoint
public class UserEndpoint {
    
    private final UserService userService;
    
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }
    
    @PayloadRoot(namespace = "http://example.com/user", 
                 localPart = "GetUserRequest")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserRequest request) {
        User user = userService.getUserById(request.getUserId());
        
        GetUserResponse response = new GetUserResponse();
        response.setUser(mapToDto(user));
        return response;
    }
    
    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
```

### What is a MessageDispatcherServlet?

MessageDispatcherServlet is similar to DispatcherServlet but specifically for SOAP web services. It routes SOAP requests to appropriate endpoints.

### How do you configure a MessageDispatcherServlet?

```xml
<servlet>
    <servlet-name>soap</servlet-name>
    <servlet-class>
        org.springframework.ws.transport.http.MessageDispatcherServlet
    </servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring-ws.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>soap</servlet-name>
    <url-pattern>/ws/*</url-pattern>
</servlet-mapping>
```

### How do you generate a WSDL using Spring Web Services?

Spring Web Services automatically generates WSDL from XSD:

```java
@Configuration
@EnableWs
public class WebServiceConfig extends WsConfigurerAdapter {
    
    @Bean
    public DefaultWsdl11Definition wsdl11Definition(XsdSchema schema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setSchema(schema);
        wsdl11Definition.setServiceName("UserService");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://example.com/user");
        return wsdl11Definition;
    }
    
    @Bean
    public XsdSchema schema() {
        return new SimpleXsdSchema(new ClassPathResource("user.xsd"));
    }
}
```

Access WSDL at: `http://localhost:8080/ws/user.wsdl`

### How do you implement error handling for SOAP Web Services?

```java
@Endpoint
public class UserEndpoint {
    
    @PayloadRoot(namespace = "http://example.com/user", 
                 localPart = "GetUserRequest")
    @ResponsePayload
    public Object getUser(@RequestPayload GetUserRequest request,
                         SoapHeader soapHeader) throws SoapFault {
        try {
            User user = userService.getUserById(request.getUserId());
            GetUserResponse response = new GetUserResponse();
            response.setUser(mapToDto(user));
            return response;
        } catch (UserNotFoundException e) {
            SoapFault fault = new SoapFault(
                "User not found", 
                SoapFault.CLIENT
            );
            throw fault;
        }
    }
}
```

### What is a SOAP Fault?

A SOAP Fault is a SOAP element that contains error information when a SOAP request fails. It includes fault code, string, and details.

```xml
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap-envelope/">
    <soap:Body>
        <soap:Fault>
            <faultcode>soap:Server</faultcode>
            <faultstring>User not found</faultstring>
            <detail>
                <message>User with ID 999 does not exist</message>
            </detail>
        </soap:Fault>
    </soap:Body>
</soap:Envelope>
```

---

## RESTful Web Services

### What is REST?

REST (Representational State Transfer) is an architectural style for building web services using HTTP. It treats all resources as entities with unique URIs and uses HTTP methods for operations.

**Key Principles:**
- Resources are identified by URIs
- Operations use HTTP methods (GET, POST, PUT, DELETE)
- Stateless communication
- Representation of resources
- HATEOAS (optional)

### What are the key concepts in designing RESTful API?

1. **Resources** - Nouns (users, posts, comments)
2. **HTTP Methods** - Verbs (GET, POST, PUT, DELETE)
3. **URIs** - Unique identifiers for resources
4. **Status Codes** - HTTP response codes
5. **Content Types** - JSON, XML
6. **Statelessness** - No server-side session
7. **Caching** - HTTP caching headers

### What are the Best Practices of RESTful Services?

1. Use nouns for resources, not verbs
2. Use HTTP methods correctly
3. Use proper HTTP status codes
4. Implement versioning for APIs
5. Provide meaningful error messages
6. Use pagination for large datasets
7. Document your API well
8. Implement security (authentication, authorization)
9. Use HATEOAS for discoverability
10. Support content negotiation

### Can you show the code for an example Get Resource method with Spring REST?

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
```

### What happens when we return a bean from a Request Mapping Method?

Spring automatically converts the bean to JSON (or other format based on Accept header) 
USING HttpMessageConverter. By default, Jackson is used for JSON serialization.

```java
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    // User object is automatically converted to JSON
    return userService.getUserById(id);
}

// Response:
// {
//   "id": 1,
//   "name": "John",
//   "email": "john@example.com"
// }
```

### What is GetMapping and what are the related methods available in Spring MVC?

`GetMapping` is a shorthand for `@RequestMapping(method = RequestMethod.GET)`. Related methods:

- `@GetMapping` - HTTP GET
- `@PostMapping` - HTTP POST
- `@PutMapping` - HTTP PUT
- `@DeleteMapping` - HTTP DELETE
- `@PatchMapping` - HTTP PATCH

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) { }
    
    @PostMapping
    public User createUser(@RequestBody User user) { }
    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) { }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) { }
    
    @PatchMapping("/{id}")
    public User partialUpdate(@PathVariable Long id, @RequestBody User user) { }
}
```

### Can you show the code for an example Post Resource method with Spring REST?

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    User createdUser = userService.saveUser(user);
    return ResponseEntity
        .created(URI.create("/api/users/" + createdUser.getId()))
        .body(createdUser);
}
```

### What is the appropriate HTTP Response Status for successful execution of a Resource Creation?

**201 (Created)** is the appropriate status code for successful resource creation.

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public User createUser(@RequestBody User user) {
    return userService.saveUser(user);
}

// Or with ResponseEntity
@PostMapping
public ResponseEntity<User> createUser(@RequestBody User user) {
    User savedUser = userService.saveUser(user);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(savedUser);
}
```

### Why do we use ResponseEntity in a RESTful Service?

ResponseEntity allows full control over HTTP response:
- Status code
- Headers
- Body

```java
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    Optional<User> user = userService.getUserById(id);
    
    return user.map(u -> ResponseEntity
        .ok()
        .header("X-Custom-Header", "value")
        .body(u))
        .orElse(ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null));
}
```

### What is HATEOAS?

HATEOAS (Hypermedia As The Engine Of Application State) 
adds navigation links in API responses, allowing clients to discover available actions dynamically.

### Can you give an Example Response for HATEOAS?

```json
{
    "id": 1,
    "name": "John",
    "email": "john@example.com",
    "_links": {
        "self": {
            "href": "http://api.example.com/users/1"
        },
        "all-users": {
            "href": "http://api.example.com/users"
        },
        "update": {
            "href": "http://api.example.com/users/1",
            "method": "PUT"
        },
        "delete": {
            "href": "http://api.example.com/users/1",
            "method": "DELETE"
        }
    }
}
```

### How do we implement it using Spring?

Spring HATEOAS library provides support for HATEOAS:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public EntityModel<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        
        return EntityModel.of(user,
            linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
            linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
    }
    
    @GetMapping
    public CollectionModel<EntityModel<User>> getAllUsers() {
        List<EntityModel<User>> users = userService.getAllUsers()
            .stream()
            .map(user -> EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUser(user.getId()))
                    .withSelfRel()))
            .collect(Collectors.toList());
        
        return CollectionModel.of(users,
            linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }
}
```

### How do you document RESTful web services?

1. **Swagger/OpenAPI** - Interactive documentation
2. **Postman** - API collection and documentation
3. **Spring REST Docs** - Test-based documentation
4. **README** - Manual documentation

### Can you give a brief idea about Swagger Documentation?

Swagger is a specification for describing REST APIs. 
It provides an interactive UI where developers can explore and test API endpoints.

### How do you automate generation of Swagger Documentation from RESTful Web Services?

Use SpringFox library:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.0</version>
</dependency>
```

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

### How do you add custom information to Swagger Documentation generated from RESTful Web Services?

```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management APIs")
public class UserController {
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<User> getUser(
        @PathVariable 
        @Parameter(description = "User ID", required = true)
        Long id) {
        // Implementation
    }
    
    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user")
    @ApiResponse(responseCode = "201", description = "User created")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Implementation
    }
}
```

### What is Swagger-UI?

Swagger-UI is an interactive web interface that displays API documentation generated from Swagger/OpenAPI specification. It allows developers to explore and test API endpoints directly from the browser.

### What is "Representation" of a Resource?

Representation is how a resource is displayed or transmitted. 
The same resource can have multiple representations (JSON, XML, HTML, etc.).

```java
// Same resource, different representations
@GetMapping(value = "/{id}", produces = "application/json")
public User getUserJson(@PathVariable Long id) { }

@GetMapping(value = "/{id}", produces = "application/xml")
public User getUserXml(@PathVariable Long id) { }
```

### What is Content Negotiation?

Content negotiation is the process where client and server agree on the representation format. 
The client specifies desired format via Accept header.

```
GET /api/users/1 HTTP/1.1
Accept: application/json
```

### Which HTTP Header is used for Content Negotiation?

**Accept** header is used by client to specify desired format. 
**Content-Type** header is used by server to indicate response format.

```
Request:
Accept: application/json, application/xml

Response:
Content-Type: application/json
```

### How do we implement it using Spring Boot?

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml"})
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
```

Spring automatically chooses format based on Accept header.

### How do you add XML support to your RESTful Services built with Spring Boot?

Add Jackson XML dependency:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

Annotate your entity:

```java
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    @XmlElement
    private Long id;
    
    @XmlElement
    private String name;
}
```

### How do you implement Exception Handling for RESTFul Web Services?

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setPath(request.getRequestURI());
        error.setTimestamp(LocalDateTime.now());
        
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(
            MethodArgumentNotValidException ex) {
        
        ErrorResponse error = new ErrorResponse();
        error.setMessage("Validation failed");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setErrors(ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList()));
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }
}
```

### What are the best practices related to Exception Handling with respect to RESTful Web Services?

1. Use appropriate HTTP status codes
2. Provide meaningful error messages
3. Use @ControllerAdvice for global exception handling
4. Log errors for debugging
5. Don't expose internal implementation details
6. Return error in consistent format
7. Validate input before processing
8. Use specific exception classes

### What are the different error status that you would return in RESTful Web Services?

- **200 OK** - Successful request
- **201 Created** - Successful resource creation
- **204 No Content** - Successful with no response body
- **400 Bad Request** - Invalid request data
- **401 Unauthorized** - Authentication required
- **403 Forbidden** - Authenticated but not authorized
- **404 Not Found** - Resource doesn't exist
- **409 Conflict** - Resource conflict
- **500 Internal Server Error** - Server error
- **503 Service Unavailable** - Service down

### How would you implement them using Spring Boot?

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        // 200 OK or 404 Not Found
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        // 201 Created
        User savedUser = userService.saveUser(user);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(savedUser);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // 204 No Content
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

### What HTTP Response Status do you return for validation errors?

**400 (Bad Request)** is the appropriate status code for validation errors.

```java
@PostMapping
public ResponseEntity<ErrorResponse> createUser(
        @Valid @RequestBody User user, BindingResult result) {
    
    if (result.hasErrors()) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("Validation failed"));
    }
    
    User savedUser = userService.saveUser(user);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(savedUser);
}
```

### How do you handle Validation Errors with RESTful Web Services?

```java
@ControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error -> errors.put(
                error.getField(), 
                error.getDefaultMessage()));
        
        ValidationErrorResponse response = new ValidationErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Validation failed");
        response.setErrors(errors);
        
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }
}

public class ValidationErrorResponse {
    private int status;
    private String message;
    private Map<String, String> errors;
    
    // Getters and setters
}
```

### Why do we need Versioning for RESTful Web Services?

Versioning allows:
- Backward compatibility when API changes
- Support for multiple API versions simultaneously
- Gradual migration of clients
- Better API management and evolution

### What are the versioning options that are available?

1. **URI Versioning** - Version in URL path
2. **Request Header Versioning** - Version in header
3. **Parameter Versioning** - Version as query parameter
4. **Content Type Versioning** - Version in Content-Type

### How do you implement Versioning for RESTful Web Services?

**URI Versioning:**
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserControllerV1 {
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) { }
}

@RestController
@RequestMapping("/api/v2/users")
public class UserControllerV2 {
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) { }
}
```

**Header Versioning:**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(value = "/{id}", headers = "X-API-Version=1")
    public User getUserV1(@PathVariable Long id) { }
    
    @GetMapping(value = "/{id}", headers = "X-API-Version=2")
    public UserDTO getUserV2(@PathVariable Long id) { }
}
```

**Parameter Versioning:**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping(value = "/{id}", params = "version=1")
    public User getUserV1(@PathVariable Long id) { }
    
    @GetMapping(value = "/{id}", params = "version=2")
    public UserDTO getUserV2(@PathVariable Long id) { }
}
```

---

This comprehensive guide covers all major Spring Framework concepts from the Udemy course. Each answer provides concise explanations with practical code examples for interview preparation.

