<div align="center">
  <img src="https://github.com/user-attachments/assets/dfa5358d-f3dd-4220-9691-f7525854a1b7" alt="icon" width="200px"/>
  <h1>스티다 - 스포츠 티켓 다이어리</h1>
  <p><b>스포츠 경기의 관람 기록을 남기고, 관리할 수 있는 모바일 다이어리 앱</b></p>
</div>

<br />

## 📚 스티다 소개
**모바일에서 간편하게 작성하고 관리할 수 있는 "스포츠 경기 다이어리" 앱**

포스트 코로나 이후로 야구, 축구 등 스포츠 경기 직관에 대한 수요가 크게 증가하면서, 직관 후 SNS를 통해 경기 후기를 공유하거나 직관 다이어리를 작성하는 문화가 보편화 되었습니다.

시중에 스포츠용 종이 다이어리가 존재하나, 실시간으로 빠르게 기록하거나 공유하기 어렵습니다.
또한 이긴 경기나 특별했던 경기를 필터링해 확인하기에도 불편함이 있습니다.

스티다는 경기 관람 후 **간편하게 핸드폰으로 직관 다이어리를 작성하고 관리**할 수 있는 스포츠용 다이어리 앱입니다.


### ✨ 스티다의 주요 기능
- 언제 어디서든 스포츠 관람 다이어리를 작성할 수 있습니다.
- 월별 보기 기능을 통해 경기 관람 기록을 한 눈에 파악할 수 있습니다.
- 관람 기록은 티켓화 되어 이미지로 출력이 가능합니다.
- 티켓 필터링, 즐겨찾기 기능을 통해 원하는 티켓을 빠르게 확인할 수 있습니다.
- 마이팀의 승률 통계를 확인할 수 있습니다.

<br />

