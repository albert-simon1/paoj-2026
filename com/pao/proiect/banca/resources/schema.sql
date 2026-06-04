DROP TABLE IF EXISTS tranzactii;
DROP TABLE IF EXISTS carduri;
DROP TABLE IF EXISTS conturi;
DROP TABLE IF EXISTS clienti;

CREATE TABLE clienti (
                         cnp VARCHAR(13) PRIMARY KEY,
                         nume VARCHAR(100) NOT NULL,
                         oras VARCHAR(50),
                         strada VARCHAR(100)
);

CREATE TABLE conturi (
                         iban VARCHAR(24) PRIMARY KEY,
                         cnp_client VARCHAR(13),
                         tip_cont VARCHAR(20) NOT NULL, -- 'CURENT' sau 'ECONOMII'
                         sold DECIMAL(15,2) DEFAULT 0.0,
                         dobanda DECIMAL(5,2) DEFAULT 0.0,
                         FOREIGN KEY (cnp_client) REFERENCES clienti(cnp) ON DELETE CASCADE
);

CREATE TABLE carduri (
                         numar VARCHAR(19) PRIMARY KEY,
                         iban_cont VARCHAR(24),
                         activ BOOLEAN DEFAULT TRUE,
                         FOREIGN KEY (iban_cont) REFERENCES conturi(iban) ON DELETE CASCADE
);

CREATE TABLE tranzactii (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            iban_cont VARCHAR(24),
                            suma DECIMAL(15,2) NOT NULL,
                            tip_tranzactie VARCHAR(20) NOT NULL, -- 'DEPUNERE', 'RETRAGERE', 'TRANSFER'
                            data_tranzactiei TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (iban_cont) REFERENCES conturi(iban) ON DELETE CASCADE
);