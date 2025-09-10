package com.codeit.team7.findex.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSyncJob is a Querydsl query type for SyncJob
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSyncJob extends EntityPathBase<SyncJob> {

    private static final long serialVersionUID = 1545826236L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSyncJob syncJob = new QSyncJob("syncJob");

    public final com.codeit.team7.findex.domain.entity.base.QBaseUpdatableEntity _super = new com.codeit.team7.findex.domain.entity.base.QBaseUpdatableEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QIndexInfo indexInfo;

    public final BooleanPath isCompleted = createBoolean("isCompleted");

    public final DateTimePath<java.time.Instant> jobTime = createDateTime("jobTime", java.time.Instant.class);

    public final StringPath jobType = createString("jobType");

    public final DatePath<java.time.LocalDate> targetDate = createDate("targetDate", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public final StringPath worker = createString("worker");

    public QSyncJob(String variable) {
        this(SyncJob.class, forVariable(variable), INITS);
    }

    public QSyncJob(Path<? extends SyncJob> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSyncJob(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSyncJob(PathMetadata metadata, PathInits inits) {
        this(SyncJob.class, metadata, inits);
    }

    public QSyncJob(Class<? extends SyncJob> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.indexInfo = inits.isInitialized("indexInfo") ? new QIndexInfo(forProperty("indexInfo")) : null;
    }

}

