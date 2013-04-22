/*L
   Copyright Ekagra Software Technologies Ltd.
   Copyright SAIC

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cacore-sdk/LICENSE.txt for details.
L*/

create SCHEMA @DB-NAME@ AUTHORIZATION DBA;

grant @DB-NAME@.* to @DB-USERNAME@@"%" identified by "@DB-PASSWORD@";