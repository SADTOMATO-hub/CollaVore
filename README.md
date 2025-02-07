<img src="https://capsule-render.vercel.app/api?type=waving&color=9172EC&height=200&section=header&text=COLLAVORE%&fontSize=40&animation=fadeIn&fontAlign=80&fontAlignY=36" />

## ✅ 注目ポイント

1. Spring MVCパターンと3層アーキテクチャを活用
   - 使用目的 : オブジェクトの役割をより明確に区別するために使用

```
📁 app
src/main/java/com/collavore.app/approvasls
├── mapper
│   ├── ApprovalsMapper.java (DAO)
├── Web  (Controller - spring MVC2のC)
│   ├── ApprovalsController.java
├── service  (Service - spring MVC2のS)
│   │   ├──impl
│   │   │    ├──approvalsServiceImpl.java(3層のビジネス層)
│   ├── approvalsService.java
│   ├── ApprovalsVO.java (3層のデータ層)
src/main/java/resources/mapper
│   ├── ApprovalsMapper.xml (MyBatis/3層のデータ接近層)
src/main/java/resources/templates
│   ├── Approvals.html (View/thymeleaf - spring MVC2のV)
```


<div>
  <h1>✉<i>&nbsp 電子決裁</i></h1>
</div>  

### 主要機能
  - <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/create_template.md">テンプレート生成</a>
    1. Naver smart editor 2.0 apiを利用して電子決裁のテンプレート生成
  - <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/create_approval.md">電子決裁文書起案</a>
    1. 最大4名までの承認者を選択
    2. Naver Smart Editor 2.0 APIを呼び出して、文書用のテンプレートを適用
  - <a href="https://github.com/leewoosang-hub/CollaVore/tree/master/EDSM.md">電子決裁文書決裁</a>
    1. ボタンを使用して承認または却下を決定
    2. 承認者のステータスに応じて文書のステータスを変更

### 他の機能
  - テンプレート修正
  - テンプレート削除
  - テンプレート項目整列
  - 電子決裁文書修正
  - 電子決裁文書削除
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
