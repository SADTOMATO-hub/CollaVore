<img src="https://capsule-render.vercel.app/api?type=waving&color=9172EC&height=200&section=header&text=COLLAVORE%&fontSize=40&animation=fadeIn&fontAlign=80&fontAlignY=36" />

<h1><i>&nbsp プロジェクト詳細</i></h1>
<strong>&nbsp&nbsp開発動機</strong>　：ピザのショッピングサイトを開発する中で、 <br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                                        チームメンバーとリソースや進捗状況を共有するためのプラットフォームがなく、<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                                        効率的な協力が困難していました。以降も同様の問題が発生しないよう、 <br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                                        チーム内で円滑に作業を進められるコラボレーションツールを開発いたしました。<br>
<strong>&nbsp&nbsp概　　要</strong>　：  このプラットフォームは人事管理、スケジュール管理、電子決裁管理、プロジェクト管理、 <br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                                         企画管理などを提供します。<br>
<strong>&nbsp&nbspS&nbspK&nbspI&nbspL&nbspL&nbspS</strong>  &nbsp ： Java, Java Spring boot, Oracle, <br>
<strong>&nbsp&nbsp開発期間</strong>　： 2024.09.30 - 2024.11.13<br>
<strong>&nbsp&nbsp開発人数</strong>　： 5名<br>
<strong>&nbsp&nbsp担　　当</strong>　： 電子決裁管理</strong><br>
<p></p>

## ✅ 注目ポイント

1. spring MVC 패턴과 3계층 아키택처를 활용
   - 사용목적 : spring MVC 패턴을 활용하여 객체 간 역할을 분리, 계층 아키택처 기법을 통해 Model객체를 각 역할에 맞게 세분화
2. API 개발
<div>
  <h1>✉<i>&nbsp 電子決裁</i></h1>
</div>  

### 主要機能
  - <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/create_template.md">テンプレート生成</a>
    1. Naver smart editor 2.0 apiを利用して電子決裁のテンプレート生成
    2. Thymeleaf를 통해 HTML5 코드 랜더링
  - <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/create_approval.md">電子決裁文書起案</a>
    1. 비동기 처리로 결재자 데이터 취득
    2. data-set을 이용하여 이벤트핸들러 공유 
    3. 배열 타입을 이용하여 다수의 데이터 처리
  - <a href="https://github.com/leewoosang-hub/CollaVore/tree/master/EDSM.md">電子決裁文書決裁</a>
    1. 비동기 처리를 통해 문서 결재 진행
    2. 프로시저를 이용하여 문서 및 승인자 상태 변경

### 他の機能
  - テンプレート修正
  - テンプレート削除
  - テンプレート項目整列
  - 電子決裁文書修正
  - 電子決裁文書削除
    <br>프로시저를 활용한 전자결재 문서 및 결재자 데이터 동시 삭제
    - 사용목적
      1. 전자결재 테이블(PK)이 결재자 테이블에서 외래키(FK)로 참조되고 있는 구조에서, 데이터 무결성을 유지하면서 삭제 처리
      2. 수작업 없이 일괄 삭제 가능하도록 자동화
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
