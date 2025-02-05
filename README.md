<img src="https://capsule-render.vercel.app/api?type=waving&color=9172EC&height=200&section=header&text=COLLAVORE%&fontSize=40&animation=fadeIn&fontAlign=80&fontAlignY=36" />

## ✅ 注目ポイント

1. spring MVC 패턴과 3계층 아키택처를 활용
   - 사용목적 : 

```
📁 프로젝트 구조 예시
src/main/java/com/example/project
├── controller  (Controller - MVCの C)
│   ├── UserController.java
├── service  (Service 계층 - 3계층의 중간)
│   ├── UserService.java
│   ├── UserServiceImpl.java
├── repository  (DAO 계층 - 3계층의 데이터 처리)
│   ├── UserRepository.java
│   ├── UserMapper.xml (MyBatis 사용 시)
├── model  (DTO / VO / Entity - MVC의 M)
│   ├── User.java

```

<div>
  <h1>✉<i>&nbsp 電子決裁</i></h1>
</div>  

### 主要機能
  - <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/create_template.md">テンプレート生成</a>
    1. Naver smart editor 2.0 apiを利用して電子決裁のテンプレート生成
  - <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/create_approval.md">電子決裁文書起案</a>
    1. 결재를 요철할 승인자를 4명까지 선택
    2. 문서에 사용할 템플릿을  Naver smart editor 2.0 api로 호출
  - <a href="https://github.com/leewoosang-hub/CollaVore/tree/master/EDSM.md">電子決裁文書決裁</a>
    1. 본인이 결재해야 할 문서를 선택하야 승인 혹은 반려
    2. 결재자의 상태에 따라 문서의 상태 갱신

### 他の機能
  - テンプレート修正
  - テンプレート削除
  - テンプレート項目整列
  - 電子決裁文書修正
  - 電子決裁文書削除
    <br>프로시저를 활용한 전자결재 문서 및 결재자 데이터 동시 삭제
    - 사용목적
      전자결재 테이블(PK)이 결재자 테이블에서 외래키(FK)로 참조되고 있는 구조에서, 데이터 무결성을 유지하면서 삭제 처리<br>
      수작업 없이 일괄 삭제 가능하도록 자동화하기 위해 사용
    - 활용기술 : Oracle PL/SQL 프로시저(Procedure) 활용
                MyBatis의 CALL명령어를 활용하여 프로시저 호출 
  - 電子決裁文書項目整列


### 利用 API
  - <a href="https://naver.github.io/smarteditor2/demo/">naver smart editor 2.0 demo
  - <a href="https://ui.toast.com/tui-editor"> toast ui

***

### コードレビュー後修正項目 (2025.01.10)

1. HTML5のインデントを改善しました。

    - 改善理由 : コードの可読性を向上させ、チームメンバーや他の開発者が混乱せず、よりスムーズに理解できるようにしました。

2. イメージフォルダのイメージファイルの拡張子を大文字から小文字に修正しました。

    - 修正理由 : 拡張子は全て小文字にする、コーディングコンベンションに従いました。
   
3. Controllerのパスをキャメルケースからスケバブケースに変更しました。

   - 修正理由 : URLはケバブケースにするウェブ開発コンベンションに従い、可読性を高めました。
    
***

### <a href="https://github.com/leewoosang-hub/LWS-portfolio">トップページに戻る</a>

<img src="https://capsule-render.vercel.app/api?type=waving&color=9172EC&height=200&section=footer&20render&fontSize=90" />
