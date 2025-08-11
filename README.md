# Spring 일정 관리 심화 프로젝트

## 목표  
Spring Boot와 JPA를 활용하여 일정, 유저, 댓글을 CRUD 할 수 있는 웹 애플리케이션 개발  
회원가입/로그인(쿠키·세션 기반 인증), 비밀번호 암호화, 예외 처리, 페이징 기능 등을 구현

---

## 개발 프로세스

### 필수 기능

### Lv 1. 일정 CRUD
- 일정 생성, 조회, 수정, 삭제
- 필드: `작성 유저명`, `할일 제목`, `할일 내용`, `작성일`, `수정일`
- `작성일`, `수정일`은 **JPA Auditing**으로 자동 관리

### Lv 2. 유저 CRUD
- 유저 생성, 조회, 수정, 삭제
- 필드: `유저명`, `이메일`, `작성일`, `수정일`
- 일정 - 유저 연관관계 설정 (`작성 유저명` → `유저 고유 식별자`로 변경)

### Lv 3. 회원가입
- 유저에 `비밀번호` 필드 추가 (암호화는 도전 기능에서 적용)

### Lv 4. 로그인(인증)
- 쿠키·세션 기반 로그인 구현 (`Filter` 적용)
- `@Configuration`으로 필터 등록
- 이메일+비밀번호로 인증
- 회원가입, 로그인 요청은 인증 예외 처리
- 로그인 실패 시 HTTP 401 반환

---

### 도전 기능

### Lv 5. 다양한 예외처리
- `@Valid`를 활용한 필드 유효성 검증

### Lv 6. 비밀번호 암호화
- `PasswordEncoder` 클래스 구현 (BCrypt)
- 회원가입·로그인·비밀번호 변경 시 적용

### Lv 7. 댓글 CRUD
- 일정에 대한 댓글 생성, 조회, 수정, 삭제
- 댓글 - 유저 - 일정 연관관계 설정

### Lv 8. 일정 페이징 조회
- `Pageable` & `Page` 활용
- 수정일 기준 내림차순 정렬
- 페이지 번호, 페이지 크기 쿼리 파라미터 처리 (기본 크기 10)

---

## 기술 스택
| 구분 | 내용 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| ORM | Spring Data JPA |
| DB | MySQL |
| Build Tool | Gradle |
| Auth | Cookie + Session (Filter 기반) |


---

## 디렉터리 구조

```
src/main/java/com/example/scheduler
├── config/
│   └── FilterConfig.java
├── controller/
│   ├── ScheduleController.java
│   ├── UserController.java
│   └── CommentController.java
├── dto/
│   ├── schedule/...
│   ├── login/...
│   ├── user/...
│   └── comment/...
├── entity/
│   ├── Schedule.java
│   ├── User.java
│   └── Comment.java
├── exception/
│   ├── CustomException.java
│   ├── ErrorCode.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
├── repository/
│   ├── ScheduleRepository.java
│   ├── UserRepository.java
│   └── CommentRepository.java
├── service/
│   ├── ScheduleService.java
│   ├── UserService.java
│   └── CommentService.java
└── SchedulerApplication.java
```

---

## 주요 클래스 설명

| 클래스 | 설명 |
|--------|------|
| `Schedule` | 일정 정보를 담는 Entity |
| `Comment` | 일정에 달린 댓글 정보를 담는 Entity |
| `ScheduleController` | 일정 및 댓글 관련 API를 처리하는 컨트롤러 |
| `ScheduleService` | 일정 로직을 처리하는 서비스 |
| `CommentService` | 댓글 로직을 처리하는 서비스 |
| `BaseEntity` | 생성일, 수정일을 자동 처리하는 공통 추상 클래스 (JPA Auditing) |

---

## API 명세

| 기능 | Method | URL | 설명 |
|------|--------|-----|------|
| 일정 생성 | POST | `/schedules` | 새 일정 등록 |
| 전체 조회 | GET | `/schedules` | 전체 일정 조회 |
| 단일 조회 | GET | `/schedules/{id}` | 댓글 포함 응답 |
| 일정 수정 | PATCH | `/schedules/{id}` | 제목/작성자명 수정, 비밀번호 필요 |
| 일정 삭제 | DELETE | `/schedules/{id}` | 비밀번호 필요 |
| 댓글 작성 | POST | `/schedules/{id}/comments` | 댓글 10개 제한, 비밀번호 필요 |


### 일정 생성
- **Method**: POST
- **URL**: /schedules
- **Request Body**:
```json
{
  "title": String,
  "content": String,
  "name": String,
  "password": String
}
```
- **Response**: 
  - 성공시: 200 OK
