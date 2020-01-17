echo 'Initializing ElasticSearch index'
response=0

while [ $response != 200 ]; do
    response=$(curl -X PUT "elasticsearch:9200/dublin_bus" --write-out "%{http_code}" --silent --output /dev/null -H 'Content-Type: application/json' -d'
      {
        "mappings": {
          "properties": {
            "operator": { "type":  "keyword" }
          }
        }
      }
      ')
    sleep 4
done

echo 'ElasticSearch index initialized'