# SET LIBRARY

ブレイキンのバトルで使うSETやムーブを登録・整理し、動画と一緒に振り返るためのWebアプリケーションです。

## 主な機能

- SETの一覧表示
- SETの新規登録・編集・削除
- SET名によるキーワード検索
- カテゴリー（`BASIC SET` / `UNIQUE SET`）による絞り込み
- SETの説明文の登録
- 動画ファイルのアップロードとブラウザ上での再生
- 登録日時の新しい順で一覧表示
- 動画ファイル名にUUIDを使用して保存

## 使用技術

- Java 21
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Data JPA / Hibernate
- Oracle Database（Oracle JDBC Driver）
- Maven
- HTML / CSS

## 必要な環境

- JDK 21
- Dockerで起動したOracle Database XEなど、接続可能なOracle Database
- Maven Wrapper（リポジトリに同梱）

## 起動方法

### 1. Oracle Databaseを準備する

Oracle Databaseを起動し、アプリケーション用のユーザーとデータベース接続先を準備してください。
このプロジェクトは、データベースのサービス名として `XEPDB1` を使用する環境を想定しています。実際の接続先は使用環境に合わせてください。

### 2. DB接続設定を作成する

`src/main/resources/application.properties` を作成し、環境に合わせて接続情報を設定してください。

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XEPDB1
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

`YOUR_DB_USERNAME` と `YOUR_DB_PASSWORD` は実際の値に置き換えてください。パスワードをGitHubなどの公開リポジトリへコミットしないでください。

### 3. アプリケーションを起動する

プロジェクトのルートディレクトリで実行します。

```bash
./mvnw spring-boot:run
```

### 4. ブラウザで開く

```text
http://localhost:8080/sets
```

## 動画ファイルについて

アップロードした動画は、プロジェクトルートの `uploads/videos/` に保存されます。動画ファイル自体はGit管理に含めず、必要に応じて各自で登録してください。

現在のアップロード処理では、拡張子 `.mp4`、`.mov`、`.m4v`、`.webm` を受け付けます。実際に再生できる形式はブラウザの対応状況にも依存します。

## プロジェクト構成

```text
src/
├── main/
│   ├── java/com/example/setlibrary/
│   │   ├── config/       # 静的リソース配信設定
│   │   ├── controller/   # 画面遷移・リクエスト処理
│   │   ├── entity/       # JPAエンティティ
│   │   ├── repository/   # データアクセス
│   │   ├── service/      # サービス層
│   │   └── SetLibraryApplication.java
│   └── resources/
│       ├── static/css/   # スタイルシート
│       └── templates/    # Thymeleafテンプレート
└── test/
```

## 今後の改善候補

- 入力値のサーバー側バリデーションの強化
- アップロード可能なファイルサイズ・MIMEタイプの検証
- サービス層への処理集約とログ出力の整理
- テストケースの追加
- 動画形式に応じた再生互換性の改善
