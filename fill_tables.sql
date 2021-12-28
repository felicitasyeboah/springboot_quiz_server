INSERT INTO PaF_GruppeB.user (user_name, password)
VALUES
    ('user1', 'password1'),
    ('user2', 'password2'),
    ('user3', 'password3');

INSERT INTO PaF_GruppeB.category (category_name)
VALUES
    ('Wissenschaft'),
    ('Politik'),
    ('Kultur'),
    ('Essen & Trinken'),
    ('Sport');

INSERT INTO PaF_GruppeB.question (category_id, question_text, answer_correct, answer_wrong1, answer_wrong2, answer_wrong3)
VALUES
    (1, 'Welches Metall leitet Wärme am besten?', 'Silber', 'Kupfer', 'Gold', 'Aluminium'),
    (1, 'Was ist H2O?', 'Wasser', 'Helium', 'Sauerstoff', 'Jod'),
    (1, 'Von wem stammt die Relativitätstheorie?', 'Albert Einstein', 'Marie Curie', 'Stephen Hawking', 'Nikola Tesla'),
    (1, 'Wie viel Meter legt man zurück, während man im Auto bei 120 km/h für drei Sekunden auf sein Handy schaut?', 'rund 100 Meter', 'ungefähr 15 Meter', 'etwa 50 Meter', 'knapp 30 Meter'),
    (2, 'Wer war Bundeskanzler vor Gerhard Schröder?', 'Helmut Kohl', 'Helmut Schmidt', 'Konrad Adenauer', 'Willy Brandt'),
    (2, 'Wie nennt man das Staatsoberhaupt von Deutschland?  ', 'Bundespräsident:in', 'Bundeskanzler:in', 'Bundesrat', 'Verteidigungsminister:in'),
    (2, 'Die richterliche Gewalt in Deutschland nennt man...', 'Judikative', 'Medien', 'Legislative', 'Exekutive'),
    (2, 'Welches Tier ist im deutschen Staatswappen zu sehen?', 'Adler', 'Bär', 'Bulle', 'Schlange'),
    (3, 'Wann lebte William Shakespeare?', 'im 16. bis 17. Jahrhundert', 'im 13. bis 14. Jahrhundert', 'im 18. Jahrhundert', 'im 17. bis 18. Jahrhundert'),
    (3, 'Wer oder was ist "Debussy"?', 'ein französischer Komponist des Impressionismus', 'eine Gitarre aus Kolumbien', 'ein Notenschlüssel aus Asien', 'ein Theaterstück von Roger Vontobel'),
    (3, 'Welcher Film ist nach Avatar (Cameron, 2009) der umsatzstärkste aller Zeiten?', 'Titanic (Cameron, 1997)', 'Jurassic Park (Spielberg, 1992)', 'Star Wars: Das Erwachen der Macht (Abrams, 2015)', 'Avengers: Infinity War (Russo & Russo, 2018)'),
    (3, 'Welcher der folgenden Künstler ist kein Vertreter des Impressionismus?', 'Andy Warhol', 'Claude Monet', 'Pierre-Auguste Renoir', 'Camille Pissarro'),
    (4, 'Einen Feinschmecker nennt man auch?', 'Gourmet', 'Genießer', 'Gourmed', 'Leckermäulchen'),
    (4, 'Aus welchem Land kommt der Gouda?', 'Niederlande', 'Ghana', 'Frankreich', 'Luxemburg'),
    (4, 'In welchem land wird am meisten Tee getrunken?', 'England', 'Türkei', 'Kuwait', 'Liechtenstein'),
    (4, 'Wie wachsen Erdnüsse?', 'in der Erde', 'an Bäumen', 'wie Erdbeeren am Boden', 'am Busch'),
    (5, 'Wie lange geht ein Marathon?', '42,195 Kilometer', '10 Kilometer', '25 Kilometer', '1000 Meter'),
    (5, 'Aus wie vielen Spieler:innen besteht eine Fußballmannschaft?', '11', '10', '12', '13'),
    (5, 'Wie oft findet eine Fußball-Weltmeisterschaft statt?', 'alle vier Jahre', 'alle zwei Jahre', 'jedes Jahr', 'alle fünf Jahre'),
    (5, 'Was macht der Platzwart von Berufs wegen?','ein paar Linien ziehen','an der Tube schnüffeln','sich einen zwitschern','ne Tüte drehen');

INSERT INTO PaF_GruppeB.played_game (time_stamp, user_id1, user_id2, user_score1, user_score2)
VALUES
    ('2021-12-28 11:10:00', 12, 3, 4000, 1450),
    ('2021-12-28 11:11:00', 1, 2, 3500, 740),
    ('2021-12-28 11:12:00', 1, 12, 840, 4000),
    ('2021-12-28 11:13:00', 12, 2, 4000, 1350),
    ('2021-12-28 11:14:00', 1, 3, 3400, 750),
    ('2021-12-28 11:15:00', 3, 12, 610, 3100),
    ('2021-12-28 11:16:00', 12, 2, 3100, 1650),
    ('2021-12-28 11:17:00', 3, 2, 3450, 770),
    ('2021-12-28 11:18:00', 2, 1, 680, 3400);