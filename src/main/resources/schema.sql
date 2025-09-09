\c
postgres;


DROP
DATABASE IF EXISTS findex;

-- 유저 생성
CREATE
USER findex_user WITH PASSWORD 'findex1234';

-- DB 생성
CREATE
DATABASE findex
    WITH
    OWNER = findex_user
    ENCODING = 'UTF8';

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
    enabled    BOOLEAN,
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
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    index_info_id BIGINT      NOT NULL,
    job_type       VARCHAR(20),
    target_dt      DATE,
    worker         VARCHAR(30) NOT NULL,
    job_time       TIMESTAMPTZ,
    is_completed   BOOLEAN,
    created_at     TIMESTAMPTZ,
    updated_at     TIMESTAMPTZ,
    CONSTRAINT fk_index_info_to_sync_job FOREIGN KEY (index_info_id)
        REFERENCES index_info (id)
        ON DELETE CASCADE
);