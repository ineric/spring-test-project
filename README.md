# spring-test-project

Open application.properties
  spring.datasource.username=YOUR_DB_USER
  spring.datasource.password=YOUR_DB_PASS
  
For add comment use:
  method:     POST
  end-point:  "/api/comments"
  body:       {"comment":"Life is good!"}
  
For get comments use page number (start with 0. Max item in page = 10)
  method:     GET
  end-point:  "/api/comments/{pageNumber}
  
For get notifications use page number (start with 0. Max item in page = 10)
  method:     GET
  end-point:  "/api/comments/notifications/{pageNumber}
   
  
