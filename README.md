# :paperclip: Notice board Project
> bootstrap, jquery, spring boot로 개발한 반응형 게시판 프로젝트입니다.


## 개요
### 1.프로젝트 소개
인프런 강의와 책을통한 독학으로 관련 기술들을 학습한 후에 제작한 게시판 프로젝트입니다. 이것 저것 하기보다는 배운거라도 확실히 하자는 생각으로 개발하였습니다.  
http://ec2-54-180-25-181.ap-northeast-2.compute.amazonaws.com:8080  
e2c서버로 배포중
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
- 추천순, 조회순으로 정렬
- 내글, 내댓글, 제목, 내용, 작성자, 제목+내용, 제목+내용+작성자 검색
- 코드
  - repository 
    https://github.com/jeongmin0709/noticeboard/blob/cd17c8fb8507be3da805643cf5b5c87e1d2ed7f7/src/main/java/com/example/noticeboard/repository/boardrepository/BoardRepositoryImpl.java#L78
    검색 파라미터에 따라 where문이 변경되거나 내댓글 검색을 할때 comment Table과 동적으로  join을 해야해서 queryDsl을 사용하였습니다. 제목, 내용, 작성자 검색은 searchCondition함수를 통해 구현하였고 내글, 내댓글 검색은 myBoardOrComment함수를 통해 구현하였습니다.
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
    getBoard 함수에서는 게시글과 댓글 수를 먼저 조회한 후 이전글과 다음글을 조회합니다. 마지막으로 BoardDto로 변환한뒤 conroller에 리턴하게 됩니다.
#### 1.3 게시글 추천 및 중복방지
  - 코드 
    - controller
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/controller/BoardRestController.java#L21
    게시글 추천은 자신의 게시글 추천이나 중복추천이 불가능해야하기때문에 처음에는 controller에서 접근을 막으려했습니다. 하지만 권한 확인을 위해서는 db데이터가 필요하기 때문에 service로 권한 확인을 위임하였습니다.
    - service
    https://github.com/jeongmin0709/noticeboard/blob/00b8e19c20cd179d9b8fca163e49d66ca591c0f4/src/main/java/com/example/noticeboard/service/BoardServiceImpl.java#L10
    recommendBoard 함수에서는 자신의 게시글이나 중복 추천일경우 추천을 하지 못하도록 검사를 합니다. 중복추천의 경우 추천을하는 회원과 게시글은 다대다 관계이기때문에 MemberBoard table을 만들어 1대다 다대1관계로 풀었습니다.
    중복추천을 확인할때 MemberBoard table에서 추천내역을 조회하고 있다면 예외를 터트리고 없다면 추천수를 증가시킨 후 memberBoard table에 추천내역을 저장하였습니다.
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
    로그인이 실패하면 실패 메세지를 로그인 페이지에 출력해야 하는데 처음에는 forward를 통해 해당 기능을 구현하려 하였습니다. 하지만 로그인은 post 요청이고 로그인 페이지는 get요청이기 때문에 예외가 발생하였습니다. 그래서
    두번째로는 redirect url에 실패메세지를 추가한 후 reirect를 했습니다. 실행은 잘되었지만 url에 긴 샐피 메시지가 직접 들어가는게 이상하다고 생각하여 마지막으로 seession에 실패메시지를 저장하고 intercepter의 posthander 함수를 통해 실패
    메시지가 seession에 있으면 modelAndView에 실패 메시지 attribute를 추가하는 방식을 사용하였습니다. 검색해보니 제가 생각해낸 방식이 redirectattributes.addflashattribute와 비슷한 방식이였습니다.
