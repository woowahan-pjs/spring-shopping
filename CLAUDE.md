# Project Rules

## Code Rules
- No database yet — only plain POJO classes and interfaces
- No JPA, JDBC, or MyBatis at this stage

## Architecture
- Each usecase interface should have one or two methods only (single responsibility)
- module-product and module-member must not depend on each other
- Modules are pure Java libraries with no Spring or framework dependencies
