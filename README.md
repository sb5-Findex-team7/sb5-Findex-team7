# 7팀

## 팀원 구성
- 규환 (https://github.com/chagyuhwan)
- 규성 (https://github.com/gyural)
- 주환 (https://github.com/parkjoohwan)
- 준희 (https://github.com/Jelo777)

## 프로젝트 소개


## 파일구조

├── 📂 config
│    ├── AppConfig.java    # 앱 config 처리
│
├── 📂 exception
│    ├── ControllerAdvice.java     # 공통 예외 처리
│
├── 📂 mapper
│    ├── userMapper.java       # mapper설정
│
├── 📂 scheduler
│    ├── ControllerAdvice.java     # 공통 예외 처리
│
├── 📂 storage
│    ├── LocalStoarge.java         # 로컬 스토리지
│
├── 📂 controller
│    ├── MemberController.java      # 회원 관련 요청 처리
│    ├── SearchController.java      # 검색 관련 요청 처리
│
├── 📂 service
│    ├── MemberService.java         # 회원 비즈니스 로직
│    ├── SearchService.java         # 검색 비즈니스 로직
│
├── 📂 repository
│    ├── MemberRepository.java      # 회원 데이터 접근
│    ├── SearchRepository.java      # 검색 데이터 접근
│
├── 📂 dto
│    ├── 📂 request
│    │    ├── MemberRequest.java    # 회원 요청 DTO
│    │    ├── SearchRequest.java    # 검색 요청 DTO
│    │
│    ├── 📂 response
│    │    ├── MemberResponse.java   # 회원 응답 DTO
│    │    ├── SearchResponse.java   # 검색 응답 DTO
│
├── 📂 domain
│    ├── 📂 entity
│    │    ├── Member.java           # 회원 엔티티
│    │
│    ├── 📂 enums
│    │    ├── MemberRole.java       # 회원 역할 Enum
│    │    ├── SearchType.java       # 검색 타입 Enum