# todo-api

Todoリストアプリ（Kotlin製Androidアプリ）用のREST APIサーバーです。
Spring BootとMySQLで、TodoのCRUD（作成・取得・更新・削除）を提供します。

## 使用技術

- Java 21（Eclipse Temurin）
- Spring Boot 4.1（Spring Web MVC / Spring Data JPA）
- MySQL 8.4（Docker Composeで起動）
- Maven

## 起動方法（Windows）

### 前提

- JDK 21
- Docker Desktop

### 手順

```powershell
# 1. リポジトリを取得する
git clone https://github.com/<ユーザー名>/todo-api.git
cd todo-api

# 2. 環境変数ファイルを作成し、DB_ROOT_PASSWORD と DB_PASSWORD に任意のパスワードを設定する
Copy-Item .env.example .env
notepad .env

# 3. MySQLを起動する（ポート3307）
docker compose up -d

# 4. アプリを起動する（http://localhost:8080）
.\mvnw.cmd spring-boot:run
```

IntelliJ IDEAから `TodoApiApplication` を実行しても起動できます。

※ `.env` にはパスワードを記載するため、Gitにはコミットしません（`.gitignore` で除外済み）。

## API一覧

ベースURL：`http://localhost:8080`

| メソッド | パス | 説明 | 成功時 | 主なエラー |
|---|---|---|---|---|
| GET | `/api/todos` | Todo一覧を取得 | 200 OK | – |
| GET | `/api/todos/{id}` | Todoを1件取得 | 200 OK | 404（存在しないID）、400（IDが数値でない） |
| POST | `/api/todos` | Todoを新規作成 | 201 Created | 500（title未指定。400に修正予定） |
| PUT | `/api/todos/{id}` | Todoを更新 | 200 OK | 404（存在しないID）、400（IDが数値でない） |
| DELETE | `/api/todos/{id}` | Todoを削除 | 204 No Content | 404（存在しないID）、400（IDが数値でない） |

### リクエスト例

POST `/api/todos`

```json
{ "title": "牛乳を買う" }
```

レスポンス（201 Created）：`completed` は常に `false`、`id` は自動採番

```json
{ "completed": false, "id": 1, "title": "牛乳を買う" }
```

PUT `/api/todos/1`

```json
{ "title": "牛乳を買う", "completed": true }
```

レスポンス（200 OK）

```json
{ "completed": true, "id": 1, "title": "牛乳を買う" }
```

## 今後の予定

- DTOの導入
- 入力チェック（Bean Validation）：現在、`title` なしのPOSTは500エラーになるため、400を返すよう修正予定
- エラーレスポンスの統一（@RestControllerAdvice）
- Flywayによるマイグレーション管理
- Spring Security + JWTによる認証
- テストの追加、Dockerfileの作成、APIドキュメント（springdoc-openapi）