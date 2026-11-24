# 바라던 바다

포항 해수욕장의 수온·물때(조석)·파고·풍속 같은 바다 상태를 공공데이터 API에서 수집해 MariaDB에 저장하고, Spring Boot REST API와 React 화면으로 보여주는 웹 서비스입니다.

## 폴더 구조

```
bada/
├─ backend/    Spring Boot (Java 17, Gradle) REST API 서버
├─ frontend/   Vite + React 프론트엔드
├─ docs/       ERD 및 REST API 설계 자료
├─ .github/    PR·이슈 템플릿, CI(GitHub Actions)
├─ .gitignore
└─ README.md
```

## 실행 방법

### Backend

필요 환경: Java 17, MariaDB

DB 접속 정보는 환경 변수로 지정합니다. 지정하지 않으면 괄호 안의 기본값이 사용됩니다.

| 환경 변수 | 설명 | 기본값 |
|---|---|---|
| `DB_URL` | JDBC URL | `jdbc:mariadb://localhost:3306/bada` |
| `DB_USERNAME` | DB 사용자 | `bada` |
| `DB_PASSWORD` | DB 비밀번호 | (빈 값) |
| `PORT` | 서버 포트 | `8080` |

```bash
cd backend
export DB_URL=jdbc:mariadb://localhost:3306/bada
export DB_USERNAME=bada
export DB_PASSWORD=비밀번호
./gradlew bootRun
```

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- 비밀번호·API 키는 절대 커밋하지 마세요. (`.env` 파일은 `.gitignore`에 포함되어 있습니다.)

### Frontend

필요 환경: Node.js 20 이상

```bash
cd frontend
npm install
npm run dev
```

- 개발 서버: http://localhost:5173

## Git flow 협업 규칙

### 브랜치

| 브랜치 | 용도 |
|---|---|
| `main` | 배포 브랜치. Vercel(프론트엔드)·Render(백엔드) 자동 배포 대상 |
| `develop` | 통합 브랜치. 저장소의 **기본 브랜치** |
| `feature/<이슈번호>-<기능명>` | 기능 개발. 예: `feature/12-beach-crud` |
| `fix/<이슈번호>-<내용>` | 버그 수정. 예: `fix/15-tide-time-format` |

- 작업은 항상 GitHub Issue에서 시작합니다. 설계보고서의 기능 요구사항(F1~F7)을 이슈로 만들고, 이슈 번호를 브랜치 이름에 넣습니다.

- `main`, `develop`에는 직접 push할 수 없습니다. 반드시 PR로 병합하며, **다른 팀원 1명의 승인**이 필요합니다.

### 작업 흐름 (Fork 방식)

**1. 처음 한 번만: Fork 후 내 컴퓨터로 clone**

GitHub에서 `HGUWebService/bada` 저장소의 **Fork** 버튼을 눌러 내 계정으로 fork합니다.

```bash
git clone https://github.com/<내-GitHub-ID>/bada.git
cd bada
git remote add upstream https://github.com/HGUWebService/bada.git
git fetch upstream
git switch develop        # fork에 develop이 없다면: git switch -c develop upstream/develop
git branch -u upstream/develop develop
git remote -v             # origin = 내 fork, upstream = HGUWebService/bada
```

**2. 기능 브랜치 만들기 (항상 최신 upstream/develop에서 시작)**

```bash
git fetch upstream
git switch -c feature/beach-crud upstream/develop
```

**3. 작업 후 커밋**

```bash
git add .
git commit -m "feat: 해수욕장 목록 조회 API 추가"
```

**4. 내 fork로 push**

```bash
git push -u origin feature/beach-crud
```

**5. Pull Request 열기**

GitHub에서 `<내-GitHub-ID>/bada:feature/beach-crud` → `HGUWebService/bada:develop` 으로 PR을 엽니다. PR 템플릿을 채워주세요.

**6. 리뷰 및 병합**

- 다른 팀원이 리뷰하고 **Approve** 합니다. (승인 1개 필수)
- CI(backend·frontend 빌드)가 통과해야 merge할 수 있습니다.
- 승인 후 **Squash and merge**로 병합합니다. (아래 Pull Request 규칙 참고)
- 리뷰 후 새 커밋을 push하면 기존 승인은 무효가 되어 다시 승인이 필요합니다.

**7. 병합 후 내 fork 동기화**

```bash
git fetch upstream
git switch develop
git merge upstream/develop
git push origin develop
```

다 쓴 기능 브랜치는 정리합니다.

```bash
git branch -d feature/beach-crud
git push origin --delete feature/beach-crud
```

### 배포 (Release)

배포할 때는 `HGUWebService/bada`에서 `develop` → `main` PR을 열고, 승인 후 merge 합니다. `main`에 병합되면 Vercel·Render가 자동으로 배포합니다.

