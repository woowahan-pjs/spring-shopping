# Project Rules

## Code Rules
- No database yet — only plain POJO classes and interfaces
- No JPA, JDBC, or MyBatis at this stage
- The main app module (src/) contains only controllers, DTOs, Spring configurations, and infrastructure adapters
- All service classes and repository/port interfaces must live in their respective domain modules

## Architecture
- Each usecase interface should have one or two methods only (single responsibility)
- module-product and module-member must not depend on each other
- Modules are pure Java libraries with no Spring or framework dependencies
