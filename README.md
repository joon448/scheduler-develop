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


## API 명세

### AUTH

| 기능   | Method | URL      | Request                                | Response (Success)  | Response (Fail)       | 상세                                                    |
|--------|--------|----------|----------------------------------------|---------------------|-----------------------|---------------------------------------------------------|
| 로그인  | POST   | /login   | { "email": "string", "password": "string" } | 200 OK              | 401 (이메일/비번 불일치), 400 (검증 실패) |                                                         |
| 로그아웃 | POST   | /logout  | -                                      | 200 OK              | -                     |                                                         |

### USER

| 기능           | Method | URL         | Request                                                            | Response (Success)                                           | Response (Fail)                          | 상세                                                                                                                                                   |
|----------------|--------|-------------|--------------------------------------------------------------------|--------------------------------------------------------------|------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| 회원가입        | POST   | /signup     | { "name": "string", "email": "string", "password": "string" }       | 201 Created <br> { "id": Long, "name": "string", "email": "string", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime } | 400 (유효성 검증), 409 (이메일 중복)        | password 최소 8자, 이메일 중복 체크                                                                                                        |
| 유저 목록 조회   | GET    | /users      | -                                                                  | 200 OK <br> [{ "id": Long, "name": "string", "email": "email", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime }, ...] | -                                        | page: 기본 0, size: 기본 10                                                                                                                                                     |
| 유저 단일 조회   | GET    | /users/{id} | -                                                                  | 200 OK <br> { "id": Long, "name": "string", "email": "email", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime }   | 404 (존재하지 않음)                     |                                                                                                                                                        |
| 유저 수정        | PATCH  | /users/{id} | { "name"?, "email"?, "password": "oldPassword", "newPassword"? }    | 200 OK <br> { "id": Long, "name": "string", "email": "email", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime } | 401 (비번 불일치), 403(본인 아님), 400 (유효성 검증), 404 (존재하지 않음), 409 (이메일 중복) | 로그인 본인만 가능, newPassword 최소 8자, 이메일 변경 시 중복 체크                                                            |
| 유저 삭제        | DELETE | /users/{id} | { "password": "string" }                                           | 204 No Content                                              | 401(비번 불일치), 403(본인 아님), 404 (존재하지 않음) | 본인만 가능, 삭제 후 세션 무효화, 연관 일정/댓글 선삭제 처리                                                                                           |

### SCHEDULE

| 기능            | Method | URL               | Request                                      | Response (Success)                                           | Response (Fail)                          | 상세                                                                                                                                                               |
|-----------------|--------|-------------------|----------------------------------------------|--------------------------------------------------------------|------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 일정 생성       | POST   | /schedules        | { "title": "string", "content": "string" }    | 201 Created <br> { "id": Long, "userId": Long, "title": "string", "content": "string", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime } | 400 (유효성 검증)                        | title 길이 30 이내 필수값, content 길이 200 이내 필수값                                                                                                            |
| 전체 일정 조회 (페이징) | GET    | /schedules?userId={userId}&page={page}&size={size} | -                                            | 200 OK <br> [{ "id": Long, "title": "string", "content": "string", "commentCount": long, "createdAt": LocalDateTime, "modifiedAt": LocalDateTime, "userName": "string" }, ...] | 400 (유효성 검증)                        | page: 기본 0, size: 기본 10, 최대 100                                                                                                                                                    |
| 단일 일정 및 댓글 조회 | GET    | /schedules/{id}   | -                                            | 200 OK <br> { "schedule": { "id": Long, "userId": Long, "title": "string", "content": "string", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime }, "comments": [...] } | 404 (존재하지 않음)                     |                                                                                                                                                                     |
| 일정 수정        | PATCH  | /schedules/{id}   | { "title"?, "content"? }                      | 200 OK <br> { "id": Long, "userId": Long, "title": "string", "content": "string", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime } | 403(본인 아님), 400(유효성 검증), 404 (존재하지 않음) | title 길이 30 이내, content 길이 200 이내                                                                                                                                             |
| 일정 삭제        | DELETE | /schedules/{id}   | -                                            | 204 No Content                                              | 403(본인 아님), 404 (존재하지 않음)       | 본인만 가능, 연관 댓글 선삭제 처리                                                                                               |

