-- \c postgres;


-- DROP DATABASE IF EXISTS findex;
--
-- -- 유저 생성
-- CREATE
--     USER findex_user WITH PASSWORD 'findex1234';
--
-- -- DB 생성
-- CREATE
--     DATABASE findex
--     WITH
--     OWNER = findex_user
--     ENCODING = 'UTF8';

-- 권한 부여 (DB 소유자 지정)
ALTER
    DATABASE findex OWNER TO findex_user;

-- 필요한 권한 추가 (테이블 생성, 쓰기/읽기)
GRANT ALL PRIVILEGES ON DATABASE
    findex TO findex_user;

DROP TABLE IF EXISTS sync_job CASCADE;
DROP TABLE IF EXISTS index_data CASCADE;
DROP TABLE IF EXISTS index_info CASCADE;

CREATE TABLE index_info
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    idx_csf     VARCHAR(300),
    idx_nm      VARCHAR(200),
    itms_cnt    INT,
    bas_pntm    DATE,
    bas_idx     NUMERIC,
    source_type VARCHAR(20),
    favorite    BOOLEAN,
    enabled     BOOLEAN,
    created_at  TIMESTAMPTZ,
    updated_at  TIMESTAMPTZ
);

CREATE TABLE index_data
(
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    index_info_id     BIGINT NOT NULL,
    bas_dt            DATE,
    source_type       VARCHAR(20),
    mkp               NUMERIC,
    clpr              NUMERIC,
    hipr              NUMERIC,
    lopr              NUMERIC,
    vs                NUMERIC,
    flt_rt            NUMERIC,
    trqu              BIGINT,
    tr_prc            BIGINT,
    lstg_mrkt_tot_amt BIGINT,
    created_at        TIMESTAMPTZ,
    updated_at        TIMESTAMPTZ,
    CONSTRAINT fk_index_info_to_index_data FOREIGN KEY (index_info_id)
        REFERENCES index_info (id)
        ON DELETE CASCADE
);

CREATE TABLE sync_job
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    index_info_id BIGINT      NOT NULL,
    job_type      VARCHAR(20),
    target_dt     DATE,
    worker        VARCHAR(30) NOT NULL,
    job_time      TIMESTAMPTZ,
    is_completed  BOOLEAN,
    created_at    TIMESTAMPTZ,
    updated_at    TIMESTAMPTZ,
    CONSTRAINT fk_index_info_to_sync_job FOREIGN KEY (index_info_id)
        REFERENCES index_info (id)
        ON DELETE CASCADE
);

-- 1) 부모 지수 1건 생성 (id는 IDENTITY 이므로 지정하지 않음)
-- WITH idx AS (
--     INSERT INTO public.index_info (
--                                    idx_csf, idx_nm, itms_cnt, bas_pntm, bas_idx,
--                                    source_type, favorite, enabled, created_at, updated_at
--         ) VALUES (
--                      'KOSPI200', '코스피 200', 200, DATE '2020-01-02', 1000,
--                      'MANUAL', FALSE, TRUE, now(), now()
--                  )
--         RETURNING id
-- )
--
-- -- 2) 자식 지수데이터 3건 생성 (위에서 생성된 id를 참조)
--    , rows AS (
--     SELECT (SELECT id FROM idx) AS idx_id, DATE '2025-09-08' AS bas_dt, 'MANUAL'::varchar AS source_type,
--            2560.12::numeric AS mkp, 2575.30::numeric AS clpr, 2587.00::numeric AS hipr, 2550.00::numeric AS lopr,
--            15.18::numeric AS vs, 0.59::numeric AS flt_rt,
--            1500000::bigint AS trqu, 3300000000::bigint AS tr_prc, 1200000000000::bigint AS lstg_mrkt_tot_amt
--     UNION ALL
--     SELECT (SELECT id FROM idx), DATE '2025-09-09', 'MANUAL',
--            2576.00, 2580.55, 2590.00, 2550.15,
--            4.55, 0.18,
--            1520000, 3400000000, 1205000000000
--     UNION ALL
--     SELECT (SELECT id FROM idx), DATE '2025-09-10', 'MANUAL',
--            2581.00, 2570.10, 2595.20, 2560.10,
--            -10.45, -0.40,
--            1470000, 3200000000, 1210000000000
--     UNION ALL
--     SELECT (SELECT id FROM idx), DATE '2025-09-09', 'OPEN_API',
--            2581.00, 2570.10, 2595.20, 2560.10,
--            -10.45, -0.40,
--            1470000, 3200000000, 1210000000000
-- )
--
-- INSERT INTO public.index_data (
--     index_info_id, bas_dt, source_type,
--     mkp, clpr, hipr, lopr, vs, flt_rt,
--     trqu, tr_prc, lstg_mrkt_tot_amt,
--     created_at, updated_at
-- )
-- SELECT idx_id, bas_dt, source_type,
--        mkp, clpr, hipr, lopr, vs, flt_rt,
--        trqu, tr_prc, lstg_mrkt_tot_amt,
--        now(), now()
-- FROM rows;