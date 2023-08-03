# :paperclip: Notice board Project
> bootstrap, jquery, spring boot로 개발한 반응형 게시판 프로젝트입니다.

## 목차
- 개요
  - 프로젝트 소개
  - 개발 환경
  - 개발 기간  
  - 개발 인원
  - 기능 소개
- 구조 및 설계
  - 프로젝트 구조
  - DB 설계
  - API 설계
- 주요 기능 설명
  - 게시글
    - 게시글 목록 정렬및 검색, 페이징
    - 게시글 조회
    - 이전글 및 다음글 조회
    - 게시글 추천 및 중복방지
    - 게시글 조회수 및 중복방지 
  - 회원
    - 로그인
    - 소셜 로그인
    - 이메일 인증
  - 댓글
     - 댓글 슬라이스
     - 답글
  - 알림
    - 추천 및 댓글 알림
## 개요
### 1.프로젝트 소개
인프런 강의와 책을통한 독학으로 관련 기술들을 학습한 후에 제작한 게시판 프로젝트입니다. 이것 저것 하기보다는 배운거라도 확실히 하자는 생각으로 개발하였습니다.
### 2.개발환경
  #### 2.1 백엔드
    - IDE: intellJ
    - java version: java 11
    - framework: spring boot 2.7.7
    - 주요 라이브러리
      - spring-data-jpa
      - query-dsl
      - spring-security
      - oauth2
  #### 2.2 프론트 엔트
    - thymeleaf
    - Html/Css
    - jquery 3.6.3
    - bootstrap 5.2.3
### 3.개발기간
  - 2023년 4월~ 2023년 7월
### 4.개발 인원
  - 1명
### 5. 기능 소개
#### 5.1 게시글 기능
  - CRUD
  - 추천 및 추천 중복방지
  - 이전글 및 다음글 조회
  - 게시글 목록 조회
  - 게시글 목록 정렬 및 검색
  - 조회수 및 조회수 중복 방지
#### 5.2 이미지 기능
  - 이미지 업로드
  - 이미지 조회
  - 이미지 삭제
#### 5.2 회원 기능
  - 회원가입
  - 일반로그인
  - oath2 로그인
  - 아이디 및 비밀번호 찾기
  - 이메일 및 비밀번호 변경
  - 이메일 인증
#### 5.3 댓글 기능
  - CRUD
  - 댓글 목록 조회
  - 답글 목록 조회
  - 추천 및 추천 중복방지
#### 5.4 알림 기능
  - 알림 구독
  - 추천 알림, 댓글 알림
  - 알림 목록 조회
## 구조 및 설계
### 1. 프로젝트 구조
<details>
  
<summary>패키지 구조 보기</summary>   


