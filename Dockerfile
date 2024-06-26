FROM gradle:8.7.0-jdk21

WORKDIR /auto-backend-trainee-assignment

COPY /auto-backend-trainee-assignment .

RUN gradle installDist

CMD ["./build/install/auto-backend-trainee-assignment/bin/auto-backend-trainee-assignment", "--host=0.0.0.0", "--port=8080" ]

EXPOSE 8080
