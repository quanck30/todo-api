package com.quan.todoapi.service;

import com.quan.todoapi.entity.Todo;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/*
      課題名：TodoService
      作成日：2026/10/28
      作成者：ディンビンクアン
 */

/**
 * Todoのビジネスロジックを担当するサービス
 * Service phụ trách business logic của Todo
 *
 * 現時点ではデータをメモリ上のリストで保持する（アプリ再起動で初期状態に戻る）
 * Hiện tại dữ liệu được giữ trong list trên bộ nhớ (khởi động lại app thì quay về trạng thái ban đầu)
 */
@Service
public class TodoService {

    // 複数リクエストから同時にアクセスされても安全なリスト
    // List an toàn khi nhiều request truy cập cùng lúc
    private final List<Todo> todos = new CopyOnWriteArrayList<>();

    // 次に採番するID（スレッドセーフに1ずつ増やす）
    // Id sẽ cấp tiếp theo (tăng dần từng 1, an toàn với nhiều thread)
    private final AtomicLong nextId = new AtomicLong(1);

    public TodoService(){
        // サンプルデータを3件登録する
        // Đăng ký sẵn 3 dữ liệu mẫu
        todos.add(new Todo(nextId.getAndIncrement(), "Spring 3週目", false));
        todos.add(new Todo(nextId.getAndIncrement(), "応用情報学習", false));
        todos.add(new Todo(nextId.getAndIncrement(), "Postman 設定", true));

        System.out.println(">>> TodoService created, Todo count: " + todos.size());
    }

    /**
     * 全てのTodoを取得する
     * Lấy toàn bộ Todo
     *
     * 外部から内部リストを変更されないよう、読み取り専用のコピーを返す
     * Trả về bản sao chỉ-đọc để bên ngoài không sửa được list bên trong
     */
    public List<Todo> findAll(){
//        戻り値はコピー版のみ、外から直接直すことできないため
        return List.copyOf(todos);
    }

    /**
     * IDでTodoを1件検索する
     * Tìm một Todo theo id
     *
     * 見つからない場合は空のOptionalを返す（nullは返さない）
     * Không tìm thấy thì trả về Optional rỗng (không trả về null)
     *
     * @param id 検索するTodoのID / id của Todo cần tìm
     * @return  見つかったTodo、または空のOptional / Todo tìm được, hoặc Optional rỗng
     */
    public Optional<Todo> findById(Long id){
        return todos.stream().filter(todo->todo.getId().equals(id)).findFirst();
    }
    /**
     * Todoを新規作成する
     * Tạo mới một Todo
     *
     * クライアントから送られたidとcompletedは無視し、titleのみ使用する
     * idはサーバー側で採番し、completedは常にfalseで作成する
     * Bỏ qua id và completed do client gửi, chỉ dùng title.
     * Id do server tự cấp, completed luôn là false khi tạo
     *
     * @param request クライアントから受け取ったTodo / Todo nhận từ client
     * @return 作成されたTodo（採番済みのid付き） / Todo vừa tạo (đã có id)
     */
    public Todo create(Todo request) {
        Todo todo = new Todo(nextId.getAndIncrement(), request.getTitle(), false);
        todos.add(todo);
        return todo;
    }

    /**
     * 指定IDのTodoのtitleとcompletedを更新する（PUT：全体を置き換える）
     * Cập nhật title và completed của Todo có id chỉ định (PUT: thay thế toàn bộ)
     *
     * IDは引数idを使い（ControllerがURLから取得）、リクエストボディのidは無視する
     * JSONにcompletedが無い場合、requestのcompletedは初期値falseのままなので、falseで更新される
     * Dùng id ở tham số (Controller lấy từ URL), bỏ qua id trong request body.
     * Nếu JSON không có completed thì completed của request giữ mặc định false, nên sẽ bị cập nhật thành false
     *
     * @param id 更新するTodoのID / id của Todo cần sửa
     * @param request クライアントから受け取った新しい内容 / Nội dung mới nhận từ client
     * @return 更新後のTodo、IDが存在しない場合は空のOptional / Todo sau khi sửa, hoặc Optional rỗng nếu id không tồn tại
     */
    public Optional<Todo> update(Long id, Todo request){
        Optional<Todo> found = findById(id);
        if(found.isEmpty()) return Optional.empty();

        Todo todo = found.get();
        todo.setTitle(request.getTitle());
        todo.setCompleted(request.isCompleted());
        return Optional.of(todo);
    }

    /**
     * 指定IDのTodoを削除する
     * Xóa Todo có id chỉ định
     *
     * @param id 削除するTodoのID / id của Todo cần xóa
     * @return 削除できた場合true、IDが存在しない場合false / true nếu xóa được, false nếu id không tồn tại
     */
    public boolean delete(Long id){
        return todos.removeIf(todo->todo.getId().equals(id));
    }

}
