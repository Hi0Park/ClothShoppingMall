package org.example.cloth_shopping_mall.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductsEntity is a Querydsl query type for ProductsEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductsEntity extends EntityPathBase<ProductsEntity> {

    private static final long serialVersionUID = 927470876L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductsEntity productsEntity = new QProductsEntity("productsEntity");

    public final QBrandEntity brandEntity;

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public QProductsEntity(String variable) {
        this(ProductsEntity.class, forVariable(variable), INITS);
    }

    public QProductsEntity(Path<? extends ProductsEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductsEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductsEntity(PathMetadata metadata, PathInits inits) {
        this(ProductsEntity.class, metadata, inits);
    }

    public QProductsEntity(Class<? extends ProductsEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.brandEntity = inits.isInitialized("brandEntity") ? new QBrandEntity(forProperty("brandEntity")) : null;
    }

}

