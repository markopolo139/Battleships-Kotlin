DROP DATABASE IF EXISTS Battleships;

CREATE DATABASE Battleships;
USE Battleships

CREATE TABLE players (
  players_id int NOT NULL primary key auto_increment,
  username varchar(150) not null unique,
  `password` varchar(150) not null,
  email varchar(200) not null unique,
  game_token varchar(400) null
);

CREATE TABLE ships(
    ships_id int not null primary key auto_increment,
    players_id int not null,
    key (players_id),
    constraint ship_to_player foreign key (players_id) references players(players_id)
);

CREATE TABLE ship_elements(
    ships_id int not null,
    pos_x int not null,
    pos_y int not null,
    key(ships_id),
    constraint ship_element_to_ship foreign key (ships_id) references ships(ships_id)
);

CREATE TABLE shots (
    shots_id int not null auto_increment primary key,
    pos_x int not null,
    pos_y int not null,
    type ENUM('HIT', 'MISS') not null,
    players_id int not null,
    key (players_id),
    constraint shot_to_player foreign key (players_id) references players (players_id)
);

CREATE TABLE games(
    games_id int not null auto_increment primary key,
    player_a_id int not null,
    player_b_id int not null,
    current_player_id int not null,
    game_type ENUM('CLASSIC', 'RUSSIAN') not null,
    key (player_a_id),
    key (player_b_id),
    key (current_player_id),
    constraint player_a_to_players foreign key (player_a_id) references players(players_id),
    constraint player_b_to_players foreign key (player_b_id) references players(players_id),
    constraint current_player_to_players foreign key (current_player_id) references players(players_id)
);
