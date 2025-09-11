-- 1) 부모 지수 1건 생성 (id는 IDENTITY 이므로 지정하지 않음)
WITH idx AS (
    INSERT INTO public.index_info (
                                   idx_csf, idx_nm, itms_cnt, bas_pntm, bas_idx,
                                   source_type, favorite, enabled, created_at, updated_at
        ) VALUES (
                     'KOSPI200', '코스피 200', 200, DATE '2020-01-02', 1000,
                     'MANUAL', FALSE, TRUE, now(), now()
                 )
        RETURNING id
)

-- 2) 자식 지수데이터 3건 생성 (위에서 생성된 id를 참조)
   , rows AS (
    SELECT (SELECT id FROM idx) AS idx_id, DATE '2025-09-08' AS bas_dt, 'USER'::varchar AS source_type,
           2560.12::numeric AS mkp, 2575.30::numeric AS clpr, 2587.00::numeric AS hipr, 2550.00::numeric AS lopr,
           15.18::numeric AS vs, 0.59::numeric AS flt_rt,
           1500000::bigint AS trqu, 3300000000::bigint AS tr_prc, 1200000000000::bigint AS lstg_mrkt_tot_amt
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-09', 'USER',
           2576.00, 2580.55, 2590.00, 2550.15,
           4.55, 0.18,
           1520000, 3400000000, 1205000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-10', 'USER',
           2581.00, 2570.10, 2595.20, 2560.10,
           -10.45, -0.40,
           1470000, 3200000000, 1210000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-09', 'OPEN_API',
           2581.00, 2570.10, 2595.20, 2560.10,
           -10.45, -0.40,
           1470000, 3200000000, 1210000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-11', 'USER',
           2570.50, 2582.30, 2592.10, 2565.00,
           11.80, 0.46,
           1530000, 3350000000, 1212000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-12', 'USER',
           2583.20, 2595.00, 2602.50, 2570.00,
           12.10, 0.47,
           1545000, 3420000000, 1215000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-13', 'OPEN_API',
           2590.00, 2587.50, 2600.00, 2575.50,
           -2.50, -0.10,
           1508000, 3300000000, 1218000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-14', 'USER',
           2587.30, 2592.20, 2601.30, 2579.80,
           4.90, 0.19,
           1552000, 3400000000, 1220000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-15', 'USER',
           2595.10, 2600.40, 2608.00, 2582.90,
           5.30, 0.20,
           1560000, 3450000000, 1222000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-16', 'OPEN_API',
           2600.50, 2595.20, 2607.00, 2588.10,
           -5.30, -0.20,
           1495000, 3280000000, 1224000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-17', 'USER',
           2593.80, 2602.70, 2610.00, 2589.50,
           8.90, 0.34,
           1580000, 3520000000, 1226000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-18', 'USER',
           2605.40, 2612.00, 2619.50, 2597.20,
           6.60, 0.25,
           1570000, 3490000000, 1229000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-19', 'USER',
           2610.20, 2608.50, 2620.00, 2599.50,
           -1.70, -0.06,
           1520000, 3320000000, 1232000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-20', 'OPEN_API',
           2607.00, 2615.80, 2625.50, 2600.20,
           8.80, 0.34,
           1590000, 3550000000, 1235000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-21', 'USER',
           2615.00, 2620.10, 2628.00, 2608.50,
           5.10, 0.19,
           1600000, 3600000000, 1238000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-22', 'USER',
           2621.50, 2625.00, 2630.00, 2610.00,
           3.50, 0.13,
           1610000, 3620000000, 1240000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-23', 'OPEN_API',
           2625.30, 2618.40, 2632.10, 2612.20,
           -6.90, -0.26,
           1515000, 3350000000, 1242000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-24', 'USER',
           2618.00, 2628.50, 2635.20, 2610.50,
           10.50, 0.40,
           1630000, 3680000000, 1245000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-25', 'USER',
           2629.00, 2635.20, 2642.00, 2615.00,
           6.20, 0.24,
           1645000, 3720000000, 1248000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-26', 'USER',
           2636.00, 2632.50, 2640.50, 2620.00,
           -3.50, -0.13,
           1550000, 3450000000, 1252000000000
    UNION ALL
    SELECT (SELECT id FROM idx), DATE '2025-09-27', 'OPEN_API',
           2632.10, 2640.20, 2648.50, 2625.50,
           8.10, 0.31,
           1650000, 3750000000, 1255000000000
)

INSERT INTO public.index_data (
    index_info_id, bas_dt, source_type,
    mkp, clpr, hipr, lopr, vs, flt_rt,
    trqu, tr_prc, lstg_mrkt_tot_amt,
    created_at, updated_at
)
SELECT idx_id, bas_dt, source_type,
       mkp, clpr, hipr, lopr, vs, flt_rt,
       trqu, tr_prc, lstg_mrkt_tot_amt,
       now(), now()
FROM rows;

SELECT * FROM index_data;
SELECT * FROM index_info;


ALTER TABLE  public.index_info  OWNER TO findex_user;
ALTER TABLE  public.index_data  OWNER TO findex_user;

DROP TABLE IF EXISTS index_data CASCADE;
DROP TABLE IF EXISTS index_info CASCADE;