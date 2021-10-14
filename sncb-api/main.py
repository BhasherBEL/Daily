import json
import uvicorn
from fastapi import FastAPI
import requests
import zipfile

app = FastAPI()

STIB_API_KEY = '1ad56dc9b90979f373b7055c4c863ada'


@app.get('/')
async def root():
    return {'Hello': 'World'}


@app.get('/sncb/{from_}/{to}/{time}/{type_}')
async def sncb(from_, to, time, type_):
    return requests.get('http://www.belgianrail.be/jp/sncb-nmbs-routeplanner/stboard.exe/fn?time=18:00&date=14.10.2021').json()


@app.get('/stib/{from_}/{to_}/{time}/{type_}')
async def stib(from_, to, time, type_):
    pass


@app.get('/stib/update_gtfs')
async def stib_update_gtfs():
    headers = {
        'Accept': 'application/zip',
        'Authorization': 'Bearer ' + STIB_API_KEY
    }

    req = requests.get('https://opendata-api.stib-mivb.be/Files/2.0/Gtfs', headers=headers)

    with open("files/stib_gtfs.zip", 'wb') as file:
        file.write(req.content)

    if zipfile.is_zipfile('files/stib_gtfs.zip'):
        zipfile.ZipFile('files/stib_gtfs.zip').extractall('files/stib_gtfs')

        return {
            'state': 1,
            'content': 'GTFS updated'
        }

    return {
        'state': 0,
        'reason': 'zipfile not found'
    }


@app.get('/stib/{stop_id}')
async def stib_next(stop_id):
    headers = {
        'Accept': 'application/json',
        'Authorization': 'Bearer ' + STIB_API_KEY
    }
    url = 'https://opendata-api.stib-mivb.be/OperationMonitoring/4.0/PassingTimeByPoint/' + stop_id

    try:
        return requests.get(url, headers=headers).json()
    except requests.exceptions.ConnectionError or json.decoder.JSONDecodeError as e:
        return {
            'state': 0,
            'reason': e.with_traceback()
        }

@app.get('/stib/stop/id-from-name/{name}')
async def stib_stop_ids_from_name(name):
    pass

if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=8000)
