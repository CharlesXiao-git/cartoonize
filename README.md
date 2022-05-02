# Cartoonize Photo Webservice / API

# Requirements
 
  Candidates should create a simple Webservice / REST API that allows a frontend client to upload images to a library and view / download cartooned versions of images in their library.
  
  Test Requirements
  1. A user should be able to upload images which are stored in a library
  2. A user should be able to list any uploaded / stored images in the library
  3. A user should be able to download a "cartoonized" version of any image in the library (as well as the original image)
  4. Implementation details of how the "cartoonize" feature works are left open for the candidate to explore. Any appropriate library, service or other implementation can be used. You can keep this simple.
  
  Taking It To Production
  1. Provide a simple explanation of what would we need to do to get this service ready for production. This explanation might include references to:
  2. What cloud infrastructure would you use to host this application in a production environment. What changes would you make?
  3. How would you scale this API to handle hundreds of uploads per second?
  4. What steps would you take to secure the application and its data from attack?
  5. How would you add user accounts and auth to the system?
  6. How would you optimise handling a CPU and memory intensive "cartoonize" feature?
  7. How would you return extra metadata with an image?
  8. How would you handle expiring old images?

#  Solution

I would like to break the requirement into three modules. 
 1.User module will handle the security and account information.
 2.Image module will provide image upload and download feature.
 3.Cartoonize module will let use to cartoonize their images. 

# Skills used in this exercise

1. Spring Boot.       As Spring boot can quick start a web application.
2. Spring MVC         To develop the Restful API
3. Swagger            To test the API and demo the application
4. OAuth(JWT)         To protect the Restful API
5. Thumbnailator      To handle the images
6. H2database         To store the meta data of images and user information
3. Maven			  To manage the Jar files.
4. Docker		      To run the application 
5. JUnit			  To test the code and generate test report	

# Develop the application

##  First stage to complete user module

1. Input username and password to registry a account
2. The registry user use username and password to login to the application, and get a token
3. The registry user use the token to access other API interfaces
4. The registry user use token to list all users in the application

##  Second stage to complete image module

1. The registry user use token to upload image to file store
2. The registry user use token to list all images,that uploaded by himself 
3. The registry user use token and filename(from 2) to download image from file store
4. All registry user can only view images uploaded by himself
5. All registry user has a directory belong to himself
6. If an image has stored for more than 3 months, the image will be expired 

##  Third stage to complete cartoonize module

1. The registry user use token and filename to gray image and store in file store
2. The registry user use token and filename to border image and store in file store

##  Fourth stage to improve performance to support hundreds of uploads per second

1. Use threadpool to improve the performance
2. @Async is used in image service. So it is very fast to response to the user, and the thread 
   will async to upload file to file store and store image meta data into db.
   
##  Fifth stage to use Swagger-ui to demo the application

1. set a global token parameter in the head of the request
2. The swagger url need to be excluded by auth

# Development Planning

1. Using 4 hours on first stage
2. Using 2 hours on second stage
3. Using 4 hours on third stage
4. Using 4 hours on fourth stage
5. Using 2 hours on fifth stage

# Before run the application

- Install Maven and JDK11

# Run locally in JDk11 Env

1. go to CartoonizePhoto folder
2. run ' mvn clean package spring-boot:run '
3. open http://localhost:8080/swagger-ui.html

# Run locally in Docker

1. go to CartoonizePhoto folder
2. docker build -t cartoonize .
3. docker run -d -p 8080:8080 --name springboot_web cartoonize
4. open http://localhost:8080/swagger-ui.html

# Test the application

### 1.Using http://localhost:8080/swagger-ui.html to test the application
### 2.Click User API Interfaces.
####   2.1 registry an account 'tom'
       a. Click Post : /user/registry. 
       b. Click 'try it out' button
       c. Input password: 123 and username: tom
       d. Click Execute, and get 'tom created'
####   2.2 login and get token
       a. Click Get : /user/getToken
       b. Click 'try it out' button
       c. Input password: 123 and username: tom
       d. click Execute, and get token :'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b20iLCJpc3MiOiJhdXRoMCIsImV4cCI6MTY1MTQyNTM1N30.eAu-ecH10zR-wiSfH3aiSNPPRAxgiExB9Sf8JmJSR0s'
####   2.3 get all users
       a. Click Get : /user/listUser
       b. Click 'try it out' button
       c. Input token from 3,2
       d. Click Execute, and get result: {"users":[{"password":"123","userName":"tom"}]}
### 3.Click Image API Interfaces.
####  3.1 upload an image 'person.jpeg'
      a. Click Post : /image/upload
      b. Input token from step 3
      c. Select an image from your computer 'person.jpeg' 
      d. Click Execute, and get result: 'Upload file : person.jpeg'
####  3.2 list all images uploaded by user
      a. Click Get :  /image/listImages
      b. Input token from step 3 
      c. Click Execute, and get result: {"images":[{"imageName":"person.jpeg","createdBy":"tom     ","imagePath":"tom/person.jpeg"}]} 
#### 3.3 download image by imagename
      a. Click Get :  /image/downloadImages
      b. Input token from step 3
      c. Input image Name from 4,2 'person.jpeg'
      c. Click Execute,then click download to download image.
       and get result:
       content-disposition: attachment; filename="person.jpeg" 
       content-length: 134024 
       content-type: image/jpeg;charset=utf-8 
       date: Sun, 01 May 2022 07:58:09 GMT
### 4.Click Cartoonize API Interfaces.
####   4.1 graying image by imagename
      a. Click Get :  /cartoonize/grayingImage
      b. Input token from step 3
      c. Input image Name from 4,2 'person.jpeg'
      d. You can download the graying image 'grayImage_person.jpeg'
####   4.2 border image by imagename
      a. Click Get :  /cartoonize/borderImage
      b. Input token from step 3
      c. Input image Name from 4,2 'person.jpeg'
      d. You can download the graying image 'borderImage_person.jpeg'    
# Technical decision when develop the application

1. Using threadPool to improve performance
2. Using scheduled job to process the expired image
3. Using JWT to generate the token and verify the token
4. Handle exception in controller layer, and other layers throws all the exception out.
5. Using try with resource when write file
6. Using lombok on model layer
7. Using Functional Programming when process image 

# Extension in the future

1. Replace imageUtil with Micro service 

# Get Test Result

From testResult folder to get screenshot of the test result.



