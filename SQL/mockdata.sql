DROP TABLE IF EXISTS PODCAST;
DROP TABLE IF EXISTS PROGRAMINFO;
DROP TABLE IF EXISTS DEFINITION;


CREATE TABLE `DEFINITION` (
  `DEFNR` int(11) NOT NULL,
  `SECTION` smallint(6) DEFAULT NULL,
  `FLAGS` smallint(6) DEFAULT NULL,
  `NAME` varchar(254) DEFAULT NULL,
  PRIMARY KEY (`DEFNR`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `PROGRAMINFO` (
  `program` int(11) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `description` text,
  `summary` text,
  `subtitle` text,
  `imglink` varchar(50) DEFAULT NULL,
  `category` varchar(60) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


CREATE TABLE `PODCAST` (
  `REFNR` int(11) NOT NULL,
  `CLASS` int(11) DEFAULT NULL,
  `TITLE` char(80) DEFAULT NULL,
  `FILENAME` char(254) DEFAULT NULL,
  `FILENAME2` char(254) DEFAULT NULL,
  `ORIGIN` char(40) DEFAULT NULL,
  `CREATOR` char(10) DEFAULT NULL,
  `CREATEDATE` int(11) DEFAULT NULL,
  `CREATETIME` int(11) DEFAULT NULL,
  `DURATION` int(11) DEFAULT NULL,
  `SOFTDEL` smallint(6) DEFAULT NULL,
  `FLAGS` char(20) DEFAULT NULL,
  `REPLFLAGS` smallint(6) DEFAULT NULL,
  `STATE` int(11) DEFAULT NULL,
  `PERSONAL` char(10) DEFAULT NULL,
  `POOL` char(10) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `IDENTIFIER` int(11) DEFAULT NULL,
  `REGISTRATION` char(40) DEFAULT NULL,
  `REPLIDENT` char(40) DEFAULT NULL,
  `MUSICID` char(20) DEFAULT NULL,
  `GEMAID` char(20) DEFAULT NULL,
  `INFORMAT` char(20) DEFAULT NULL,
  `LANGUAGE` int(11) DEFAULT NULL,
  `STORY` int(11) DEFAULT NULL,
  `GENERATION` smallint(6) DEFAULT NULL,
  `USECOUNT` int(11) DEFAULT NULL,
  `RESSORT` smallint(6) DEFAULT NULL,
  `SUBRESSORT` smallint(6) DEFAULT NULL,
  `PROGRAM` int(11) DEFAULT NULL,
  `BROADCAST` char(40) DEFAULT NULL,
  `BROADCASTDATE` int(11) DEFAULT NULL,
  `AUTHOR` char(254) DEFAULT NULL,
  `EDITOR` char(254) DEFAULT NULL,
  `PRODUCTION` int(11) DEFAULT NULL,
  `SOURCE` char(40) DEFAULT NULL,
  `KEYWORDS` char(100) DEFAULT NULL,
  `CHANGEUSER` char(10) DEFAULT NULL,
  `CHANGEDATE` int(11) DEFAULT NULL,
  `CHANGETIME` int(11) DEFAULT NULL,
  `REMARK` mediumtext,
  `FILESIZE` int(11) DEFAULT NULL,
  `AUDIOFORMAT` int(11) DEFAULT NULL,
  `AUDIOMODE` int(11) DEFAULT NULL,
  `SAMPLERATE` int(11) DEFAULT NULL,
  `BITRATE` int(11) DEFAULT NULL,
  `MAXLEVEL` int(11) DEFAULT NULL,
  `SENDRIGHTS` int(11) DEFAULT NULL,
  `BROADCASTINGS` int(11) DEFAULT NULL,
  `FIRSTUSEDATE` int(11) DEFAULT NULL,
  `LASTUSEDATE` int(11) DEFAULT NULL,
  `DELETEDATE` int(11) DEFAULT NULL,
  `RECORDDATE` int(11) DEFAULT NULL,
  `FADEIN` int(11) DEFAULT NULL,
  `FADEOUT` int(11) DEFAULT NULL,
  `MARKIN` int(11) DEFAULT NULL,
  `MARKOUT` int(11) DEFAULT NULL,
  `INTRO` int(11) DEFAULT NULL,
  `OUTRO` int(11) DEFAULT NULL,
  `FADING` int(11) DEFAULT NULL,
  `RAMP` smallint(6) DEFAULT NULL,
  `DONUT` smallint(6) DEFAULT NULL,
  `TAG` smallint(6) DEFAULT NULL,
  `PRESENTER` int(11) DEFAULT NULL,
  `SPEAKER` int(11) DEFAULT NULL,
  `PROJECT` int(11) DEFAULT NULL,
  `CATEGORY` int(11) DEFAULT NULL,
  `MUSICFORMAT` int(11) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `INTENSITY` int(11) DEFAULT NULL,
  `MOOD` int(11) DEFAULT NULL,
  `TEMPO` int(11) DEFAULT NULL,
  `STYLE` int(11) DEFAULT NULL,
  `ERA` int(11) DEFAULT NULL,
  `INSTRUMENTATION` int(11) DEFAULT NULL,
  `TARGET` int(11) DEFAULT NULL,
  `PLAYTIME` int(11) DEFAULT NULL,
  `SEASONAL` int(11) DEFAULT NULL,
  `WEEKDAY` smallint(6) DEFAULT NULL,
  `CARTPRIORITY` smallint(6) DEFAULT NULL,
  `ENDCODE` int(11) DEFAULT NULL,
  `CARRIER` int(11) DEFAULT NULL,
  `MOTIVE` char(40) DEFAULT NULL,
  `FOREIGNMOTIVE` char(40) DEFAULT NULL,
  `SPOTLENGTH` int(11) DEFAULT NULL,
  `CUSTOMER` int(11) DEFAULT NULL,
  `PRODUCT` int(11) DEFAULT NULL,
  `PRODUCTGROUP` int(11) DEFAULT NULL,
  `EXTDEVICE` int(11) DEFAULT NULL,
  `ARCHIVEDATE` int(11) DEFAULT NULL,
  `MEDIUMTYPE` int(11) DEFAULT NULL,
  `MEDIUMNAME` char(40) DEFAULT NULL,
  `MEDIUMCODE` int(11) DEFAULT NULL,
  PRIMARY KEY (`REFNR`),
  KEY `PODCAST_SOFTDEL` (`SOFTDEL`),
  KEY `PODCAST_CREATEDATE` (`CREATEDATE`),
  KEY `PODCAST_CREATETIME` (`CREATETIME`),
  KEY `PODCAST_TIT` (`TITLE`),
  KEY `PODCAST_TIM` (`CREATEDATE`,`CREATETIME`),
  KEY `PODCAST_AUT` (`AUTHOR`),
  KEY `PODCAST_EDI` (`EDITOR`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



INSERT INTO DEFINITION VALUES('74', '5', '0', 'Sivilisasjonen');
INSERT INTO DEFINITION VALUES('9', '5', '0', 'Hardcore');
INSERT INTO DEFINITION VALUES('90', '5', '0', 'Med Gandalf til galaksen');
INSERT INTO DEFINITION VALUES('54', '5', '0', 'Grenseland');

INSERT INTO PROGRAMINFO VALUES('9', 'Hardcore', 'Spillpodcast om konsoll- og PC-gaming med reportasjer, tilbakeblikk, anmeldelser og gjester fra spillindustrien hver tirsdag kl 11 p&#229; http://hardcore.srib.no', 'Spillpodcast om konsoll- og PC-gaming med reportasjer, tilbakeblikk, anmeldelser og gjester fra spillindustrien hver tirsdag.\r\nhttp://srib.no', 'Spillmagasinet til Studentradioen i Bergen', 'http://i.imgur.com/YGHKhKv.png', 'Games & Hobbies');

INSERT INTO PROGRAMINFO VALUES('90', 'Med Gandalf til galaksen', 'Med Gandalf til Galaksen handler om fantasy og science fiction i begrepets videste definisjon. \r\n\r\nVi &#248;nsker &#229; vise deg spennvidden innenfor disse to sjangerene, og vil p&#229; veien ta opp temaer som zombier, alver, romfart, realisme og magi. \r\n\r\nHver tirsdag fra 12 til 13 kan du h&#248;re oss p&#229; studentradioen i Bergen.', 'Med Gandalf til Galaksen handler om fantasy og science fiction i begrepets videste definisjon. \r\n', 'Med Gandalf til Galaksen handler om fantasy og science fiction i begrepets videste definisjon. \r\n', 'http://i.imgur.com/tGIJnpv.jpg', 'Society & Culture');

INSERT INTO PROGRAMINFO VALUES('54', 'Grenseland', 'Hvorfor skal du f&#248;ye deg etter samfunnets regler og forventninger? Er det ikke egentlig du selv som bestemmer hvordan du skal f&#248;re deg i hverdagen? I Grenseland blir samfunnets normer og forventninger revet opp ved roten, ja kanskje til og med latterliggjort.', 'humor bergen kleint sosialt stigma', 'Vi ses i Grenseland!', 'http://i.imgur.com/EnBaYgf.png', 'Comedy');

INSERT INTO PODCAST VALUES('3295', '1024', 'Hardcore - Podcast 07.01.2013 - RTh', 'w:\\SRRED13_A704B379AA29452AABD45256E2CDD288.MP3', '', 'Multitrack-Editor', 'RED', '20140106', '67011', '2822760', '0', '', '0', '3', '', '', '0', '0', '', '', '', '', '', '0', '0', '2', '21212121', '0', '0', '9', 'Hardcore', '0', 'RTh', 'RTh', '0', '', '', 'RED', '20140122', '60712', 'Rolf ønsker deg et godt nytt år og forteller deg hjva han syns om forrige semester, hva han håper med dette. Og hvordan han og Sebastian skal takle Tormods avskjed', '90328320', '8388608', '0', '48000', '256', '0', '0', '0', '0', '0', '0', '0', '21212121', '21212121', '0', '2822712', '21212121', '21212121', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'NONE', '', '21212121', '0', '0', '0', '0', '0', '0', '', '0');

INSERT INTO PODCAST VALUES('3285', '1024', 'Med Gandalf til Galaksen - Andre Medier - 07.01.2014', 'w:\\SRRED13_EDF6F7CCD5FE4F18A62C2C9DD9A8EBAC.MP3', '', 'Multitrack-Editor', 'RED', '20140106', '75300', '2341824', '0', '', '0', '3', '', '', '0', '0', '', '', '', '', '', '0', '0', '2', '21212121', '0', '0', '90', 'Med Gandalf til Galaksen', '20140107', 'Gandalf', 'RED', '0', '', '', 'RED', '20140211', '59647', 'Årets første sending! I denne episoden tar vi for oss når en ting går over til å bli noe annet, som f.eks. Firefly som ble kansellert og deretter fikk en konkluderende film.\r\n\r\nI studio: Cicilie, Stine og Ingrid', '74938368', '8388608', '0', '48000', '256', '0', '0', '0', '0', '0', '0', '0', '21212121', '21212121', '0', '2341776', '21212121', '21212121', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'NONE', '', '21212121', '0', '0', '0', '0', '0', '0', '', '0');

INSERT INTO PODCAST VALUES('3287', '1024', 'Grenseland - Fetisjer', 'w:\\SRRED13_6C9F96544B494F64B99AFDB9B2D36E95.MP3', '', 'Multitrack-Editor', 'RED', '20140107', '82399', '2298240', '0', 'RF', '0', '3', '', '', '0', '0', '', '', '', '', '', '0', '0', '2', '21212121', '0', '0', '54', 'Grenseland', '0', 'RED', 'RED', '0', '', '', 'RED', '20140108', '75140', 'Heter det fettisjj eller feeetisj? Hvilken kjendis tenner på armhuler? Hvilke unormale fetisjer er de mest normale? Hva gjør du om dama di spør om du kan bæsje på brystet hennes samtidig som du kommer in her face? Dette, og masse mer, får du svar på i ukens Grenseland.', '73543680', '8388608', '0', '48000', '256', '0', '0', '0', '0', '0', '0', '0', '21212121', '21212121', '0', '2298192', '21212121', '21212121', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'NONE', '', '21212121', '0', '0', '0', '0', '0', '0', '', '0');

INSERT INTO PODCAST VALUES('3292', '1024', 'Med Gandalf til Galaksen -  Kjærlighet- 14.01.14 -', 'w:\\SRRED18_67B08F3F3C2A4BD8A2F95DBE1FE93E48.MP3', '', 'Multitrack-Editor', 'RED', '20140113', '66426', '2165880', '0', '', '0', '3', '', '', '0', '0', '', '', '', '', '', '0', '0', '2', '21212121', '0', '0', '90', '', '0', 'Gandalf', 'RED', '0', '', '', 'RED', '20140211', '59584', 'Denne uken tar vi for oss kjærlighet, og om alle måtene dette kan gå galt. Og i science fiction og fantasy er det betydelig flere måter enn vanlig.\r\n\r\nI studio: Stine, Ingrid og Martin', '69308160', '8388608', '0', '48000', '256', '0', '0', '0', '0', '0', '0', '0', '21212121', '21212121', '0', '2165832', '21212121', '21212121', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'NONE', '', '21212121', '0', '0', '0', '0', '0', '0', '', '0');

INSERT INTO PODCAST VALUES('2907', '1024', 'Sivilisasjonen - Krig - Podcast', 'w:\\SRRED18_D53ECDB998C845DCAC100DBE8C057F28.MP3', '', 'Multitrack-Editor', 'RED', '20130528', '55538', '2061648', '0', '', '0', '3', '', '', '0', '0', '', '', '', '', '', '0', '0', '2', '21212121', '693', '697', '74', 'Sivilisasjonen', '0', 'MALD', 'MALD', '0', '', '', 'RED', '20130528', '55682', '', '65972736', '8388608', '0', '48000', '256', '0', '0', '0', '0', '0', '0', '0', '21212121', '21212121', '0', '2061600', '21212121', '21212121', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', 'NONE', '', '21212121', '0', '0', '0', '0', '0', '0', '', '0');
