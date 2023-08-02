# :paperclip: Notice board Project
> bootstrap, jquery, spring boot로 개발한 반응형 게시판 프로젝트입니다.

## 목차
- 개요
  - 프로젝트 소개
  - 개발 환경
    - 백엔드
    - 프론트엔드
  - 개발 기간  
  - 개발 인원
- 구조 및 설계
  - 프로젝트 구조
  - DB 설계
  - API 설계
- 기능 설명
  - 게시글
    - 게시글 등록
    - 게시글 수정
    - 게시글 삭제
    - 게시글 목록 보기
    - 게시글 상세 보기
    - 게시글 추천
  - 회원
    - 회원가입
    - 로그인
    - 소셜 로그인
    - 아아디 및 비밀번호 찾기
    - 이메일 및 비밀번호 변경
    - 이메일 인증
  - 댓글
     - CRUD
     - 페이징
     - 답글
     - 추천
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

## 기능 설명
### 1. 게시글
#### 1.1 게시글 등록
  - 제목, 내용과 함께 이미지도 6개까지 등록 가능
  - summernote web editor 적용
  - 로그인한 사용자만 게시글 등록 가능
#### 1.2 게시글 삭제
  - 게시글 id를 이용해 삭제
  - 게시글 삭제시 댓글, 알림, 추천 내역까지 함꼐 삭제
  - 자신의 게시글만 삭제 가능
#### 1.3 게시글 수정
  - 제목, 내용, 이미지 수정 가능
  - summernote web editor 적용
  - 자신의 게시글만 수정 가능
#### 1.4 게시글 목록 보기
  - 페이징을 사용하여 한페이지에 20개의 제목, 작성작, 추천수, 조회수, 작성일, 이미지 존재여부, 댓글존재여부를 가져옴
  - 이미지 존재여부, 댓글존재여부는 아이콘을 이용해 나타냄
  - 추천순, 조회순으로 정렬 가능
  - 내글, 내댓글, 제목, 내용, 제목+내용, 제목+내용+작성자로 검색 가능
    - 내글, 내댓글 기능은 로그인한 사용자만 사용가능
#### 1.5 게시글 상세보기
  - 게시글 전체와 댓글수, 이전페이지, 다음페이지를 가져옴
  - 게시글 상세보기를 클릭하면 조회수 증가
  - 쿠키를 이용하여 조회수 중복증가 방지
    1) 게시글 상세페이지 요청이오면 게시글 조회 쿠키가 있는지 확인
    2) 쿠키가 없다면 조회수를 증가시키고 쿠키를 생성, 쿠키에 게시글 id를 추가한 후 응답
    3) 쿠키가 있다면 게시글 조회 쿠키에 현재 조회한 게시글 id가 잇는지 확인
    4) 없다면 조회수를 증가시키고 쿠키에 게시글 id를 추가한 후 응답
    5) 있다면 조회수 증가 x
      

    
    
    
  
