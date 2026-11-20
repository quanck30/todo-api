package com.quan.todoapi.entity;

/*
      課題名：Todo
      作成日：2026/10/28
      作成者：ディンビンクアン
 */
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Todoリストの1件のタスクを表すエンティティ
 * Entity biểu diễn một công việc trong Todo list
 *
 * JPAによりMySQLのtodosテーブルと対応付けられる（1オブジェクト＝1行）
 * Được JPA ánh xạ với bảng todos trong MySQL (1 object = 1 dòng)
 *
 * DTO導入（第11回）までは、APIでのJSONの送受信にもこのクラスを直接使う
 * Cho đến khi dùng DTO (Bài 11), class này cũng được dùng trực tiếp để nhận/trả JSON của API
 */
@Entity
@Table(name = "todos")
public class Todo {

    // 主キー。値はMySQLのAUTO_INCREMENTで自動採番される
    // Khóa chính. Giá trị do AUTO_INCREMENT của MySQL tự cấp
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // タイトルは必須項目（NOT NULL制約を付ける）
    // Tiêu đề là bắt buộc (gắn ràng buộc NOT NULL)
    @Column(nullable = false)
    private String title;

    // プリミティブ型なので、カラムは自動的にNOT NULLになる
    // Vì là kiểu primitive nên cột tự động là NOT NULL
    private boolean completed;

    // 引数なしコンストラクタ：JPAがDBの値からエンティティを生成する際に必須（Jacksonも使用する）
    // Constructor không tham số: JPA bắt buộc cần khi tạo entity từ giá trị trong DB (Jackson cũng dùng)
    public Todo() {}

    // 全フィールドを指定するコンストラクタ：TodoService.create()で新規登録用のエンティティを作る際に使う
    // Constructor đầy đủ: dùng trong TodoService.create() để tạo entity cần lưu mới
    // JSONからオブジェクトを生成する際（デシリアライズ時）、Jacksonがこのコンストラクタを使わないよう無効化する（引数なしコンストラクタとsetterを使わせるため）
    // Khi tạo object từ JSON (lúc deserialize), chặn Jackson dùng constructor này (để Jackson dùng constructor không tham số và setter)
    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    public Todo(Long id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public boolean isCompleted() { return completed; }

    // setterはJacksonがJSONの値をセットする際に呼ばれる
    // Setter được Jackson gọi khi gán giá trị từ JSON
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}