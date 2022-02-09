create database jukebox
use jukebox
create table users(
Username varchar(16) primary key,
Passwords varchar(16) not null,
Age int,
Gender varchar(5),
MobileNo varchar(10)
)
-----------------------------------------------------------------
create table song(
SongId char(7) primary key,
SongName varchar(200) not null,
Artist varchar(30) not null,
Genre varchar(30) not null,
Album varchar(30) not null,
Duration varchar(5) not null
)
------------------------------------------------------------------
create table userplaylist(
PlayListId char(7) primary key,
PlayListName varchar(80),
Username varchar(16)
)
Alter table userplaylist add constraint usernamefk foreign key (Username) references users(Username)
-------------------------------------------------------------------
create table podcast(
PodId char(7) primary key,
Celebrity varchar(20),
Genre varchar(20),
DateOfPodcast date
)
-------------------------------------------------------------------
create table playlist(
ListId int unsigned not null Auto_Increment,
primary key(ListId),
PlayListId char(7) not null,
SongId char(7),
PodId char(7)
)
Alter table playlist add constraint playlistidfk foreign key (PlayListId) references userplaylist(PlayListId)
Alter table playlist add constraint songidfk foreign key (SongId) references song(SongId)
Alter table playlist add constraint podidfk foreign key (PodId) references podcast(PodId)
-------------------------------------------------------------------
create table podcastsonglist(
Id int unsigned not null Auto_Increment,
primary key(Id),
PodId char(7),
SongId char(7)
)
Alter table podcastsonglist add constraint songidpodcastfk foreign key (SongId) references song(SongId)
Alter table podcastsonglist add constraint podcastidfk foreign key (PodId) references podcast(PodId)
-------------------------------------------------------------------
select * from users
select * from song
describe song
-------------------------------------------------------------------
Insert into users values('user','pass',18,'F','225262')
Insert into song values('SNG1001','Raatan Lambiyan','Jubin Nautiyal','Romantic','Shershaah','03:50')
Insert into song values('SNG1002','Jab Koi Baat Bigad Jaye','Kumar Sanu','Romantic','Jurm','08:00')
Insert into song values('SNG1003','Ranjha','Jasleen Royal','Sad','Shershaah','03:48')
Insert into song values('SNG1004','Pal Pal Dil Ke Paas','Kishore Kumar','Romantic','Blackmail','05:09')
Insert into song values('SNG1005','Bekarar Karke Hume Yu Na Jayiye','Hemand Kumar','Romantic','Bees Saal Baad','03:07')
Insert into song values('SNG1006','Kajra Mohobbat Wala','Asha Bhosle','Bollywood','Kismat','06:22')
Insert into song values('SNG1007','Laga Chunari Mein Daag','Manna Dey','Classic','Dil Hi To Hai','06:27')
Insert into song values('SNG1008','Na Yeh Chand Hoga','Hemant Kumar','Classic','Shart','03:12')
Insert into song values('SNG1009','Yeh Shaam Mastani','Kishore Kumar','Bollywood','Kati Patang','04:41')
Insert into song values('SNG1010','Meri Bheegi Bheegi Si','Kishore Kumar','Sad','Anamika','04:06')
Insert into song values('SNG1011','Mere Sapnon Ki Rani','Kishore Kumar','Bollywood','Aradhana','05:00')
Insert into song values('SNG1012','Aye Meri Zohra Jabeen','Manna Dey','Bollywood','Waqt','03:53')
Insert into song values('SNG1013','Tera Chehra Jab Nazar Aaye','Adnan Sami','Romantic','Tera Chehra','04:43')
Insert into song values('SNG1014','Yeh Sama Sama Hai Ye Pyaar Ka','Lata Mangeshkar','Romantic','Jab Jab Phool Khile','03:20')
Insert into song values('SNG1015','Raat Akeli Hai','Asha Bhonsle','Bollywood','Jewel Thief','05:17')
Insert into song values('SNG1016','Socha Hai','Farhan Akhtar','Rock','Rock On!','04:11')
Insert into song values('SNG1017','Nadaan Parinde','A.R. Rahman','Rock','Rockstar','06:26')
Insert into song values('SNG1018','Chudi','Falguni Pathak','Pop','Yaad Piya Ki','04:15')
Insert into song values('SNG1019','Maine Payal Hai Chhankai','Falguni Pathak','Pop','Yaad Piya Ki','04:39')
Insert into song values('SNG1020','Dil Diyan Gallan','Atif Aslam','Melody','Tiger Zinda Hai','04:20')
Insert into song values('SNG1021','Banjara','Mohammed Irfan','Melody','Ek Villain','05:36')
-------------------------------------------------------------------
Insert into podcast values('POD1001','Kishore Kumar','Bollywood','1971-01-29')
Insert into podcastsonglist (PodId,SongId) values ('POD1001','SNG1009'),('POD1001','SNG1011')
select * from podcastsonglist
Insert into podcast values('POD1002','Falguni Pathak','Pop','1998-03-12')
Insert into podcastsonglist (PodId,SongId) values ('POD1002','SNG1018'),('POD1002','SNG1019')
-------------------------------------------------------------------
Insert into userplaylist values('PLS1000',null,'j')
Insert into playlist (PlayListId,SongId,PodId) values('PLS1000',null,null)
SELECT * FROM PlayList
select * from userplaylist
select playlistid,playlistname from userplaylist where username='user1'
select * from song where songId in(select songId from PlayList  where PlaylistId  in (select playlistId from userplaylist where username='user1' AND playlistId='PLS1003'))
Select song.songId,song.songname,song.artist,song.genre,song.album,song.duration from song inner join playlist on song.songId=playlist.songId where (select playlistid from userplaylist where playlistname='S')
-------------------------------------------------------------------
select * from podcast where Genre='Pop'
select * from song where songId in(select songId from podcastsonglist where PodId='POD1001')
select SongId from podcastsonglist where PodId='POD1002'
