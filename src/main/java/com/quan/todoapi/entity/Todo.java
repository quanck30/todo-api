package com.quan.todoapi.entity;

/*
      課題名：Todo
      作成日：2026/10/28
      作成者：ディンビンクアン
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;

/**
 * Todoリストの1件のタスクを表すクラス
 * Class biểu diễn một công việc trong Todo list
 * 現時点では普通のJavaクラス（POJO）、MySQL・JPA導入時に@Entityを付ける予定
 * Hiện tại chỉ là class Java thường (POJO). Khi dùng MySQL + JPA sẽ gắn thêm @Entity
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

    // 引数なしコンストラクタ：JacksonがJSONからオブジェクトを生成する際に使う（JPAでも必須）
    // Constructor không tham số: Jackson dùng khi tạo object từ JSON (JPA cũng bắt buộc cần)
    public Todo(){}

    // 全フィールドを指定するコンストラクタ：サンプルデータや新規作成時に使う
    // Constructor đầy đủ: dùng khi tạo dữ liệu mẫu và khi tạo mới
    @JsonCreator(mode = JsonCreator.Mode.DISABLED)
    public Todo(Long id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public boolean isCompleted() {
        return completed;
    }

    // setterはJacksonがJSONの値をセットする際に呼ばれる
    // Setter được Jackson gọi khi gán giá trị từ JSON
    public void setId(Long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
