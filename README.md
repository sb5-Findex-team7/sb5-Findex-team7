# 7팀

## 팀원 구성
- 규환 (https://github.com/chagyuhwan)
- 규성 (https://github.com/gyural)
- 주환 (https://github.com/parkjoohwan)
- 준희 (https://github.com/Jelo777)

## 프로젝트 소개
💹 한눈에 보는 금융 지수 데이터!
Findex는 외부 Open API와 연동하여 금융 지수 데이터를 제공하는 대시보드 서비스입니다.
사용자는 직관적인 UI에서 금융 지수의 흐름을 파악하고, 자동 연동 기능을 통해 최신 데이터를 분석할 수 있습니다. 지수별 성과 분석, 이동평균선 계산, 자동 데이터 업데이트 기능을 통해 가볍고 강력한 금융 분석 도구
## 기술 스택
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Data JPA](https://img.shields.io/badge/Data%20JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-0088CC?style=for-the-badge&logo=databricks&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
## 파일구조
```markdown
├── 📂 batch
│    ├── SyncIndexDataBatchJob
├── 📂 config
│    ├── AppConfig.java    # 앱 config 처리
│    ├── QuerydslConfig
│
├── 📂 controller
│    ├── IndexInfoController.java
│    ├── IndexDataController.java
│    ├── SyncConfigController.java
│    ├── SyncJobController.java
│
├── 📂 domain
│    ├── 📂 entity
│    │    ├── 📂 base
│    │    │    ├── BaseEntity.java
│    │    │    ├── BaseUpdatableEntity.java
│    │    ├── IndexInfo
│    │    ├── IndexData
│    │    ├── SyncJob
│    ├── 📂 enums
│    │    ├── IndexDataSortDirection.java
│    │    ├── IndexDataSortField.java
│    │    ├── JobStatus.java
│    │    ├── JobType.java
│    │    ├── PeriodType.java
│    │    ├── SortedDirection.java
│    │    ├── SourceType.java
│    │    ├── SyncConfigSoredField.java
│    │    ├── SyncJobSortedField.java
│
├── 📂 dto
│    ├── 📂 command
│    │    ├── ExportCsvCommand.java
│    │    ├── GetNewIndexDataCommand.java
│    │    ├── GetSyncConfigCommand.java
│    │    ├── GetSyncJobCommand.java
│    │    ├── IndexDataDto.java
│    │    ├── IndexDataQueryCommand.java
│    │    ├── IndexInfoDto.java
│    │    ├── SyncJobDto.java
│    ├── 📂 request
│    │    ├── IndexDataCreateRequest.java
│    │    ├── IndexDataRequest.java
│    │    ├── IndexDataSyncRequest.java
│    │    ├── IndexDataUpdateRequest.java
│    │    ├── IndexInfoCreateRequest.java
│    │    ├── IndexInfoUpdateRequest.java
│    │    ├── LinkIndexDataRequest.java
│    │    ├── PatchSyncReqeust.java
│    │    ├── StockMarketIndexRequest.java
│    │    ├── SyncJobRequest.java
│    ├── 📂 response
│    │    ├── CursorPageResponseIndexDataDto.java
│    │    ├── CursorPageResponseSyncConfigResponse.java
│    │    ├── CursorPageResponseSyncJobResponse.java
│    │    ├── ErrorResponse.java
│    │    ├── IndexCharDto.java
│    │    ├── IndexDataDto.java
│    │    ├── IndexInfoSummaryDto.java
│    │    ├── IndexPerfomanceRankDto.java
│    │    ├── LinkIndexInfosResponse.java
│    │    ├── StockMarketIndexResponse.java
│    │    ├── SyncConfigResponse.java
│    │    ├── SyncJobResponse.java
│    │    ├── SyncResponse.java
│    ├── CursorPageResponseIndexInfoDto.java
│    ├── CursorPageResponseSyncConfigDto.java
│    ├── CursorPageResponseSyncJobDto.java
│    ├── GetNewIndexDataResult.java
│    ├── GetNewIndexInfosResult.java
│    ├── IndexDataScrollRequest.java
│    ├── LinkIndexInfosDto.java
│    ├── PaginatedResult.java
│    ├── PatchSyncConfigCommand.java
│    ├── SyncConfigDto.java
│    ├── SyncJobDto.java
│
├── 📂 exception
│    ├── 📂 handler
│    │    ├── GlobalExceptionHandler.java
│    ├── 📂 sync
│    │    ├── AlreadyUpdatedException.java
│    │    ├── NonNewDataException.java
│    │    ├── SyncException.java
│    ├── 📂 syncConfig
│    │    ├── NoIndexInfoException.java
│    │    ├── SyncConfigException.java
│
├── 📂 mapper
│    ├── 📂 SyncConfigMapper
│    │    ├── SyncConfigMapper.java
│    │    ├── SyncJobMapper.java
│    ├── IndexDataMapper.java
│
├── 📂 repository
│    ├── 📂 impl
│    │    ├── IndexDataRespositoryImpl.java
│    │    ├── IndexInfoQueryRepositoryImpl.java
│    │    ├── SyncConfigQueryRepositoryImpl.java
│    │    ├── SyncJobQueryRepositoryImpl.java
│    ├── 📂 indexData
│    │    ├── IndexDataRepositoryCustom.java
│    │    ├── IndexDataRepositoryCustomImpl.java
│    │    ├── IndexPerfomanceProjection.java
│    ├── IndexDataQueryRepository.java
│    ├── IndexDataRepository.java
│    ├── IndexInfoQueryRepository.java
│    ├── IndexInfoRepository.java
│    ├── SyncConfigQueryRepository.java
│    ├── SyncConfigRepository.java
│    ├── SncJobQueryRepository.java
│    ├── SyncJobRepository.java
│
├── 📂 service
│    ├── 📂 impl
│    │    ├── BatchindexDataServiceImpl.java
│    │    ├── BatchIndexInfoServiceImpl.java
│    │    ├── IndexDataServiceImpl.java
│    │    ├── IndexInfoServiceImpl.java
│    │    ├── LinkIndexInfoServiceImpl.java
│    │    ├── OpenApiServiceImpl.java
│    │    ├── SyncConfigServiceImpl.java
│    │    ├── SyncJobServiceImpl.java
│    ├── IndexDataService.java
│    ├── IndexInfoService.java
│    ├── LinkIndexDataDto.java
│    ├── LinkIndexInfoService.java
│    ├── OpenApiService.java
│    ├── SyncConfigService.java
│    ├── SyncIndexDataService.java
│    ├── SyncIndexInfoService.java
│    ├── SyncJobService.java
│ 
├── 📂 util
│    ├── OpenApiUtil.java
│    ├── StockDateUtil.java
│
├── FindexApplication.java
```
## 구현홈페이지
https://sb5-findex-team7-production.up.railway.app/#/dashboard
