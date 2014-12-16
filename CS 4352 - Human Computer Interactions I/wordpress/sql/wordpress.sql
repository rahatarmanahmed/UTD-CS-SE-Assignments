-- phpMyAdmin SQL Dump
-- version 3.3.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 19, 2014 at 12:07 AM
-- Server version: 5.1.46
-- PHP Version: 5.3.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `wordpress`
--

-- --------------------------------------------------------

--
-- Table structure for table `experiments`
--

DROP TABLE IF EXISTS `experiments`;
CREATE TABLE IF NOT EXISTS `experiments` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(160) COLLATE latin1_general_ci NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `DESCRIPTION` varchar(10000) COLLATE latin1_general_ci DEFAULT NULL,
  `REQUIREMENT_WARNING` varchar(10000) COLLATE latin1_general_ci DEFAULT NULL,
  `SIGNUP_TEST` varchar(256) COLLATE latin1_general_ci NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=9 ;

--
-- Dumping data for table `experiments`
--

INSERT INTO `experiments` (`ID`, `NAME`, `USER_ID`, `DESCRIPTION`, `REQUIREMENT_WARNING`, `SIGNUP_TEST`) VALUES
(1, 'Experiment Number One', 1, 'This experiment is definitely real and not fake. Signup for this experiment because it is real and happening.', 'You cannot wear sandals to this experiment.',''),
(2, 'Some Science Study', 1, 'This is an experiment where science-y stuff happens. Signup if you like science.', '','http://www.science.com'),
(3, 'Experiment Number Two', 1, 'This experiment is definitely real and not fake. Signup for this experiment because it is real and happening.', '',''),
(4, 'Experiment Number Three', 1, 'This experiment is definitely real and not fake. Signup for this experiment because it is real and happening.', '',''),
(5, 'Experiment Number Four', 1, 'This experiment is definitely real and not fake. Signup for this experiment because it is real and happening.', '',''),
(6, 'Experiment Number Five', 1, 'This experiment is definitely real and not fake. Signup for this experiment because it is real and happening.', '','');

-- --------------------------------------------------------

--
-- Table structure for table `timeslots`
--

DROP TABLE IF EXISTS `timeslots`;
CREATE TABLE IF NOT EXISTS `timeslots` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EXPERIMENT_ID` int(11) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `END_TIME` datetime NOT NULL,
  `MAX_PARTICIPANTS` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=12 ;

--
-- Dumping data for table `timeslots`
--

INSERT INTO `timeslots` (`ID`, `EXPERIMENT_ID`, `START_TIME`, `END_TIME`, `MAX_PARTICIPANTS`) VALUES
(1, 1, '2014-11-15 10:00:00', '2014-11-15 11:00:00', 1),
(2, 1, '2014-11-16 10:00:00', '2014-11-16 11:00:00', 1),
(3, 1, '2014-11-16 14:00:00', '2014-11-16 16:00:00', 2),
(4, 2, '2014-11-15 10:00:00', '2014-11-15 11:00:00', 1),
(5, 2, '2014-11-16 10:00:00', '2014-11-16 11:00:00', 1),
(6, 2, '2014-11-16 14:00:00', '2014-11-16 16:00:00', 2),
(7, 3, '2014-11-15 10:00:00', '2014-11-15 11:00:00', 1),
(8, 3, '2014-11-16 10:00:00', '2014-11-16 11:00:00', 1),
(9, 3, '2014-11-16 14:00:00', '2014-11-16 16:00:00', 2),
(10, 4, '2014-11-15 10:00:00', '2014-11-15 11:00:00', 1),
(11, 4, '2014-11-16 10:00:00', '2014-11-16 11:00:00', 1),
(12, 4, '2014-11-16 14:00:00', '2014-11-16 16:00:00', 2),
(13, 5, '2014-11-15 10:00:00', '2014-11-15 11:00:00', 1),
(14, 5, '2014-11-16 10:00:00', '2014-11-16 11:00:00', 1),
(15, 5, '2014-11-16 14:00:00', '2014-11-16 16:00:00', 2),
(16, 6, '2014-11-15 10:00:00', '2014-11-15 11:00:00', 1),
(17, 6, '2014-11-16 10:00:00', '2014-11-16 11:00:00', 1),
(18, 6, '2014-11-16 14:00:00', '2014-11-16 16:00:00', 2);



-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(64) COLLATE latin1_general_ci NOT NULL,
  `PASSWORD` varchar(128) COLLATE latin1_general_ci NOT NULL,
  `NAME` varchar(256) COLLATE latin1_general_ci NOT NULL,
  `IS_EXPERIMENTER` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=4 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID`, `USERNAME`, `PASSWORD`, `NAME`, `IS_EXPERIMENTER`) VALUES
(1, 'experimenter@utd.edu', 'password', 'Ryan McMahan', 1),
(2, 'participant@utd.edu', 'password', 'John Doe', 0);


-- --------------------------------------------------------

--
-- Table structure for table `signups`
--

DROP TABLE IF EXISTS `signups`;
CREATE TABLE IF NOT EXISTS `signups` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TIMESLOT_ID` int(11) NOT NULL,
  `PARTICIPANT_ID` int(11) NOT NULL,
  `COHORT_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `cohorts`
--

DROP TABLE IF EXISTS `cohorts`;
CREATE TABLE IF NOT EXISTS `cohorts` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EXPERIMENT_ID` int(11) NOT NULL,
  `NAME` varchar(256) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci AUTO_INCREMENT=1 ;

--
-- Dumping data for table `users`
--

INSERT INTO `cohorts` (`ID`, `EXPERIMENT_ID`, `NAME`) VALUES
(1, 1, 'A'),
(2, 1, 'B'),
(3, 2, 'A'),
(4, 2, 'B'),
(5, 3, 'A'),
(6, 3, 'B'),
(7, 4, 'A'),
(8, 4, 'B'),
(9, 5, 'A'),
(10, 5, 'B'),
(11, 6, 'A'),
(12, 6, 'B');