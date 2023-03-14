DROP SCHEMA IF EXISTS buddy;

CREATE SCHEMA buddy;

use buddy;

create table IF NOT EXISTS `users`
(
    `id`       bigint       not null auto_increment,
    `email`    varchar(255) not null,
    `enabled`  bit          not null,
    `name`     varchar(255),
    `password` varchar(255),
    `username` varchar(255),
    PRIMARY KEY (`id`)
);


INSERT INTO users (email, enabled, name, password, username)
VALUES ('cartman@yahoo.fr', true, 'cartman', '$2a$10$BKCfT2p8s.Mzv2zS03KOW.OwCPrwpsSyej8p1hGIES5dzSMbcKp8y', 'cartman');
INSERT INTO users (email, enabled, name, password, username)
VALUES ('laplume@yahoo.fr', true, 'laplume', '$2a$10$BKCfT2p8s.Mzv2zS03KOW.OwCPrwpsSyej8p1hGIES5dzSMbcKp8y', 'laplume');
INSERT INTO users (email, enabled, name, password, username)
VALUES ('ludivine@yahoo.fr', true, 'ludivine', '$2a$10$BKCfT2p8s.Mzv2zS03KOW.OwCPrwpsSyej8p1hGIES5dzSMbcKp8y',
        'ludivine');
INSERT INTO users (email, enabled, name, password, username)
VALUES ('arnold@yahoo.fr', true, 'arnold', '$2a$10$BKCfT2p8s.Mzv2zS03KOW.OwCPrwpsSyej8p1hGIES5dzSMbcKp8y', 'arnold');
INSERT INTO users (email, enabled, name, password, username)
VALUES ('patrick@yahoo.fr', true, 'patrick', '$2a$10$BKCfT2p8s.Mzv2zS03KOW.OwCPrwpsSyej8p1hGIES5dzSMbcKp8y', 'patrick');
INSERT INTO users (email, enabled, name, password, username)
VALUES ('jean@yahoo.fr', true, 'jean', '$2a$10$BKCfT2p8s.Mzv2zS03KOW.OwCPrwpsSyej8p1hGIES5dzSMbcKp8y', 'jean');
INSERT INTO users (email, enabled, name, password, username)
VALUES ('francois@yahoo.fr', true, 'francois', '$2a$10$BKCfT2p8s.Mzv2zS03KOW.OwCPrwpsSyej8p1hGIES5dzSMbcKp8y',
        'francois');
INSERT INTO users (email, enabled, name, password, username)
VALUES ('maxitella@yahoo.fr', true, 'Ricky', '$2a$10$BKCfT2p8s.Mzv2zS03KOW.OwCPrwpsSyej8p1hGIES5dzSMbcKp8y', 'eric');



create table IF NOT EXISTS `account`
(
    `id`         bigint           not null auto_increment,
    `balance`    double precision not null,
    `created_at` datetime,
    `user_id`    bigint,
    foreign key (`user_id`) references users (`id`) ON DELETE CASCADE,
    PRIMARY KEY (`id`)
);



INSERT INTO account (id, balance, created_at, user_id)
VALUES (1, 0, '2022-06-29 06:41:05', 1);
INSERT INTO account (id, balance, created_at, user_id)
VALUES (2, 133.75, '2022-06-29 06:41:07', 2);
INSERT INTO account (id, balance, created_at, user_id)
VALUES (3, 80, '2022-06-29 06:41:07', 3);
INSERT INTO account (id, balance, created_at, user_id)
VALUES (4, 0, '2022-06-29 06:41:08', 4);
INSERT INTO account (id, balance, created_at, user_id)
VALUES (5, 25, '2022-06-29 06:41:08', 5);
INSERT INTO account (id, balance, created_at, user_id)
VALUES (6, 57, '2022-06-29 06:41:08', 6);
INSERT INTO account (id, balance, created_at, user_id)
VALUES (7, 70, '2022-06-29 06:41:08', 7);
INSERT INTO account (id, balance, created_at, user_id)
VALUES (8, 4.25, '2022-06-29 06:41:08', 8);