#### 2.2 소셜 로그인
  - 소셜로그인은 oauth2를 사용해 구현.
  - 네이버, 카카오, 구글 로그인이 가능.
  - 코드
    - service
    https://github.com/jeongmin0709/noticeboard/blob/7a91c730eb35b4b0cfbb0b4ec596b60f5bec95b5/src/main/java/com/example/noticeboard/security/service/Oauth2MemberDetailsService.java#L28
    DefaultOAuth2UserService를 상속받아 Oauth2MemberDetailsService를 구현하였습니다. super.loadUser를 통해 소셜 로그인 사용자 정보를 받아오고 해당 정보를 바탕으로 member table에서 memberEntity를 가져옵니다.
    최초로그인일 경우 member table에 저장하게 됩니다.
#### 2.3 이메일 인증
  - 쿠기를 통해 인증번호를 저장할수도 있지만 frontend에서 인증번호를 가지고있는 것은 좋지 않다고 생각하여 redis memory DB를 이용하여 해당 기능을 구현하였습니다.
  - 코드
    - service
    https://github.com/jeongmin0709/noticeboard/blob/ab83b41c98f7c7b2495ce54c1f75a9adbf71afaa/src/main/java/com/example/noticeboard/service/EmailServiceImpl.java#L25
    JavaMeilSender를 이용해 랜덤으로 생성된 인증번호를 receiver에게 전송하게 됩니다. 그리고 이메일과 인증번호를 30분의 유효시간으로 redis에 저장합니다. 인증 요청이 오면 이메일을 통해 redis에서 인증번호를 가져오고 인증을 진행합니다.
### 3. 댓글
#### 3.1 댓글목록 조회
  - 댓글 목록을조회는 무한스크롤 방식을 사용하였습니다.
  - 코드
    - repository
    https://github.com/jeongmin0709/noticeboard/blob/a9d3d37374c1d65e1d0850eedec23a602b8cd2de/src/main/java/com/example/noticeboard/repository/commentRepository/CommentRepositoryImpl.java#L28
    무한스크롤 방식의경우 데이터의 전체 크기가 필요없고 다음 페이지가 존재하는지만 알면 되기때문에 댓글 목록과 답글 개수를 조회한 후 Column 개수가 pageSize 보다 작으면 hasNext=true 아니면 false로 구현하였습니다.
    마지막으로 Slice 클래스를 리턴합니다.
#### 3.2 답글목록 조회
  - 답글의경우 최대한 간단하게 comment Entity에 parent id를 추가하는 방식으로 구현하였습니다. 조회는 댓글 조회와 방식이 같습니다.
### 4. 알림
  - 알림 기능의 경우 서버에서 생성된 알림이 요청 없이도 사용자에게 전송되어야 하기때문에 기존의 httpProtocol로는 구현할수 없어 SSE(Server Send Message)를 사용하였습니다. WebSocket사용도 고려해 보았지만 서버 클라이언트 단방향
  통신만 있어도 알림기능을 구현할수있기 때문에 더가볍고 사용하기 쉬운 sse를 선택하였습니다.
#### 3.1 알림 구독
  - repository
  https://github.com/jeongmin0709/noticeboard/blob/a9d3d37374c1d65e1d0850eedec23a602b8cd2de/src/main/java/com/example/noticeboard/repository/EmitterRepositoryImpl.java#L11
  ConcurrentHashMap을 사용하여 멀티쓰레딩에서도 안전하게 만들었습니다. SseEmitter 저장, 조회, 삭제를 수행합니다.
  - service
  https://github.com/jeongmin0709/noticeboard/blob/a9d3d37374c1d65e1d0850eedec23a602b8cd2de/src/main/java/com/example/noticeboard/service/NotificationServiceImpl.java#L46
  알림 구독 요청이오면 emitter id와 emitter을 생성합니다. emitter id를 username + system.currentTimeMillis() 로 하는 이유는 브라우저에서 최대 6개의 emitter을 가질수 있기 때문입니다. emmiter을 저장한 후 최초로 알림목록을 전송하게됩니다.
