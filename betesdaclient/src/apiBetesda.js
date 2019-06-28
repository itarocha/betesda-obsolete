export default {

  getHospedagemInfo(hospedagemId, response, error) {
    var dados = {
      hospedagemId : hospedagemId
    }

    petra.axiosPost("/app/hospedagem/mapa/hospedagem_info", dados, false)
      .then(response).catch(error)
  },
  
}
