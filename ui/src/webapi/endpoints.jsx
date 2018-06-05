import { getToken } from '../services/AuthService';


const COMMON_FETCH_OPTIONS = {
  credentials: 'same-origin',
};
  
const COMMON_FETCH_HEADERS = new Headers({
'Accept': 'application/json',
});

const apiRoot = '/api';

export function getNewE14() {
  return get(apiRoot + '/newe14', {
    accept: "application/json"
  })
}

export function getCandidates() {
  return get(apiRoot + '/candidates', {
    accept: "application/json"
  })    
}

export function sendReport(data) {
  return post(apiRoot + '/saveReport', data)    
}

// returns Promise[object], where object comes from JSON of response
function get(url: string, searchParams?: { [string]: string }) {
  return rawAPIFetch(url, 'GET', { searchParams }).then(res => res);
}

export function post(url: string, content: RequestContent): Promise<number> {
  return rawAPIFetch(url, 'POST', JSON.stringify(content))
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
            Promise.reject("ErrOR")
    );
}


export function rawAPIFetch(url: string, method: fetch.MethodType, content: ?(string | Blob)): Promise<any> {
    let headers = new Headers(COMMON_FETCH_HEADERS);
    if (content !== undefined) {
      headers.set('Content-Type', 'application/json');
    }
    headers.set( 'Authorization', getToken());
  
    let request = new Request(url,
    {
    method,
    body: content,
    headers,
    });
  
    return fetch(request)
        .then(
            checkValidJson,
            // fetch() only rejects if the browser doesn't even try - perms or offline
            e => {
              const errorMsg = 'Cannot contact the server. You may be offline. Please check your internet connection';
              return Promise.reject(new Error(errorMsg));
            }
        );
  }