CREATE TABLE `brand` (
            `id`	        BigInt	        NOT NULL    AUTO_INCREMENT,
            `name`	        varchar(20)	    NULL
        );

CREATE TABLE `products` (
            `id`	        BigInt	        NOT NULL    AUTO_INCREMENT,
            `category`	    varchar(20)	    NULL,
            `price`	        int	NULL,
            `brand_id`	    BigInt	        NOT NULL,
            `deleted_at`	timestamp	    NULL	    DEFAULT NULL
);

ALTER TABLE `brand` ADD CONSTRAINT `PK_BRAND` PRIMARY KEY (`id`);

ALTER TABLE `products` ADD CONSTRAINT `PK_PRODUCTS` PRIMARY KEY (`id`);

CREATE INDEX si_price ON `products` (`price`);

