\c postgres;


DROP DATABASE IF EXISTS findex;

-- 유저 생성
CREATE USER findex_user WITH PASSWORD 'findex1234';

-- DB 생성
CREATE DATABASE findex
    WITH
    OWNER = findex_user
    ENCODING = 'UTF8';

-- 권한 부여 (DB 소유자 지정)
ALTER DATABASE findex OWNER TO findex_user;

-- 필요한 권한 추가 (테이블 생성, 쓰기/읽기)
GRANT ALL PRIVILEGES ON DATABASE findex TO findex_user;