var moment = require('moment')
moment.locale('pt-br');

export default {

    //base_uri: 'http://petrasistemas.com.br:8080/betesda/api',
    base_uri: 'http://localhost:8088/api',

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

