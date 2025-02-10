## SMTP 이메일 인증

각종 프로젝트에서 무분별한 회원가입을 방지하기 위해 구글 STMP를 이용해보자.
SMTP란 Simple Mail Transfer Protocol의 약자로, 인터넷을 통해 email을 주고받을 떄 사용되는 프로토콜이다.

1. 구글 이메일 설정
설정 - 전달 및 POP/IMAP - IMAP 사용으로 변경
2. 계정 보안키 받아오기
보안 - 2단계 인증 - 앱 비밀번호 생성
3. 로직 구현
* "email/send" - 인증 이메일 발송
* "email/check" - 인증 이메일 검증