#### 3.2 알림 전송
  - service
  https://github.com/jeongmin0709/noticeboard/blob/a9d3d37374c1d65e1d0850eedec23a602b8cd2de/src/main/java/com/example/noticeboard/service/NotificationServiceImpl.java#L91
  알림전송은 모듈간의 의존성을 낮추기 위해  @EventListener 어노테이션을 사용하였습니다. 댓글 등록이나 추천을 하면 알림 이벤트가 발행 되고 해당 함수가 이벤트를 처리하게 됩니다. 알림 종류에 따라 알림을 생성하고 receiver에게 알림을 전송합니다.
#### 3.2 알림 읽음처리
  - service
  https://github.com/jeongmin0709/noticeboard/blob/a9d3d37374c1d65e1d0850eedec23a602b8cd2de/src/main/java/com/example/noticeboard/service/NotificationServiceImpl.java#L72
  사용자에게 읽지 않은 알림의 개수를 표시해야하기 때문에 Notification Entity는 isRead 필드를 가지고 있는데 해당 필드를 업테이트 하는 함수입니다.
### 예외처리
  - ControllerAdvice
  https://github.com/jeongmin0709/noticeboard/blob/a9d3d37374c1d65e1d0850eedec23a602b8cd2de/src/main/java/com/example/noticeboard/exception/exception_handler/RestControllerExceptionHandler.java#L33  
  - ErrorCode
  https://github.com/jeongmin0709/noticeboard/blob/a9d3d37374c1d65e1d0850eedec23a602b8cd2de/src/main/java/com/example/noticeboard/exception/ErrorCode.java#L9
  - ExceptionDTO
  https://github.com/jeongmin0709/noticeboard/blob/a9d3d37374c1d65e1d0850eedec23a602b8cd2de/src/main/java/com/example/noticeboard/exception/ExceptionDTO.java#L17
  예외처리의 경우 전역으로 예외를 처리할 수 있는 @ControllerAdivce 어노테이션을 사용하였습니다. 예외를 전역으로 처리하려다보니 기존의 Exception들 만으로는 힘들다고 판단하여 CustomException을 사용하였습니다. 또한 예외에 대한
  Reponse메시지의 일관성이 중요하다는 내용을 강의에서 배웠기때문에 ExceptionDTO를 정의하였습니다. ExceptionDTO에는 서버에서 정의한 code, message, status, fieldErrorList 4개로 구성됩니다.
## 마치며
  여러 강의와 책을통해 이론으로 배운 내용들을 직접 개발하면서 배울점도 많고 느끼는 점도 많았습니다.   
  첫번째로 테스트코드의 중요성이였습니다. 개발초기에는 테스트코드를 작성을 제대로 하지않고 개발을 하다가 코드가 점점 많아지면서 어떤 모듈에서 문제가 발생했는지 찾기 점점 힘들어졌습니다. 이런식으로는 안되겠다는 생각이 들어 
  테스트코드를 작성하고 확인하니 눈에 잘뛰지 않았던 문제점이나 실수한 부분을 많이 고칠수 있었고 개발시간도 많이 줄일수 있었습니다.  
  두번쨰로는 어떤설계가 좋은 설계인지 많은 생각을 할 수 있었습니다. 모듈간의 결합도를 낮추기위해 어떻게하는게 좋은지, 예외를 어떤상황에 발생시키고 어떻게 처리를 할지 등등 많은 생각을 하게 하였습니다. 결합도가 높은
  모듈은 테스트코드 작성도 어렵게 만든다는것도 느낄수 있었습니다.  
  아쉬운점도 있었습니다. 혼자하는 프로젝트이다보니 트레픽 처리, 최적화같은 고민이 부족한 프로젝트가 되었습니다. 다음 프로젝트는 트래픽 처리나 최적화같은 문제들을 고려하는 프로젝트를 해보고 싶습니다.  
  이번 프로젝트는 저에게 많은 도움을 주었고 발전할 방향을 알려주는 프로젝트가 되었던것 같습니다.  
  긴글 읽어주셔서 감사합니다.
  
  
