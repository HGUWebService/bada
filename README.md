# 바라던 바다

포항 해수욕장의 수온·물때(조석)·파고·풍속 같은 바다 상태를 공공데이터 API에서 수집해 MariaDB에 저장하고, Spring Boot REST API와 React 화면으로 보여주는 웹 서비스입니다.

## 폴더 구조

```
bada/
├─ backend/    Spring Boot (Java 17, Gradle) REST API 서버
├─ frontend/   Vite + React 프론트엔드
├─ docs/       ERD·REST API 설계 자료, 협업 규칙(collaboration.md)
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


## 배포 URL

| 구분 | URL |
|---|---|
| Frontend (Vercel) | 배포 후 추가 |
| Backend (Render) | 배포 후 추가 |
| Swagger UI | 배포 후 추가 (`/swagger-ui/index.html`) |

## 팀원과 담당 기능

| 이름 | 담당 |
|---|---|
| 송준선 | Backend: 해변·관측 기록 CRUD, Query Method, 공통 예외 처리, Swagger 테스트, Spring Security 로그인, 복합 검색, Render 배포 |
| 손동현 | Frontend: React 화면·API 연동, Vercel 배포 / Backend: 파일 업로드·다운로드, 공공데이터 수집 |

## 협업 규칙

Git flow(Fork → feature 브랜치 → PR → 리뷰 → merge), 커밋 메시지, PR·코드 리뷰 규칙은 [docs/collaboration.md](docs/collaboration.md)를 참고하세요.
