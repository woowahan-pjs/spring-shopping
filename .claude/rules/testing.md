---
description: 테스트 코드를 작성하거나 수정할 때 적용
globs: "**/test/**/*.java,**/test/**/*.feature"
---

# Testing Rules

- Unit tests must NEVER use mocking frameworks (Mockito, EasyMock, etc.)
- Use HashMap-based in-memory repository implementations instead of mocks
- API documentation must be maintained using Cucumber + REST Docs
- Feature files must use business language only — no HTTP status codes or API URLs in Given/When/Then steps
- API URLs may only appear as comments in feature files, not in scenario steps
- External API calls must use test doubles (fakes) in tests — never call real external services
