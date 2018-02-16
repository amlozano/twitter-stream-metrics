## Framework to aggregate Twitter Stream metrics and show results in Grafana.

- Use build.sh to compile and generate docker images. 
You need docker, sbt and scala in the machine to build and generate the images.

- The command `docker-compose up` starts the system once it is compiled.
This step requires to have docker compose installed in the machine, apart from some steps:

    1. You need a Twitter account. You should configure a token following the guide:
https://developer.twitter.com/en/docs/basics/authentication/overview

    2. You should initialize the environment variables `TWITTER_CONSUMER_KEY`, `TWITTER_CONSUMER_SECRET`, 
    `TWITTER_TOKEN` and `TWITTER_TOKEN_SECRET` with the values taken from the step 1.
    
    3. You should initialize the environment variables `INFLUX_USER` y `INFLUX_USER_PASSWORD` 
    with the desired values. These will be the credentials to access the local InfluxDB.

    4. You should launch the command `docker-compose up`

- Metrics definitions should be located in the folder `metrics/`

- You can access Grafana going to http://localhost:3000/

