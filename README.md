# tb.lx-code-challenge
Web Service to query Dublin bus GPS sample data

## Pre-requisites
- docker
- docker-compose

## How to run
Launch Elastic Stack `docker-compose up`.

It might take a few minutes to start the stack and it will also import the sample data in `sample-data/siri.20121125.csv` to ElasticSearch.

Stop and erase data related to Elastic Stack with `docker-compose down -v`

## Example usage
POST http://localhost:8080/operators/running

Body:
```
{
  "startTime": "2012-11-25T06:00:00",
  "endTime": "2012-11-25T08:00:00"
}
```