```
📦src
 ┣ 📂main
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┃ ┗ 📂noticeboard
 ┃ ┃ ┃ ┃ ┃ ┣ 📂config
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JpaConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtSecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜RedisConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WebConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardRestController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentRestController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ImageRestController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberRestController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationRestController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂memberdto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FindPasswordForm.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜FindUsernameForm.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberSaveForm.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ModifyEmailForm.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ModifyPasswordForm.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ImageDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PageRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PageResultDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜PagingBoardDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜SliceRequestDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SliceResultDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂member
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Member.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Role.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂notification
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Notification.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationType.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BaseEntity.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Board.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Comment.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailAuthToken.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜Image.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberBoard.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberComment.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂custom_exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardNotFoundException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentNotFoundException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DuplicateEmailException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜DuplicateRecommendException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailNotFoundException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ExpiredCodeException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜SelfRecommendException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂exception_handler
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ControllerExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RestControllerExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ErrorCode.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜ExceptionDTO.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂boardrepository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardRepositoryCustom.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜BoardRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂commentRepository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentRepositoryCustom.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CommentRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂notificationrepository
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationRepositoryCustom.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmitterRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmitterRepositoryImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ImageRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberBoardRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberCommentRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RedisRepository.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂security
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜MemberDTO.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂filter
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜JwtAuthenticationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜JwtAuthorizationFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂handler
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomAccessDeniedHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CustomAuthenticationEntryPoint.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜LoginFailureHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LoginSuccessHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberDetailsService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜Oauth2MemberDetailsService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂event
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ModifyBoardEvent.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationEvent.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RemoveBoardEvent.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ImageService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜ImageServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationServiceImpl.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂web
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂interceptor
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜LogInterceptor.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜NoticeboardApplication.java
 ┃ ┗ 📂resources
 ┃ ┃ ┣ 📂static
 ┃ ┃ ┃ ┣ 📂assets
 ┃ ┃ ┃ ┃ ┣ 📂img
 ┃ ┃ ┃ ┃ ┃ ┣ 📜app-store-badge.svg
 ┃ ┃ ┃ ┃ ┃ ┣ 📜demo-screen.mp4
 ┃ ┃ ┃ ┃ ┃ ┣ 📜google.png
 ┃ ┃ ┃ ┃ ┃ ┣ 📜kakao.png
 ┃ ┃ ┃ ┃ ┃ ┣ 📜member.png
 ┃ ┃ ┃ ┃ ┃ ┣ 📜naver.png
 ┃ ┃ ┃ ┃ ┃ ┣ 📜portrait_black.png
 ┃ ┃ ┃ ┃ ┃ ┗ 📜tnw-logo.svg
 ┃ ┃ ┃ ┃ ┗ 📜favicon.ico
 ┃ ┃ ┃ ┣ 📂css
 ┃ ┃ ┃ ┃ ┗ 📜styles.css
 ┃ ┃ ┃ ┗ 📂js
 ┃ ┃ ┃ ┃ ┗ 📜scripts.js
 ┃ ┃ ┣ 📂templates
 ┃ ┃ ┃ ┣ 📂error
 ┃ ┃ ┃ ┃ ┣ 📜403.html
 ┃ ┃ ┃ ┃ ┣ 📜404.html
 ┃ ┃ ┃ ┃ ┗ 📜500.html
 ┃ ┃ ┃ ┣ 📂find
 ┃ ┃ ┃ ┃ ┣ 📜password.html
 ┃ ┃ ┃ ┃ ┗ 📜username.html
 ┃ ┃ ┃ ┣ 📜layout.html
 ┃ ┃ ┃ ┣ 📜list.html
 ┃ ┃ ┃ ┣ 📜loginForm.html
 ┃ ┃ ┃ ┣ 📜modify.html
 ┃ ┃ ┃ ┣ 📜oldlayout.html
 ┃ ┃ ┃ ┣ 📜read.html
 ┃ ┃ ┃ ┣ 📜register.html
 ┃ ┃ ┃ ┣ 📜signup.html
 ┃ ┃ ┃ ┗ 📜userInfo.html
 ┃ ┃ ┣ 📜application-oauth.properties
 ┃ ┃ ┣ 📜application.properties
 ┃ ┃ ┣ 📜email.properties
 ┃ ┃ ┣ 📜errors.properties
 ┃ ┃ ┣ 📜logback-spring.xml
 ┃ ┃ ┗ 📜messages.properties
 ┗ 📂test
 ┃ ┣ 📂java
 ┃ ┃ ┗ 📂com
 ┃ ┃ ┃ ┗ 📂example
 ┃ ┃ ┃ ┃ ┗ 📂noticeboard
 ┃ ┃ ┃ ┃ ┃ ┣ 📂controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardControllerTests.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜CommentControllerTests.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂eannotation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WithMockCustomUser.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardRepositoryTests.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentRepositoryTests.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmitterRepositoryTests.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberRepositoryTests.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜NotificationRepositoryTests.java  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜RedisRepositoryTest.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂sercurity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜WithMockCustomUserSecurityContextFactory.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜BoardServiceTests.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜CommentServiceTests.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜EmailServiceTests.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜MemberServiceTests.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜NotificationServiceTests.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📜DummyDataProvider.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜NoticeboardApplicationTests.java
 ┃ ┗ 📂resources
 ┃ ┃ ┗ 📜application.properties
 ```


</details>
<br/>

