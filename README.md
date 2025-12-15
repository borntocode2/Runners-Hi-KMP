# Runner's Hi KMP Repository

러너스하이(Runner's Hi) 서비스의 서버/모바일 레포지토리입니다.

KMP에 대한 가이드는 [KMP_GUIDE.md](https://github.com/GoodSpace-Kr/Runners-Hi-KMP/blob/main/KMP_GUIDE.md)에서 제공합니다.

## 초기 설정

### 1. local.properties 설정

프로젝트를 클론한 후, `local.properties.example` 파일을 복사하여 `local.properties` 파일을 생성하고 필요한 값을 입력하세요:

```bash
cp local.properties.example local.properties
```

`local.properties` 파일에 다음 값들을 설정해야 합니다:

- **MAPS_API_KEY**: Google Maps API 키
  - 발급 방법: [Google Cloud Console](https://console.cloud.google.com/google/maps-apis)
  - 1. 프로젝트 생성 또는 선택
  - 2. "APIs & Services" > "Library"에서 "Maps SDK for Android" 활성화
  - 3. "APIs & Services" > "Credentials"에서 API 키 생성
  - 4. 생성한 API 키를 `MAPS_API_KEY`에 입력

> ⚠️ **주의**: `local.properties` 파일은 Git에 커밋되지 않습니다. 각 개발자가 자신의 로컬 환경에 맞게 설정해야 합니다.

## Conventions

1. 커밋 작성은 [AngularJS Git Commit Message Conventions](https://gist.github.com/stephenparish/9941e89d80e2bc58a153)를 따른다.(이슈 번호를 커밋 메시지에 명시하지 않는다)
2. 작업은 클론 레포지토리에서 진행하고, PR을 통해 main 브랜치(혹은 develop 브랜치)에 병합한다.
3. 브랜치 이름은 `<모듈명>/<기능>` 형식으로 구성한다. (ex_ `server/google-oauth`)
