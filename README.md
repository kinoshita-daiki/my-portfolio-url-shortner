![example workflow](https://github.com/kinoshita-daiki/my-portfolio-url-shortner/actions/workflows/deploy.yml/badge.svg)

ポートフォリオとして作成した、URL短縮サイトのバックエンドサーバ側のリポジトリです。

## URL短縮サイト
下記よりWebアプリをご利用ください。<br>
https://kinoshitadaiki.work/us/urlShortner

[!NOTE]
このWebアプリはポートフォリオとして作成されています。機能は簡素であることにご留意ください。

## URL短縮の仕方
1. 入力欄に短縮したいURLを入力します。
2. 変換ボタンを押下します。
3. 画面に短縮されたURLが表示されます。

## 特徴
WebAPIを作成し、他サーバからの呼び出しを想定しています。<br>
プッシュされた場合、自動でコンパイル、テスト、テストレポートを出力するパイプラインを構築しています。<br>
また手動トリガーでパッケージの作成(RunnableJar)、サーバへの自動デプロイが可能です。

## 使用技術
- Java(17)
- Quarkus
	- JSON-P/JSON-B
	- JAX-RS
- Amazon DynamoDB
- Docker
- Maven
- lombok