### COMMENT

| 기능            | Method | URL                           | Request                                | Response (Success)                                           | Response (Fail)                          | 상세                                                                                                                                                               |
|-----------------|--------|-------------------------------|----------------------------------------|--------------------------------------------------------------|------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 댓글 생성       | POST   | /schedules/{scheduleId}/comments | { "content": "string" }                | 201 Created <br> { "id": Long, "scheduleId": Long, "userId": Long, "content": "string", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime } | 400 (유효성 검증), 404 (존재하지 않음)  | content 길이 100 이내                                                                                                                                                              |
| 댓글 목록 조회  | GET    | /schedules/{scheduleId}/comments | -                                      | 200 OK <br> [{ "id": Long, "scheduleId": Long, "userId": Long, "content": "string", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime }, ...] | 404 (존재하지 않음)                     | page: 기본 0, size: 기본 10                                                                                                                                                             |
| 댓글 단일 조회  | GET    | /schedules/{scheduleId}/comments/{commentId} | -                                      | 200 OK <br> { "id": Long, "scheduleId": Long, "userId": Long, "content": "string", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime } | 400(경로 - 일정 불일치), 404 (존재하지 않음) |                                                                                                                                                                     |
| 댓글 수정       | PATCH  | /schedules/{scheduleId}/comments/{commentId} | { "content": "string" }                | 200 OK <br> { "id": Long, "scheduleId": Long, "userId": Long, "content": "string", "createdAt": LocalDateTime, "modifiedAt": LocalDateTime } | 403(본인 아님), 400 (유효성 검증), 404 (존재하지 않음) | 로그인 본인만 가능, content 길이 최대 100자 이내                                                                                           |
| 댓글 삭제       | DELETE | /schedules/{scheduleId}/comments/{commentId} | -                                      | 204 No Content                                              | 403(본인 아님), 404 (존재하지 않음)       | 본인만 가능                                                                                                                                                              |

./Scheduler-develop.postman_collection.json : Postman Collection 문서

# 요청 및 응답 예시

---

## 1. AUTH

### 1) 로그인

**POST /login**

```json
// 요청
{
    "email": "qwe1@qwe.com",
    "password": "11111111"
}
```

```
// 응답 (200 OK)
```

---

### 2) 로그아웃

**POST /logout**

```
// 응답 (200 OK)
```

---

## 2. USER

### 1) 회원가입

**POST /signup**

```json
// 요청
{
    "name": "11",
    "email": "qwe1@qwe.com",
    "password": "11111111"
}
```

```json
// 응답 (201 Created)
{
    "id": 1,
    "name": "11",
    "email": "qwe1@qwe.com",
    "createdAt": "2025-08-12T20:39:46.9401505",
    "modifiedAt": "2025-08-12T20:39:46.9401505"
}
```

---

### 2) 유저 전체 조회

**GET /users**

```json
// 응답 (200 OK)
[
    {
        "id": 2,
        "name": "22",
        "email": "qwe2@qwe.com",
        "createdAt": "2025-08-08T19:45:43.935697",
        "modifiedAt": "2025-08-08T19:47:13.730426"
    },
    {
        "id": 1,
        "name": "11",
        "email": "qwe1@qwe.com",
        "createdAt": "2025-08-08T19:45:57.115509",
        "modifiedAt": "2025-08-08T19:45:57.115509"
    }
]
```

---

### 3) 유저 단일 조회

**GET /users/{userId}**

```json
// 응답 (200 OK)
{
    "id": 1,
    "name": "11",
    "email": "qwe1@qwe.com",
    "createdAt": "2025-08-08T19:45:43.935697",
    "modifiedAt": "2025-08-08T19:47:13.730426"
}
```

---

### 4) 유저 수정

**PATCH /users/{userId}**

```json
// 요청
{
    "name":"11수정함",
    "password":"11111111"
}
```

```json
// 응답 (200 OK)
{
    "id": 1,
    "name": "11수정함",
    "email": "qwe1@qwe.com",
    "createdAt": "2025-08-08T19:45:43.935697",
    "modifiedAt": "2025-08-12T20:42:36.7928243"
}
```

