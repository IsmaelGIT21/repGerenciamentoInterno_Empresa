CREATE TABLE `locais` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `ativo` BIT(1) DEFAULT NULL,
    `endereco` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `nome` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `tipo` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `fornecedores` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `ativo` BIT(1) NOT NULL,
    `nome` VARCHAR(150) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK63n2okh7dpmg7pjlwgdmfog70` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `usuarios` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `ativo` BIT(1) NOT NULL,
    `password` VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `username` VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKm2dvbwfge291euvmk6vkkocao` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `contas` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `data_pagamento` DATE DEFAULT NULL,
    `data_vencimento` DATE DEFAULT NULL,
    `descricao` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `observacoes` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `status` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `valor` DECIMAL(38,2) DEFAULT NULL,
    `local_id` BIGINT NOT NULL,
    `fornecedor_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_contas_locais` (`local_id`),
    CONSTRAINT `fk_contas_locais`
        FOREIGN KEY (`local_id`) REFERENCES `locais` (`id`)
        ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE `apolices` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `ano_modelo` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `comissao` DECIMAL(38,2) DEFAULT NULL,
    `corretor` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `data_inicio` DATE DEFAULT NULL,
    `data_vencimento` DATE DEFAULT NULL,
    `descricao` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `identificador` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `lmi` DECIMAL(38,2) DEFAULT NULL,
    `numero_apolice` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `parcelas` INT DEFAULT NULL,
    `seguradora` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `valor_franquia` DECIMAL(38,2) DEFAULT NULL,
    `valor_premio` DECIMAL(38,2) DEFAULT NULL,
    `local_id` BIGINT DEFAULT NULL,
    `valor_parcela` DECIMAL(38,2) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK9hyla0pqg0wgdtcnn70nrsuyg` (`local_id`),
    CONSTRAINT `FK9hyla0pqg0wgdtcnn70nrsuyg`
        FOREIGN KEY (`local_id`) REFERENCES `locais` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
