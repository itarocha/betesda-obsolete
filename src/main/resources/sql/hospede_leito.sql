SELECT      new br.com.itarocha.betesda.model.HospedeLeitoVO(hospedagem, hospede, hospedeLeito, leito.id) 
FROM        HospedeLeito hospedeLeito
INNER JOIN  hospedeLeito.leito leito
INNER JOIN  hospedeLeito.hospede hospede
INNER JOIN  hospede.hospedagem hospedagem
WHERE       :DATA_INI BETWEEN hospedeLeito.dataEntrada AND hospedeLeito.dataSaida 
OR          :DATA_FIM BETWEEN hospedeLeito.dataEntrada AND hospedeLeito.dataSaida 
ORDER BY    leito.id

