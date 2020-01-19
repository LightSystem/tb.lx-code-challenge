# tb.lx-code-challenge
Web Service to query Dublin bus GPS sample data

## Pre-requisites
- docker
- docker-compose
- sbt

## How to run
### Elastic Stack
The sample data resides in ElasticSearch.

Launch Elastic Stack (elasticsearch, logstash) with `docker-compose up`.

It might take a few minutes to start the stack and it will also import the sample data in `sample-data/siri.20121125.csv` to ElasticSearch.

Stop and erase data related to Elastic Stack with `docker-compose down -v`

### Web Server
http4s was the Web Server chosen to serve the requests. It listens on port `8080`.

Type `sbt run` in a terminal while in the project's root directory to start it.

## Example usage
### Get running Operators for a time frame
POST `/operators/list`

JSON Body:
```
{
  "startTime": "2012-11-25T06:00:00",
  "endTime": "2012-11-25T08:00:00"
}
```

### Get Vehicle IDs for an Operator and time frame
POST `/vehicles/list`

JSON Body:
```
{
  "startTime": "2012-11-25T03:00:00",
  "endTime": "2012-11-25T07:00:00",
  "operator": "SL"
}
```

### Get Vehicle IDs filtering if "at stop" for an Operator and time frame
Same endpoint as above. Filter in JSON accepts optional boolean filter:
```
{
  "startTime": "2012-11-25T00:00:00",
  "endTime": "2012-11-25T07:00:00",
  "operator": "CF",
  "atStop": true
}
```

### Get GPS trace for given Vehicle ID
POST `/vehicles/trace`
```
{
  "startTime": "2012-11-25T00:00:00",
  "endTime": "2012-11-25T07:00:00",
  "vehicleID": "33234"
}
```