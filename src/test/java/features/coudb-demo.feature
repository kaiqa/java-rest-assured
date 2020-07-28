Feature: testing an API

  Background:
    Given a url of http://localhost:5984/

   Scenario: create a database
     Given path test/
     Then  I send a get request and the response contains error "not_found" and reason "Database does not exist."


  Scenario: create verify and delete data
     # create
     Given path words/
     And I send a post request and the "questionlanguage" is "English" and the "answerlanguage" is "Spanish" and the "question" is "which bus goes to the beach" and the "answer" is "qué autobús va a la playa"

     Then path words/_design/view1/_view/new-view
     And  I store the first reference ID I can find
     And  I store the first ID I can find
     # verify
     Given path words/
     Given the first entry ID exists

#     # become admin
     Given path _session
     And I log in as user:"admin" with password:"test"
     # delete
     Given path words/
     Then I delete the first entry


  Scenario:
    Given path words/
    Then I create entries with a cvs file




