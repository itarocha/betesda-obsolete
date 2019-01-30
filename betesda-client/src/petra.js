var moment = require('moment')
moment.locale('pt-br');

var base_uri =  'http://localhost:8088/api'
//var base_uri = 'http://petrasistemas.com.br:8080/betesda/api'

export default {

    base_uri,

    showMessageInfo(texto){
      var mensagem = {
        text : texto,
        type : 'info'
      }
      store.dispatch('showFlashMessage', mensagem)
    },

    showMessageWarning(texto){
      var mensagem = {
        text : texto,
        type : 'warning'
      }
      store.dispatch('showFlashMessage', mensagem)
    },

    showMessageError(texto){
      var mensagem = {
        text : texto,
        type : 'error'
      }
      store.dispatch('showFlashMessage', mensagem)
    },

    showMessageSuccess(texto){
      var mensagem = {
        text : texto,
        type : 'success'
      }
      store.dispatch('showFlashMessage', mensagem)
    },

    parseJwt(token) {
      var base64Url = token.split('.')[1]
      var base64 = base64Url.replace('-', '+').replace('_', '/')
      return JSON.parse(window.atob(base64))
    },

    //var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');

    resolveToken(){
      var token = localStorage.getItem('accessToken') || null
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`

      var decode = this.parseJwt(token)
      //console.log("==============> ", decode)
    },

    axiosGet(endpoint, showPostErrors){
      showPostErrors = showPostErrors != null ? showPostErrors : true
      this.resolveToken()
      return new Promise((resolve, reject) => {
          axios.get(`${base_uri}${endpoint}`)
          .then(response => {
            resolve(response)
          }).catch(error => {

            this.tratarRequestError(error, reject, showPostErrors)

          })
      })
    },

    axiosPost(endpoint, data, showPostErrors){
      showPostErrors = showPostErrors != null ? showPostErrors : true
      this.resolveToken()
      return new Promise((resolve, reject) => {
          axios.post(base_uri+endpoint, data)
          .then(response => {
            resolve(response)
          }).catch(error => {

            this.tratarRequestError(error, reject, showPostErrors)

          })
      })
    },

    axiosDelete(endpoint, showPostErrors){
      showPostErrors = showPostErrors != null ? showPostErrors : true
      this.resolveToken()
      return new Promise((resolve, reject) => {
          axios.delete(base_uri+endpoint)
          .then(response => {
            resolve(response)
          }).catch(error => {

            this.tratarRequestError(error, reject, showPostErrors)

          })
      })
    },


    tratarRequestError(error, reject, showPostErrors){
      if ((error.response) && (error.response.status)) {
        if ((error.response.status >= 400) && (error.response.status < 500) && showPostErrors){
          this.showMessageError("Erro: "+error.response.data.errors[0].errorMessage)
        } else if ((error.response.status == 500)){
          if (error.response.data.errors){
            this.showMessageError("Erro: "+error.response.data.message)
          } else {
            this.showMessageError("Erro: "+error.response.data)
          }
        }
      } else {
        this.showMessageError(error)
      }

      reject(error)
    },


    /*
    axios.get('https://appdividend.com', {
    headers: {
      Authorization: 'Bearer ' + token //the token is a variable which holds the token
    }
    });

    let config = {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'blob'
    };

    axios.post('https://appdividend.com', data, config)
        .then((response) => {
            console.log(response.data);
    });

    #Axios Response Object
    When the HTTP request is successful sent, than then() callback will receive a response object with the following properties:

    -data: the payload returned from the server. By default, Axios expects JSON and will parse this back into a JavaScript object for you.
    -status: the HTTP code returned from the server.
    -statusText: the HTTP status message returned by the server.
    -headers: all the headers sent back by the server.
    -config: the original request configuration.
    -request: the actual XMLHttpRequest object (when running in a browser).

    axios.defaults.baseURL = 'https://api.example.com';
    axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
    axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

*/

    tratarErros(error, debug){
        if (error.response) {
          if (debug) {
            //console.log(error.response.data.errors); // fieldName, errorMessage
            //console.log('RESPONSE.DATA: ');
            //console.log(error.response.data);
            //console.log('RESPONSE.STATUS: ');
            //console.log(error.response.status);
            //console.log('RESPONSE.HEADERS: ');
            //console.log(error.response.headers);
          }
          return error.response.data.errors;
        } else if (error.request) {
          if (debug){
            //console.log(error.request);
          }
        } else {
          if (debug){
            //console.log('Error', error.message);
          }
        }
          //console.log(error.config);
      },

      getErrors(fieldName, array_errors){
        const errors = []
        for (var e in array_errors){
          if (array_errors[e].fieldName == fieldName) {
            errors.push(array_errors[e].errorMessage);
          }
        }
        return errors
      },


      sem_acento(val, replaceBy) {
        replaceBy = replaceBy || '-';
        var mapaAcentosHex 	= { // by @marioluan and @lelotnk
          a : /[\xE0-\xE6]/g,
          A : /[\xC0-\xC6]/g,
          e : /[\xE8-\xEB]/g, // if you're gonna echo this
          E : /[\xC8-\xCB]/g, // JS code through PHP, do
          i : /[\xEC-\xEF]/g, // not forget to escape these
          I : /[\xCC-\xCF]/g, // backslashes (\), by repeating
          o : /[\xF2-\xF6]/g, // them (\\)
          O : /[\xD2-\xD6]/g,
          u : /[\xF9-\xFC]/g,
          U : /[\xD9-\xDC]/g,
          c : /\xE7/g,
          C : /\xC7/g,
          n : /\xF1/g,
          N : /\xD1/g,
        };
        
        for ( var letra in mapaAcentosHex ) {
          var expressaoRegular = mapaAcentosHex[letra];
          val = val.replace( expressaoRegular, letra );
        }
        
        val = val.toLowerCase();
        val = val.replace(/[^a-z0-9\-]/g, " ");
        
        val = val.replace(/ {2,}/g, " ");
          
        val = val.trim();    
        //val = val.replace(/\s/g, replaceBy);
        
        return val;
      },

      removerAcentos(s) {
        return s.normalize('NFD').replace(/[\u0300-\u036f]/g, "")
      },

      letrasENumeros(s) {
        return s.replace(/[^a-zA-Z0-9]/g,"")
      },


      randomColor(id) {
        //const r = () => Math.floor(256 * Math.random());
  
        //return this.colorCache[id] || (this.colorCache[id] = `rgb(${r()}, ${r()}, ${r()})`);
        return this.colorCache[id] || (this.colorCache[id] = this.randDarkColor())
      },    
  
      randDarkColor() {
          //var lum = -0.25;
          var lum = -0.2;
          var hex = String('#' + Math.random().toString(16).slice(2, 8).toUpperCase()).replace(/[^0-9a-f]/gi, '');
          if (hex.length < 6) {
              hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
          }
          var rgb = "#",
              c, i;
          for (i = 0; i < 3; i++) {
              c = parseInt(hex.substr(i * 2, 2), 16);
              c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
              rgb += ("00" + c).substr(c.length);
          }
          return rgb;
      },
  
      colorLuminance(hex, lum) {
        // validate hex string
        hex = String(hex).replace(/[^0-9a-f]/gi, '');
        if (hex.length < 6) {
          hex = hex[0]+hex[0]+hex[1]+hex[1]+hex[2]+hex[2];
        }
        lum = lum || 0;
  
        // convert to decimal and change luminosity
        var rgb = "#", c, i;
        for (i = 0; i < 3; i++) {
          c = parseInt(hex.substr(i*2,2), 16);
          c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
          rgb += ("00"+c).substr(c.length);
        }
  
        return rgb;
      },
  
}
      /*
      var retorno = ''
      if (hospedeLeitoId > 0){
        //console.log("buscando index = "+index)
        this.dados.hospedagens.forEach(function(hospedagem){
          if (hospedagem.id == hospedeLeitoId){
            hospedagem.dias.forEach(function(dia){
              if (dia.indice == index){
                //console.log("encontrou indice ",dia)
                if (dia.tipo == "inicio") {
                  retorno = 'grafico grafico_inicio'
                  return
                } else 
                if (dia.tipo == "durante") {
                  retorno = 'grafico grafico_durante'
                  return
                } else 
                if (dia.tipo == "fim") {
                  retorno = 'grafico grafico_fim'
                  return
                }
              }
            });

          }
        });
        return retorno
      } else {
        return ''
      }
      */
