-- 1) index info 30개 생성
INSERT INTO index_info (idx_csf, idx_nm, itms_cnt, bas_pntm, bas_idx,
                        source_type, favorite, enabled, created_at, updated_at)
SELECT 'KOSPI200_' || g                             AS idx_csf,
       '코스피 200 (' || g || ')'                      AS idx_nm,
       200 + g                                      AS itms_cnt,
       DATE '2020-01-01' + (g || ' days')::interval AS bas_pntm,
       1000 + g                                     AS bas_idx,
       'MANUAL',
       FALSE,
       TRUE,
       now(),
       now()
FROM generate_series(1, 30) g;

-- 2) syncJob 10,000개 생성

-- 1) 현재 존재하는 index_info Ids 가져오기
-- 1) index_info Ids 를 외래키로 가지는 sync_Job 생성
INSERT INTO sync_job (index_info_id,
                      job_type,
                      target_dt,
                      worker,
                      job_time,
                      is_completed,
                      created_at,
                      updated_at)
SELECT
    -- index_info_id : 랜덤으로 선택
    (SELECT id FROM index_info ORDER BY random() LIMIT 1)                             AS index_info_id,

    -- job_type : FULL, DELTA, MANUAL 중 랜덤
    (ARRAY ['INDEX_INFO', 'INDEX_DATA'])[floor(random() * 2 + 1)]                     AS job_type,

    -- target_dt : 최근 3년 동안 균일 분포
    (CURRENT_DATE - '3 years'::interval)
        + (floor(random() * (365 * 3)) || ' days')::interval                          AS target_dt,

    -- worker : worker_번호
    'worker_' || g                                                                    AS worker,

    -- job_time : 최근 3년 동안 균일 분포
    (now() - '3 years'::interval)
        + (random() * (EXTRACT(EPOCH FROM interval '3 years')) * interval '1 second') AS job_time,

    -- is_completed : true/false 랜덤
    (random() > 0.5)                                                                  AS is_completed,

    -- created_at : 최근 3년 동안 균일 분포
    (now() - '3 years'::interval)
        + (random() * (EXTRACT(EPOCH FROM interval '3 years')) * interval '1 second') AS created_at,

    -- updated_at : created_at 이후 ~ 현재 사이 랜덤
    (now() - '3 years'::interval)
        + (random() * (EXTRACT(EPOCH FROM interval '3 years')) * interval '1 second') AS updated_at

FROM generate_series(1, 10000) g;


truncate sync_job

