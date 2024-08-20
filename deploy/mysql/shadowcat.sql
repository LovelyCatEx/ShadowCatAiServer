-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `shadowcat`
--

CREATE DATABASE shadowcat;
USE shadowcat;

-- --------------------------------------------------------

--
-- 表的结构 `messages_0`
--

CREATE TABLE `messages_0` (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `session_id` varchar(64) NOT NULL,
  `assistant` tinyint(1) NOT NULL,
  `message_type` tinyint NOT NULL,
  `message` text NOT NULL,
  `datetime` bigint NOT NULL,
  `depends` bigint NOT NULL,
  `recalled` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 表的结构 `messages_1`
--

CREATE TABLE `messages_1` (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `session_id` varchar(64) NOT NULL,
  `assistant` tinyint(1) NOT NULL,
  `message_type` tinyint NOT NULL,
  `message` text NOT NULL,
  `datetime` bigint NOT NULL,
  `depends` bigint NOT NULL,
  `recalled` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 表的结构 `messages_2`
--

CREATE TABLE `messages_2` (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `session_id` varchar(64) NOT NULL,
  `assistant` tinyint(1) NOT NULL,
  `message_type` tinyint NOT NULL,
  `message` text NOT NULL,
  `datetime` bigint NOT NULL,
  `depends` bigint NOT NULL,
  `recalled` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 表的结构 `messages_3`
--

CREATE TABLE `messages_3` (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `session_id` varchar(64) NOT NULL,
  `assistant` tinyint(1) NOT NULL,
  `message_type` tinyint NOT NULL,
  `message` text NOT NULL,
  `datetime` bigint NOT NULL,
  `depends` bigint NOT NULL,
  `recalled` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 表的结构 `models`
--

CREATE TABLE `models` (
  `id` bigint NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) NOT NULL,
  `qualified_name` varchar(64) NOT NULL,
  `available` tinyint(1) NOT NULL DEFAULT '1',
  `support_image` tinyint(1) NOT NULL,
  `support_video` tinyint(1) NOT NULL,
  `support_audio` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 表的结构 `resources_0`
--

CREATE TABLE `resources_0` (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `extension` varchar(32) NOT NULL,
  `file_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `timestamp` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 表的结构 `resources_1`
--

CREATE TABLE `resources_1` (
  `id` bigint NOT NULL,
  `uid` bigint NOT NULL,
  `extension` varchar(32) NOT NULL,
  `file_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `timestamp` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 表的结构 `sessions_0`
--

CREATE TABLE `sessions_0` (
  `id` varchar(64) NOT NULL,
  `uid` bigint NOT NULL,
  `model_id` bigint NOT NULL,
  `name` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 表的结构 `sessions_1`
--

CREATE TABLE `sessions_1` (
  `id` varchar(64) NOT NULL,
  `uid` bigint NOT NULL,
  `model_id` bigint NOT NULL,
  `name` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- 表的结构 `users`
--

CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(128) NOT NULL,
  `nickname` varchar(32) NOT NULL,
  `phone_region` tinyint NOT NULL,
  `phone` varchar(32) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `registered_time` bigint NOT NULL,
  `avatar` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 转储表的索引
--

--
-- 表的索引 `messages_0`
--
ALTER TABLE `messages_0`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `messages_1`
--
ALTER TABLE `messages_1`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `messages_2`
--
ALTER TABLE `messages_2`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `messages_3`
--
ALTER TABLE `messages_3`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `models`
--
ALTER TABLE `models`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `qualified_name` (`qualified_name`);

--
-- 表的索引 `resources_0`
--
ALTER TABLE `resources_0`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `resources_1`
--
ALTER TABLE `resources_1`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `sessions_0`
--
ALTER TABLE `sessions_0`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `sessions_1`
--
ALTER TABLE `sessions_1`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `models`
--
ALTER TABLE `models`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