---

### 5) 유저 삭제

**DELETE /users/{userId}**

```json
// 요청
{
    "password": "11111111"
}
```

```
// 응답 (204 No Content)
```

---

## 3. SCHEDULE

### 1) 일정 생성

**POST /schedules**

```json
// 요청
{
    "title": "제목",
    "content": "내용"
}
```

```json
// 응답 (201 Created)
{
    "id": 1,
    "userId": 1,
    "title": "제목",
    "content": "내용",
    "createdAt": "2025-08-12T20:46:35.5681833",
    "modifiedAt": "2025-08-12T20:46:35.5681833"
}
```

---

### 2) 일정 전체 조회 (페이지)

**GET /schedules**

| 파라미터 | 타입 | 설명 | 상세 |
| -------- | ---- | ---- | ---- |
| userId   | Long | 작성자 ID | (선택) |
| page     | int  | 페이지 | (선택) 기본 0 |
| size     | int  | 결과 개수 | (선택) 기본 10, 최대 100 |

```json
// 응답 (200 OK)
{
    "content": [
        {
            "id": 4,
            "title": "제목",
            "content": "내용",
            "commentCount": 0,
            "createdAt": "2025-08-12T20:46:35.568183",
            "modifiedAt": "2025-08-12T20:46:35.568183",
            "userName": "11수정함"
        },
        {
            "id": 2,
            "title": "qwer1",
            "content": "내용1",
            "commentCount": 0,
            "createdAt": "2025-08-08T19:47:42.906091",
            "modifiedAt": "2025-08-08T19:47:42.906091",
            "userName": "11수정함"
        },
        {
            "id": 1,
            "title": "qwer",
            "content": "내용",
            "commentCount": 2,
            "createdAt": "2025-08-08T19:47:34.336517",
            "modifiedAt": "2025-08-08T19:47:34.336517",
            "userName": "11수정함"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 5,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 3,
    "totalPages": 1,
    "size": 5,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 3,
    "empty": false
}
```

---

### 3) 일정 단일 조회

**GET /schedules/{scheduleId}**

```json
// 응답 (200 OK)
{
    "comments": [
        {
            "id": 4,
            "userId": 1,
            "scheduleId": 1,
            "content": "댓글2",
            "createdAt": "2025-08-08T20:21:08.330616",
            "modifiedAt": "2025-08-08T20:21:08.330616"
        },
        {
            "id": 2,
            "userId": 1,
            "scheduleId": 1,
            "content": "댓글",
            "createdAt": "2025-08-08T20:15:30.136355",
            "modifiedAt": "2025-08-08T20:15:30.136355"
        }
    ],
    "schedule": {
        "id": 1,
        "userId": 1,
        "title": "qwer",
        "content": "내용",
        "createdAt": "2025-08-08T19:47:34.336517",
        "modifiedAt": "2025-08-08T19:47:34.336517"
    }
}
```

---

### 4) 일정 수정

**PATCH /schedules/{scheduleId}**

```json
// 요청
{
    "title": "제목수정함"
}
```

```json
// 응답 (200 OK)
{
    "id": 1,
    "userId": 1,
    "title": "제목수정함",
    "content": "내용",
    "createdAt": "2025-08-08T19:47:34.336517",
    "modifiedAt": "2025-08-12T20:58:43.6591477"
}
```

---

### 5) 일정 삭제

**DELETE /schedules/{scheduleId}**

```
// 응답 (204 No Content)
```

---

## 4. COMMENT

### 1) 댓글 생성

**POST /schedules/{scheduleId}/comments**

```json
// 요청
{
    "content": "댓글"
}
```

```json
// 응답 (201 Created)
{
    "id": 5,
    "userId": 1,
    "scheduleId": 1,
    "content": "댓글",
    "createdAt": "2025-08-12T21:00:31.5056736",
    "modifiedAt": "2025-08-12T21:00:31.5056736"
}
```

---

### 2) 댓글 목록 조회

**GET /schedules/{scheduleId}/comments**