> **↘️ [스티다 프로젝트 문서](https://lavish-bay-be0.notion.site/Project-57d55c0dc36c4855be6c5fe5e7ec4c75)**
>
> 사용자 요구사항 명세서를 비롯하여, 프로젝트를 구현하며 집중한 부분, 자주 보였던 에러 등, 개발 과정에 대해 작성한 문서입니다.  


<br />

## 🚩 팀 소개
- **2인 프로젝트** | 이유진(백엔드), 이유정(프론트엔드)
    - **이유진**: 백엔드 아키텍처 및 API 설계, 개발, 기획
    - **이유정**: UI/UX 및 디자인 제작, 기획, 프론트엔드 개발
- 2024.07 ~ 2024.08

## ⛳️ 사용 스택
### 프론트엔드
- **개발 언어 및 프레임워크** | Dart, Flutter
- **상태 관리 라이브러리** | GetX
- **로컬 DB** | ISAR

### 백엔드
- **개발 언어 및 프레임워크** | Java17, Spring Framework(Springboot), Spring Data JPA
- **데이터베이스** | MySQL
- **배포** | AWS EC2(API 서버 및 bastion 서버), AWS RDS(데이터베이스), AWS S3(이미지 저장)

<br />

## 🚀 API 문서
**[↘️ https://stida.gitbook.io/stida-api-docs/](https://stida.gitbook.io/stida-api-docs/)**

<div align="center">
    <img width="600px" alt="stida_apidocs" src="https://github.com/user-attachments/assets/99e99d84-afa8-4771-bdb5-e97c12b1631a">
</div>

<br>
<br>

## 🚀 아키텍처 및 스키마

### 백엔드 아키텍처

<div align="center">
    <img width="800px" alt="stida_architecture_1" src="https://github.com/user-attachments/assets/7a63a559-ef57-4200-a50c-cace8580286d">
</div>
<div align="center">
    <img width="800px" alt="stida_architecture_2" src="https://github.com/user-attachments/assets/0a0046ba-7492-4a92-8c42-427c589b903f">
</div>

<br>
<br>

### 데이터베이스 스키마

<div align="center">
    <img width="800px" alt="stida_schema" src="https://github.com/user-attachments/assets/78a611a7-7f34-4c22-99d9-2f136c739cff">
</div>

<br>
<br>

## 🚀 사용 방법
#### 직접 사용해 보고 싶으시다면?
- 안드로이드용 APK 파일 다운로드 후 실제 사용이 가능합니다.
  ➡️ [GoogleDrive](https://drive.google.com/file/d/1bA9DIQQeE_0ptbunPMsKjGgbsIPGxSJz/view)

#### Flutter와 AMD가 설치되어 있다면? (VSCode 기준)
1. 스티다 프로젝트를 Clone해 주세요.
2. 터미널 > `flutter pub get` 실행해 주세요.
3. 안드로이드 에뮬레이터 디바이스를 연동해 주세요.
4. `flutter run` 혹은, `main.dart`에서 `▶️`를 클릭하여 스티다 프로젝트를 실행하세요.

_현재 앱스토어 출시를 준비 중이며, 비공개 사용자 피드백을 반영한 업데이트를 계획하고 있습니다._

<br />

## 🚀 주요 기능
- 🌟 **간단한 경기 기록을 작성 및 기록 티켓 발급**
    - 팀 이름 / 스코어만 적어도 작성 가능 (0점인 경우 팀 이름만으로도 가능)
    - 다이어리의 경우, 작성하고 싶은 위젯만 선택 가능

      <img src="https://github.com/user-attachments/assets/649ac8f1-f104-4113-bf45-b72375f5308b" alt="기록 작성" width="350px"/>

<br />
<br />
<br />

- 🌟 **경기 기록 수정 및 삭제**

  <img src="https://github.com/user-attachments/assets/0d6b9112-4345-46b1-86d9-3ed02903df94" alt="수정 삭제" width="350px"/>

<br />
<br />
<br />

- 🌟 **한 달간의 관람 현황을 한눈에 확인 가능**
    - 오늘자 경기 작성 시, 하단의 기록 작성 버튼(연필 아이콘)이 기록 페이지 이동 버튼(메모 아이콘)으로 변경
      
    <img src="https://github.com/user-attachments/assets/5283b65c-44d8-45d1-83cc-31d94ab3fb92" alt="티켓 저장" width="350px"/>

<br />
<br />
<br />

- 🌟 **관람 티켓 갤러리에 저장**
    - 관람 기록 및 다이어리 둘 다 저장 가능
    
    <img src="https://github.com/user-attachments/assets/9df2e478-7a1a-447b-ae39-35f58886eb6c" alt="티켓 저장" width="350px"/>

<br />
<br />
<br />

- 🌟 **원하는 것만 볼 수 있는 리스트 필터 기능 4종 및 2가지 View 옵션**
    - 필터 기능 (전체보기 및 직관만, 집관만, 이긴 경기만 등)
    - 순서 (최근순, 오래된순, 작성순) 정렬, 좋아요 및 검색 기능
    
    <img src="https://github.com/user-attachments/assets/a8269e47-711b-4093-8afd-2cf1456ff0f2" alt="필터 기능" width="350px"/>

<br />
<br />
<br />

- 🌟 **직관 및 집관 통계 기능 제공**
    
    <img src="https://github.com/user-attachments/assets/aabf617b-6744-43c8-b99e-af2f47b26e2d" alt="통계" width="350px"/>

<br />
<br />
<br />

- 🌟 **소셜 로그인(구글) 및 로그아웃**

    <img src="https://github.com/user-attachments/assets/e9d5a0d7-ba22-43b5-9501-10d5a2c5a5b5" alt="로그인 로그아웃" width="350px"/>

<br />
<br />
<br />

- 🌟 **이전 데이터 불러오기**
    - 로그인 시, 이전에 썼던 데이터 불러올 수 있음
    
    <img src="https://github.com/user-attachments/assets/f91d81e5-1aee-4865-806b-26d49d641b7e" alt="데이터 불러오기" width="350px"/>


---

감사합니다! 😊  