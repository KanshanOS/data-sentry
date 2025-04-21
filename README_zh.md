# DataSentry

**DataSentry** 是一个轻量级、可扩展的 Spring Boot 框架，专为检测 HTTP 响应中的敏感数据而设计。它帮助开发者识别并记录敏感信息，如手机号码、银行卡号、电子邮箱等，确保数据隐私并符合 GDPR、CCPA 等法规要求。DataSentry 采用模块化架构，易于集成到 Spring Boot 应用中，并支持灵活的定制。

## 功能

- **敏感数据检测**：自动识别以下类型的敏感数据：
    - 手机号码
    - 身份证号码
    - 银行卡号
    - 电子邮箱
    - 中文姓名
    - 中国地址
    - 固定电话号码
- **灵活的请求过滤**：支持多种配置化的请求过滤机制：
    - 采样率控制请求处理概率
    - URI 白名单排除特定接口
    - 请求频率限制控制处理频率
    - 缓存机制避免对近期已检测请求的重复处理
- **无缝 Spring Boot 集成**：以 Spring Boot Starter 形式提供，自动配置，开箱即用。
- **可扩展设计**：基于责任链的请求过滤和数据检测架构，便于添加自定义检测器或过滤器。
- **可定制输出**：通过 SLF4J 或自定义输出方式（如控制台、文件或外部系统）记录敏感数据检测结果。
- **性能优化**：使用缓存和高效的正则表达式检测，最大程度减少性能开销。

## 项目结构

DataSentry 包含三个主要模块：

- `data-sentry-core`：核心库，包含检测逻辑、过滤链和工具类。
- `data-sentry-springboot-starter`：Spring Boot Starter，简化集成。
- `data-sentry-sample`：示例应用，展示 DataSentry 的使用方法。

```
data-sentry/
├── data-sentry-core/                # 核心库
├── data-sentry-springboot-starter/  # Spring Boot Starter
├── data-sentry-sample/              # 示例应用
└── pom.xml                          # 父 POM 文件
```

## 快速开始

### 前置条件

- **Java**：8 或更高版本
- **Maven**：3.6.0 或更高版本
- **Spring Boot**：2.0.9.RELEASE 或兼容版本
- **依赖**：通过 Maven 管理（见 `pom.xml`）

### 安装步骤

1. **添加依赖**

   在 Spring Boot 项目的 `pom.xml` 中添加 `data-sentry-springboot-starter` 依赖：

   ```xml
   <dependency>
       <groupId>io.github.kanshanos</groupId>
       <artifactId>data-sentry-springboot-starter</artifactId>
       <version>${latest}</version>
   </dependency>
   ```

   *注意*：发布到 Maven 仓库后，请将 `latest` 替换为最新版本号。

2. **配置应用**

   在 `application.yml` 或 `application.properties` 中添加 DataSentry 配置：

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

   详见配置说明。

3. **运行应用**

   DataSentry 将自动拦截 HTTP 响应，检测敏感数据，并通过 SLF4J 或自定义 `ContextOutput` 实现记录结果。

### 示例使用

`data-sentry-sample` 模块提供了一个完整的示例应用。运行步骤如下：

1. **克隆仓库**

   ```bash
   git clone https://github.com/KanshanOS/data-sentry.git
   cd data-sentry
   ```

2. **构建项目**

   ```bash
   mvn clean install
   ```

3. **运行示例应用**

   ```bash
   cd data-sentry-sample
   mvn spring-boot:run
   ```

4. **测试接口**

   访问 `http://localhost:8080/user/get/1`，查看 DataSentry 的运行效果。响应将返回一个用户对象，控制台将记录检测到的敏感数据，例如：

   ```
   Sentry Data URI :[GET] /user/get/{id}, Senses List : {"type":"mobile","name":"phone","data":"15912341234"},{"type":"idcard","name":"idcard","data":"310112199009015335"},{"type":"email","name":"email","data":"123456@gmail.com"},{"type":"chinese_address","name":"address","data":"上海市浦东新区世纪大道1号"},{"type":"chinese_name","name":"name","data":"张三"}
   ```

## 配置说明

DataSentry 支持通过 `application.yml` 或 `application.properties` 进行高度定制。以下是可用配置项：

| 配置项 | 描述 | 默认值 |
| --- | --- | --- |
| `kanshanos.datasentry.enabled` | 是否启用 DataSentry | `true` |
| `kanshanos.datasentry.sampling-rate` | 请求处理概率（0.0 到 1.0） | `0.5` |
| `kanshanos.datasentry.exclude-path-patterns` | 排除检测的 URI 模式列表 | `[]` |
| `kanshanos.datasentry.request-rate-window-interval-seconds` | 同一请求处理的最小间隔（秒） | `600`（10 分钟） |
| `kanshanos.datasentry.sensitive-detection-hit-window-interval-seconds` | 敏感数据检测缓存有效期（秒） | `86400`（7 天） |

示例配置：

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

## 自定义开发

### 添加自定义检测器

要检测新的敏感数据类型，可扩展 `AbstractSensitiveDataDetector` 或 `RegexBasedDetector`：

1. **创建自定义检测器**

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

2. **注册检测器**

   在配置中更新 `sensitiveDataDetector` bean：

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
               .next(new PassportDetector()); // 添加自定义检测器
           return head;
       }
   }
   ```

### 自定义输出

默认情况下，DataSentry 使用 SLF4J 记录日志。要自定义输出（如记录到数据库或外部系统），实现 `ContextOutput` 接口：

```java
package com.example;

import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.output.ContextOutput;

public class CustomOutput implements ContextOutput {
    @Override
    public void outputContext(SentryDataContext context) {
        // 记录到数据库或外部系统
        System.out.println("自定义输出: " + context);
    }

    @Override
    public void error(String msg, Throwable t) {
        // 处理错误
        System.err.println("错误: " + msg + ", " + t.getMessage());
    }
}
```

注册自定义输出为 bean：

```java
@Bean
public ContextOutput contextOutput() {
    return new CustomOutput();
}
```

## 贡献指南

我们欢迎社区为 DataSentry 贡献力量！贡献方式包括：

1. **Fork 仓库**

   ```bash
   git clone https://github.com/KanshanOS/data-sentry.git
   ```

2. **创建功能分支**

   ```bash
   git checkout -b feature/your-feature
   ```

3. **提交 Pull Request**

   确保代码符合项目编码规范并包含测试用例。

4. **报告问题**

   通过 GitHub Issues 页面报告 Bug 或提出改进建议。

## 许可证

DataSentry 采用 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt) 许可证。

## 联系方式

- **作者**：Kanshan
- **邮箱**：im.neoyu@gmail.com
- **GitHub**：KanshanOS

---

使用 DataSentry，愉快编码！
