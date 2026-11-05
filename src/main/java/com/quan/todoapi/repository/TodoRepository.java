package com.quan.todoapi.repository;

/*
      課題名：TodoRepository
      作成日：2026/11/05
      作成者：ディンビンクアン
 */

import com.quan.todoapi.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Todoエンティティのデータアクセスを担当するリポジトリ
 * Repository phụ trách truy cập dữ liệu của entity Todo
 *
 * インターフェースを宣言するだけで、実装クラスは起動時にSpring Data JPAが自動生成する
 * Chỉ cần khai báo interface, Spring Data JPA sẽ tự sinh class implementation khi khởi động
 *
 * 型パラメータ：<エンティティの型, 主キー（id）の型>
 * Tham số kiểu: <kiểu entity, kiểu khóa chính (id)>
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {

}
