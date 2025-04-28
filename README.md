# DataSentry

**DataSentry** is a lightweight and extensible Spring Boot framework designed to detect sensitive data in HTTP responses. It helps developers identify and log sensitive information such as phone numbers, bank card numbers, email addresses, and more, ensuring data privacy and compliance with regulations like GDPR and CCPA. DataSentry is built with a modular architecture, allowing easy integration into Spring Boot applications and customization for specific use cases.

## Features

- **Sensitive Data Detection**: Automatically identifies sensitive data types, including:
    - Mobile numbers
    - ID cards
    - Bank card numbers
    - Email addresses
    - Chinese names
    - Chinese addresses
    - Fixed phone numbers
- **Flexible Filtering**: Supports configurable request filtering based on:
    - Sampling rate for probabilistic processing
    - URI whitelist to exclude specific endpoints
    - Request rate limiting to control processing frequency
    - Caching to avoid redundant detection for recently processed requests
- **Seamless Spring Boot Integration**: Provided as a Spring Boot Starter, with auto-configuration and minimal setup.
- **Extensible Design**: Chain-based architecture for request filtering and data detection, making it easy to add custom detectors or filters.
- **Customizable Output**: Logs sensitive data detections via SLF4J or custom output implementations (e.g., console, file, or external systems).
- **Performance Optimized**: Uses caching and efficient regex-based detection to minimize overhead.

## Project Structure

DataSentry is organized into three main modules:

- `data-sentry-core`: Core library containing the detection logic, filtering chains, and utilities.
- `data-sentry-springboot-starter`: Spring Boot Starter for easy integration with Spring applications.
- `data-sentry-sample`: A sample application demonstrating how to use DataSentry.

```
data-sentry/
├── data-sentry-core/                # Core library
├── data-sentry-springboot-starter/  # Spring Boot Starter
├── data-sentry-sample/              # Sample application
└── pom.xml                          # Parent POM
```

## Getting Started

### Prerequisites

- **Java**: 8 or higher
- **Maven**: 3.6.0 or higher
- **Spring Boot**: 2.0.9.RELEASE or compatible versions
- **Dependencies**: Managed via Maven (see `pom.xml`)

### Installation

1. **Add Dependency**

   Add the `data-sentry-springboot-starter` dependency to your Spring Boot project's `pom.xml`:

   ```xml
   <dependency>
       <groupId>io.github.kanshanos</groupId>
       <artifactId>data-sentry-springboot-starter</artifactId>
       <version>${latest}</version>
   </dependency>
   ```

   *Note*: Replace `latest` with the latest release version once published to a Maven repository.

2. **Configure Application**

   Add DataSentry configuration to your `application.yml` or `application.properties`:

   ```yaml
   kanshanos:
     datasentry:
       enabled: true
       sampling-rate: 1.0
       exclude-path-patterns:
         - /public/**
       request-rate-window-interval-seconds: 600
       sensitive-detection-hit-window-interval-seconds: 86400
   ```

   See Configuration for detailed options.

3. **Run the Application**

   DataSentry will automatically intercept HTTP responses, detect sensitive data, and log findings via SLF4J or a custom `ContextOutput` implementation.

### Example Usage

The `data-sentry-sample` module provides a working example. To run it:

1. **Clone the Repository**

   ```bash
   git clone https://github.com/KanshanOS/data-sentry.git
   cd data-sentry
   ```

2. **Build the Project**

   ```bash
   mvn clean install
   ```

3. **Run the Sample Application**

   ```bash
   cd data-sentry-sample
   mvn spring-boot:run
   ```

4. **Test the Endpoint**

   Access `http://localhost:8080/user/get/1` to see DataSentry in action. The response will contain a sample user object, and the console will log detected sensitive data, such as:

   ```
   Sentry Data URI :[GET] /user/get/{id}, Senses List : {"type":"mobile","name":"phone","data":"15912341234"},{"type":"idcard","name":"idcard","data":"310112199009015335"},{"type":"email","name":"email","data":"123456@gmail.com"},{"type":"chinese_address","name":"address","data":"上海市浦东新区世纪大道1号"},{"type":"chinese_name","name":"name","data":"张三"}
   ```

