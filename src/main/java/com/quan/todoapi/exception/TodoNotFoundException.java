package com.quan.todoapi.exception;

/*
      課題名：TodoNotFoundException
      作成日：2026/11/13
      作成者：ディンビンクアン
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 指定IDのTodoが存在しない場合に送出する例外
 * Exception được ném ra khi không tồn tại Todo có id chỉ định
 *
 * {@code @ResponseStatus}を付けているため、Controllerの外まで投げられるとSpringが404 Not Foundを返す
 * Vì có gắn {@code @ResponseStatus}, khi exception bị ném ra khỏi Controller, Spring sẽ trả về 404 Not Found
 *
 * RuntimeException（非検査例外）を継承するので、メソッドにthrows宣言を書く必要はない
 * Kế thừa RuntimeException (unchecked exception) nên không cần khai báo throws ở method
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TodoNotFoundException extends RuntimeException {

    /**
     * 見つからなかったTodoのIDからメッセージを組み立てる
     * Tạo message từ id của Todo không tìm thấy
     *
     * メッセージはデバッグ用。本番の既定設定ではレスポンスJSONに含まれない（DevTools有効時は含まれる）
     * Message dùng để debug. Với thiết lập mặc định khi chạy production thì không có trong JSON response (khi DevTools bật thì có)
     *
     * @param id 見つからなかったTodoのID / id của Todo không tìm thấy
     */
    public TodoNotFoundException(Long id) {
        super("Todo not found: id=" + id);
    }
}
