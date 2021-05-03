
/*
CREATE TABLE IF NOT EXISTS `staging_table` (

	`statging_id` INT AUTO_INCREMENT  PRIMARY KEY,
	`file_id` INT NOT NULL,
	`file_name` VARCHAR(50) NOT NULL,  
	`message` VARCHAR(1000)
);


CREATE TABLE IF NOT EXISTS `transaction_table` (
	`transaction_id` INT AUTO_INCREMENT  PRIMARY KEY,
	`file_id` INT NOT NULL,
	`status` VARCHAR(100),
	`trade_id` VARCHAR(100),
	`instrument_id` VARCHAR(100),
	`book` VARCHAR(100),
	`counterparty` VARCHAR(100),
	`order_quantity` VARCHAR(100),
	`side` VARCHAR(100),
	`exec_quantity` VARCHAR(100),
	`order_price` VARCHAR(100),
	`trade_date` VARCHAR(100),
	`exec_price` VARCHAR(100)
);*/