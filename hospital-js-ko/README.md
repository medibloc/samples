## MediBloc Javascript library sample project
본 프로젝트는 [medjs](https://github.com/medibloc/medjs), [phr-js](https://github.com/medibloc/phr-js) Javascript library 를 이용하여

병원 측에서 [Panacea(MediBloc 블록체인)](https://github.com/medibloc/go-medibloc) 에 접근, 데이터 진본 증명을 하는 방법을 제시 합니다.

### 설치
```bash
npm install
```

### 실행
```bash
npm start
```

### 설명
병원 측에서 메디블록 블록체인을 이용하여 데이터 진본 증명을 수행 하기 위해서는 src/hospital.js 파일의 Hospital 클래스에 속한 기능을 구현 해야 합니다.

src/index.js 파일의 run 함수에는 병원, MediBloc, 사용자(환자)가 데이터를 주고 받는 순서가 기술 되어 있습니다.

병원이 Hospital 클래스의 기능을 구현하면, MediBloc 과 사용자(환자)는 src/index.js 파일에 제시된 방식으로 블록체에 데이터를 기록한 뒤 병원에 데이터를 전달하여 데이터의 진본 증명을 수행 할 수 있습니다.
