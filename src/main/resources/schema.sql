DROP TABLE IF EXISTS collegue;
  
CREATE TABLE collegue (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  matricule VARCHAR(250) NOT NULL,
  nom VARCHAR(250) NOT NULL,
  prenoms VARCHAR(250) NOT NULL,
  email VARCHAR(250),
  date_de_Naissance DATE,
  photo_url VARCHAR(250) 
);