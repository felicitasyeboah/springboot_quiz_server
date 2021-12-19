INSERT INTO PaF_GruppeB.user (user_name, password, is_ready)
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
        
INSERT INTO PaF_GruppeB.question (category_id, question_text, answer_correct, answer_wrong1, answer_wrong2, answer_wrong3)
	VALUES
		(4, 'Einen Feinschmecker nennt man auch?', 'Gourmet', 'Genießer', 'Gourmed', 'Leckermäulchen'),
        (1, 'Welches Metall leitet Wärme am besten?', 'Silber', 'Kupfer', 'Gold', 'Aluminium'),
        (5, 'Wie lange geht ein Marathon?', '42,195 Kilometer', '10 Kilometer', '25 Kilometer', '1000 Meter');
        
INSERT INTO PaF_GruppeB.played_games (user_id1, user_id2, user_score1, user_score2)
	VALUES
		(1, 2, 5, 2),
        (1, 3, 3, 2),
        (2, 3, 1, 4);
        
INSERT INTO PaF_GruppeB.highscore_global (highscore_id, game_id, user_id)
	VALUES
		(1, 1, 1),
        (2, 3, 3),
        (3, 2, 1);