### 커밋 메시지 규칙

`타입: 짧은 요약` 형식으로 작성합니다. 요약은 한국어 또는 영어 모두 괜찮습니다.

| 타입 | 의미 | 예시 |
|---|---|---|
| `feat:` | 새 기능 | `feat: 수온 조회 API 추가` |
| `fix:` | 버그 수정 | `fix: 물때 시간 포맷 오류 수정` |
| `docs:` | 문서 수정 | `docs: README 실행 방법 보완` |
| `refactor:` | 동작 변화 없는 코드 개선 | `refactor: BeachService 메서드 분리` |
| `test:` | 테스트 추가·수정 | `test: BeachController 테스트 추가` |
| `chore:` | 빌드·설정 등 기타 작업 | `chore: springdoc 버전 업데이트` |

- 한 커밋에는 한 가지 일만 담습니다. 기능 추가와 포맷 정리를 섞지 않습니다.

## Pull Request 규칙

- PR 하나에는 기능 하나, 변경 300줄 안팎. Entity·Repository·Service·Controller를 한 번에 올리지 말고 2~3개로 나눕니다.
- 올리기 전에 GitHub의 **Files changed**를 직접 한 번 봅니다(셀프 리뷰). 디버그 출력, 주석 처리한 코드는 여기서 지웁니다.
- 본문에 `Closes #이슈번호`를 적으면 merge할 때 이슈가 자동으로 닫힙니다.
- merge 방식

| 방향 | 방식 | 이유 |
|---|---|---|
| `feature/*` → `develop` | **Squash and merge** | develop 기록이 기능당 커밋 1개로 정리됨 |
| `develop` → `main` | **Create a merge commit** | squash하면 두 브랜치 기록이 갈라져 다음 배포 때 충돌이 남 |

- merge된 브랜치는 GitHub에서 자동 삭제됩니다.

## 코드 리뷰 규칙

- PR이 올라오면 **24시간 안에** 리뷰합니다.
- 코멘트 앞에 강도를 붙입니다.

| 표시 | 의미 |
|---|---|
| `[필수]` | 고쳐야 merge 가능 |
| `[제안]` | 반영은 선택 |
| `[질문]` | 이해를 위한 확인 |

- 리뷰 때 확인할 것
  - 기능 요구사항(F번호)대로 동작하는가
  - HTTP 상태 코드와 DTO 필드가 API 명세(`docs/`)와 같은가
  - Entity를 그대로 응답하지 않는가
  - 인증키·비밀번호 같은 비밀값이 없는가
- "LGTM"만 남기지 말고 근거 있는 코멘트를 최소 하나 남깁니다. 리뷰 기록은 설계보고서의 "코드 리뷰 내용"에 그대로 쓰입니다.

## 완료 기준 (Definition of Done)

기능은 아래 4가지를 모두 만족해야 "완료"입니다.

1. Swagger 또는 화면에서 예상 결과 확인
2. 다른 팀원의 리뷰 승인 + CI 통과
3. `develop`에 merge
4. API가 바뀌었다면 `docs/`의 명세 갱신

## API 계약과 코드 규칙

- **명세 먼저**: `docs/`의 API 명세와 DTO 필드명을 먼저 합의하고 그대로 구현합니다. 명세를 바꿀 때는 문서를 먼저 고칩니다.
- **프론트엔드 병행 개발**: 백엔드 API가 완성되기 전에는 명세와 같은 모양의 가짜 데이터로 화면을 만들고, 이후 실제 API 호출로 바꿉니다.
- **오류 응답 형식 통일**: 모든 오류는 `{status, code, message, timestamp}` 형식으로, `@RestControllerAdvice` 한 곳에서 처리합니다.
- **Backend**: `@Transactional`은 Service에만 둡니다. Controller는 DTO만 주고받습니다. 설정값은 `application.yml`과 환경 변수로만 넣습니다.
- **Frontend**: API 호출은 `src/api/`에 모읍니다. 서버 주소는 `VITE_API_BASE_URL` 환경 변수로 받고, 코드에 Render 주소를 직접 쓰지 않습니다.

## 비밀 정보 관리 (Public 저장소 주의)

- 공공데이터 인증키, DB 비밀번호는 **절대 커밋하지 않습니다.** Render·Vercel 환경 변수와 로컬 `.env` 파일에만 둡니다.
- 실수로 커밋했다면 기록 삭제보다 **키 재발급이 먼저**입니다. 공개 저장소는 커밋을 지워도 이미 복사됐을 수 있습니다.

## 상태 공유

하루 한 번 팀 채팅에 세 줄로 공유합니다: 어제 한 일 / 오늘 할 일 / 막힌 것.
