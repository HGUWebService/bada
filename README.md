# 포항바다지금 (Pohang Sea Now)

포항 해수욕장의 수온·물때(조석)·파고·풍속 같은 바다 상태를 공공데이터 API에서 수집해 MariaDB에 저장하고, Spring Boot REST API와 React 화면으로 보여주는 웹 서비스입니다.

## 폴더 구조

```
bada/
├─ backend/    Spring Boot (Java 17, Gradle) REST API 서버
├─ frontend/   Vite + React 프론트엔드
├─ docs/       ERD 및 REST API 설계 자료
├─ .github/    PR 템플릿
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
| `feature/<기능명>` | 기능 개발. 예: `feature/beach-crud` |
| `fix/<내용>` | 버그 수정. 예: `fix/tide-time-format` |

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
- 승인 후 PR을 merge 합니다.
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