```json
// 응답 (200 OK)
[
    {
        "id": 5,
        "userId": 1,
        "scheduleId": 1,
        "content": "댓글3",
        "createdAt": "2025-08-12T21:00:31.505674",
        "modifiedAt": "2025-08-12T21:00:31.505674"
    },
    {
        "id": 4,
        "userId": 1,
        "scheduleId": 1,
        "content": "댓글2",
        "createdAt": "2025-08-08T20:21:08.330616",
        "modifiedAt": "2025-08-08T20:21:08.330616"
    },
    {
        "id": 2,
        "userId": 1,
        "scheduleId": 1,
        "content": "댓글",
        "createdAt": "2025-08-08T20:15:30.136355",
        "modifiedAt": "2025-08-08T20:15:30.136355"
    }
]
```

---

### 3) 댓글 단일 조회

**GET /schedules/{scheduleId}/comments/{commentId}**

```json
// 응답 (200 OK)
{
    "id": 1,
    "userId": 1,
    "scheduleId": 1,
    "content": "댓글",
    "createdAt": "2025-08-08T20:15:30.136355",
    "modifiedAt": "2025-08-08T20:15:30.136355"
}
```

---

### 4) 댓글 수정

**PATCH /schedules/{scheduleId}/comments/{commentId}**

```json
// 요청
{
    "content": "댓글수정함"
}
```

```json
// 응답 (200 OK)
{
    "id": 1,
    "userId": 1,
    "scheduleId": 1,
    "content": "댓글수정함",
    "createdAt": "2025-08-08T20:15:30.136355",
    "modifiedAt": "2025-08-12T21:02:10.9417525"
}
```

---

### 5) 댓글 삭제

**DELETE /schedules/{scheduleId}/comments/{commentId}**

```
// 응답 (204 No Content)
```

---

## ERROR 응답

```json
{
  "status": 400,
  "errorCode": "VAL-001",
  "message": "유효성 검증에 실패했습니다.",
  "path": "/schedules",
  "timestamp": "2025-08-08T12:34:56",
  "errors": {
    "title": "제목은 최대 30자입니다"
  }
}
```

```json
{
    "status": 403,
    "errorCode": "AUTH-403",
    "message": "본인만 삭제할 수 있습니다.",
    "path": "/users/3",
    "timestamp": "2025-08-12T20:45:31.1630731",
    "errors": null
}
```

| 코드       | HTTP 상태 | 설명                    | 메시지                         |
| ---------- | --------- | ----------------------- | ------------------------------ |
| VAL-400    | 400       | VALIDATION_FAILED       | 유효성 검증에 실패했습니다.    |
| AUTH-400   | 400       | PASSWORD_SAME_AS_OLD    | 새 비밀번호가 기존 비밀번호와 동일합니다. |
| AUTH-401   | 401       | PASSWORD_INCORRECT      | 비밀번호가 올바르지 않습니다.  |
| AUTH-403   | 403       | FORBIDDEN_NOT_OWNER     | 접근 권한이 없습니다.          |
| USER-404   | 404       | USER_NOT_FOUND          | 존재하지 않는 사용자입니다.    |
| USER-409   | 409       | DUPLICATE_USER          | 이미 가입된 사용자입니다.      |
| SCH-400    | 400       | INVALID_PAGING_PARAM    | page/size 파라미터가 올바르지 않습니다. size는 최대 100까지 가능합니다. |
| SCH-404    | 404       | SCHEDULE_NOT_FOUND      | 존재하지 않는 일정입니다.      |
| CMT-400    | 400       | COMMENT_SCHEDULE_MISMATCH | 댓글이 요청한 일정에 속하지 않습니다. |
| CMT-404    | 404       | COMMENT_NOT_FOUND       | 존재하지 않는 댓글입니다.      |


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

9. **JPQL 적용하여 페이지네이션 구현**
   - 단방향 연관관계(일정-댓글)에서 댓글 개수를 가져오기 위해 쿼리 작성함

---

## 학습 포인트
- Spring MVC 계층 구조 설계
- 연관관계 매핑
- Cookie/Session 기반 인증 흐름
- Validation & 예외 처리 패턴
- BCrypt를 활용한 비밀번호 암호화
- Pageable 기반 페이징 처리
