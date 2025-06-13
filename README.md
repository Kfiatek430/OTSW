# OTSW - Websocket Server Testing Software

A Java-based testing framework for evaluating server performance under various load conditions. The project consists of a multi-threaded server and a comprehensive testing suite that can simulate multiple concurrent clients.

## Project Structure

```
src/
├── main/java/com/kfiatek/otsw/
│   ├── clients/
│   │   ├── Client.java              # Base client interface
│   │   ├── ConsoleClient.java       # Interactive console client
│   │   └── TestingClient.java       # Automated testing client
│   ├── config/
│   │   ├── ConfigLoader.java        # YAML configuration loader
│   │   ├── ConnectionConfig.java    # Connection settings
│   │   ├── MessageConfig.java       # Message generation settings
│   │   ├── TestConfig.java          # Test configuration
│   │   └── ValidationConfig.java    # Validation settings
│   ├── enums/
│   │   └── TextTypes.java           # Character set types
│   ├── helpers/
│   │   └── Helper.java              # Helper functions
│   ├── server/
│   │   ├── Connection.java          # Client connection handler
│   │   ├── Main.java                # Server entry point
│   │   └── Server.java              # Main server class
│   └── testingsoftware/
│       ├── Main.java                # Testing entry point
│       └── TestingSoftware.java     # Testing Software class
└── resources/
    ├── testcases/                   # Test configuration files
    └── log4j2.xml                   # Logging configuration
```

## Requirements

- Java 23 or higher
- Maven 3.6+

## Dependencies

- **Log4j2**: Logging framework
- **Lombok**: Code generation for boilerplate reduction
- **SnakeYAML**: YAML configuration parsing

## Getting Started

### 1. Clone and Build

```bash
git clone <repository-url>
cd OTSW
mvn clean compile
```

### 2. Start the Server

```bash
mvn exec:java -Dexec.mainClass="com.kfiatek.otsw.server.Main"
```

The server will start on port 8000 by default.

### 3. Run Tests

```bash
mvn exec:java -Dexec.mainClass="com.kfiatek.otsw.testingsoftware.Main"
```

### 4. Interactive Console Client

```bash
mvn exec:java -Dexec.mainClass="com.kfiatek.otsw.clients.ConsoleClient"
```

## Configuration

Test configurations are defined in YAML format:

```yaml
testName: "Your Test Name"
clients: 10 # Number of concurrent clients
message:
  count: 100 # Messages per client
  length: 50 # Message length in characters
  characters: # Character sets to use
    - LOWER_CASE
    - UPPER_CASE
    - DIGITS
  frequencyMs: 10 # Delay between messages (ms)
connection:
  port: 8000 # Server port
  connectDelayMs: 100 # Delay between client connections (ms)
validation:
  timeoutMs: 5000 # Response timeout (ms)
```

### Available Character Sets

- `LOWER_CASE`: a-z
- `UPPER_CASE`: A-Z
- `DIGITS`: 0-9
- `SPECIAL_CHARS`: Special characters

You can modify `testingsoftware/Main.java` to use your configuration:

```java
TestConfig config = ConfigLoader.load("testcases/your_custom_test.yaml");
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
