# Wishlist Feature Plan

## Overview
Authenticated users can manage a personal wishlist of products: view their wishlist, add a product, and remove a product.

## API Endpoints
- `GET /api/wishes` — view my wishlist (requires auth token)
- `POST /api/wishes/{productId}` — add a product to my wishlist (requires auth token)
- `DELETE /api/wishes/{productId}` — remove a product from my wishlist (requires auth token)

## Authentication Prerequisite
The current codebase creates JWT tokens but never extracts/validates them from incoming requests. Before wishlist endpoints can identify "who is calling," we need:

1. **Add `extractEmail(String token)` to `TokenProvider` port** (in module-member) — parse a JWT and return the email subject
2. **Implement `extractEmail` in `JwtTokenProvider` adapter** (in src/) — use JJWT to parse and validate the token
3. **Resolve the authenticated member in the controller** — extract the `Authorization: Bearer <token>` header, call `extractEmail`, then look up the member

## Module: module-wishlist (pure Java, no Spring)

### Domain
- `Wish` — POJO with fields: `id (Long)`, `memberId (Long)`, `productId (Long)`

### Usecase Interfaces
- `AddWish` — `Wish execute(Long memberId, Long productId)`
- `RemoveWish` — `void execute(Long memberId, Long productId)`
- `FindWish` — `List<Wish> execute(Long memberId)`

### Service Implementations
- `AddWishService` — validates no duplicate (same member + product), saves via port
- `RemoveWishService` — deletes by memberId + productId
- `FindWishService` — returns all wishes for a member

### Port Interface
- `WishRepository`
  - `Wish save(Wish wish)`
  - `List<Wish> findByMemberId(Long memberId)`
  - `void deleteByMemberIdAndProductId(Long memberId, Long productId)`
  - `boolean existsByMemberIdAndProductId(Long memberId, Long productId)`

## Main App (src/) — Spring layer

### Adapter
- `InMemoryWishRepository` (`@Repository`) — ConcurrentHashMap + AtomicLong, same pattern as InMemoryProductRepository

### Configuration
- `WishConfiguration` (`@Configuration`) — wire AddWishService, RemoveWishService, FindWishService as beans

### Controller
- `WishController` (`@RestController`, `/api/wishes`) — injects usecases + TokenProvider + MemberRepository
  - Extracts token from `Authorization` header, resolves memberId, delegates to usecases

### DTOs
- `WishResponse` — record with productId (or product details if needed)

## Build Changes
- Add `module-wishlist` to `settings.gradle.kts`
- Create `module-wishlist/build.gradle.kts` (pure Java library, no Spring deps)
- Add `implementation(project(":module-wishlist"))` to root `build.gradle.kts`

## Implementation Order
1. Add `extractEmail` to TokenProvider port + JwtTokenProvider adapter
2. Create module-wishlist with domain, usecases, services, port
3. Create InMemoryWishRepository adapter in src/
4. Create WishConfiguration in src/
5. Create WishController + WishResponse in src/
6. Add Cucumber feature file + step definitions for wishlist scenarios
