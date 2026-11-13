
/*
      課題名：TodoService
      作成日：2026/10/28
      作成者：ディンビンクアン
 */
package com.quan.todoapi.service;

import com.quan.todoapi.entity.Todo;
import com.quan.todoapi.exception.TodoNotFoundException;
import com.quan.todoapi.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Todoのビジネスロジックを担当するサービス
 * Service phụ trách business logic của Todo
 *
 * データはTodoRepository経由でMySQLのtodosテーブルに保存する（アプリを再起動しても消えない）
 * Dữ liệu được lưu vào bảng todos của MySQL thông qua TodoRepository (khởi động lại app cũng không mất)
 *
 * 対象のTodoが存在しない場合はTodoNotFoundExceptionを投げる（Spring MVCが404に変換する）
 * Khi không tồn tại Todo cần xử lý thì ném TodoNotFoundException (Spring MVC sẽ chuyển thành 404)
 */
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    // コンストラクタインジェクションでTodoRepositoryを受け取る
    // Nhận TodoRepository bằng constructor injection
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * 全てのTodoを取得する
     * Lấy toàn bộ Todo
     *
     * @return DBに保存されている全Todo（0件の場合は空のリスト） / Toàn bộ Todo trong DB (không có thì là list rỗng)
     */
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    /**
     * IDでTodoを1件取得する（LaravelのfindOrFail()に相当）
     * Lấy một Todo theo id (tương đương findOrFail() của Laravel)
     *
     * Optionalが空の場合、orElseThrow()に渡したラムダで例外を生成して投げる
     * Nếu Optional rỗng thì orElseThrow() gọi lambda được truyền vào để tạo exception rồi ném ra
     *
     * @param id 取得するTodoのID / id của Todo cần lấy
     * @return 見つかったTodo（nullは返さない） / Todo tìm được (không bao giờ trả về null)
     * @throws TodoNotFoundException IDが存在しない場合 / Khi id không tồn tại
     */
    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    /**
     * Todoを新規作成する
     * Tạo mới một Todo
     *
     * クライアントから送られたidとcompletedは無視し、titleのみ使用する
     * idをnullにしておくことで、save()がINSERTを実行し、MySQLのAUTO_INCREMENTで採番される
     * Bỏ qua id và completed do client gửi, chỉ dùng title.
     * Để id là null thì save() sẽ thực hiện INSERT, và id được AUTO_INCREMENT của MySQL cấp
     *
     * @param request クライアントから受け取ったTodo / Todo nhận từ client
     * @return 保存されたTodo（採番済みのid付き） / Todo đã lưu (đã có id)
     */
    public Todo create(Todo request) {
        Todo todo = new Todo(null, request.getTitle(), false);
        return todoRepository.save(todo);
    }

    /**
     * 指定IDのTodoのtitleとcompletedを更新する（PUT：全体を置き換える）
     * Cập nhật title và completed của Todo có id chỉ định (PUT: thay thế toàn bộ)
     *
     * IDは引数idを使い（ControllerがURLから取得）、リクエストボディのidは無視する
     * JSONにcompletedが無い場合、requestのcompletedは初期値falseのままなので、falseで更新される
     * 変更内容はsave()を明示的に呼んでDBに反映する
     * Dùng id ở tham số (Controller lấy từ URL), bỏ qua id trong request body.
     * Nếu JSON không có completed thì completed của request giữ mặc định false, nên sẽ bị cập nhật thành false.
     * Gọi save() một cách tường minh để ghi thay đổi xuống DB
     *
     * @param id 更新するTodoのID / id của Todo cần sửa
     * @param request クライアントから受け取った新しい内容 / Nội dung mới nhận từ client
     * @return 更新後のTodo / Todo sau khi sửa
     * @throws TodoNotFoundException IDが存在しない場合 / Khi id không tồn tại
     */
    public Todo update(Long id, Todo request) {
        // 存在しなければここで例外が投げられ、以降の処理は実行されない
        // Nếu không tồn tại thì exception bị ném ngay tại đây, các dòng phía sau không chạy
        Todo todo = findById(id);
        todo.setTitle(request.getTitle());
        todo.setCompleted(request.isCompleted());
        return todoRepository.save(todo);
    }

    /**
     * 指定IDのTodoを削除する
     * Xóa Todo có id chỉ định
     *
     * deleteById()は対象が無くても例外を出さないため、先にexistsById()で存在を確認する
     * deleteById() không báo lỗi khi id không tồn tại, nên kiểm tra tồn tại bằng existsById() trước
     *
     * @param id 削除するTodoのID / id của Todo cần xóa
     * @throws TodoNotFoundException IDが存在しない場合 / Khi id không tồn tại
     */
    public void delete(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoundException(id);
        }
        todoRepository.deleteById(id);
    }
}