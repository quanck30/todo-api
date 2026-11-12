package com.quan.todoapi.service;

import com.quan.todoapi.entity.Todo;
import com.quan.todoapi.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
//    private final List<Todo> todos = new CopyOnWriteArrayList<>();
    private final TodoRepository todoRepository;

    // 次に採番するID（スレッドセーフに1ずつ増やす）
    // Id sẽ cấp tiếp theo (tăng dần từng 1, an toàn với nhiều thread)
//    private final AtomicLong nextId = new AtomicLong(1);

//    public TodoService(){
//        // サンプルデータを3件登録する
//        // Đăng ký sẵn 3 dữ liệu mẫu
//        todos.add(new Todo(nextId.getAndIncrement(), "Spring 3週目", false));
//        todos.add(new Todo(nextId.getAndIncrement(), "応用情報学習", false));
//        todos.add(new Todo(nextId.getAndIncrement(), "Postman 設定", true));
//
//        System.out.println(">>> TodoService created, Todo count: " + todos.size());
//    }
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    /**
     * 全てのTodoを取得する
     * Lấy toàn bộ Todo
     *
     *  DBに保存されている全Todo（0件の場合は空のリスト） /
     *  Toàn bộ Todo trong DB (không có thì là list rỗng)
     */
    public List<Todo> findAll(){
//        戻り値はコピー版のみ、外から直接直すことできないため
//        return List.copyOf(todos);
        return todoRepository.findAll();
    }

    /**
     * IDでTodoを1件検索する
     * Tìm một Todo theo id
     *
     * 見つからない場合は空のOptionalを返す（nullは返さない）
     * Không tìm thấy thì trả về Optional rỗng (không trả về null)
     *
     * @param id 検索するTodoのID / id của Todo cần tìm
     * @return 見つかったTodo、または空のOptional / Todo tìm được, hoặc Optional rỗng
     */
    public Optional<Todo> findById(Long id){

//        return todos.stream().filter(todo->todo.getId().equals(id)).findFirst();
        return todoRepository.findById(id);
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
     * @return 更新後のTodo、IDが存在しない場合は空のOptional / Todo sau khi sửa, hoặc Optional rỗng nếu id không tồn tại
     */
    public Optional<Todo> update(Long id, Todo request){
        Optional<Todo> found = todoRepository.findById(id);
        if(found.isEmpty()) return Optional.empty();

        Todo todo = found.get();
        todo.setTitle(request.getTitle());
        todo.setCompleted(request.isCompleted());
        return Optional.of(todoRepository.save(todo));
    }

    /**
     * 指定IDのTodoを削除する
     * Xóa Todo có id chỉ định
     *
     * deleteById()は対象が無くても例外を出さないため、先にexistsById()で存在を確認する
     * deleteById() không báo lỗi khi không có dữ liệu cần xóa (id không tồn tại), nên kiểm tra tồn tại bằng existsById() trước
     *
     * @param id 削除するTodoのID / id của Todo cần xóa
     * @return 削除できた場合true、IDが存在しない場合false / true nếu xóa được, false nếu id không tồn tại
     */
    public boolean delete(Long id){
//        return todos.removeIf(todo->todo.getId().equals(id));
        if(!todoRepository.existsById(id)) return false;
        todoRepository.deleteById(id);
        return true;
    }

}
