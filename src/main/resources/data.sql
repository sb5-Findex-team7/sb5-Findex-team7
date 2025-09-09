-- index_info 더미 데이터 10개 삽입
INSERT INTO index_info (idx_csf, idx_nm, itms_cnt, bas_pntm, bas_idx, source_type, favorite,
                        enabled, created_at, updated_at)
SELECT 'IDX' || LPAD(g::text, 3, '0'),                   -- IDX001, IDX002 ...
       'Sample Index ' || g,                             -- Sample Index 1 ...
       (g * 10) % 200 + 1,                               -- 아이템 수 (1~200 사이)
       current_date - (g || ' days')::interval,          -- 기준시점 = 오늘 기준 g일 전
       1000 + g * 5,                                     -- bas_idx (1000, 1005, 1010 ...)
       CASE WHEN g % 2 = 0 THEN 'STOCK' ELSE 'BOND' END, -- STOCK, BOND 번갈아
       (g % 2 = 0),                                      -- favorite true/false 번갈아
       true,                                             -- enabled는 전부 true
       now() - (g || ' hours')::interval,
       now()
FROM generate_series(1, 10) g;

-- sync_job 30개 삽입
INSERT INTO sync_job (index_info_id, job_type, target_dt, worker, job_time, is_completed,
                      created_at, updated_at)
SELECT g % 10 + 1,                        -- index_info_id (기존 index_info의 PK)
       job_type,                          -- INDEX_INFO, INDEX_DATA
       current_date - (g % 10),           -- 최근 10일 랜덤 날짜
       CASE
           WHEN g % 3 = 0
               THEN 'system' -- 배치에 의해 연동된 경우
           ELSE '192.168.0.' || (g % 255) -- 사용자가 직접 연동한 경우 요청 IP
           END,
       now() - (g || ' hours')::interval, -- g 시간 전
       (g % 2 = 0),                       -- 짝수면 true, 홀수면 false
       now() - (g || ' minutes')::interval,
       now()
FROM generate_series(1, 30) g
         CROSS JOIN (VALUES ('INDEX_INFO'), ('INDEX_DATA')) AS jt(job_type)
LIMIT 30;