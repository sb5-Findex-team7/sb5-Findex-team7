# 7팀

## 팀원 구성
- 규환 (https://github.com/chagyuhwan)
- 규성 (https://github.com/gyural)
- 주환 (https://github.com/parkjoohwan)
- 준희 (https://github.com/Jelo777)

## 프로젝트 소개

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