```json
{
  "id": Long,
  "title": String,
  "content": String,
  "name": String,
  "createdAt": DateTime,
  "modifiedAt": DateTime
}
```
  - 실패시:
    - 400 Bad Request: 필수값이 없는 경우, 길이 제한을 초과한 경우

---

### 전체 일정 조회
- **Method**: GET
- **URL**: /schedules?name=
- **Response**:
  - 성공시: 200 OK
```json
[
  {
    "id": Long,
    "title": String,
    "content": String,
    "name": String,
    "createdAt": DateTime,
    "modifiedAt": DateTime
  }
]
```
  - 조건에 맞는 결과가 없을 경우:
```json
[]
```

---

### 단일 일정 조회
- **Method**: GET
- **URL**: /schedules/{id}
- **Response**:
  - 성공시: 200 OK
```json
{
  schedule: {
    "id": Long,
    "title": String,
    "content": String,
    "name": String,
    "createdAt": DateTime,
    "modifiedAt": DateTime
  }
  comments: [
    //...
  ]
}
```
  - 실패시:
    - 404 Not Found: 해당 ID가 존재하지 않을 경우

---

### 일정 수정
- **Method**: PATCH
- **URL**: /schedules/{id}
- **Request Body**:
```json
{
  "title": String, 
  "name": String,
  "password": String
}
```
- **Response**:
  - 성공시: 200 OK
```json
{
  "id": Long,
  "title": String,
  "content": String,
  "name": String,
  "createdAt": DateTime,
  "modifiedAt": DateTime
}
```
  - 실패시:
    - 404 Not Found: ID가 존재하지 않음
    - 400 Bad Request: 필수값 누락, 길이 제한 초과, title&name 둘 다 없는 경우
    - 401 Unauthorized: 비밀번호 불일치

---

### 일정 삭제
- **Method**: DELETE
- **URL**: /schedules/{id}
- **Request Body**:
```json
{
  "password": String
}
```
- **Response**:
  - 성공시: 200 OK
  - 실패시:
    - 404 Not Found: ID가 존재하지 않음
    - 400 Bad Request: 필수값 누락
    - 401 Unauthorized: 비밀번호 불일치

---

### 댓글 생성
- **Method**: POST
- **URL**: /schedules/{id}/comments
- **Request Body**:
```json
{
  "content": String,
  "name": String,
  "password": String
}
```
- **Response**: 
  - 성공시: 200 OK
```json
{
  "id": Long,
  "content": String,
  "name": String,
  "scheduleId": Long,
  "createdAt": DateTime,
  "modifiedAt": DateTime
}
```
  - 실패시:
    - 404 Not Found: ID가 존재하지 않음
    - 400 Bad Request: 필수값이 없는 경우, 길이 제한을 초과한 경우

---

## ERD

<img width="592" height="338" alt="ScheduleApp-Develop_ERD" src="https://github.com/user-attachments/assets/ff26498d-a7bc-475b-bda4-5bb47d661642" />


---

## 개발 중 해결한 문제
1. **본인 작성 일정만 수정/삭제 가능하게**
   ```java
   if (!schedule.getUser().getId().equals(sessionUserId)) {
       throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 일정만 삭제할 수 있습니다.");
   }
   ```

2. **유저 수정/삭제 시 비밀번호 재확인**  
   - Request Body에서 비밀번호 추가 입력받아 기존 비밀번호와 비교
   - 삭제 후 세션 `invalidate()`로 자동 로그아웃

3. **비밀번호 변경 로직**
   - 기존 비밀번호와 동일하면 예외 발생
   - 최소 8자 이상 조건 검증

4. **이메일 변경 시 중복 검증 로직** 추가

5. **댓글 조회 범위 고민**
   - 전체 댓글 조회는 불필요 → 일정별 댓글 조회로 한정

6. **연관 데이터 삭제**
   - 유저 삭제 시 → 해당 유저의 일정 & 댓글 자동 삭제
   - 일정 삭제 시 → 해당 일정의 댓글 자동 삭제

7. **@Valid vs @Validated 차이 이해**
   - `@Valid` : 표준, 중첩 객체 검증 가능, 그룹 검증 불가
   - `@Validated` : Spring 제공, 그룹 검증 가능

8. **커스텀 예외 메시지 우선 출력**
   - 기본 메시지보다 커스텀 메시지가 먼저 나오도록 ExceptionHandler 수정

---

## 학습 포인트
- Spring MVC 계층 구조 설계
- 연관관계 매핑
- Cookie/Session 기반 인증 흐름
- Validation & 예외 처리 패턴
- BCrypt를 활용한 비밀번호 암호화
- Pageable 기반 페이징 처리
