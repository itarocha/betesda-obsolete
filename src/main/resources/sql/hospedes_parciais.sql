SELECT      new br.com.itarocha.betesda.model.HospedeHospedagemVO(hospede, hospedagem) 
FROM        Hospede hospede
INNER JOIN  hospede.hospedagem hospedagem
WHERE       ((hospedagem.dataEntrada BETWEEN :DATA_INI AND :DATA_FIM) OR (COALESCE(hospedagem.dataEfetivaSaida,hospedagem.dataPrevistaSaida) BETWEEN :DATA_INI AND :DATA_FIM))
OR          ((hospedagem.dataEntrada <= :DATA_INI) AND (COALESCE(hospedagem.dataEfetivaSaida,hospedagem.dataPrevistaSaida) >= :DATA_FIM))
ORDER BY    hospede.pessoa.nome
