import { getToken } from '../services/AuthService';


// const COMMON_FETCH_OPTIONS = {
//   credentials: 'same-origin',
// };
  
const appPrefix = 'pv';
export const apiRoot = '/' + appPrefix + '/api';

export function getNewE14() {
  return get(apiRoot + '/newe14', {
    accept: "application/json"
  })
}

export function getUserInfo() {
  return get(apiRoot + '/userInfo', {
    accept: "application/json"
  })
}

export function getDepartamentos() {
  return get(apiRoot + '/departamentos', {
    accept: "application/json"
  })
}

export function getMunicipios() {
  return get(apiRoot + '/municipios', {
    accept: "application/json"
  })
}

export function getCandidates() {
  return get(apiRoot + '/candidates', {
    accept: "application/json"
  })    
}

export function getResumen() {
  return get(apiRoot + '/stats/resumen', {
    accept: "application/json"
  })
}

export function sendReport(data) {
  return post(apiRoot + '/saveReport', data)    
}

export function getPorcentajeTotal() {
  return get(apiRoot + '/stats/porcentaje', {
    accept: "application/json"
  })    
}

// returns Promise[object], where object comes from JSON of response
function get(url: string, searchParams?: { [string]: string }) {
  return rawAPIFetch(url, 'GET', { searchParams }).then(res => res);
}

export function post(url: string, body: RequestContent): Promise<number> {
  return rawAPIFetch(url, 'POST', JSON.stringify(body))
    .then(res => res);
}

function checkValidJson(fetchResponse): Promise<any> {
    if (!/^application\/json/.test(fetchResponse.headers.get('content-type'))) {
      // we expect even error responses to contain valid JSON
      return fetchResponse.text().then(
          body => Promise.reject('plain string')
      );
    }
  
    return fetchResponse.json().then(
        json => fetchResponse.ok ?
            Promise.resolve(json) :
            Promise.reject("ERROR")
    );
}


export function rawAPIFetch(url: string, method, content): Promise<any> {
    let headers = new Headers();
    if (content !== undefined) {
      headers.set('Content-Type', 'application/json');
    }
    headers.set( 'Authorization', getToken()); 
    let request;
    if(method === 'POST')
      request = new Request(url, {method, body: content, headers});
    if(method === 'GET')
      request = new Request(url, {method, content, headers});
  
    return fetch(request)
        .then(
            checkValidJson,
            // fetch() only rejects if the browser doesn't even try - perms or offline
            e => {
              const errorMsg = 'Cannot contact the server. You may be offline. Please check your internet connection' + e;
              return Promise.reject(new Error(errorMsg));
            }
        );
  }