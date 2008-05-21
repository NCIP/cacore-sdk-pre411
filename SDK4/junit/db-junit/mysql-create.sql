create database if not exists cacoresdk;
grant all privileges on cacoresdk.* to root@"%" identified by "";
grant all privileges on cacoresdk.* to root@localhost identified by "";

-- Extra user/host privilege added to account for Fedora Core default hostname quirk.
grant all privileges on cacoresdk.* to root@localhost.localdomain identified by "";

-- You may have to explicitly define your hostname in order for things
-- to work correctly.  For example:
-- grant all privileges on cacoresdk.* to root@host.domain.com identified by "";