create table IF NOT EXISTS `bank`
(
    `id`                bigint           not null auto_increment,
    `balance`           double precision not null,
    `bank_account_name` varchar(255),
    `description`       varchar(255),
    `iban`              varchar(255),
    `user_id`           bigint,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`) ON DELETE CASCADE
);

INSERT INTO bank (id, balance, bank_account_name, description, iban, user_id)
VALUES (1, 0, 'Boursorama', 'la banque du future', 'azerty', 2);
INSERT INTO bank (id, balance, bank_account_name, description, iban, user_id)
VALUES (2, 0, 'Credit agricole', 'la banque des louseurs', 'azerty', 3);


create table IF NOT EXISTS `connection`
(
    `id`          bigint not null auto_increment,
    `created_at`  datetime,
    `status`      varchar(255),
    `receiver_id` bigint,
    `sender_id`   bigint,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`receiver_id`) REFERENCES users (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`sender_id`) REFERENCES users (`id`) ON DELETE CASCADE
);

INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 6, 2);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 2, 3);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 2, 7);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 4, 1);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 5, 2);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 1, 8);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 2, 8);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 3, 8);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 4, 8);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 5, 8);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 6, 8);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 7, 8);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 2, 2);
INSERT INTO connection (created_at, status, receiver_id, sender_id)
VALUES ('2022-06-29 06:41:09', 'ACTIVE', 3, 3);

create table IF NOT EXISTS `contact`
(
    `id`         bigint not null auto_increment,
    `created_at` datetime,
    `email`      varchar(255),
    `firstname`  varchar(255),
    `lastname`   varchar(255),
    `message`    varchar(255),
    `subject`    varchar(255),
    PRIMARY KEY (`id`)
);

INSERT INTO contact (id, created_at, email, firstname, lastname, message, subject)
VALUES (1, '2022-06-30 20:14:01', 'maximus@yahoo.fr', 'maximus', 'maximus', 'j''aimerai me faire rembourser',
        'remboursement');
INSERT INTO contact (id, created_at, email, firstname, lastname, message, subject)
VALUES (2, '2022-06-30 10:14:01', 'aleonore@yahoo.fr', 'aleonore', 'aleonore', 'comment puis je creer un compte',
        'renseignement');
INSERT INTO contact (id, created_at, email, firstname, lastname, message, subject)
VALUES (3, '2022-06-30 12:14:01', 'valere@yahoo.fr', 'valere', 'valere', 'voulez vous supprimer mon compte svp',
        'suppression de compte');


create table IF NOT EXISTS `coupon`
(
    `id`         bigint           not null auto_increment,
    `expired_at` datetime,
    `price`      double precision not null,
    `status`     varchar(255),
    PRIMARY KEY (`id`)
);

INSERT INTO coupon (id, expired_at, price, status)
VALUES (1, '2029-12-01 20:14:01', 90, 'ACTIVE');
INSERT INTO coupon (id, expired_at, price, status)
VALUES (2, '2022-07-01 20:14:01', 90, 'INACTIVE');
INSERT INTO coupon (id, expired_at, price, status)
VALUES (3, '2022-04-01 20:14:01', 90, 'ACTIVE');
INSERT INTO coupon (id, expired_at, price, status)
VALUES (4, '2022-07-01 20:14:01', 90, 'ACTIVE');
INSERT INTO coupon (id, expired_at, price, status)
VALUES (5, '2022-07-01 20:14:01', 90, 'ACTIVE');


create table IF NOT EXISTS `role`
(
    `role_id` bigint not null,
    `name`    varchar(255),
    PRIMARY KEY (`role_id`)
);

INSERT INTO role (role_id, name)
VALUES (1, 'ROLE_USER');
INSERT INTO role (role_id, name)
VALUES (2, 'ROLE_ADMIN');

create table IF NOT EXISTS `transfer`
(
    `id`            bigint           not null auto_increment,
    `amount`        double precision not null,
    `commission`    double precision not null,
    `description`   varchar(255),
    `transfer_date` datetime,
    `type`          varchar(255),
    `connection_id` bigint,
    `credit_id`     bigint,
    `debtor_id`     bigint,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`credit_id`) REFERENCES users (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`debtor_id`) REFERENCES users (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`connection_id`) REFERENCES connection (`id`) ON DELETE CASCADE
);

INSERT INTO transfer (amount, commission, description, transfer_date, type, connection_id, credit_id, debtor_id)
VALUES (25, 1.25, 'Movies tickets', '2022-06-29 06:41:09', 'OPERATION', 5, 2, 5);
INSERT INTO transfer (amount, commission, description, transfer_date, type, connection_id, credit_id, debtor_id)
VALUES (60, 3, 'Money bank', '2022-06-29 06:41:09', 'OPERATION', 1, 6, 2);


create table IF NOT EXISTS `user_role`
(
    `user_role_id` bigint not null auto_increment,
    `role_id`      bigint null,
    `user_id`      bigint null,
    foreign key (role_id) references role (`role_id`),
    foreign key (user_id) references users (id) ON DELETE CASCADE,
    PRIMARY KEY (`user_role_id`)
);


INSERT INTO user_role (user_role_id, role_id, user_id)
VALUES (1, 1, 1);
INSERT INTO user_role (user_role_id, role_id, user_id)
VALUES (2, 1, 2);
INSERT INTO user_role (user_role_id, role_id, user_id)
VALUES (3, 1, 3);
INSERT INTO user_role (user_role_id, role_id, user_id)
VALUES (4, 1, 4);
INSERT INTO user_role (user_role_id, role_id, user_id)
VALUES (5, 1, 5);
INSERT INTO user_role (user_role_id, role_id, user_id)
VALUES (6, 1, 6);
INSERT INTO user_role (user_role_id, role_id, user_id)
VALUES (7, 1, 7);
INSERT INTO user_role (user_role_id, role_id, user_id)
VALUES (8, 2, 8);
