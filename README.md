<img src="https://capsule-render.vercel.app/api?type=waving&color=9172EC&height=200&section=header&text=COLLAVORE%[electric_approval]%&fontSize=40&animation=fadeIn&fontAlign=64&fontAlignY=36" />

<div>
  <h1>✉<i>&nbsp 電子決裁</i></h1>
</div>  

### 主要機能
  - <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/create_template.md">テンプレート生成</a>
    1. Naver smart editor 2.0 apiを利用して電子決裁のテンプレート生成
  - <a href="https://github.com/leewoosang-hub/CollaVore/blob/master/create_approval.md">電子決裁文書起案</a>
    1. 電子決裁のタイトルを決めます。
    2. 承認者情報を非同期処理で取得後決めます。
    3. 電子決裁のテンプレートを選択します。
    4. 上記の三つが決められたたら起案します。
  - <a href="https://github.com/leewoosang-hub/CollaVore/tree/master/EDSM.md">電子決裁文書決裁</a>
    1. プロシージャを利用して電子決裁に含まれる承認者の承認状態を変更します。
    2. プロシージャを利用して電子決裁状態を変更します。
    3. ログイン中のセッションの順番のみ承認できる「先決機能」。

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

### コードレビュー後修正項目 

  - **コードレビューを行う後、開発当時には気付かなかった問題点を改善しました。**

  1. HTML5のインデントを改善しました。
     
 ```
    $(document)
				.ready(
						function() {
							$(".approval-row")
									.on(
											"click",
											function(event) {
												if (!$(event.target).hasClass(
														"delete-button")) {
													const eaNo = event.currentTarget/*이벤트가 걸려 있는 타겟*/.dataset.eaNo;
													location.href = '/approvals/readApprInfo?eaNo='
															+ eaNo;
												}
											});
							$(".delete-button")
									.on(
											"click",
											function(event) {
												const eaNo = event.currentTarget.parentElement.parentElement.dataset.eaNo;
												Swal.fire({
										              title: "해당 전자결재를 삭제하시겠습니까?",
										              icon: "warning",
										              showCancelButton: true,
										              confirmButtonColor: "#3085d6",
										              cancelButtonColor: "#d33",
										              confirmButtonText: "예",
										              cancelButtonText:"아니요"
										            }).then((result) => {
										              if (result.isConfirmed) {
										                  // 아작스
															location.href = '/approvals/deleteAppr?eaNo='
																+ eaNo;
										                    
										              }
										            });
											});
						});      
```
    
  - 改善前

````
    $(document).ready(function () {
      $(".approval-row").on("click", function (event) {
        if (!$(event.target).hasClass("delete-button")) { // 対象にdelete-buttonクラスがない場合、こーどを実行
          const eaNo = event.currentTarget.dataset.eaNo;
          location.href = "/approvals/readApprInfo?eaNo=" + eaNo;
        }
      });
      $(".delete-button").on("click", function (event) {
        const eaNo =
          event.currentTarget.parentElement.parentElement.dataset.eaNo;
        Swal.fire({
          title: "削除しますか",
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: "#3085d6",
          cancelButtonColor: "#d33",
          confirmButtonText: "はい",
          cancelButtonText: "いいえ",
        }).then((result) => {
          if (result.isConfirmed) {
            location.href = "/approvals/deleteAppr?eaNo=" + eaNo;
          }
        });
      });
    });
````
    
- 改善後

  - 改善理由 : コードの可読性を向上させ、チームメンバーや他の開発者が混乱せず、よりスムーズに理解できるようにしました。

2. イメージフォルダのイメージファイルの拡張子を修正しました。
3. Controllerのパスをキャメルケースからスケバブケースに変更しました。

    
***

### <a href="https://github.com/leewoosang-hub/LWS-portfolio">トップページに戻る</a>

<img src="https://capsule-render.vercel.app/api?type=waving&color=9172EC&height=200&section=footer&20render&fontSize=90" />
