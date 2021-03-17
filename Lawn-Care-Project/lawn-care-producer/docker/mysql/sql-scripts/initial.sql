CREATE TABLE IF NOT EXISTS `users` (
    `user_id` int NOT NULL AUTO_INCREMENT,
    `address` varchar(255) DEFAULT NULL,
    `authority_string` varchar(255) DEFAULT NULL,
    `email` varchar(255) DEFAULT NULL,
    `is_contractor` bit(1) NOT NULL,
    `password` varchar(255) DEFAULT NULL,
    `user_name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`user_id`)
);

CREATE TABLE IF NOT EXISTS `request` (
    `id` int NOT NULL AUTO_INCREMENT,
    `request_status` varchar(255) DEFAULT NULL,
    `time_scheduled` date DEFAULT NULL,
    `users_with_request_user_id` int DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK8ltub0la1tx9vk3pcnu3sv3q9` (`users_with_request_user_id`),
    CONSTRAINT `FK8ltub0la1tx9vk3pcnu3sv3q9` FOREIGN KEY (`users_with_request_user_id`) REFERENCES `users` (`user_id`)
);

-- INSERT INTO users (user_id, address, authority_string, email, is_contractor, password, user_name)
-- VALUES(1, '123 street rd.', 'CONTRACTOR', 'test@email.com', 1, '$2a$10$iPBCsSs6x8GNoHqr8arfie1wmKO7OCZXpb1EE.wmFDn9A651.FOeu', 'bob');