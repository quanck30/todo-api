package com.quan.todoapi.controller;

/*
      課題名：TodoController
      作成日：2026/10/30
      作成者：ディンビンクアン
 */

import com.quan.todoapi.entity.Todo;
import com.quan.todoapi.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Todo APIのエンドポイントを提供するコントローラー（/api/todos）
 * Controller cung cấp các endpoint của Todo API (/api/todos)
 */
@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;


    // コンストラクタインジェクションでTodoServiceを受け取る
    // Nhận TodoService bằng constructor injection
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * Todo一覧を取得する（GET /api/todos）
     * Lấy danh sách Todo (GET /api/todos)
     */
    @GetMapping
    public List<Todo> getAll(){
        return todoService.findAll();
    }

    /**
     * IDでTodoを1件取得する（GET /api/todos/{id}）
     * Lấy một Todo theo id (GET /api/todos/{id})
     *
     * 見つかれば200 OK、該当IDのTodoが存在しなければ404 Not Found（ボディなし）を返す
     * Tìm thấy thì trả 200 OK, không có Todo với id đó thì trả 404 Not Found (không có body)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id){
        Optional<Todo> found = todoService.findById(id);
        if(found.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(found.get());
    }
    /**
     * Todoを新規作成する（POST /api/todos）
     * Tạo mới Todo (POST /api/todos)
     *
     * リクエストボディのJSONをJacksonがTodoに変換する
     * 作成成功時は常に201 Createdなので、@ResponseStatusで固定する
     * Jackson chuyển JSON trong request body thành Todo.
     * Tạo thành công thì luôn là 201 Created, nên cố định bằng @ResponseStatus
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo create(@RequestBody Todo request){
        return todoService.create(request);
    }

    /**
     * Todoを更新する（PUT /api/todos/{id}）
     * Cập nhật Todo (PUT /api/todos/{id})
     *
     * PUTは全体の置き換えなので、titleとcompletedを両方送ること
     * IDはURLの値を使い、ボディ内のidは信用しない
     * 更新成功で200 OK（更新後のTodo）、該当IDのTodoが存在しなければ404 Not Found
     * PUT là thay thế toàn bộ, nên phải gửi cả title và completed.
     * Id lấy từ URL, không tin id trong body.
     * Sửa thành công trả 200 OK (kèm Todo sau khi sửa), không có Todo với id đó thì trả 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody Todo request){
        Optional<Todo> updated = todoService.update(id, request);
        if(updated.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated.get());
    }

    /**
     * Todoを削除する（DELETE /api/todos/{id}）
     * Xóa Todo (DELETE /api/todos/{id})
     *
     * 削除成功で204 No Content（ボディなし）、該当IDのTodoが存在しなければ404 Not Found
     * Xóa thành công trả 204 No Content (không có body), không có Todo với id đó thì trả 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if(todoService.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
