
INSERT INTO users (  user_email,user_password,user_firstname,user_lastname )
                       VALUES
                       ( "mohan@gmail.com","abcd","mohan","bala");

INSERT INTO users (  user_email,user_password,user_firstname,user_lastname )
                       VALUES
                       ( "milin@gmail.com","abcd","milin","joshi");

INSERT INTO users (  user_email,user_password,user_firstname,user_lastname )
                       VALUES
                       ( "Jaymin@gmail.com","abcd","jaymin","patel");

INSERT INTO users (  user_email,user_password,user_firstname,user_lastname )
                       VALUES
                       ( "febi@gmail.com","abcd","febi","kennedy");


INSERT INTO location (user_id,latitude,longitude)
                       VALUES
                       (1,"51.5034070","-0.1275920");

INSERT INTO location (user_id,latitude,longitude)
                       VALUES
                        (2,"22.7201320","71.6495360");

INSERT INTO location (user_id,latitude,longitude)
                       VALUES
                        (2,"23.0225050","72.5713620");

INSERT INTO friends (friends_id,user_email,friend_email, is_friend)
                       VALUES
                        (1,"milin@gmail.com","mohan@gmail.com","true");

INSERT INTO friends (friends_id,user_email,friend_email, is_friend)
                       VALUES
                        (2,"milin@gmail.com","Jaymin@gmail.com","true");

INSERT INTO friends (friends_id,user_email,friend_email, is_friend)
                       VALUES
                        (3,"Jaymin.com","mohan@gmail.com","true");
