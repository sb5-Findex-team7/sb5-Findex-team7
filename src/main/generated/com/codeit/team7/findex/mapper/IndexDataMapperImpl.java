package com.codeit.team7.findex.mapper;

import com.codeit.team7.findex.domain.entity.IndexData;
import com.codeit.team7.findex.domain.entity.IndexInfo;
import com.codeit.team7.findex.dto.command.IndexDataDto;
import com.codeit.team7.findex.dto.request.IndexDataCreateRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-10T12:36:18+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Eclipse Adoptium)"
)
@Component
public class IndexDataMapperImpl implements IndexDataMapper {

    @Override
    public IndexData toEntity(IndexDataCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        IndexData indexData = new IndexData();

        indexData.setBaseDate( request.getBaseDate() );
        indexData.setMarketPrice( request.getMarketPrice() );
        indexData.setClosingPrice( request.getClosingPrice() );
        indexData.setHighPrice( request.getHighPrice() );
        indexData.setLowPrice( request.getLowPrice() );
        indexData.setVersus( request.getVersus() );
        indexData.setFluctuationRate( request.getFluctuationRate() );
        indexData.setTradingQuantity( request.getTradingQuantity() );
        indexData.setTradingPrice( request.getTradingPrice() );
        indexData.setMarketTotalAmount( request.getMarketTotalAmount() );

        return indexData;
    }

    @Override
    public IndexDataDto toDto(IndexData entity) {
        if ( entity == null ) {
            return null;
        }

        IndexDataDto indexDataDto = new IndexDataDto();

        indexDataDto.setIndexInfoId( entityIndexInfoId( entity ) );
        indexDataDto.setId( entity.getId() );
        indexDataDto.setBaseDate( entity.getBaseDate() );
        indexDataDto.setSourceType( entity.getSourceType() );
        indexDataDto.setMarketPrice( entity.getMarketPrice() );
        indexDataDto.setClosingPrice( entity.getClosingPrice() );
        indexDataDto.setHighPrice( entity.getHighPrice() );
        indexDataDto.setLowPrice( entity.getLowPrice() );
        indexDataDto.setVersus( entity.getVersus() );
        indexDataDto.setFluctuationRate( entity.getFluctuationRate() );
        indexDataDto.setTradingQuantity( entity.getTradingQuantity() );
        indexDataDto.setTradingPrice( entity.getTradingPrice() );
        indexDataDto.setMarketTotalAmount( entity.getMarketTotalAmount() );
        indexDataDto.setCreatedAt( entity.getCreatedAt() );

        return indexDataDto;
    }

    private Long entityIndexInfoId(IndexData indexData) {
        if ( indexData == null ) {
            return null;
        }
        IndexInfo indexInfo = indexData.getIndexInfo();
        if ( indexInfo == null ) {
            return null;
        }
        Long id = indexInfo.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
