# 치매 예방 및 인지 기능 강화 솔루션 프로젝트 : 기억지키미

### 제11회 SW개발보안 경진대회 본선 진출, 한국인터넷진흥원장상 수상

---

## 프로젝트 소개

### 문제 상황
- 한국 초고령화와 치매 인구 증가로 인한 사회적 문제 대두
- 노인 대상 지능 범죄 증가

### 제안 솔루션
1. **자가 진단 테스트**
   - 사용자 개인의 치매 위험도를 사전에 파악할 수 있도록 도움을 주는 솔루션
   - 추상 이미지 데이터를 활용한 퀴즈로 인지 기능(언어, 시공간, 실행계획 판단, 지남력) 파악
2. **생성형 AI 기반 퀴즈**
   - 사회적 트렌드 반영 및 디지털 범죄 예방 문제 제공
   - 노인층의 인지 훈련을 효과적으로 지원
3. **주변 치매 센터 정보 제공**
   - 사용자 위치 기반 주변 치매 센터 확인 기능
   - 치매 상담 전화 바로 연결 기능

---

## 주요 기능

1. **회원 가입 및 인증**
   - 휴대폰 번호 인증 및 계정 찾기 기능 제공
   - 강력한 비밀번호 규칙 설정 (대문자, 소문자, 숫자 포함 8자 이상)

2. **인지 능력 퀴즈 및 결과 분석**
   - 인지 능력별 퀴즈 생성 및 점수 반환
   - 퀴즈 결과를 통해 학습이 필요한 영역 파악 가능
   - 일자별 점수 변동을 마이페이지에서 확인 가능

3. **주변 치매 센터 정보 확인**
   - 사용자 정보 기반으로 치매 센터 및 프로그램 정보 제공
   - 클릭 시 지도에서 해당 위치 확인 가능
   - 치매 상담 전화 기능 (1899-9988로 바로 연결)

4. **이미지 파일 업로드 기능**
   - jpg 형식의 1MB 이하 파일만 업로드 가능
   - 파일명 비식별화(UUID) 및 실행 권한 제거를 통한 보안 강화
   - 파일 데이터 Base64 인코딩 전송

---

## 기술 스택 및 API

- **Backend**: Python, Flask
- **Frontend**: React.js
- **Database**: PostgreSQL
- **사용 API**:
  - Open API
  - SMS API
  - Kakaomap Maps API
  - OpenAI API
  - 공공데이터포털 (전국치매센터 표준 데이터)

---

## 설치 및 실행 방법

### 1. 환경 설정
- Python 3.8 이상 설치
- Node.js 16 이상 설치

### 2. 소스코드 클론
```bash
git clone https://github.com/username/project-name.git
cd project-name
```

### 3. 백엔드 실행
```bash
cd backend
pip install -r requirements.txt
python app.py
```

### 4. 프론트엔드 실행
cd frontend
npm install
npm start

### 참고자료
- 보건복지부 표준 데이터
- AI Hub (추상 이미지 데이터)
