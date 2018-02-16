# Framework to aggregate Twitter Stream metrics and show results in Grafana.

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

## Metrics definition

Metrics should have the following structure:

```
{
  "textFilters": {
    "#JsonPath":["#Content"]
  },
  "name":"#MetricName",
  "groupBy":["#JsonPath"],
  "ifHasNot": "#JsonPath",
  "ifHas": "#JsonPath"
}
```

Being `#JsonPath` a dot-separated path into the input json, and `#Content` a substring 
to be present in the content of the `#JsonPath` value. Fields in the metric definitions
correspond with:

- `textFilters`: Defines filters in the metric. If the filter passes, the message will be 
counted for the calculation. Text filters are case insensitive.

- `name`: Defines the name of the metric. That name will be translated to a measurement in 
InfluxDB, needed to query for the data later on in Grafana.

- `groupBy`: Defines a grouping in the metric. It corresponds with a tag in InfluxDB, that means
you could group by or filter by it in the defined query in Grafana.

- `ifHasNot`: Defines a filter in the message. If the json message contains the field, 
the filter passes.

- `ifHas`: Defines a filter in the message. If the json message does not contain the field, the
filter passes.

One example of a metric definition could be:

```
{
  "textFilters": {
    "text":["hello"]
  },
  "name":"containsHello",
  "groupBy":["user.lang"],
  "ifHasNot": "weirdField"
}
```

- This definition will look for messages that doesn't contain the field delete, and that has the 
word `hello` in the value of the field `text`. 
- The name of the resultant metric will be `containsHello`.
- The metric will ve divided depending on the value of their `user.lang`.
- Only messages that do not contain the key `delete` will be taken into account.

Example of the metric definition applied to messages:

Message 1:
```
{
  "text": "hola, ¿que tal?",
  "user": {
     "lang": "spanish"
   }
}

```

Message 2:
```
{
  "text": "hello, how are you?",
  "user": {
     "lang": "english"
   }
}

```

Message 3:
```
{
  "text": "hello again!",
  "user": {
     "lang": "spanish"
   },
   "weirdField": "I am a weird field"
}

```

Message 4:
```
{
  "text": "'Hello' ist ein englisches Wort",
  "user": {
     "lang": "german"
   }
}

```

Message 5:
```
{
  "delete": "Message deleted",
  "user": {
     "lang": "german"
   }
}

```

- Message 1 won't be considered, since the `textFilter` requires to have the word `hello` to
to be present in the `text` field.

- Message 2 will be considered since it has `hello` in the `text` field, and it doesn't have 
a field `weirdField` present in the message. The value of its tag `user.lang` will be `english`

- Message 3 won't be considered since it contains the field `weirdField`.

- Message 4 will be considered since it has `Hello` in the `text` field, and the filter is not
case sensitive. The value of the `user.lang` tag will be `german`

- Message 5 won't be considered since it doesn't have the field `text`, mandatory to apply the
text filter.
 