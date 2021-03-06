SELECT      new br.com.itarocha.betesda.model.HospedeLeitoVO('T', hospedagem, hospede, hospedeLeito) 
FROM        HospedeLeito hospedeLeito
INNER JOIN  hospedeLeito.leito leito
INNER JOIN  hospedeLeito.hospede hospede
INNER JOIN  hospede.hospedagem hospedagem
WHERE       ((hospedeLeito.dataEntrada BETWEEN :DATA_INI AND :DATA_FIM) OR (hospedeLeito.dataSaida BETWEEN :DATA_INI AND :DATA_FIM))
OR          ((hospedeLeito.dataEntrada <= :DATA_INI) AND (hospedeLeito.dataSaida >= :DATA_FIM))
ORDER BY    leito.id
