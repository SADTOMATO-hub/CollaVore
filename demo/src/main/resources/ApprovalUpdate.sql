CREATE OR REPLACE PROCEDURE "APPR_STATUS_UPDATE" (
    p_ear_no IN NUMBER,
    p_apprStatus IN VARCHAR2
) AS
    v_ea_no NUMBER; -- 決裁文書番号
    v_total_count NUMBER; -- 決裁者の総数
    v_approved_count NUMBER; -- 承認した決裁者の数
    v_rejected_count NUMBER; -- 否認した決裁者の数
BEGIN
    -- ear_no に該当する ea_no を取得
    SELECT ea_no 
    INTO v_ea_no
    FROM approvers
    WHERE ear_no = p_ear_no;

    -- 決裁者のステータスを更新
    UPDATE approvers
    SET status = p_apprStatus,
        proc_date = SYSDATE
    WHERE ear_no = p_ear_no;

    -- ea_no に関連する総決裁者数を取得
    SELECT COUNT(*) INTO v_total_count
    FROM approvers
    WHERE ea_no = v_ea_no;

    -- 承認状態 (b2) の決裁者数を取得
    SELECT COUNT(*) INTO v_approved_count
    FROM approvers
    WHERE ea_no = v_ea_no AND status = 'b2';

    -- 却下状態 (b3) の決裁者数を取得
    SELECT COUNT(*) INTO v_rejected_count
    FROM approvers
    WHERE ea_no = v_ea_no AND status = 'b3';

    -- 却下状態が1件でもある場合、即時否認処理
    IF v_rejected_count > 0 THEN
        UPDATE approvals
        SET status = 'a4',  -- 否認完了
            comp_date = SYSDATE
        WHERE ea_no = v_ea_no;

    -- すべての決裁者が承認した場合、承認完了処理
    ELSIF v_approved_count = v_total_count THEN
        UPDATE approvals
        SET status = 'a3',  -- 承認完了
            comp_date = SYSDATE
        WHERE ea_no = v_ea_no;

    -- その他の場合、進行中の状態を維持
    ELSE
        UPDATE approvals
        SET status = 'a2'  -- 進行中
        WHERE ea_no = v_ea_no;
    END IF;

    COMMIT;  -- トランザクション完了
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('該当する ear_no のデータが存在しません。');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('エラー発生: ' || SQLERRM);
        ROLLBACK;  -- エラー発生時にロールバック
END appr_status_update;