## Configuration

DataSentry is highly configurable via `application.yml` or `application.properties`. Below are the available properties:

| Property | Description | Default |
| --- | --- | --- |
| `kanshanos.datasentry.enabled` | Enable or disable DataSentry | `true` |
| `kanshanos.datasentry.sampling-rate` | Probability of processing a request (0.0 to 1.0) | `0.5` |
| `kanshanos.datasentry.exclude-path-patterns` | List of URI patterns to exclude from detection | `[]` |
| `kanshanos.datasentry.request-rate-window-interval-seconds` | Minimum interval (seconds) between processing the same request | `600` (10 minutes) |
| `kanshanos.datasentry.sensitive-detection-hit-window-interval-seconds` | Cache duration (seconds) for requests with detected sensitive data | `86400` (7 days) |

Example configuration:

```yaml
kanshanos:
  datasentry:
    enabled: true
    sampling-rate: 0.8
    exclude-path-patterns:
      - /api/public/**
      - /health
    request-rate-window-interval-seconds: 300
    sensitive-detection-hit-window-interval-seconds: 3600
```

## Customization

### Adding Custom Detectors

To detect additional sensitive data types, extend `AbstractSensitiveDataDetector` or `RegexBasedDetector`:

1. **Create a Custom Detector**

   ```java
   package com.example;
   
   import io.github.kanshanos.datasentry.chain.data.AbstractSensitiveDataDetector;
   import io.github.kanshanos.datasentry.chain.data.DetectorConfig;
   import io.github.kanshanos.datasentry.context.SensitiveDataItem;
   
   public class PassportDetector extends AbstractSensitiveDataDetector {
       private static final DetectorConfig CONFIG = new DetectorConfig("passport", 9, 9);
   
       public PassportDetector() {
           super(CONFIG);
       }
   
       @Override
       protected SensitiveDataItem doDetect(String name, String data) {
           return data.matches("^[A-Z0-9]{9}$") ? new SensitiveDataItem(CONFIG.getType(), name, data) : null;
       }
   }
   ```

2. **Register the Detector**

   Update the `sensitiveDataDetector` bean in your configuration:

   ```java
   import io.github.kanshanos.datasentry.chain.data.SensitiveDataDetector;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   
   @Configuration
   public class CustomDataSentryConfig {
       @Bean
       public SensitiveDataDetector sensitiveDataDetector() {
           AbstractSensitiveDataDetector head = new MobileDetector();
           head.next(new IdcardDetector())
               .next(new PassportDetector()); // Add custom detector
           return head;
       }
   }
   ```

### Customizing Output

By default, DataSentry logs to SLF4J. To use a custom output (e.g., to a database or external system), implement the `ContextOutput` interface:

```java
package com.example;

import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.output.ContextOutput;

public class CustomOutput implements ContextOutput {
    @Override
    public void outputContext(SentryDataContext context) {
        // Log to database or external system
        System.out.println("Custom Output: " + context);
    }
}
```

Register the custom output as a bean:

```java
@Bean
public ContextOutput contextOutput() {
    return new CustomOutput();
}
```

## Contributing

We welcome contributions to DataSentry! To contribute:

1. **Fork the Repository**

   ```bash
   git clone https://github.com/KanshanOS/data-sentry.git
   ```

2. **Create a Feature Branch**

   ```bash
   git checkout -b feature/your-feature
   ```

3. **Submit a Pull Request**

   Ensure your code follows the project's coding standards and includes tests.

4. **Report Issues**

   Use the GitHub Issues page to report bugs or suggest enhancements.

## License

DataSentry is licensed under the Apache License 2.0.

## Contact

- **Author**: Kanshan
- **Email**: im.neoyu@gmail.com
- **GitHub**: KanshanOS

---

Happy coding with DataSentry!