### 2.DB 설계
![erd](https://github.com/jeongmin0709/noticeboard/assets/121369135/beabc99c-c0bb-4ba5-bfe6-bfa64d06a902)

### 3.API 설계

#### 3.1 게시글 API
![게시글api](https://github.com/jeongmin0709/noticeboard/assets/121369135/12851469-d8e9-486f-b471-e4fdd5e43985)

#### 3.2 댓글 API
![댓글api](https://github.com/jeongmin0709/noticeboard/assets/121369135/094a5323-b14d-4438-a12c-90016fbae8d1)

#### 3.3 유저 API
![유저api](https://github.com/jeongmin0709/noticeboard/assets/121369135/61ce14c0-884c-4690-ab30-e6e917f9af4d)

#### 3.4 이미지 API
![이미지api](https://github.com/jeongmin0709/noticeboard/assets/121369135/1ae63037-0a52-4148-88a3-be4fd31a570f)

#### 3.5 알림 API
![알림api](https://github.com/jeongmin0709/noticeboard/assets/121369135/0cc27fd5-42e0-462a-b2ea-92d54bb7b92a)

## 주요 기능 설명
### 1.게시글
#### 1.1 게시글 목록 조회, 검색, 정렬
- 추천순, 조회순으로 정렬 기능
- 내글, 내댓글, 제목, 내용, 작성자, 제목+내용, 제목+내용+작성자 검색 기능
- 코드
  - repository 
    https://github.com/jeongmin0709/noticeboard/blob/cd17c8fb8507be3da805643cf5b5c87e1d2ed7f7/src/main/java/com/example/noticeboard/repository/boardrepository/BoardRepositoryImpl.java#L78
    검색 파라미터에 따라 where문이 변경되거나 내댓글 검색을 할때 join을 comment Table과 동적으로 해야해서 queryDsl을 사용하였습니다. 제목, 내용, 작성자 검색은 searchCondition함수를 통해 구현하였고 내글, 내댓글 검색은 myBoardOrComment함수를 통해 구현하였습니다.
    이미지 존재여부와 댓글 수를 표시하고 싶어서 처음에는 comment Table과 image Table을 조인 후 group by를 통해 구현하려 하였습니다. 하지만 모든 게시글에 대한 group by는 성능을 너무 악화 시켜서
    select 절의 서브쿼리로 해당 기능을 구현하였습니다. select절의 서브쿼리는 limit를 한뒤 실행되기때문에 성능이 많이 향상되었습니다.
  - service
    https://github.com/jeongmin0709/noticeboard/blob/2aabd4ed4ddf95a86510762a0da1814c92b6b68e/src/main/java/com/example/noticeboard/service/BoardServiceImpl.java#L48
  - PageRequestDto
    https://github.com/jeongmin0709/noticeboard/blob/2aabd4ed4ddf95a86510762a0da1814c92b6b68e/src/main/java/com/example/noticeboard/dto/PageRequestDTO.java#L14
  - PageResulteDto
    https://github.com/jeongmin0709/noticeboard/blob/2aabd4ed4ddf95a86510762a0da1814c92b6b68e/src/main/java/com/example/noticeboard/dto/PageResultDTO.java#L19
    pageResultDTO는 재사용성을 위해 Generics 타입을 이용하였습니다. makePageList함수를 통해 이전, 다음, 시작페이지, 끝페이지를 계산합니다
#### 1.2 게시글 조회 및 이전게시글 다음게시글 조회
  - 게시글 조회시 게시글과 댓글 수, 이전게시글과 다음게시글의 아이디와 제목을 조회
  - 코드
    - repositorty 
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/repository/boardrepository/BoardRepositoryImpl.java#L38
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/repository/boardrepository/BoardRepositoryImpl.java#L61
    getBoard 함수는 게시글과 댓글 수를, getPrevAndNextBoard는 이전글과 다음글을 조회하는 함수입니다. 이전글과 다음글을 구현할때 LEAD, LAG 윈도우함수등을 사용할까 했지만 native query는 되도록 지양하고 싶어서 다음과 같이 구현하였습니다.
    - service
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/service/BoardServiceImpl.java#L70
    getBoard 함수에서는 게시글과 댓글 수를 먼저 조회한 후, 이전글과 다음글으 조회합니다. 그리고 dto로 변환하고 conroller로 리턴하게 됩니다.
#### 1.3 게시글 추천 및 중복방지
  - 코드 
    - controller
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/controller/BoardRestController.java#L21
    게시글 추천은 자신의 게시글 추천이나 중복추천이 불가능해야하기때문에 처음에는 controller에서 접근을 막으려했습니다. 하지만 권한 확인을 위해서는 db데이터가 필요하기 때문에 service로 권한 확인을 위임하였습니다.
    - service
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/service/BoardServiceImpl.java#L10
    recommendBoard 함수에서는 자신의 게시글이나 중복 추천일경우 추천을 하지 못하도록 검사를 합니다. 중복추천의 경우 추천을하는 회원과 게시글은 다대다 관계이기때문에 memberBoard table을 만들어 1대다 다대1관계로 풀었습니다.
    중복추천을 확인할때 MemberBoard table에서 추천내역을 조회하고 있다면 예외를 터트리고 없다면 추천수를 증가시키고 memberBoard table에 추천내역을 저장하였습니다.
#### 1.4 게시글 조회수 및 중복 방지
  - 코드
    - controller
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/controller/BoardController.java#L57
    게시글 조회 요청이 오면 게시글 조회 후 조회수 증가 함수인 increaseViewNum함수를 호출합니다.
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/controller/BoardController.java#L130
    조회수 중복 방지는 쿠키를 이용합니다.
      1) request에서 "boardView"라는 쿠키가 있는지 확인합니다.
      2) 쿠키가 없다면 "boardview" 쿠키를 생성하고 쿠키안에 게시글 id를 저장하고 쿠키가 있다면 쿠키안에 게시글 id가 있는지 확인합니다.
      3) 게시글 id가 없다면 조회수를 증가시킨 후 쿠키에 게시글 id를 저장하고 있다면 조회수를 증가시키지 않습니다.  
### 2. 회원
  - 회원 서비스를 개발하기 위해서 spring security를 사용하였습니다. jwt방식과 session방식중 뭘 선택할까 고민하였는데 토이프로젝트에서는 session방식 더적합하다고 생각하여 session방식 방식을 사용하였습니다.
#### 2.1 일반 로그인
  - 코드
    - MemberDTO 
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/security/dto/MemberDTO.java#L20
    sping security 인증, 인가기능을 사용하기 위해서 UserDetails interface를 구현하였습니다
    - service
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/security/service/MemberDetailsService.java#L28
    로그인을 위해 UsernamePasswordAuthenticationFilter가 실행되는데 해당필터는 getAuthenticationManager에게 인증을 위임하고 UserDetailsService를 호출하기 때문에 UserDetailsService
    의 구현체 MemberDetailsService를 개발하였습니다.
    - loginSuccessHandler
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/security/handler/LoginSuccessHandler.java#L16
    제프로젝트에는 로그인화면으로가는 3가지 경우의 수가 있습니다.
      1) 로그인 하지않은 사용자가 로그인이 필요한 페이지를 요청(spring security가 강제인터셉트후 로그인 페이지로 redirect)
      2) 로그인 하지않은 사용자가 로그인이 필요한 비동기 요청(frontend에서 401 response를 받으면 location.href를 통해 로그인 페이지로 이동)
      3) 직접 로그인 링크를 눌러서 로그인 페이지로 이동   
      1)의 경우 이전 페이지가 HttpSessionRequestCache()저장되어 있고 2), 3)의 경우 로그인 페이지 요청시 header의 Referer 필드에 이전페이지가 저장되어있기때문에 session에 이전페이지를 저장한 후 사용하였습니다.
    - loginFailuerHandelr
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/security/handler/LoginFailureHandler.java#L25
    - interceptor
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/web/interceptor/LogInterceptor.java#L35
    로그인이 실패하면 실패 메세지를 로그인 페이지에 출력해야 하는데 처음에는 forward를 통해 해당 기능을 구현하려 하였습니다. 하지만 로그인은 post 요청이고 로그인 페이지는 get요청이기 떄문에 작동하지 않았습니다. 그래서
    두번째로는 redirect url에 실패메세지를 실어서 reirect를 했습니다. 실행은 잘되었지만 url에 긴 메시지가 직접적으로 들어가는게 이상하다고 생각하여 마지막으로 seession에 실패메시지를 저장하고 intercepter의 posthander 함수를 통해 실패
    메시지가 seession에 있으면 modelAndView에 실패 메시지 attribute를 추가하는 방식을 사용하였습니다. 검색해보니 제가 생각해낸 방식이 redirectattributes.addflashattribute와 비슷한 방식이였습니다.
#### 2.2 소셜 로그인
  - 소셜로그인은 oauth2를 사용해 구현하였습니다.
  - 네이버, 카카오, 구글 로그인이 가능힙낟.
  - 코드
    - service
    https://github.com/jeongmin0709/noticeboard/blob/7a91c730eb35b4b0cfbb0b4ec596b60f5bec95b5/src/main/java/com/example/noticeboard/security/service/Oauth2MemberDetailsService.java#L22C49-L22C73
    DefaultOAuth2UserService를 상속받아 Oauth2MemberDetailsService를 구현하였습니다. sper.loadUser를 통해 소셜 로그인 사용자 정보를 받아오고 해당 정보를 바탕으로 member table에서 memberEntity를 가져옵니다.
    최초로그인일 경우 member table에 저장하게 됩니다.
