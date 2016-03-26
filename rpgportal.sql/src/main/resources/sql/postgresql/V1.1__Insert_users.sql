INSERT INTO rpg_role (name) VALUES ('ADMIN'), ('USER');

DO $$
DECLARE userid int8;
BEGIN
  INSERT INTO rpg_user (username, password, failed_attempts, locked)
    VALUES ('user', '$2a$10$j4Zc5NtQWgMpptYhedIZ8uMzHlXkOunfoiWjAGVQ8XR1hfC6EF4iC', 0, FALSE)
  RETURNING id INTO userid;

  INSERT INTO rpg_user_roles (user_id, role) VALUES (userid, 'USER');
END$$;

DO $$
DECLARE userid int8;
BEGIN
  INSERT INTO rpg_user (username, password, failed_attempts, locked)
  VALUES ('admin', '$2a$10$Hk660c5QUho496VCXpunWuls1Y1ReXBDOPvikMQl0nEDM6snd7GOa', 0, FALSE)
  RETURNING id INTO userid;

  INSERT INTO rpg_user_roles (user_id, role) VALUES (userid, 'USER');
  INSERT INTO rpg_user_roles (user_id, role) VALUES (userid, 'ADMIN');
END$$;