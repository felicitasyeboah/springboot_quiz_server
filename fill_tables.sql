INSERT INTO PaF_GruppeB.user (user_name, password, is_online)
	VALUES
		('user1', 'password1', false),
        ('user2', 'password2', false),
        ('user3', 'password3', false);

INSERT INTO PaF_GruppeB.category (category_name) 
	VALUES
		('Wissenschaft'),
        ('Politik'),
        ('Kultur'),
        ('Essen & Trinken'),
        ('Sport');
        
INSERT INTO PaF_GruppeB.question (categoryid, question_text, answer_correct, answer_wrong1, answer_wrong2, answer_wrong3)
	VALUES
		(4, 'Einen Feinschmecker nennt man auch?', 'Gourmet', 'Genießer', 'Gourmed', 'Leckermäulchen'),
        (1, 'Welches Metall leitet Wärme am besten?', 'Silber', 'Kupfer', 'Gold', 'Aluminium'),
        (5, 'Wie lange geht ein Marathon?', '42,195 Kilometer', '10 Kilometer', '25 Kilometer', '1000 Meter');
        
INSERT INTO PaF_GruppeB.played_games (user1id, user2id, user1score, user2score)
	VALUES
		(1, 2, 5, 2),
        (1, 3, 3, 2),
        (2, 3, 1, 4);
        
INSERT INTO PaF_GruppeB.highscore_global (highscoreid, gameid, userid)
	VALUES
		(1, 1, 1),
        (2, 3, 3),
        (3, 2, 1);
