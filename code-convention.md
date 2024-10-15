# 코드 컨벤션

이 문서는 프로젝트의 일관성을 높이기 위해 준수해야 할 코드 컨벤션을 정의합니다.

## 1. **URL 규칙**
- URL은 `kebab-case` 형식을 사용하며, 단어는 하이픈(`-`)으로 구분합니다.
  - 예: `user-delete`는 올바른 형식입니다.
  - 잘못된 예: `userDelete`는 잘못된 형식입니다.

## 2. **패키지 및 클래스 구조**
- **패키지 이름**: 소문자를 사용하며, 의미 있는 그룹으로 구성하고 계층 구조를 명확히 합니다.
  - 예: `com.example.domainname.controller`
  
- **클래스 이름**: `UpperCamelCase` 형식으로 작성합니다.
  - 예: `UserController`, `ProductService`, `OrderRepository`

## 3. **클래스명 접미사**
- **Controller 클래스**: `*Controller.java`
- **Service 클래스**: `*Service.java`
- **Repository 클래스**: `*Repository.java`
- **Entity 클래스**: `*Entity.java`
- **DTO 클래스**:
  - **Response DTO**: 클라이언트에게 반환할 데이터를 정의합니다.
    - 예: `UserResponseDTO.java`
  - **Request DTO**: 클라이언트로부터 받을 요청 데이터를 정의합니다.
    - 예: `UserRequestDTO.java`

## 4. **Entry에서 DTO 변환**

### ModelMapper 사용 예시

```java
SampleDTO sampleDTO = modelMapper.map(sample, SampleDTO.class); // Entity를 DTO로 변환
```
