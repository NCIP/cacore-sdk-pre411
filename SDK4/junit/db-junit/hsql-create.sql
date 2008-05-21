create SCHEMA cacoresdk AUTHORIZATION DBA;
grant all privileges on cacoresdk.* to sa@"%" identified by "";
grant all privileges on cacoresdk.* to sa@localhost identified by "";

-- Extra user/host privilege added to account for Fedora Core default hostname quirk.
grant all privileges on cacoresdk.* to sa@localhost.localdomain identified by "";

-- You may have to explicitly define your hostname in order for things
-- to work correctly.  For example:
-- grant all privileges on cacoresdk.* to sa@host.domain.com identified by "";